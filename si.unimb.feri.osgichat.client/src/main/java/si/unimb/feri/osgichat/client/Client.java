package si.unimb.feri.osgichat.client;

import si.unimb.feri.osgichat.ChatService;
import si.unimb.feri.osgichat.client.gui.MainWindow;

/**
 * Java Swing GUI client chat application.
 */
public class Client {
	
	private ChatService chatService;
	
	private MainWindow mainWindow = new MainWindow();
	
	/**
	 * Called when all services have been set
	 */
	public synchronized void activate(){
		mainWindow.createMainWindow(chatService);
	}
	
	/**
	 * Called when service is deactivating and before services are unset
	 */
	public synchronized void deactivate(){
		mainWindow.disposeMainWindow();
	}
	
	/**
	 * Set chat service 
	 * @param chatService ChatService implementation
	 */
	public synchronized void setChatService(ChatService chatService){
		this.chatService = chatService;
	}
	
	/**
	 * Unset chat service
	 * @param chatService ChatService implementation to unset
	 */
	public synchronized void unsetChatService(ChatService chatService){
		this.chatService = null;
	}

}
