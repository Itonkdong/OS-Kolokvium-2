package Networking.ExtendClasses;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public abstract class AUDPClient extends Thread
{

    protected final InetAddress serverIP;
    protected final int serverPort;
    protected final DatagramSocket socket;

    public AUDPClient(InetAddress serverIP, int serverPort)
    {

        this.serverIP = serverIP;
        this.serverPort = serverPort;
        try
        {
            this.socket = new DatagramSocket();
        } catch (SocketException e)
        {
            throw new RuntimeException(e);
        }
    }

    public String reciveMessage() throws IOException
    {
        byte[] buffer = new byte[256];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        this.socket.receive(packet);
        return new String(packet.getData(), 0,packet.getLength());
    }

    public void sendMessage(String message) throws IOException
    {
        DatagramPacket packet = new DatagramPacket(message.getBytes(), message.length(), this.serverIP, this.serverPort);
        this.socket.send(packet);
    }
}
