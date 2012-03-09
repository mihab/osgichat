package si.unimb.feri.osgichat.client.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Hashtable;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import si.unimb.feri.osgichat.ChatException;
import si.unimb.feri.osgichat.ChatService;
import si.unimb.feri.osgichat.ChatSession;

/**
 * Main chat window used to communicate.
 */
public class ChatWindow {
	
	private JFrame frame;
	
	private JPanel buttonPanel;
	
	private JTabbedPane pane;
	
	private ChatSession chatSession;
	
	private ChatService chatService;
	
	private Map<String, ChannelPanel> joinedChannels = new Hashtable<String, ChannelPanel>();
	
	private Map<String, PrivateChatPanel> privateChats = new Hashtable<String, PrivateChatPanel>();
	
	private boolean nicknameRegistered = false;
	
	/**
	 * Create main chat window
	 * @param chatService ChatService to use
	 */
	public void createChatWindow(ChatService chatService){
		this.chatService = chatService;
		createAndShowGUI();
	}
	
	/**
	 * Dispose of main chat window logging out if logged in
	 */
	public void disposeChatWindow(){
		if(chatSession != null){
			chatService.logout(chatSession);
			chatSession = null;
		}
		chatService = null;
		if(frame.isVisible()){
			frame.setVisible(false);
			frame.dispose();
		}
	}
	
	/**
	 * A private message was received from another user
	 * @param fromNickname Nickname of user who sent the message 
	 * @param message Message sent
	 */
	public void privateMessageReceived(String fromNickname,String message){
		PrivateChatPanel privateChatPanel = privateChats.get(fromNickname);
		if(privateChatPanel == null){
			privateChatPanel = new PrivateChatPanel(ChatWindow.this,fromNickname,chatSession.getNickname());
			pane.add(fromNickname, privateChatPanel);
			pane.setTabComponentAt(pane.getTabCount()-1,new ButtonTabComponent(pane,this));
			privateChats.put(fromNickname, privateChatPanel);
		}
		privateChatPanel.messageReceived(message);
	}
	
	/**
	 * Channel message received from another user
	 * @param fromChannel Channel on which message was received
	 * @param fromNickname Nickname of user who sent the message
	 * @param message Message sent
	 */
	public void channelMessageReceived(String fromChannel,String fromNickname,String message) {
		ChannelPanel channelPanel = joinedChannels.get(fromChannel);
		channelPanel.messageReceived(fromNickname, message);
	}
	
	/**
	 * List of channel subscribers changed, if ChannelPanel is not open for this channel then open it
	 * @param channel Channel on which subscribers changed
	 * @param subscribers Array of subscriber nicknames
	 */
	public void channelSubscribersChanged(String channel, String[] subscribers) {
		ChannelPanel channelPanel = joinedChannels.get(channel);
		if(channelPanel == null){
			channelPanel = new ChannelPanel(this,channel,chatSession.getNickname());
			pane.add("#"+channel, channelPanel);
			pane.setTabComponentAt(pane.getTabCount()-1,new ButtonTabComponent(pane,this));
			pane.setSelectedComponent(channelPanel);
			joinedChannels.put(channel, channelPanel);
		}
		channelPanel.setSubscribers(subscribers);
	}
	
	/**
	 * Leave channel and close ChannelPanel. Usually a user has closed a ChannelPanel.
	 * @param channel Channel to close
	 */
	public void leaveChannel(String channel){
		chatService.leaveChannel(channel, chatSession);
		ChannelPanel channelPanel = joinedChannels.remove(channel);
		pane.remove(channelPanel);
	}
	
	/**
	 * Send channel message to ChatService
	 * @param channel Channel to send message on
	 * @param message Message to send
	 */
	public void sendChannelMessage(String channel,String message){
		chatService.sendChannelMessage(channel, message, chatSession);
	}
	
	/**
	 * Send private message to another user
	 * @param nickname Nickname of other user
	 * @param message Message to send
	 */
	public void sendPrivateMessage(String nickname,String message){
		chatService.sendPrivateMessage(nickname, message, chatSession);
	}
	
