package si.unimb.feri.osgichat.hook.internal;

import si.unimb.feri.osgichat.ChatService;
import si.unimb.feri.osgichat.hook.Hook;

/**
 * Simple Hook Activator that hooks back into the web application from the server side OSGi framework to provide
 * the web application with an ChatService implementation. 
 */
public class HookActivator {
	
	/**
	 * Set chat service
	 * @param chatService ChatService to use
	 */
	public void setChatService(ChatService chatService){
		Hook.setChatService(chatService);
	}
	
	/**
	 * Unset chat service
	 * @param chatService ChatService to unset
	 */
	public void unsetChatService(ChatService chatService){
		Hook.unsetChatService(chatService);
	}

}
