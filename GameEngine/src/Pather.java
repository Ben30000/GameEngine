
public class Pather extends Creature {

	private double speed;
	private int direction;        // 1 = left, 2 = right
	private double mass;
	
	public Pather(double x, double y, double z) throws Exception {
		super(x, y, z);
		// TODO Auto-generated constructor stub
	}
	
	public void update(double test) {
		super.setFalling(false);
		super.setJumping(false);

		this.setSpeed(0.02);
		this.setDirection(2);
		
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public double getMass() {
		return mass;
	}

	public void setMass(double mass) {
		this.mass = mass;
	}

}
