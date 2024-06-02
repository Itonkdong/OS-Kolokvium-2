package Networking.OldKolokviumski.Ex8;

import Networking.ExtendClasses.ATCPServer;
import Networking.ExtendClasses.AWorkerThread;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

public class TCPServerEx8 extends ATCPServer
{
    public TCPServerEx8(int port)
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
                new WorkerThreadEx8(clientSocket, "D:\\OS Kolokvium Playground\\OS Kolokvium Exercises\\src\\Networking\\OldKolokviumski\\Ex8\\ServerContent").start();

            }
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args)
    {
        TCPServerEx8 server = new TCPServerEx8(7894);
        server.startServer();
    }
}

class WorkerThreadEx8 extends AWorkerThread
{

    private final String srcFolderPath;
    public WorkerThreadEx8(Socket clientSocket, String srcFolderPath)
    {
        super(clientSocket);
        this.srcFolderPath = srcFolderPath;
    }

    @Override
    public void run()
    {
        String clientAction = null;
        try
        {
            clientAction = this.reader.readLine();

            this.printClientRequest(clientAction);

            File srcFolder = new File(this.srcFolderPath);
            for (File file : Objects.requireNonNull(srcFolder.listFiles()))
            {
                this.writer.println(file.getName());
            }
            this.writer.println();

            String clientFileAction = this.reader.readLine();
            String fileName = clientFileAction.split("=")[1];

            String fullFileName = String.format("%s\\%s", srcFolder, fileName);
            BufferedReader fileReader = new BufferedReader(new FileReader(fullFileName));

            this.writer.println("downloadfile:start");

            fileReader.lines().forEach(line->this.writer.println(line));

            this.writer.println("downloadfile:end");

            fileReader.close();

            this.closeEverything(clientSocket);

        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
