package simulator.model;

public enum State {
	NORMAL, MATE, HUNGER, DANGER, DEAD;
	
	public static State stringToEnum(String state) {
		switch(state.toLowerCase()) {
			case "normal": return State.NORMAL;
			case "mate": return State.MATE;
			case "hunger": return State.HUNGER;
			case "danger": return State.DANGER;
			case "dead": return State.DEAD;
			default: return null;
		}
	}
}
