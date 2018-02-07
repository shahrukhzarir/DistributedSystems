import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.security.NoSuchAlgorithmException;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class KeyGenImpl extends UnicastRemoteObject implements KeyGen {
   
   static BufferedWriter bw = null;
   static FileWriter fw = null;
   static File file = null;
   static ArrayList<String> userID = null;
   static BufferedReader br = null;
   static ArrayList<String> inUse = null;
   public KeyGenImpl() throws RemoteException {
      super();
	  try{
		  userID = new ArrayList<String>();
		  inUse = new ArrayList<String>();
		  file = new File("Users.txt");
		  
		  if(!file.exists()){
			  file.createNewFile();
		  }
		  br = new BufferedReader(new FileReader(file));
		  String line ="";
		  String[] userCred = null;
			while((line = br.readLine())!= null){
				userCred = line.split(":");
				userID.add(userCred[0]);
			}
	  }
	  catch(IOException e){
		  e.printStackTrace();
	  }
   }
		//	Generating unique key for every unique string name
   public String generateKey(String fullNameString) {
	   String serialNumberEncoded ="";
	   try{
			serialNumberEncoded = calculateHash(fullNameString,"MD2") +
			calculateHash(fullNameString,"MD5") +
			calculateHash(fullNameString,"SHA1");
	   }
	   catch(NoSuchAlgorithmException e){
		   System.out.println(e);
	   }
			//	Generating unique key by joining the hashed keys and selecting unique char
		String key = ""
		+ serialNumberEncoded.charAt(32)  + serialNumberEncoded.charAt(76)
		+ serialNumberEncoded.charAt(100) + serialNumberEncoded.charAt(50) + "-"
		+ serialNumberEncoded.charAt(2)   + serialNumberEncoded.charAt(91)
		+ serialNumberEncoded.charAt(73)  + serialNumberEncoded.charAt(72)
		+ serialNumberEncoded.charAt(98)  + "-"
		+ serialNumberEncoded.charAt(47)  + serialNumberEncoded.charAt(65)
		+ serialNumberEncoded.charAt(18)  + serialNumberEncoded.charAt(85) + "-"
		+ serialNumberEncoded.charAt(27)  + serialNumberEncoded.charAt(53)
		+ serialNumberEncoded.charAt(102) + serialNumberEncoded.charAt(15)
		+ serialNumberEncoded.charAt(99);
		
		return key;
   }
		//	Convert string name into a hashed key
   private String calculateHash(String stringInput, String algorithmName)
    throws NoSuchAlgorithmException {
        String hexMessageEncode = "";
        byte[] buffer = stringInput.getBytes();
        java.security.MessageDigest messageDigest =
            java.security.MessageDigest.getInstance(algorithmName);
        messageDigest.update(buffer);
        byte[] messageDigestBytes = messageDigest.digest();
        for (int index=0; index < messageDigestBytes.length ; index ++) {
            int countEncode = messageDigestBytes[index] & 0xff;
            if (Integer.toHexString(countEncode).length() == 1)
                hexMessageEncode = hexMessageEncode + "0";
            hexMessageEncode = hexMessageEncode + Integer.toHexString(countEncode);
        }
        return hexMessageEncode;
    }
		//	Registering new user into the system
	public String register(String username, String key){
		try{
				//	Do not allow registration of multiple client with same ID
			for(int i = 0;i<userID.size();i++){
				if(userID.get(i).equals(username)){
					return "User already exist";
				}
			}
				//	Store registered user to a file
			String data = "\n"+username+":"+key;
			fw = new FileWriter(file.getAbsoluteFile(), true);
			bw = new BufferedWriter(fw);
			bw.write(data);
			bw.newLine();
			userID.add(username);
			return "User Registration Successful.";
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try {
				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}
		}
		return "Registration failed. ";
	}
		// Client Login
	public boolean login(String username, String key){
		String calcKey = this.generateKey(username);
		String[] userCred = null;
		try{
				//	Check if user is a registered user
			for(int i=0;i<userID.size();i++){
				if(userID.get(i).equals(username)){
						//	Check if user is already logged in
					if(inUse.size()>0){
						for(int j=0;j<inUse.size();j++){
							if(inUse.get(j).equals(username)){
								System.out.println("User already logged in!");
								return false;
							}
						}
					}
					inUse.add(username);
					return true;
				}
			}
		}
		catch (Exception e) {
            e.printStackTrace();
        }
		System.out.println("Login failed :(");
		return false;
	}
		//	Example of remote resource that client is accessing 
	public int calcFac(int n){
		int fact = 1;
		for(int j = 0 ;j<inUse.size();j++){
			
		}
		 for(int i=n;i>=1;i--) {
		   fact = fact * i;
		 }
		 return fact;
	}
}