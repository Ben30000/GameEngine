public class Interval {

	private double x1;
	private double x2;
	private double y1;             // Refers to beginning y position at left end point of interval
	private double y2;					// Refers to ending y position at right end point of interval
	private double z1;
	private double z2;
	private double platformAngle;	
	private boolean leftCliff, rightCliff;
	private int type;                     // type: 1 = Interval, 2 = Wall, 3 = Ceiling
	private double landingPrecision;
	
	public Interval(double x1, double x2, double y1, double y2, double z1, double z2, boolean leftCliff, boolean rightCliff, int type, double landingPrecision) {
		this.x1 = x1;
		this.x2 = x2;
		this.z1 = z1;
		this.z2 = z2;
		this.platformAngle = Math.atan2(y2 - y1,x2 - x1);
		this.y1 = y1;
		this.y2 = y2;
		this.leftCliff = leftCliff;
		this.rightCliff = rightCliff;
		this.type = type;
		this.landingPrecision = landingPrecision;
		
		if (type == 3) {
			System.out.println("CEILING");
		}
	}
	
	public double getX1(double width) {
			return this.x1;
	}
	public double getX1(Entity entity) {
		return this.x1;
}
	
	public double getX2(double width) {
			return this.x2;
	}
	public double getX2(Entity entity) {
		return this.x2;
	}

	public void setX1(int x1) {
		this.x1 = x1;
	}
	
	public void setX2(int x2) {
		this.x2 = x2;
	}
	
	public double getLandingPosition(double widthToUse, Entity entity) {
		double landingPosition = this.y1 + (entity.getX() - this.x1)*Math.tan(platformAngle);
		return landingPosition;
	} 

   
	public double getLandingPosition(Entity entity) {
		double landingPosition = this.y1 + (entity.getX() - this.x1)*Math.tan(platformAngle);
		return landingPosition;
	}
   
	public double getLandingPositionFromSpecificPosition(double x) {
		double landingPosition = this.y1 + (x - x1)*Math.tan(platformAngle);
		return landingPosition;
	}

	public double getY1(double widthToUse) {
		return this.y1;
	}

	public double getY1(Entity entity) {
		return this.y1;
	}

	public void setY1(double y1) {
		this.y1 = y1;
	}

	public double getPlatformAngle() {
		return this.platformAngle;
	}

	public boolean isLeftCliff() {
		return this.leftCliff;
	}

	public void setLeftCliff(boolean leftCliff) {
		this.leftCliff = leftCliff;
	}

	public boolean isRightCliff() {
		return this.rightCliff;
	}

	public void setRightCliff(boolean rightCliff) {
		this.rightCliff = rightCliff;
	}

	public double getY2(double widthToUse) {
		return this.y2;
	}

	public double getY2(Entity entity) {
		return this.y2;
	}

	public void setY2(double y2) {
		this.y2 = y2;
	}

	public double getZ1() {
		return this.z1;
	}

	public void setZ1(double z1) {
		this.z1 = z1;
	}

	public double getZ2() {
		return this.z2;
	}

	public void setZ2(double z2) {
		this.z2 = z2;
	}

	public int getType() {
		return this.type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public boolean isPositionAtX1(double x) {
		if (Math.abs(x1 - x) <= landingPrecision  &&  Math.abs(getLandingPositionFromSpecificPosition(x) - y1) <= landingPrecision) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean isPositionAtX2(double x) {
		if (Math.abs(x2 - x) <= landingPrecision  &&  Math.abs(getLandingPositionFromSpecificPosition(x) - y2) <= landingPrecision) {
			return true;
		}
		else {
			return false;
		}
	}

}
