package si.unimb.feri.osgichat.impl;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;

import si.unimb.feri.osgichat.ChatException;
import si.unimb.feri.osgichat.ChatHandler;
import si.unimb.feri.osgichat.ChatService;
import si.unimb.feri.osgichat.ChatSession;
import si.unimb.feri.osgichat.nickserv.NickService;

/**
 * ChatService that uses the EventAdmin OSGi service for the chat service implementation.
 */
public class ChatServiceImpl implements ChatService {
	
	/**
	 * List of active nicknames with valid sessions mapped to them
	 */
	private Map<String, ChatSession> activeNicknames = new Hashtable<String, ChatSession>();
	
	/**
	 * ChatSession to ChatSessionProperties mapping
	 */
	private Map<ChatSession, ChatSessionProperties> chatSessionPropertiesMap = new Hashtable<ChatSession, ChatSessionProperties>();
	
	/**
	 * Map of channel to a list of subscribers to that channel
	 */
	private Map<String, List<String>> channelSubscribers = new Hashtable<String, List<String>>();
	
	/**
	 * Bundle to register services upon
	 */
	private BundleContext bundleContext;

	/**
	 * Event admin to use for publish/subscribe messaging
	 */
	private EventAdmin eventAdmin;
	
	/**
	 * NickService to use
	 */
	private NickService nickService;

	/**
	 * Activate this ChatService implementation, store the BundleContext referece.
	 * @param bundleContext BundleContext of this bundle
	 */
	public void activate(BundleContext bundleContext){
		this.bundleContext = bundleContext;
	}

	/**
	 * Deactivate this ChatService implementation and release all references
	 * @param bundleContext BundleContext of this bundle
	 */
	public void deactivate(BundleContext bundleContext){
		this.bundleContext = null;
		for(ChatSessionProperties chatSessionProperties:chatSessionPropertiesMap.values()){
			for(ServiceRegistration serviceRegistration:chatSessionProperties.getServiceRegistrations()){
				serviceRegistration.unregister();
			}
			chatSessionProperties.getServiceRegistrations().clear();
		}
		chatSessionPropertiesMap.clear();
		activeNicknames.clear();
		channelSubscribers.clear();
	}

	/**
	 * Set EventAdmin
	 * @param eventAdmin EventAdmin to set
	 */
	public void setEventAdmin(EventAdmin eventAdmin){
		this.eventAdmin = eventAdmin;
	}

	/**
	 * Unset EventAdmin
	 * @param eventAdmin EventAdmin to unset
	 */
	public void unsetEventAdmin(EventAdmin eventAdmin){
		this.eventAdmin = null;
	}
	
	/**
	 * Set NickService
	 * @param nickService NickService to set
	 */
	public void setNickService(NickService nickService){
		this.nickService = nickService;
	}
	
