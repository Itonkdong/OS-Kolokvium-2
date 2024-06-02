import java.io.IOException;
import java.net.*;
import java.util.Random;

public class UDPClient extends Thread
{
    private InetAddress serverIp;
    private int serverPort;
    private boolean loggOut;
    private DatagramSocket socket;
    private String message;
    private final static Random random = new Random();

    public UDPClient(InetAddress serverIp, int serverPort, boolean loggOut, String message) throws SocketException
    {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.loggOut = loggOut;
        this.message = message;
        this.socket = new DatagramSocket();
    }

    @Override
    public void run()
    {
        try
        {
            this.sendMessage("Log in");
            String responseMessage = this.getResponse();
            System.out.println(responseMessage);

            this.sendMessage(this.message);
            responseMessage = this.getResponse();
            System.out.println(responseMessage);

            if (!this.loggOut)
            {
                this.sendMessage("Stay logged in");
                return;
            }

            this.sendMessage("Log out");
            responseMessage = this.getResponse();
            System.out.println(responseMessage);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    private void sendMessage(String message) throws IOException
    {
        DatagramPacket datagramPacket = new DatagramPacket(message.getBytes(), message.getBytes().length, serverIp, serverPort);
        this.socket.send(datagramPacket);
    }

    private String getResponse() throws IOException
    {
        byte[] buffer = new byte[256];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        this.socket.receive(packet);
        return new String(packet.getData(), 0, packet.getLength());
    }

    public static void main(String[] args) throws UnknownHostException, SocketException
    {
        int serverPort = Integer.parseInt(System.getenv("SERVER_PORT"));
        String serverName = System.getenv("SERVER_NAME");
        InetAddress serverIp = InetAddress.getByName(serverName);
        boolean shouldLogout = random.nextBoolean();
        UDPClient udpClient = new UDPClient(serverIp, serverPort, shouldLogout, "OS is a poorly organized course, please do better job next year");
        udpClient.start();
    }
}