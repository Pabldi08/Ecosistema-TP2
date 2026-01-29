package simulator.model;

import simulator.misc.Vector2D;

public class Sheep extends Animal {
	
	private Animal dangerSource;
	private SelectionStrategy dangerStrategy;
	
	private double dt; // TODO: ver cual es la mejor forma de implementarlo

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
		this.dt = dt;
		if(this.getState() != State.DEAD) {
			if(this.getEnergy() == 0.0 || this.getAge() > MAX_AGE_SHEEP) {
				this.setState(State.DEAD);
			} else {
				// TODO: Si su estado no es DEAD, pide comida al gestor
				// de regiones usando getFood(this, dt) y se añade a su energy (manteniéndolo siempre entre 0.0 y 100.0)
			  	switch (this.getState()) {
			  	case NORMAL:
			  		setNormalStateAction();
			  		break;
			  	case DANGER:
			  		setMateStateAction();
			  		break;
			  	case MATE:
			  		setHungerStateAction();
			  		break;
				default:
					break;
			  	}
			}
		}
	}
	
	// TODO: revisar y cambiar todo lo que depende del gestor de regiones en el update y los métodos que se usan en update
	
	@Override
	protected void setNormalStateAction() {
		if(this.getPosition().distanceTo(getDest()) < 8.0) { // esto no se puede mover dentro de setNormalStateAction pr hace falta dt
			setDest(Vector2D.createRandomPosition(regionMngr.getWidth()-1, regionMngr.getHeight()-1)); // TODO: revisar generacion de pos aleatoria
		}
		this.move(getSpeed()*dt*Math.exp((getEnergy()-100.0)*0.007));
		this.setAge(this.getAge()+dt);
		
		double newEnergy = this.getEnergy()-(20.0*dt);
		if(newEnergy < 0.0) newEnergy = 0.0;
		this.setEnergy(newEnergy);
		
		double newDesire = this.getDesire()-(40.0*dt);
		if(newDesire < 0.0) newDesire = 0.0;
		this.setDesire(newDesire);
		
		// Cambio de estado
		if (dangerSource == null) {
			// TODO: Si dangerSource es null, buscar un nuevo animal que se considere peligroso.
			// Para buscar un animal que se considere peligroso, hay que pedir al gestor de regiones la lista de animales carnívoros en el campo visual,
			// usando el método getAnimalsInRange, y después elegir uno usando la estrategia de selección correspondiente.
			if(this.getDesire() > 65.0) this.setState(State.MATE);
		} else { 
			this.setState(State.DANGER);
		}
	}
	
	@Override
	protected void setDangerStateAction() {
		if(this.dangerSource != null && this.getState().equals(getState().DEAD)) this.dangerSource = null;
		if(this.dangerSource == null) {
			if(this.getPosition().distanceTo(getDest()) < 8.0) { // esto no se puede mover dentro de setNormalStateAction pr hace falta dt
				setDest(Vector2D.createRandomPosition(regionMngr.getWidth()-1, regionMngr.getHeight()-1)); // TODO: revisar generacion de pos aleatoria
			}
		} else {
			this.setDest(this.getPosition().plus(this.getPosition().minus(dangerSource.getPosition()).direction()));
			this.move(2.0*this.getSpeed()*dt*Math.exp((this.getEnergy()-100.0)*0.007));
			this.setAge(this.getAge()+dt);
			
			double newEnergy = this.getEnergy()-(20.0*1.2*dt);
			if(newEnergy < 0.0) newEnergy = 0.0;
			this.setEnergy(newEnergy);
			
			double newDesire = this.getDesire()-(40.0*dt);
			if(newDesire < 0.0) newDesire = 0.0;
			this.setDesire(newDesire);
		}
		
		// Cambio de estado
		if(dangerSource == null || !isInSightRange(dangerSource))  {
			// TODO: Si dangerSource es null, buscar un nuevo animal que se considere peligroso.
			// Para buscar un animal que se considere peligroso, hay que pedir al gestor de regiones la lista de animales carnívoros en el campo visual,
			// usando el método getAnimalsInRange, y después elegir uno usando la estrategia de selección correspondiente.
			if(this.getDesire() < 65.0) this.setState(State.NORMAL);
			else this.setState(State.MATE);
		} 
	}
	
	@Override
	protected void setMateStateAction() {
		if(this.getMateTarget() != null &&
				(this.getMateTarget().getState().equals(State.DEAD) || isInSightRange(this.getMateTarget())))  {
			this.setMateTarget(null);
		} else {
			// buscar un animal para emparejarse y si no se encuentra uno avanza normalmente como el punto 1 del caso NORMAL arriba; en otro caso (donde mateTarget ya no es null):
			// Para buscar un animal para emparejarse, hay que pedir al gestor de regiones la lista de animales con el mismo código genético en el campo visual,
			// usando el método getAnimalsInRange, y después elegir uno usando la estrategia de selección correspondiente.
			// si no se encuentra animal seguir como en NORMAL:
			double newEnergy, newDesire;
			if(this.getPosition().distanceTo(getDest()) < 8.0) { // esto no se puede mover dentro de setNormalStateAction pr hace falta dt
				setDest(Vector2D.createRandomPosition(regionMngr.getWidth()-1, regionMngr.getHeight()-1)); // TODO: revisar generacion de pos aleatoria
			}
			this.move(getSpeed()*dt*Math.exp((getEnergy()-100.0)*0.007));
			this.setAge(this.getAge()+dt);
			newEnergy = this.getEnergy()-(20.0*dt);
			if(newEnergy < 0.0) newEnergy = 0.0;
			this.setEnergy(newEnergy);
			newDesire = this.getDesire()-(40.0*dt);
			if(newDesire < 0.0) newDesire = 0.0;
			this.setDesire(newDesire);
			
			// si mateTarget NO es null
			this.setDest(this.getMateTarget().getPosition());
			this.move(2.0*this.getSpeed()*dt*Math.exp((this.getEnergy()-100.0)*0.007));
			this.setAge(this.getAge()+dt);
			newEnergy = this.getEnergy()-(20.0*1.2*dt);
			if(newEnergy < 0.0) newEnergy = 0.0;
			this.setEnergy(newEnergy);
			newDesire = this.getDesire()-(40.0*dt);
			if(newDesire < 0.0) newDesire = 0.0;
			this.setDesire(newDesire);
			
			if(this.getMateTarget().getPosition().distanceTo(this.getPosition()) < 8.0) {
				this.setDesire(0.0);
				this.getMateTarget().setDesire(0.0);
				if(this.getBaby() == null) {
					if(probabilidad > 0.0) {
						this.setBaby(Sheep(this, this.getMateTarget()); // TODO como hacer que use el constructor de sheep?
					}
				}
				this.setMateTarget(null);
			}
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
	
	private boolean isInSightRange(Animal a) {
		return a.getPosition().distanceTo(this.getPosition()) <= this.getSightRange();
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

		@Override
		public double getAge() {
			// TODO Auto-generated method stub
			return 0;
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

		@Override
		protected void setHungerStateAction() {
			// TODO Auto-generated method stub
			
		}

		@Override
		protected void setDeadStateAction() {
			// TODO Auto-generated method stub
			
		}
		
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
		final static double PREGNANT_PROBABILITY_SHEEP = 0.9;
}
