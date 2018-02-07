import javax.xml.datatype.Duration;
import java.math.BigInteger;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Time;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by shahrukhzarir on 17-11-09.
 */
public class FactorImpl extends UnicastRemoteObject implements Factor, Runnable{

    //simple set up
    private int packages;
    private BigInteger factor;
    private long limit;
    boolean complete = false;

    //clients specific
    private ArrayList<FactorClient> clientList;

    //organized packages
    private Set<Package> checked;
    private Set<Package> toCheck;
    private Set<Package> currentlyChecking;
    private HashMap<FactorClient, Package> clientsPackage;

    //thread related
    ExecutorService starter;
    long start;



    public FactorImpl() throws RemoteException{
        super();
        //client variable startup
        clientList = new ArrayList<FactorClient>();

        //start thread helper
        starter = Executors.newSingleThreadExecutor();
        start = System.currentTimeMillis();

        //packages start up
        clientsPackage = new HashMap<FactorClient,Package>();
        checked = new HashSet<Package>();
        toCheck = new HashSet<Package>();
        currentlyChecking = new HashSet<Package>();
    }

    public FactorImpl(BigInteger factor, int packages) throws RemoteException {
        this();

        //update variables after start up
        this.factor = factor;
        this.packages = packages;

        //setup packages
        this.limit = findMax(factor);
        long starting =2;
        long ending = limit/packages;

        long range = limit/packages;
        //set up new packages
        for (int i = 0; i < packages; i++) {
            toCheck.add(new Package(starting, ending));
            starting = ending+1;
            ending +=range;
        }

        //start thread
        start();

    }

    //helper function to assist in finding square root
    //max prime factor can only be square root of the big integer
    public long findMax(BigInteger number) throws IllegalArgumentException{
        //trivial checks
        if (number == BigInteger.ZERO || number == BigInteger.ONE) {
            return number.longValue();
        }

        //check non-trivial square roots
        BigInteger two = BigInteger.valueOf(2L);
        BigInteger y;
        // starting with y = x / 2 avoids magnitude issues with x squared
        for (y = number.divide(two); y.compareTo(number.divide(y)) > 0; y = ((number.divide(y)).add(y)).divide(two));
        if (number.compareTo(y.multiply(y)) == 0) {
            return y.longValue();
        } else {
            return (y.add(BigInteger.ONE)).longValue();
        }
    }

    //start the thread
    public void start(){
        new Thread(this).start();
    }

    //run the threads as clients
    public void run(){
        while (!complete){
            try {
                Thread.sleep(80000);
            } catch (InterruptedException e){
                System.out.println(e.getMessage());
            }

        Iterator clients = clientList.iterator();
            while (clients.hasNext()) {
                FactorClient newClient = (FactorClient) clients.next();

                //exhausting the client checker
                try{
                    Future<Integer> exhausted = starter.submit(new Callable(){
                        @Override
                        public Integer call() throws Exception{
                            return 0;
                        }});

                    //see if exhausted
                    try {
                        exhausted.get(5, TimeUnit.MILLISECONDS);
                    } catch(Exception e) {
                        deleteClient(newClient);
                        exhausted.cancel(true);
                        clients.remove();
                        continue;
                    }
                } catch  (Exception l){
                    deleteClient(newClient);
                    clients.remove();
                    continue;
                }
            }
        }
    }

    //computes the factors of the workload the clients have
    public void getClientInfo(FactorClient client) throws RemoteException {
        //when the packages are done
        //trivial
        if (toCheck.size()  == 0 ){
            return;
        }
        //adds a list of client
        clientList.add(client);

        //check all packages
        while (!toCheck.isEmpty()) {
            Package nextPackage = toCheck.iterator().next();
            toCheck.remove(nextPackage);
            currentlyChecking.add(nextPackage);
            clientsPackage.put(client, nextPackage);

            //check answers
            long potentialAnswer = client.factorize(nextPackage, factor);
            //exhausted all numbers in the package
            if (potentialAnswer == -1) {
                currentlyChecking.remove(nextPackage);
                checked.add(nextPackage);
                clientsPackage.remove(client);
            }

            //this is the answer
            else {
                output(client, potentialAnswer);
            }
        }
    }

    /*
    deletes client if exhausted and resets it
     */
    public void deleteClient(FactorClient client) {
        Package exhausted = clientsPackage.get(client);
        currentlyChecking.remove(exhausted);
        toCheck.add(exhausted);
        clientsPackage.remove(client);
    }

    //outputs all the stated/required information
    public void output(FactorClient client, long answer) throws RemoteException {
        complete = true;
        System.out.println("CLIENT #" + (clientList.indexOf(client)+1) + " FOUND A PAIR OF FACTORS");
        System.out.println(answer + " * " + factor.divide(BigInteger.valueOf(answer)) + " = "  + answer);
        System.out.println("Number of Clients used" + clientList.size());
        System.out.println("Percentage of Space Examined: " + ((checked.size()) * 100/packages));
        System.out.print("Time Taken: " + ((new Date()).getTime()- start)/1000 + "seconds");
    }
}
