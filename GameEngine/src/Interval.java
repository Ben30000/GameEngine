import org.joml.Vector2d;

public class Interval {

	static public double landingPrecision;    // static variables and methods are defined before the creation of an instances of an object and can be referenced as Class.variable/method
	static public double entityAlignmentMode;
	static public boolean exactMode;
	
	static final public int NONCLIFF = 1;
	static final public int LEFTCLIFF = 2;
	static final public int RIGHTCLIFF = 3;
	
	private double x1;
	private double x2;
	private double y1;
	private double y2;
	private double z1;
	private double z2;
	private Interval leftInterval, rightInterval;
	private int type;                     // type: 1 = Terrain, 2 = Wall, 3 = Ceiling
	private boolean traversed;
	
	
	
	public Interval(double x1, double x2, double y1, double y2, double z1, double z2, Interval leftInterval, Interval rightInterval, int type) {
		this.x1 = x1;
		this.x2 = x2;
		this.z1 = z1;
		this.z2 = z2;
		this.y1 = y1;
		this.y2 = y2;
		this.leftInterval = leftInterval;
		this.rightInterval = rightInterval;
		this.type = type;
		this.traversed = false;
	}
	
	
	public double getX1(Entity entity, int intervalMode) {

		if (entityAlignmentMode == 1) {
			if (intervalMode == NONCLIFF) {
				if ( (getType() == 2 && getPlatformAngle(intervalMode) >= 0.0)
				|| (getType() == 3 && getPlatformAngle(intervalMode) <= 0.0) ) {                        // width extend walls/ceilings
					return this.x1 - 0.5*entity.getFeetWidth();
				}
				else if ( (getType() == 2 && getPlatformAngle(intervalMode) < 0.0)
				|| (getType() == 3 && getPlatformAngle(intervalMode) > 0.0) ) {						// width extend walls/ceilings
					return this.x1 + 0.5*entity.getFeetWidth();
				}
				return this.x1;																			// standard terrain
			}
			else if (intervalMode == LEFTCLIFF) {			
				return this.x1 - 0.5*entity.getFeetWidth();												// left cliff terrain, left cliff ceiling
			}
			else if (intervalMode == RIGHTCLIFF) {
				if (getType() == 1) {
					return this.x2;																			// right cliff terrain
				}
				else if (getType() == 3) {
					return this.x2 - 0.5*entity.getFeetWidth();
				}
			}
			
			return this.x1;																				// default non-cliff
		}
		
		
		else if (entityAlignmentMode == 2) {
			if (intervalMode == NONCLIFF) {
				if (  ( (getType() == 1 || getType() == 2) && getPlatformAngle(intervalMode) >= 0.0)
					|| ( getType() == 3 && getPlatformAngle(intervalMode) < 0.0) ) {
					return this.x1 - 0.5*entity.getFeetWidth();
				}
				else if (  ( (getType() == 1 || getType() == 2) && getPlatformAngle(intervalMode) < 0.0)
						|| ( getType() == 3 && getPlatformAngle(intervalMode) >= 0.0) ) {
						return this.x1 + 0.5*entity.getFeetWidth();
				}
				return this.x1;
			}
			else if (intervalMode == LEFTCLIFF) {
				return this.x1 - 0.5*entity.getFeetWidth();
			}
			else if (intervalMode == RIGHTCLIFF) {
				return this.x2 - 0.5*entity.getFeetWidth();
			}
			
			return this.x1;
		}
		
		return this.x1;
	}
	
	
	public double getX2(Entity entity, int intervalMode) {

		if (entityAlignmentMode == 1) {
			if (intervalMode == NONCLIFF) {
				if ( (getType() == 2 && getPlatformAngle(intervalMode) >= 0.0)
				|| (getType() == 3 && getPlatformAngle(intervalMode) < 0.0) ) {							// width extend walls/ceilings
					return this.x2 - 0.5*entity.getFeetWidth();
				}
				else if ( (getType() == 2 && getPlatformAngle(intervalMode) < 0.0)
				|| (getType() == 3 && getPlatformAngle(intervalMode) >= 0.0) ) {						// width extend walls/ceilings
					return this.x2 + 0.5*entity.getFeetWidth();
				}
				return this.x2;																			// standard terrain
			}
			else if (intervalMode == LEFTCLIFF) {
				if (getType() == 1) {
					return this.x1;																			// left cliff terrain
				}
				else if (getType() == 3) {		
					return this.x1 + 0.5*entity.getFeetWidth();												// left cliff ceiling
				}
			}
			else if (intervalMode == RIGHTCLIFF) {
				return this.x2 + 0.5*entity.getFeetWidth();												// right cliff terrain, right cliff ceiling
			}
			
			return this.x2;																				// default non-cliff
		}
		
		else if (entityAlignmentMode == 2) {
			if (intervalMode == NONCLIFF) {
				if (  ( (getType() == 1 || getType() == 2) && getPlatformAngle(intervalMode) >= 0.0)
					|| ( getType() == 3 && getPlatformAngle(intervalMode) < 0.0) ) {
					return this.x2 - 0.5*entity.getFeetWidth();
				}
				else if (  ( (getType() == 1 || getType() == 2) && getPlatformAngle(intervalMode) < 0.0)
						|| ( getType() == 3 && getPlatformAngle(intervalMode) >= 0.0) ) {
						return this.x2 + 0.5*entity.getFeetWidth();
				}
				return this.x2;
			}
			else if (intervalMode == LEFTCLIFF) {
				return this.x1 + 0.5*entity.getFeetWidth();
			}
			else if (intervalMode == RIGHTCLIFF) {
				return this.x2 + 0.5*entity.getFeetWidth();
			}
			
			return this.x2;
		}
		
		return this.x2;
	}

