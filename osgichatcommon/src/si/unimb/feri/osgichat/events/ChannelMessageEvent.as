package si.unimb.feri.osgichat.events
{
	public class ChannelMessageEvent
	{
		
		public var channel:String;
		
		public var nickname:String;
		
		public var message:String;
		
		[Selector]
		public var type:String;
		
		public function ChannelMessageEvent(type:String,channel:String,nickname:String,message:String)
		{
			this.type = type;
			this.channel = channel;
			this.nickname = nickname;
			this.message = message;
		}
	}
}