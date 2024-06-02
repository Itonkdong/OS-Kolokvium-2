package Networking.Ex1;

import Networking.ExtendClasses.ATCPClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

public class SimpleTCPClient extends ATCPClient
{

    public SimpleTCPClient(InetAddress serverIP, int serverPort)
    {
        super(serverIP, serverPort);
    }

    @Override
    public void run()
    {
        try
        {
            Socket clientSocket = new Socket(this.serverIP, this.serverPort);
            this.setupReaderWriter(clientSocket);
            Random random = new Random();
            String method = random.nextBoolean() ? "GET" : "POST";


            this.writer.println(String.format("%s /movies/%d HTTP/1.1", method, random.nextInt()));
            this.writer.println("User:Chrome");
            this.writer.println();

            clientSocket.shutdownOutput();

            reader.lines().forEach(line -> System.out.printf("Server send: %s\n", line));

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
            SimpleTCPClient simpleTCPClient = new SimpleTCPClient(InetAddress.getLocalHost(), 8000);
            simpleTCPClient.start();
        }

    }
}
