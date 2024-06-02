package Networking.Ex4;

import Networking.ExtendClasses.ATCPClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.stream.Collectors;

public class MoreComplexTCPClient extends ATCPClient
{
    public MoreComplexTCPClient(InetAddress serverIP, int serverPort)
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

            this.writer.println("GET / HTTP/1.1");
            this.writer.println("User:JavaClient");
            this.writer.println("Accept:text/txt");
            this.writer.println();
            clientSocket.shutdownOutput();

            List<String> serverResponse = this.reader.lines().collect(Collectors.toList());
            serverResponse.stream().forEach(line-> System.out.println("Server Response: " + line));

            this.closeEverything(clientSocket);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws UnknownHostException
    {

//        MoreComplexTCPClient moreComplexTCPClient = new MoreComplexTCPClient(InetAddress.getLocalHost(), 7070);
//        moreComplexTCPClient.start();
        for (int i = 0; i < 100; i++)
        {
            MoreComplexTCPClient moreComplexTCPClient = new MoreComplexTCPClient(InetAddress.getLocalHost(), 7070);
            moreComplexTCPClient.start();
        }
    }
}
