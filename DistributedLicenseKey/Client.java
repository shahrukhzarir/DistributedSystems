import java.io.*;
import java.rmi.Naming;
import java.util.Scanner;

public class Client {
	static BufferedReader br;
	static KeyGen keyGen;
	
  public static void main(String[] args) {
		br = new BufferedReader(new InputStreamReader(System.in));
		Scanner sc=new Scanner(System.in);  
		try{
		keyGen = (KeyGen) Naming.lookup(KeyGen.SERVICENAME);
	}
	catch(Exception e){
		System.out.println("Lookup failed!!!");
	}
	  System.out.println("Enter q to quit");
	  String x = "";
	  String s = null;
	  boolean flag=true;
	  while(flag){
		System.out.println("Enter 1 to Register & 2 to Login");
		try{
			x = br.readLine();
		}catch(Exception e){
			System.out.println("Read failed!");
		} 
		  switch(x){
				case "1":	
					register();
					break;
				case "2":
					login();
					break;
				case "q":
					System.out.println("Quiting...");
					flag = false;
					break;
				default:
					break;
		  }
	  }
   }
   
   private static void login(){
	   String name ="";
	   String key = "";
	   try{
		   System.out.print("Enter your name: ");
		   name = br.readLine();
		   System.out.print("Enter serial key: ");
		   key = br.readLine();
		   if(keyGen.login(name, key)){
			   System.out.println("Welcome to the system...");
		   }
		   else
			   System.out.println("Access denied");
	   }
	   catch(IOException ioe){
		   System.err.println(ioe.getMessage());
	   }
   }
   
   private static void register(){
	   System.out.print("Enter your name: ");
		String s="";
	   
	   try {
         s = br.readLine();
      } catch(IOException ioe) {
         System.err.println(ioe.getMessage());
      }
     
      try {
        
        String fn = keyGen.generateKey(s);
		String msg = keyGen.register(s, fn);
        System.out.println("Generated Key: " + fn);
		System.out.println(msg);
      } catch(Exception e) {
        System.err.println("Remote exception: "+e.getMessage());
        e.printStackTrace();
      }
   }
}