package si.unimb.feri.osgichat.webclient;

import si.unimb.feri.osgichat.ChatSession;

/**
 * Web chat session properties for storing the chatsession properties of the webclient. 
 */
public class WebChatSessionProperties{
	
	private ChatSession chatSession;
	
	private long lastMessageTime;
	
	/**
	 * Create a new instance
	 * @param chatSession Valid ChatSession of client
	 * @param lastMessageTime Last message time used for timeout
	 */
	public WebChatSessionProperties(ChatSession chatSession,long lastMessageTime){
		this.chatSession = chatSession;
		this.lastMessageTime = lastMessageTime;
	}
	
	/**
	 * Get ChatSession
	 * @return A valid ChatSession
	 */
	public ChatSession getChatSession() {
		return chatSession;
	}

	/**
	 * Get last message time
	 * @return long time in millis
	 */
	public long getLastMessageTime() {
		return lastMessageTime;
	}

	/**
	 * Set last message time
	 * @param lastMessageTime long time in millis
	 */
	public void setLastMessageTime(long lastMessageTime) {
		this.lastMessageTime = lastMessageTime;
	}

}
