package si.unimb.feri.osgichat.impl;

import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

/**
 * ChatService EventHandler implementation which listens for messages from the EventAdmin service
 */
public class ChatEventHandler implements EventHandler {

	private ChatSessionProperties chatSessionProperties;

	/**
	 * Create a new ChatEventHandler
	 * @param chatSessionProperties ChatSessionProperties to send messages to
	 */
	public ChatEventHandler(ChatSessionProperties chatSessionProperties){
		this.chatSessionProperties = chatSessionProperties;
	}

	/**
	 * Handles events from the EventAdmin service and calls the appropriate methods on the ChatHandler 
	 */
	@Override
	public void handleEvent(Event event) {
		int eventType = (Integer) event.getProperty("eventType");
		switch (eventType) {
		case ChatEvent.PRIVATE_MESSAGE:
			String fromNickname = (String) event.getProperty("fromNickname");
			String message = (String) event.getProperty("message");
			String[] ignoredUsers = chatSessionProperties.getIgnoredUsers();
			if(ignoredUsers != null){
				for(String ignoredUser:ignoredUsers){
					if(ignoredUser.equals(fromNickname))return;
				}
			}
			chatSessionProperties.getChatHandler().privateMessageReceived(fromNickname, message);
			break;
		case ChatEvent.CHANNEL_MESSAGE:
			String fromChannel = (String) event.getProperty("fromChannel");
			fromNickname = (String) event.getProperty("fromNickname");
			message = (String) event.getProperty("message");
			ignoredUsers = chatSessionProperties.getIgnoredUsers();
			if(ignoredUsers != null){
				for(String ignoredUser:ignoredUsers){
					if(ignoredUser.equals(fromNickname))return;
				}
			}
			chatSessionProperties.getChatHandler().channelMessageReceived(fromChannel, fromNickname, message);
			break;
		case ChatEvent.CHANNEL_SUBSCRIBERS_CHANGED:
			String channel = (String) event.getProperty("channel");
			String[] subscribers = (String[]) event.getProperty("subscribers");
			chatSessionProperties.getChatHandler().channelSubscribersChanged(channel, subscribers);
			break;
		default:
			throw new RuntimeException("Unsupported message received!");
		}
	}

}
