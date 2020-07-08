
public class CameraInterval {

	private double x1;
	private double x2;
	private double y1;
	private double y2;
	private double threshold;
	private Background b;
	
	
	public CameraInterval(double x1, double x2, double y1, double y2, double threshold, Background b) {
		this.setX1(x1);
		this.setX2(x2);
		this.setY1(y1);
		this.setY2(y2);
		this.setThreshold(threshold);
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


	public double getThreshold() {
		return threshold;
	}


	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}
}
