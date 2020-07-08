import java.util.ArrayList;

import org.joml.Vector2d;
import org.joml.Vector2f;

public interface Entity {

	public void setDX(double dx);
	public void setDY(double dy);
	public double getX();
	public void setX(double x);
	public double getY();
	public void setY(double y);
	public double getZ();
	public void setZ(double z);
	
	public double getInitialFallVelocity();
	public void setInitialFallVelocity(double initialFallVelocity);
	public void setFallVelocity(double fallVelocity);
	public double getFallVelocity();
	public void setVelocityX(double velX);
	public double getVelocityX();
	public void setVelocityY(double velY);
	public double getVelocityY();
	
	public ArrayList<Vector2d> getFeetPositions();
	public void setFeetPositions(Vector2f[] points);
	
	public double getFeetWidth();
	public double getWidth();
	public double getHeight();
	public boolean getJumping();
	public void setJumping(boolean jumping);
	public boolean getFalling();
	public void setFalling(boolean falling);
	public boolean getSlidingOnWall();
	public void setSlidingOnWall(boolean slidingOnWall);
	public void setEntitiesSlopedWallAngle(double angle);
	public double getEntitiesSlopedWallAngle();
	
	public double getFallTime();
	public double getJumpTime();
	public void setFallTime(double fallTime);
	public void setJumpTime(double jumpTime);
	
	public double getTargetHorizontalThresholdLeft();
	public double getTargetHorizontalThresholdRight();

	public String getFacingDirection();
	public void setFacingDirection(String facingDirection);
	public Vector2d getMotionVector();
	public void setMotionVector(Vector2d motionVector);
	public void move(int type);
	
}
