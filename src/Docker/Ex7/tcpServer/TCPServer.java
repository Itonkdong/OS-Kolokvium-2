
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TCPServer
{
    public final static String LOG_SERVER_NAME = System.getenv("LOG_SERVER_NAME");
    public final static InetAddress LOG_SERVER_IP;

    public final static int LOG_SERVER_PORT = Integer.parseInt(System.getenv("LOG_SERVER_PORT"));

    static
    {
        try
        {
            LOG_SERVER_IP = InetAddress.getByName(LOG_SERVER_NAME);
        } catch (UnknownHostException e)
        {
            throw new RuntimeException(e);
        }
    }

    private final int port;

    public TCPServer(int port)
    {
        this.port = port;
    }

    public void startServer() throws IOException
    {
        ServerSocket serverSocket = new ServerSocket(this.port);
        System.out.println("Server Starting...");
        while (true)
        {
            Socket clientSocket = serverSocket.accept();
            System.out.println("New client connection!");
            new WorkerThread(clientSocket).start();

        }
    }

    public static void main(String[] args) throws IOException
    {
        int port = Integer.parseInt(System.getenv("PORT"));
        TCPServer tcpServer = new TCPServer(port);
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
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);

            List<String> lines = new ArrayList<>();
            String line = null;
            while (!(line = reader.readLine()).equals(""))
            {
                lines.add(line);
            }

            Request request = Request.of(lines);
            this.sendLog(request, clientSocket.getInetAddress());

            String user = request.getHeaders().get("User");
            String contentType = request.getHeaders().get("Content-type");

            printWriter.println(String.format("HTTP/1.1 200 OK"));
            printWriter.println(String.format("Hello %s, you have send me content of type: %s", user, contentType));


            printWriter.close();
            reader.close();
            clientSocket.close();

        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void sendLog(Request request, InetAddress clientIP) throws IOException
    {
        String logString = String.format("[%s] %s %s %s", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME), clientIP.toString(), request.getMethod(), request.getUri());
        Socket logSocket = new Socket(TCPServer.LOG_SERVER_IP, TCPServer.LOG_SERVER_PORT);
        PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(logSocket.getOutputStream()), true);

        printWriter.println(logString);

        printWriter.close();
        logSocket.close();
    }
}

class Request
{
    private String method;
    private String uri;
    private String httpVersion;

    private Map<String, String> headers;

    public String getMethod()
    {
        return method;
    }

    public String getUri()
    {
        return uri;
    }

    public String getHttpVersion()
    {
        return httpVersion;
    }

    public Map<String, String> getHeaders()
    {
        return headers;
    }

    public Request(String method, String uri, String httpVersion, Map<String, String> headers)
    {
        this.method = method;
        this.uri = uri;
        this.httpVersion = httpVersion;
        this.headers = headers;
    }

    public static Request of(List<String> lines)
    {
        String[] parts = lines.get(0).split("\\s+");
        String method = parts[0];
        String uri = parts[1];
        String httpVersion = parts[2];

        HashMap<String, String> headers = lines.stream().skip(1)
                .collect(Collectors.toMap(
                        line -> line.split(":")[0],
                        line -> line.split(":")[1],
                        (o, n) -> n,
                        HashMap::new

                ));

        return new Request(method, uri, httpVersion, headers);

    }
}
