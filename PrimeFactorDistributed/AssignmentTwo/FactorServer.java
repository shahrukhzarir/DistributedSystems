/**
 * Created by shahrukhzarir on 17-11-09.
 */
import java.math.BigInteger;
import java.rmi.*;

public class FactorServer {
    public static void main(String[] args) {
        /*
        System.setSecurityManager(new RMISecurityManager());
        */


        /*
        Server class to run the clients
         */
        try {
            System.out.println("Published in RMI registery, ready...");
            System.out.println("Clients can now connect to this server");
            BigInteger target = new BigInteger(args[0]);
            int packages = Integer.parseInt(args[0]);
            FactorImpl factor = new FactorImpl(target, packages);
            Naming.rebind(Factor.SERVICENAME, factor);

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
