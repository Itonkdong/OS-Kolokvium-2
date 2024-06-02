package Networking.OldKolokviumski.ChatApp;

import Networking.ExtendClasses.ATCPClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class TCPClientChat extends ATCPClient
{
    private int retries;
    private final boolean isSender;

    private final static int MAX_RETRIES = 3;
    private final static int MAX_MESSAGES = 3;
    public TCPClientChat(InetAddress serverIP, int serverPort, boolean isSender)
    {
        super(serverIP, serverPort);
        this.isSender = isSender;
        this.retries = 0;
    }

    @Override
    public void run()
    {
        if (retries == MAX_RETRIES) {
            System.out.println("Max Attempts Reached! Closing Connections");
            return;
        }

        System.out.println("Login To Server:");

        Socket clientSocket = null;
        try
        {
            clientSocket = new Socket(this.serverIP, this.serverPort);
            this.setupReaderWriter(clientSocket);


            BufferedReader systemInReader = new BufferedReader(new InputStreamReader(System.in));
            String loginMessage = systemInReader.readLine();
            this.writer.println(loginMessage);

            String loginStatus = this.reader.readLine();
            if (!loginStatus.equals("200 OK"))
            {
                retries++;
                this.printServerResponse(loginStatus);
                this.run();
                return;
            }

            this.printServerResponse(loginStatus);

            String helloMessage = systemInReader.readLine();
            this.writer.println(helloMessage);

            // You can chat now!
            String helloResponse = this.reader.readLine();
            this.printServerResponse(helloResponse);

            if (this.isSender)
            {
                this.sender(systemInReader);
            }
            else
            {
                this.receiver();
            }

            systemInReader.close();

            this.closeEverything(clientSocket);




        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }

    }

    public void sender(BufferedReader systemInReader) throws IOException
    {
        while (true)
        {
            String userMessage = systemInReader.readLine();

            if (userMessage.equals("DONE"))
            {
                this.writer.println("DONE");
                return;
            }

            this.writer.println(userMessage);

            String messageResponse = this.reader.readLine();
            String[] parts = messageResponse.split(",");
            int code = Integer.parseInt(parts[0]);
            String message = parts[1];

            // 200,Successfully Send Message - ok
            // 300,{userMessage}- echo
            // 404,User Not Logged In- error

            if (code == 200)
            {
                this.printServerResponse(message);
            }
            else if (code == 300)
            {
                System.out.println("Echo: " + message);
            }
            else if (code == 404)
            {
                System.out.println("Error: " + message);
            }
        }

    }

    public void receiver() throws IOException
    {
        int count =0;
        while (true)
        {
            String otherUserMessage = this.reader.readLine();
            count += 1;
            System.out.println("Message from user: " + otherUserMessage);
            if (count == MAX_MESSAGES) {

                this.writer.println("DONE");
                return;
            }

        }
    }

    public static void main(String[] args) throws UnknownHostException
    {

        TCPClientChat client = new TCPClientChat(InetAddress.getLocalHost(), 9753, true);
        client.start();
    }
}
