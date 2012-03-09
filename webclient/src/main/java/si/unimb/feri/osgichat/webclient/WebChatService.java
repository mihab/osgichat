package si.unimb.feri.osgichat.webclient;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.springframework.flex.messaging.MessageTemplate;

import flex.messaging.MessageBroker;
import flex.messaging.services.MessageService;

import si.unimb.feri.osgichat.ChatException;
import si.unimb.feri.osgichat.ChatSession;
import si.unimb.feri.osgichat.hook.Hook;

/**
 * WebClient presents the web interface entry point to OSGi chat service. It uses BlazeDS messaging for simulating
 * near-realtime push of chat messages to the client. It also logouts if the client from the ChatService if keep-alive
 * messages from the client cease.
 */
public class WebChatService {
	
	public static final int TIMEOUT_SECONDS = 45;
	
	/**
	 * Map of active users unique ids to their WebChatSessionProperties
	 */
	private Map<String, WebChatSessionProperties> activeUsers = new Hashtable<String, WebChatSessionProperties>();
	
	/**
	 * Message broker
	 */
	private MessageBroker messageBroker;
	
	/**
	 * Message template used for sending messages
	 */
	private MessageTemplate messageTemplate;
	
	/**
	 * Set the message template
	 * @param messageTemplate Spring MessageTemplate used to send messages 
	 */
	public void setMessageTemplate(MessageTemplate messageTemplate){
		this.messageTemplate = messageTemplate;
	}

	/**
	 * BlazeDS MessageBroker to use to create client message destinations
	 * @param messageBroker BlazeDS MessageBroker to use
	 */
	public void setMessageBroker(MessageBroker messageBroker){
		this.messageBroker = messageBroker;
	}
	
	/**
	 * Create new instance
	 */
	public WebChatService(){
		Thread t = new Thread(){
			public void run(){
				while(true){
					try {
						Thread.sleep(TIMEOUT_SECONDS*1000);
						synchronized(WebChatService.this){
							List<String> usersTimeouted = new ArrayList<String>();
							for(String id:activeUsers.keySet()){
								if(System.currentTimeMillis() - activeUsers.get(id).getLastMessageTime()>TIMEOUT_SECONDS*1000){
									usersTimeouted.add(id);
								}
							}
							for(String id:usersTimeouted){
								logoutUser(id);
							}
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
						return;
					}
				}
			}
		};
		t.start();
	}
	
	/**
	 * Login to ChatService
	 * @param nickname Nickname to use
	 * @return Unique id required on subsequent invocations
	 * @throws ChatException if login failed
	 */
	public synchronized String login(String nickname) throws ChatException{
		WebChatHandler chatHandler = new WebChatHandler(nickname,messageTemplate);
		ChatSession chatSession = Hook.getChatService().login(nickname, chatHandler);
		MessageService messageService = (MessageService) messageBroker.getService("message-service");
		messageService.createDestination(nickname);
		messageService.start();
		WebChatSessionProperties webChatSessionProperties = new WebChatSessionProperties(chatSession,System.currentTimeMillis());
		String id = System.currentTimeMillis()+":"+nickname;
		activeUsers.put(id, webChatSessionProperties);
		return id;
	}
	
	/**
	 * Login to ChatService
	 * @param nickname Nickname to use
	 * @param password Password of nickname
	 * @return Unique id required on subsequent invocations
	 * @throws ChatException if login failed
	 */
	public synchronized String login(String nickname,String password) throws ChatException{
		WebChatHandler chatHandler = new WebChatHandler(nickname,messageTemplate);
		ChatSession chatSession = Hook.getChatService().login(nickname,password,chatHandler);
		MessageService messageService = (MessageService) messageBroker.getService("message-service");
		messageService.createDestination(nickname);
		messageService.start();
		WebChatSessionProperties webChatSessionProperties = new WebChatSessionProperties(chatSession,System.currentTimeMillis());
		String id = System.currentTimeMillis()+":"+nickname;
		activeUsers.put(id, webChatSessionProperties);
		return id;
	}
	
	/**
	 * Logout from ChatService
	 * @param id Unique client id
	 */
	public synchronized void logoutUser(String id){
		WebChatSessionProperties webChatSessionProperties = activeUsers.remove(id);
		Hook.getChatService().logout(webChatSessionProperties.getChatSession());
		MessageService messageService = (MessageService) messageBroker.getService("message-service");
		messageService.removeDestination(webChatSessionProperties.getChatSession().getNickname());
	}
	
	/**
	 * Register current nickname
	 * @param password Password of nickname
	 * @param id Unique client id
	 */
	public void registerNickname(String password,String id){
		Hook.getChatService().registerNickname(password, activeUsers.get(id).getChatSession());
	}
	
	/**
	 * Change password of nickname
	 * @param newPassword New password
	 * @param id Unique client id
	 */
	public void changePassword(String newPassword,String id){
		Hook.getChatService().changePassword(newPassword, activeUsers.get(id).getChatSession());
	}
	
	/**
	 * Join channel
	 * @param channel Channel to join
	 * @param id Unique client id
	 */
	public void joinChannel(String channel,String id){
		Hook.getChatService().joinChannel(channel, activeUsers.get(id).getChatSession());
	}
	
	/**
	 * Leave channel
	 * @param channel Channel to leave
	 * @param id Unique client id
	 */
	public void leaveChannel(String channel,String id){
		Hook.getChatService().leaveChannel(channel, activeUsers.get(id).getChatSession());
	}
	
	/**
	 * Send private message
	 * @param toNickname Nickname to send message to
	 * @param message Message to send
	 * @param id Unique client id
	 */
	public void sendPrivateMessage(String toNickname,String message,String id){
		Hook.getChatService().sendPrivateMessage(toNickname, message, activeUsers.get(id).getChatSession());
	}
	
	/**
	 * Send channel message
	 * @param toChannel Channel to send message to
	 * @param message Message to send
	 * @param id Unique client id
	 */
	public void sendChannelMessage(String toChannel,String message,String id){
		Hook.getChatService().sendChannelMessage(toChannel, message, activeUsers.get(id).getChatSession());
	}
	
	/**
	 * Get ignored users list
	 * @param id Unique client id
	 * @return Current ignored users list
	 */
	public String[] getIgnoredUsers(String id){
		return Hook.getChatService().getIgnoredUsers(activeUsers.get(id).getChatSession());
	}
	
	/**
	 * Ignore user
	 * @param nicknameToIgnore Nickname to ignore
	 * @param id Unique client id
	 * @return Current ignored users list
	 */
	public String[] ignoreUser(String nicknameToIgnore,String id){
		return Hook.getChatService().ignoreUser(nicknameToIgnore, activeUsers.get(id).getChatSession());
	}
	
	/**
	 * Unignore user
	 * @param nicknameToUnignore Nickname to unignore
	 * @param id Unique client id
	 * @return Current ignored users list
	 */
	public String[] unignoreUser(String nicknameToUnignore,String id){
		return Hook.getChatService().unignoreUser(nicknameToUnignore, activeUsers.get(id).getChatSession());
	}
	
	/**
	 * Keep alive current chat session
	 * @param id Unique client id
	 */
	public void keepAlive(String id){
		activeUsers.get(id).setLastMessageTime(System.currentTimeMillis());
	}
	
}
