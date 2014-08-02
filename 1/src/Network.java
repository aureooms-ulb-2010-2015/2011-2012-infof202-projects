import java.util.ArrayList;
import java.util.Random;
import java.io.IOException;

/** Network class
 * 
 *  @author OOMS Aurélien <aureooms@ulb.ac.be>
 *  @version 2011.12.22
 */
class Network{
	
	/**Next instance Id.*/
	private static int nextInstanceId = 0;
	
	/**Instance Id assigned during construction.*/
	private int id = nextInstanceId++;

	/**Allows personalized output messages.*/
	private Printer printer = new Printer("Network",this.id); 
	
	/**ArrayList object wherein waiting Request Object instance are stored.*/
	private ArrayList<Request> queue = new ArrayList<Request>();
	
	/**Probability of generating an IOException while an Object tries to use the Network Object.*/
	private float reliability;
	
	/**Time in milliseconds after which requests are considered as "timed out".*/
	private int timeout = 10000;
	
	/**Maximum number of timeouts for a single Request before throwing an exception.*/
	private int max_timeout = 3;

	/** Network object on which Request objects are sent.
	 * 
	 *  The Request objects are stored in an ArrayList object.
	 *  
	 *  @param reliability the probability of touching the Network object for each interaction
	 */
	public Network(float reliability){
		this.reliability = reliability;
		this.printer.output("created");
	}

	/** Method called by a Client Thread that puts a Request Object at the end of the queue and waits for its completion.
	 * 
	 *  @param request Request Object to put in the queue
	 *  @throws IOException if the Client failed to touch the Network
	 *  @throws RequestTimeoutException if the Request is considered as "timed out"
	 */
	public void sendRequest(Request request) throws IOException, RequestTimeoutException{
		this.reliabilityTest();
		this.queue.add(request);
		request.setProcessing();
		synchronized(this.queue){
			this.queue.notify();
		}
		int wokeUp = 0;
		while (!request.handled() && wokeUp<this.max_timeout){
			synchronized(request){
				try{
					request.wait(timeout);
					wokeUp += 1;
				}catch(InterruptedException e){}
			}
		}
		if (!request.handled() && !(wokeUp<this.max_timeout)){
			this.printer.output("Request timed out "+this.max_timeout+" times, I throw an Exception");
			throw new RequestTimeoutException();
		}
	}
	
	/** Method called by a Bank Thread that waits for a non-empty queue and returns the first Request of the queue.
	 * 
	 *  @return the first Request of the queue
	 *  @throws IOException if the Client failed to touch the Network
	 */
	public synchronized Request getRequest() throws IOException{
		this.reliabilityTest();
		while (this.queue.isEmpty()){
			synchronized(this.queue){
				try{
					this.queue.wait();
					this.printer.output("take this request");
					return this.queue.remove(0);
				}catch(InterruptedException e){}
			}
		}

		this.printer.output("take this request");
		return this.queue.remove(0);
	}
	
	/** Determines whether or not Network has been reached.
	 * 
	 *  @throws IOException if Network hasn't been reached
	 */
	private void reliabilityTest() throws IOException{
		Random random = new Random();
		if(random.nextInt(99)+1 < (1-this.reliability)*100){
			this.printer.output("Failed to connect");
			throw new IOException();
		}
		this.printer.output("Connect succeeded");
	}
}