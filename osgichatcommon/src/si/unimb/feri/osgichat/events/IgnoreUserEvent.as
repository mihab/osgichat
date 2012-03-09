package si.unimb.feri.osgichat.events
{
	public class IgnoreUserEvent
	{
		
		public var nicknameToIgnore:String;
		
		public function IgnoreUserEvent(nicknameToIgnore:String)
		{
			this.nicknameToIgnore = nicknameToIgnore;
		}
	}
}