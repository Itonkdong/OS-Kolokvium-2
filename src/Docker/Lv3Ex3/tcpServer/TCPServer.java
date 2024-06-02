/*
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Docker.Ex7.tcpServer.TCPServer
{
    private int port;
    public static int numClientsServed = 0;

    public Docker.Ex7.tcpServer.TCPServer(int port)
    {
        this.port = port;
    }

    public static synchronized void addClient()
    {
        numClientsServed += 1;
    }

    public static synchronized int getClients()
    {
        return numClientsServed;
    }

    public static synchronized void printTotalNumberClients()
    {
        System.out.println(String.format("Total number of server clients is: %d", getClients()));
    }

    public void startServer() throws IOException
    {
        System.out.println("Starting the Server...");
        ServerSocket serverSocket = new ServerSocket(this.port);
        while (true)
        {
            Socket clientSocket = serverSocket.accept();
            System.out.println("New Client Accepted!");
            new WorkerThread(clientSocket).start();
        }
    }

    public static void main(String[] args) throws IOException
    {
        int port = Integer.parseInt(System.getenv("SERVER_PORT"));
        Docker.Ex7.tcpServer.TCPServer tcpServer = new Docker.Ex7.tcpServer.TCPServer(port);
        tcpServer.startServer();
    }
}

class WorkerThread extends Thread
{
    private Socket clientSocket;

    public WorkerThread(Socket clientSocket)
    {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run()
    {
        try
        {
            Docker.Ex7.tcpServer.TCPServer.addClient();
            Docker.Ex7.tcpServer.TCPServer.printTotalNumberClients();
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

            String action = reader.readLine();

            if (!action.equals("Login"))
            {
                System.out.println("Invalid connection! Closing...");
                clientSocket.close();
                return;
            }

            writer.write("Logged in\n");
            writer.flush();

            String message = reader.readLine();
            System.out.println("Client's message: " + message);

            writer.write(String.format("%s\n", message));
            writer.flush();


            String lastAction = reader.readLine();
            if (lastAction == null)
            {
                System.out.println("Client stayed logged in");
                clientSocket.close();
                return;
            }

            if (!lastAction.equals("Logout"))
            {
                System.out.println("Invalid action! Closing...");
                clientSocket.close();
                return;
            }
            System.out.println(String.format("Client's want's to: %s", lastAction));
            writer.write("Logged out\n");
            writer.flush();

            clientSocket.close();
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}*/
