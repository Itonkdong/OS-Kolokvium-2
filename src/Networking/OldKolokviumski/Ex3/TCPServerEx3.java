package Networking.OldKolokviumski.Ex3;

import Networking.ExtendClasses.ATCPServer;
import Networking.ExtendClasses.AWorkerThread;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TCPServerEx3 extends ATCPServer
{
    public TCPServerEx3(int port)
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
                WorkerThreadEx3 workerThread = new WorkerThreadEx3(clientSocket,
                        "D:\\OS Kolokvium Playground\\OS Kolokvium Exercises\\src\\Networking\\OldKolokviumski\\Ex3\\logs.txt");
                workerThread.start();
            }


        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args)
    {
        TCPServerEx3 tcpServerEx3 = new TCPServerEx3(8080);
        tcpServerEx3.startServer();
    }
}

interface ServerAction
{
    void execute(PrintWriter writer, BufferedReader  reader,String... args) throws IOException;
}

class ServerActionBuilder
{
    public static ServerAction createAction(String action, String logFilePath)
    {
        if (action.equals("COPY"))
        {
            return (writer, reader,args) ->
            {
                String fromFilePath = args[0];
                String toFilePath = args[1];
                PrintWriter fileWriter = new PrintWriter(new FileWriter(toFilePath, false), true);
                BufferedReader fileReader = new BufferedReader(new FileReader(fromFilePath));
                fileReader.lines().forEach(line ->
                {
                    fileWriter.println(line);
                });

                File fromFile = new File(fromFilePath);
                File toFile = new File(toFilePath);

                fileWriter.close();
                fileReader.close();
                writer.println(String.format("Successfully copied %s to %s",fromFile.getName(), toFile.getName() ));
            };
        }
        else if (action.equals("SAVE_LOG"))
        {
            return (writer, reader,args) ->
            {
                String log = args[0];
                WorkerThreadEx3.lock.lock();
                PrintWriter fileWrite = new PrintWriter(new FileWriter(logFilePath, true), true);
                BufferedReader fileReader = new BufferedReader(new FileReader(logFilePath));
                long nextLogID = fileReader.lines().count() + 1;
                fileWrite.println(String.format("%d. %s", nextLogID, log));
                WorkerThreadEx3.lock.unlock();

                fileWrite.close();
                fileReader.close();


                writer.println(String.format("Successfully written log: %s",log ));
            };
        }
        else if(action.equals("DOWNLOAD"))
        {
            return (writer, reader,args) -> {
                String fileName = args[0];
                BufferedReader fileReader = new BufferedReader(new FileReader(fileName));
                writer.println("BEGIN");
                fileReader.lines().forEach(line->writer.println(line));
                writer.println("END");

            };
        }
        else if(action.equals("SIZE"))
        {
            return (writer, reader,args) -> {
                String fileName = args[0];
                File file = new File(fileName);
                writer.println(String.format("Size of file %s: %s", file.getName(), file.length()));
            };
        }
        else
        {
            return (writer, reader,args) -> {
                String uploadFileName = args[0];


                String line = reader.readLine();
                if (!line.equals("BEGIN")) return;

                WorkerThreadEx3.uploadLock.lock();

                String[] parts = uploadFileName.split("\\.");
                String fileName = parts[0];
                String extension = parts[1];
                String saveFolderPath = "D:\\OS Kolokvium Playground\\OS Kolokvium Exercises\\src\\Networking\\OldKolokviumski\\Ex3\\ServerFileFolder";
                File saveFolder = new File(saveFolderPath);
                int fileCount = 0;

                for (File file : Objects.requireNonNull(saveFolder.listFiles()))
                {
                    if (file.getName().contains(fileName)) fileCount +=1;
                }

                if (fileCount != 0)
                {
                    uploadFileName = String.format("%s (%s).%s", fileName, fileCount, extension);
                }

                String fullPath = String.format("%s\\%s", saveFolderPath, uploadFileName);
                PrintWriter fileWriter = new PrintWriter(new FileWriter(fullPath));

                while (true)
                {
                    line = reader.readLine();
                    if (line.equals("END")) break;
                    fileWriter.println(line);
                }

                writer.println("File Successfully Uploaded Under The Name: " + uploadFileName);

                fileWriter.close();

                WorkerThreadEx3.uploadLock.unlock();

            };
        }
    }
}


class WorkerThreadEx3 extends AWorkerThread
{

    public static final Lock lock = new ReentrantLock();
    public static final Lock uploadLock = new ReentrantLock();

    private final String logFilePath;

    public WorkerThreadEx3(Socket clientSocket, String logFilePath)
    {
        super(clientSocket);
        this.logFilePath = logFilePath;
    }

    @Override
    public void run()
    {
        try
        {
            String actionLine = this.reader.readLine();
            String[] split = actionLine.split(",");
            String action = split[0];
            String[] args = Arrays.copyOfRange(split, 1, split.length);
            ServerAction serverAction = ServerActionBuilder.createAction(action, this.logFilePath);
            serverAction.execute(this.writer, this.reader,args);


            this.closeEverything(this.clientSocket);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}