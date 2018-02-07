/**
 *Shahrukh Zarir
 * Distributed Systems
 * ProxyRequestThread.java
 * 10/13/2017
 * Takes any request from the user and sends a request
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class ProxyRequestThread extends Thread {
    private static boolean running = true;
    public static ArrayList<String> Block = new ArrayList<String>();
    public ProxyRequestThread(){

    }
    @Override
    public void run(){
        Scanner input = new Scanner(System.in);

        //sends a request to whatever the client selects
        while(running){
            System.out.println("Please Choose 1/2/3");
            System.out.println("1) Block a new site.");
            System.out.println("2) View Log");
            System.out.println("3) Exit");

            String choice = input.nextLine();

            //adds site to the block list
            if (choice.equals("1")){
                System.out.println("Which site would you like to block[exclude[http://]:");
                String link = input.nextLine();
                Block.add(link);
                System.out.println("We blocked: " + link);


                //reads from a file which is the log
            } else if (choice.equals("2")){

                try (BufferedReader br = new BufferedReader(new FileReader(Proxy.logFile))) {
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        System.out.println(line);
                    }

                } catch (Exception e) {
                    e.getMessage();
                }


                //stops running
            } else if (choice.equals("3")){
                running = false;
            }
        }
    }
}
