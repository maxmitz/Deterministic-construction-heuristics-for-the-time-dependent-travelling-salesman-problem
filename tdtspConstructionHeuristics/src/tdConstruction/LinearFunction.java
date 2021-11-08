package tdConstruction;

public class LinearFunction {
	private double a;
	private double slope;
	private double interval;
	private double b;

	public LinearFunction(double a, double interval) {

		this.a=a;
		this.interval=interval;
		this.b=this.interval+a;
		this.slope=-1;

	}

	public double getValue(double time) {
		return slope*time+b;
	}

	public double getB() {
		return this.b;
	}
}
