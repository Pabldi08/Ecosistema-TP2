package simulator.model;

import java.util.List;

import simulator.misc.Utils;
import simulator.misc.Vector2D;

public class Sheep extends Animal {
	
	private Animal dangerSource;
	private SelectionStrategy dangerStrategy;
	
	
	public Sheep(SelectionStrategy mateStrategy, SelectionStrategy dangerStrategy,  Vector2D pos) {
		super(Constants.SHEEP_GENETIC_CODE, Diet.HERBIVORE, Constants.INIT_SIGHT_SHEEP, Constants.INIT_SPEED_SHEEP, mateStrategy, pos);
		this.dangerStrategy = dangerStrategy;
		this.dangerSource= null; 
	}
	
	protected Sheep(Sheep p1, Animal p2) {
		super(p1,p2);
		this.dangerStrategy = p1.dangerStrategy;
		this.dangerSource= null;
	}

	@Override
	public void update(double dt) {
		if(this.getState() != State.DEAD) {
			switch (this.getState()) {
		  	case NORMAL:
		  		applyNormalState(dt);
		  		break;
		  	case DANGER:
		  		applyDangerState(dt);
		  		break;
		  	case MATE:
		  		applyMateState(dt);
		  		break;
			default:
				break;
		  	}
			if(this.adjustPosition()) this.setState(State.NORMAL);
			if(this.getEnergy() == 0.0 || this.getAge() >Constants.MAX_AGE_SHEEP) this.setState(State.DEAD);
			if(this.getState() != State.DEAD){
				this.setEnergy(this.getEnergy()+this.getRegionMngr().getFood(this, dt));
			}
		}
	}	
	
	public void applyNormalState(double dt) {
		if(this.getDestination().distanceTo(this.getPosition())< Constants.COLLISION_RANGE) {
			setDest(Animal.createRandomPosition(this.getRegionMngr().getWidth() - 1, this.getRegionMngr().getHeight() - 1));
		}
		moveIn(dt); // TODO: subir a Animal y hacer genérico
		if(this.dangerSource == null) {
			List<Animal> animalsInRange = this.getRegionMngr().getAnimalsInRange(this, (a)-> a.getDiet() == Diet.CARNIVORE);
			this.dangerSource = this.dangerStrategy.select(this, animalsInRange);
		}
		if(this.dangerSource != null){
			this.setState(State.DANGER);
		}else {
			if(this.getDesire() > Constants.DESIRE_THRESHOLD_SHEEP) {
				this.setState(State.MATE);
			}
		}
	}
	
	public void applyDangerState(double dt) {
		if(this.dangerSource != null && this.dangerSource.getState() == State.DEAD ) {
			this.dangerSource = null;
		}
		if(this.dangerSource == null) {
			moveIn(dt);
		}else {
			this.setDest(this.getPosition().plus(this.getPosition().minus(dangerSource.getPosition()).direction()));
			this.move(Constants.BOOST_FACTOR_SHEEP*this.getSpeed()*dt*Math.exp((this.getEnergy()-Constants.MAX_ENERGY)*Constants.HUNGER_DECAY_EXP_FACTOR));
			this.setAge(this.getAge()+dt); 
			
			double newEnergy = this.getEnergy() - Constants.FOOD_DROP_RATE_SHEEP * Constants.FOOD_DROP_BOOST_FACTOR_SHEEP*dt;
			this.setEnergy(newEnergy);
			
			double newDesire = this.getDesire() + Constants.DESIRE_INCREASE_RATE_SHEEP*dt;//1.v
			this.setDesire(newDesire);
		}
		if(this.dangerSource == null || !isInSightRange(this.dangerSource)) {
			List<Animal> animalsInRange = this.getRegionMngr().getAnimalsInRange(this, (a)-> a.getDiet() == Diet.CARNIVORE);
			this.dangerSource = this.dangerStrategy.select(this, animalsInRange);
			if(this.dangerSource == null) {
				if(this.getDesire() < Constants.DESIRE_THRESHOLD_SHEEP) this.setState(State.NORMAL);
				else this.setState(State.MATE);
			}
		}
	}
	
	private void applyMateState(double dt) {
		if(this.getMateTarget() != null && (this.getMateTarget().getState() == State.DEAD) || !isInSightRange(this.getMateTarget()))  {
			this.setMateTarget(null);
		}
		if(this.getMateTarget() == null){
			List<Animal> animalsInRange = this.getRegionMngr().getAnimalsInRange(this, (a)-> a.getGeneticCode() == Constants.SHEEP_GENETIC_CODE);
			this.setMateTarget(this.getMateStrategy().select(this, animalsInRange));
			if(this.getMateTarget() == null) moveIn(dt);
		} else {
			this.setDest(this.getMateTarget().getPosition()); //2.i
			this.move(Constants.BOOST_FACTOR_SHEEP*this.getSpeed()*dt*Math.exp((this.getEnergy()-Constants.MAX_ENERGY)*Constants.HUNGER_DECAY_EXP_FACTOR)); // 2.ii
			this.setAge(this.getAge()+dt); //2.iii
			
			double newEnergy = this.getEnergy() - Constants.FOOD_DROP_RATE_SHEEP * Constants.FOOD_DROP_BOOST_FACTOR_SHEEP*dt; //2. iv
			this.setEnergy(newEnergy);			
			
			double newDesire = this.getDesire() + Constants.DESIRE_INCREASE_RATE_SHEEP*dt;//2.v
			this.setDesire(newDesire);
	
			if(this.getMateTarget().getPosition().distanceTo(this.getPosition()) < Constants.COLLISION_RANGE) {
				this.setDesire(0.0);
				this.getMateTarget().setDesire(0.0);
				if(!this.isPregnant()) {
					if(Utils.RAND.nextDouble() < Constants.PREGNANT_PROBABILITY_SHEEP) this.setBaby(new Sheep(this, this.getMateTarget()));
				}
				this.setMateTarget(null);
			}
		}
			
		if(this.dangerSource == null) {
			List<Animal> animalsInRange = this.getRegionMngr().getAnimalsInRange(this, (a)-> a.getDiet() == Diet.CARNIVORE);
			this.dangerSource = this.dangerStrategy.select(this, animalsInRange);
		}
		if(this.dangerSource != null) this.setState(State.DANGER);
		else if (this.getDesire() < Constants.DESIRE_THRESHOLD_SHEEP) this.setState(State.NORMAL);
	}
	

	@Override
	protected void setNormalStateAction() {
		this.dangerSource = null;
		this.setMateTarget(null);
	}
	
	@Override
	protected void setDangerStateAction() {
		this.setMateTarget(null);
	}
	
	@Override
	protected void setMateStateAction() {	
		this.dangerSource = null;
	}
	
	@Override
	protected void setHungerStateAction() {}

	@Override
	protected void setDeadStateAction() {}
	
	private void moveIn(double dt) {
		this.move(this.getSpeed()*dt*Math.exp((this.getEnergy()-Constants.MAX_ENERGY)*Constants.HUNGER_DECAY_EXP_FACTOR));
		this.setAge(this.getAge()+dt);
		
		double newEnergy = this.getEnergy() - Constants.FOOD_DROP_RATE_SHEEP * dt;
		this.setEnergy(newEnergy);
		
		double newDesire = this.getDesire() + Constants.DESIRE_INCREASE_RATE_SHEEP * dt; 
		this.setDesire(newDesire);
	}	
}
