package Networking.Ex2;

import Networking.ExtendClasses.AUDPClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class SimpleUDPClient extends AUDPClient
{

    private final String message;

    public SimpleUDPClient(InetAddress serverIP, int serverPort, String message)
    {
        super(serverIP, serverPort);
        this.message = message;
    }


    @Override
    public void run()
    {
        try
        {
            this.sendMessage(this.message);
            String serverMessage = this.reciveMessage();
            System.out.println("Server send: " + serverMessage);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws UnknownHostException
    {
        SimpleUDPClient simpleUDPClient = new SimpleUDPClient(InetAddress.getLocalHost(), 8000, "Hello MF");
        simpleUDPClient.start();
    }
}
