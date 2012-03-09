package si.unimb.feri.osgichat.test;

import si.unimb.feri.osgichat.ChatException;
import si.unimb.feri.osgichat.ChatService;
import si.unimb.feri.osgichat.ChatSession;

public class AllTests {
	
	private ChatService chatService;
	
	public void activate(){
		System.out.println("AllTests activated");
		runTests();
	}
	
	public void deactivate(){
		System.out.println("AllTests deactivated");
	}
	
	public void setChatService(ChatService chatService){
		this.chatService = chatService;
		System.out.println("ChatService set");
	}
	
	public void unsetChatService(ChatService chatService){
		this.chatService = null;
		System.out.println("ChatService unset");
	}
	
	private void runTests(){
		try{
			runLoginLogoutTests();
			runPrivateMessageTests();
			runChannelTests();
			runChannelMessageTests();
			runNicknameRegistrationTests();
			runIgnoredUsersTests();
		} catch (Exception e){
			throw new RuntimeException(e);
		}
	}
	
	private void runLoginLogoutTests() throws Exception{
		ChatHandlerMock chatHandlerMock = new ChatHandlerMock();
		ChatSession chatSession = chatService.login("test1", chatHandlerMock);
		chatService.logout(chatSession);
		chatSession = chatService.login("test1", chatHandlerMock);
		try {
			chatService.login("test1", chatHandlerMock);
			throw new RuntimeException("Nickname should be taken");
		} catch (ChatException e) {
			System.out.println("Login successfully failed");
		}
		chatService.logout(chatSession);
		System.out.println("runLoginLogoutTests successful");
	}
	
	private void runPrivateMessageTests() throws Exception{
		ChatHandlerMock chatHandler1 = new ChatHandlerMock();
		ChatSession chatSession1 = chatService.login("nickname1", chatHandler1);
		ChatHandlerMock chatHandler2 = new ChatHandlerMock();
		ChatSession chatSession2 = chatService.login("nickname2", chatHandler2);
		chatService.sendPrivateMessage("nickname2", "Hello from nickname1", chatSession1);
		if(!chatHandler2.getLastPrivateMessageNickname().equals("nickname1"))throw new Exception("Incorrect behaviour");
		if(!chatHandler2.getLastPrivateMessage().equals("Hello from nickname1"))throw new Exception("Incorrect behaviour");
		chatService.sendPrivateMessage("nickname1", "Hello from nickname2", chatSession2);
		if(!chatHandler1.getLastPrivateMessageNickname().equals("nickname2")) throw new Exception("Incorrect behaviour");
		if(!chatHandler1.getLastPrivateMessage().equals("Hello from nickname2")) throw new Exception("Incorrect behaviour");
		chatService.logout(chatSession1);
		chatService.logout(chatSession2);
		ChatHandlerMock chatHandler = new ChatHandlerMock();
		ChatSession chatSession = chatService.login("test", chatHandler);
		chatService.sendPrivateMessage("nickname1", "Hello from test", chatSession);
		if(chatHandler1.getLastPrivateMessageNickname().equals("test")) throw new Exception("Incorrect behaviour");
		chatService.sendPrivateMessage("test", "Hello myself", chatSession);
		if(!chatHandler.getLastPrivateMessageNickname().equals(chatSession.getNickname())) throw new Exception("Incorrect behaviour");
		if(!chatHandler.getLastPrivateMessage().equals("Hello myself")) throw new Exception("Incorrect behaviour");
		System.out.println("runPrivateMessageTests successful");
	}
	
