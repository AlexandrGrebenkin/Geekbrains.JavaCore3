package chat.client;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Network {
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    public Network (int port) throws IOException {
        this("localhost", port);
    }

    public Network(String address, int port) throws IOException{
        socket = new Socket(address, port);
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
    }

    public boolean isConnected(){
        return socket != null && !socket.isClosed();
    }

    public void sendMessage(String message) throws IOException {
        out.writeUTF(message);
    }

    public String readMessage() throws IOException {
        return in.readUTF();
    }

    public void closeAll(){
        tryClose(in);
        tryClose(out);
        tryClose(socket);
    }

    private void tryClose(Closeable closeable){
        try{
            if(closeable != null){
                closeable.close();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
