
public class Camera {

	private double eyeX, eyeY, eyeZ;
	private double vPointX, vPointY, vPointZ;
	private double eyeDX, eyeDY, eyeDZ;
	private double vPointDX, vPointDY, vPointDZ;
	
	private boolean panningLeft, panningRight;
	private boolean panningUp, panningDown;
	private double targetVerticalPosition;
	
	private double targetHorizontalDistanceRightOfEntity = 0.45, targetHorizontalDistanceLeftOfEntity = 0.45;
	private double minHorizontalPanSpeed = 0.0032;
	private boolean panningHorizontally;
	private double currentTargetEyeX;
	
	static public double landingPrecision;
	
	
	public Camera(double eyeX,double eyeY,double eyeZ, double vPointX, double vPointY, double vPointZ) {

		this.eyeX = eyeX;
		this.eyeY = eyeY;
		this.eyeZ = eyeZ;
		
		this.vPointX = vPointX;
		this.vPointY = vPointY;
		this.vPointZ = vPointZ;
		
		this.panningHorizontally = false;
		this.panningUp = false;
		this.panningDown = false;
	}

	public double getEyeX() {
		return eyeX;
	}

	public void setEyeX(double eyeX) {
		this.eyeX = eyeX;
	}

	public double getEyeY() {
		return eyeY;
	}

	public void setEyeY(double eyeY) {
		this.eyeY = eyeY;
	}

	public double getEyeZ() {
		return eyeZ;
	}

	public void setEyeZ(double eyeZ) {
		this.eyeZ = eyeZ;
	}

	public double getVPointX() {
		return vPointX;
	}

	public void setVPointX(double vPointX) {
		this.vPointX = vPointX;
	}

	public double getVPointY() {
		return vPointY;
	}

	public void setVPointY(double vPointY) {
		this.vPointY = vPointY;
	}

	public double getVPointZ() {
		return vPointZ;
	}

	public void setVPointZ(double vPointZ) {
		this.vPointZ = vPointZ;
	}

	public double getEyeDX() {
		return eyeDX;
	}

	public void setEyeDX(double eyeDX) {
		this.eyeDX = eyeDX;
	}

	public double getEyeDY() {
		return eyeDY;
	}

	public void setEyeDY(double eyeDY) {
		this.eyeDY = eyeDY;
	}

	public double getEyeDZ() {
		return eyeDZ;
	}

	public void setEyeDZ(double eyeDZ) {
		this.eyeDZ = eyeDZ;
	}

	public double getVPointDX() {
		return vPointDX;
	}

	public void setVPointDX(double vPointDX) {
		this.vPointDX = vPointDX;
	}

	public double getVPointDY() {
		return vPointDY;
	}

	public void setVPointDY(double vPointDY) {
		this.vPointDY = vPointDY;
	}

	public double getVPointDZ() {
		return vPointDZ;
	}

	public void setVPointDZ(double vPointDZ) {
		this.vPointDZ = vPointDZ;
	}
	
	public void moveEye() {
		
		this.eyeX = this.eyeX + this.eyeDX;
		this.eyeY = this.eyeY + this.eyeDY;
		this.eyeZ = this.eyeZ + this.eyeDZ;
		
	}
	public void moveVPoint() {
	
		this.vPointX = this.vPointX + this.vPointDX;
		this.vPointY = this.vPointY + this.vPointDY;
		this.vPointZ = this.vPointZ + this.vPointDZ;
		
	}

	public boolean isPanningUp() {
		return panningUp;
	}

	public void setPanningUp(boolean panningUp) {
		this.panningUp = panningUp;
	}

	public boolean isPanningDown() {
		return panningDown;
	}

	public void setPanningDown(boolean panningDown) {
		this.panningDown = panningDown;
	}

	public double getTargetVerticalPosition() {
		return targetVerticalPosition;
	}

	public void setTargetVerticalPosition(double targetVerticalPosition) {
		this.targetVerticalPosition = targetVerticalPosition;
	}
	
	public void panHorizontally(Entity entity) {
		
		boolean leftEntityMotion = false, rightEntityMotion = false;
		if (entity.getMotionVector().x < 0.0) {
			leftEntityMotion = true;
		}
		else if (entity.getMotionVector().x > 0.0) {
			rightEntityMotion = true;
		}
		double cameraSpeedScale = 0.024;
		
		if (!(Math.abs(getEyeX() - (entity.getX() + this.targetHorizontalDistanceRightOfEntity)) <= landingPrecision) && (rightEntityMotion
			|| entity.getFacingDirection().compareTo("right") == 0) ) {
				this.panningHorizontally = true;
				this.currentTargetEyeX = entity.getX() + this.targetHorizontalDistanceRightOfEntity; 
					
		}

		if (!(Math.abs(getEyeX() - (entity.getX() - this.targetHorizontalDistanceLeftOfEntity)) <= landingPrecision) && (leftEntityMotion
			|| entity.getFacingDirection().compareTo("left") == 0) ) {
				this.panningHorizontally = true;
				this.currentTargetEyeX = entity.getX() - this.targetHorizontalDistanceLeftOfEntity;
		}


		if (this.panningHorizontally) {

			double cameraDXAbsolute = Math.max(cameraSpeedScale * Math.abs(this.currentTargetEyeX - getEyeX()), this.minHorizontalPanSpeed);
			double cameraDX = Math.abs(this.currentTargetEyeX - getEyeX()) / (this.currentTargetEyeX - getEyeX())  *  cameraDXAbsolute;
			double initialEyeX = getEyeX();
			double newEyeX = getEyeX() + cameraDX;
			
			if ( (this.currentTargetEyeX > Math.min(initialEyeX, newEyeX) || Math.abs(this.currentTargetEyeX - Math.min(initialEyeX, newEyeX)) <= landingPrecision) && (this.currentTargetEyeX < Math.max(initialEyeX, newEyeX) || Math.abs(this.currentTargetEyeX - Math.max(initialEyeX, newEyeX)) <= landingPrecision)) {


				double closingDistance = this.currentTargetEyeX - getEyeX();

				setEyeDX(1.0*closingDistance);
				setEyeDY(0.0);
				setEyeDZ(0.0);
				setVPointDX(1.0*closingDistance);
				setVPointDY(0.0);
				setVPointDZ(0.0);
				moveEye();
				moveVPoint();

				this.panningHorizontally = false;
				
			} else {
				setEyeDX(1.0*cameraDX);
				setEyeDY(0.0);
				setEyeDZ(0.0);
				setVPointDX(1.0*cameraDX);
				setVPointDY(0.0);
				setVPointDZ(0.0);
				
				moveEye();
				moveVPoint();
			}
		}


	}

	public void panVertically(Entity entity) {
		
	}

	public boolean isPanningLeft() {
		return panningLeft;
	}

	public void setPanningLeft(boolean panningLeft) {
		this.panningLeft = panningLeft;
	}

	public boolean isPanningRight() {
		return panningRight;
	}

	public void setPanningRight(boolean panningRight) {
		this.panningRight = panningRight;
	}
	
}
