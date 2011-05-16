package edoor.security;

/**
 * Class representing a user that be managed by security manager
 */
public class User {

	private String _ID = "";
	
	/**
	 * Get identity of user
	 * @return a string with user's identity
	 */
	public String getID() { return _ID; };
	
	/**
	 * Set identity of user
	 * @param id new identity of user
	 * @return 
	 */
	public void setID(String id) { _ID = id; };
}
