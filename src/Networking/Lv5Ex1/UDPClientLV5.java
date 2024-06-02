package Networking.Lv5Ex1;

import Networking.ExtendClasses.AUDPClient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;

public class UDPClientLV5 extends AUDPClient
{
    private final String message;
    public UDPClientLV5(InetAddress serverIP, int serverPort, String message)
    {
        super(serverIP, serverPort);
        this.message = message;
    }

    @Override
    public void run()
    {
        try
        {
            this.sendMessage("Login");
            String serverResponse = this.reciveMessage();
            System.out.println("Server responded: " + serverResponse);

            this.sendMessage(this.message);
            String serversEcho = this.reciveMessage();
            System.out.println("Servers Echo: " + serversEcho);


            Random random = new Random();
            boolean shouldLogout = random.nextBoolean();

            if (shouldLogout)
            {
                this.sendMessage("Logout");
            }
            else
            {
                this.sendMessage("Stay logged in");
            }

            String serverLogResponse = this.reciveMessage();
            System.out.println("Server Responded: "  + serverLogResponse);

        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) throws UnknownHostException
    {
        UDPClientLV5 udpClientLV5 = new UDPClientLV5(InetAddress.getLocalHost(), 9090, "YEYE OS WE LEARN");
        udpClientLV5.start();
    }
}
