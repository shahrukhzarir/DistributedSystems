/**
 *Shahrukh Zarir
 * Distributed Systems
 * ProxyImplementation.java
 * 10/13/2017
 * helper functions
 */

import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ProxyImplentation {


    /*
    checks if url is blocked
     */
    public static boolean isUrlBlocked (ArrayList<String> Blocked, String url){
        for (int i=0; i <Blocked.size(); i++){
            if (url.contains(Blocked.get(i))){
                return true;
            }
        }
        return false;
    }


    /*
    records information for log
     */
    public static void recordInformation(File file, String line) {
        try {

            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file, true), "UTF-8");
            BufferedWriter fbw = new BufferedWriter(writer);
            fbw.write(line);
            fbw.newLine();
            fbw.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    /*
    finds the time stamp required for log
     */
    public static String timeStamp() {
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp time = new Timestamp(System.currentTimeMillis());
        return date.format(time);
    }

}
