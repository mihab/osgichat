package si.unimb.feri.osgichat;

/**
 * ChatHandler interface which clients need to implement to deal with the ChatService callbacks.
 * Client need to provide an implementation of this interface to the ChatService upon login.  
 */
public interface ChatHandler {
	
	/**
	 * Received private message.
	 * @param fromNickname Nickname the message came from
	 * @param message Message string received
	 */
	void privateMessageReceived(String fromNickname,String message);
	
	/**
	 * Received channel message.
	 * @param fromChannel Channel message was received
	 * @param fromNickname Nickname of user who sent the message
	 * @param message Message string received
	 */
	void channelMessageReceived(String fromChannel,String fromNickname,String message);
	
	/**
	 * Subscribers to channel changed
	 * @param channel Channel whose subscribers changed
	 * @param subscribers List of new subscribers
	 */
	void channelSubscribersChanged(String channel,String[] subscribers);

}
