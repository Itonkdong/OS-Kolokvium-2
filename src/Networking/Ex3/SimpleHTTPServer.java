package Networking.Ex3;

import Networking.ExtendClasses.ATCPServer;
import Networking.ExtendClasses.AWorkerThread;
import Networking.ExtendClasses.Request;

import javax.swing.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleHTTPServer extends ATCPServer
{
    public SimpleHTTPServer(int port)
    {
        super(port);
    }

    @Override
    public void startServer()
    {
        System.out.println("String the server...");
        try
        {
            ServerSocket serverSocket = new ServerSocket(this.port);
            while (true)
            {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client");
                new SimpleHTTPWorkerThread(clientSocket).start();
            }



        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args)
    {
        SimpleHTTPServer simpleHTTPServer = new SimpleHTTPServer(8080);
        simpleHTTPServer.startServer();
    }
}

class SimpleHTTPWorkerThread extends AWorkerThread
{
    public SimpleHTTPWorkerThread(Socket clientSocket)
    {
        super(clientSocket);
    }

    @Override
    public void run()
    {
        StringBuilder stringBuilder = new StringBuilder();
        while (true)
        {
            String line;
            try
            {
                line = this.reader.readLine();
            } catch (IOException e)
            {
                throw new RuntimeException(e);
            }
            if (line == null || line.isEmpty()) break;
            stringBuilder.append(line);
            stringBuilder.append("\n");
        }
        Request request = Request.of(stringBuilder.toString());
        request.testPrint();

        // BE CAREFULLY HOW YOU FORMAT THE RESPONSE, THAT IS REALLY IMPORTANT, ESPECIALLY THE FIRST TWO LINES
        this.writer.println("HTTP/1.1 200 OK");
        this.writer.println("Content-type: text/html");
        this.writer.println();
        this.writer.println("<html>");
        this.writer.println("<head></head>");
        this.writer.println("<body>");
        this.writer.printf("<h1>%s</h1>\n", request.getQueryParameter("name"));
        this.writer.printf("<h1>%s</h1>\n", request.getQueryParameter("surname"));
        this.writer.println("</body>");
        this.writer.println("</html>");

        try
        {
            this.closeEverything(clientSocket);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
