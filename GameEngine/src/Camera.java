
public class Camera {

	private double eyeX, eyeY, eyeZ;
	private double vPointX, vPointY, vPointZ;
	private double eyeDX, eyeDY, eyeDZ;
	private double vPointDX, vPointDY, vPointDZ;
	
	private boolean panningUp, panningDown;
	private double targetVerticalPosition;
	
	public Camera(double eyeX,double eyeY,double eyeZ, double vPointX, double vPointY, double vPointZ) {

		this.eyeX = eyeX;
		this.eyeY = eyeY;
		this.eyeZ = eyeZ;
		
		this.vPointX = vPointX;
		this.vPointY = vPointY;
		this.vPointZ = vPointZ;
		
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
		
		// for testing POM
	//	if (this.eyeY <= -19.50) {
			//this.eyeY = -19.50;
	//	}
		
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
}
