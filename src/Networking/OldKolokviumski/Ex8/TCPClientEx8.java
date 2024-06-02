package Networking.OldKolokviumski.Ex8;

import Networking.ExtendClasses.ATCPClient;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;

public class TCPClientEx8 extends ATCPClient
{

    private final String destFolderPath;
    public TCPClientEx8(InetAddress serverIP, int serverPort, String destFolderPath)
    {
        super(serverIP, serverPort);
        this.destFolderPath = destFolderPath;
    }

    @Override
    public void run()
    {

        try
        {
            Socket clientSocket = new Socket(this.serverIP, this.serverPort);
            this.setupReaderWriter(clientSocket);

            this.writer.println("listFiles");

            Set<String> availableFiles = new HashSet<>();
            System.out.println("Available Files on Server:");
            while (true)
            {
                String line = this.reader.readLine();
                if (line.isBlank()) break;
                System.out.println(line);
                availableFiles.add(line);
            }

            System.out.println("Please insert a file name to download");
            BufferedReader systemInReader = new BufferedReader(new InputStreamReader(System.in));
            String userInput;
            while (true)
            {
                userInput = systemInReader.readLine();
                if (availableFiles.contains(userInput)) break;
                System.out.println("Invalid file name! Input again!");
            }
            this.writer.println(String.format("download?name=%s", userInput));

            String serverResponse = this.reader.readLine();
            this.printServerResponse(serverResponse);

            String fullFileName = String.format("%s\\%s", this.destFolderPath, userInput);
            PrintWriter fileWriter = new PrintWriter(new FileWriter(fullFileName, false), true);

            String fileContentLine;
            while (true)
            {
                fileContentLine = this.reader.readLine();
                if (fileContentLine.equals("downloadfile:end"))
                {
                    this.printServerResponse(fileContentLine);
                    break;
                }

                fileWriter.println(fileContentLine);
            }

            fileWriter.close();

            this.closeEverything(clientSocket);


        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws UnknownHostException
    {
        TCPClientEx8 client = new TCPClientEx8(InetAddress.getLocalHost(), 7894, "D:\\OS Kolokvium Playground\\OS Kolokvium Exercises\\src\\Networking\\OldKolokviumski\\Ex8\\ClientContent");
        client.start();
    }
}
