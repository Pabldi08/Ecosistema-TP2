package simulator.model;

import simulator.misc.Utils;
import simulator.misc.Vector2D;

public class Sheep extends Animal {
	
	private Animal dangerSource;
	private SelectionStrategy dangerStrategy;
	
	// Constants
	final static String SHEEP_GENETIC_CODE = "Sheep";
	final static double INIT_SIGHT_SHEEP = 40;
	final static double INIT_SPEED_SHEEP = 35;
	final static double BOOST_FACTOR_SHEEP = 2.0;
	final static double MAX_AGE_SHEEP = 8;
	final static double FOOD_DROP_BOOST_FACTOR_SHEEP = 1.2;
	final static double FOOD_DROP_RATE_SHEEP = 20.0;
	final static double DESIRE_THRESHOLD_SHEEP = 65.0;
	final static double DESIRE_INCREASE_RATE_SHEEP = 40.0;
	final static double PREGNANT_PROBABILITY_SHEEP = 0.75;
	
	public Sheep(SelectionStrategy mateStrategy, SelectionStrategy dangerStrategy,  Vector2D pos) {
		super(SHEEP_GENETIC_CODE, Diet.HERBIVORE, INIT_SIGHT_SHEEP, INIT_SPEED_SHEEP, mateStrategy, pos);
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
			if(this.getEnergy() == 0.0 || this.getAge() > MAX_AGE_SHEEP) {
				this.setState(State.DEAD);
			} else {
				// TODO: Si su estado no es DEAD, pide comida al gestor
				// de regiones usando getFood(this, dt) y se añade a su energy (manteniéndolo siempre entre 0.0 y 100.0)
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
			this.move(2.0*this.getSpeed()*dt*Math.exp((this.getEnergy()-100.0)*0.007));
			this.setAge(this.getAge()+dt); 
			
			double newEnergy = this.getEnergy() - FOOD_DROP_RATE_SHEEP * FOOD_DROP_BOOST_FACTOR_SHEEP*dt;
			if(newEnergy <  0.0) {
				this.setEnergy(0.0);
			}else if(newEnergy > 100.0) {
				this.setEnergy(100.0);
			}else {
				this.setEnergy(newEnergy);
			}
			
			
			double newDesire = this.getDesire() + DESIRE_INCREASE_RATE_SHEEP*dt;//1.v
			if(newDesire <  0.0) {
				this.setDesire(0.0);
			}else if(newDesire > 100.0) {
				this.setDesire(100.0);
			}else { 
				this.setDesire(newDesire);
			}
		}
		//cambio de estado
		if(this.dangerSource == null || !isInSightRange(this.dangerSource)) {
			// TODO: Si dangerSource es null, buscar un nuevo animal que se considere peligroso.
            // Para buscar un animal que se considere peligroso, hay que pedir al gestor de regiones la lista de animales carnívoros en el campo visual,
            // usando el método getAnimalsInRange, y después elegir uno usando la estrategia de selección correspondiente.
			if(this.dangerSource == null) {
				if(this.getDesire() < DESIRE_THRESHOLD_SHEEP) this.setState(State.NORMAL);
				else this.setState(State.MATE);
			}
		}
	}
	
	public void applyNormalState(double dt) {
		if(this.getDestination().distanceTo(this.getPosition())< 8.0) {
			setDest(Animal.createRandomPosition(regionMngr.getWidth() - 1, regionMngr.getHeight() - 1));
		}
		moveIn(dt);
		if(this.dangerSource == null) {
			//TODO getAnimalsInRange(...)
		}
		if(this.dangerSource != null){
			this.setState(State.DANGER);
		}else {
			if(this.getDesire() > DESIRE_THRESHOLD_SHEEP) {
				this.setState(State.MATE);
			}
		}
	}
	
