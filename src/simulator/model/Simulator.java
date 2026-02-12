package simulator.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Collections;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.factories.Factory;
import simulator.misc.Vector2D;

public class Simulator implements JSONable {
	
	private Factory<Animal> animalsFactory;
	private Factory<Region> regionsFactory;
	private RegionManager rgnMngr;
	private double dt;
	private List<Animal> simulatedAnimals;
	
	public Simulator(int cols, int rows, int width, int height,   
            Factory<Animal> animalsFactory, Factory<Region> regionsFactory) {
		this.dt = 0.0;
		this.simulatedAnimals = new ArrayList<>();
		this.animalsFactory = animalsFactory;
		this.regionsFactory = regionsFactory;
		this.rgnMngr = new RegionManager(cols, rows, width, height);
	}
	
	private void setRegion(int row, int col, Region r) {
		this.rgnMngr.setRegion(row, col, r);
	}
	
	void setRegion(int row, int col, JSONObject rJson) {
		Region R = this.regionsFactory.createInstance(rJson);
		this.setRegion(row, col, R);
	}
	
	private void addAnimal (Animal a) {
		this.simulatedAnimals.add(a);
		this.rgnMngr.registerAnimal(a);
	}
	
	public void addAnimal(JSONObject aJson) {
		Animal a = this.animalsFactory.createInstance(aJson);
		this.addAnimal(a);
	}
	
	public MapInfo getMapInfo() {
		return this.rgnMngr;
	}
	
	public List<? extends AnimalInfo> getAnimals(){
		return Collections.unmodifiableList(this.simulatedAnimals);
	}
	
	public double getTime() {
		return this.dt;
	}
}
