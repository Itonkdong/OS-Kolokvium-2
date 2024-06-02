/*
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPServer
{
    private int port;
    private DatagramSocket socket;

    public UDPServer(int port)
    {
        this.port = port;
    }

    public void startServer() throws IOException
    {
        System.out.println("Starting the server...");
        this.socket = new DatagramSocket(this.port);
        while (true)
        {
            DatagramPacket datagram = this.getDatagram();
            new WorkerThread(datagram, this.socket).start();
        }
    }

    private DatagramPacket getDatagram() throws IOException
    {
        byte[] buffer = new byte[256];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        this.socket.receive(packet);
        return packet;
    }

    public static void main(String[] args) throws IOException
    {
        int port = Integer.parseInt(System.getenv("SERVER_PORT"));
        UDPServer udpServer = new UDPServer(port);
        udpServer.startServer();
    }
}

class WorkerThread extends Thread
{
    private DatagramPacket datagram;
    private DatagramSocket socket;

    public WorkerThread(DatagramPacket packet, DatagramSocket socket)
    {
        this.datagram = packet;
        this.socket = socket;
    }

    @Override
    public void run()
    {
        try
        {
            String message = this.getMessage(this.datagram);
            if (message.equals("Log in"))
            {
                System.out.println("Client wants to " + message);
                this.sendMessage("Logged in", this.datagram.getAddress(), this.datagram.getPort());

            }
            else if (message.equals("Log out"))
            {
                System.out.println("Client wants to " + message);
                this.sendMessage("Logout", this.datagram.getAddress(), this.datagram.getPort());
            }
            else if (message.equals("Stay logged in"))
            {
                System.out.println("Client wants to " + message);
            }
            else
            {
                System.out.println("Message from clients: " + message);
                this.sendMessage(message, this.datagram.getAddress(), this.datagram.getPort());
            }

        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    private String getMessage(DatagramPacket datagramPacket)
    {
        return new String(datagramPacket.getData(), 0, datagramPacket.getLength());
    }

    private void sendMessage(String message, InetAddress ip, int port) throws IOException
    {
        DatagramPacket datagramPacket = new DatagramPacket(message.getBytes(), message.getBytes().length, ip, port);
        this.socket.send(datagramPacket);
    }
}*/
