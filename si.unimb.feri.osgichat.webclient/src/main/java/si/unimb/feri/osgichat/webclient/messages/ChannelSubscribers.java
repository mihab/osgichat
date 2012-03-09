package si.unimb.feri.osgichat.webclient.messages;

public class ChannelSubscribers {
	
	public int type = 3;
	
	private String channel;
	
	private String[] subscribers;
	
	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String[] getSubscribers() {
		return subscribers;
	}

	public void setSubscribers(String[] subscribers) {
		this.subscribers = subscribers;
	}
}