	/**
	 * Unset NickService
	 * @param nickService NickService to unset
	 */
	public void unsetNickService(NickService nickService){
		this.nickService = null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized ChatSession login(String nickname, ChatHandler chatHandler) throws ChatException {
		if(activeNicknames.get(nickname) != null){
			throw new ChatException(ChatException.NICKNAME_TAKEN_EXCEPTION,"Nickname taken");
		}
		if(nickService.checkRegistered(nickname)){
			throw new ChatException(ChatException.NICKNAME_REGISTERED_EXCEPTION,"Nickname registered");
		}
		ChatSession chatSession = new ChatSessionImpl(nickname);
		activeNicknames.put(nickname, chatSession);
		ChatSessionProperties chatSessionProperties = new ChatSessionProperties(nickname,chatHandler);
		chatSessionPropertiesMap.put(chatSession, chatSessionProperties);
		Dictionary<String, String> dictionary = new Hashtable<String, String>();
		dictionary.put(EventConstants.EVENT_TOPIC, ChatEvent.PRIVATE_TOPIC_PREFIX+nickname);
		ChatEventHandler chatEventHandler = new ChatEventHandler(chatSessionProperties);
		ServiceRegistration serviceRegistration = bundleContext.registerService(EventHandler.class.getName(),chatEventHandler,dictionary);
		chatSessionProperties.getServiceRegistrations().add(serviceRegistration);
		return chatSession;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized ChatSession login(String nickname, String password,ChatHandler chatHandler) throws ChatException {
		if(activeNicknames.get(nickname) != null){
			throw new ChatException(ChatException.NICKNAME_TAKEN_EXCEPTION,"Nickname taken");
		}
		if(!nickService.checkRegistered(nickname)){
			throw new ChatException(ChatException.NICKNAME_NOT_REGISTERED_EXCEPTION,"Nickname not registered");
		}
		if(!nickService.checkPassword(nickname,password)){
			throw new ChatException(ChatException.WRONG_PASSWORD_EXCEPTION,"Wrong password");
		}
		ChatSession chatSession = new ChatSessionImpl(nickname);
		activeNicknames.put(nickname, chatSession);
		ChatSessionProperties chatSessionProperties = new ChatSessionProperties(nickname,password,nickService.getIgnoredUsers(nickname, password),chatHandler);
		chatSessionPropertiesMap.put(chatSession, chatSessionProperties);
		Dictionary<String, String> dictionary = new Hashtable<String, String>();
		dictionary.put(EventConstants.EVENT_TOPIC, ChatEvent.PRIVATE_TOPIC_PREFIX+nickname);
		ChatEventHandler chatEventHandler = new ChatEventHandler(chatSessionProperties);
		ServiceRegistration serviceRegistration = bundleContext.registerService(EventHandler.class.getName(),chatEventHandler,dictionary);
		chatSessionProperties.getServiceRegistrations().add(serviceRegistration);
		return chatSession;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void logout(ChatSession chatSession) {
		if(activeNicknames.get(chatSession.getNickname()) != chatSession){
			throw new IllegalArgumentException("Invalid chat session");
		}
		List<ServiceRegistration> serviceRegistrations = chatSessionPropertiesMap.get(chatSession).getServiceRegistrations();
		for(ServiceRegistration serviceRegistration:serviceRegistrations){
			String topic = (String) serviceRegistration.getReference().getProperty(EventConstants.EVENT_TOPIC);
			serviceRegistration.unregister();
			if(topic.contains(ChatEvent.CHANNEL_TOPIC_PREFIX)){
				String channel = topic.replace(ChatEvent.CHANNEL_TOPIC_PREFIX, "");
				channelSubscribers.get(channel).remove(chatSession.getNickname());
				Map<String, Object> p = new Hashtable<String, Object>();
				p.put("eventType", ChatEvent.CHANNEL_SUBSCRIBERS_CHANGED);
				p.put("channel", channel);
				p.put("subscribers", channelSubscribers.get(channel).toArray(new String[0]));
				eventAdmin.sendEvent(new Event(ChatEvent.CHANNEL_TOPIC_PREFIX+channel, (Dictionary) p));
			}
		}
		serviceRegistrations.clear();
		chatSessionPropertiesMap.remove(chatSession);
		activeNicknames.remove(chatSession.getNickname());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void registerNickname(String password, ChatSession chatSession) {
		if(activeNicknames.get(chatSession.getNickname()) != chatSession){
			throw new IllegalArgumentException("Invalid chat session");
		}
		nickService.registerNickname(chatSession.getNickname(), password);
		chatSessionPropertiesMap.get(chatSession).setPassword(password);
		chatSessionPropertiesMap.get(chatSession).setIgnoredUsers(nickService.getIgnoredUsers(chatSession.getNickname(), password));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void changePassword(String newPassword, ChatSession chatSession) {
		if(activeNicknames.get(chatSession.getNickname()) != chatSession){
			throw new IllegalArgumentException("Invalid chat session");
		}
		nickService.changePassword(chatSession.getNickname(), chatSessionPropertiesMap.get(chatSession).getPassword(), newPassword);
		chatSessionPropertiesMap.get(chatSession).setPassword(newPassword);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void joinChannel(String channel, ChatSession chatSession) {
		if(activeNicknames.get(chatSession.getNickname()) != chatSession){
			throw new IllegalArgumentException("Invalid chat session");
		}
		if(channelSubscribers.get(channel) != null && channelSubscribers.get(channel).contains(chatSession.getNickname())){
			throw new RuntimeException("Already subscribed to channel");
		}
		Dictionary<String, String> dictionary = new Hashtable<String, String>();
		dictionary.put(EventConstants.EVENT_TOPIC, ChatEvent.CHANNEL_TOPIC_PREFIX+channel);
		ChatEventHandler chatEventHandler = new ChatEventHandler(chatSessionPropertiesMap.get(chatSession));
		ServiceRegistration serviceRegistration = bundleContext.registerService(EventHandler.class.getName(),chatEventHandler,dictionary);
		List<ServiceRegistration> serviceRegistrations = chatSessionPropertiesMap.get(chatSession).getServiceRegistrations();
		serviceRegistrations.add(serviceRegistration);
		if(channelSubscribers.get(channel) == null){
			channelSubscribers.put(channel, new ArrayList<String>());
		}
		List<String> subscribers = channelSubscribers.get(channel);
		subscribers.add(chatSession.getNickname());
		Map<String, Object> p = new Hashtable<String, Object>();
		p.put("eventType", ChatEvent.CHANNEL_SUBSCRIBERS_CHANGED);
		p.put("channel", channel);
		p.put("subscribers", channelSubscribers.get(channel).toArray(new String[0]));
		eventAdmin.sendEvent(new Event(ChatEvent.CHANNEL_TOPIC_PREFIX+channel, (Dictionary) p));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void leaveChannel(String channel, ChatSession chatSession) {
		if(activeNicknames.get(chatSession.getNickname()) != chatSession){
			throw new IllegalArgumentException("Invalid chat session");
		}
		if(channelSubscribers.get(channel) == null || !channelSubscribers.get(channel).contains(chatSession.getNickname())){
			throw new RuntimeException("Not subscribed to channel");
		}
		List<ServiceRegistration> serviceRegistrations = chatSessionPropertiesMap.get(chatSession).getServiceRegistrations();
		for(ServiceRegistration serviceRegistration:serviceRegistrations){
			if(serviceRegistration.getReference().getProperty(EventConstants.EVENT_TOPIC).equals(ChatEvent.CHANNEL_TOPIC_PREFIX+channel)){
				serviceRegistrations.remove(serviceRegistration);
				serviceRegistration.unregister();
				break;
			}
		}
		channelSubscribers.get(channel).remove(chatSession.getNickname());
		Map<String, Object> p = new Hashtable<String, Object>();
		p.put("eventType", ChatEvent.CHANNEL_SUBSCRIBERS_CHANGED);
		p.put("channel", channel);
		p.put("subscribers", channelSubscribers.get(channel).toArray(new String[0]));
		eventAdmin.sendEvent(new Event(ChatEvent.CHANNEL_TOPIC_PREFIX+channel, (Dictionary) p));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void sendPrivateMessage(String toNickname, String message,ChatSession chatSession) {
		if(activeNicknames.get(chatSession.getNickname()) != chatSession){
			throw new IllegalArgumentException("Invalid chat session");
		}
		Map<String, Object> p = new Hashtable<String, Object>();
		p.put("eventType", ChatEvent.PRIVATE_MESSAGE);
		p.put("fromNickname", chatSession.getNickname());
		p.put("message", message);
		eventAdmin.sendEvent(new Event(ChatEvent.PRIVATE_TOPIC_PREFIX+toNickname, (Dictionary) p));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void sendChannelMessage(String toChannel, String message,ChatSession chatSession) {
		if(activeNicknames.get(chatSession.getNickname()) != chatSession){
			throw new IllegalArgumentException("Invalid chat session");
		}
		if(channelSubscribers.get(toChannel) == null || !channelSubscribers.get(toChannel).contains(chatSession.getNickname())){
			throw new RuntimeException("Not subscribed to channel");
		}
		Map<String, Object> p = new Hashtable<String, Object>();
		p.put("eventType", ChatEvent.CHANNEL_MESSAGE);
		p.put("fromChannel", toChannel);
		p.put("fromNickname", chatSession.getNickname());
		p.put("message", message);
		eventAdmin.sendEvent(new Event(ChatEvent.CHANNEL_TOPIC_PREFIX+toChannel, (Dictionary) p));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized String[] getIgnoredUsers(ChatSession chatSession) {
		if(activeNicknames.get(chatSession.getNickname()) != chatSession){
			throw new IllegalArgumentException("Invalid chat session");
		}
		return chatSessionPropertiesMap.get(chatSession).getIgnoredUsers();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized String[] ignoreUser(String nicknameToIgnore, ChatSession chatSession) {
		if(activeNicknames.get(chatSession.getNickname()) != chatSession){
			throw new IllegalArgumentException("Invalid chat session");
		}
		String[] ignoredUsers = nickService.ignoreUser(nicknameToIgnore, chatSession.getNickname(), chatSessionPropertiesMap.get(chatSession).getPassword()); 
		chatSessionPropertiesMap.get(chatSession).setIgnoredUsers(ignoredUsers);
		return ignoredUsers;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized String[] unignoreUser(String nicknameToUnignore,ChatSession chatSession) {
		if(activeNicknames.get(chatSession.getNickname()) != chatSession){
			throw new IllegalArgumentException("Invalid chat session");
		}
		String[] ignoredUsers = nickService.unignoreUser(nicknameToUnignore, chatSession.getNickname(), chatSessionPropertiesMap.get(chatSession).getPassword()); 
		chatSessionPropertiesMap.get(chatSession).setIgnoredUsers(ignoredUsers);
		return ignoredUsers;
	}

}
