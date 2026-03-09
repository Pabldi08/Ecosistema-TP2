package simulator.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

import org.json.JSONObject;

import simulator.factories.Factory;

public class Simulator implements JSONable, Observable<EcoSysObserver>{
	
	private Factory<Animal> animalsFactory;
	private Factory<Region> regionsFactory;
	private RegionManager rgnMngr;
	private double time;
	private List<Animal> simulatedAnimals;
	private List<EcoSysObserver> observerList;
	
	public Simulator(int cols, int rows, int width, int height,   
            Factory<Animal> animalsFactory, Factory<Region> regionsFactory) {
		this.time = 0.0;
		this.simulatedAnimals = new ArrayList<>();
		this.animalsFactory = animalsFactory;
		this.regionsFactory = regionsFactory;
		this.rgnMngr = new RegionManager(cols, rows, width, height);
		this.observerList = new ArrayList<>();
	}
	
	private void setRegion(int row, int col, Region r) {
		this.rgnMngr.setRegion(row, col, r);
	}
	
	public void setRegion(int row, int col, JSONObject rJson) {		
		Region R = this.regionsFactory.createInstance(rJson);	
		this.setRegion(row, col, R);
		this.notifyOnRegionSet(row, col, R);
	}
	
	private void addAnimal (Animal a) {
		this.simulatedAnimals.add(a);
		this.rgnMngr.registerAnimal(a);
	}
	
	public void addAnimal(JSONObject aJson) {
		Animal a = this.animalsFactory.createInstance(aJson);
		this.addAnimal(a);
		notifyOnAnimalAdded(a);
	}
	
	public MapInfo getMapInfo() {
		return this.rgnMngr;
	}
	
	public List<? extends AnimalInfo> getAnimals(){
		return Collections.unmodifiableList(this.simulatedAnimals);
	}
	
	public double getTime() {
		return this.time;
	}
	
	public void advance(double dt) {
		this.time += dt;						//i
		
		List<Animal> aux = new ArrayList<>();	//ii
		for(Animal a: this.simulatedAnimals) {	
			if(a.getState() == Animal.State.DEAD) {
				aux.add(a);
				this.rgnMngr.unregisterAnimal(a);
			}
		}
		this.simulatedAnimals.removeAll(aux);
		
		for(Animal a: this.simulatedAnimals) {	//iii
			a.update(dt);
			this.rgnMngr.updateAnimalRegion(a);
		}
		this.rgnMngr.updateAllRegions(dt);
		
		aux.clear();
		for(Animal a: this.simulatedAnimals) {	//iv
			if(a.isPregnant()) {
				Animal baby = a.deliverBaby();
				aux.add(baby);
			}
		}
		for(Animal a: aux) {
			addAnimal(a);
		}
		this.notifyOnAdvance(dt);
	}
	
	public JSONObject asJSON() {
		JSONObject obj = new JSONObject();
		obj.put("time", this.time);
		obj.put("state", this.rgnMngr.asJSON());
		return obj;
	}
	
	public void reset(int cols, int rows, int width, int height) {
		this.simulatedAnimals = new ArrayList<>();
		this.rgnMngr = new RegionManager(cols, rows, width, height);
		this.time = 0.0;
		this.notifyOnReset();
	}

	public void addObserver(EcoSysObserver o) {
		this.observerList.add(o);
		notifyOnRegister(o);
	}

	@Override
	public void removeObserver(EcoSysObserver o) {
		this.observerList.remove(o);
	}
	
	private void notifyOnAdvance(double dt) {  
		  List<AnimalInfo> animals = new ArrayList<>(simulatedAnimals);
		  for(EcoSysObserver o : this.observerList) {
				o.onAdvance(time, rgnMngr, animals, dt);
			}
		}

	private void notifyOnReset() {
		ArrayList<AnimalInfo> animalInfo = new ArrayList<>(simulatedAnimals);
		for(EcoSysObserver o : this.observerList) {
			o.onReset(time, rgnMngr, animalInfo);
		}
	}
	
	private void notifyOnAnimalAdded(Animal a) {
		ArrayList<AnimalInfo> animalInfo = new ArrayList<>(simulatedAnimals);
		for(EcoSysObserver o : this.observerList) {
			o.onAnimalAdded(time, rgnMngr, animalInfo, a);
		}
	}
	
	private void notifyOnRegionSet(int row, int col, Region R) {
		ArrayList<AnimalInfo> animalInfo = new ArrayList<>(simulatedAnimals);
		for(EcoSysObserver o : this.observerList) {
			o.onRegionSet(row, col, rgnMngr, R);
		}
	}
	
	private void notifyOnRegister(EcoSysObserver o) {
		ArrayList<AnimalInfo> animalInfo = new ArrayList<>(simulatedAnimals);
		o.onRegister(time, rgnMngr, animalInfo);
	}

}
