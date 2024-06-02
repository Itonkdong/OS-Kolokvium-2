package Networking.Ex4;

import Networking.ExtendClasses.ATCPServer;
import Networking.ExtendClasses.AWorkerThread;
import Networking.ExtendClasses.Request;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

public class MoreComplexTCPServer extends ATCPServer
{
    public MoreComplexTCPServer(int port)
    {
        super(port);
    }

    @Override
    public void startServer()
    {
        System.out.println("Starting the server...");

        try
        {
            ServerSocket serverSocket = new ServerSocket(this.port);
            while (true)
            {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New Client!");
                new MoreComplexWorkerThread(clientSocket).start();
            }
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }


    }

    public static void main(String[] args)
    {

        MoreComplexTCPServer moreComplexTCPServer = new MoreComplexTCPServer(7070);
        moreComplexTCPServer.startServer();
    }
}

class MoreComplexWorkerThread extends AWorkerThread
{
    public MoreComplexWorkerThread(Socket clientSocket)
    {
        super(clientSocket);
    }

    @Override
    public void run()
    {
        String requestString = this.reader.lines().collect(Collectors.joining("\n"));
        Request request = Request.of(requestString);
        this.handleRequestLog(request);

        this.writer.println("HTTP/1.1 200 OK");
        this.writer.println("Hello User: " + request.getHeader("User"));
        this.writer.println("You Requested: " + request.getUri());

        try
        {
            this.closeEverything(this.clientSocket);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }


    }

    public void handleRequestLog(Request request)
    {
        try
        {
            Socket logServerSocket = new Socket(InetAddress.getLocalHost(), 8080);
            PrintWriter logWriter = new PrintWriter(new OutputStreamWriter(logServerSocket.getOutputStream()),true);
            BufferedReader logReader = new BufferedReader(new InputStreamReader(logServerSocket.getInputStream()));
            String logInfo = String.format("[%s] %s %s", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), request.getUri(), clientSocket.getInetAddress());

            logWriter.println(logInfo);

            String logServerResponse = logReader.readLine();
            System.out.println("Log Server Response: " + logServerResponse);

            logReader.close();
            logReader.close();

            logServerSocket.close();

        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
