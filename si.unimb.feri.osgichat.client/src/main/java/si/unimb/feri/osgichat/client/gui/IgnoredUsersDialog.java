package si.unimb.feri.osgichat.client.gui;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Show ignored users dialog 
 */
public class IgnoredUsersDialog extends JDialog implements ListSelectionListener{

	private JList list;
	
	private DefaultListModel listModel;

	private static final String ignoreString = "Ignore";
	
	private static final String unignoreString = "Unignore";
	
	private JButton unignoreButton;
	
	private JTextField ignoreUserName;
	
	private ChatWindow chatWindow;
	
	private String nickname;

	/**
	 * Create a new IgnoredUsersDialog
	 * @param frame Frame of dialog
	 * @param chatWindow ChatWindow this dialog belongs to
	 * @param nickname Nickname of user
	 * @param ignoredUsers List of currently ignored users
	 */
	public IgnoredUsersDialog(Frame frame,ChatWindow chatWindow,String nickname,String[] ignoredUsers) {
		super(frame);
		this.chatWindow = chatWindow;
		this.nickname = nickname;
		JPanel panel = new JPanel(new BorderLayout());
		listModel = new DefaultListModel();
		for(String ignoredUser:ignoredUsers){
			listModel.addElement(ignoredUser);
		}
		list = new JList(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);
		list.addListSelectionListener(this);
		list.setVisibleRowCount(5);
		JScrollPane listScrollPane = new JScrollPane(list);
		JButton ignoreButton = new JButton(ignoreString);
		IgnoreListener ignoreListener = new IgnoreListener(ignoreButton);
		ignoreButton.setActionCommand(ignoreString);
		ignoreButton.addActionListener(ignoreListener);
		ignoreButton.setEnabled(false);
		unignoreButton = new JButton(unignoreString);
		unignoreButton.setActionCommand(unignoreString);
		unignoreButton.addActionListener(new UnignoreListener());
		ignoreUserName = new JTextField(10);
		ignoreUserName.addActionListener(ignoreListener);
		ignoreUserName.getDocument().addDocumentListener(ignoreListener);
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane,BoxLayout.LINE_AXIS));
		buttonPane.add(unignoreButton);
		buttonPane.add(Box.createHorizontalStrut(5));
		buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
		buttonPane.add(Box.createHorizontalStrut(5));
		buttonPane.add(ignoreUserName);
		buttonPane.add(ignoreButton);
		buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		panel.add(listScrollPane, BorderLayout.CENTER);
		panel.add(buttonPane, BorderLayout.PAGE_END);
		add(panel);
		setTitle("Ignored users");
        setModalityType(Dialog.DEFAULT_MODALITY_TYPE);
        getContentPane().add(panel);
        pack();
        setLocationRelativeTo(frame);
        setVisible(true);
	}

	/**
	 * Listener for unignoring user 
	 */
	class UnignoreListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			chatWindow.unignoreUser((String) list.getSelectedValue());
			int index = list.getSelectedIndex();
			listModel.remove(index);
			int size = listModel.getSize();
			if(size == 0) {
				unignoreButton.setEnabled(false);
			}else {
				if(index == listModel.getSize()) {
					index--;
				}
				list.setSelectedIndex(index);
				list.ensureIndexIsVisible(index);
			}
		}
	}

	/**
	 * Listener for ignoring user 
	 */
	class IgnoreListener implements ActionListener, DocumentListener {
		private boolean alreadyEnabled = false;
		private JButton button;

		public IgnoreListener(JButton button) {
			this.button = button;
		}

		public void actionPerformed(ActionEvent e) {
			String name = ignoreUserName.getText();
			if (name.equals("") || alreadyInList(name) || name.equals(nickname)) {
				ignoreUserName.requestFocusInWindow();
				ignoreUserName.selectAll();
				return;
			}
			chatWindow.ignoreUser(ignoreUserName.getText());
			int index = list.getSelectedIndex(); //get selected index
			if (index == -1) { //no selection, so insert at beginning
				index = 0;
			} else {           //add after the selected item
				index++;
			}
			listModel.insertElementAt(ignoreUserName.getText(), index);
			ignoreUserName.requestFocusInWindow();
			ignoreUserName.setText("");
			list.setSelectedIndex(index);
			list.ensureIndexIsVisible(index);
		}

		protected boolean alreadyInList(String name) {
			return listModel.contains(name);
		}

		public void insertUpdate(DocumentEvent e) {
			enableButton();
		}

		public void removeUpdate(DocumentEvent e) {
			handleEmptyTextField(e);
		}

		public void changedUpdate(DocumentEvent e) {
			if (!handleEmptyTextField(e)) {
				enableButton();
			}
		}

		private void enableButton() {
			if (!alreadyEnabled) {
				button.setEnabled(true);
			}
		}

		private boolean handleEmptyTextField(DocumentEvent e) {
			if (e.getDocument().getLength() <= 0) {
				button.setEnabled(false);
				alreadyEnabled = false;
				return true;
			}
			return false;
		}
	}

	/**
	 * Value change on list
	 */
	public void valueChanged(ListSelectionEvent e) {
		if (e.getValueIsAdjusting() == false) {
			if (list.getSelectedIndex() == -1) {
				unignoreButton.setEnabled(false);
			} else {
				unignoreButton.setEnabled(true);
			}
		}
	}
}
