/**
 *Shahrukh Zarir
 * Distributed Systems
 * ProxyReturnThread.java
 * 10/13/2017
 * Recieves request from proxy
 */


import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ProxyReturnThread extends  Thread {
    Socket socketClient;
    Socket socketServer;

    public ProxyReturnThread(Socket client, Socket server){
        socketClient = client;
        socketServer = server;
    }

    @Override
    public void run() {
        try {
            //proxy to client stream
            final OutputStream clientSide = socketClient.getOutputStream();

            //server to proxy stream
            final InputStream serverSide = socketServer.getInputStream();

            //need bye to send data from server -> proxy -> client
            byte reply[] = new byte[32768];
            int countBytes;
            try {

                //continuously read byte data from server to proxy
                while ((countBytes = serverSide.read(reply)) != -1) {
                    clientSide.write(reply, 0, countBytes);
                    clientSide.flush();
                }
            } catch (Exception e) {
                e.getMessage();
            }

            socketServer.close();
            socketClient.close();
        } catch (Exception e) {
            e.getMessage();
        }
    }
}
