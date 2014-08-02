import java.util.Random;
/** Project public class
 * 
 *  @author OOMS Aurélien <aureooms@ulb.ac.be>
 *  @version 2011.12.22
 */
public class Project {
	private static float networkReliability = 1;
	private static int numberOfAccounts = 2;
	private static int numberOfBanks = 1;
	private static int numberOfClients = 1;
	private static int numberOfCouplesOfRequests = 1;
	private static int maximumSuccessiveFailedAttempts = 1;
	
	/**Allows personalized output messages.*/
	private static Printer printer = new Printer("Projet",1);
	
	/**Simulates interactions between Client and Bank Threads on a Network Object with randomly set parameters.*/
	public static void main(String[] args){
		printer.output("start");
		setRandomParameters(100);
		Network network = new Network((float)networkReliability);
		Accounts accounts = randomCreateAccounts(numberOfAccounts);
		createBanks(numberOfBanks,accounts,network,maximumSuccessiveFailedAttempts);
		Client clients[] = createClients(numberOfClients,numberOfCouplesOfRequests,accounts,network,maximumSuccessiveFailedAttempts);
		for(Client client:clients)
			try{client.join();}catch(Exception e){}
		accounts.printVerification();
		printer.output("end");
	}

	/** Run Bank Threads from given parameters.
	 * 
	 *  @return an array with the generated Threads
	 */
	public static Bank[] createBanks(int n,Accounts accounts,Network network,int max_attempts){
		Bank banks [] = new Bank [n];
		int i = 0;
		while (i<n){
			Bank bank = new Bank(accounts,network,max_attempts);
			bank.start();
			banks[i] = bank;
			i++;
		}
		return banks;
	}

	/** Run Client Threads from given parameters.
	 * 
	 *  @return an array with the generated Threads
	 */
	public static Client[] createClients(int n, int operations,Accounts accounts,Network network,int max_attempts){
		Client clients [] = new Client [n];
		int i = 0;
		while (i<n){
			Client client = new Client(operations,new AccountNumbers(accounts),network,max_attempts);
			client.start();
			clients[i] = client;
			i++;
		}
		return clients;		
	}
	
	/** Create Account Objects from given parameters, the balance of each Account is randomly set.
	 * 
	 *  @return an array with the generated Threads
	 */
	public static Accounts randomCreateAccounts(int n){
		Accounts accounts = new Accounts();
		accounts.setRandom(n);
		accounts.printVerification();
		return accounts;
	}
	
	/** Randomly sets parameters.
	 *  @param n determines the scale of the simulation
	 */
	public static void setRandomParameters(int n){
		Random random = new Random();
		networkReliability = random.nextFloat();
		numberOfAccounts = random.nextInt(n)+2;
		numberOfClients = random.nextInt(n)+1;
		numberOfBanks = random.nextInt(n)+1;
		numberOfCouplesOfRequests = random.nextInt(n)+1;
		maximumSuccessiveFailedAttempts = random.nextInt(n)+1;
		printer.output("random parameters generated");
	}
}
