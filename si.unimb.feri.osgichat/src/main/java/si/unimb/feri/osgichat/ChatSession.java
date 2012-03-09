package si.unimb.feri.osgichat;

/**
 * ChatSession the client receives upon successful login to ChatService and needs to provide upon each
 * later invocation of the ChatService. 
 */
public interface ChatSession {
	
	/**
	 * Returns the client nickname
	 * @return String nickname
	 */
	String getNickname();
	
}
