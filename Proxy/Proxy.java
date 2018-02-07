/**
 *Shahrukh Zarir
 * Distributed Systems
 * Proxy.java
 * 10/13/2017
 * Runs the main proxy calls the thread to start proxy
 */
import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;

public class Proxy extends Thread{
    private static boolean running = true;
    private static ServerSocket serverSocket = null;
    public static File myLogFile;
    public static String logFile;

    /*
    Runs proxy
     */
    public static void main (String[] args){
        //int length =args.length;
        //int port = Integer.parseInt(args[0]);
        //String logFile = args[1];

        int port = 7878;
        logFile = "C:\\Users\\100489271\\Desktop";
        Proxy proxy = new Proxy(port, logFile);
    }

    /*
    Creates a thread to run reach request from a different client
     */
    public Proxy(int port, String logFile){
        ProxyRequestThread requestThread = new ProxyRequestThread();
        requestThread.start();
        try {
            myLogFile = new File(logFile, "rw");

        } catch (Exception e){
            e.getMessage();
        }
        try {
            ServerSocket serverSocket = new ServerSocket(port);
        } catch (Exception e){
            e.getMessage();
            System.exit(-1);
        }

        //This takes the information requested and runs it through the thread
        while (running){
            try{
                Socket client = serverSocket.accept();
                ProxySentThread sent = new ProxySentThread(client);
                sent.start();
            } catch(Exception e) {
                e.getMessage();
            }
        }
        shutDown();
    }

    /*
    shuts down entire proxy
     */
    public void shutDown(){
        try {
            serverSocket.close();
        } catch (Exception e){
            e.getMessage();
        }
    }

    }
