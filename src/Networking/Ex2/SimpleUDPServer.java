package Networking.Ex2;

import Networking.ExtendClasses.AUDPServer;

import java.io.IOException;
import java.net.DatagramPacket;

public class SimpleUDPServer extends AUDPServer
{

    public SimpleUDPServer(int port)
    {
        super(port);
    }

    @Override
    public void startServer()
    {
        System.out.println("Starting the server...");
        try
        {

            while (true)
            {
                DatagramPacket datagram = this.reciveDatagram();
                String message = this.getMessage(datagram);
                System.out.println("Server Received: " + message);
                this.sendMessage(message, datagram);
            }


        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args)
    {
        SimpleUDPServer simpleUDPServer = new SimpleUDPServer(8000);
        simpleUDPServer.startServer();
    }
}
