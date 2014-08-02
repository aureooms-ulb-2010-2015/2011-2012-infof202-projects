/** Account class
 * 
 *  @author OOMS Aurélien <aureooms@ulb.ac.be>
 *  @version 2011.12.22
 */
class Account{
	
	/**Id of the account.*/
	private String id;
	
	/**Allows personalized output messages.*/
	private Printer printer;
	
	/**Actual balance of the account.*/
	private int balance;

	/** Account object that stores a balance and an id.
	 * 
	 * The available methods are :
	 * deposit(), withdraw() and getBalance().
	 * 
	 * @param id id of the Account object
	 * @param amount starting balance of the Account object
	 */
	public Account(String id, int amount){
		this.id = id;
		this.printer = new Printer("Account",this.id);
		this.balance = amount;
		this.printer.output("created");
	}

	/** Increments the balance of a certain amount.
	 * 
	 *  @param amount deposit amount
	 */
	public synchronized void deposit(int amount){
		this.balance += amount;
		this.printer.output(amount+" deposit, actual balance is "+this.balance);
	}

	/** Decrements the balance of a certain amount.
	 * 
	 *  @param amount withdraw amount
	 *  @throws LowBalanceException if Account Object's balance is too low
	 */
	public synchronized void withdraw(int amount) throws LowBalanceException{
		if(amount>this.balance){
			this.printer.error(amount+" withdraw generated LowBalanceException");
			throw new LowBalanceException();
		}
		this.balance -= amount;
		this.printer.output(amount+" withdraw, actual balance is "+this.balance);
	}
	
	/**
	 * @return Account Object's actual balance
	 */
	public synchronized int getBalance(){
		return this.balance;
	}
	
	/**
	 * @return Account Object's Id
	 */
	public String getId(){
		return this.id;
	}
}