	public void setX1(int x1) {
		this.x1 = x1;
	}
	
	public void setX2(int x2) {
		this.x2 = x2;
	}
	

	public double getLandingPosition(Entity entity, int intervalMode) {

		double footX = entity.getX();
		

		if (intervalMode == NONCLIFF) {
			
			return getY1(entity,intervalMode) + (footX - getX1(entity,intervalMode))*(getY2(entity,intervalMode) - getY1(entity,intervalMode))/(getX2(entity,intervalMode) - getX1(entity,intervalMode));
		}
		else if (intervalMode == LEFTCLIFF) {
			if (isLeftCliff()) {
				
				return this.y1;
			}
		}
		else if (intervalMode == RIGHTCLIFF) {
			if (isRightCliff()) {
				
				return this.y2;
			}
		}
		return getY1(entity,intervalMode) + (footX - getX1(entity,intervalMode))*(getY2(entity,intervalMode) - getY1(entity,intervalMode))/(getX2(entity,intervalMode) - getX1(entity,intervalMode));     // default
		
	}
   
	public double getLandingPositionFromSpecificPosition(Entity entity, double x, int intervalMode) {

		double footX = x;

		if (intervalMode == NONCLIFF) {
			
			return getY1(entity,intervalMode) + (footX - getX1(entity,intervalMode))*(getY2(entity,intervalMode) - getY1(entity,intervalMode))/(getX2(entity,intervalMode) - getX1(entity,intervalMode));
		}
		else if (intervalMode == LEFTCLIFF) {
			if (isLeftCliff()) {
				
				return this.y1;
			}
		}
		else if (intervalMode == RIGHTCLIFF) {
			if (isRightCliff()) {
				
				return this.y2;
			}
		}
		return getY1(entity,intervalMode) + (footX - getX1(entity,intervalMode))*(getY2(entity,intervalMode) - getY1(entity,intervalMode))/(getX2(entity,intervalMode) - getX1(entity,intervalMode));     // default

	}

	

	public double getY1(Entity entity, int intervalMode) {
		if (intervalMode == RIGHTCLIFF) {
			return this.y2;
		}
		return this.y1;
	}

	public void setY1(double y1) {
		this.y1 = y1;
	}
	

	public double getY2(Entity entity, int intervalMode) {
		if (intervalMode == LEFTCLIFF) {
			return this.y1;
		}
		return this.y2;
	}

	public void setY2(double y2) {
		this.y2 = y2;
	}


	public double getPlatformAngle(int intervalMode) {

		if (intervalMode == NONCLIFF) {
			return Math.atan2(y2 - y1,x2 - x1);                   // dont need to use getX() or getY() for (potentially) width extended endpoints here since angle won't change
		}
		return 0.0;

	}
	

