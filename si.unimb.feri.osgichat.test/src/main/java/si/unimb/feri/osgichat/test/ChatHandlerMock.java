package si.unimb.feri.osgichat.test;

import java.util.Arrays;

import si.unimb.feri.osgichat.ChatHandler;

public class ChatHandlerMock implements ChatHandler {
	
	private String lastPrivateMessageNickname;
	
	private String lastPrivateMessage;
	
	private String lastChannelMessageNickname;
	
	private String lastChannelMessage;
	
	private String lastChannelMessageChannel;
	
	private String lastChannelSubscribersChanged;
	
	private String[] lastChannelSubscribersList;
	
	public String getLastPrivateMessageNickname() {
		return lastPrivateMessageNickname;
	}

	public String getLastPrivateMessage() {
		return lastPrivateMessage;
	}

	public String getLastChannelMessageNickname() {
		return lastChannelMessageNickname;
	}

	public String getLastChannelMessage() {
		return lastChannelMessage;
	}

	public String getLastChannelMessageChannel() {
		return lastChannelMessageChannel;
	}

	public String getLastChannelSubscribersChanged() {
		return lastChannelSubscribersChanged;
	}

	public String[] getLastChannelSubscribersList() {
		return lastChannelSubscribersList;
	}

	@Override
	public void privateMessageReceived(String fromNickname, String message) {
		System.out.println("Private message received from "+fromNickname+", message:"+message);
		lastPrivateMessageNickname = fromNickname;
		lastPrivateMessage = message;
	}

	@Override
	public void channelMessageReceived(String fromChannel, String fromNickname,String message) {
		System.out.println("Channel message received on "+fromChannel+" from "+fromNickname+", message:"+message);
		lastChannelMessageChannel = fromChannel;
		lastChannelMessageNickname = fromNickname;
		lastChannelMessage = message;
	}

	@Override
	public void channelSubscribersChanged(String channel, String[] subscribers) {
		System.out.println("Channel subscribers changed on "+channel+", subscribers:"+Arrays.toString(subscribers));
		lastChannelSubscribersChanged = channel;
		lastChannelSubscribersList = subscribers;
	}

}
