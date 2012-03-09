package si.unimb.feri.osgichat.client.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;

import si.unimb.feri.osgichat.ChatService;

/**
 * Main window class shown first. Launches separate ChatWindow instances for each new desired chat window.
 * Also moves the creation of the first frame and disposal of all frames from the OSGi thread to the AWT Event thread.
 */
public class MainWindow{

	private ChatService chatService;

	private JFrame frame;

	private List<ChatWindow> chatWindows = new ArrayList<ChatWindow>();

	/**
	 * Set the ChatService and create the main frame on AWT Event Event thread.
	 * @param chatService ChatService to use
	 */
	public void createMainWindow(ChatService chatService){
		this.chatService = chatService;
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	/**
	 * Dispose main window and also all child chat windows. Disposal occurs on AWT Event Thread. 
	 */
	public void disposeMainWindow(){
		chatService = null;
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				for(ChatWindow chatWindow:chatWindows){
					chatWindow.disposeChatWindow();
				}
				chatWindows.clear();
				if(frame.isVisible()){
					frame.setVisible(false);
					frame.dispose();
				}
			}
		});
	}

	/**
	 * Setup GUI, this method must be called on the AWT Event Thread
	 */
	private void createAndShowGUI() {
		frame = new JFrame("Main chat window");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent arg0) {
				MainWindow.this.disposeMainWindow();
			}
		});
		JButton button = new JButton("Launch new chat window");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChatWindow chatWindow = new ChatWindow();
				chatWindows.add(chatWindow);
				chatWindow.createChatWindow(chatService);
			}
		});
		frame.getContentPane().add(button,BorderLayout.CENTER);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

}
