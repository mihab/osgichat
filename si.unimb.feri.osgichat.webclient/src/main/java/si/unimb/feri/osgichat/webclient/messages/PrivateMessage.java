package si.unimb.feri.osgichat.webclient.messages;

public class PrivateMessage {
	
	public int type = 1;
	
	private String fromNickname;
	
	private String message;

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
