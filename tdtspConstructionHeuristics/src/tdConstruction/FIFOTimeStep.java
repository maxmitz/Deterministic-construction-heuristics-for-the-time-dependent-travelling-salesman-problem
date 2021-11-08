package tdConstruction;


public class FIFOTimeStep {
	private double realCost;
	private LinearFunction f;

	public FIFOTimeStep(double realCost) {
		
		this.realCost=realCost;
		this.f =  new LinearFunction(100000,100000);
		
	}
	

	public void setLinearFunction(LinearFunction f) {	
		this.f = f;
	}

	public double getCost(double time) {
		return Double.min(f.getValue(time), this.realCost);
	}

	public LinearFunction getF() {
		return this.f;
	}
}
