package Networking.Lv5Ex2;

import Networking.ExtendClasses.ATCPServer;
import Networking.ExtendClasses.AWorkerThread;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServerLV5 extends ATCPServer
{

    public TCPServerLV5(int port)
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
                new WorkerThreadLV5(clientSocket, "D:\\OS Kolokvium Playground\\OS Kolokvium Exercises\\src\\Networking\\Lv5Ex2\\count.txt").start();

            }
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args)
    {
        TCPServerLV5 tcpServerLV5 = new TCPServerLV5(8080);
        tcpServerLV5.startServer();
    }
}

class WorkerThreadLV5 extends AWorkerThread
{
    private final String countFilePath;
    public WorkerThreadLV5(Socket clientSocket, String countFilePath)
    {
        super(clientSocket);
        this.countFilePath = countFilePath;
    }

    @Override
    public void run()
    {
        int localCounter = 0;
        try
        {
            String clientAuthAction = this.reader.readLine();
            localCounter += 1;
            this.printClientRequest(clientAuthAction);

            if (!clientAuthAction.equals("Login"))
            {
                System.out.println("Invalid Auth Action...Closing Connection!");
                this.handleMessageCount(localCounter);
                this.closeEverything(clientSocket);
                return;
            }

            this.writer.println("Logged in");

            String clientMessage = this.reader.readLine();
            localCounter+=1;
            this.printClientRequest(clientMessage);


            this.writer.println(clientMessage);

            clientAuthAction = this.reader.readLine();
            if (clientAuthAction == null)
            {
                this.printClientRequest("Stay logged in");
                this.closeEverything(clientSocket);
                this.handleMessageCount(localCounter);
                return;
            }
            this.printClientRequest(clientAuthAction);
            localCounter+=1;

            this.writer.println("Successfully logged out!");
            this.handleMessageCount(localCounter);

            this.closeEverything(clientSocket);


        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }


    }

    public void handleMessageCount(int localCount)
    {
        synchronized (WorkerThreadLV5.class)
        {
            try
            {
                RandomAccessFile accessFile = new RandomAccessFile(this.countFilePath, "rw");
                String line = accessFile.readLine();
                int count = 0;
                if (line != null)
                {
                    count = Integer.parseInt(line);
                }
                count += localCount;
                accessFile.seek(0);
                accessFile.writeBytes(String.valueOf(count));
                accessFile.close();

            } catch (FileNotFoundException e)
            {
                throw new RuntimeException(e);
            } catch (IOException e)
            {
                throw new RuntimeException(e);
            }

        }
    }
}
