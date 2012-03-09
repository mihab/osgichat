package si.unimb.feri.osgichat.events
{
	public class LoginEvent
	{
		
		public var nickname:String;
		
		public var password:String;
		
		public function LoginEvent(nickname:String,password:String=null){
			this.nickname = nickname;
			this.password = password;
		}
	}
}