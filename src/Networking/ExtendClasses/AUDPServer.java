package Networking.ExtendClasses;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public abstract class AUDPServer
{

    protected final int port;

    protected final DatagramSocket socket;

    public AUDPServer(int port)
    {
        this.port = port;
        try
        {
            this.socket = new DatagramSocket(this.port);
        } catch (SocketException e)
        {
            throw new RuntimeException(e);
        }
    }

    public abstract void startServer();

    public DatagramPacket reciveDatagram() throws IOException
    {
        byte[] buffer = new byte[256];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        this.socket.receive(packet);
        return packet;
    }

    public String getMessage(DatagramPacket packet)
    {
        return new String(packet.getData(),0, packet.getLength());
    }

    public void sendMessage(String message, DatagramPacket metaDataPacket) throws IOException
    {
        InetAddress clientIP = metaDataPacket.getAddress();
        int clientPort = metaDataPacket.getPort();
        DatagramPacket sendPacket = new DatagramPacket(message.getBytes(), message.getBytes().length, clientIP, clientPort);
        this.socket.send(sendPacket);
    }
}
