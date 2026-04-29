package simulator.model;

import simulator.misc.Utils;

public class DryRegion extends DefaultRegion {
	
	private double N;
	private double M;
	private double R1;
	private double R2;
	
	private double cont;
	private double periodoSequia;
	
	public DryRegion(double N, double M, double R1, double R2) {
		if(N < 0 || N > 1) {
			throw new IllegalArgumentException("N tiene que estar entre 0 y 1");
		}
		if(M < 0 || M > 1) {
			throw new IllegalArgumentException("M tiene que estar entre 0 y 1");
		}
		if(R1 <= 0) {
			throw new IllegalArgumentException("R1 tiene que ser positivo");
		}
		if(R2 <= 0) {
			throw new IllegalArgumentException("R2 tiene que ser positivo");
		}
		this.N = N;
		this.M = M;
		this.R1 = R1;
		this.R2 = R2;
		this.cont = 0;
		this.periodoSequia = Utils.RAND.nextDouble(1, M);
	}
	
	@Override
	public String toString() {
		return "Dry";
	}
	
	@Override
	public void update(double dt) {
		this.cont += dt;
		if(this.cont >= this.N+this.periodoSequia) {
			this.cont = 0;
			this.R1 *= R2;
			this.periodoSequia = Utils.RAND.nextDouble(1, M);
		}
	}
	
	@Override
	public double getFood(AnimalInfo a, double dt) {
		double food = 0;
		if(this.cont <= this.N) {
			food = super.getFood(a, dt);
		} else {
			food = R1*super.getFood(a, dt);
		}
		return food;
	}
}
