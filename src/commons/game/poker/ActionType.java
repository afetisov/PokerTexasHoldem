package commons.game.poker;

public enum ActionType {
	BET {
		@Override
		public String toString() {
			return "Bet";
		}
	},
	CALL {

		@Override
		public String toString() {
			return "Call";
		}
	},
	CHECK {

		@Override
		public String toString() {
			return "Check";
		}
	},
	FOLD {

		@Override
		public String toString() {
			return "Fold";
		}
	},
	RAISE {

		@Override
		public String toString() {
			return "Raise";
		}
	},
	ALLIN {

		@Override
		public String toString() {
			return "All-in";
		}
	};
}
