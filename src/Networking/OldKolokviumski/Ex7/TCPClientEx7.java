package Networking.OldKolokviumski.Ex7;

import Networking.ExtendClasses.ATCPClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Objects;

public class TCPClientEx7 extends ATCPClient
{

    protected final String srcFolderPath;
    public TCPClientEx7(InetAddress serverIP, int serverPort, String srcFolderPath)
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
            for (File file : Objects.requireNonNull(srcFolder.listFiles()))
            {
                if (file.isDirectory()) continue;
                String extension = file.getName().split("\\.")[1];
                if (!extension.equals("txt")) continue;

                this.writer.println(String.format("###%s###", file.getName()));

                BufferedReader fileReader = new BufferedReader(new FileReader(file.getAbsoluteFile()));

                fileReader.lines().forEach(line->this.writer.println(line));

                this.writer.println("!!!END!!!");
            }

            this.closeEverything(clientSocket);

        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws UnknownHostException
    {
        for (int i = 0; i < 10; i++)
        {
            TCPClientEx7 client = new TCPClientEx7(InetAddress.getLocalHost(), 9876, "D:\\OS Kolokvium Playground\\OS Kolokvium Exercises\\src\\Networking\\OldKolokviumski\\Ex7");
            client.start();
        }


    }
}
