import java.io.IOException;
import java.lang.Thread;
import java.util.Random;

/** Client thread
 * 
 * @author OOMS Aurélien <aureooms@ulb.ac.be>
 * @version 2011.12.22
 */
class Client extends Thread{
	
	/**Next instance Id.*/
	private static int nextInstanceId = 0;
	
	/**Instance Id assigned during construction.*/
	private int id = nextInstanceId++;

	/**Allows personalized output messages.*/
	private Printer printer = new Printer("Client",this.id); 
	
	/**Number of couple of Request Objects sent.*/
	private int n;
	
	/**Accounts Object storing Account Objects which Request Objects are concerning.*/
	private AccountNumbers accountNumbers;
	
	/**Network Object which on Request Objects are sent.*/
	private Network network;
	
	/**Maximum number of successive failed attempts to connect to the Network Object.*/
	private int max_attempts;

	/** Thread sending Request Objects on a Network Object.
	 * 
	 *  Thread sending Request Objects on a Network Object,
	 *  the Request Objects are handled by Bank threads. The Client thread
	 *  always waits that the previous Request Object has been handled before
	 *  sending another one.
	 *  
	 *  @param n number of couple of Request Objects sent
	 *  @param accountNumbers AccountNumbers Object storing account numbers which Request Objects are concerning
	 *  @param network Network Object which on Request Objects are sent
	 *  @param max_attempts maximum number of successive failed attempts to connect to the Network Object
	 * 
	 */
	public Client(int n, AccountNumbers accountNumbers, Network network, int max_attempts){
		this.n = n;
		this.accountNumbers = accountNumbers;
		this.network = network;
		this.max_attempts = max_attempts;
		this.printer.output("created");
	}
	
	/** Sends n couple of Request Objects on the Network.
	 * 
	 *  Stops when done or Network found down.
	 */
	public void run(){
		int i = 0;
		Random random = new Random();
		AccountCouple accountCouple = new AccountCouple(this.accountNumbers);
		while (i<this.n){
			accountCouple.getNextCouple();
			String debtor = accountCouple.getDebtor();
			String creditor = accountCouple.getCreditor();
			
			try{
				this.printer.output("create balance request");
				BalanceRequest balanceRequest = this.sendBalanceRequest(debtor);
				this.printer.output("balance request has been handled");
				if (balanceRequest.succeeded() && balanceRequest.getBalance() > 0){
					this.printer.output("create transfer request");
					int amount = random.nextInt(balanceRequest.getBalance())+1;
					this.sendTransferRequest(debtor, amount, creditor);
					this.printer.output("transfer request has been handled");
				}
			}catch(NetworkDownException e){
				this.printer.output("NetworkDown, I stop sending requests");
				return;
			}catch(RequestTimeoutException e){
				this.printer.output("Last request timed out, I stop sending requests");
				return;
			}
			i++;
		}
		this.printer.output("All requests sent, see ya");
	}
	
	/** Sends a BalanceRequest on the Network and waits for its completion.
	 * 
	 *  @param account Id of the BalanceRequest concerned Account
	 *  @return the BalanceRequest generated
	 *  @throws NetworkDownException if Network is found down
	 *  @throws RequestTimeoutException if BalanceRequest timed out
	 */
	public BalanceRequest sendBalanceRequest(String account) throws NetworkDownException, RequestTimeoutException{
		
		BalanceRequest balanceRequest = new BalanceRequest(account);
		
		do {
			try{
				this.network.sendRequest(balanceRequest);
			}catch(IOException e){
				balanceRequest.incrementAttempts();
				if (!(balanceRequest.attempts()<this.max_attempts)){
					throw new NetworkDownException();
				}
			}
		}while(balanceRequest.notSent());
		return balanceRequest;
	}	
	/** Sends a TransferRequest on the Network and waits for its completion.
	 * 
	 * @param debtor Id of the BalanceRequest concerned debtor Account
	 * @param amount amount of the transaction
	 * @param creditor Id of the BalanceRequest concerned creditor Account
	 * @return the TransferRequest generated
	 * @throws NetworkDownException if Network is found down
	 * @throws RequestTimeoutException if TransfeRequest timed out
	 */
	public TransferRequest sendTransferRequest(String debtor, int amount, String creditor) throws NetworkDownException, RequestTimeoutException{

		TransferRequest transferRequest = new TransferRequest(debtor,amount,creditor);
		do {
			try{
				this.network.sendRequest(transferRequest);
			}catch(IOException e){
				transferRequest.incrementAttempts();
				if (!(transferRequest.attempts()<this.max_attempts)){
					throw new NetworkDownException();
				}
			}
		}while(transferRequest.notSent());
		
		return transferRequest;
	}
}