
public class Interval {

	private double x1;
	private double x2;
	private double y1;
	private double y2;
	private double z1;
	private double z2;
	private double playerDX;
	private double playerDY;
	private double bgDX;
	private double bgDY;
	private boolean cameraPanning;
	private Background b;
	private Player p;
	private double platformAngle;
	private double landingPosition;
	private double startingLandingPosition;             // Refers to beginning landing position at left end point of interval, regardless of inclusion status
	private double xToUse;
	private boolean moving;
	private boolean lvlOrUp;
	private boolean down;
	private boolean lvlWDownOnLeft;
	private boolean lvlWDownOnRight;
	private boolean hasLandingPosition;
	private boolean inclusiveWallY1;
	private boolean hasLeftWall;
	private boolean hasRightWall;
	private double endLandingPosition;
	
	private double wallY1;
	private double wallY2;
	private int bgMovesX;
	private int bgMovesY;
	private int widthPlusLeft;                     // widthPlusLeft and widthPlusRight: 1 for +, 2 for -, 3 for base 
	private int widthPlusRight;
	private int inclusionType;                       // inclusionType: 1 if inclusive of neither left or right, 2 of left, 3 of right, 4 of both end points of interval
	private boolean leftCliff, rightCliff;
	private int type;                     // type: 1 = Interval, 2 = Wall, 3 = Ceiling
	
	public Interval(double x1, double x2, double startingLandingPosition, double endLandingPosition, double z1, double z2, boolean leftCliff, boolean rightCliff, Background b, Player p, int type) {
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = 1.0;
		this.y2 = 2.0;
		this.z1 = z1;
		this.z2 = z2;
		this.b = b;
		this.p = p;
		this.platformAngle = Math.atan2(endLandingPosition - startingLandingPosition,x2 - x1);
		this.startingLandingPosition = startingLandingPosition;
	//	this.hasLandingPosition = hasLandingPosition;
		this.endLandingPosition = endLandingPosition;
		this.bgMovesX = 1;
		this.bgMovesY = 0;
		//this.widthPlusLeft = widthPlusLeft;
		//this.widthPlusRight = widthPlusRight;
		//this.inclusionType = inclusionType;
		this.setLeftCliff(leftCliff);
		this.setRightCliff(rightCliff);
		this.type = type;
		if (this.platformAngle < 0.0) 
			System.out.println("THIS INTERVAL HAS AN ANGLE OF "+this.platformAngle);
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

	public double getY1() {
		return y1;
	}
	
	public double getY2() {
		return y2;
	}
	
	public void setX1(int x1) {
		this.x1 = x1;
	}
	
	public void setX2(int x2) {
		this.x2 = x2;
	}
	
	public void setY1(int y1) {
		this.y1 = y1;
	}
	
	public void setY2(int y2) {
		this.y2 = y2;
	}
	
	//public void setPlayerDX(int playerDX) {
	//	this.playerDX = playerDX;
	//}
	
	public void setPlayerDY(int playerDY) {
		this.playerDY = playerDY;
	}
	
	public double getPlayerDX() {
		return playerDX;
	}
	
	public double getPlayerDY() {
		return playerDY;
	}
	
	public double getBgDX() {
		return bgDX;
	}
	
	public double getBgDY() {
		return bgDY;
	}
	
	public void setBgDX(int bgDX) {
		this.bgDX = bgDX;
	}
	
    public void setBgDY(int bgDY) {
    	this.bgDY = bgDY;
    }
    
    public boolean isLevelOrSlopedUp() {
    	return lvlOrUp;
    }
    
    public boolean isSlopedDown() {
    	return down;
    }
    
    public boolean isLevelWithSlopedDownOnLeft() {
    	return lvlWDownOnLeft;
    }
    
    public boolean isLevelWithSlopedDownOnRight() {
    	return lvlWDownOnRight;
    }
   
   public boolean hasCameraPanning() {
	   return cameraPanning;
   }
   
   public double getLandingPosition(double widthToUse, Entity entity) {
	   landingPosition = startingLandingPosition + (entity.getX() - x1)*Math.tan(platformAngle);
	   return landingPosition;
   } 

   
   public double getLandingPosition(Entity entity) {
	  landingPosition = startingLandingPosition + (entity.getX() - x1)*Math.tan(platformAngle);
	  return landingPosition;
   }
   
   public double getLandingPositionFromPosition(double x) {
		  landingPosition = startingLandingPosition + (x - x1)*Math.tan(platformAngle);
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


public boolean getHasLandingPosition() {
	return hasLandingPosition;
}

public void setHasLandingPosition(boolean hasLandingPosition) {
	
	this.hasLandingPosition = hasLandingPosition;
}

public boolean isInclusiveWallY1() {
	return inclusiveWallY1;
}

public void setInclusiveWallY1(boolean inclusiveWallY1) {
	this.inclusiveWallY1 = inclusiveWallY1;
}

public boolean hasLeftWall() {
	return hasLeftWall;
}

public void setHasLeftWall(boolean hasLeftWall) {
	this.hasLeftWall = hasLeftWall;
}

public boolean hasRightWall() {
	return hasRightWall;
}

public void setHasRightWall(boolean hasRightWall) {
	this.hasRightWall = hasRightWall;
}

public double getWallY1() {
	return wallY1;
}

public void setWallY1(double wallY1) {
	this.wallY1 = wallY1;
}

public double getWallY2() {
	return wallY2;
}

public void setWallY2(double wallY2) {
	this.wallY2 = wallY2;
}

public double getPlatformAngle() {
	return platformAngle;
}

public int getBGMovesX() {
	return bgMovesX;
}

public void setBGMovesX(int bgMovesX) {
	this.bgMovesX = bgMovesX;
}

public int getBGMovesY() {
	return bgMovesY;
}

public void setBGMovesY(int bgMovesY) {
	this.bgMovesY = bgMovesY;
}

public int getInclusionType() {
	return inclusionType;
}
public Background getBG() {
	return this.b;
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
