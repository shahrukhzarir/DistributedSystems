import java.rmi.*;

public class KeyServer {
   public static void main(String[] args) {
      System.setSecurityManager(new RMISecurityManager());
      try {
        KeyGenImpl keyGen = new KeyGenImpl();
        Naming.rebind(KeyGen.SERVICENAME, keyGen);
        System.out.println("Published in RMI registery, ready...");
      } catch (Exception e) {
        System.err.println(e.getMessage());
      }
   }
}