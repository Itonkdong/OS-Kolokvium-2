package Networking.ExtendClasses;

import java.io.*;
import java.net.Socket;

public abstract class AWorkerThread extends Thread
{
    protected final Socket clientSocket;
    protected   final BufferedReader reader;
    protected   final PrintWriter writer;


    public AWorkerThread(Socket clientSocket)
    {
        this.clientSocket = clientSocket;
        try
        {
            this.reader = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
            this.writer = new PrintWriter(new OutputStreamWriter(this.clientSocket.getOutputStream()), true);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    protected void closeEverything(Socket clientSocket) throws IOException
    {
        clientSocket.close();
        this.writer.flush();
        this.writer.close();
        this.reader.close();
    }

    protected void printClientRequest(String message)
    {
        System.out.println("Client request: " + message);
    }
}
