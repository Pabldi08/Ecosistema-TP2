package simulator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.json.JSONArray;
import org.json.JSONObject;

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
		for(Animal a: regions[row][col].getAnimals()) {
			animalRegion.put(a, r);
		}
	}
	
	void registerAnimal(Animal a) {
		a.init(this);							//TODO: El init va aqui?
		getRegion(a).addAnimal(a);
		this.animalRegion.put(a,getRegion(a));
	}
	
	void unregisterAnimal(Animal a) {
		this.animalRegion.get(a).removeAnimal(a);
		this.animalRegion.remove(a);
	}
	
	void updateAnimalRegion(Animal a) {
		if(!getRegion(a).equals(animalRegion.get(a))) {
			this.animalRegion.put(a, getRegion(a));
		}
	}
	
	@Override
	public double getFood(AnimalInfo a, double dt) {
		return this.animalRegion.get(a).getFood(a, dt);
	}
	
	void updateAllRegions(double dt) {
		for(Region[] x: regions) {
			for(Region r: x) {
				r.update(dt);
			}
		}
	}

	@Override
	public List<Animal> getAnimalsInRange(Animal e, Predicate<Animal> filter) {
		List<Animal> animalsInRange = new ArrayList<>();
		
		Vector2D leftSup = new Vector2D(e.getPosition().getX()-e.getSightRange(), e.getPosition().getY()-e.getSightRange());
		Vector2D rightBot = new Vector2D(e.getPosition().getX()+e.getSightRange(), e.getPosition().getY()+e.getSightRange());
		
		int regionIniX = Math.max(0, (int) (leftSup.getX()/this.regionWidth));
		int regionFinX = Math.min(cols - 1, (int) (rightBot.getX()/this.regionWidth));
		
		int regionIniY = Math.max(0, (int) (leftSup.getY()/this.regionHeight));
		int regionFinY = Math.min(rows - 1, (int) (rightBot.getY()/this.regionHeight));
		
		for(int i = regionIniX; i <= regionFinX; i++) {
			for(int j = regionIniY; j <= regionFinY;j++) {
				List<Animal> animalsInRegion = this.regions[j][i].getAnimals();
				for(Animal a: animalsInRegion) {
					if(a != e && e.isInSightRange(a) && filter.test(a)) animalsInRange.add(a);
				}
			}
		}
		return animalsInRange;
	}
	
	public JSONObject asJSON() {
		JSONObject obj = new JSONObject();
		JSONArray arr = new JSONArray();
		for(int i = 0; i < regions.length; i++) {
			for(int j = 0; j < regions[i].length; j++) {
				JSONObject aux = new JSONObject();
				aux.put("row",i);
				aux.put("col",j);
				aux.put("data", regions[i][j].asJSON());
				arr.put(aux);
			}
			
		}
		obj.put("regions", arr);
		return obj;
	}
	
	private Region getRegion (Animal a) {
		int x = (int) a.getPosition().getX()/this.regionWidth;
		int y = (int) a.getPosition().getY()/this.regionHeight;
		return this.regions[y][x];
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