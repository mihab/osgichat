package si.unimb.feri.osgichat.webclient.messages;

public class ChannelMessage {
	
	public int type = 2;
	
	private String fromChannel;
	
	private String fromNickname;
	
	private String message;

	public String getFromChannel() {
		return fromChannel;
	}

	public void setFromChannel(String fromChannel) {
		this.fromChannel = fromChannel;
	}

	public String getFromNickname() {
		return fromNickname;
	}

	public void setFromNickname(String fromNickname) {
		this.fromNickname = fromNickname;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
