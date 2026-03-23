package simulator.model;

import java.util.List;

import simulator.model.Animal.Diet;

public class DefaultRegion extends Region {
	

	@Override
	public String toString() {
		return "default";
	}


	public void update(double dt) {
	}

	
	public double getFood(AnimalInfo a, double dt) {
		if(a.getDiet() == Diet.CARNIVORE) {
			return 0.0;
		}else {
			int n = countHerbivores();
			return 60.0*Math.exp(-Math.max(0,n-5.0)*2.0)*dt;
		}
	}

}
