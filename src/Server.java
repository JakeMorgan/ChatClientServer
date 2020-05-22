import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    static final int PORT = 5555;

    private ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>();

    public Server() {
        Socket clientSocket = null;
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Сервер запущен");

            while (true) {
                clientSocket = serverSocket.accept();
                ClientHandler client = new ClientHandler(clientSocket, this);
                clients.add(client);
                new Thread(client).start();
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        finally {
            try {
                clientSocket.close();
                System.out.println("Сервер остановлен");
                serverSocket.close();
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void removeClient(ClientHandler client) {
        clients.remove(client);
    }

    public void sendMessageToAll(String message, ClientHandler cl) {
        if(cl == null){
            for (ClientHandler o : clients) {
                o.sendMessage(message);
            }
        }else
        {
            for (ClientHandler o : clients) {
                if(!o.equals(cl))
                    o.sendMessage(message);
            }
        }

    }

}
