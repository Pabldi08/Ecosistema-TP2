package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public abstract class Region implements Entity, FoodSupplier, RegionInfo {
	protected List<Animal> animalList;
	
	public Region() {
		animalList = new ArrayList<>();
	}
	
	final void addAnimal(Animal a) {
		this.animalList.add(a);
	}
	
	final void removeAnimal(Animal a) {
		this.animalList.remove(a);
	}
	
	final List<Animal> getAnimals(){
		return Collections.unmodifiableList(this.animalList);
	}
	
	public JSONObject asJSON() {
		JSONObject obj = new JSONObject();
		JSONArray arr = new JSONArray();
		
		for(Animal a : this.animalList) {
			arr.put(a.asJSON());
		}
		obj.put("animal", arr);
		return obj;
	}
}