	private void runChannelTests() throws Exception{
		ChatHandlerMock chatHandler1 = new ChatHandlerMock();
		ChatSession chatSession1 = chatService.login("nickname1", chatHandler1);
		chatService.joinChannel("channel", chatSession1);
		if(!chatHandler1.getLastChannelSubscribersChanged().equals("channel"))throw new Exception("Incorrect behaviour");
		if(!chatHandler1.getLastChannelSubscribersList()[0].equals("nickname1"))throw new Exception("Incorrect behaviour");
		ChatHandlerMock chatHandler2 = new ChatHandlerMock();
		ChatSession chatSession2 = chatService.login("nickname2", chatHandler2);
		chatService.joinChannel("channel", chatSession2);
		if(chatHandler1.getLastChannelSubscribersList().length != 2)throw new Exception("Incorrect behaviour");
		chatService.leaveChannel("channel", chatSession2);
		if(chatHandler1.getLastChannelSubscribersList().length != 1)throw new Exception("Incorrect behaviour");
		chatService.joinChannel("channel", chatSession2);
		if(chatHandler1.getLastChannelSubscribersList().length != 2)throw new Exception("Incorrect behaviour");
		chatService.logout(chatSession2);
		if(chatHandler1.getLastChannelSubscribersList().length != 1)throw new Exception("Incorrect behaviour");
		System.out.println("runChannelTests successful");
		chatService.logout(chatSession1);
	}
	
	private void runChannelMessageTests() throws Exception{
		ChatHandlerMock chatHandler1 = new ChatHandlerMock();
		ChatSession chatSession1 = chatService.login("nickname1", chatHandler1);
		ChatHandlerMock chatHandler2 = new ChatHandlerMock();
		ChatSession chatSession2 = chatService.login("nickname2", chatHandler2);
		chatService.joinChannel("channel", chatSession1);
		chatService.joinChannel("channel", chatSession2);
		chatService.sendChannelMessage("channel", "Message 1", chatSession1);
		if(!chatHandler1.getLastChannelMessageChannel().equals("channel"))throw new Exception("Incorrect behaviour");
		if(!chatHandler2.getLastChannelMessage().equals("Message 1"))throw new Exception("Incorrect behaviour");
		chatService.sendChannelMessage("channel", "Message 2", chatSession2);
		if(!chatHandler1.getLastChannelMessageNickname().equals("nickname2"))throw new Exception("Incorrect behaviour");
		if(!chatHandler1.getLastChannelMessage().equals("Message 2"))throw new Exception("Incorrect behaviour");
		chatService.joinChannel("channel2", chatSession1);
		chatService.joinChannel("channel2", chatSession2);
		chatService.sendChannelMessage("channel2", "Message 3", chatSession1);
		if(!chatHandler2.getLastChannelMessageChannel().equals("channel2"))throw new Exception("Incorrect behaviour");
		if(!chatHandler2.getLastChannelMessage().equals("Message 3"))throw new Exception("Incorrect behaviour");
		if(!chatHandler2.getLastChannelMessageNickname().equals("nickname1"))throw new Exception("Incorrect behaviour");
		chatService.leaveChannel("channel2", chatSession2);
		if(chatHandler1.getLastChannelSubscribersList().length != 1)throw new Exception("Incorrect behaviour");
		chatService.logout(chatSession2);
		if(chatHandler1.getLastChannelSubscribersList().length != 1)throw new Exception("Incorrect behaviour");
		chatService.logout(chatSession1);
		System.out.println("runChannelMessageTests successful");
	}
	
