package client.game.gui.components;

public enum ToggleButtonType {
	FOLD_CHECK {
		@Override
		public String getText() {
			return "Fold/Check";
		}
	},
	FOLD {
		@Override
		public String getText() {
			return "Fold";
		}
	},
	CHECK {
		@Override
		public String getText() {
			return "Check";
		}
	},
	CALL {
		@Override
		public String getText() {
			return "Call";
		}
	};
	
	public abstract String getText();
}
