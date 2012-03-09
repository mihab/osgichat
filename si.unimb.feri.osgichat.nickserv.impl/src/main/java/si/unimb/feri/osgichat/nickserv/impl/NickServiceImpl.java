package si.unimb.feri.osgichat.nickserv.impl;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import si.unimb.feri.osgichat.nickserv.NickService;

/**
 * This NickService implementation does not store nicknames persistently to disk but keeps all values in memory.
 */
public class NickServiceImpl implements NickService {
	
	/**
	 * Map of registered nickname passwords
	 */
	private Map<String, String> registeredNicknamePasswords = new Hashtable<String, String>();
	
	/**
	 * Map of registered nicknames to ignored nicknames list for that user
	 */
	private Map<String, List<String>> ignoredNicknames = new Hashtable<String, List<String>>();

	/**
	 * {@inheritDoc} 
	 */
	@Override
	public boolean checkRegistered(String nickname) {
		if(registeredNicknamePasswords.containsKey(nickname))return true;
		return false;
	}

	/**
	 * {@inheritDoc} 
	 */
	@Override
	public boolean checkPassword(String nickname, String password) {
		if(!checkRegistered(nickname)) throw new RuntimeException("Nickname not registered");
		if(registeredNicknamePasswords.get(nickname).equals(password))return true;
		return false;
	}

	/**
	 * {@inheritDoc} 
	 */
	@Override
	public void registerNickname(String nickname, String password) {
		if(checkRegistered(nickname)) throw new RuntimeException("Nickname already registered");
		registeredNicknamePasswords.put(nickname, password);
	}

	/**
	 * {@inheritDoc} 
	 */
	@Override
	public void changePassword(String nickname, String oldPassword,String newPassword) {
		if(!checkPassword(nickname, oldPassword))throw new RuntimeException("Old password does not match nickname");
		registeredNicknamePasswords.put(nickname, newPassword);
	}

	/**
	 * {@inheritDoc} 
	 */
	@Override
	public String[] getIgnoredUsers(String nickname, String password) {
		if(!checkPassword(nickname, password))throw new RuntimeException("Password does not match nickname");
		if(ignoredNicknames.get(nickname) == null)return new String[0];
		return ignoredNicknames.get(nickname).toArray(new String[0]);
	}

	/**
	 * {@inheritDoc} 
	 */
	@Override
	public String[] ignoreUser(String nicknameToIgnore, String nickname,String password) {
		if(!checkPassword(nickname, password))throw new RuntimeException("Password does not match nickname");
		if(ignoredNicknames.get(nickname) == null){
			ignoredNicknames.put(nickname, new ArrayList<String>());
		}
		if(!ignoredNicknames.get(nickname).contains(nicknameToIgnore))ignoredNicknames.get(nickname).add(nicknameToIgnore);
		return ignoredNicknames.get(nickname).toArray(new String[0]);
	}

	/**
	 * {@inheritDoc} 
	 */
	@Override
	public String[] unignoreUser(String nicknameToUnignore, String nickname,String password) {
		if(!checkPassword(nickname, password))throw new RuntimeException("Password does not match nickname");
		if(ignoredNicknames.get(nickname) == null)return new String[0];
		if(ignoredNicknames.get(nickname).contains(nicknameToUnignore)){
			ignoredNicknames.get(nickname).remove(nicknameToUnignore);
		}
		return ignoredNicknames.get(nickname).toArray(new String[0]);
	}

}
