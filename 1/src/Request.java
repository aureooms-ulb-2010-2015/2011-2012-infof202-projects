/** BalanceRequest class
 * 
 * @author OOMS Aurélien <aureooms@ulb.ac.be>
 * @version 2011.12.22
 */
class BalanceRequest extends Request{
	
	/**Next instance Id.*/
	private static int nextInstanceId = 0;
	
	/**Instance Id assigned during construction.*/
	private int id = nextInstanceId++;

	/**Allows personalized output messages.*/
	private Printer printer = new Printer("BalanceRequest",this.id); 
	
	/**Id of the account.*/
	private String account;
	
	/**Field containing the result of the request.*/
	private int balance;

	/** Request sent on a Network object by a Client thread and handled by a Bank thread.
	 * 
	 *  Request sent on a Network object by a Client thread and handled by a Bank thread.
	 *  The Client thread that sent it always wait for it to be handled or timeout.
	 *  This Request object requests the balance of one Account object from wich id is given.
	 *  
	 *  @param account id of the account from which balance is requested
	 * 
	 */
	public BalanceRequest(String account){
		this.account = account;
		this.printer.output("created");
	}
	
	/**@return the result of the request*/
	public int getBalance(){
		return this.balance;
	}
	
	
	public void handle(Accounts accounts){
		this.balance = accounts.get(this.account).getBalance();
		this.printer.output("balance = "+this.balance);
		this.setSucceeded();
		synchronized(this){
			this.notify();
		}
		this.printer.output("handled");
	}
}

/** TransferRequest class
 * 
 * @author OOMS Aur�lien <aureooms@ulb.ac.be>
 * @version 2011.12.22
 */
class TransferRequest extends Request{
	
	/**Next instance Id.*/
	private static int nextInstanceId = 0;
	
	/**Instance Id assigned during construction.*/
	private int id = nextInstanceId++;
	
	/**Allows personalized output messages.*/
	private Printer printer = new Printer("TransferRequest",this.id); 
	
	/**Id of the debtor Account Object.*/
	private String debtor;
	
	/**Transfer amount.*/
	private int amount;
	
	/**Id of the creditor Account Object.*/
	private String creditor;

	/** Request sent on a Network object by a Client thread and handled by a Bank thread.
	 * 
	 *  Request sent on a Network object by a Client thread and handled by a Bank thread.
	 *  The Client thread that sent it always wait for it to be handled or timeout.
	 *  This Request object transfers a given amount from one given account to another given one.
	 *  
	 *  @param debtor id of the account from which given amount is removed
	 *  @param amount amount that is being transfered
	 *  @param creditor id of the account from which given amount is added
	 * 
	 */
	public TransferRequest(String debtor,int amount,String creditor){
		this.debtor = debtor;
		this.amount = amount;
		this.creditor = creditor;
		this.printer.output("created");
	}
	

	public void handle(Accounts accounts){
		try{
			accounts.get(this.debtor).withdraw(this.amount);
			accounts.get(this.creditor).deposit(this.amount);
			this.setSucceeded();
		}catch(LowBalanceException e){
			this.setFailed();
		}
		synchronized(this){
			this.notify();
		}
		this.printer.output("handled");
	}
}

/** Request abstract class
 * 
 * @author OOMS Aur�lien <aureooms@ulb.ac.be>
 * @version 2011.12.22
 */
abstract class Request{

	/**Next instance Id.*/
	private static int nextInstanceId = 0;
	
	/**Instance Id assigned during construction.*/
	private int id = nextInstanceId++;
	
	/**Status Object used to know whether or not the Request has been handled.*/
	private Status status = new Status();
	
	/**Number of successive failed handling attempts.*/
	private int attempts = 0;

	/** Abstract class of a Request object sent on a Network object by a Client thread and handled by a Bank thread.
	 * 
	 *  It principally establish the concept of Request object status and Network Object connecting attempts.
	 * 
	 */
	public Request(){}
	
	/** Handles the request.
	 * 
	 *  @param accounts Accounts Object containing the Account Object(s) needed for handling
	 */
	public abstract void handle(Accounts accounts);
	
	/**Increments the number of successive failed handling attempts for this Request Object.*/
	public void incrementAttempts(){
		this.attempts++;
	}
	
	/**@return the number of successive failed handling attempts*/
	public int attempts(){
		return this.attempts;
	}
	
	/**Sets Status field to "not sent".*/
	public void setNotSent(){
		this.status.setNotSent();
	}
	
	/**Sets Status field to "processing".*/
	public void setProcessing(){
		this.status.setProcessing();
	}

	/**Sets Status field to "succeeded".*/
	public void setSucceeded(){
		this.status.setSucceeded();
	}

	/**Sets Status field to "failed".*/
	public void setFailed(){
		this.status.setFailed();
	}
	
	/**@return whether or not the Request Object is not sent*/
	public boolean notSent(){
		return this.status.notSent();
	}

	/**@return whether or not the Request Object is processing*/
	public boolean processing(){
		return this.status.processing();
	}

	/**@return whether or not the Request Object has succeeded*/
	public boolean succeeded(){
		return this.status.succeeded();
	}

	/**@return whether or not the Request Object has failed*/
	public boolean failed(){
		return this.status.failed();
	}

	/**@return whether or not the Request Object is sent*/
	public boolean sent(){
		return this.status.sent();
	}
	
	/**@return whether or not the Request Object has been handled*/
	public boolean handled(){
		return this.status.handled();
	}

	/**@return the Id of the Request Object*/
	public int getId(){
		return this.id;
	}
}

/** Status class
 * 
 *  @author OOMS Aur�lien <aureooms@ulb.ac.be>
 *  @version 2011.12.22
 */
class Status{
	
	/**Defines the value for a "not sent" status.*/
	private static int NOTSENT = 0;
	
	/**Defines the value for a "processing" status.*/
	private static int PROCESSING = 1;
	
	/**Defines the value for a "succeeded" status.*/
	private static int SUCCEEDED = 2;
	
	/**Defines the value for a "failed" status.*/
	private static int FAILED = 3;
	
	/**Defines the actual Status tag.*/
	private int tag;
	
	/** Used in Request Objects to handle status interactions.*/
	public Status(){
		this.setNotSent();
	}
	
	/**Sets tag field to "not sent".*/
	public void setNotSent(){
		this.tag = Status.NOTSENT;
	}
	
	/**Sets tag field to "processing".*/
	public void setProcessing(){
		this.tag = Status.PROCESSING;
	}
	
	/**Sets tag field to "succeeded".*/
	public void setSucceeded(){
		this.tag = Status.SUCCEEDED;
	}
	
	/**Sets tag field to "failed".*/
	public void setFailed(){
		this.tag = Status.FAILED;
	}
	
	/**@return whether or not the Status is "not sent"*/
	public boolean notSent(){
		return this.tag == Status.NOTSENT;
	}
	
	/**@return whether or not the Status is "processing"*/
	public boolean processing(){
		return this.tag == Status.PROCESSING;
	}
	
	/**@return whether or not the Status is "succeeded"*/
	public boolean succeeded(){
		return this.tag == Status.SUCCEEDED;
	}
	
	/**@return whether or not the Status is "failed"*/
	public boolean failed(){
		return this.tag == Status.FAILED;
	}
	
	/**@return whether or not the Status is "sent"*/
	public boolean sent(){
		return !this.notSent();
	}
	
	/**@return whether or not the Status is "handled"*/
	public boolean handled(){
		return (this.failed() || this.succeeded());
	}
}