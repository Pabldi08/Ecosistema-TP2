package simulator.model;

import simulator.misc.Utils;

public class DynamicSupplyRegion extends Region{
	
	private double food;
	private double factor;

	
	public DynamicSupplyRegion(double initFood, double growthFactor) {
		this.food = initFood;
		this.factor = growthFactor;
	}

	@Override
	public void update(double dt) {
		if(Utils.RAND.nextDouble() >0.5) {
			this.food *= dt*factor;
		}
	}
	
	@Override
	public double getFood(AnimalInfo a, double dt) {
		if(a.getDiet() == Diet.CARNIVORE) {
			return 0.0;
		}else {
			//TODO lo mismo que en default region
			int n = 0;
			for(Animal an: this.animalList) {
				if(an.getDiet() == Diet.HERBIVORE) n++;
			}
			double comida = Math.min(food,60.0*Math.exp(-Math.max(0,n-5.0)*2.0)*dt);
			this.food -= comida;
			
			return comida;
		}
	}


}
