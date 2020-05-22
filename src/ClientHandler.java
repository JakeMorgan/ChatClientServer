import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ClientHandler implements Runnable {

    private static final String HOST = "localhost";
    private static final int PORT = 5555;
    private Socket clientSocket = null;
    private static int clientsCount = 0;
    private Server server;
    private Scanner inMessage;
    private PrintWriter outMessage;

    public ClientHandler(Socket socket, Server server) {
        try {
            clientsCount++;
            this.server = server;
            this.clientSocket = socket;
            this.inMessage = new Scanner(socket.getInputStream());
            this.outMessage = new PrintWriter(socket.getOutputStream());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                server.sendMessageToAll("Вошёл новый клиент", null);
                server.sendMessageToAll("Клиентов в чате = " + clientsCount, null);
                break;
            }

            while (true) {
                if (inMessage.hasNext()) {
                    String clientMessage = inMessage.nextLine();
                    if (clientMessage.equalsIgnoreCase("##sessionend##")) {
                        break;
                    }
                    System.out.println(clientMessage);
                    server.sendMessageToAll(clientMessage, this);
                }
                Thread.sleep(100);
            }
        }
        catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        finally {
            this.close();
        }
    }

    public void sendMessage(String message) {
        try {
            outMessage.println(message);
            outMessage.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void close() {
        server.removeClient(this);
        clientsCount--;
        server.sendMessageToAll("Клиентов в чате = " + clientsCount, null);
    }
}