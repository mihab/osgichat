package si.unimb.feri.osgichat.webclient;

import org.springframework.flex.messaging.MessageTemplate;

import si.unimb.feri.osgichat.ChatHandler;
import si.unimb.feri.osgichat.webclient.messages.ChannelMessage;
import si.unimb.feri.osgichat.webclient.messages.ChannelSubscribers;
import si.unimb.feri.osgichat.webclient.messages.PrivateMessage;

/**
 * ChatHandler to use with the WebClient. Uses the MessageTemplate for sending messages to clients.
 */
public class WebChatHandler implements ChatHandler {
	
	private String id;
	
	private MessageTemplate messageTemplate;
	
	/**
	 * Create a new instance
	 * @param id Destination id to send messages to
	 * @param messageTemplate MessageTemplate to use for sending messages
	 */
	public WebChatHandler(String id,MessageTemplate messageTemplate){
		this.id = id;
		this.messageTemplate = messageTemplate;
	}

	/**
	 * {@inheritDoc}
	 */
	public void privateMessageReceived(String fromNickname, String message) {
		PrivateMessage privateMessage = new PrivateMessage();
		privateMessage.setFromNickname(fromNickname);
		privateMessage.setMessage(message);
		messageTemplate.send(id, privateMessage);
	}

	/**
	 * {@inheritDoc}
	 */
	public void channelMessageReceived(String fromChannel, String fromNickname,String message) {
		ChannelMessage channelMessage = new ChannelMessage();
		channelMessage.setFromChannel(fromChannel);
		channelMessage.setFromNickname(fromNickname);
		channelMessage.setMessage(message);
		messageTemplate.send(id, channelMessage);
	}

	/**
	 * {@inheritDoc}
	 */
	public void channelSubscribersChanged(String channel, String[] subscribers) {
		ChannelSubscribers channelSubscribers = new ChannelSubscribers();
		channelSubscribers.setChannel(channel);
		channelSubscribers.setSubscribers(subscribers);
		messageTemplate.send(id, channelSubscribers);
	}

}
