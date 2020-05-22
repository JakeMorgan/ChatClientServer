import java.io.*;
import java.net.Socket;
import java.util.Scanner;
public class Client {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 5555;
    private Socket clientSocket;
    private Scanner inMessage;
    private PrintWriter outMessage;
    private String clientName = "";

    public String getClientName() {
        return this.clientName;
    }
    public void setClientName(String name){
        this.clientName = name;
    }

    public Client(){
        try {
            clientSocket = new Socket(SERVER_HOST, SERVER_PORT);
            inMessage = new Scanner(clientSocket.getInputStream());
            outMessage = new PrintWriter(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Введите имя");
        Scanner consoleScanner = new Scanner(System.in);
        setClientName(consoleScanner.nextLine());

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        if (inMessage.hasNext()) {
                            String inMes = inMessage.nextLine();
                            System.out.println(inMes);
                        }
                    }
                } catch (Exception e) {

                }
            }
        }).start();

        while (true){
            String message = consoleScanner.nextLine();
            if(message.equalsIgnoreCase("Quit")){
                if (!clientName.isEmpty()) {
                    outMessage.println(clientName + " вышел");
                } else {
                    outMessage.println("Кто-то вышел не указав имя");
                }
                outMessage.println("##sessionend##");
                outMessage.flush();
                outMessage.close();
                inMessage.close();
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else
                SendMessage(message);
        }


    }

    private void SendMessage(String message){
        outMessage.println(getClientName()+ ": " + message);
        outMessage.flush();
    }


}
