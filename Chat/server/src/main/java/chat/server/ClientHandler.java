package chat.server;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    private static final Logger LOGGER = LogManager.getLogger(ClientHandler.class);

    private Server server;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private String nickname;

    private boolean authFlag = true;
    private boolean chatFlag = true;

    public ClientHandler(Server server, Socket socket) throws IOException{
        this.server = server;
        this.socket = socket;
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());
        new Thread(() ->{
            try{
                while (authFlag){
                    authenticatingStage();
                }
                while (chatFlag){
                    chattingStage();
                }
            } catch (IOException e){
                LOGGER.throwing(Level.ERROR, e);
            } finally {
                close();
            }
        }).start();
    }

    private void authenticatingStage() throws IOException {
        String message = in.readUTF();
        LOGGER.info("Message from Client: " + message);
        if(message.startsWith("/auth ")){
            String[] tokens = message.split(" ", 3);
            String nickFromAuthManager = server.getAuthManager().getNicknameByLoginAndPassword(tokens[1], tokens[2]);
            if(nickFromAuthManager != null){
                if(server.isNickOnline(nickFromAuthManager)){
                    sendMessage("User is already logged in.");
                    return;
                }
                nickname = nickFromAuthManager;
                sendMessage("/authok " + nickname);
                server.subscribe(this);
                authFlag = false;
            } else {
                sendMessage("Login or password is incorrect");
            }
        }
    }

    private void chattingStage() throws IOException {
        String message = in.readUTF();
        LOGGER.info("Message from Client: " + message);
        if (message.startsWith("/")){
            if (message.startsWith("/w ")){
                String[] tokens = message.split(" ", 3);
                server.sendPrivateMessage(this, tokens[1], tokens[2]);
                return;
            }
            if (message.startsWith("/change_nick ")){
                String[] tokens = message.split(" ", 2);
                server.changeNickname(this, tokens[1]);
            }
            if (message.equals("/end")){
                sendMessage("/end_confirm");
                chatFlag = false;
            }
        } else {
            server.broadcastMessage(nickname + ": " + message);
        }
    }

    public void sendMessage(String message){
        try{
            out.writeUTF(message);
        } catch (IOException e){
            LOGGER.throwing(Level.ERROR, e);
        }
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void close(){
        server.unsubscribe(this);
        nickname = null;
        tryClose(in);
        tryClose(out);
        tryClose(socket);
    }

    private void tryClose(Closeable closeable){
        try{
            closeable.close();
        } catch (IOException e){
            LOGGER.throwing(Level.ERROR, e);
        }
    }
}
