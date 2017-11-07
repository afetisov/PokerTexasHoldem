package commons.model;

import java.io.Serializable;

public class PlayersFilter implements Serializable {
	
	private boolean blockedUsers;

	public PlayersFilter() {}
	
	public boolean isBlockedUsers() {
		return blockedUsers;
	}

	public void setBlockedUsers(boolean blocked) {
		this.blockedUsers = blocked;
	}
}
