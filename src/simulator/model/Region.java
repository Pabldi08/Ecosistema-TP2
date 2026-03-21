package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import simulator.model.Animal.Diet;


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
	
	final void setAnimals(List<Animal> animalList){
	    this.animalList = new ArrayList<>(animalList);
	}
	
	protected int countHerbivores() {
		return this.animalList.stream().filter(an -> an.getDiet() == Diet.HERBIVORE).toList().size();
	}
	
	public JSONObject asJSON() {
		JSONObject obj = new JSONObject();
		JSONArray arr = new JSONArray();
		
		for(Animal a : this.animalList) {
			arr.put(a.asJSON());
		}
		obj.put("animals", arr);
		return obj;
	}
	
	@Override
	public List<AnimalInfo> getAnimalsInfo() {  
		 return new ArrayList<>(animalList); // se puede usar Collections.unmodifiableList(animals);  
	}
}
