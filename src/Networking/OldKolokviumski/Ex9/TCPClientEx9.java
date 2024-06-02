package Networking.OldKolokviumski.Ex9;

import Networking.ExtendClasses.ATCPClient;
import Networking.OldKolokviumski.Ex6.TCPClientEx6;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPClientEx9 extends ATCPClient
{
    private final String srcFolderPath;
    public TCPClientEx9(InetAddress serverIP, int serverPort, String srcFolderPath)
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
            int counter = 0;

            for (File file : srcFolder.listFiles())
            {
                String[] parts = file.getName().split("\\.");
                if (file.isDirectory()) continue;
                String fileExtension = parts[1];
                if (!fileExtension.equals("txt") && !fileExtension.equals("csv")) continue;

                if (file.length() > 20000) continue;
                counter +=1;


            }

            this.writer.println(String.format("%s %s %d", clientSocket.getInetAddress(), clientSocket.getPort(), counter));
            System.out.println("here");

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
            TCPClientEx9 client = new TCPClientEx9(InetAddress.getLocalHost(), 4498, "D:\\OS Kolokvium Playground\\OS Kolokvium Exercises\\src\\Networking\\OldKolokviumski\\Ex9");
            client.start();
        }

    }
}
