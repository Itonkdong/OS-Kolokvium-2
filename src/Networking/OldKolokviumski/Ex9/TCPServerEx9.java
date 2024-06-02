package Networking.OldKolokviumski.Ex9;

import Networking.ExtendClasses.ATCPServer;
import Networking.ExtendClasses.AWorkerThread;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class TCPServerEx9 extends ATCPServer
{
    public TCPServerEx9(int port)
    {
        super(port);
    }

    @Override
    public void startServer()
    {
        System.out.println("Starting the server...");
        try
        {
            ServerSocket serverSocket = new ServerSocket(this.port);
            while (true)
            {
                Socket clientSocket = serverSocket.accept();
                new WorkerThreadEx9(clientSocket, "D:\\OS Kolokvium Playground\\OS Kolokvium Exercises\\src\\Networking\\OldKolokviumski\\Ex9\\ServerData\\result.txt").start();


            }
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args)
    {
        TCPServerEx9 server = new TCPServerEx9(4498);
        server.startServer();
    }
}

class WorkerThreadEx9 extends AWorkerThread
{

    protected final String clientDataFilePath;
    public WorkerThreadEx9(Socket clientSocket, String clientDataFilePath)
    {
        super(clientSocket);
        this.clientDataFilePath = clientDataFilePath;
    }

    @Override
    public void run()
    {
        String clientFileInfo = null;
        try
        {
            clientFileInfo = this.reader.readLine();
            this.handleClientFileInfo(clientFileInfo);
            this.writer.println("200 OK");

            this.closeEverything(clientSocket);

        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }

    }

    public void handleClientFileInfo(String clientFileInfo)
    {
        synchronized (WorkerThreadEx9.class)
        {
            try
            {
                PrintWriter fileWriter = new PrintWriter(new FileWriter(this.clientDataFilePath, true), true);
                fileWriter.println(clientFileInfo);
                fileWriter.close();
            } catch (IOException e)
            {
                throw new RuntimeException(e);
            }




        }
    }
}
