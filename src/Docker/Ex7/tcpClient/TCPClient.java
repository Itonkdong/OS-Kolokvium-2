
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPClient extends Thread
{

    private final InetAddress serverIP;
    private final int serverPort;

    public TCPClient(InetAddress serverIP, int serverPort)
    {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
    }

    @Override
    public void run()
    {
        try
        {
            Socket clientSocket = new Socket(this.serverIP, this.serverPort);
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);

            printWriter.println("GET / HTTP/1.1");
            printWriter.println("User:Mozilla-Firefox");
            printWriter.println("Content-type:text/txt");
            printWriter.println("\n");
            printWriter.flush();

            String line = null;

            while (true)
            {
                line = reader.readLine();
                if (line == null) break;
                System.out.println("Server sent: " + line);
            }

            printWriter.close();
            reader.close();
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
        TCPClient tcpClient = new TCPClient(serverIP, serverPort);
        tcpClient.start();

    }
}
