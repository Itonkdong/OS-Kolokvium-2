package Networking.ExtendClasses;

public abstract class ATCPServer
{

    protected final int port;

    public ATCPServer(int port)
    {
        this.port = port;
    }

    public abstract void startServer();
}
