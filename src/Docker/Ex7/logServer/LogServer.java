
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class LogServer
{
    private final int port;

    public LogServer(int port)
    {
        this.port = port;
    }

    private static final String LOG_FILE_PATH = System.getenv("LOG_FILE_PATH");
    private static final String COUNT_FILE_PATH = System.getenv("COUNT_FILE_PATH");

    public void startServer() throws IOException
    {
        ServerSocket serverSocket = new ServerSocket(this.port);
        System.out.println("Log-Server Starting...");
        while (true)
        {
            Socket clientSocket = serverSocket.accept();
            System.out.println("New client connection!");
            new LogWorkThread(clientSocket,LOG_FILE_PATH , COUNT_FILE_PATH).start();

        }
    }

    public static void main(String[] args) throws IOException
    {
        int port = Integer.parseInt(System.getenv("PORT"));
        LogServer logServer = new LogServer(port);
        logServer.startServer();
    }
}

class LogWorkThread extends Thread
{
    private Socket clientSocket;
    private String logFilePath;
    private String countFilePath;

    public LogWorkThread(Socket clientSocket, String logFilePath, String countFilePath)
    {
        this.clientSocket = clientSocket;
        this.logFilePath = logFilePath;
        this.countFilePath = countFilePath;
    }

    @Override
    public void run()
    {
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String logLine = reader.readLine();
            this.writeInFiles(logLine);
            System.out.println("LogWorker got: " + logLine);

            clientSocket.close();

        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void writeInFiles(String logLine) throws IOException
    {
        synchronized (LogServer.class)
        {
            this.writeLog(logLine);
            this.writeCount();
        }
    }

    public void writeLog(String logLine) throws IOException
    {
        BufferedWriter writer = new BufferedWriter(new FileWriter(this.logFilePath, true));
        writer.write(logLine);
        writer.newLine();
        writer.flush();
        writer.close();
    }

    public void writeCount() throws IOException
    {

        BufferedReader reader = null;

        try
        {
            new FileReader(this.countFilePath);

        }
        catch (FileNotFoundException e)
        {
            new FileWriter(this.countFilePath, true);
        }
        reader = new BufferedReader(new FileReader(this.countFilePath));


        int count = 1;

        String line = reader.readLine();
        if (line != null)
        {
            count = Integer.parseInt(line) + 1;
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(this.countFilePath, false));
        writer.write(String.valueOf(count));
        writer.flush();

        reader.close();
        writer.close();
    }
}
