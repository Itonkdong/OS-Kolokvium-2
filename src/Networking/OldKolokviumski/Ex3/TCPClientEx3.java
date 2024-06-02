package Networking.OldKolokviumski.Ex3;

import Networking.ExtendClasses.ATCPClient;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TCPClientEx3 extends ATCPClient
{

    private final String action;
    public TCPClientEx3(InetAddress serverIP, int serverPort, String action)
    {
        super(serverIP, serverPort);
        this.action = action;
    }

    @Override
    public void run()
    {

        try
        {
            Socket clientSocket = new Socket(this.serverIP, this.serverPort);
            this.setupReaderWriter(clientSocket);
            String actionString = this.getActionString(clientSocket);
            this.writer.println(actionString);
            this.handleCertainAction();

            this.handleResponse();

            this.closeEverything(clientSocket);

        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }


    }

    private void handleCertainAction()
    {
        if (!this.action.equals("UPLOAD")) return;

        try
        {
            BufferedReader fileReader = new BufferedReader(new FileReader("D:\\OS Kolokvium Playground\\OS Kolokvium Exercises\\src\\Networking\\OldKolokviumski\\Ex3\\uploadFile.txt"));
            this.writer.println("BEGIN");
            fileReader.lines().forEach(line->this.writer.println(line));
            this.writer.println("END");

            fileReader.close();

        } catch (FileNotFoundException e)
        {
            throw new RuntimeException(e);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }

    }

    private String getActionString(Socket clientSocket)
    {
        String actionString;
        if (this.action.equals("COPY"))
        {
            actionString = String.format("COPY,D:\\OS Kolokvium Playground\\OS Kolokvium Exercises\\src\\Networking\\OldKolokviumski\\Ex3\\copyFile.txt,D:\\OS Kolokvium Playground\\OS Kolokvium Exercises\\src\\Networking\\OldKolokviumski\\Ex3\\DestinationFolder\\fileCopy.txt");
        }
        else if (this.action.equals("SAVE_LOG"))
        {
            actionString = String.format("SAVE_LOG,[%s] %s %s", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), clientSocket.getInetAddress(), clientSocket.getPort());
        }
        else if (this.action.equals("DOWNLOAD"))
        {
            actionString = String.format("DOWNLOAD,D:\\OS Kolokvium Playground\\OS Kolokvium Exercises\\src\\Networking\\OldKolokviumski\\Ex3\\downloadFile.txt");
        }
        else if (this.action.equals("SIZE"))
        {
            actionString = String.format("SIZE,D:\\OS Kolokvium Playground\\OS Kolokvium Exercises\\src\\Networking\\OldKolokviumski\\Ex3\\downloadFile.txt");
        }
        else if (this.action.equals("UPLOAD"))
        {
            actionString = String.format("UPLOAD,myUploadedFile.txt");
        }
        else
        {
            actionString = null;
        }

        return  actionString;
    }

    public void handleResponse()
    {

        try
        {
            if (this.action.equals("COPY"))
            {
                String serverResponse = this.reader.readLine();
                this.printServerResponse(serverResponse);
            }
            else if (this.action.equals("SAVE_LOG"))
            {
                String serverResponse = this.reader.readLine();
                this.printServerResponse(serverResponse);
            }
            else if (this.action.equals("UPLOAD"))
            {
                String serverResponse = this.reader.readLine();
                this.printServerResponse(serverResponse);
            }
            else if (this.action.equals("DOWNLOAD"))
            {
                String line = this.reader.readLine();
                if (!line.equals("BEGIN"))
                {
                    System.out.println("DOWNLOAD FAILED!");
                    return;
                }

                System.out.println("File Content:");
                while (true)
                {
                    line = this.reader.readLine();
                    if (line.equals("END")) break;
                    System.out.println(line);
                }

            }
            else if (this.action.equals("SIZE"))
            {
                String fileSizeInfo = this.reader.readLine();
                System.out.println(fileSizeInfo);
            }
            else
            {
                System.out.println("INVALID ACTION");
            }
        } catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) throws UnknownHostException
    {
        TCPClientEx3 copyClient = new TCPClientEx3(InetAddress.getLocalHost(), 8080, "COPY");
        copyClient.start();
        for (int i = 0; i < 50; i++)
        {
            TCPClientEx3 logClient = new TCPClientEx3(InetAddress.getLocalHost(), 8080, "SAVE_LOG");
            logClient.start();
        }

        TCPClientEx3 downloadClient = new TCPClientEx3(InetAddress.getLocalHost(), 8080, "DOWNLOAD");
        downloadClient.start();
        TCPClientEx3 sizeClient = new TCPClientEx3(InetAddress.getLocalHost(), 8080, "SIZE");
        sizeClient.start();

        for (int i = 0; i < 30; i++)
        {
            TCPClientEx3 uploadClient = new TCPClientEx3(InetAddress.getLocalHost(), 8080, "UPLOAD");
            uploadClient.start();
        }

    }

}
