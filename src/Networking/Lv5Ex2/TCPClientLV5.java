package Networking.Lv5Ex2;

import Networking.ExtendClasses.ATCPClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

public class TCPClientLV5 extends ATCPClient
{
    private final String message;
    public TCPClientLV5(InetAddress serverIP, int serverPort, String message)
    {
        super(serverIP, serverPort);
        this.message = message;
    }

    @Override
    public void run()
    {
        Random random = new Random();
        try
        {
            Socket clientSocket = new Socket(this.serverIP, this.serverPort);
            this.setupReaderWriter(clientSocket);

            this.writer.println(random.nextBoolean() ? "Login" : "Loggin");
//            this.writer.println(random.nextBoolean() ? "Login" : "Login");

            String serverLoginResponse = this.reader.readLine();
            if (serverLoginResponse == null)
            {
                System.out.println("Connection forcefully closed from the server");
                this.closeEverything(clientSocket);
                return;
            }

            this.printServerResponse(serverLoginResponse);

            this.writer.println(this.message);

            String serverResponse = this.reader.readLine();
            this.printServerResponse(serverResponse);

            boolean shouldLogOut = random.nextBoolean();
//            boolean shouldLogOut = true;
            if (!shouldLogOut)
            {
                this.closeEverything(clientSocket);
                return;
            }

            this.writer.println("Logout");

            serverResponse = this.reader.readLine();
            this.printServerResponse(serverResponse);

            this.closeEverything(clientSocket);

        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws UnknownHostException
    {
        for (int i = 0; i < 100; i++)
        {
            TCPClientLV5 tcpClientLV5 = new TCPClientLV5(InetAddress.getLocalHost(), 8080, "This is really exciting");
            tcpClientLV5.start();
        }

    }
}
