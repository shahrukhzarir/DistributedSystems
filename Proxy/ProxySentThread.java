/**
 *Shahrukh Zarir
 * Distributed Systems
 * ProxySentThread.java
 * 10/13/2017
 * Runs the main proxy calls the thread to start proxy
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;


public class ProxySentThread extends Thread{

    Socket socketClient = null;
    Socket socketServer = null;


    public ProxySentThread(Socket server){
        socketClient = server;
    }

    @Override
    public void run(){
        //in this block we get pieces of information for the log from substrings
        String newHost = null;
        try {
            ArrayList<String> listOfLinks = new ArrayList<String>();
            BufferedReader input = new BufferedReader((new InputStreamReader(socketClient.getInputStream())));
            String line ="";
            //this area will get us the host
            while(!(line = input.readLine()).equals("")) {
                if (line.length() > 4 && line.substring(0, 4).equals("Host")) {
                    newHost = line.substring(6);
                }
                listOfLinks.add(line);
            }

            //this area will let us know if the host is blocked
            boolean isBlocked = ProxyImplentation.isUrlBlocked(ProxyRequestThread.Block, newHost);
            String isAllowed="";
            if (isBlocked){
                isAllowed ="NotAllowed";
                System.out.println("Site is blocked");

            } else {
                isAllowed="Allowed";
            }
            //now we collect information on the ip address, time and whether we are allowed to load page or not
            String IPaddress = socketClient.getInetAddress().getHostAddress();
            String time = ProxyImplentation.timeStamp();
            String message = newHost + "\t" +IPaddress+ "\t" + time + "\t" +  isAllowed + "\n";
            ProxyImplentation.recordInformation(Proxy.myLogFile, message);

            //as a proxy, this block will send information back to the server, creates a socket for the division between
            //proxy and server
            InetAddress clientIp;
            clientIp = InetAddress.getByName(newHost);

            //creat a socket for this division
            socketServer= new Socket(clientIp,80);

            //read information from the proxy to server
            BufferedWriter proxyToServer = new BufferedWriter(new OutputStreamWriter(socketServer.getOutputStream()));

            //this block assists us in making a URL more useful for out proxy so we have to filter things out
            for (int i = 0; i < listOfLinks.size(); i++){
                if (listOfLinks.get(i).substring(0,11).equals("GET http://")) {
                    String request = listOfLinks.get(i).substring(11);
                    int split = 0;
                    while (split < request.length() && request.charAt(split)!='/'){
                        split++;
                    }

                    //fix list of links for future reference
                    listOfLinks.set(i, "GET " + request.substring(split));
                }
            }

            proxyToServer.write("\r\n");
            proxyToServer.flush();


            //next step of a proxy is to recieve the request again
            ProxyReturnThread answer = new ProxyReturnThread(socketClient, socketServer);
            answer.start();

        } catch (Exception e){
            e.getMessage();
        }


    }

}
