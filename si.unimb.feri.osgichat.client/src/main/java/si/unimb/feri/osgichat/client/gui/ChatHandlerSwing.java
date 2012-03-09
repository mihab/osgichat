package si.unimb.feri.osgichat.client.gui;

import si.unimb.feri.osgichat.ChatHandler;

/**
 * {@inheritDoc}
 * This implementation puts received messages on the AWT Event Thread for safe display update.
 */
public class ChatHandlerSwing implements ChatHandler {
	
	private ChatWindow chatWindow;
	
	/**
	 * Create a new ChatHandlerSwing
	 * @param chatWindow ChatWindow this chat handler belongs to and sends messages to
	 */
	public ChatHandlerSwing(ChatWindow chatWindow){
		this.chatWindow = chatWindow;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void privateMessageReceived(final String fromNickname,final String message) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				chatWindow.privateMessageReceived(fromNickname, message);
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void channelMessageReceived(final String fromChannel,final String fromNickname,final String message) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				chatWindow.channelMessageReceived(fromChannel, fromNickname, message);
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void channelSubscribersChanged(final String channel,final String[] subscribers) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				chatWindow.channelSubscribersChanged(channel, subscribers);
			}
		});
	}

}
