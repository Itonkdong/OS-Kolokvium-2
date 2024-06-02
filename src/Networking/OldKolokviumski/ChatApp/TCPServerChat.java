package Networking.OldKolokviumski.ChatApp;

import Networking.ExtendClasses.ATCPServer;
import Networking.ExtendClasses.AWorkerThread;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class TCPServerChat extends ATCPServer
{

    private final Map<String, Socket> clientSockets;

    public TCPServerChat(int port)
    {
        super(port);
        this.clientSockets = new HashMap<>();
    }

    public synchronized void addClientSocket(String index, Socket clientSocket)
    {
        this.clientSockets.put(index, clientSocket);
    }

    public synchronized boolean doesExist(String index)
    {
        return this.clientSockets.containsKey(index);
    }

    public synchronized Socket getClientSocket(String index)
    {
        return this.clientSockets.get(index);
    }

    @Override
    public void startServer()
    {
        System.out.println("Staring the server...");
        try
        {
            ServerSocket serverSocket = new ServerSocket(this.port);
            while (true)
            {
                Socket clientSocket = serverSocket.accept();
                new WorkerThreadChat(clientSocket, this).start();

            }
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args)
    {
        TCPServerChat server = new TCPServerChat(9753);
        server.startServer();
    }
}

class WorkerThreadChat extends AWorkerThread
{

    private final TCPServerChat server;
    public WorkerThreadChat(Socket clientSocket, TCPServerChat server)
    {
        super(clientSocket);
        this.server = server;
    }

    @Override
    public void run()
    {
        String authMessage = null;
        try
        {
            authMessage = this.reader.readLine();
            String[] parts = authMessage.split(":");
            String index = parts[1];
            String authAction = parts[0];
            System.out.println("Auth action " + authAction);
            if (!authAction.equals("login"))
            {
                this.writer.println("404 Bad Auth Action");
                this.closeEverything(clientSocket);
                return;
            }
            System.out.println("here");
            this.writer.println("200 OK");

            String helloMessage = this.reader.readLine();
            parts = helloMessage.split(":");
            String hello = parts[0];
            this.writer.println("You can chat now!");

            this.server.addClientSocket(index, clientSocket);

            while (true)
            {
                String clientMessage = this.reader.readLine();

                if(clientMessage.equals("DONE")) break;

                String[] messageParts = clientMessage.split(":");
                String toIndex = messageParts[0];


                boolean doesExist = this.server.doesExist(toIndex);
                if (!doesExist)
                {
                    this.writer.println("404,User Not Logged In");
                    continue;
                }

                if (messageParts.length != 2)
                {
                    this.writer.println("404,Badly formatted message");
                    continue;
                }
                String toUserMessage = messageParts[1];

                if (toIndex.equals(index))
                {
                    this.writer.println(String.format("300,%s", toUserMessage));
                }
                else {
                    Socket toUserSocket = this.server.getClientSocket(toIndex);
                    PrintWriter outWriter = new PrintWriter(new OutputStreamWriter(toUserSocket.getOutputStream()),true);
                    this.writer.println("200, Successfully send message");
                    outWriter.println(toUserMessage);
                }


            }

            this.closeEverything(clientSocket);


        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }

    }
}



