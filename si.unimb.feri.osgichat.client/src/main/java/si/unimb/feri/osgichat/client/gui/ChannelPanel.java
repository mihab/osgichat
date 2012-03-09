package si.unimb.feri.osgichat.client.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

/**
 * Channel panel to show a single channel
 */
public class ChannelPanel extends JPanel{

	private ChatWindow chatWindow;

	private String channel;

	private String nickname;

	private DefaultListModel listModel = new DefaultListModel();

	private JTextField textField;

	private JTextArea textArea;

	/**
	 * Create a new ChannelPanel
	 * @param chatWindow ChatWindow this ChannelPanel belongs to
	 * @param channel String channel name to talk on
	 * @param nickname Nickname of user
	 */
	public ChannelPanel(ChatWindow chatWindow,String channel,String nickname){
		this.chatWindow = chatWindow;
		this.channel = channel;
		this.nickname = nickname;
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		JScrollPane textAreaPane = new JScrollPane(textArea);
		final JList list = new JList(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane listScrollPane = new JScrollPane(list);
		listScrollPane.setPreferredSize(new Dimension(100,100));
		listScrollPane.setMinimumSize(new Dimension(100,100));
		list.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2){
					if(list.getSelectedValue() != null){
						ChannelPanel.this.chatWindow.startPrivateChat((String) list.getSelectedValue());
					}
				}
			}
			public void mousePressed(MouseEvent e){
				if(e.isPopupTrigger()){
					showPopup(e);
				}
			}
			public void mouseReleased(MouseEvent e){
				if(e.isPopupTrigger()){
					showPopup(e);
				}
			}
			private void showPopup(MouseEvent e){
				final String name = (String) list.getSelectedValue();
				if(name == null || name.equals(ChannelPanel.this.nickname))return;
				JPopupMenu popup = new JPopupMenu();
				JMenuItem menuItem = new JMenuItem("Star chat");
				menuItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						ChannelPanel.this.chatWindow.startPrivateChat(name);
					}
				});
				popup.add(menuItem);
				if(ChannelPanel.this.chatWindow.getRegistered()){
					String[] ignoredUsers = ChannelPanel.this.chatWindow.getIgnoredUsers();
					if(Arrays.asList(ignoredUsers).contains(name)){
						menuItem = new JMenuItem("Unignore user");
						menuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent arg0) {
								ChannelPanel.this.chatWindow.unignoreUser(name);
							}
						});
					}else{
						menuItem = new JMenuItem("Ignore user");
						menuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent arg0) {
								ChannelPanel.this.chatWindow.ignoreUser(name);
							}
						});
					}
				}
				popup.add(menuItem);
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
		textField = new JTextField();
		textField.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					if(!textField.getText().equals("")){
						ChannelPanel.this.chatWindow.sendChannelMessage(ChannelPanel.this.channel, textField.getText());
						textField.setText("");
					}
				}
			}
		});
		JButton button = new JButton("Send");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!textField.getText().equals("")){
					ChannelPanel.this.chatWindow.sendChannelMessage(ChannelPanel.this.channel, textField.getText());
					textField.setText("");
				}
			}
		});
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.fill = GridBagConstraints.BOTH;
		add(textAreaPane,c);
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 0;
		c.weighty = 1.0;
		c.fill = GridBagConstraints.VERTICAL;
		add(listScrollPane,c);
		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 1.0;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(textField,c);
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 1;
		c.fill = GridBagConstraints.NONE;
		add(button,c);
	}

	/**
	 * Set subscribers
	 * @param subscribers Subscribers on this channel
	 */
	public void setSubscribers(String[] subscribers){
		listModel.clear();
		for(String subscriber:subscribers){
			listModel.addElement(subscriber);
		}
	}

	/**
	 * A channel messages has been received, show it in the text area
	 * @param fromNickname Nickname who sent the messages
	 * @param message Message sent
	 */
	public void messageReceived(String fromNickname,String message){
		textArea.append("\n");
		textArea.append(fromNickname+":"+message);
	}

}
