package Networking.OldKolokviumski.Ex6;

import Networking.ExtendClasses.ATCPServer;
import Networking.ExtendClasses.AWorkerThread;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.stream.Collectors;

public class TCPServerEx6 extends ATCPServer
{
    public TCPServerEx6(int port)
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
                new WorkerThreadEx6(clientSocket, "D:\\OS Kolokvium Playground\\OS Kolokvium Exercises\\src\\Networking\\OldKolokviumski\\Ex6\\ServerData\\clients_data.txt").start();


            }
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args)
    {
        TCPServerEx6 server = new TCPServerEx6(3398);
        server.startServer();
    }
}

class WorkerThreadEx6 extends AWorkerThread
{

    protected final String clientDataFilePath;
    public WorkerThreadEx6(Socket clientSocket, String clientDataFilePath)
    {
        super(clientSocket);
        this.clientDataFilePath = clientDataFilePath;
    }

    @Override
    public void run()
    {
        List<String> clientFileInfo = this.reader.lines().collect(Collectors.toList());
        this.handleClientFileInfo(clientFileInfo);

        this.writer.println("FILE INFO SUCCESSFULLY RECEIVED");

        try
        {
            this.closeEverything(this.clientSocket);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }

    }

    public void handleClientFileInfo(List<String> clientFileInfo)
    {
        synchronized (WorkerThreadEx6.class)
        {
            try
            {
                PrintWriter fileWriter = new PrintWriter(new FileWriter(this.clientDataFilePath, true), true);
                clientFileInfo.stream()
                        .forEach(fileInfo->fileWriter.println(fileInfo));

                fileWriter.close();
            } catch (IOException e)
            {
                throw new RuntimeException(e);
            }

        }
    }
}
