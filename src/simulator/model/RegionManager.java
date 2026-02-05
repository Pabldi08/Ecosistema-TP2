package simulator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import simulator.misc.Vector2D;

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
	
	void setRegion(int row, int col, Region r) {
		r.setAnimals(regions[row][col].getAnimals());
		this.regions[row][col] = r;
		// TODO: y actualiza sus entradas en animalRegion
	}

	

	@Override
	public double getFood(AnimalInfo a, double dt) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Animal> getAnimalsInRange(Animal e, Predicate<Animal> filter) {
		List<Animal> animalsInRange = new ArrayList<>();
		Vector2D leftSup = new Vector2D(e.getPosition().getX()-e.getSightRange(), e.getPosition().getY()-e.getSightRange());
		Vector2D rightBot = new Vector2D(e.getPosition().getX()+e.getSightRange(), e.getPosition().getY()+e.getSightRange());
		int regionIniX = Math.max(0, (int) (leftSup.getX()/this.regionWidth)); // TODO: preguntar cast
		int regionIniY = Math.max(0, (int) (leftSup.getY()/this.regionHeight));
		int regionFinX = Math.min(regions.length - 1, (int) (rightBot.getX()/this.regionWidth));
		int regionFinY = Math.min(regions[0].length - 1, (int) (rightBot.getY()/this.regionHeight));
		for(int i = regionIniX; i <= regionFinX; i++) {
			for(int j = regionIniY; j <= regionFinY;j++) {
				List<Animal> animalsInRegion = this.regions[i][j].getAnimals();
				for(Animal a: animalsInRegion) {
					if(a != e && e.isInSightRange(a) && filter.test(a)) animalsInRange.add(a);
				}
			}
		}
		return animalsInRange;
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
