package si.unimb.feri.osgichat.controller
{
	import flash.events.TimerEvent;
	import flash.utils.Timer;
	
	import mx.messaging.Consumer;
	import mx.messaging.events.MessageEvent;
	import mx.rpc.AsyncToken;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.remoting.RemoteObject;
	
	import si.unimb.feri.osgichat.events.ChangePasswordEvent;
	import si.unimb.feri.osgichat.events.ChannelMessageEvent;
	import si.unimb.feri.osgichat.events.ChannelSubscribersEvent;
	import si.unimb.feri.osgichat.events.IgnoreUserEvent;
	import si.unimb.feri.osgichat.events.IgnoredUsersEvent;
	import si.unimb.feri.osgichat.events.JoinChannelEvent;
	import si.unimb.feri.osgichat.events.LeaveChannelEvent;
	import si.unimb.feri.osgichat.events.LoginEvent;
	import si.unimb.feri.osgichat.events.LogoutEvent;
	import si.unimb.feri.osgichat.events.PrivateMessageEvent;
	import si.unimb.feri.osgichat.events.RegisterNicknameEvent;
	import si.unimb.feri.osgichat.events.UnignoreUserEvent;
	import si.unimb.feri.osgichat.model.ChatModel;

	/**
	 * ChatService controller that contains the logic for connecting to the backend logic
	 */
	public class ChatServiceController
	{
		/**
		 * ID received upon successful login
		 */
		private var id:String;
		
		/**
		 * Timer to prevent timeout from server
		 */
		private var timer:Timer = new Timer(30*1000);
		
		/**
		 * Consumer to subscribe to messaging destination
		 */
		private var _consumer:Consumer;
		
		[MessageDispatcher]
		public var dispatcher:Function;
		
		/**
		 * RemoteObject object properly configured to connect to service
		 */
		[Inject(id="chatService")]
		public var chatService:RemoteObject;
		
		/**
		 * ChatModel to update
		 */
		[Inject]
		public var chatModel:ChatModel;
		
		public function ChatServiceController(){
			timer.addEventListener(TimerEvent.TIMER,sendKeepAlive);
		}
		
		[Inject(id="consumer")]
		public function set consumer(value:Consumer):void{
			_consumer = value;
			_consumer.addEventListener(MessageEvent.MESSAGE,receivedMessage);
		}
		
		[Command]
		public function login(event:LoginEvent):AsyncToken {
			if(event.password != null)return chatService.login(event.nickname,event.password);
			return chatService.login(event.nickname);
		}
		
		[CommandResult]
		public function loginResult(id:String, trigger:LoginEvent):void {
			this.id = id;
			chatModel.nickname = trigger.nickname;
			_consumer.destination = trigger.nickname;
			_consumer.subscribe();
			timer.reset();
			timer.start();
			if(trigger.password != null){
				chatModel.registered = true;
				dispatcher(new IgnoredUsersEvent());
			}
		}
		
		[Command]
		public function logout(event:LogoutEvent):AsyncToken{
			timer.stop();
			_consumer.unsubscribe();
			chatModel.nickname = null;
			chatModel.registered = false;
			return chatService.logoutUser(id);
		}
		
		[Command]
		public function registerNickname(event:RegisterNicknameEvent):AsyncToken{
			return chatService.registerNickname(event.password,id);
		}
		
		[CommandResult]
		public function registerNicknameResult(result:ResultEvent,trigger:RegisterNicknameEvent):void {
			chatModel.registered = true;
			dispatcher(new IgnoredUsersEvent());
		}
		
		[Command]
		public function changePassword(event:ChangePasswordEvent):AsyncToken{
			return chatService.changePassword(event.newPassword,id);
		}
		
		[Command]
		public function getIgnoredUsers(event:IgnoredUsersEvent):AsyncToken{
			return chatService.getIgnoredUsers(id);
		}
		
		[CommandResult]
		public function getIgnoredUsersResult(ignoredUsers:Array,trigger:IgnoredUsersEvent):void{
			chatModel.ignoredUsers.source = ignoredUsers;
			chatModel.ignoredUsers.refresh();
		}
		
		[Command]
		public function ignoreUser(event:IgnoreUserEvent):AsyncToken{
			return chatService.ignoreUser(event.nicknameToIgnore,id);
		}
		
		[CommandResult]
		public function ignoreUserResult(ignoredUsers:Array,trigger:IgnoreUserEvent):void{
			chatModel.ignoredUsers.source = ignoredUsers;
			chatModel.ignoredUsers.refresh();
		}
		
		[Command]
		public function unignoreUser(event:UnignoreUserEvent):AsyncToken{
			return chatService.unignoreUser(event.nicknameToUnignore,id);
		}
		
		[CommandResult]
		public function unignoreUserResult(ignoredUsers:Array,trigger:UnignoreUserEvent):void{
			chatModel.ignoredUsers.source = ignoredUsers;
			chatModel.ignoredUsers.refresh();
		}
		
		[Command]
		public function joinChannel(event:JoinChannelEvent):AsyncToken{
			return chatService.joinChannel(event.channel,id);
		}
		
		[Command]
		public function leaveChannel(event:LeaveChannelEvent):AsyncToken{
			return chatService.leaveChannel(event.channel,id);
		}
		
		[MessageHandler(selector="send")]
		public function sendChannelMessage(event:ChannelMessageEvent):void{
			chatService.sendChannelMessage(event.channel,event.message,id);
		}
		
		[MessageHandler(selector="send")]
		public function sendPrivateMessage(event:PrivateMessageEvent):void{
			chatService.sendPrivateMessage(event.nickname,event.message,id);
		}
		
		/**
		 * Received message from server, dispatch appropriate event
		 */
		private function receivedMessage(event:MessageEvent):void{
			var type:int = event.message.body.type;
			switch(type){
				case 1:
					var fromNickname:String = event.message.body.fromNickname;
					var message:String = event.message.body.message;
					dispatcher(new PrivateMessageEvent("receive",fromNickname,message));
					break;
				case 2:
					var fromChannel:String = event.message.body.fromChannel;
					fromNickname = event.message.body.fromNickname;
					message = event.message.body.message;
					dispatcher(new ChannelMessageEvent("receive",fromChannel,fromNickname,message));
					break;
				case 3:
					var channel:String = event.message.body.channel;
					var subscribers:Array = event.message.body.subscribers;
					dispatcher(new ChannelSubscribersEvent(channel,subscribers));
					break;
				default:
					throw new Error("Unknown message received");
			}
		}
		
		/**
		 * Send keep alive to server to we dont timeout
		 */
		private function sendKeepAlive(event:TimerEvent):void{
			chatService.keepAlive(id);
		}
	}
}