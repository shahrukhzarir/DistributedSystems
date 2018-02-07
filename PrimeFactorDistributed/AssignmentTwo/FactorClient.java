import java.math.BigInteger;
import java.rmi.RemoteException;

/**
 * Created by shahrukhzarir on 17-11-09.
 */


public  interface FactorClient extends java.rmi.Remote {
     long factorize(Package newPackage, BigInteger factor) throws RemoteException;
}
