package Networking.OldKolokviumski.ChatApp;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class TMPClient
{

    public static void main(String[] args) throws UnknownHostException
    {
        TCPClientChat client = new TCPClientChat(InetAddress.getLocalHost(), 9753, false);
        client.start();
    }
}
