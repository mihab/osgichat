package si.unimb.feri.osgichat.events
{
	public class ChannelSubscribersEvent
	{
		
		public var channel:String;
		
		public var subscribers:Array;
		
		public function ChannelSubscribersEvent(channel:String,subscribers:Array)
		{
			this.channel = channel;
			this.subscribers = subscribers;
		}
	}
}