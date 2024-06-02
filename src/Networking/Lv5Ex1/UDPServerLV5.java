package Networking.Lv5Ex1;

import Networking.ExtendClasses.AUDPServer;

import java.io.IOException;
import java.net.DatagramPacket;

public class UDPServerLV5 extends AUDPServer
{
    public UDPServerLV5(int port)
    {
        super(port);
    }

    @Override
    public void startServer()
    {
        System.out.println("Starting the server");

        while (true)
        {
            try
            {
                DatagramPacket packet = this.reciveDatagram();
                String userLoginMessage = this.getMessage(packet);
                System.out.println("User wants to: " + userLoginMessage);
                this.sendMessage("Logged In", packet);

                packet = this.reciveDatagram();
                String userMessage = this.getMessage(packet);
                System.out.println("User's Message: " + userMessage);
                this.sendMessage(userMessage, packet);

                packet = this.reciveDatagram();
                String userLogStatus = this.getMessage(packet);

                System.out.println("User wants to: " + userLogStatus);
                if (userLogStatus.equals("Logout"))
                {
                    this.sendMessage("Successfully logged out", packet);
                }
                else
                {
                    this.sendMessage("Stayed logged in", packet);
                }
            } catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }

    }

    public static void main(String[] args)
    {
        UDPServerLV5 udpServerLV5 = new UDPServerLV5(9090);
        udpServerLV5.startServer();
    }
}