	public boolean isLeftCliff() {
		if (entityAlignmentMode == 1) {
			if ( this.leftInterval == null) {
				if (getType() == 1 || (getType() == 3 && getPlatformAngle(NONCLIFF) > 0.0)) {
					return true;
				}
				return false;
			}
			else {
				if ((getType() == 1 && this.leftInterval.getType() != 1)
				|| (getType() == 3 &&  getPlatformAngle(NONCLIFF) > 0.0 && (this.leftInterval.getType() == 1 || this.leftInterval.getType() == 2 || 
					   (this.leftInterval.getType() == 3 && this.leftInterval.getPlatformAngle(NONCLIFF) < 0.0)) )
				){
					return true;
				}
				return false;
			}
		}
		
		else if (entityAlignmentMode == 2) {
			if ( (getPlatformAngle(NONCLIFF) < 0.0 && getType() == 1)
			  || (getPlatformAngle(NONCLIFF) >= 0.0 && getType() == 3)) {
				if (this.leftInterval == null) {
					return true;
				}
				else if ( (getType() == 1 && this.leftInterval.getPlatformAngle(NONCLIFF) >= 0.0)
						|| (getType() == 3 && this.leftInterval.getPlatformAngle(NONCLIFF) < 0.0)) {
					return true;
				}
				return false;
			}
		}
		
		return false;

	}

	public boolean isRightCliff() {
		if (entityAlignmentMode == 1) {
			if (this.rightInterval == null) {
				if (getType() == 1 || (getType() == 3 && getPlatformAngle(NONCLIFF) < 0.0)) {
					return true;
				}
				return false;
			}
			else {
				if ((this.getType() == 1 && this.rightInterval.getType() != 1)
				|| (getType() == 3 &&  getPlatformAngle(NONCLIFF) < 0.0 && (this.leftInterval.getType() == 1 || this.leftInterval.getType() == 2 || 
						(this.leftInterval.getType() == 3 && this.leftInterval.getPlatformAngle(NONCLIFF) > 0.0)) )) {
					return true;
				}
				return false;
			}
		}
		
		else if (entityAlignmentMode == 2) {
			if ( (getPlatformAngle(NONCLIFF) >= 0.0 && getType() == 1)
			  || (getPlatformAngle(NONCLIFF) < 0.0 && getType() == 3)) {
				if (this.rightInterval == null) {
					return true;
				}
				else if ( (getType() == 1 && this.rightInterval.getPlatformAngle(NONCLIFF) < 0.0) 
						|| (getType() == 3 && this.rightInterval.getPlatformAngle(NONCLIFF) >= 0.0) ) {
					return true;
				}
				return false;
			}
		}

		return false;
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
	
	public boolean isPositionAtX1(double x, Entity entity, int intervalMode) {
		
		double x1ToUse = getX1(entity,intervalMode);
		double y1ToUse = getY1(entity,intervalMode);

		if (exactMode) {
			if (x == x1ToUse && Math.abs(getLandingPositionFromSpecificPosition(entity, x, intervalMode) - y1ToUse ) <= landingPrecision) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			if (Math.abs(x1ToUse - x) <= landingPrecision  &&  Math.abs(getLandingPositionFromSpecificPosition(entity, x, intervalMode) - y1ToUse) <= landingPrecision) {
				return true;
			}
			else {
				return false;
			}
		}
	
	 }
	
	public boolean isPositionAtX2(double x, Entity entity, int intervalMode) {
		
		double x2ToUse = getX2(entity,intervalMode);
		double y2ToUse = getY2(entity,intervalMode);
		
		if (exactMode) {
			if (x == x2ToUse && Math.abs(getLandingPositionFromSpecificPosition(entity, x, intervalMode) - y2ToUse ) <= landingPrecision) {
				return true;
			}
			else {
				return false;
			}
		}
		else {
			if (Math.abs(x2ToUse - x) <= landingPrecision  &&  Math.abs(getLandingPositionFromSpecificPosition(entity, x, intervalMode) - y2ToUse) <= landingPrecision) {
				return true;
			}
			else {
				return false;
			}
		}
	
	}

	public Interval getLeftInterval() {
		return this.leftInterval;
	}
	
	public void setLeftInterval(Interval interval) {
		this.leftInterval = interval;
	}
	
	public Interval getRightInterval() {
		return this.rightInterval;
	}
	
	public void setRightInterval(Interval interval) {
		this.rightInterval = interval;
	}


	public boolean isTraversed() {
		return traversed;
	}


	public void setTraversed(boolean traversed) {
		this.traversed = traversed;
	}
	
	static public boolean areIntervalsSame(Entity entity, Interval intv1, Interval intv2) {
		
		if (intv1.getX1(entity, 1) == intv2.getX1(entity, 1) && intv1.getY1(entity, 1) == intv2.getY1(entity, 1)
				&& intv1.getX2(entity, 1) == intv2.getX2(entity, 1) && intv1.getY2(entity, 1) == intv2.getY2(entity, 1)) {
			return true;
		}
		
		return false;
		
	}
}
