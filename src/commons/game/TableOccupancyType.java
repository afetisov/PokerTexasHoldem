package commons.game;

public enum TableOccupancyType {
	EMPTY {
		@Override
		public String getName() {
			return "Empty";
		}
	},
	FULL {
		@Override
		public String getName() {
			return "Full";
		}
	},
	NOT_FULL {
		@Override
		public String getName() {
			return "Not full";
		}
	};
	
	public abstract String getName();
	
	public String toString() {
		return getName();
	}
	
	public static final short MAX_PLAYERS = 10;
}
