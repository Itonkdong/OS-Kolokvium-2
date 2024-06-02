package Networking.Ex4;

import Networking.ExtendClasses.ATCPServer;
import Networking.ExtendClasses.AWorkerThread;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class LogServer extends ATCPServer
{
    public LogServer(int port)
    {
        super(port);
    }

    @Override
    public void startServer()
    {
        System.out.println("Starting the Logs Server");

        try
        {
            ServerSocket serverSocket = new ServerSocket(this.port);
            while (true)
            {
                Socket clientSocket = serverSocket.accept();
                new LogWorkerThread(clientSocket, "D:\\OS Kolokvium Playground\\OS Kolokvium Exercises\\src\\Networking\\Ex4\\log.txt",
                        "D:\\OS Kolokvium Playground\\OS Kolokvium Exercises\\src\\Networking\\Ex4\\count.txt"
                ).start();
            }


        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args)
    {
        LogServer logServer = new LogServer(8080);
        logServer.startServer();
    }
}

class LogWorkerThread extends AWorkerThread
{
    private final String logFilePath;
    private final String countFilePath;
    public LogWorkerThread(Socket clientSocket, String logFilePath, String countFilePath)
    {
        super(clientSocket);
        this.logFilePath = logFilePath;
        this.countFilePath = countFilePath;
    }

    @Override
    public void run()
    {
        String logInfo = null;
        try
        {
            logInfo = this.reader.readLine();
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        this.handleLogWriting(logInfo);
        System.out.println("Log Worker Got the Log: " + logInfo);
        this.writer.write("LOG RECEIVED, 200 OK");

        try
        {
            this.closeEverything(this.clientSocket);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void handleLogWriting(String log)
    {
        synchronized (LogWorkerThread.class)
        {
            this.writeLog(log);
            this.writeCount();
        }
    }

    public void writeLog(String log)
    {
        PrintWriter writer;
        try
        {
            writer = new PrintWriter(new FileWriter(this.logFilePath, true), true);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        writer.println(log);
        writer.flush();
        writer.close();
    }

    public void writeCount()
    {
        try
        {
            RandomAccessFile counterRAF = new RandomAccessFile(this.countFilePath, "rw");

            String line = counterRAF.readLine();
            int counter = 1;
            if (line != null)
            {
                System.out.println("Non Empty file");
                counter = Integer.parseInt(line) + 1;
            }
            else
            {
                System.out.println("Empty file");
            }
            counterRAF.seek(0);
            counterRAF.writeBytes(Integer.toString(counter));
            counterRAF.close();

        } catch (FileNotFoundException e)
        {
            throw new RuntimeException(e);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}

