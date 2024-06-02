package Networking.ExtendClasses;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public abstract class AUDPWorkerThread extends Thread
{
    protected final DatagramPacket packet;
    protected final DatagramSocket socketFromServer;

    public AUDPWorkerThread(DatagramPacket packet, DatagramSocket socketFromServer)
    {
        this.packet = packet;
        this.socketFromServer = socketFromServer;
    }

    public DatagramPacket getDatagram() throws IOException
    {
        byte[] buffer = new byte[256];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        this.socketFromServer.receive(packet);
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
        this.socketFromServer.send(sendPacket);
    }
}
