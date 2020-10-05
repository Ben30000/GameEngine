
public class DeadEndInterval {
	
	
	private double x1, x2;
	private double y1, y2;
	private int type;                       // Type 1 = LDE, 2 = RDE, 3 = UDE, 4 = BDE.
	private double targetX, targetY;
	private World b;
	
	public DeadEndInterval(double x1, double x2, double y1, double y2, double targetX, double targetY, World b,int type) {
		
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
		this.targetX = targetX;
		this.targetY = targetY;
		this.setType(type);
		this.b = b;
		
		
	}
	public double getX1() {
		return b.getX() + x1;
	}
	public void setX1(double x1) {
		this.x1 = x1;
	}
	public double getX2() {
		return b.getX() + x2;
	}
	public void setX2(double x2) {
		this.x2 = x2;
	}
	public double getY1() {
		return b.getY() + y1;
	}
	public void setY1(double y1) {
		this.y1 = y1;
	}
	public double getY2() {
		return b.getY() + y2;
	}
	public void setY2(double y2) {
		this.y2 = y2;
	}
	public double getTargetX() {
		return targetX;
	}
	public void setTargetX(double targetX) {
		this.targetX = targetX;
	}
	public double getTargetY() {
		return targetY;
	}
	public void setTargetY(double targetY) {
		this.targetY = targetY;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}

}
