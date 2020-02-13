package chat.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;

public class Controller implements Initializable{
    @FXML
    TextArea textArea;

    @FXML
    TextField msgField, loginField;

    @FXML
    PasswordField passField;

    @FXML
    HBox loginBox;

    @FXML
    ListView<String> clientsList;

    private Network network;
    private boolean authenticated;
    private String nickname;

    public void setAuthenticated(boolean authenticated){
        this.authenticated = authenticated;
        loginBox.setVisible(!authenticated);
        loginBox.setManaged(!authenticated);
        msgField.setVisible(authenticated);
        msgField.setManaged(authenticated);
        clientsList.setVisible(authenticated);
        clientsList.setManaged(authenticated);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setAuthenticated(false);
        clientsList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                msgField.setText("/w " + clientsList.getSelectionModel().getSelectedItem() + " ");
                msgField.requestFocus();
                msgField.selectEnd();
            }
        });
    }

    public void tryToConnect(){
        try {
            setAuthenticated(false);
            network = new Network(8189);
            Thread thread = new Thread(() -> {
                try {
                    while (true){
                        String message = network.readMessage();
                        if(message.startsWith("/authok ")){
                            nickname = message.split(" ")[1];
                            textArea.appendText("You entered in chat as " + nickname + "\n");
                            setAuthenticated(true);
                            break;
                        }
                        textArea.appendText(message + "\n");
                    }
                    while (true){
                        String message = network.readMessage();
                        if (message.startsWith("/")){
                            if(message.equals("/end_confirm")){
                                textArea.appendText("Connection with server ended\n");
                                break;
                            }
                            if (message.startsWith("/clients_list ")) {
                                Platform.runLater(() -> {
                                    clientsList.getItems().clear();
                                    String[] tokens = message.split(" ");
                                    for (int i = 1; i < tokens.length; i++) {
                                        if(!nickname.equals(tokens[i])){
                                            clientsList.getItems().add(tokens[i]);
                                        }
                                    }
                                });
                            }
                            if (message.startsWith("/change_nick_OK ")){
                                nickname = message.split(" ")[1];
                            }
                        } else {
                            textArea.appendText(message + "\n");
                        }
                    }
                } catch (IOException e) {
                    Platform.runLater(()-> {
                        Alert alert = new Alert(
                                Alert.AlertType.WARNING,
                                "Connection with server lost",
                                ButtonType.CLOSE);
                        alert.showAndWait();
                    });
                } finally {
                    network.closeAll();
                    setAuthenticated(false);
                    nickname = null;
                }
            });
            thread.setDaemon(true);
            thread.start();
        } catch (IOException e) {
            Alert alert = new Alert(
                    Alert.AlertType.WARNING,
                    "Cannot connect to server",
                    ButtonType.CLOSE);
            alert.showAndWait();
        }
    }


    public void sendMessage(ActionEvent actionEvent) {
        try {
            network.sendMessage(msgField.getText());
            msgField.clear();
            msgField.requestFocus();
        } catch (IOException e){
            Alert alert = new Alert(
                    Alert.AlertType.WARNING,
                    "Can't sent message. Check network connection",
                    ButtonType.CLOSE);
            alert.showAndWait();
        }
    }

    public void tryToAuth(ActionEvent actionEvent) {
        try {
            tryToConnect();
            network.sendMessage("/auth " + loginField.getText() + " " + passField.getText());
            loginField.clear();
            passField.clear();
        } catch (IOException e){
            Alert alert = new Alert(
                    Alert.AlertType.WARNING,
                    "Can't sent message. Check network connection",
                    ButtonType.CLOSE);
            alert.showAndWait();
        }
    }
}
