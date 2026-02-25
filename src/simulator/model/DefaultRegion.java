package simulator.model;

public class DefaultRegion extends Region {
	

	public void update(double dt) {
	}

	
	public double getFood(AnimalInfo a, double dt) {
		if(a.getDiet() == Diet.CARNIVORE) {
			return 0.0;
		}else {
			//TODO Se puede subir esta funcion a Region
			int n = 0;
			for(Animal an: this.animalList) {
				if(an.getDiet() == Diet.HERBIVORE) n++;
			}
			return 60.0*Math.exp(-Math.max(0,n-5.0)*2.0)*dt;
		}
	}

}
