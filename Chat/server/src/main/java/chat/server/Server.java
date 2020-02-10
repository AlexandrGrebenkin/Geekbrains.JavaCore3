package chat.server;

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

    public AuthManager getAuthManager(){return authManager;}

    public Server(int port){
        clients = new ArrayList<>();
        authManager = new DataBaseAuthManager();
        try (ServerSocket serverSocket = new ServerSocket(port)){
            System.out.println("Server started.");
            while(true){
                Socket socket = serverSocket.accept();
                System.out.println("Client connected.");
                new ClientHandler(this, socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
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

    public boolean isNickBusy(String nickname){
        for (String nick : authManager.getNicknameList()){
            if(nick.equals(nickname)){
                return true;
            }
        }
        return false;
    }

    public void changeNickname (ClientHandler clientHandler, String newNickname){
        String oldNickname = clientHandler.getNickname();
        if(isNickBusy(newNickname)){
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
