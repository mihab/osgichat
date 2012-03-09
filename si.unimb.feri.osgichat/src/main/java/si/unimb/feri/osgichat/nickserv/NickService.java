package si.unimb.feri.osgichat.nickserv;

/**
 * NickService provides nickname registration and other nickname services to the ChatService. Registration
 * protects the nickname with a password and the correct combination of nickname and password must be provided on
 * each sequential access of the NickService. NickService implementations can choose to persistently store registered
 * nicknames with the properties.
 */
public interface NickService {
	
	/**
	 * Check whether the nickname is registered with this NickService
	 * @param nickname Nickname to check
	 * @return true if nickname registered with this NickService, false otherwise
	 */
	boolean checkRegistered(String nickname);
	
	/**
	 * Check whether the given password is the correct password for the given nickname
	 * @param nickname Nickname to check
	 * @param password Password to check if it matches the given nickname
	 * @return true if combination matches, otherwise false
	 */
	boolean checkPassword(String nickname,String password);
	
	/**
	 * Registers the given nickname with the given password. Each nickname can be registered only once
	 * @param nickname Nickname to register
	 * @param password Password to protect the nickname with
	 */
	void registerNickname(String nickname,String password);
	
	/**
	 * Changes the password of the nickname
	 * @param nickname Nickname to change password of
	 * @param oldPassword Old password
	 * @param newPassword New password
	 */
	void changePassword(String nickname,String oldPassword,String newPassword);
	
	/**
	 * Get ignored users list
	 * @param nickname Nickname to get list for
	 * @param password Valid password of nickname
	 * @return Returns String array of currently ignored user nicknames
	 */
	String[] getIgnoredUsers(String nickname,String password);
	
	/**
	 * Adds nicknameToIgnore to ignored users list
	 * @param nicknameToIgnore Nickname to add to ignored users list
	 * @param nickname Nickname the list belongs to
	 * @param password Password of nickname the list belongs to
	 * @return Returns String array of currently ignored user nicknames
	 */
	String[] ignoreUser(String nicknameToIgnore,String nickname,String password);
	
	/**
	 * Removes nicknameToUnignore from ignored users list
	 * @param nicknameToUnignore Nickname to remove from ignored users list
	 * @param nickname Nickname the list belongs to
	 * @param password Password of nickname the list belongs to
	 * @return Returns String array of currently ignored user nicknames
	 */
	String[] unignoreUser(String nicknameToUnignore,String nickname,String password);

}
