package si.unimb.feri.osgichat;

/**
 * Checked exception that can be thrown by the ChatService. 
 */
public class ChatException extends Exception {
	
	public static final int NICKNAME_TAKEN_EXCEPTION = 1;
	
	public static final int NICKNAME_REGISTERED_EXCEPTION = 2;
	
	public static final int NICKNAME_NOT_REGISTERED_EXCEPTION = 3;
	
	public static final int WRONG_PASSWORD_EXCEPTION = 4;
	
	private int type;
	
	/**
	 * Creates an instance of the exception
	 * @param type Type of ChatException, should be one of the predefined integer values of the ChatException class
	 */
	public ChatException(int type){
		this.type = type;
	}
	
	/**
	 * Creates an instance of the exception
	 * @param type Type of ChatException, should be one of the predefined integer values of the ChatException class
	 * @param message Message associated with the exception
	 */
	public ChatException(int type,String message){
		super(message);
		this.type = type;
	}
	
	/**
	 * Creates an instance of the exception
	 * @param type Type of ChatException, should be one of the predefined integer values of the ChatException class
	 * @param message Message associated with the exception
	 * @param cause the originating exception
	 */
	public ChatException(int type,String message,Throwable cause){
		super(message,cause);
		this.type = type;
	}
	
	/**
	 * Get message type
	 * @return Type of ChatException, should be one of the predefined integer values of the ChatException class 
	 */
	public int getType(){
		return type;
	}

}
