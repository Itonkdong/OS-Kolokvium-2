package Networking.Ex1;

import Networking.ExtendClasses.ATCPServer;
import Networking.ExtendClasses.AWorkerThread;
import Networking.ExtendClasses.Request;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLOutput;
import java.util.stream.Collectors;

public class SimpleTCPServer extends ATCPServer
{
    public SimpleTCPServer(int port)
    {
        super(port);
    }

    @Override
    public void startServer()
    {
        System.out.println("STARTING THE SERVER...");
        try
        {
            ServerSocket serverSocket = new ServerSocket(this.port);
            System.out.println("SERVER STARTED!");
            while (true)
            {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New Client Accepted");
                new SimpleTCPWorkerThread(clientSocket).start();
            }

        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args)
    {
        SimpleTCPServer simpleTCPServer = new SimpleTCPServer(8000);
        simpleTCPServer.startServer();
    }
}

class SimpleTCPWorkerThread extends AWorkerThread
{

    public SimpleTCPWorkerThread(Socket clientSocket)
    {
        super(clientSocket);
    }

    @Override
    public void run()
    {
        String requestString = this.reader.lines().collect(Collectors.joining("\n"));
        Request request = Request.of(requestString);
        writer.printf("200 OK %s\n", request.getHttpVersion());
        writer.printf("Hello %s\n", request.getHeader("User"));
        writer.printf("You requested: %s\n", request.getUri());
        try
        {
            this.closeEverything(clientSocket);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }

    }
}
