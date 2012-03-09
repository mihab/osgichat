package si.unimb.feri.osgichat.events
{
	public class JoinChannelEvent
	{
		
		public var channel:String
		
		public function JoinChannelEvent(channel:String)
		{
			this.channel = channel;
		}
	}
}