	private void applyMateState(double dt) {
		if(this.getMateTarget() != null && (this.getMateTarget().getState().equals(State.DEAD) || !isInSightRange(this.getMateTarget())))  {
			this.setMateTarget(null);
		}
		if(this.getMateTarget() == null){
			// buscar un animal para emparejarse y si no se encuentra uno avanza normalmente como el punto 1 del caso NORMAL arriba; en otro caso (donde mateTarget ya no es null):
			// Para buscar un animal para emparejarse, hay que pedir al gestor de regiones la lista de animales con el mismo código genético en el campo visual,
			// usando el método getAnimalsInRange, y después elegir uno usando la estrategia de selección correspondiente.
			// si no se encuentra animal seguir como en NORMAL:
			if(this.getMateTarget() == null) moveIn(dt);
		}else {
			this.setDest(this.getMateTarget().getPosition()); //2.i
			this.move(2.0*this.getSpeed()*dt*Math.exp((this.getEnergy()-100.0)*0.007)); // 2.ii
			this.setAge(this.getAge()+dt); //2.iii
			
			//TODO: podemos hacer un metodo aparte.
			double newEnergy = this.getEnergy() - FOOD_DROP_RATE_SHEEP * FOOD_DROP_BOOST_FACTOR_SHEEP*dt; //2. iv
			if(newEnergy <  0.0) {
				this.setEnergy(0.0);
			}else if(newEnergy > 100.0) {
				this.setEnergy(100.0);
			}else {
				this.setEnergy(newEnergy);
			}
			
			
			double newDesire = this.getDesire() + DESIRE_INCREASE_RATE_SHEEP*dt;//2.v
			if(newDesire <  0.0) {
				this.setDesire(0.0);
			}else if(newDesire > 100.0) {
				this.setDesire(100.0);
			}else { 
				this.setDesire(newDesire);
			}
			
			if(this.getMateTarget().getPosition().distanceTo(this.getPosition()) < 8.0) {
				
			}
		}
			
			
			
			if(this.getPosition().distanceTo(getDest()) < 8.0) { // esto no se puede mover dentro de setNormalStateAction pr hace falta dt
				setDest(Vector2D.createRandomPosition(regionMngr.getWidth()-1, regionMngr.getHeight()-1)); // TODO: revisar generacion de pos aleatoria
			}
			this.move(getSpeed()*dt*Math.exp((getEnergy()-100.0)*0.007));
			this.setAge(this.getAge()+dt);
			
			
			
			
			// si mateTarget NO es null
			
			
			
			newEnergy = this.getEnergy()-(20.0*1.2*dt);
			if(newEnergy < 0.0) newEnergy = 0.0;
			this.setEnergy(newEnergy);
			newDesire = this.getDesire()-(40.0*dt);
			if(newDesire < 0.0) newDesire = 0.0;
			this.setDesire(newDesire);
			
			if(this.getMateTarget().getPosition().distanceTo(this.getPosition()) < 8.0) {
				this.setDesire(0.0);
				this.getMateTarget().setDesire(0.0);
				if(!this.isPregnant()) {
					double probabilidad = Utils.RAND.nextDouble();
					if(probabilidad < PREGNANT_PROBABILITY_SHEEP) {
						this.setBaby(new Sheep(this, this.getMateTarget()));
					}
				}
				this.setMateTarget(null);
			}
		
		if(dangerSource == null) {
			// TODO: Si dangerSource es null, buscar un nuevo animal que se considere peligroso.
			// Para buscar un animal que se considere peligroso, hay que pedir al gestor de regiones la lista de animales carnívoros en el campo visual,
			// usando el método getAnimalsInRange, y después elegir uno usando la estrategia de selección correspondiente.
			
		} else {
			this.setState(State.DANGER);
			if(this.getDesire() < 65.0) this.setState(State.NORMAL);
		}
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
		//TODO
	}
	
	@Override
	protected void setHungerStateAction() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setDeadStateAction() {
		// TODO Auto-generated method stub
		
	}
	
	private boolean isInSightRange(Animal a) {
		return a.getPosition().distanceTo(this.getPosition()) <= this.getSightRange();
	}
	
	private void moveIn(double dt) {
		this.move(this.getSpeed()*dt*Math.exp((this.getEnergy()-100.0)*0.007));
		this.setAge(this.getAge()+dt);
		
		
		double newEnergy = this.getEnergy() - FOOD_DROP_RATE_SHEEP * dt;
		if(newEnergy <  0.0) {
			this.setEnergy(0.0);
		}else if(newEnergy > 100.0) {
			this.setEnergy(100.0);
		}else {
			this.setEnergy(newEnergy);
		}
		
		
		double newDesire = this.getDesire() + DESIRE_INCREASE_RATE_SHEEP * dt; 
		if(newDesire <  0.0) {
			this.setDesire(0.0);
		}else if(newDesire > 100.0) {
			this.setDesire(100.0);
		}else { 
			this.setDesire(newDesire);
		}
	}



		

	

	
	
	

	
}
