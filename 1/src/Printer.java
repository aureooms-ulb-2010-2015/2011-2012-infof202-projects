import java.util.Calendar;

/** Printer class
 * 
 *  @author OOMS Aur�lien <aureooms@ulb.ac.be>
 *  @version 2011.12.22
 */
class Printer {
	
	/**Id for next printed error message.*/
	private static int nextErrorInstanceId = 0;
	
	/**Name of the user*/
	private String user;
	
	/** Class that allows to print structured messages.
	 *  
	 *  The general canvas of a message is HOUR - USER - CONTENT.
	 *  
	 *  @param type type of the Object using the Printer class
	 *  @param id id of the Object using the Printer class
	 */
	public Printer(String type,int id){
		this.user = type+"#"+id;
	}
	
	/** Class that allows to print structured messages.
	 *  
	 *  The general canvas of a message is HOUR - USER - CONTENT.
	 *  
	 *  @param type type of the Object using the Printer class
	 *  @param id id of the Object using the Printer class
	 */
	public Printer(String type,String id){
		this.user = type+"#"+id;
	}

	/** Print a message as an error message.
	 * 
	 *  @param message message to print
	 */
	public void error(String message){
		int id = Printer.nextErrorInstanceId++;
		System.out.println(this.clock()+" ERROR #"+id+"\n         "+this.user+" : "+message);
	}
	
	/** Print a message as a regular message.
	 * 
	 *  @param message message to print
	 */
	public void output(String message){
		System.out.println(this.clock()+" "+this.user+" : "+message);
	}

	/** Print a message as a contrasted message.
	 * 
	 *  @param message message to print
	 */
	public void emph(String message){
		System.out.println(this.clock()+" "+this.user+" :                            "+message);
	}
	
	/**
	 *  @return the String representation of the actual hour
	 */
	public String clock(){
		Calendar time= Calendar.getInstance();
		String hours = ((time.get(Calendar.HOUR)+"").length()>1)?time.get(Calendar.HOUR)+"":"0"+time.get(Calendar.HOUR);
		String minutes = ((time.get(Calendar.MINUTE)+"").length()>1)?time.get(Calendar.MINUTE)+"":"0"+time.get(Calendar.MINUTE);
		String seconds = ((time.get(Calendar.SECOND)+"").length()>1)?time.get(Calendar.SECOND)+"":"0"+time.get(Calendar.SECOND);
		String milliseconds = ((time.get(Calendar.MILLISECOND)+"").length()>2)?time.get(Calendar.MILLISECOND)+"":(("0"+time.get(Calendar.MILLISECOND)).length()>2)?("0"+time.get(Calendar.MILLISECOND)):("00"+time.get(Calendar.MILLISECOND));
		String clock = hours+":"+minutes+":"+seconds+"."+milliseconds;
		return clock;
	}
}
