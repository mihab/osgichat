package si.unimb.feri.osgichat.events
{
	public class UnignoreUserEvent
	{
		
		public var nicknameToUnignore:String;
		
		public function UnignoreUserEvent(nicknameToUnignore:String)
		{
			this.nicknameToUnignore = nicknameToUnignore;
		}
	}
}