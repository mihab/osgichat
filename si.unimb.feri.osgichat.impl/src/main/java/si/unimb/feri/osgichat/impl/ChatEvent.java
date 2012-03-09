package si.unimb.feri.osgichat.impl;

/**
 * ChatEvent that can be published on the EventAdmin 
 */
public class ChatEvent {
	
	public final static String PRIVATE_TOPIC_PREFIX = "si/unimb/osgichat/feri/event/private/";
	
	public final static String CHANNEL_TOPIC_PREFIX = "si/unimb/osgichat/feri/event/channel/";
	
	public static final int PRIVATE_MESSAGE = 1;
	
	public static final int CHANNEL_MESSAGE = 2;
	
	public static final int CHANNEL_SUBSCRIBERS_CHANGED = 3;

}
