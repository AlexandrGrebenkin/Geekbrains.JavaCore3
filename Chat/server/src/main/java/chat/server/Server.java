package chat.server;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Server {
    private AuthManager authManager;
    private List<ClientHandler> clients;
    private final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("dd-MM-yy HH:mm:ss");
    private static final Logger LOGGER = LogManager.getLogger(Server.class);

    public AuthManager getAuthManager(){return authManager;}

    public Server(int port){
        clients = new ArrayList<>();
        authManager = new DataBaseAuthManager();
        authManager.start();
        try (ServerSocket serverSocket = new ServerSocket(port)){
            LOGGER.info("Server started");
            while(true){
                Socket socket = serverSocket.accept();
                LOGGER.info("Client connected");
                new ClientHandler(this, socket);
            }
        } catch (IOException e) {
            LOGGER.throwing(Level.ERROR, e);
        } finally {
            authManager.stop();
            LOGGER.info("Server stopped");
        }
    }

    public void broadcastMessage(String message, boolean withDateTime){
        if (withDateTime) {
            message = String.format("[%s] %s", LocalDateTime.now().format(DTF), message);
        }
        for (ClientHandler c : clients){
            c.sendMessage(message);
        }
    }

    public void broadcastMessage(String message){
        broadcastMessage(message, true);
    }

    public void broadcastClientsList() {
        StringBuilder stringBuilder = new StringBuilder("/clients_list ");
        for (ClientHandler c : clients){
            stringBuilder.append(c.getNickname()).append(" ");
        }
        String out = stringBuilder.toString().trim();
        broadcastMessage(out, false);
    }

    public void sendPrivateMessage(ClientHandler sender, String recipientNickname, String message){
        if (sender.getNickname().equals(recipientNickname)){
            sender.sendMessage("Cannot sent message to self.");
            return;
        }
        for (ClientHandler c : clients){
            if (c.getNickname().equals(recipientNickname)){
                c.sendMessage("Whispered from " + sender.getNickname() + ": " + message);
                sender.sendMessage("Whispered to " + recipientNickname + ": " + message);
                return;
            }
        }
        sender.sendMessage(recipientNickname + " is not online.");
    }

    public boolean isNickOnline(String nickname){
        for(ClientHandler c : clients){
            if(c.getNickname().equals(nickname)){
                return true;
            }
        }
        return false;
    }

    public void changeNickname (ClientHandler clientHandler, String newNickname){
        String oldNickname = clientHandler.getNickname();
        if(authManager.isNickBusy(newNickname)){
            clientHandler.sendMessage(newNickname+ " is busy.");
        } else {
            if(authManager.replaceNickname(oldNickname, newNickname)){
                clientHandler.setNickname(newNickname);
                clientHandler.sendMessage("/change_nick_OK " + newNickname);
                broadcastMessage("User " + oldNickname + " changed nickname to " + newNickname);
                broadcastClientsList();
            } else {
                clientHandler.sendMessage("Change nickname failed.");
            }
        }
    }

    public synchronized void subscribe(ClientHandler clientHandler){
        broadcastMessage(clientHandler.getNickname() + " logged in.");
        clients.add(clientHandler);
        broadcastClientsList();
    }

    public synchronized void unsubscribe(ClientHandler clientHandler){
        if (clients.contains(clientHandler)){
            broadcastMessage(clientHandler.getNickname() + " logged out.");
            clients.remove(clientHandler);
            broadcastClientsList();
        }
    }
}
