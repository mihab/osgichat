package si.unimb.feri.osgichat.events
{
	public class ChangePasswordEvent
	{
		
		public var newPassword:String;
		
		public function ChangePasswordEvent(newPassword:String){
			this.newPassword = newPassword;
		}
	}
}