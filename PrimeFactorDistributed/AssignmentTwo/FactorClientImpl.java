import java.io.IOException;
import java.math.BigInteger;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by shahrukhzarir on 17-11-09.
 */
public class FactorClientImpl extends UnicastRemoteObject implements FactorClient {


    public FactorClientImpl() throws RemoteException{

    }
     public static void main (String [] args) throws IOException, NotBoundException{
         new FactorClientImpl().execute();
     }

    private void execute() throws IOException, NotBoundException {

        /*
        if (System.getSecurityManager() == null) {
            System.setSecurityManager((new RMISecurityManager()));
        }
*/
        Factor factor = (Factor) Naming.lookup("FactorService");

        //starter for the factorization
        try{
            factor.getClientInfo(this);
        } catch(Exception e){
            System.exit(0);
        }
    }

    public long factorize(Package newPackage, BigInteger factor) throws RemoteException {
        long starting = newPackage.starting;
        long ending = newPackage.ending;

        //Here is where the math is using the principle provided in class
        for (long l = starting; l <= ending; l += 1L) {
            if ((l % 2L != 0L) && (l % 3L != 0L) && (l % 5L != 0L) &&
                    (factor.mod(BigInteger.valueOf(l)).equals(BigInteger.ZERO))) {
                System.out.println("FOUND FACTOR " + l);
                return l;
            }
        }
        return -1L;
    }

}
