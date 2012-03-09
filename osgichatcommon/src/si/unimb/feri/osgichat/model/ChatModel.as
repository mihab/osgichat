package si.unimb.feri.osgichat.model
{
	import mx.collections.ArrayCollection;

	/**
	 * ChatModel that contains bindable data.
	 */
	public class ChatModel
	{
		[Bindable]
		public var nickname:String;
		
		[Bindable]
		public var registered:Boolean = false;
		
		[Bindable]
		public var ignoredUsers:ArrayCollection = new ArrayCollection();
		
	}
}