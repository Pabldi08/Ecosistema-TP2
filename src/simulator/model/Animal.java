package simulator.model;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Utils;
import simulator.misc.Vector2D;

public abstract class Animal implements Entity, AnimalInfo{
	private String geneticCode;
	private Diet diet;
	private State state;
	private Vector2D pos;
	private Vector2D dest;
	private double energy;
	private double speed;
	private double age;
	private double desire;
	private double sightRange;
	private Animal mateTarget;
	private Animal baby;
	private AnimalMapView regionMngr;
	private SelectionStrategy mateStrategy;
	
	protected Animal(String geneticCode, Diet diet, double sightRange,
		double initSpeed, SelectionStrategy mateStrategy, Vector2D pos) {
		
		if(geneticCode.isBlank() || sightRange < 0 || initSpeed < 0 || mateStrategy.equals(null)) {
			throw new IllegalArgumentException();
		}
		if(this.pos.equals(null)) {
			// TODO: rellenar
		}else {
			this.pos = pos;
		}
		this.geneticCode = geneticCode;
		this.diet = diet;
		this.state = State.NORMAL;
		
		this.dest = null;
		this.energy = 100.0;
		this.speed = Utils.getRandomizedParameter(initSpeed, 0.1);
		this.age = 0.0;	
		this.desire = 0.0;
		this.sightRange = sightRange;
		this.mateTarget = null;
		this.baby = null;
		this.regionMngr = null;
		this.mateStrategy = mateStrategy;
	}
	
	protected Animal(Animal p1, Animal p2) {
		this.geneticCode = p1.geneticCode;
		this.diet = p1.diet;
		this.state = State.NORMAL;
		this.pos = p1.getPosition().plus(Vector2D.getRandomVector(-1,1).scale(60.0*(Utils.RAND.nextGaussian()+1)));
		this.dest = null;
		this.energy = (p1.energy + p2.energy)/2;
		this.speed = Utils.getRandomizedParameter((p1.getSpeed()+p2.getSpeed())/2, 0.2);
		this.age = 0.0;	
		this.desire = 0.0;
		this.sightRange = Utils.getRandomizedParameter((p1.getSightRange()+p2.getSightRange())/2, 0.2);
		this.mateTarget = null;
		this.baby = null;
		this.regionMngr = null;
		this.mateStrategy = p2.mateStrategy;
	}
	
	void init(AnimalMapView regMngr) {
		this.regionMngr = regMngr;
		if(this.pos == null) {
			this.pos = Animal.createRandomPosition(regionMngr.getWidth()-1, regionMngr.getHeight()-1);
		} else { // TODO corregir este código para asegurar que siempre está en el mapa
			double x = this.getPosition().getX();
			double y = this.getPosition().getY();
			int width = this.getRegionMngr().getWidth();
			int height = this.getRegionMngr().getHeight();
			
			while (x >= width) x = (x - width);  
			while (x < 0) x = (x + width);  
			while (y >= height) y = (y - height);  
			while (y < 0) y = (y + height);
			
		    this.pos = new Vector2D(x,y);
		}
		this.dest = Animal.createRandomPosition(regionMngr.getWidth()-1, regionMngr.getHeight()-1);
	}
	
	Animal deliverBaby() {
		Animal baby = this.baby;
		this.baby = null;
		return baby;
	}
	
	protected void move(double speed) {
		pos = pos.plus(dest.minus(pos).direction().scale(speed));		
	}
	
	protected void setState(State state) {
	  	this.state = state;
	  	switch (state) {
	  	case NORMAL:
	  		setNormalStateAction();
	  		break;
	  	case MATE:
	  		setMateStateAction();
	  		break;
	  	case HUNGER:
	  		setHungerStateAction();
	  		break;
	  	case DANGER:
	  		setDangerStateAction();
	  		break;
	  	case DEAD:
	  		setDeadStateAction();
	  		break;
	  }
}
	
	abstract protected void setNormalStateAction();
	
	abstract protected void setMateStateAction();
	
	abstract protected void setHungerStateAction();
	
	abstract protected void setDangerStateAction();
	
	abstract protected void setDeadStateAction();
	
	public JSONObject asJSON() {
		JSONObject archivo = new JSONObject();
		JSONArray position = new JSONArray();
		position.put(this.pos.getX());
		position.put(this.pos.getY());
		
		archivo.put("pos", position);
		archivo.put("gcode", this.geneticCode);
		archivo.put("diet", this.diet.name());
		archivo.put("state", this.state.name());
		
		return archivo;
	}
	
	public static Vector2D createRandomPosition(double width, double height) {

	    double x = Utils.RAND.nextDouble() * width;
	    double y = Utils.RAND.nextDouble() * height;
	    
	    return new Vector2D(x, y);
	}
	
	// Getters:

	public Diet getDiet() {
		return diet;
	}

	public Vector2D getDest() {
		return dest;
	}

	public double getEnergy() {
		return energy;
	}

	public double getSpeed() {
		return speed;
	}

	public double getDesire() {
		return desire;
	}
	
	public State getState() {
		return state;
	}
	
	public Animal getMateTarget() {
		return mateTarget;
	}

	
	public Vector2D getPosition() {
		return pos;
	}
	
	public String getGeneticCode() {
		return this.geneticCode;
	}
	
	public double getSightRange() {
		return sightRange;
	}
	public double getAge() {
		return age;
	}
	public Vector2D getDestination() {
		return dest;
	}
	
	public AnimalMapView getRegionMngr() {
		return regionMngr;
	}

	public boolean isPregnant() {
		return this.baby != null;
	}
	// Setters:
	
	public void setBaby(Animal baby) {
		this.baby = baby;
	}
	
	public void setMateTarget(Animal mateTarget) {
		this.mateTarget = mateTarget;
	}

	public void setDesire(double desire) {
		this.desire = desire;
	}
	
	public void setAge(double age) {
		this.age = age;
	}

	public void setEnergy(double energy) {
		this.energy = energy;
	}
	
	public void setDest(Vector2D dest) {
		this.dest = dest;
	}

	
}