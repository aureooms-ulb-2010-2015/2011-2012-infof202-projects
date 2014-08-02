import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

/** Accounts class
 * 
 * @author OOMS Aurélien <aureooms@ulb.ac.be>
 * @version 2011.12.22
 */
class Accounts{
	
	/**Next instance Id.*/
	private static int nextInstanceId = 0;
	
	/**Instance Id assigned during construction.*/
	private int id = nextInstanceId++;

	/**Allows personalized output messages.*/
	private Printer printer = new Printer("Accounts",this.id); 

	/**Account Objects hash table*/
	private Hashtable<String,Account> accounts;
	
	/**account numbers array*/
	private String[] accountNumbers;
	
	/**Number of Account Objects in accounts.*/
	private int length = 0;

	/**Sum of all accounts Account Objects balance just after the execution of the setRandom method.*/
	private int totalAmount = 0;

	/** Accounts Object that allows to generate a given n number of Account Objects.
	 * 
	 * The generation method is setRandom.
	 */
	public Accounts(){
		this.printer.output("created");
	}
	
	/**
	 *  @param id id of the wanted Account Object
	 *  @return the Account Object which matches the id parameter
	 */
	public Account get(String id){
		return this.accounts.get(id);
	}
	
	/**
	 * @return the number of Account Objects in accounts
	 */
	public int length(){
		return this.length;
	}
	
	/** Generates a bunch of Account Objects stored in accounts.
	 * 
	 *  The number of Account Objects created depends on the length parameter.
	 *  
	 *  @param length number of Account Objects created
	 */
	public void setRandom(int length){
		this.totalAmount = 0;
		this.accounts = new Hashtable<String,Account>();
		this.accountNumbers = generateAccountNumbers(length);
		int i = 0;
		Random random = new Random();
		while (i<length){
			this.accounts.put(this.accountNumbers[i],new Account(this.accountNumbers[i], random.nextInt(3000)+1000));
			this.totalAmount += this.get(this.accountNumbers[i]).getBalance();
			i++;
		}
		this.length = length;
	}
	

	/**@return an array of the account numbers*/
	public String [] getAccountNumbers(){
		return this.accountNumbers;
	}

	/** Sums all of the accounts Account Objects balance.
	 * 
	 * @return the sum of all of the accounts Account Objects balance
	 */
	public synchronized int getRealTotalAmount(){
		int totalAmount = 0;
		for (Account account:this.accounts.values()){
			totalAmount += account.getBalance();
		}
		return totalAmount;
	}
	
	/**@return the sum of all of the accounts Account Objects balance resulting of the execution of the setRandom method*/
	public synchronized int getCreationTotalAmount(){
		return this.totalAmount;
	}
	
	/** Prints both totalAmount attribute and getRealTotalAmount method return value.
	 * 
	 *  The two printed value should be the same.
	 */
	public void printVerification(){
		this.printer.emph("totalAmount = "+this.getRealTotalAmount()+" @currentTime, "+this.getCreationTotalAmount()+" @creation");
	}
	
	/** Generate an array of distinct account numbers.
	 * 
	 *  This method should generate issues as we don't really check for account numbers duplicates.
	 *  However, this shouldn't happen since the simulation never asks for more than 1,000,000,000,000 Account Objects to be created.
	 * 
	 *  @param n number of account numbers to create
	 *  @return an array of n distinct account numbers
	 */
	private static String [] generateAccountNumbers(int n){
		
		int bankCodes[] = {77,75,85,11,78,16,41,53};
		Random random = new Random();
		String accountNumbers[] = new String[n];
		int i = 0;
		while (i<n){
			String lastNumbers = i+"";
			while (lastNumbers.length()<12){
				lastNumbers = "0"+lastNumbers;
			}
			accountNumbers[i] = "BE"+bankCodes[random.nextInt(bankCodes.length)]+lastNumbers;
			i++;
		}
		
		return accountNumbers;
	}
}





/** AccountNumbers class
 * 
 *  @author OOMS Aur�lien <aureooms@ulb.ac.be>
 *  @version 2011.12.22
 */
class AccountNumbers{
	
	/**number of stored account numbers*/
	private int length = 0;
	
	/**stored account numbers array*/
	private String accountNumbers[];
	
	/** Generates an Object storing a sublist of account numbers from an Accounts Object.
	 * 
	 *  @param accounts Accounts Object from which account numbers are randomly chosen
	 */
	public AccountNumbers(Accounts accounts){
		this.setRandom(accounts);
	}

	private void setRandom(Accounts accounts) {
		int length = accounts.length();
		ArrayList <String> accountNumbersLeft = new ArrayList<String>();
		int i = 0;
		while (i<length){
			accountNumbersLeft.add(accounts.getAccountNumbers()[i]);
			i++;
		}
		
		Random random = new Random();
		int j = 0;
		int n = random.nextInt(length-2)+2;
		this.accountNumbers = new String[n];
		this.length = n;
		while (j<n){
			this.accountNumbers[j] = accountNumbersLeft.remove(random.nextInt(accountNumbersLeft.size()));
			j++;
		}
		
	}

	public String get(int i) {
		return this.accountNumbers[i];
	}

	public int length() {
		return this.length;
	}
}


/** AccountCouple class
 * 
 *  @author OOMS Aur�lien <aureooms@ulb.ac.be>
 *  @version 2011.12.22
 */
class AccountCouple{
	private AccountNumbers accountNumbers;
	private String debtor;
	private String creditor;

	/** AccountCouple Object that allows to generate a random couple of Account Object from an Accounts Object.
	 *  
	 *  @param accountNumbers the AccountNumbers Object from which account numbers are randomly chosen
	 */
	public AccountCouple(AccountNumbers accountNumbers){
		this.accountNumbers = accountNumbers;
	}
	
	/** Generate a random couple of Account Object.
	 * 
	 *  Randomly assigns debtor and creditor, they have to be different.
	 * 
	 *  @return the Object itself
	 */
	public AccountCouple getNextCouple(){
		
		int length = this.accountNumbers.length();
		ArrayList <String> accountsLeft = new ArrayList<String>();
		int i = 0;
		while (i<length){
			accountsLeft.add(this.accountNumbers.get(i));
			i++;
		}
		
		Random random = new Random();
		this.debtor = accountsLeft.remove(random.nextInt(accountsLeft.size()));
		this.creditor = accountsLeft.remove(random.nextInt(accountsLeft.size()));
		return this;
	}

	/**
	 * @return the debtor Id
	 */
	public String getDebtor(){
		return this.debtor;
	}
	
	/**
	 * @return the creditor Id
	 */
	public String getCreditor(){
		return this.creditor;
	}

}