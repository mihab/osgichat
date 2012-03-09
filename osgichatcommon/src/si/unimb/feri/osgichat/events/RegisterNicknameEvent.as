package si.unimb.feri.osgichat.events
{
	public class RegisterNicknameEvent
	{
		
		public var password:String;
		
		public function RegisterNicknameEvent(password:String)
		{
			this.password = password;
		}
	}
}