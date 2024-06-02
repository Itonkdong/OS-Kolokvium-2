package Networking.OldKolokviumski.CovidEx;

import Networking.ExtendClasses.ATCPServer;
import Networking.ExtendClasses.AWorkerThread;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class TCPServerCovid extends ATCPServer
{

    private final String fileOutputPath;
    public TCPServerCovid(int port, String fileOutputPath)
    {
        super(port);
        this.fileOutputPath = fileOutputPath;
    }

    @Override
    public void startServer()
    {
        System.out.println("Starting the server...");
        ServerSocket serverSocket = null;
        try
        {
            serverSocket  = new ServerSocket(this.port);

            while (true)
            {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New Client Connection");
                new WorkerThreadCovid(clientSocket, this.fileOutputPath).start();
            }



        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args)
    {
        TCPServerCovid server = new TCPServerCovid(8888, "D:\\OS Kolokvium Playground\\OS Kolokvium Exercises\\src\\Networking\\OldKolokviumski\\CovidEx\\ServerData\\data.csv");
        server.startServer();
    }
}

class WorkerThreadCovid extends AWorkerThread
{

//    private static final Lock lock = new ReentrantLock();
    private final String fileOutputPath;

    public WorkerThreadCovid(Socket clientSocket, String fileOutputPath)
    {
        super(clientSocket);
        this.fileOutputPath = fileOutputPath;
    }

    @Override
    public void run()
    {
        this.writer.println(String.format("HELLO %s", this.clientSocket.getInetAddress()));

        try
        {
            String clientHelloMessage = this.reader.readLine();

            if (clientHelloMessage == null)
            {
                throw new RuntimeException("Something went wrong");
            }
            this.printClientRequest(clientHelloMessage);

            this.writer.println("SEND DAILY DATA");

            String clientDataString = this.reader.readLine();
            String[] dataParts = clientDataString.split(",");

            if (dataParts.length != 3)
            {
                throw new RuntimeException("Invalid Data");
            }

            this.handleData(dataParts);

            this.writer.println("OK");

            String endLine = this.reader.readLine();
            this.printClientRequest(endLine);

            this.closeEverything(clientSocket);


        } catch (IOException e)
        {
            System.out.println(e.getMessage());
        }

    }

    public void handleData(String[] dataParts) throws IOException
    {

        synchronized (WorkerThreadCovid.class)
        {
            PrintWriter fileWriter = new PrintWriter(new FileWriter(this.fileOutputPath, true), true);
            BufferedReader fileReader = new BufferedReader(new FileReader(this.fileOutputPath));

            String line = fileReader.readLine();
            if (line == null)
            {
                fileWriter.println("Data,New Cases,Hospitalized,Recovered");
            }

            fileWriter.println(String.format("%s,%s,%s", dataParts[0], dataParts[1], dataParts[2]));

            fileWriter.close();
            fileReader.close();
        }
    }
}
