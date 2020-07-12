public class Interval {

	private double x1;
	private double x2;
	private double startingLandingPosition;             // Refers to beginning y position at left end point of interval
	private double endLandingPosition;					// Refers to ending y position at right end point of interval
	private double z1;
	private double z2;
	private double platformAngle;	
	private boolean leftCliff, rightCliff;
	private int type;                     // type: 1 = Interval, 2 = Wall, 3 = Ceiling
	
	public Interval(double x1, double x2, double startingLandingPosition, double endLandingPosition, double z1, double z2, boolean leftCliff, boolean rightCliff, int type) {
		this.x1 = x1;
		this.x2 = x2;
		this.z1 = z1;
		this.z2 = z2;
		this.platformAngle = Math.atan2(endLandingPosition - startingLandingPosition,x2 - x1);
		this.startingLandingPosition = startingLandingPosition;
		this.endLandingPosition = endLandingPosition;
		this.leftCliff = leftCliff;
		this.rightCliff = rightCliff;
		this.type = type;
		
		if (type == 3) {
			System.out.println("CEILING");
		}
	}
	
	public double getX1(double width) {
			return x1;
	}
	public double getX1(Entity entity) {
		return x1;
}
	
	public double getX2(double width) {
			return x2;
	}
	public double getX2(Entity entity) {
		return x2;
	}

	public void setX1(int x1) {
		this.x1 = x1;
	}
	
	public void setX2(int x2) {
		this.x2 = x2;
	}
	
	public double getLandingPosition(double widthToUse, Entity entity) {
		double landingPosition = startingLandingPosition + (entity.getX() - x1)*Math.tan(platformAngle);
		return landingPosition;
	} 

   
	public double getLandingPosition(Entity entity) {
		double landingPosition = startingLandingPosition + (entity.getX() - x1)*Math.tan(platformAngle);
		return landingPosition;
	}
   
	public double getLandingPositionFromSpecificPosition(double x) {
		double landingPosition = startingLandingPosition + (x - x1)*Math.tan(platformAngle);
		return landingPosition;
	}

	public double getStartingPosition(double widthToUse) {
		return startingLandingPosition;
	}

	public double getStartingPosition(Entity entity) {
		return startingLandingPosition;
	}

	public void setStartingPosition(double startingPosition) {
		this.startingLandingPosition = startingPosition;
	}

	public double getPlatformAngle() {
		return platformAngle;
	}

	public boolean isLeftCliff() {
		return leftCliff;
	}

	public void setLeftCliff(boolean leftCliff) {
		this.leftCliff = leftCliff;
	}

	public boolean isRightCliff() {
		return rightCliff;
	}

	public void setRightCliff(boolean rightCliff) {
		this.rightCliff = rightCliff;
	}

	public double getEndingPosition(double widthToUse) {
		return endLandingPosition;
	}

	public double getEndingPosition(Entity entity) {
		return endLandingPosition;
	}

	public void setEndingPosition(double endLandingPosition) {
		this.endLandingPosition = endLandingPosition ;
	}

	public double getZ1() {
		return z1;
	}

	public void setZ1(double z1) {
		this.z1 = z1;
	}

	public double getZ2() {
		return z2;
	}

	public void setZ2(double z2) {
		this.z2 = z2;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
