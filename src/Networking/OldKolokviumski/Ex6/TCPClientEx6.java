package Networking.OldKolokviumski.Ex6;

import Networking.ExtendClasses.ATCPClient;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPClientEx6 extends ATCPClient
{
    private final String srcFolderPath;
    public TCPClientEx6(InetAddress serverIP, int serverPort, String srcFolderPath)
    {
        super(serverIP, serverPort);
        this.srcFolderPath = srcFolderPath;
    }

    @Override
    public void run()
    {
        try
        {
            Socket clientSocket = new Socket(this.serverIP, this.serverPort);
            this.setupReaderWriter(clientSocket);

            File srcFolder = new File(this.srcFolderPath);

            for (File file : srcFolder.listFiles())
            {
                String[] parts = file.getName().split("\\.");
                if (file.isDirectory()) continue;
                String fileExtension = parts[1];
                if (!fileExtension.equals("txt") && !fileExtension.equals("csv")) continue;

//                if (file.length() < 10000 || file.length() > 100000) continue;

                this.writer.println(String.format("%s %s %s", clientSocket.getInetAddress(), file.getName(), file.length()));
            }

            clientSocket.shutdownOutput();

            String serverResponse = this.reader.readLine();
            this.printServerResponse(serverResponse);

            this.closeEverything(clientSocket);

        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws UnknownHostException
    {

        for (int i = 0; i < 3; i++)
        {
            TCPClientEx6 client = new TCPClientEx6(InetAddress.getLocalHost(), 3398, "D:\\OS Kolokvium Playground\\OS Kolokvium Exercises\\src\\Networking\\OldKolokviumski\\Ex6");
            client.start();
        }

    }
}
