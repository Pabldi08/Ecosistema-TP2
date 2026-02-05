package simulator.model;

import java.util.List;

public class SelectClosest implements SelectionStrategy {

	@Override
	public Animal select(Animal a, List<Animal> as) {
		Animal closest = null;
		double min = Double.MAX_VALUE;	
		for(Animal an: as) {
			if(an.getPosition().distanceTo(a.getPosition()) < min) {
				min = an.getPosition().distanceTo(a.getPosition());
				closest = an;
			}
		}
		return closest;
	}

}
