package Networking.Ex3;

import Networking.ExtendClasses.ATCPClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class SimpleHTTPClient extends ATCPClient
{
    private final String serverName;
    private final String name;
    private final String surname;
    public SimpleHTTPClient(InetAddress serverIP, int serverPort, String serverName, String name, String surname)
    {
        super(serverIP, serverPort);
        this.serverName = serverName;
        this.name = name;
        this.surname = surname;
    }

    @Override
    public void run()
    {
        String urlString = String.format("http://%s:%s?name=%s&surname=%s", serverName, serverPort, name, surname);
        try
        {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            this.reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while (true)
            {
                String line = this.reader.readLine();
                if (line == null) break;
                System.out.println(line);
            }
            this.reader.close();
            connection.disconnect();

        } catch (MalformedURLException e)
        {
            throw new RuntimeException(e);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws UnknownHostException
    {
        SimpleHTTPClient simpleHTTPClient = new SimpleHTTPClient(InetAddress.getLocalHost(), 8080, "localhost", "Viktor", "Kostadinoski");
        simpleHTTPClient.start();
    }
}
