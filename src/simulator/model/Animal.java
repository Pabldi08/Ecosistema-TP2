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
			//TODO hay que poner algo
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
			this.pos = Vector2D.createRandomPosition(regionMngr.getWidth()-1, regionMngr.getHeight()-1);
		}else {
			
		}
		this.dest = Vector2D.createRandomPosition(regionMngr.getWidth()-1, regionMngr.getHeight()-1);
		
	}
	
	
	public Animal deliverBaby() {
		Animal babyAux;
		babyAux = this.baby;
		this.baby = null;
		return babyAux ;
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
		
		JSONArray pos = new JSONArray();
		pos.put(this.pos.getX());
		pos.put(this.pos.getY());
		
		archivo.put("pos", pos);
		archivo.put("gcode", this.geneticCode);
		archivo.put("diet", this.diet.name());
		archivo.put("state", this.state.name());		
		return archivo;
	}
	
	//GETTERS
	public double getAge() {
		return age;
	}
	
	public Diet getDiet() {
		return diet;
	}

	public Vector2D getDest() {
		return dest;
	}

	public double getEnergy() {
		return energy;
	}

	public double getDesire() {
		return desire;
	}
	
	public double getSpeed() {
		return speed;
	}

	public Vector2D getPos() {
		return pos;
	}
	
	public State getState() {
		return state;
	}
	
	//SETTERS
	public void setEnergy(double energy) {
		this.energy = energy;
	}
	
	public void setAge(double age) {
		this.age = age;
	}

	public void setDesire(double desire) {
		this.desire = desire;
	}

	public void setDest(Vector2D dest) {
		this.dest = dest;
	}
	
	
	
	
}
