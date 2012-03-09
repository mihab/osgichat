package si.unimb.feri.osgichat;

/**
 * ChatService is the clients entry point to the chat server. Clients login using a desired nickname
 * providing their ChatHandler implementation on which they receive callbacks. Upon successful login
 * clients receive a ChatSession object, which is how the ChatService keeps track of connected clients
 * and needs to be provided by the client upon each later invocation of the ChatService. ChatService
 * also allows registration of nicknames using a password, which must then be provided on the next
 * login to the ChatService.
 */
public interface ChatService {
	
	/**
	 * Login to ChatService using given nickname. Clients also have to supply an implementation of the
	 * ChatHandler interface on which they wish to receive callbacks. Upon successful login the ChatService
	 * return a valid ChatSession with the given settings set. 
	 * @param nickname Nickname of user
	 * @param chatHandler An implementation of the ChatHandler interface to receive callbacks
	 * @return Valid client ChatSession object
	 * @throws ChatException If given nickname has been taken already or is registered
	 */
	ChatSession login(String nickname,ChatHandler chatHandler) throws ChatException;
	
	/**
	 * Login to ChatService using given nickname and password. The nickname must've been previously
	 * registered with this ChatService for the login to succeed.
	 * @param nickname Nick of user
	 * @param password Password for nickname
	 * @param chatHandler An implementation of the ChatHandler interface to receive callbacks
	 * @return Valid client ChatSession object
	 * @throws ChatException If given nickname has been taken already
	 */
	ChatSession login(String nickname,String password,ChatHandler chatHandler) throws ChatException;
	
	/**
	 * Logout from ChatService.
	 * @param chatSession A valid client ChatSession object
	 */
	void logout(ChatSession chatSession);
	
	/**
	 * Register the current nickname with the desired password
	 * @param password Password for nickname
	 * @param chatSession A valid client ChatSession object
	 */
	void registerNickname(String password,ChatSession chatSession);
	
	/**
	 * Change password for currently registered nickname
	 * @param newPassword New password for nickname
	 * @param chatSession A valid client ChatSession object
	 */
	void changePassword(String newPassword,ChatSession chatSession);
	
	/**
	 * Join a channel and subscribe to messages on it.
	 * @param channel Channel name to join
	 * @param chatSession A valid client ChatSession object
	 */
	void joinChannel(String channel,ChatSession chatSession);
	
	/**
	 * Leave a channel and unsubscribe from it.
	 * @param channel Channel name to leave
	 * @param chatSession A valid client ChatSession object
	 */
	void leaveChannel(String channel,ChatSession chatSession);
	
	/**
	 * Send private message to other user
	 * @param toNickname Nickname of other user
	 * @param message Message to send
	 * @param chatSession A valid client ChatSession object
	 */
	void sendPrivateMessage(String toNickname,String message,ChatSession chatSession);
	
	/**
	 * Send message to channel
	 * @param toChannel Name of channel
	 * @param message Message to send
	 * @param chatSession A valid client ChatSession object
	 */
	void sendChannelMessage(String toChannel,String message,ChatSession chatSession);
	
	/**
	 * Get ignored users list for current ChatSession
	 * @param chatSession A valid client ChatSession object
	 * @return Current ignored users list, can be empty
	 */
	String[] getIgnoredUsers(ChatSession chatSession);
	
	/**
	 * Add a nickname to the ignored users list
	 * @param nicknameToIgnore Nickname to add to the ignored users list
	 * @param chatSession A valid client ChatSession object
	 * @return Current ignored users list
	 */
	String[] ignoreUser(String nicknameToIgnore,ChatSession chatSession);
	
	/**
	 * Remove a nickname from the ignored users list
	 * @param nicknameToUnignore Nickname to remove from the ignored users list
	 * @param chatSession A valid client ChatSession object
	 * @return Current ignored users list, can be empty
	 */
	String[] unignoreUser(String nicknameToUnignore,ChatSession chatSession);

}
