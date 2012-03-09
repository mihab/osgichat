package si.unimb.feri.osgichat.impl;

import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.ServiceRegistration;

import si.unimb.feri.osgichat.ChatHandler;

/**
 * Properties of the ChatSession used by the ChatService
 */
public class ChatSessionProperties {
	
	private String nickname;
	
	private String password;
	
	private ChatHandler chatHandler;
	
	private String[] ignoredUsers;
	
	private List<ServiceRegistration> serviceRegistrations = new ArrayList<ServiceRegistration>();
	
	/**
	 * Create a new unregistered ChatSessionProperties
	 * @param nickname Nickname of ChatSession
	 * @param password Password of CHatSession
	 */
	public ChatSessionProperties(String nickname,ChatHandler chatHandler){
		this.nickname = nickname;
		this.chatHandler = chatHandler;
	}
	
	/**
	 * Create a new registered ChatSessionProperties
	 * @param nickname Nickname of ChatSession
	 * @param password Password of CHatSession
	 * @param ignoredUsers List of nicknames to ignore
	 * @param chatHandler ChatHandler to use for callbacks
	 */
	public ChatSessionProperties(String nickname,String password,String[] ignoredUsers,ChatHandler chatHandler){
		this.nickname = nickname;
		this.password = password;
		this.ignoredUsers = ignoredUsers;
		this.chatHandler = chatHandler;
	}

	/**
	 * Get nickname
	 * @return Nickname of ChatSession
	 */
	public String getNickname() {
		return nickname;
	}

	/**
	 * Set nickname
	 * @param nickname Nickname of ChatSession
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	/**
	 * Get password
	 * @return Password of ChatSession
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Set password
	 * @param password Password of ChatSession
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Get ChatHandler
	 * @return ChatHandler of ChatSession
	 */
	public ChatHandler getChatHandler() {
		return chatHandler;
	}

	/**
	 * Set ChatHandler
	 * @param chatHandler ChatHandler of ChatSession
	 */
	public void setChatHandler(ChatHandler chatHandler) {
		this.chatHandler = chatHandler;
	}

	/**
	 * Get ignored users
	 * @return List of ignored users
	 */
	public String[] getIgnoredUsers() {
		return ignoredUsers;
	}

	/**
	 * Set ignored users
	 * @param ignoredUsers List of ignored users
	 */
	public void setIgnoredUsers(String[] ignoredUsers) {
		this.ignoredUsers = ignoredUsers;
	}

	/**
	 * Get service registrations
	 * @return List of service registrations
	 */
	public List<ServiceRegistration> getServiceRegistrations() {
		return serviceRegistrations;
	}

	/**
	 * Set service registrations
	 * @param serviceRegistrations List of service registrations
	 */
	public void setServiceRegistrations(List<ServiceRegistration> serviceRegistrations) {
		this.serviceRegistrations = serviceRegistrations;
	}

}
