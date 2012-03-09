package si.unimb.feri.osgichat.client.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * Private chat panel to talk to a single user
 */
public class PrivateChatPanel extends JPanel{
	
	private ChatWindow chatWindow;
	
	private String nickname;
	
	private String userNickname;
	
	private JTextArea textArea;
	
	private JTextField textField;
	
	/**
	 * Construct a new PrivateChatPanel to talk to a user
	 * @param chatWindow ChatWindow instance this PrivateChatPanel belongs to
	 * @param nickname Nickname of user to talk to
	 * @param userNickname Nickname of this user
	 */
	public PrivateChatPanel(ChatWindow chatWindow,String nickname,String userNickname){
		this.chatWindow = chatWindow;
		this.nickname = nickname;
		this.userNickname = userNickname;
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		JScrollPane textAreaPane = new JScrollPane(textArea);
		textField = new JTextField();
		textField.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					if(!textField.getText().equals("")){
						textArea.append("\n");
						textArea.append(PrivateChatPanel.this.userNickname+":"+textField.getText());
						PrivateChatPanel.this.chatWindow.sendPrivateMessage(PrivateChatPanel.this.nickname, textField.getText());
						textField.setText("");
					}
				}
			}
		});
		JButton button = new JButton("Send");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(!textField.getText().equals("")){
					textArea.append("\n");
					textArea.append(PrivateChatPanel.this.userNickname+":"+textField.getText());
					PrivateChatPanel.this.chatWindow.sendPrivateMessage(PrivateChatPanel.this.nickname, textField.getText());
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
		c.gridwidth = 2;
		add(textAreaPane,c);
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
	 * Received a private message
	 * @param fromNickname Nickname who sent the message
	 * @param message Message received
	 */
	public void messageReceived(String message){
		textArea.append("\n");
		textArea.append(nickname+":"+message);
	}

}