	/**
	 * Open a new PrivateChatPanel to send private messages to another user
	 * @param nickname Nickname of user to send messages from the PrivateChatPanel to
	 */
	public void startPrivateChat(String nickname){
		if(nickname.equals(chatSession.getNickname()))return;
		if(privateChats.get(nickname) != null)return;
		PrivateChatPanel privateChatPanel = new PrivateChatPanel(ChatWindow.this,nickname,chatSession.getNickname());
		pane.add(nickname, privateChatPanel);
		pane.setTabComponentAt(pane.getTabCount()-1,new ButtonTabComponent(pane,ChatWindow.this));
		pane.setSelectedComponent(privateChatPanel);
		privateChats.put(nickname, privateChatPanel);
	}
	
	/**
	 * Close private PrivateChatPanel
	 * @param nickname Nickname to associate the PrivateChatPanel to
	 */
	public void closePrivateChat(String nickname){
		PrivateChatPanel privateChatPanel = privateChats.remove(nickname);
		pane.remove(privateChatPanel);
	}
	
	/**
	 * Ignore user
	 * @param nickname User to ignore
	 */
	public void ignoreUser(String nickname){
		chatService.ignoreUser(nickname, chatSession);
	}
	
	/**
	 * Unignore user
	 * @param nickname User to unignore
	 */
	public void unignoreUser(String nickname){
		chatService.unignoreUser(nickname, chatSession);
	}
	
	/**
	 * Return whether the current nickname is registered
	 * @return true if nickname registered, false otherwise
	 */
	public boolean getRegistered(){
		return nicknameRegistered;
	}
	
	/**
	 * Get ignored users list
	 * @return Ignored users list
	 */
	public String[] getIgnoredUsers(){
		return chatService.getIgnoredUsers(chatSession);
	}
	
