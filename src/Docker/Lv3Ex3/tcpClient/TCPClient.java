/*
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

public class Docker.Ex7.tcpClient.TCPClient extends Thread
{
    private InetAddress serverIp;
    private int port;
    private String message;
    public final static Random random = new Random();

    public Docker.Ex7.tcpClient.TCPClient(InetAddress serverIp, int port, String message)
    {
        this.serverIp = serverIp;
        this.port = port;
        this.message = message;

    }

    @Override
    public void run()
    {
        try
        {
            Socket clientSocket = new Socket(this.serverIp, this.port);
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            String firstMessage = random.nextInt(0, 100) % 2 == 0 ? "Login" : "Logg in";


            writer.write(String.format("%s\n", firstMessage));
            writer.flush();

            String response = reader.readLine();
            if (response == null)
            {
                System.out.println("Connection forcefully closed from the server");
                clientSocket.close();
                return;
            }

            System.out.printf("Server Response: %s\n", response);

            writer.write(String.format("%s %.2f\n", this.message, random.nextDouble(5, 10)));
            writer.flush();

            response = reader.readLine();
            System.out.printf("Server Response: %s\n", response);

            boolean logOut = random.nextInt(0, 100) % 2 == 0;

            if (!logOut)
            {
                clientSocket.close();
                return;
            }
            System.out.println("Trying to Logout from the server...");
            writer.write("Logout\n");
            writer.flush();

            response = reader.readLine();

            if (response == null)
            {
                System.out.println("Connection forcefully closed from the server");
                clientSocket.close();
                return;
            }

            System.out.printf("Successfully %s !\n", response);
            clientSocket.close();
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws UnknownHostException
    {
        String serverName = System.getenv("SERVER_NAME");
        InetAddress serverIP = InetAddress.getByName(serverName);
        int serverPort = Integer.parseInt(System.getenv("SERVER_PORT"));
        Docker.Ex7.tcpClient.TCPClient tcpClient = new Docker.Ex7.tcpClient.TCPClient(serverIP, serverPort, "Why is Docker in this course? The average grade last midterm was: ");
        tcpClient.start();
    }
}*/
