package si.unimb.feri.osgichat.impl;

import si.unimb.feri.osgichat.ChatSession;

/**
 * {@inheritDoc}
 */
public class ChatSessionImpl implements ChatSession {

	private String nickname;
	
	/**
	 * Create a new ChatSessionImpl
	 * @param nickname Nickname of ChatSession
	 */
	public ChatSessionImpl(String nickname){
		this.nickname = nickname;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getNickname() {
		return nickname;
	}

}
