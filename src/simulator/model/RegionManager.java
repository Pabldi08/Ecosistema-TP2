package simulator.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class RegionManager implements AnimalMapView {
	
	private int height;
	private int width;
	private int cols;
	private int rows;
	private int regionHeight;
	private int regionWidth;
	
	private Region[][] regions;
	private Map<Animal, Region> animalRegion;
	
	public RegionManager(int cols, int rows, int width, int height) {
		this.cols = cols;
		this.rows = rows;
		this.width = width;
		this.height = height;
		
		this.regionWidth = width/cols;
		this.regionHeight = height/rows;
		
		this.regions = new Region[rows][cols];
		for(int i = 0; i < regions.length; i++) {
			for(int j = 0; j < regions[i].length;j++) {
				this.regions[i][j] = new DefaultRegion();
			}
		}
		
		this.animalRegion = new HashMap<>();
	}

	

	@Override
	public double getFood(AnimalInfo a, double dt) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Animal> getAnimalsInRange(Animal e, Predicate<Animal> filter) {
		// TODO Auto-generated method stub
		return null;
	}
	
	//GETTERS
	public int getCols() {
		return this.cols;
	}

	public int getRows() {
		return this.rows;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public int getRegionWidth() {
		return this.regionWidth;
	}

	public int getRegionHeight() {
		return this.regionHeight;
	}

}
