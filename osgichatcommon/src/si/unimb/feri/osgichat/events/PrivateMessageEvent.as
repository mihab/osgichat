package si.unimb.feri.osgichat.events
{
	public class PrivateMessageEvent
	{
		
		public var nickname:String;
		
		public var message:String;
		
		[Selector]
		public var type:String;
		
		public function PrivateMessageEvent(type:String,nickname:String,message:String)
		{
			this.type = type;
			this.nickname = nickname;
			this.message = message;
		}
	}
}