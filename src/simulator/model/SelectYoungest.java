package simulator.model;

import java.util.List;

public class SelectYoungest implements SelectionStrategy {

	@Override
	public Animal select(Animal a, List<Animal> as) {
		Animal youngest = null;
		double min = Double.MAX_VALUE;		
		for(Animal an: as) {
			if(an.getAge() < min) {
				youngest = an;
				min = an.getAge();
			}
		}
		return youngest;
	}
}