	private void runNicknameRegistrationTests() throws Exception{
		ChatHandlerMock chatHandler = new ChatHandlerMock();
		ChatSession chatSession;
		try{
			chatSession = chatService.login("nickname","password",chatHandler);
			throw new Exception("Nickname should not be registered");
		} catch (ChatException chatException){
			if(chatException.getType() !=  ChatException.NICKNAME_NOT_REGISTERED_EXCEPTION){
				throw new Exception("Incorrect behavior");
			}
		}
		chatSession = chatService.login("nickname", chatHandler);
		try{
			chatService.changePassword("newpassword", chatSession);
			throw new Exception("Nickname should not be registered");
		} catch (Exception exception){
			System.out.println("Change password successfully failed");
		}
		chatService.registerNickname("password", chatSession);
		chatService.changePassword("newpassword", chatSession);
		chatService.logout(chatSession);
		try{
			chatService.login("nickname", chatHandler);
		} catch (ChatException chatException){
			if(chatException.getType() != ChatException.NICKNAME_REGISTERED_EXCEPTION){
				throw new Exception("Incorrect behavior");
			}
		}
		try{
			chatService.login("nickname","password", chatHandler);
		} catch (ChatException chatException){
			if(chatException.getType() != ChatException.WRONG_PASSWORD_EXCEPTION){
				throw new Exception("Incorrect behavior");
			}
		}
		chatSession = chatService.login("nickname","newpassword", chatHandler);
		chatService.logout(chatSession);
		System.out.println("runNicknameRegistrationTests successful");
	}
	
	private void runIgnoredUsersTests()throws Exception{
		ChatHandlerMock chatHandler1 = new ChatHandlerMock();
		ChatSession chatSession1 = chatService.login("nickname1", chatHandler1);
		String[] ignoredUsers = chatService.getIgnoredUsers(chatSession1);
		if(ignoredUsers != null)throw new Exception("Incorrect behavior");
		chatService.registerNickname("password1", chatSession1);
		ignoredUsers = chatService.getIgnoredUsers(chatSession1);
		if(ignoredUsers.length != 0)throw new Exception("Incorrect behavior");
		ChatHandlerMock chatHandler2 = new ChatHandlerMock();
		ChatSession chatSession2 = chatService.login("nickname2", chatHandler2);
		chatService.registerNickname("nickname2", chatSession2);
		chatService.sendPrivateMessage("nickname1", "Message 1", chatSession2);
		if(!chatHandler1.getLastPrivateMessage().equals("Message 1"))throw new Exception("Incorrect behavior");
		ignoredUsers = chatService.ignoreUser("nickname2", chatSession1);
		if(!ignoredUsers[0].equals("nickname2"))throw new Exception("Incorrect behavior");
		ignoredUsers = chatService.getIgnoredUsers(chatSession1);
		if(!ignoredUsers[0].equals("nickname2"))throw new Exception("Incorrect behavior");
		chatService.sendPrivateMessage("nickname1", "Message 2", chatSession2);
		if(!chatHandler1.getLastPrivateMessage().equals("Message 1"))throw new Exception("Incorrect behavior");
		ignoredUsers = chatService.unignoreUser("nickname2", chatSession1);
		if(ignoredUsers.length != 0)throw new Exception("Incorrect behavior");
		ignoredUsers = chatService.getIgnoredUsers(chatSession1);
		if(ignoredUsers.length != 0)throw new Exception("Incorrect behavior");
		chatService.sendPrivateMessage("nickname1", "Message 3", chatSession2);
		if(!chatHandler1.getLastPrivateMessage().equals("Message 3"))throw new Exception("Incorrect behavior");
		chatService.joinChannel("channel1", chatSession1);
		chatService.joinChannel("channel1", chatSession2);
		chatService.sendChannelMessage("channel1", "Message 4", chatSession1);
		if(!chatHandler2.getLastChannelMessage().equals("Message 4"))throw new Exception("Incorrect behavior");
		chatService.ignoreUser("nickname1", chatSession2);
		chatService.sendChannelMessage("channel1", "Message 5", chatSession1);
		if(!chatHandler2.getLastChannelMessage().equals("Message 4"))throw new Exception("Incorrect behavior");
		chatService.unignoreUser("nickname1", chatSession2);
		chatService.sendChannelMessage("channel1", "Message 6", chatSession1);
		if(!chatHandler2.getLastChannelMessage().equals("Message 6"))throw new Exception("Incorrect behavior");
		chatService.logout(chatSession1);
		chatService.logout(chatSession2);
		System.out.println("runIgnoredUsersTests successful");
	}

}
