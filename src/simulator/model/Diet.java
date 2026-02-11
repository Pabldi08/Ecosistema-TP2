package simulator.model;

public enum Diet {
	HERBIVORE, CARNIVORE;
	
	// TODO: se pueden hacer métodos públicos aquí?
	public static Diet stringToEnum(String diet) {
		switch(diet.toLowerCase()) {
			case "herbivore": return Diet.HERBIVORE;
			case "carnivore": return Diet.CARNIVORE;
			default: return null;
		}
	}
}