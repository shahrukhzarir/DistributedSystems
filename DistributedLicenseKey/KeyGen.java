
public interface KeyGen extends java.rmi.Remote {
   public final static String SERVICENAME="KeyGenerator";
   public String generateKey(String name) throws java.rmi.RemoteException;
   public boolean login(String username, String key) throws java.rmi.RemoteException;
   public String register(String username, String key) throws java.rmi.RemoteException;
   public int calcFac(int n) throws java.rmi.RemoteException;
}