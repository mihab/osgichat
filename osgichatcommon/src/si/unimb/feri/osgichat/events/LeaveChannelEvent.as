package si.unimb.feri.osgichat.events
{
	public class LeaveChannelEvent
	{
		
		public var channel:String
		
		public function LeaveChannelEvent(channel:String)
		{
			this.channel = channel;
		}
	}
}