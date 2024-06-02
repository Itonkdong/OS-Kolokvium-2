package Networking.OldKolokviumski.CovidEx;

import Networking.ExtendClasses.ATCPClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

public class TCPClientCovid extends ATCPClient
{
    public TCPClientCovid(InetAddress serverIP, int serverPort)
    {
        super(serverIP, serverPort);
    }

    @Override
    public void run()
    {
        Socket clientSocket = null;
        try
        {
            clientSocket = new Socket(this.serverIP, this.serverPort);
            this.setupReaderWriter(clientSocket);

            String serverMessage = this.reader.readLine();
            this.printServerResponse(serverMessage);

            this.writer.println(String.format("HELLO %d", clientSocket.getPort()));


            String serverAction = this.reader.readLine();
            this.printServerResponse(serverAction);

            Random random = new Random();
            int newCases = random.nextInt(40, 120);
            int hospitalized = random.nextInt(40, 120);
            int recovered = random.nextInt(40, 120);
            this.writer.println(String.format("%d,%d,%d",newCases, hospitalized, recovered ));

            String serverLastLine = this.reader.readLine();
            this.printServerResponse(serverLastLine);

            this.writer.println("QUIT");

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
            TCPClientCovid client = new TCPClientCovid(InetAddress.getLocalHost(), 8888);
            client.start();
        }

    }
}
