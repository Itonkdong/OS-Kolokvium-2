package Networking.ExtendClasses;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public abstract class ATCPClient extends Thread
{
    protected final InetAddress serverIP;
    protected final int serverPort;
    protected BufferedReader reader;
    protected PrintWriter writer;

    public ATCPClient(InetAddress serverIP, int serverPort)
    {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
    }

    protected void setupReaderWriter(Socket clientSocket) throws IOException
    {
        this.reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.writer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);
    }

    protected void closeEverything(Socket clientSocket) throws IOException
    {
        this.writer.flush();
        this.writer.close();
        this.reader.close();
        clientSocket.close();
    }

    protected void printServerResponse(String message)
    {
        System.out.println("Server response: " + message);
    }
}
