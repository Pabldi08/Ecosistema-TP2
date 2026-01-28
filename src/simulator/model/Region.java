package simulator.model;

import java.util.List;

public abstract class Region implements Entity, FoodSupplier, RegionInfo {
	
	private List<Animal> list;

	@Override
	public double getFood(AnimalInfo a, double dt) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void update(double dt) {
		// TODO Auto-generated method stub

	}

}
