package simulator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols;j++) {
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
		a.init(this);	
		getRegion(a).addAnimal(a);
		this.animalRegion.put(a,getRegion(a));
	}
	
	void unregisterAnimal(Animal a) {
		this.animalRegion.get(a).removeAnimal(a);
		this.animalRegion.remove(a);
	}
	
	void updateAnimalRegion(Animal a) {
		Region newRegion = getRegion(a);
		Region oldRegion = animalRegion.get(a);
		if(!newRegion.equals(oldRegion)) {
			newRegion.addAnimal(a);
			oldRegion.removeAnimal(a);
			this.animalRegion.put(a, newRegion);
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
		

		
		int leftSupX = (int)(e.getPosition().getX()-e.getSightRange());
		int leftSupY = (int)(e.getPosition().getY()-e.getSightRange());
		int rightBotX = (int)(e.getPosition().getX()+e.getSightRange());
		int rightBotY = (int)(e.getPosition().getY()+e.getSightRange());
		
		int regionIniX = Math.max(0,leftSupX/this.regionWidth);
		int regionFinX = Math.min(cols - 1, (int) (rightBotX/this.regionWidth));
		
		int regionIniY = Math.max(0, (int) (leftSupY/this.regionHeight));
		int regionFinY = Math.min(rows - 1, (int) (rightBotY/this.regionHeight));
		
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
		int x = Math.min(cols - 1, (int)(a.getPosition().getX() / regionWidth));
		int y = Math.min(rows - 1, (int)(a.getPosition().getY() / regionHeight));
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
	
	public RegionInfo getRegionInfo(int i, int j) {
		return regions[i][j];
	}

	@Override
	public Iterator<RegionData> iterator() {
		return new Iterator<RegionData>(){
			private int currentRow = 0;
	        private int currentCol = 0;

	        @Override
	        public boolean hasNext() {
	            return currentRow < rows;
	        }

	        @Override
	        public RegionData next() {
	            if (!hasNext()) {
	                throw new java.util.NoSuchElementException();
	            }

	            RegionInfo regionInfo = (RegionInfo) regions[currentRow][currentCol];

	            RegionData data = new RegionData(currentRow, currentCol, regionInfo);
	            
	            currentCol++;
	            
	            if (currentCol >= cols) {
	                currentCol = 0;
	                currentRow++;
	            }

	            return data;
	        }
			
		};
	}

}