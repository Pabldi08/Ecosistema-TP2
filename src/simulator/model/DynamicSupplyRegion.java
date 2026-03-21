package simulator.model;

import simulator.misc.Utils;
import simulator.model.Animal.Diet;


public class DynamicSupplyRegion extends Region{
	
	@Override
	public String toString() {
		return "DynamicSupplyRegion: dynamic food supply.";
	}

	private double food;
	private double factor;

	
	public DynamicSupplyRegion(double initFood, double growthFactor) {
		this.food = initFood;
		this.factor = growthFactor;
	}

	@Override
	public void update(double dt) {
		if(Utils.RAND.nextDouble() >0.5) {
			this.food += dt*factor;
		}
	}
	
	@Override
	public double getFood(AnimalInfo a, double dt) {
		if(a.getDiet() == Diet.CARNIVORE) {
			return 0.0;
		}else {
			int n = countHerbivores();
			double comida = Math.min(food,60.0*Math.exp(-Math.max(0,n-5.0)*2.0)*dt);
			this.food -= comida;
			
			return comida;
		}
	}


}
