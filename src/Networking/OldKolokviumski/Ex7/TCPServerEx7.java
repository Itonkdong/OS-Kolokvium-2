package Networking.OldKolokviumski.Ex7;

import Networking.ExtendClasses.ATCPServer;
import Networking.ExtendClasses.AWorkerThread;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class TCPServerEx7 extends ATCPServer
{
    public TCPServerEx7(int port)
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
                new WorkerThreadEx7(clientSocket, "D:\\OS Kolokvium Playground\\OS Kolokvium Exercises\\src\\Networking\\OldKolokviumski\\Ex7\\ServerFileFolder").start();
            }

        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args)
    {
        TCPServerEx7 server = new TCPServerEx7(9876);
        server.startServer();
    }
}

class WorkerThreadEx7 extends AWorkerThread
{

    private final String destinationFolderPath;
    public WorkerThreadEx7(Socket clientSocket, String destinationFolderPath)
    {
        super(clientSocket);
        this.destinationFolderPath = destinationFolderPath;
    }

    @Override
    public void run()
    {
        List<String> clientData = this.reader.lines().collect(Collectors.toList());
        Map<String, List<String>> fileData = this.getFileData(clientData);

        this.handleFileData(fileData);

        try
        {
            this.closeEverything(this.clientSocket);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }


    }

    public Map<String, List<String>> getFileData(List<String> data)
    {
        Map<String, List<String>> files = new LinkedHashMap<>();
        String currentKey = null;

        for (String dataLine : data)
        {
            if (dataLine.startsWith("###"))
            {
                String fileName = dataLine.substring(3, dataLine.length() - 3);
                currentKey = fileName;
                continue;
            }

            if (dataLine.equals("!!!END!!!")) continue;

            files.putIfAbsent(currentKey, new ArrayList<>());
            files.get(currentKey).add(dataLine);
        }

        return files;
    }

    public void handleFileData(Map<String, List<String>> fileData)
    {
        synchronized (WorkerThreadEx7.class)
        {
            String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            timeStamp = timeStamp.replaceAll(":", "-");
            timeStamp = timeStamp.replaceAll("\\.", "-");
            String _newFolderName = timeStamp;
            String userFolderPath = String.format("%s\\%s",this.destinationFolderPath,_newFolderName);
            File userFolder = new File(userFolderPath);
            if (!userFolder.exists())
            {
                userFolder.mkdirs();
            }

            fileData.entrySet().stream().forEach(entry->{
                String fileName = entry.getKey();
                List<String> fileContent = entry.getValue();
                String fullFilePath = String.format("%s\\%s", userFolder, fileName);
                try
                {
                    PrintWriter fileWriter = new PrintWriter(new FileWriter(fullFilePath, true), true);
                    fileContent.stream().forEach(line->fileWriter.println(line));

                    fileWriter.close();

                } catch (IOException e)
                {
                    System.out.println(e.getMessage());
                }
            });
        }

    }
}
