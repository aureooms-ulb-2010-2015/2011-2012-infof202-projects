import java.io.IOException;
import java.lang.Thread;

/** Bank Thread.
 * 
 *  @author OOMS Aurélien <aureooms@ulb.ac.be>
 *  @version 2011.12.22
 */
class Bank extends Thread{
	
	/**Next instance Id.*/
	private static int nextInstanceId = 0;
	
	/**Instance Id assigned during construction.*/
	private int id = nextInstanceId++;
	
	/**Allows personalized output messages.*/
	private Printer printer = new Printer("Bank",this.id); 
	
	/**Accounts managed by the bank.*/
	private Accounts accounts;
	
	/**Network the bank communicates over.*/
	private Network network;
	
	/**Number of successive failed network connection attempts.*/
	private int successiveFailedAttempts = 0;
	
	/**Maximum number of successive failed network connection attempts before giving up the task.*/
	private int max_attempts;

	/** Thread handling Request Objects sent on a Network Object.
	 * 
	 *  The Request Objects are sent by Client threads. If there's no Request Object
	 *  on the Network Object, it waits until there is one.
	 *  
	 *  @param accounts list of Account Objects which Request Objects are concerning
	 *  @param network Network Object which on Request Objects are sent
	 *  @param max_attempts maximum number of successive failed attempts to connect to the Network Object
	 * 
	 */
	public Bank(Accounts accounts, Network network, int max_attempts){
		this.accounts = accounts;
		this.network = network;
		this.max_attempts = max_attempts;
		this.printer.output("created");
		this.setDaemon(true);
	}
	
	/**Handles tasks until network is down*/
	public void run(){
		while (true){
			try{
				Request request = this.getRequest();
				this.printer.output("handling request");
				request.handle(this.accounts);
			}catch(NetworkDownException e){
				this.printer.error("NetworkDownException");
				break;
			}
		}
		this.printer.output("I stop working");
	}
	/** Asks network for a new Request to handle.
	 * 
	 *  @return Request Object
	 *  @throws NetworkDownException
	 */
	public Request getRequest() throws NetworkDownException{
		this.resetSuccessiveFailedAttempts();
		do{
			try{			
				this.printer.output("waiting for request");
				return this.network.getRequest();			
			}catch(IOException e){			
				this.incrementSuccessiveFailedAttempts();			
				if (!(this.successiveFailedAttempts<this.max_attempts)){
					
					this.printer.output("Network is down, I stop trying getting requests from it");
					throw new NetworkDownException();	
				}	
			}		
		}while(true);
	}
	
	/**Resets successive failed attempts*/
	public void resetSuccessiveFailedAttempts(){
		this.successiveFailedAttempts = 0;
	}
	
	/**Increments successive failed attempts*/
	public void incrementSuccessiveFailedAttempts(){
		this.successiveFailedAttempts++;
	}
}