package simulator.model;

import simulator.misc.Vector2D;

public class Sheep extends Animal {
	
	private Animal dangerSource;
	private SelectionStrategy dangerStrategy;
	
	public Sheep(SelectionStrategy mateStrategy, SelectionStrategy dangerStrategy,  Vector2D pos) {
		super("Sheep", Diet.HERBIVORE , 40.0, 35.0, mateStrategy, pos);
		this.dangerStrategy = dangerStrategy;
		//this.dangerSource = null;
	}
	
	
	protected Sheep(Sheep p1, Animal p2) {
		super(p1, p2);
		this.dangerStrategy = p1.dangerStrategy;
		this.dangerSource = null;
	}
	

	@Override
	public void update(double dt) {
		if(this.getState() != State.DEAD) {
			if(this.getEnergy() == 0.0 || this.getAge() >8.0) {
				this.setState(State.DEAD);
			}else {
				switch(this.getState()) {
				case NORMAL:
					applyNormalState(dt);
					break;
				case DANGER:
					applyDangerState(dt);
					break;
				case DEAD:
					break;
				case HUNGER:
					break;
				case MATE:
					applyMateState(dt);
					break;
				}
			}
		}
		

	}


	@Override
	public Vector2D getDestination() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isPregnant() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void applyNormalState(double dt) {
		if(this.getDestination().distanceTo(this.getPos())< 8.0) {
			setDest(Vector2D.createRandomPosition(regionMngr.getWidth() - 1, regionMngr.getHeight() - 1));
		}
		this.move(this.getSpeed()*dt*Math.exp((this.getEnergy()-100.0)*0.007)); //1.ii
		this.setAge(this.getAge()+dt); //1.iii
		
		
		double newEnergy = this.getEnergy() - 20.0*dt; //1.iv
		if(newEnergy <  0.0) {
			this.setEnergy(0.0);
		}else if(newEnergy > 100.0) {
			this.setEnergy(100.0);
		}else {
			this.setEnergy(newEnergy);
		}
		
		
		double newDesire = this.getDesire() + 40.0*dt; //1.v
		if(newDesire <  0.0) {
			this.setDesire(0.0);
		}else if(newDesire > 100.0) {
			this.setDesire(100.0);
		}else { 
			this.setDesire(newDesire);
		}
		
		
		if(this.dangerSource == null) {
			//TODO getAnimalsInRange(...)
			this.setState(State.DANGER);
		}else {
			if(this.getDesire() > 65.0) {
				this.setState(State.MATE);
			}
		}
	}
	
	public void applyDangerState(double dt) {
		if(this.dangerSource != null && this.getState() == State.DEAD ) {
			this.dangerSource = null;
		}
		if(this.dangerSource == null) {
			this.move(this.getSpeed()*dt*Math.exp((this.getEnergy()-100.0)*0.007));
		}else {
			this.setDest(this.getPos().plus(this.getPos().minus(dangerSource.getPosition()).direction()));
			this.move(2.0*this.getSpeed()*dt*Math.exp((this.getEnergy()-100.0)*0.007));
			this.setAge(this.getAge()+dt); //1.iii
			
			double newEnergy = this.getEnergy() - 20.0*1.2*dt; //1.iv
			if(newEnergy <  0.0) {
				this.setEnergy(0.0);
			}else if(newEnergy > 100.0) {
				this.setEnergy(100.0);
			}else {
				this.setEnergy(newEnergy);
			}
			
			
			double newDesire = this.getDesire() + 40.0*dt; //1.v
			if(newDesire <  0.0) {
				this.setDesire(0.0);
			}else if(newDesire > 100.0) {
				this.setDesire(100.0);
			}else { 
				this.setDesire(newDesire);
			}
		}
	}
	
	public void applyMateState(double dt) {
		
	}

	@Override
	protected void setNormalStateAction() {
		//TODO
	}
	
	
	
	@Override
	protected void setDangerStateAction() {
		//TODO
	}

	@Override
	protected void setMateStateAction() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setHungerStateAction() {
		// TODO Auto-generated method stub

	}


	@Override
	protected void setDeadStateAction() {
		// TODO Auto-generated method stub

	}


	@Override
	public Vector2D getPosition() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getGeneticCode() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public double getSightRange() {
		// TODO Auto-generated method stub
		return 0;
	}

}
