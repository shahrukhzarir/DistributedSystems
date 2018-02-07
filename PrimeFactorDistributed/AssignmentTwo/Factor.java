/**
 * Created by shahrukhzarir on 17-11-09.
 */


/*
interface for the implementation class
 */
public interface Factor extends java.rmi.Remote {
    public final static String SERVICENAME="FactorService";
    public void getClientInfo(FactorClient client) throws java.rmi.RemoteException;
}
