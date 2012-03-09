package si.unimb.feri.osgichat.hook;

import si.unimb.feri.osgichat.ChatService;

/**
 * Hook class that receives a ChatService implementation from the OSGi framework and which 
 * the web application can then retrieve statically 
 */
public class Hook {
	
	/**
	 * Chat service
	 */
	private static ChatService chatService;
	
	/**
	 * Set chat service
	 * @param chatService ChatService to use
	 */
	public static void setChatService(ChatService chatService){
		Hook.chatService = chatService;
	}
	
	/**
	 * Unset chat service
	 * @param chatService ChatService to unset
	 */
	public static void unsetChatService(ChatService chatService){
		Hook.chatService = null;
	}
	
	/**
	 * Get chat service
	 * @return ChatService or null if no ChatService available
	 */
	public static ChatService getChatService(){
		return Hook.chatService;
	}
}