	/**
	 * Create and show the main window
	 */
	private void createAndShowGUI(){
		frame = new JFrame("Chat window");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent arg0) {
				ChatWindow.this.disposeChatWindow();
			}
		});
		frame.setPreferredSize(new Dimension(700,500));
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
		JButton loginButton = new JButton("Login");
		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				showLoginDialog();
			}
		});
		buttonPanel.add(loginButton);
		JButton logoutButton = new JButton("Logout");
		logoutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				logout();				
			}
		});
		logoutButton.setVisible(false);
		buttonPanel.add(logoutButton);
		JButton joinChannelButton = new JButton("Join Channel");
		joinChannelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String channel = JOptionPane.showInputDialog(frame,"Enter channel name");
				if ((channel != null) && (channel.length() > 0)) {
					if(joinedChannels.get(channel) != null)return;
					chatService.joinChannel(channel, chatSession);
				}
			}
		});
		joinChannelButton.setVisible(false);
		buttonPanel.add(joinChannelButton);
		JButton startPrivateChatButton = new JButton("Start private chat");
		startPrivateChatButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String nickname  = JOptionPane.showInputDialog(frame,"Enter nickname");
				if ((nickname != null) && (nickname.length() > 0)) {
					startPrivateChat(nickname);
				}
			}
		});
		startPrivateChatButton.setVisible(false);
		buttonPanel.add(startPrivateChatButton);
		buttonPanel.add(Box.createHorizontalGlue());
		JButton registerNicknameButton = new JButton("Register nickname");
		registerNicknameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				registerNickname();
			}
		});
		registerNicknameButton.setVisible(false);
		buttonPanel.add(registerNicknameButton);
		JButton changePasswordButton = new JButton("Change password");
		changePasswordButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changePassword();
			}
		});
		changePasswordButton.setVisible(false);
		buttonPanel.add(changePasswordButton);
		JButton viewIgnoredUsersButton = new JButton("View ignored users");
		viewIgnoredUsersButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showIgnoredUsersDialog();
			}
		});
		viewIgnoredUsersButton.setVisible(false);
		buttonPanel.add(viewIgnoredUsersButton);
		pane = new JTabbedPane();
		pane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		frame.add(buttonPanel);
		frame.add(pane);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	/**
	 * Show ignored users dialog
	 */
	private void showIgnoredUsersDialog(){
		new IgnoredUsersDialog(frame,this,chatSession.getNickname(),chatService.getIgnoredUsers(chatSession));        
	}
	
	/**
	 * Register current nickname
	 */
	private void registerNickname(){
		String password = JOptionPane.showInputDialog(frame,"Enter nickname password");
		if ((password != null) && (password.length() > 0)) {
			chatService.registerNickname(password, chatSession);
			nicknameRegistered = true;
			changeButtons(true, true);
			JOptionPane.showMessageDialog(frame, "Nickname successfully registered!");
		}
	}
	
	/**
	 * Change password
	 */
	private void changePassword(){
		String password = JOptionPane.showInputDialog(frame,"Enter new password");
		if ((password != null) && (password.length() > 0)) {
			chatService.changePassword(password, chatSession);
			JOptionPane.showMessageDialog(frame, "Password successfully changed!");
		}
	}
	
	/**
	 * Shows login dialog to enter nickname and optional password
	 */
	private void showLoginDialog(){
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(4,1));
		panel.add(new JLabel("Enter desired nickname"));
		JTextField nicknameTextField = new JTextField();
		panel.add(nicknameTextField);
		panel.add(new JLabel("Enter nickname password"));
		JTextField passwordTextField = new JTextField();
		panel.add(passwordTextField);
		int value = JOptionPane.showConfirmDialog(frame,panel,"Put nickname and password",JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE);
		if(value == JOptionPane.OK_OPTION){
			String nickname = nicknameTextField.getText();
			String password = new String(passwordTextField.getText());
			if ((nickname != null) && (nickname.length() > 0)) {
				if((password != null)&&(password.length() > 0)){
					login(nickname,password);
				}else{
					login(nickname,null);
				}
			}
		}
	}
	
	/**
	 * Login to ChatService
	 * @param nickname Desired nickname to use
	 * @param password Password of nickname or null
	 */
	private void login(String nickname,String password){
		ChatHandlerSwing chatHandlerSwing = new ChatHandlerSwing(this);
		try {
			if(password != null){
				chatSession = chatService.login(nickname,password,chatHandlerSwing);
				nicknameRegistered = true;
				changeButtons(true, true);
			}
			else{
				chatSession = chatService.login(nickname,chatHandlerSwing);
				nicknameRegistered = false;
				changeButtons(true, false);
			}
			frame.setTitle("Chat window - "+nickname);
		} catch (ChatException e) {
			switch (e.getType()) {
			case ChatException.NICKNAME_TAKEN_EXCEPTION:
				JOptionPane.showMessageDialog(frame,"Nickname taken");
				break;
			case ChatException.NICKNAME_REGISTERED_EXCEPTION:
				JOptionPane.showMessageDialog(frame,"Nickname registered");
				break;
			case ChatException.NICKNAME_NOT_REGISTERED_EXCEPTION:
				JOptionPane.showMessageDialog(frame,"Nickname not registered");
				break;
			case ChatException.WRONG_PASSWORD_EXCEPTION:
				JOptionPane.showMessageDialog(frame,"Wrong password");
				break;
			default:
				throw new RuntimeException("Unknown chat exception");
			}
			return;
		}
	}
	
	/**
	 * Logout from ChatService
	 */
	private void logout(){
		chatService.logout(chatSession);
		chatSession = null;
		nicknameRegistered = false;
		changeButtons(false, false);
		frame.setTitle("Chat window");
		for(ChannelPanel channelPanel:joinedChannels.values()){
			pane.remove(channelPanel);
		}
		joinedChannels.clear();
		for(PrivateChatPanel privateChatPanel:privateChats.values()){
			pane.remove(privateChatPanel);
		}
		privateChats.clear();
	}
	
	/**
	 * Change buttons displayed
	 * @param logined If logined
	 * @param registered If nickname registered
	 */
	private void changeButtons(boolean logined,boolean registered){
		if(logined){
			buttonPanel.getComponent(0).setVisible(false);
			buttonPanel.getComponent(1).setVisible(true);
			buttonPanel.getComponent(2).setVisible(true);
			buttonPanel.getComponent(3).setVisible(true);
			if(registered){
				buttonPanel.getComponent(5).setVisible(false);
				buttonPanel.getComponent(6).setVisible(true);
				buttonPanel.getComponent(7).setVisible(true);
			}else{
				buttonPanel.getComponent(5).setVisible(true);
				buttonPanel.getComponent(6).setVisible(false);
				buttonPanel.getComponent(7).setVisible(false);
			}
		}else{
			buttonPanel.getComponent(0).setVisible(true);
			buttonPanel.getComponent(1).setVisible(false);
			buttonPanel.getComponent(2).setVisible(false);
			buttonPanel.getComponent(3).setVisible(false);
			buttonPanel.getComponent(5).setVisible(false);
			buttonPanel.getComponent(6).setVisible(false);
			buttonPanel.getComponent(7).setVisible(false);
		}
	}
	
}
