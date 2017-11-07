package commons.game.poker;

public enum HandRank {
	
	RoyalFlush{

		@Override
		public String toString() {
			return "Royal Flush";
		}
		
	},
	StraightFlush{
	
		@Override
		public String toString() {
			return "Straight Flush";
		}
	}, 
	FourOfKind{
		@Override
		public String toString() {
			return "Four of a kind";
		}		
	}, 
	FullHouse{
		@Override
		public String toString() {
			return "Full House";
		}		
	}, 
	Flush{
		@Override
		public String toString() {
			return "Flush";
		}		
	}, 
	Straight{
		@Override
		public String toString() {
			return "Straight";
		}		
	}, 
	ThreeOfKind{
		@Override
		public String toString() {
			return "Three of a kind";
		}		
	}, 
	TwoPairs{
		@Override
		public String toString() {
			return "Two Pairs";
		}		
	}, 
	Pair{
		@Override
		public String toString() {
			return "Pair";
		}		
	}, 
	HighCard{
		@Override
		public String toString() {
			return "High Card";
		}		
	}
}
