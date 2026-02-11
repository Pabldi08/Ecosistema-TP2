package simulator.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
		Region R = new DefaultRegion();
		JSONArray animals = rJson.getJSONArray("animals");
		Iterator<Object> it = animals.iterator();
		while(it.hasNext()) {
			JSONObject animal = (JSONObject) it.next();
			this.addAnimal(animal);
			//R.addAnimal();
		}
		this.setRegion(row, col, R);
	}
	
	private void addAnimal (Animal a) {
		this.simulatedAnimals.add(a);
		this.rgnMngr.registerAnimal(a);
	}
	
	public void addAnimal(JSONObject aJson) {
		JSONArray pos = animal.getJSONArray("pos");
		Vector2D position = new Vector2D(pos.getDouble(0),pos.getDouble(1));
		String gcode = animal.getString("gcode");
		String diet = animal.getString("diet");
		String state = animal.getString("state");
		Diet dieta = Diet.stringToEnum(diet); // TODO: completar el new Animal
		State estado = State.stringToEnum(state);
		Animal a = new Animal(gcode,Diet.stringToEnum(diet),20.0,21.0,null,position);
	}
	
}
