package si.unimb.feri.osgichat.events
{
	public class IgnoredUsersEvent
	{
		
		public var ignoredUsers:Array;
		
		public function IgnoredUsersEvent(ignoredUsers:Array=null)
		{
			this.ignoredUsers = ignoredUsers;
		}
	}
}