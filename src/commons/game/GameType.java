package commons.game;

public enum GameType {
	FIXED_BETS {
		@Override
		public String getName() {
			return "Fixed bets";
		}
	},
	NOLIMIT {
		@Override
		public String getName() {
			return "No limit bets";
		}
	};
	
	public abstract String getName();
	
	public String toString() {
		return getName();
	}
}
