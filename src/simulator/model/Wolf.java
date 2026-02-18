package simulator.model;

import java.util.List;

import simulator.misc.Utils;
import simulator.misc.Vector2D;

public class Wolf extends Animal {
	
	private Animal huntTarget;
	private SelectionStrategy huntingStrategy;
	
	
	public Wolf(SelectionStrategy mateStrategy, SelectionStrategy huntingStrategy,  Vector2D pos) {
		super(Constants.WOLF_GENETIC_CODE, Diet.CARNIVORE, Constants.INIT_SIGHT_WOLF, Constants.INIT_SPEED_WOLF, mateStrategy, pos);
		this.huntingStrategy = huntingStrategy;
		this.huntTarget = null;
	}
	
	protected Wolf(Wolf p1, Animal p2) {
		super(p1,p2);
		this.huntingStrategy = p1.huntingStrategy;
		this.huntTarget = null;
	}

	@Override
	public void update(double dt) {
		if(this.getState() != State.DEAD) {
			switch (this.getState()) {
		  	case NORMAL:
		  		applyNormalState(dt);
		  		break;
		  	case HUNGER:
		  		applyHungerState(dt);
		  		break;
		  	case MATE:
		  		applyMateState(dt);
		  		break;
			default:
				break;
		  	}
			if(this.adjustPosition()) this.setState(State.NORMAL);
			if(this.getEnergy() == 0.0 || this.getAge() > Constants.MAX_AGE_WOLF) this.setState(State.DEAD);
			if(this.getState() != State.DEAD){
				this.setEnergy(this.getEnergy()+this.getRegionMngr().getFood(this, dt));
			}
		}
	}
	
	private void applyNormalState(double dt) {
		if(this.getDestination().distanceTo(this.getPosition())< Constants.COLLISION_RANGE) {
			setDest(Animal.createRandomPosition(this.getRegionMngr().getWidth() - 1, this.getRegionMngr().getHeight() - 1));
		}
		moveIn(dt, Constants.FOOD_DROP_RATE_WOLF, Constants.DESIRE_INCREASE_RATE_WOLF,1);
		if(this.getEnergy() <Constants.FOOD_THRESHOLD_WOLF) this.setState(State.HUNGER);
		else if(this.getDesire() > Constants.DESIRE_THRESHOLD_WOLF) this.setState(State.MATE);
		
	}
	
	
	private void applyHungerState(double dt) {
		if(this.huntTarget == null || (this.huntTarget != null && this.huntTarget.getState() == State.DEAD) || !this.isInSightRange(huntTarget)) {
			List<Animal> animalsInRange = this.getRegionMngr().getAnimalsInRange(this, a -> a.getDiet() == Diet.HERBIVORE && a.getState() != State.DEAD);
			this.huntTarget = this.huntingStrategy.select(this, animalsInRange);
		}
		if(this.huntTarget == null) {
			if(this.getDestination().distanceTo(this.getPosition())< Constants.COLLISION_RANGE) {
				setDest(Animal.createRandomPosition(this.getRegionMngr().getWidth() - 1, this.getRegionMngr().getHeight() - 1));
			}
			moveIn(dt, Constants.FOOD_DROP_RATE_WOLF, Constants.DESIRE_INCREASE_RATE_WOLF,1);
		}else {
			setDest(huntTarget.getPosition());
			moveIn(dt, Constants.FOOD_DROP_RATE_WOLF*Constants.FOOD_DROP_BOOST_FACTOR_WOLF, Constants.DESIRE_INCREASE_RATE_WOLF,Constants.BOOST_FACTOR_WOLF);
			if(this.getPosition().distanceTo(this.huntTarget.getPosition()) < Constants.COLLISION_RANGE) {
				this.huntTarget.setState(State.DEAD);
				this.huntTarget = null;
				this.setEnergy(this.getEnergy() + Constants.FOOD_EAT_VALUE_WOLF);
			}
		}
		if(this.getEnergy() > Constants.FOOD_THRESHOLD_WOLF) {
			if(this.getDesire() < Constants.DESIRE_THRESHOLD_WOLF) {
				this.setState(State.NORMAL);
			}else {
				this.setState(State.MATE);
			}
		}
	}

	private void applyMateState(double dt) {
		if(this.getMateTarget() != null && (this.getMateTarget().getState() == State.DEAD || !isInSightRange(this.getMateTarget())))  {
			this.setMateTarget(null);
		}
		if(this.getMateTarget() == null) {
			List<Animal> animalsInRange = this.getRegionMngr().getAnimalsInRange(this, a-> a.getGeneticCode() == Constants.WOLF_GENETIC_CODE && a.getState() != State.DEAD);
			this.setMateTarget(this.getMateStrategy().select(this, animalsInRange));
			if(this.getMateTarget() == null) {
				if(this.getDestination().distanceTo(this.getPosition())< Constants.COLLISION_RANGE) {
					setDest(Animal.createRandomPosition(this.getRegionMngr().getWidth() - 1, this.getRegionMngr().getHeight() - 1));
				}
				moveIn(dt, Constants.FOOD_DROP_RATE_WOLF, Constants.DESIRE_INCREASE_RATE_WOLF,1);
			}
		}else {
			setDest(this.getMateTarget().getPosition());
			moveIn(dt, Constants.FOOD_DROP_RATE_WOLF*Constants.FOOD_DROP_BOOST_FACTOR_WOLF, Constants.DESIRE_INCREASE_RATE_WOLF,Constants.BOOST_FACTOR_WOLF);
			if(this.getPosition().distanceTo(this.getMateTarget().getPosition()) < Constants.COLLISION_RANGE) {
				this.setDesire(0.0);
				this.getMateTarget().setDesire(0.0);
				if(!this.isPregnant()) {
					if(Utils.RAND.nextDouble() < Constants.PREGNANT_PROBABILITY_WOLF) {
						this.setBaby(new Wolf(this, this.getMateTarget()));
					}
				}
				this.setEnergy(this.getEnergy()-Constants.FOOD_DROP_DESIRE_WOLF);
				this.setMateTarget(null);
			}
		}
		if(this.getEnergy()<Constants.FOOD_THRESHOLD_WOLF) this.setState(State.HUNGER);
		else if(this.getDesire() < Constants.DESIRE_THRESHOLD_WOLF) this.setState(State.NORMAL);

	}
	
	@Override
	protected void setNormalStateAction() {
		this.huntTarget = null;
		this.setMateTarget(null);
	}

	@Override
	protected void setMateStateAction() {
		this.huntTarget = null;
	}

	@Override
	protected void setHungerStateAction() {
		this.setMateTarget(null);
	}

	@Override
	protected void setDangerStateAction() {}

	@Override
	protected void setDeadStateAction() {}
	
	
	
}
