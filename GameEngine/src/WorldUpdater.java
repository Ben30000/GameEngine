import java.util.ArrayList;
import org.joml.Matrix4f;
import org.joml.Matrix4d;
import org.joml.Vector3f;
import org.joml.Vector3d;
import org.joml.Quaternionf;
import org.joml.Vector2d;

public class WorldUpdater {

	private World currentWorld;
	private ArrayList<Creature> creatures;
	private int dx;
	private int dy;
	private Player player;
	private boolean glide;
	private int glideCounter = 2;
	private boolean rightKeyPressed;
	private boolean leftKeyPressed;
	private boolean finishedCameraShift;
	private boolean leftKeyR;
	private boolean rightKeyR;
	private double totalBGDX;
	private double totalBGDY;
	public double totalPlayerDX;
	public double totalPlayerDY;
	private boolean busy;
	private boolean rightwardMovement;
	private boolean leftwardMovement;
	private double amntToMove;
	private double initialJumpOrFallVelocity;
	private double gravityAcceleration;
	private double displacement;
	private double landingPrecision;
	private double outOfIntervalRangeThreshold = 0.401;  // used for a terrain path tracing optimization
	boolean eventHappened = false;
	private double fallConstant1, fallConstant2;
	private double jumpConstant1, jumpConstant2;
	public Interval emptySpaceInterval;
	public Interval creaturesIntervalInWallRegion;
	
	// For testing efficiency
	int intervalsInspected = 0;

	
	
	public WorldUpdater(int initialScrollCounter, Player p, double landingPrecision) {
		dx = 0;
		dy = 0;
		totalBGDX = 0;
		totalBGDY = 0;
		player = p;
		rightKeyPressed = false;       
		leftKeyPressed = false;
		glide = false;
		busy = false;
		glideCounter = 2;
		gravityAcceleration = 34.62;
		gravityAcceleration = 30.22;

		initialJumpOrFallVelocity = 0.001;
		fallConstant1 = 1.0;
		fallConstant2 = 0.554;

		jumpConstant1 = fallConstant1;
		jumpConstant2 = fallConstant2;
		
		this.landingPrecision = landingPrecision;
		System.out.println("WORLD GENERAOR CONSTRUCTOR: landingPrecision = "+landingPrecision);
	}

	
	public void updateWorld(Camera mainCamera) {
		
		//double piDivTwo = 1.5707963267948966 - 0.00000001;
		double testSP = 20.0, testEP = 1000.0;
		double testX1 = 10.000000, testX2 = 10.000000000000001;
		double testAngle = Math.atan2(testEP - testSP,testX2 - testX1);
		double testResult = testSP + (testX2 - testX1)*Math.tan(testAngle);
		double testResultMethod2 = testSP + (testX1 - testX1)*(testEP - testSP)/(testX2 - testX1);
		double testMovePositionX = testX1 + Math.cos(testAngle)*980.0;
		double testMovePositionY = testSP + Math.sin(testAngle)*980.0;


		player.setPreviousX(player.getX());
		player.setPreviousY(player.getY());
		busy = true;
	
		
		// Camera Work
		
		mainCamera.panHorizontally(player);
		//this.panCameraVertically(mainCamera, player);

		double pYB42 = player.getY() - 0.5 * player.getHeight();
	

		if (finishedCameraShift) {
			finishedCameraShift = false;
		}

		

		amntToMove = 0.0;
				
		if (dx == 1) {
			amntToMove = 0.04;
			leftwardMovement = true;
			rightwardMovement = false;
			player.setFacingDirection("left");
			player.setMotionVector(new Vector2d(-1.0,0.0));

		} else if (dx == -1) {
			amntToMove = 0.04;
			rightwardMovement = true;
			leftwardMovement = false;
			player.setFacingDirection("right");
			player.setMotionVector(new Vector2d(1.0,0.0));

		} else if (leftKeyR && dx == 0 && glide) {
			if (glideCounter > 0) {
				amntToMove = 0.0007;
				leftwardMovement = true;
				rightwardMovement = false;
				player.setFacingDirection("left");
				player.setMotionVector(new Vector2d(-1.0,0.0));
				glideCounter--;
			} else {
				amntToMove = 0.0;
				glide = false;
				glideCounter = 10;
				player.setMotionVector(new Vector2d(0.0,0.0));
			}
		} else if (rightKeyR && dx == 0 && glide) {
			if (glideCounter > 0) {
				amntToMove = 0.0007;
				rightwardMovement = true;
				leftwardMovement = false;
				player.setFacingDirection("right");
				player.setMotionVector(new Vector2d(1.0,0.0));
				glideCounter--;

			} else {
				amntToMove = 0.0;
				player.setMotionVector(new Vector2d(0.0,0.0));
				glide = false;
				glideCounter = 10;
			}
		}

		Vector2d motionDirectionPlayer = new Vector2d(0.0,0.0);
		if (rightwardMovement) {
			motionDirectionPlayer = new Vector2d(1.0,0.0);
		}
		else if (leftwardMovement) {
			motionDirectionPlayer = new Vector2d(-1.0,0.0);
		}
	
	// Falling or Jumping for each entity
		
		double playerYBefore = player.getY();
		boolean playerFallingBefore = player.getFalling();
		
		
		
		if (isBelowPlatform(player, player.getX(), player.getY() - 0.5*player.getHeight())) {
			System.out.println("IS BELOW PLATFORM: BEFORE jumpOrFall");
			System.exit(0);
		}
		double timeB4JumpOrFall = System.nanoTime();
		this.jumpOrFall(player);
		double timeAfterJumpOrFall = System.nanoTime();
		System.out.println("!@#$%^ jumpOrFall took "+((timeAfterJumpOrFall-timeB4JumpOrFall)/1000000000.0)+" s");
		if (isBelowPlatform(player, player.getX(), player.getY() - 0.5*player.getHeight())) {
			System.out.println("IS BELOW PLATFORM: AFTER jumpOrFall");
			System.exit(0);
		}
		
		
		
		double playerYAfter = player.getY();
		
		boolean playerFallingAfter = player.getFalling();
		
		if ((playerFallingBefore || playerFallingAfter) && mainCamera.isPanningDown()) {
			mainCamera.setEyeDX(0.0);
			mainCamera.setEyeDY(playerYAfter - playerYBefore);
			mainCamera.setEyeDZ(0.0);
			mainCamera.setVPointDX(0.0);
			mainCamera.setVPointDY(playerYAfter - playerYBefore);
			mainCamera.setVPointDZ(0.0);
			
			mainCamera.moveEye();
			mainCamera.moveVPoint();
			mainCamera.setTargetVerticalPosition(mainCamera.getTargetVerticalPosition() + (playerYAfter - playerYBefore));
		}
		
		
		//this.jumpOrFall(creatures.get(0));
	
		double playerXBefore = player.getX();
		 
		
		if (isBelowPlatform(player, player.getX(),player.getY()-0.5*player.getHeight())) {
			System.out.println("BEFORE NAVIGATE OR FALL, BELOW PLATFORM");
			System.exit(0);
		}
		
		double tb4nav = System.nanoTime();
		double pFXB4Move = player.getX();
		double pFYB4Move = player.getY();
		this.intervalsInspected = 0;
		this.navigate(player, motionDirectionPlayer, amntToMove, 1);
		
		System.out.println("During player navigate, "+this.intervalsInspected +" intervals were searched");
		double pFXAfMove = player.getX();
		double pFYAfMove = player.getY();
		if (Math.abs(pFXAfMove-pFXB4Move) > 0.1) {
			System.out.println("pFXAfMove-pFXB4Move = "+(pFXAfMove-pFXB4Move));
			System.exit(0);
		}
		if (Math.abs(pFYAfMove-pFYB4Move) > 0.1) {
			System.out.println("pFYAfMove-pFYB4Move = "+(pFYAfMove-pFYB4Move));
			System.exit(0);
		}
		double navTimePassed = (System.nanoTime() - tb4nav)/1000000000.0;
		System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		System.out.println("Nav took "+navTimePassed+"s");
		System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		if (navTimePassed > 0.005) {
			System.out.println("nav took too long");
			//System.exit(0);
		}
		
		if (isBelowPlatform(player, player.getX(),player.getY()-0.5*player.getHeight())) {
			System.out.println("AFTER NAVIGATE OR FALL, BELOW PLATFORM");
			System.exit(0);
		}
		
		double playerXAfter = player.getX();
		
		mainCamera.setEyeDX(playerXAfter - playerXBefore);
		mainCamera.setEyeDY(0.0);
		mainCamera.setEyeDZ(0.0);
		mainCamera.setVPointDX(playerXAfter - playerXBefore);
		mainCamera.setVPointDY(0.0);
		mainCamera.setVPointDZ(0.0);
		mainCamera.moveEye();
		mainCamera.moveVPoint();
		
		amntToMove = 0.04;
		//this.navigate(creatures.get(0), 2, 0.02, 2);




	}



	public void setDX(int dx) { 
		this.dx = dx;
	}

	public void setDY(int dy) {
		this.dy = dy;
	}

	public int getDX() {
		return dx;
	}

	public double getTotalPlayerDX() {
		return totalPlayerDX;
	}

	public double getTotalPlayerDY() {
		return totalPlayerDY;
	}

	public void setTotalPlayerDX(double totalPlayerDX) {
		this.totalPlayerDX = totalPlayerDX;
	}

	public void setTotalPlayerDY(double totalPlayerDY) {
		this.totalPlayerDY = totalPlayerDY;
	}

	public double getTotalBGDX() {
		return totalBGDX;
	}

	public double getTotalBGDY() {
		return totalBGDY;
	}

	public void setTotalBGDX(double totalBGDX) {
		this.totalBGDX = totalBGDX;
	}

	public void setTotalBGDY(double totalBGDY) {
		this.totalBGDY = totalBGDY;
	}

	// returns amountToMove = (amountToMove or 0 if wall is hit)
	public double checkForWallsThenMove(Entity entity, double amountToMove, double dxAmount, double dyAmount, Vector2d motionVector) {

		boolean leftwardMovement = false, rightwardMovement = false;
		
		if (motionVector.x < 0.0) {
			leftwardMovement = true;
		}
		else if (motionVector.x > 0.0) {
			rightwardMovement = true;
		}
		
		
		// Check if a ceiling will stop movement while on a terrain platforrm, or if it will alter trajectory while in air
		//if (isBelowPlatform(entity, entity.getX(), entity.getY() - 0.5*entity.getHeight())) {
		//	System.out.println("IS BELOW PLATFORM: BEFORE checkForCeiling");
		//	System.exit(0);
		//}
		double footX = entity.getX(),   footY = entity.getY() - 0.5*entity.getHeight();
		double newFootX = entity.getX() + dxAmount,   newFootY = entity.getY() - 0.5*entity.getHeight() + dyAmount;
		double tenativeDX = dxAmount,   tenativeDY = dyAmount;
		double finalDX = tenativeDX, finalDY = tenativeDY;   
		// newAmountToMove is the value returned by this method
		double newAmountToMove = amountToMove;
		
		// Finally, Check if a sloped wall will intercept the final movement
		System.out.println("    initialFootX = "+footX+" initialFootY = "+footY);
		System.out.println("	newFootX = "+newFootX+" newFootY = "+newFootY);
		Double closestIntersectionDistance = Double.MAX_VALUE;
		
		// ** Use approximate mode for end point checking when dealing with motion being walled **
		Interval.exactMode = false;
		
		double entityExtentPaddingLeft = 0.0, entityExtentPaddingRight = 0.0;
		if (dxAmount > 0.0) {
			entityExtentPaddingRight = dxAmount;
		}
		else if (dxAmount <= 0.0) {
			entityExtentPaddingLeft= dxAmount;
		}
		int hashCodes[] = currentWorld.getSlopedWalls().genHashCodesFromKeysX(footX + entityExtentPaddingLeft - 0.5*entity.getFeetWidth(),  footX + entityExtentPaddingRight + 0.5*entity.getFeetWidth());	
		
		for (int k = 0; k < hashCodes.length; k++) {
				
			int thisHashCode = hashCodes[k];
			ArrayList<Interval> bucketOfWalls = currentWorld.getSlopedWalls().getIntervalList(thisHashCode);
			if (bucketOfWalls == null) {
				continue;
			}
			
			for (int r = 0; r < bucketOfWalls.size(); r++ ) {
				this.intervalsInspected++;
				Interval aWall = bucketOfWalls.get(r);
				double wallAngle = aWall.getPlatformAngle(1);
				double x1 = aWall.getX1(entity,1),  x2 = aWall.getX2(entity,1);
				
				
				double startingWallPosition = aWall.getY1(entity, 1),  endingWallPosition = aWall.getY2(entity, 1);
				
				
				
				
				
				if ( (rightwardMovement && wallAngle > 0.0)    ||    (leftwardMovement && wallAngle < 0.0) ) {
					// This section checks for intersection between entity and the wall	
					double wallIntersectionResult[] = lineIntersection(footX, footY,   newFootX, newFootY,
																			x1, startingWallPosition,   x2, endingWallPosition);
						if (wallIntersectionResult != null) {
							System.out.println("	WALL INTERSECTION POTENTIALLY FOUND ///// ");
							//System.out.println("        wallAngle = "+wallAngle);

						    if ( (wallAngle > 0.0 && !aWall.isPositionAtX2(wallIntersectionResult[0], entity, 1))   ||   (wallAngle < 0.0 && !aWall.isPositionAtX1(wallIntersectionResult[0],entity,1)) ) {
					    		double someDX = wallIntersectionResult[0] - footX;
								double someDY = wallIntersectionResult[1] - footY;
								double intersectionDistance = Math.sqrt(someDX*someDX + someDY*someDY);

								if (intersectionDistance < closestIntersectionDistance) {
									System.out.println("/////// WALLED");
									System.out.println("wallAngle = "+wallAngle);
									System.out.println("aWall.isPositionAtX1(wallIntersectionResult[0],entity,1) = "+aWall.isPositionAtX1(wallIntersectionResult[0],entity,1));
									System.out.println("wallIntersectionResult[0] = "+wallIntersectionResult[0]);
									System.out.println("x1 = "+x1);
									closestIntersectionDistance = intersectionDistance;
									finalDX = someDX;
									finalDY = someDY;
									newAmountToMove = 0.0;
								}
						    }
						}
						else {
							// This section checks for intersection between entity and the bottom, perpendicular part of the wall
							double perpendicularPartOfWallIntersectionResult[] = null;
							if (rightwardMovement && wallAngle >= 0.0) {
								perpendicularPartOfWallIntersectionResult = lineIntersection(footX, footY,   newFootX, newFootY,
										x1, startingWallPosition, x1, startingWallPosition - entity.getHeight());										
							}
							else if (leftwardMovement && wallAngle < 0.0) {
								perpendicularPartOfWallIntersectionResult = lineIntersection(footX, footY,   newFootX, newFootY,
										x2, endingWallPosition, x2, endingWallPosition - entity.getHeight());								
							}

							if (perpendicularPartOfWallIntersectionResult != null) {
								System.out.println("	WALL INTERSECTION POTENTIALLY FOUND |||| ");
								//System.out.println("      wallAngle = "+wallAngle);
								
							    if ( (wallAngle > 0.0 && !aWall.isPositionAtX2(perpendicularPartOfWallIntersectionResult[0],entity,1))   ||   (wallAngle < 0.0 && !aWall.isPositionAtX1(perpendicularPartOfWallIntersectionResult[0],entity,1)) ) {
											
						    		double someDX = perpendicularPartOfWallIntersectionResult[0] - footX;
									double someDY = perpendicularPartOfWallIntersectionResult[1] - footY;
									double intersectionDistance = Math.sqrt(someDX*someDX + someDY*someDY);
									if (intersectionDistance < closestIntersectionDistance) {
										System.out.println("|||||| WALLED: PERPENDICULAR");
										closestIntersectionDistance = intersectionDistance;
										finalDX = someDX;
										finalDY = someDY;
										newAmountToMove = 0.0;
									}

							    }								
							}
																			
						}
						
				}
			}
		}		
		
		entity.setDX(finalDX);
		entity.setDY(finalDY);
		entity.move(3);
		entity.setDX(0.0);
		entity.setDY(0.0);
		
		//if (isBelowPlatform(entity, entity.getX(), entity.getY() - 0.5*entity.getHeight())) {
		//	System.out.println("IS BELOW PLATFORM: AFTER checkForCeiling and wall tests");
		//	System.exit(0);
		//}
		return newAmountToMove;


	}

	public void setRightKeyPressed(boolean s) {
		rightKeyPressed = s;
	}

	public boolean getRightKeyPressed() {
		return rightKeyPressed;
	}

	public void setLeftKeyPressed(boolean s) {
		leftKeyPressed = s;
	}

	public boolean getLeftKeyPressed() {
		return leftKeyPressed;
	}

	public void setLeftKeyR(boolean s) {
		this.leftKeyR = s;
	}

	public void setRightKeyR(boolean s) {
		this.rightKeyR = s;
	}

	public World getCurrentWorld() {
		return this.currentWorld;
	}

	public void setCurrentWorld(World currentWorld) {
		this.currentWorld = currentWorld;
	}


	public void setBusy(boolean b) {
		busy = b;
	}

	public boolean getBusy() {
		return busy;
	}

	public void setGlide(boolean s) {
		glide = s;
	}

	public boolean getGlide() {
		return glide;
	}

	public void setGlideCounter(int glideCounter) {
		this.glideCounter = glideCounter;
	}


	public double[] lineIntersection(double x1, double y1, double x2, double y2, double a1, double b1, double a2, double b2) {
		// returns {intersectionPointX, intersectionPointY} or null if lines do not intersect

		double sTime = System.nanoTime();
		
		double boundsP = landingPrecision;
		double A1 = y2 - y1;
		double B1 = x1 - x2;
		double C1 = A1 * x1 + B1 * y1;

		double A2 = b2 - b1;
		double B2 = a1 - a2;
		double C2 = A2 * a1 + B2 * b1;

		double d = A1 * B2 - A2 * B1;
		
		double intersectionPointX, intersectionPointY;
		
		if (Math.abs(d) <= 0.000000000000000000000001) {
			//System.out.println("Lines do not intersect at all");
			return null;
		}
		else {
			double pointX = (B2 * C1 - B1 * C2) / d;
			double pointY = (A1 * C2 - A2 * C1) / d;
			if ((pointX > Math.min(x1, x2) || Math.abs(pointX - Math.min(x1, x2)) <= boundsP)
					&& (pointX < Math.max(x1, x2) || Math.abs(pointX - Math.max(x1, x2)) <= boundsP)
					&& (pointY > Math.min(y1, y2) || Math.abs(pointY - Math.min(y1, y2)) <= boundsP)
					&& (pointY < Math.max(y1, y2) || Math.abs(pointY - Math.max(y1, y2)) <= boundsP)
					&& (pointX > Math.min(a1, a2) || Math.abs(pointX - Math.min(a1, a2)) <= boundsP)
					&& (pointX < Math.max(a1, a2) || Math.abs(pointX - Math.max(a1, a2)) <= boundsP)
					&& (pointY > Math.min(b1, b2) || Math.abs(pointY - Math.min(b1, b2)) <= boundsP)
					&& (pointY < Math.max(b1, b2) || Math.abs(pointY - Math.max(b1, b2)) <= boundsP)) {
			
				intersectionPointX = pointX;
				intersectionPointY = pointY;
				//System.out.println("LINES INTERSECT with intersectionPointX and intersectionPointY being "+intersectionPointX+ ", "+intersectionPointY);
				double fTime = System.nanoTime();
			//System.out.println("LINE INTERSECTION FOUND IN TIME: "+ (fTime-sTime));

				return new double[] {intersectionPointX, intersectionPointY};
			} else {
				//System.out.println("Lines Intersect but Solution is oob");
				intersectionPointX = pointX;
				intersectionPointY = pointY;
				//System.out.println("intrX = "+intersectionPointX+", intrY = "+intersectionPointY);
			//	System.out.println("LINES DO NOT INTERSECT with intersectionPointX and intersectionPointY being "+intersectionPointX+ ", "+intersectionPointY);
				return null;
			}
		}

	}

	

	public double[] lineIntersectionExactEndpointMode(double x1, double y1, double x2, double y2, double a1, double b1, double a2, double b2) {
		// returns {intersectionPointX, intersectionPointY} or null if lines do not intersect

		double sTime = System.nanoTime();
		
		double A1 = y2 - y1;
		double B1 = x1 - x2;
		double C1 = A1 * x1 + B1 * y1;

		double A2 = b2 - b1;
		double B2 = a1 - a2;
		double C2 = A2 * a1 + B2 * b1;

		double d = A1 * B2 - A2 * B1;
		
		double intersectionPointX, intersectionPointY;
		
		if (Math.abs(d) <= 0.000000000000000000000001) {
			//System.out.println("Lines do not intersect at all");
			return null;
		}
		else {
			double pointX = (B2 * C1 - B1 * C2) / d;
			double pointY = (A1 * C2 - A2 * C1) / d;
			if ((pointX > Math.min(x1, x2) || pointX == Math.min(x1, x2) )
					&& (pointX < Math.max(x1, x2) || pointX == Math.max(x1, x2) )
					&& (pointY > Math.min(y1, y2) || pointY == Math.min(y1, y2))
					&& (pointY < Math.max(y1, y2) || pointY == Math.max(y1, y2))
					&& (pointX > Math.min(a1, a2) || pointX == Math.min(a1, a2))
					&& (pointX < Math.max(a1, a2) || pointX == Math.max(a1, a2))
					&& (pointY > Math.min(b1, b2) || pointY == Math.min(b1, b2))
					&& (pointY < Math.max(b1, b2) || pointY == Math.max(b1, b2))) {
			
				intersectionPointX = pointX;
				intersectionPointY = pointY;
				//System.out.println("LINES INTERSECT with intersectionPointX and intersectionPointY being "+intersectionPointX+ ", "+intersectionPointY);
				double fTime = System.nanoTime();
			//System.out.println("LINE INTERSECTION FOUND IN TIME: "+ (fTime-sTime));

				return new double[] {intersectionPointX, intersectionPointY};
			} else {
				System.out.println("EXMODE: Lines Intersect but Solution is oob");
				intersectionPointX = pointX;
				intersectionPointY = pointY;
				System.out.println("intrX = "+intersectionPointX+", intrY = "+intersectionPointY);
			//	System.out.println("LINES DO NOT INTERSECT with intersectionPointX and intersectionPointY being "+intersectionPointX+ ", "+intersectionPointY);
				return null;
			}
		}

	}
	
	/*
	 ********************************************************************************************************* 
	 **   navigate() handles horizontal entity movement (other than sliding on a sloped wall) in the world; **
	 *****     walls and ceilings are searched for to see if they'll obstruct this horizontal motion   *******
	 *********************************************************************************************************
	 * entity: the entity to move, motionType: 1 for left, 2 for right, distance: total to move, entityType: 1 for player, 2 for creature
	 ****************************************************************************************************************************************  
	 */
	public void navigate(Entity entity, Vector2d motionVector, double distance, int entityType) {
		System.out.println("--------------------------------------------------------------------");
		System.out.println("--------------------------------------------------------------------");
		System.out.println("-----------------------navigate BEGIN-------------------------------");
		System.out.println("--------------------------------------------------------------------");
		System.out.println("--------------------------------------------------------------------");
	   	
		double requestedDistanceToMove = 0.0;
		double startEntXPos = entity.getX();
		double amountToMove = distance;
		boolean leftwardMovement, rightwardMovement;
		motionVector.normalize();
		if (motionVector.x < 0.0) {
			leftwardMovement = true;
			rightwardMovement = false;
		}
		else if (motionVector.x > 0.0) {
			leftwardMovement = false;
			rightwardMovement = true;
		}
		else {
			leftwardMovement = false;
			rightwardMovement = false;
		}
		
		double motionVectorX = 0.0;
		if (rightwardMovement) {
			motionVectorX = 1.0;
		} else if (leftwardMovement) {
			motionVectorX = -1.0;
		}


		double[] lastFootPosition = null;
		
		while (amountToMove > 0.0) {
			System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
			System.out.println("NAVIGATE WHILE LOOP START: amountToMove = "+amountToMove);
			System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
			double footX = entity.getX();
			double footY = entity.getY() - 0.5*entity.getHeight();
			double headY = entity.getY() + 0.5*entity.getHeight();
			this.outOfIntervalRangeThreshold = amountToMove + 0.00000001;
			
			System.out.println("NAVIGATE: footX = "+footX+ " footY = "+footY);
		
			Interval terrain = null;
			Interval closestTerrain = null;
			Interval ceiling = null;
			Interval closestCeiling = null;
			
			int terrainMode = 1;
			int closestTerrainMode = 1;
			
			//
			//  This block of code handles the issue of intending to move to an end point but barely missing it (rounding issue)
			//
			/*double sameFootPosThreshold = 0.00000000001;
			Interval.exactMode = true;
			if (lastFootPosition != null) {
				if (Math.abs(footX - lastFootPosition[0]) <= sameFootPosThreshold 
					&& Math.abs(footY - lastFootPosition[1]) <= sameFootPosThreshold) {
					Interval.exactMode = false;
				}
			}
			lastFootPosition = new double[] {footX, footY};*/
			Interval.exactMode = false;
			//
			
			double entityExtentPaddingLeft = 0.0, entityExtentPaddingRight = 0.0;
			if (leftwardMovement) {
				entityExtentPaddingLeft = -amountToMove;
			} else if (rightwardMovement) {
				entityExtentPaddingRight = amountToMove;
			}
			int hashCodes[] = currentWorld.getTerrain().genHashCodesFromKeysX(entity.getX() + entityExtentPaddingLeft - 0.5*entity.getFeetWidth(),  entity.getX() + entityExtentPaddingRight + 0.5*entity.getFeetWidth());	
			
			for (int a = 0; a < hashCodes.length; a++) {
					
				int thisHashCode = hashCodes[a];
				ArrayList<Interval> bucketOfTerrain = currentWorld.getTerrain().getIntervalList(thisHashCode);
				if (bucketOfTerrain == null) {
					continue;
				}
				
				for (int v = 0; v < bucketOfTerrain.size(); v++) {
					
					// Check if this interval is a left and/or right cliff
					ArrayList<Integer> terrainModes = new ArrayList<Integer>(); // mode == 1: standard, mode == 2: leftCliff, mode == 3: rightCliff
					terrainModes.add(1);
					if (bucketOfTerrain.get(v).isLeftCliff()) {
						terrainModes.add(2);
					}
					if (bucketOfTerrain.get(v).isRightCliff()) {
						terrainModes.add(3);
					}
				
					
					
					for (int m = 1; m <= terrainModes.size(); m++) {
						this.intervalsInspected++;
						Interval anInterval = bucketOfTerrain.get(v);
						int anIntervalMode = terrainModes.get(m - 1);
						double x1 = anInterval.getX1(entity, anIntervalMode),   x2 = anInterval.getX2(entity, anIntervalMode);
						
						// Quickly rule out distant intervals
						if (footX < x1 && Math.abs(footX - x1) > outOfIntervalRangeThreshold) {
							continue;
						}
						if (footX > x2 && Math.abs(footX - x2) > outOfIntervalRangeThreshold) {
							continue;
						}
						if (footY < Math.min(anInterval.getY1(null, 1), anInterval.getY2(null, 1)) && Math.abs(footY - Math.min(anInterval.getY1(null, 1), anInterval.getY2(null, 1))) > outOfIntervalRangeThreshold) {
							continue;
						}
						if (footY > Math.max(anInterval.getY1(null, 1), anInterval.getY2(null, 1)) && Math.abs(footY - Math.max(anInterval.getY1(null, 1), anInterval.getY2(null, 1))) > outOfIntervalRangeThreshold) {
							continue;
						}
						
						double landingPosition = anInterval.getLandingPosition(entity, anIntervalMode);
						double startingPosition = anInterval.getY1(entity, anIntervalMode),   endingPosition = anInterval.getY2(entity, anIntervalMode);
						double angle = anInterval.getPlatformAngle(anIntervalMode);
						
						if ( 
						(rightwardMovement && (footX > x1 ||  anInterval.isPositionAtX1(footX,entity, anIntervalMode)) && (footX < x2) && !(anInterval.isPositionAtX2(footX,entity,anIntervalMode)) )
						||
						 (leftwardMovement && (footX > x1) && (footX < x2 ||  anInterval.isPositionAtX2(footX,entity,anIntervalMode)) && !(anInterval.isPositionAtX1(footX,entity,anIntervalMode)) )      
						 ) {
							//System.out.println("NAVIGATE: WITHIN BOUNDS OF A TERRAIN");
								if (terrain == null) {
		
									if (footY > landingPosition || Math.abs(footY - landingPosition) <= landingPrecision) {
												terrain = anInterval;
												terrainMode = anIntervalMode;
									}
									
								}
								else {
									
									
									if ((footY > landingPosition || Math.abs(footY - landingPosition) <= landingPrecision)
										&& (Math.abs(landingPosition - terrain.getLandingPosition(entity,terrainMode)) <= landingPrecision)
										&& ( (leftwardMovement && angle <= terrain.getPlatformAngle(terrainMode)) ||
												(rightwardMovement && angle >= terrain.getPlatformAngle(terrainMode))  )
										) {
										// This case is for terrain that intersects other terrain, when feet are at the intersection point of two terrain, tie-breaker
								//				System.out.println("TERRAIN-TERRAIN INTERSECTION!");
										//		System.exit(0);
												terrain = anInterval;
												terrainMode = anIntervalMode;
									}
									else if ((footY > landingPosition || Math.abs(footY - landingPosition) <= landingPrecision)
											&& (landingPosition > terrain.getLandingPosition(entity,terrainMode))) {
										// Otherwise, just pick the terrain with landing position closest to feet
										terrain = anInterval;
										terrainMode = anIntervalMode;
									}
										
										
								}
						}
						else {
			
								if (closestTerrain == null) {
		
									if ( ( rightwardMovement && (footY > startingPosition  || Math.abs(footY - startingPosition) <= landingPrecision)
										&& (x1 > footX || Math.abs(x1 - footX) <= landingPrecision) )
											||
										( leftwardMovement && (footY > endingPosition  || Math.abs(footY - endingPosition) <= landingPrecision)
										&& (x2 < footX || Math.abs(x2 - footX) <= landingPrecision) )	) {
												closestTerrain = anInterval;
												closestTerrainMode = anIntervalMode;
									}
		
								}
								else {
		
									if ( ( rightwardMovement && ( ( (footY > startingPosition || Math.abs(footY - startingPosition) <= landingPrecision)
											&& (x1 > footX || Math.abs(x1 - footX) <= landingPrecision) && (x1 < closestTerrain.getX1(entity,closestTerrainMode)) )
											||
											( (footY > startingPosition || Math.abs(footY - startingPosition) <= landingPrecision) && (x1 > footX || Math.abs(x1 - footX) <= landingPrecision)
											&& (Math.abs(x1 - closestTerrain.getX1(entity,closestTerrainMode)) <= landingPrecision) && (startingPosition > closestTerrain.getY1(entity, closestTerrainMode))  )  ) ) 
										||
											(leftwardMovement && ( ( (footY > endingPosition || Math.abs(footY - endingPosition) <= landingPrecision)
											&& (x2 < footX || Math.abs(x2 - footX) <= landingPrecision) && (x2 > closestTerrain.getX2(entity,closestTerrainMode)) )
											||
											( (footY > endingPosition || Math.abs(footY - endingPosition) <= landingPrecision) && (x2 < footX || Math.abs(x2 - footX) <= landingPrecision)
											&& (Math.abs(x2 - closestTerrain.getX2(entity,closestTerrainMode)) <= landingPrecision) && (endingPosition > closestTerrain.getY2(entity, closestTerrainMode))  )  ) )
											) {
												closestTerrain = anInterval;
												closestTerrainMode = anIntervalMode;
									}
								}
									
						}
				
					}
				}
			}

			
			double x1Value = 0.0, x2Value = 0.0, startingPosition = 0.0, endingPosition = 0.0, landingPosition = 0.0, angle = 0.0;
			boolean onTerrain = false, onCeiling = false;
			
		
			/*/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\
			 *    CHECK AND HANDLE TERRAIN-TERRAIN INTERSECTION    *
			 /\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\/\*/
			
			/*double closestIntersectionDistance = Double.MAX_VALUE;
			double closestIntersectionX = 0.0, closestIntersectionY = 0.0;
			
			
			if (terrain != null) {
				
				for (int a = 0; a < activeBackgrounds.size(); a++) {
					for (int v = 0; v < activeBackgrounds.get(a).getIntervals().size(); v++) {
					
						// Check if this interval is a left and/or right cliff
						ArrayList<Integer> terrainModes = new ArrayList<Integer>(); // mode == 1: standard, mode == 2: leftCliff, mode == 3: rightCliff
						terrainModes.add(1);
						if (activeBackgrounds.get(a).getIntervals().get(v).isLeftCliff()) {
							terrainModes.add(2);
						}
						if (activeBackgrounds.get(a).getIntervals().get(v).isRightCliff()) {
							terrainModes.add(3);
						}
						
						for (int m = 0; m < terrainModes.size(); m++) {
							Interval anInterval = activeBackgrounds.get(a).getIntervals().get(v);
							int anIntervalMode = terrainModes.get(m);
							double x1 = anInterval.getX1(entity, anIntervalMode),   x2 = anInterval.getX2(entity, anIntervalMode);
							double y1 = anInterval.getY1(entity, anIntervalMode),   y2 = anInterval.getY2(entity, anIntervalMode);
							double terrainX1 = terrain.getX1(entity, terrainMode), terrainX2 = terrain.getX2(entity, terrainMode);
							double terrainY1 = terrain.getY1(entity, terrainMode), terrainY2 = terrain.getY2(entity, terrainMode);
							double landingPositionAtFootX = terrain.getLandingPositionFromSpecificPosition(entity, footX, terrainMode);
							
							double[] terrainTerrainIntersectionResult = null;
						
							//double relevantEndpointXForThisInterval = leftwardMovement ? terrainX1 : terrainX2;
						//	double relevantEndpointYForThisInterval = leftwardMovement ? terrainY1 : terrainY2;
						//	if (relevantEndpointXForThisInterval == x1 && relevantEndpointYForThisInterval == y1) {
						//		terrainTerrainIntersectionResult = new double[] {x1, y1};   // USED FOR ROUNDING
						//	}
						//	else if (relevantEndpointXForThisInterval == x2 && relevantEndpointYForThisInterval == y2) {
						//		terrainTerrainIntersectionResult = new double[] {x2, y2};   // USED FOR ROUNDING
							//}
							//else {
								terrainTerrainIntersectionResult = lineIntersection(x1,y1,  x2,y2,    terrainX1,terrainY1,  terrainX2,terrainY2);
							//}
							
							if (terrainTerrainIntersectionResult != null) {
								if ((leftwardMovement && isWithinBoundsExclusive(terrainTerrainIntersectionResult[0],terrainTerrainIntersectionResult[1], terrainX1, terrainY1, footX, landingPositionAtFootX))
									||
									(rightwardMovement && isWithinBoundsExclusive(terrainTerrainIntersectionResult[0],terrainTerrainIntersectionResult[1], footX, landingPositionAtFootX, terrainX2, terrainY2))
								) {
									System.out.println("It intercepts terrain!");
									double distanceToIntersection = Math.sqrt((terrainTerrainIntersectionResult[0] - footX)*(terrainTerrainIntersectionResult[0] - footX) +
											(terrainTerrainIntersectionResult[1] - landingPositionAtFootX)*(terrainTerrainIntersectionResult[1] - landingPositionAtFootX) );
									if (distanceToIntersection < closestIntersectionDistance) {
										closestIntersectionDistance = distanceToIntersection;
										closestIntersectionX = terrainTerrainIntersectionResult[0];
										closestIntersectionY = terrainTerrainIntersectionResult[1];
									}
									
								}
							}
				
						}
					
					}
				}
				
			}
			
			*/
			
		
			/*
			 * Interval Analysis
			 */

			double tX1 = -Double.MAX_VALUE, tX2 = Double.MAX_VALUE, tY1 = 0.0, tY2 = 0.0;
			if (terrain != null) {
				tX1 = terrain.getX1(entity, terrainMode);
				tX2 = terrain.getX2(entity, terrainMode);
				tY1 = terrain.getY1(entity, terrainMode);
				tY2 = terrain.getY2(entity, terrainMode);
			}
			if (closestTerrain != null) {
				if (rightwardMovement && closestTerrain.getX1(entity, closestTerrainMode) < tX2) {
					tX2 = closestTerrain.getX1(entity, closestTerrainMode);
					if (terrain != null) {
						tY2 = terrain.getLandingPositionFromSpecificPosition(entity, closestTerrain.getX1(entity, closestTerrainMode), terrainMode);
					}
				}
				else if (leftwardMovement && closestTerrain.getX2(entity, closestTerrainMode) > tX1) {
					tX1 = closestTerrain.getX2(entity, closestTerrainMode);
					if (terrain != null) {
						tY1 = terrain.getLandingPositionFromSpecificPosition(entity, closestTerrain.getX2(entity, closestTerrainMode), terrainMode);
					}
				}
			}
			/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
			 * FINALLY, UPDATE LEFT OR RIGHT ENDPOINT IF A TERRAIN INTERSECTS BEFORE ENDPOINT  *
			 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
			//if (closestIntersectionDistance <= 10.0) {
		//		System.out.println("closestIntersectionDistance <= 10.0");
		//		System.exit(0);
			//}
			/*
			if (terrain != null) {
				double landingPositionAtFootX = terrain.getLandingPositionFromSpecificPosition(entity, footX, terrainMode);
				if (leftwardMovement) {
					double distanceToLeftEndpoint = Math.sqrt( (x1Value - footX)*(x1Value - footX) + (startingPosition - landingPositionAtFootX)*(startingPosition - landingPositionAtFootX) );
					if (closestIntersectionDistance < distanceToLeftEndpoint) {
						System.out.println("navigate(): overwriting x1");
						x1Value = closestIntersectionX;
						startingPosition = closestIntersectionY;
					}
				}
				else if (rightwardMovement) {
					double distanceToRightEndpoint = Math.sqrt( (x2Value - footX)*(x2Value - footX) + (endingPosition - landingPositionAtFootX)*(endingPosition - landingPositionAtFootX) );
					if (closestIntersectionDistance < distanceToRightEndpoint) {
						System.out.println("navigate(): overwriting x2");
						x2Value = closestIntersectionX;
						endingPosition = closestIntersectionY;
					}
				}
			}
			*/
			
			double intervalEndpointX = 0.0;
			double intervalEndpointY = 0.0;
			
			if (rightwardMovement) {
				intervalEndpointX = tX2;
				intervalEndpointY = tY2;
			} else if (leftwardMovement) {
				intervalEndpointX = tX1;
				intervalEndpointY = tY1;
			}
	
			
			// Finally, call navigateTerrainAndCeilings
			amountToMove = traverseTerrainAndCeilings(entity,amountToMove,terrain,terrainMode,new double[] {intervalEndpointX,intervalEndpointY},motionVector);
		
		
			/*if (terrain != null) {
				entityDepth = terrain.getZ2()
						* ((entity.getX() - terrain.getX1(0.5 * entity.getFeetWidth()))
								/ (terrain.getX2(0.5 * entity.getFeetWidth())
										- terrain.getX1(0.5 * entity.getFeetWidth())))
						+ terrain.getZ1()
								* (1 - ((entity.getX() - terrain.getX1(0.5 * entity.getFeetWidth()))
										/ (terrain.getX2(0.5 * entity.getFeetWidth())
												- terrain.getX1(0.5 * entity.getFeetWidth())))); }
				else {
					entityDepth = entity.getZ();
				}
				entity.setZ(entityDepth);*/

		    


		}
		
		double endEntXPos = entity.getX();
		if (Math.abs(endEntXPos - startEntXPos) > 0.0401) {
			System.out.println("Abnormally large dx: "+(endEntXPos - startEntXPos));
		//	System.exit(0);
		}
		System.out.println("--------------------------------------------------------------------");
		System.out.println("-----------------------navigate END-------------------------------");
		System.out.println("--------------------------------------------------------------------");

		
	}
	
	
	
	
	
	// returns amountToMove remaining
	public double traverseTerrainAndCeilings(Entity entity, double totalAmountToMove, Interval terrain, int terrainMode, double[] terrainEndpoint, Vector2d motionVector) {

		//System.out.println(" ");
		//System.out.println(" ");
		//System.out.println("^^^^^^^^^^^^^^^^^^^^^traverse BEGIN^^^^^^^^^^^^^^^^^^^^^");

		if (leftwardMovement) {
			//System.out.println("direction : LEFT");
		}
		else if (rightwardMovement) {
			//System.out.println("direction : RIGHT");
		}
		
		double amountToMove = totalAmountToMove;
		double dxToMove = 0.0, dyToMove = 0.0;
		boolean leftwardMovement, rightwardMovement;
		
		if (motionVector.x < 0.0) {
			leftwardMovement = true;
			rightwardMovement = false;
		}
		else if (motionVector.x > 0.0) {
			rightwardMovement = true;
			leftwardMovement = false;
		}
		else {
			leftwardMovement = false;
			rightwardMovement = false;
		}
		double motionVectorX = 0.0;
		if (rightwardMovement) {
			motionVectorX = 1.0;
		} else if (leftwardMovement) {
			motionVectorX = -1.0;
		}
		
		double terrainAngleSign, terrainAngle;
		  if (terrain != null) {	
			if (terrain.getPlatformAngle(terrainMode) > 0.0
					|| Math.abs(terrain.getPlatformAngle(terrainMode)) <= 0.00000000001) {
				terrainAngleSign = 1.0;
			} else {
				terrainAngleSign = -1.0;
			}
			
			terrainAngle = terrain.getPlatformAngle(terrainMode);
		  }
		  else {
			  terrainAngleSign = 1.0;
			  terrainAngle = 0.0;
		  }
		
		double terrainEndpointX = terrainEndpoint[0], terrainEndpointY = terrainEndpoint[1];
		double atTerrainEndpointPrecision = landingPrecision;
		double lastFootPosition[] = null;
		
		while (amountToMove > 0.0) {
			//System.out.println("                 **                   ");
			//System.out.println("*****TRAVERSE: WHILE LOOP BEGIN**********");
			 
			double footX = entity.getX() + dxToMove;
			double footY = entity.getY() - 0.5*entity.getHeight() + dyToMove;
			double headY = entity.getY() + 0.5*entity.getHeight() + dyToMove;
			
			this.outOfIntervalRangeThreshold = amountToMove + 0.00000001;
			//System.out.println("TRAVERSE: terrainEPX = "+terrainEndpointX +", terrainEPY = "+terrainEndpointY);
			//System.out.println("TRAVERSE: aTM = "+amountToMove);
			if (Math.abs(footX - terrainEndpointX) <= atTerrainEndpointPrecision) {
				/*
				 * Terrain endpoint reached, so finished pacing through ceilings! (for now)
				 */
				//System.out.println("HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH");
				//System.out.println("HHHHHHH Terrain endpoint reached HHHHHHHHHH");
				//System.out.println("HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH");
		//		System.exit(0);
				break;
			}


			// Need to check onPlatform status every iteration of the while loop here because
			// ceiling can change trajectory while in air and cause a platform intersection
			boolean onPlatform = false;
		   if (terrain != null) {
			   //System.out.println("TERRAIN != NULL");  
	  		if (Math.abs(footY - terrain.getLandingPositionFromSpecificPosition(entity, footX, terrainMode)) <= landingPrecision) {
						onPlatform = true;
				//		System.out.println("   trav: terrainLP = "+terrain.getLandingPositionFromSpecificPosition(entity, footX, terrainMode));
			}
		   }
			//System.out.println("TRAVERSE: footX = "+footX+", footY = "+footY);
			//System.out.println("   traverse: onPlatform = "+onPlatform);
			if (terrain != null) {
				//System.out.println("Terrain LP = "+terrain.getLandingPositionFromSpecificPosition(entity, footX, terrainMode));
			}
			
			Interval creaturesCeiling = null;
			int creaturesCeilingIntervalMode = 1;
			Interval closestCeiling = null;
			int closestCeilingIntervalMode = 1;
			
			//
			//  This block of code handles the issue of intending to move to an end point but barely missing it (rounding issue)
			//
			Interval.exactMode = false;
			/*double sameFootPosThreshold = 0.00000000001;
			Interval.exactMode = true;
			if (lastFootPosition != null) {
				if (Math.abs(footX - lastFootPosition[0]) <= sameFootPosThreshold 
					&& Math.abs(footY - lastFootPosition[1]) <= sameFootPosThreshold) {
					Interval.exactMode = false;
				}
			}
			lastFootPosition = new double[] {footX, footY};*/
			//
			
			double entityExtentPaddingLeft = 0.0, entityExtentPaddingRight = 0.0;
			if (leftwardMovement) {
				entityExtentPaddingLeft = -amountToMove;
			} else if (rightwardMovement) {
				entityExtentPaddingRight = amountToMove;
			}
			int hashCodes[] = currentWorld.getCeilings().genHashCodesFromKeysX(entity.getX() + entityExtentPaddingLeft - 0.5*entity.getFeetWidth(),  entity.getX() + entityExtentPaddingRight + 0.5*entity.getFeetWidth());	

			for (int a = 0; a < hashCodes.length; a++) {
					
				int thisHashCode = hashCodes[a];
				ArrayList<Interval> bucketOfCeilings = currentWorld.getCeilings().getIntervalList(thisHashCode);
				if (bucketOfCeilings == null) {
					continue;
				}
				
				for (int v = 0; v < bucketOfCeilings.size(); v++) {
					this.intervalsInspected++;
					
					ArrayList<Integer> intervalModes = new ArrayList<Integer>();
					intervalModes.add(1);
					if (bucketOfCeilings.get(v).isLeftCliff()) {
						intervalModes.add(2);
					}
					if (bucketOfCeilings.get(v).isLeftCliff()) {
						intervalModes.add(3);
					}
				
					for (int m = 0; m < intervalModes.size(); m++) {
						Interval aCeiling = bucketOfCeilings.get(v);
						int aCeilingIntervalMode = intervalModes.get(m);
						double x1 = aCeiling.getX1(entity, aCeilingIntervalMode),  x2 = aCeiling.getX2(entity, aCeilingIntervalMode);
						
						// Quickly rule out distant intervals
						if (footX < x1 && Math.abs(footX - x1) > outOfIntervalRangeThreshold) {
							continue;
						}
						if (footX > x2 && Math.abs(footX - x2) > outOfIntervalRangeThreshold) {
							continue;
						}
						if (headY < Math.min(aCeiling.getY1(null, 1), aCeiling.getY2(null, 1)) && Math.abs(headY - Math.min(aCeiling.getY1(null, 1), aCeiling.getY2(null, 1))) > outOfIntervalRangeThreshold) {
							continue;
						}
						if (headY > Math.max(aCeiling.getY1(null, 1), aCeiling.getY2(null, 1)) && Math.abs(headY - Math.max(aCeiling.getY1(null, 1), aCeiling.getY2(null, 1))) > outOfIntervalRangeThreshold) {
							continue;
						}
						
						// Have to compute ceilingPosition manually here because entity position is not actually updated until the while loop in navigate finishes
						double ceilingPosition = aCeiling.getLandingPositionFromSpecificPosition(entity, footX, aCeilingIntervalMode);
						if (headY > ceilingPosition && Math.abs(headY - ceilingPosition) > outOfIntervalRangeThreshold) {
							continue;
						}
						double cY1 = aCeiling.getY1(entity, aCeilingIntervalMode),  cY2 = aCeiling.getY2(entity, aCeilingIntervalMode);
						double angle = aCeiling.getPlatformAngle(aCeilingIntervalMode);

	
						
						// Test for ceiling acting as perpendicular wall
						if ( ( (aCeiling.isPositionAtX1(footX,entity, aCeilingIntervalMode) || aCeiling.isPositionAtX2(footX,entity, aCeilingIntervalMode)) && Math.abs(footY - ceilingPosition) <= landingPrecision)
								) {
							 // Let the entity pass  
						}
						else if (  ( rightwardMovement && aCeiling.isPositionAtX1(footX,entity, aCeilingIntervalMode)  && headY > ceilingPosition && headY < ceilingPosition + entity.getHeight())
								||
								( leftwardMovement && aCeiling.isPositionAtX2(footX,entity, aCeilingIntervalMode) && headY > ceilingPosition && headY < ceilingPosition + entity.getHeight())
								) {
									// Ceiling acts as perpendicular wall
									checkForWallsThenMove(entity, amountToMove, dxToMove, dyToMove, motionVector);
									amountToMove = 0.0;
									return amountToMove;
						}
						
						
						
						if ( 
							(rightwardMovement  &&  (footX > x1 ||  aCeiling.isPositionAtX1(footX,entity, aCeilingIntervalMode))  &&  footX < x2  &&  !aCeiling.isPositionAtX2(footX,entity, aCeilingIntervalMode)  &&  (Math.abs(angle) <= landingPrecision || angle < 0.0))
							||
							 (leftwardMovement  &&  footX > x1  &&  (footX < x2 ||  aCeiling.isPositionAtX2(footX,entity, aCeilingIntervalMode))  &&  !aCeiling.isPositionAtX1(footX,entity, aCeilingIntervalMode)  &&  (Math.abs(angle) <= landingPrecision || angle > 0.0))      
							 ) {
								//	System.out.println("Within the bounds of a ceiling");
									
									if (creaturesCeiling == null) {
			
										if (headY < ceilingPosition + entity.getHeight() || Math.abs(headY - (ceilingPosition + entity.getHeight())) <= landingPrecision) {
													creaturesCeiling = aCeiling;
													creaturesCeilingIntervalMode = aCeilingIntervalMode;
										}
										
									}
									else {
										double creaturesCeilingCeilingPosition = creaturesCeiling.getLandingPositionFromSpecificPosition(entity, footX, creaturesCeilingIntervalMode);
										
										if ((headY < ceilingPosition + entity.getHeight() || Math.abs(headY - (ceilingPosition + entity.getHeight())) <= landingPrecision)
											&& (ceilingPosition < creaturesCeilingCeilingPosition
											|| Math.abs(ceilingPosition - creaturesCeilingCeilingPosition) <= landingPrecision)) {
													creaturesCeiling = aCeiling;
													creaturesCeilingIntervalMode = aCeilingIntervalMode;
										}
									}
						}
						else {
									double relevantCeilingPosition = 0.0;
									if (leftwardMovement) {
										relevantCeilingPosition = cY2;
									} else if (rightwardMovement) {
										relevantCeilingPosition = cY1;
									}
			
									if (headY < relevantCeilingPosition + entity.getHeight() || Math.abs(headY - (relevantCeilingPosition + entity.getHeight())) <= landingPrecision) {
											
											if (closestCeiling == null) {
												if ( ( rightwardMovement && (x1 > footX || Math.abs(x1 - footX) <= landingPrecision) && (Math.abs(angle) <= landingPrecision || angle < 0.0))
														||
													( leftwardMovement && (x2 < footX || Math.abs(x2 - footX) <= landingPrecision) && (Math.abs(angle) <= landingPrecision || angle > 0.0))	) {
					
															closestCeiling = aCeiling;
															closestCeilingIntervalMode = aCeilingIntervalMode;
												}
					
											}
											else {
					
												if ( ( rightwardMovement && ( ( (x1 > footX || Math.abs(x1 - footX) <= landingPrecision) && (x1 < closestCeiling.getX1(entity, closestCeilingIntervalMode)) )
														||
														( Math.abs(x1 - closestCeiling.getX1(entity, closestCeilingIntervalMode)) <= landingPrecision && (x1 > footX || Math.abs(x1 - footX) <= landingPrecision) && cY1 < closestCeiling.getY1(entity, closestCeilingIntervalMode)  )  )
														&& (Math.abs(angle) <= landingPrecision || angle < 0.0)) 
													||
														(leftwardMovement && ( ( (x2 < footX || Math.abs(x2 - footX) <= landingPrecision) && (x2 > closestCeiling.getX2(entity, closestCeilingIntervalMode)) )
														||
														( Math.abs(x2 - closestCeiling.getX2(entity, closestCeilingIntervalMode)) <= landingPrecision && (x2 < footX || Math.abs(x2 - footX) <= landingPrecision) && cY2 < closestCeiling.getY2(entity, closestCeilingIntervalMode)  )  )
																&& (Math.abs(angle) <= landingPrecision || angle > 0.0))
														) {
															closestCeiling = aCeiling;
															closestCeilingIntervalMode = aCeilingIntervalMode;
												}
											}
									}
									
						}
					}
					
				}
			}


			/*
			 * SET CEILING X1, Y1, X2, Y2
			 */
			
			double cX1 = -Double.MAX_VALUE, cX2 = Double.MAX_VALUE, cY1 = 0.0, cY2 = 0.0, ceilingPosition = 0.0, angle = 0.0;
			double tX1 = -Double.MAX_VALUE, tX2 = Double.MAX_VALUE, tY1 = 0.0, tY2 = 0.0;
			boolean onCeiling = false;			
		
		// FOR TESTING!	
			if (creaturesCeiling != null) {
				//System.out.println("CEILING != NULL");
				//System.out.println("BASELINE ceiling.x1 = "+creaturesCeiling.getX1(entity, creaturesCeilingIntervalMode)+", ceiling.x2 = "+creaturesCeiling.getX2(entity, creaturesCeilingIntervalMode));
				//System.out.println("BASELINE ceiling.y1 = "+creaturesCeiling.getY1(entity, creaturesCeilingIntervalMode)+", ceiling.y2 = "+creaturesCeiling.getY2(entity, creaturesCeilingIntervalMode));
				
			}
			
			
			if (creaturesCeiling != null) {
				//System.out.println(" aNaLySiS: creaturesCeiling is NOT NULL");
				cX1 = creaturesCeiling.getX1(entity, creaturesCeilingIntervalMode);
				cX2 = creaturesCeiling.getX2(entity, creaturesCeilingIntervalMode);
				cY1 = creaturesCeiling.getY1(entity, creaturesCeilingIntervalMode);
				cY2 = creaturesCeiling.getY2(entity, creaturesCeilingIntervalMode);
				
				ceilingPosition = creaturesCeiling.getLandingPositionFromSpecificPosition(entity, footX, creaturesCeilingIntervalMode);
				//System.out.println("TRAVERSE: ceilingPosition = "+ceilingPosition);
				//System.out.println("TRAVERSE: headY = "+headY);
				angle = creaturesCeiling.getPlatformAngle(creaturesCeilingIntervalMode);
				if (Math.abs(headY - ceilingPosition) <= landingPrecision) {
					onCeiling = true;
				}
			}
			if (closestCeiling != null) {
				//System.out.println(" aNaLySiS: closest Ceiling is NOT NULL");
				if (creaturesCeiling == null) {
					if (rightwardMovement) {
						cX2 = closestCeiling.getX1(entity, closestCeilingIntervalMode);
						cY2 = closestCeiling.getY1(entity, closestCeilingIntervalMode);
					}
					if (leftwardMovement) {
						cX1 = closestCeiling.getX2(entity, closestCeilingIntervalMode);
						cY1 = closestCeiling.getY2(entity, closestCeilingIntervalMode);
					}
				}
				if (creaturesCeiling != null) {
					if (leftwardMovement && (creaturesCeiling.getX1(entity, creaturesCeilingIntervalMode) < closestCeiling.getX2(entity, closestCeilingIntervalMode)) ) {
						cX1 = closestCeiling.getX2(entity, closestCeilingIntervalMode);
						cY1 = creaturesCeiling.getLandingPositionFromSpecificPosition(entity, closestCeiling.getX2(entity, closestCeilingIntervalMode), creaturesCeilingIntervalMode);
					} else {
						cX1 = creaturesCeiling.getX1(entity, creaturesCeilingIntervalMode);
						cY1 = creaturesCeiling.getY1(entity, creaturesCeilingIntervalMode);
					}
					if (rightwardMovement && (creaturesCeiling.getX2(entity, creaturesCeilingIntervalMode) > closestCeiling.getX1(entity, closestCeilingIntervalMode)) ) {
						cX2 = closestCeiling.getX1(entity, closestCeilingIntervalMode);
						cY2 = creaturesCeiling.getLandingPositionFromSpecificPosition(entity, closestCeiling.getX1(entity, closestCeilingIntervalMode), creaturesCeilingIntervalMode);
					} else { 
						cX2 = creaturesCeiling.getX2(entity, creaturesCeilingIntervalMode);
						cY2 = creaturesCeiling.getY2(entity, creaturesCeilingIntervalMode);
					}						
				}
			}
			
			/*
			 * NOTE: up to this point cX1 and cX2 represent the closest ceiling endpoint (factors in closest)
			 * NOW MODIFY CX1, CY1, CX2, CY2 IF TERRAIN ENDPOINT IS CLOSER THAN CEILING ENDPOINT 
			 */
			
			
			if (( rightwardMovement && terrainEndpointX < cX2 )     ||    (leftwardMovement && terrainEndpointX > cX1)) {
				//System.out.println("TRAVERSE: Updating ceiling ENDPOINT to Terrain ENDPOINT");
				if (rightwardMovement) {
					//System.out.println("          RIGHT");
					cX2 = terrainEndpointX;
					if (creaturesCeiling == null) {
						//System.out.println("                         creaturesCeiling == null");
						cY2 = terrainEndpointY;
					}
					else {
						cY2 = creaturesCeiling.getLandingPositionFromSpecificPosition(entity, terrainEndpointX, creaturesCeilingIntervalMode);
					}
					
				}
				if (leftwardMovement) {
					//System.out.println("          LEFT");
					cX1 = terrainEndpointX;
					if (creaturesCeiling == null) {
						//System.out.println("                         creaturesCeiling == null");
						cY1 = terrainEndpointY;
					}
					else {						
						cY1 = creaturesCeiling.getLandingPositionFromSpecificPosition(entity, terrainEndpointX, creaturesCeilingIntervalMode);
					} 
				}
			}
			
			/*
			 * FINALLY, SET TERRAIN X1, Y1, X2, Y2
			 */
			tX1 = cX1;
			tX2 = cX2;
			tY1 = terrainEndpointY;
			tY2 = terrainEndpointY;
			
			  if (( rightwardMovement && cX2 < terrainEndpointX)     ||    (leftwardMovement && cX1 > terrainEndpointX)) {
				//System.out.println("TRAVERSE: Updating ceiling ENDPOINT to Terrain ENDPOINT");
				if (rightwardMovement) {
					//System.out.println("          RIGHT");
					if (terrain != null) {
						tY2 = terrain.getLandingPositionFromSpecificPosition(entity, cX2, terrainMode);
					}
					
				}
				if (leftwardMovement) {
					//System.out.println("          LEFT");
					if (terrain != null) {
						tY1 = terrain.getLandingPositionFromSpecificPosition(entity, cX1, terrainMode);
					} 
				}
			}
			
			if (terrain != null) {
				//System.out.println("           ttttttttttttt t.x1 = "+terrain.getX1(entity, terrainMode)+", t.x2 = "+terrain.getX2(entity, terrainMode));
				//System.out.println("           ttttttttttttt t.y1 = "+terrain.getY1(entity, terrainMode)+", t.y2 = "+terrain.getY2(entity, terrainMode));
			}
			//System.out.println("             $$$$$$$$$ TRAVERSE: After any potential ceiling endpoint update, cX1 = "+cX1+", cX2 = "+cX2);
			//System.out.println("             $$$$$$$$$ TRAVERSE: After any potential ceiling endpoint update, cY1 = "+cY1+", cY2 = "+cY2);
			
			//System.out.println("terrain angle = "+terrainAngle);
			//System.out.println("ceiling angle = "+angle);

			double ceilingIntervalEndpointX = 0.0;
			double ceilingIntervalEndpointY = 0.0;
			double terrainIntervalEndpointX = 0.0;
			double terrainIntervalEndpointY = 0.0;
			
			if (rightwardMovement) {
				ceilingIntervalEndpointX = cX2;
				ceilingIntervalEndpointY = cY2;
				terrainIntervalEndpointX = tX2;
				terrainIntervalEndpointY = tY2;
			} else if (leftwardMovement) {
				ceilingIntervalEndpointX = cX1;
				ceilingIntervalEndpointY = cY1;
				terrainIntervalEndpointX = tX1;
				terrainIntervalEndpointY = tY1;
			}
	
			
			//System.out.println("OK.....SO");
			//System.out.println("EndPoint.x = "+ceilingIntervalEndpointX);
			//System.out.println("EndPoint.y = "+ceilingIntervalEndpointY);
			
			if (onPlatform) {
				//System.out.println("@TRAVERSE: onPlatform");
				/*
				 * In this section, entity is onPlatform, 
				 * check if onPlatform motion will cause an intersection between entity head and a ceiling
				 * NOTE:
				 * Passed into this method is a baseline terrain DX (moveAmountDX) and baseline terrain DY (moveAmountDY)
				 * These values can shrink here because the entity must pace movement through CEILING intervals while moving on TERRAIN intervals
				 */
					// onPlatform == true, onCeiling == true OR false (head could intersect ceiling)
					// The terrain deltas can be modified because terrain movement must now be paced through ceiling intervals
					double distanceToEndOfIntervalONPLATFORM = Math.sqrt( (terrainIntervalEndpointX - footX)*(terrainIntervalEndpointX - footX) + (terrainIntervalEndpointY - footY)*(terrainIntervalEndpointY - footY) );
					//System.out.println("dTEONPLAT = "+distanceToEndOfIntervalONPLATFORM);
					double newDXToMove = motionVectorX * Math.cos(terrainAngle) * Math.min(distanceToEndOfIntervalONPLATFORM, amountToMove);
					double newDYToMove = motionVectorX * Math.sin(terrainAngle) * Math.min(distanceToEndOfIntervalONPLATFORM, amountToMove);
					//System.out.println("newDXToMove = "+newDXToMove);
					//System.out.println("newDYToMove = "+newDYToMove);
					
					double intersectionTest[] = null;
				 	if (creaturesCeiling != null) {
						intersectionTest = lineIntersection(footX, headY,   footX + newDXToMove, headY + newDYToMove,     
								creaturesCeiling.getX1(entity, creaturesCeilingIntervalMode), creaturesCeiling.getY1(entity, creaturesCeilingIntervalMode),  creaturesCeiling.getX2(entity, creaturesCeilingIntervalMode), creaturesCeiling.getY2(entity, creaturesCeilingIntervalMode));
					}
					
					if (intersectionTest != null) {
						//System.out.println("  @TRAVERSE: head and ceiling intersection");
						// ENTITY HEAD MOVEMENT WILL INTERSECT CEILING
						if (Math.abs(footX - intersectionTest[0]) <= landingPrecision) {
							// Entity is already at the ceiling wall
						}
						else {
							// Move entity the remaining distance to the ceiling wall
							amountToMove = checkForWallsThenMove(entity, amountToMove, intersectionTest[0] - footX, intersectionTest[1] - headY, motionVector);							
						}
						amountToMove = 0.0;
					} 
					else {
						// ENTITY HEAD MOVEMENT WILL NOT INTERSECT CEILING
						//System.out.println("  @TRAVERSE: NO head and ceiling intersection");
						double modifiedAmountMoved = Math.sqrt(newDXToMove*newDXToMove + newDYToMove*newDYToMove);	
						amountToMove = setZeroIfThreshold(amountToMove - modifiedAmountMoved, landingPrecision);
						amountToMove = checkForWallsThenMove(entity, amountToMove, newDXToMove, newDYToMove, motionVector);
						
					}
					
					continue;
				
			}   
			
			
			else if ( ! onPlatform) {
				//System.out.println("@TRAVERSE: ! onPlatform");
				double airDistanceToEndOfCeiling = Math.abs(ceilingIntervalEndpointX - footX);
				double[] entityTerrainIntersection = null;
				double[] entityCeilingIntersection = null;

				// If not on a ceiling and in a terrain interval, check for air entity-terrain intersection
				if (!onCeiling && terrain != null) {
					//System.out.println("    @TRAVERSE: !onCeiling && terrain != null.");
					//System.out.println("airDTE = "+airDistanceToEndOfCeiling);
					//System.out.println("aTM = "+amountToMove);
					//System.out.println("BEING EXPLICIT: VALUES USED IN LINE INTERSECTION TEST (EXACT MODE)");
					//System.out.println(" footX = "+footX+", footY = "+footY+", footX + motionVectorX*Math.min(airDistanceToEndOfCeiling, amountToMove) = "+(footX + motionVectorX*Math.min(airDistanceToEndOfCeiling, amountToMove))+", footY = "+footY);
   					//System.out.println("terrain.getX1(entity, terrainMode) = "+terrain.getX1(entity, terrainMode)+", terrain.getY1(entity, terrainMode) = "+terrain.getY1(entity, terrainMode)+", terrain.getX2(entity, terrainMode) = "+terrain.getX2(entity, terrainMode)+", terrain.getY2(entity, terrainMode) = "+terrain.getY2(entity, terrainMode));
					// do l_t test
					entityTerrainIntersection = lineIntersection(footX, footY,  (footX + motionVectorX*Math.min(airDistanceToEndOfCeiling, amountToMove)), footY,
							terrain.getX1(entity, terrainMode), terrain.getY1(entity, terrainMode),  terrain.getX2(entity, terrainMode), terrain.getY2(entity, terrainMode));
										
				}
				// If not on a ceiling and in a ceiling interval, check for air entity-ceiling intersection
				if (creaturesCeiling != null && !onCeiling) {
					// do l_c test
					//System.out.println("    @TRAVERSE: !onCeiling && creaturesCeiling != null.");
					entityCeilingIntersection = lineIntersection(footX, headY,  footX + motionVectorX*Math.min(airDistanceToEndOfCeiling, amountToMove), headY,
							creaturesCeiling.getX1(entity, creaturesCeilingIntervalMode), creaturesCeiling.getY1(entity, creaturesCeilingIntervalMode),  creaturesCeiling.getX2(entity, creaturesCeilingIntervalMode), creaturesCeiling.getY2(entity, creaturesCeilingIntervalMode));
					//System.out.println("        footX = "+footX+", headY = "+headY+", footX + DX = "+(footX + motionVectorX*Math.min(airDistanceToEndOfCeiling, amountToMove))+", headY = "+headY);
					//System.out.println("cX1 = "+cX1+", cX2 = "+cX2+", cY1 = "+cY1+", cY2 = "+cY2);
				}
				
				//System.out.println("    @TRAVERSE: AFTER INITIAL L TEST STUFF: entityTerrainIntersection = "+entityTerrainIntersection);
				//System.out.println("    @TRAVERSE: AFTER INITIAL L TEST STUFF: entityCeilIntersection = "+entityCeilingIntersection);
				
				
				
				
				
				 
				
				
				
				
				if (!onCeiling) {
//					System.out.println("    @TRAVERSE: L TESTS, !onCeiling && !onPlatform");
					/*
					 * NOT ONPLATFORM and NOT ONCEILING
					 * TESTING l_t and l_c
					 */
					if (entityTerrainIntersection == null && entityCeilingIntersection == null) {
	//					System.out.println("        @TRAVERSE: l");
						// move by min(dTEC, aTM)
						double l = motionVectorX*Math.min(airDistanceToEndOfCeiling, amountToMove);
		//				System.out.println("        @TRAVERSE: l = "+l);
						amountToMove = setZeroIfThreshold(amountToMove - Math.abs(l), landingPrecision);
						amountToMove = checkForWallsThenMove(entity, amountToMove, l, 0.0, motionVector);
						
					}
					else if (entityTerrainIntersection != null && entityCeilingIntersection == null) {
			//			System.out.println("        @TRAVERSE: l_t");
						// move by l_t
						double l_t = motionVectorX*Math.abs(entityTerrainIntersection[0] - footX);
				//		System.out.println("        @TRAVERSE: l_t = "+l_t);
						amountToMove = setZeroIfThreshold(amountToMove - Math.abs(l_t), landingPrecision);
						amountToMove = checkForWallsThenMove(entity, amountToMove, l_t, 0.0, motionVector);
					}
					else if (entityTerrainIntersection == null && entityCeilingIntersection != null) {
					//	System.out.println("        @TRAVERSE: l_c");
						// move by l_c
						double l_c = motionVectorX*Math.abs(entityCeilingIntersection[0] - footX);
//						System.out.println("        @TRAVERSE: l_c = "+l_c);
						amountToMove = setZeroIfThreshold(amountToMove - Math.abs(l_c), landingPrecision);
						amountToMove = checkForWallsThenMove(entity, amountToMove, l_c, 0.0, motionVector);
					}
					else if (entityTerrainIntersection != null && entityCeilingIntersection != null) {
	//					System.out.println("        @TRAVERSE: l_t AND l_c");
						// move by min(l_t, l_c)
						double l_t = Math.abs(entityTerrainIntersection[0] - footX);
						double l_c = Math.abs(entityCeilingIntersection[0] - footX);
		//				System.out.println("        @TRAVERSE: l_t = "+l_t); 
			//			System.out.println("        @TRAVERSE: l_c = "+l_c);
						double l = motionVectorX*Math.min(l_t, l_c);
				//		System.out.println("        @TRAVERSE: l = "+l);
						amountToMove = setZeroIfThreshold(amountToMove - Math.abs(l), landingPrecision);
						amountToMove = checkForWallsThenMove(entity, amountToMove, l, 0.0, motionVector);
					}
				}
				
				else if (creaturesCeiling != null && onCeiling) {
//					System.out.println("    @TRAVERSE: onCeiling && creaturesCeiling != nulll");
						// HERE, CEILING CONTROLS HORIZONTAL/VERTICAL TRAJECTORY
						// onPlatform == false, onCeiling == true OR false 
						// 						(could have hasCeilingPosition == false or (hasCeilingPosition == true && onCeiling == true))
						double ceilingDistanceToEndOfCeiling = Math.sqrt( (ceilingIntervalEndpointX - footX)*(ceilingIntervalEndpointX - footX) + 
																			(ceilingIntervalEndpointY - ceilingPosition)*(ceilingIntervalEndpointY - ceilingPosition)
																		  );
						/*
						 * * * * * * * * * * * * * * * * * * * *
						 * 			Movement on ceiling        *
						 * * * * * * * * * * * * * * * * * * * *
						 * * Check if movement along ceiling * *  
						 * * will cause an intersection with * *
						 * *             platform            * *
						 * * * * * * * * * * * * * * * * * * * *
						 */
						
						double newDXToMove = motionVectorX*Math.abs(Math.cos(angle))*Math.min(amountToMove, ceilingDistanceToEndOfCeiling);
						double newDYToMove = motionVectorX*Math.sin(angle)*Math.min(amountToMove, ceilingDistanceToEndOfCeiling);
	//					System.out.println("newDXToMove, newDYToMove = "+newDXToMove+", "+newDYToMove);
						if (terrain != null) {
								double intersectionTest[] = lineIntersection(footX, footY,   footX + newDXToMove, footY + newDYToMove,
									terrain.getX1(entity,terrainMode), terrain.getY1(entity,terrainMode),   terrain.getX2(entity,terrainMode), terrain.getY2(entity,terrainMode));
								if (intersectionTest != null) {
											// Movement along ceiling while in air WILL cause intersection with terrain here
		//							System.out.println("Movement on ceiling WILL intercept terrain");
											double remainingXToMove = intersectionTest[0] - footX;
											double remainingYToMove = intersectionTest[1] - footY;											
											double dDistanceToMove = Math.sqrt(remainingXToMove*remainingXToMove + remainingYToMove*remainingYToMove);
											amountToMove = setZeroIfThreshold(amountToMove - dDistanceToMove,landingPrecision);
											amountToMove = checkForWallsThenMove(entity, amountToMove, remainingXToMove, remainingYToMove, motionVector);
								}
								else {
									// Movement along ceiling while in air will NOT cause intersection with platform here
			//						System.out.println("Movement on ceiling will NOT intercept terrain");
				//					System.out.println("dxToMove = "+dxToMove);
									double dDistanceToMove = Math.sqrt(newDXToMove*newDXToMove + newDYToMove*newDYToMove);
									amountToMove = setZeroIfThreshold(amountToMove - dDistanceToMove,landingPrecision);
									amountToMove = checkForWallsThenMove(entity, amountToMove, newDXToMove, newDYToMove, motionVector);
								}
						}
						else {							
								// Not within a TERRAIN interval
								double dDistanceToMove = Math.sqrt(newDXToMove*newDXToMove + newDYToMove*newDYToMove);
								amountToMove = setZeroIfThreshold(amountToMove - dDistanceToMove,landingPrecision);
								amountToMove = checkForWallsThenMove(entity, amountToMove, newDXToMove, newDYToMove, motionVector);
						}
						continue;
						
						
				}
				else if (creaturesCeiling == null) {
					// REMEMBER, NOT ON PLATFORM HERE					
					double newDXToMove = motionVectorX*Math.min(Math.abs(amountToMove), Math.abs(ceilingIntervalEndpointX - footX));
					double newDYToMove = 0.0;
					double dDistanceToMove = Math.sqrt(newDXToMove*newDXToMove + newDYToMove*newDYToMove);
					amountToMove = setZeroIfThreshold(amountToMove - dDistanceToMove,landingPrecision);
					amountToMove = checkForWallsThenMove(entity, amountToMove, newDXToMove, newDYToMove, motionVector);
					continue;
					
				}
				
				
			}
			//System.out.println("*****TRAVERSE: WHILE LOOP END**********");
			//System.out.println("                 **                   ");
		}
		// OUTSIDE THE WHILE LOOP
		
		System.out.println("^^^^^^^^^^^^^^^^^^^^^traverse END^^^^^^^^^^^^^^^^^^^^^, finishing with amountToMove = "+amountToMove);
		System.out.println(" ");
		System.out.println(" ");
		return amountToMove;
		
		
		
	
		
	}
	
	
	
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    METHOD FOR HANDLING MOTION DUE TO FALLING OR JUMPING,
	    INCLUDING SLIPPING ALONG SLOPED WALLS                 
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	
	//
	// At footX = 2.239998532495665 a problem is occurring
	//
	
	public void jumpOrFall(Entity entity) {
System.out.println("***************jumpOrFall START****************");
System.out.println("FOOTX IS "+(entity.getX()));
System.out.println("FOOTY IS "+(entity.getY()-0.5*entity.getHeight()));
if (Double.isNaN(entity.getX())) {
	
	System.out.println("footx is null");
	System.exit(0);
	
}
		emptySpaceInterval = null;
		creaturesIntervalInWallRegion = null;

		/**********************
		****    JUMPING    ****
		**********************/
		if (entity.getJumping()) {

			if (entity.getJumpTime() > 1.50) {
				System.exit(0);
			}

			if (entity.getJumpTime() < 0.04) {
				entity.setJumping(false);
				entity.setJumpTime(0.48);
				entity.setFallTime(0.0);
				entity.setFalling(true);
				entity.setFallVelocity(0.0);
				
			} else {
				displacement = jumpConstant1 * initialJumpOrFallVelocity
						+ jumpConstant2 * 0.02*gravityAcceleration * entity.getJumpTime();

				entity.setJumpTime(entity.getJumpTime() - 0.0174667);

				// creaturesIntervalFound = false;

				//creaturesInterval = null;
				Interval ceilingInterval = null;
				int ceilingIntervalMode = 1;

				double footX = entity.getX();
				double headY = entity.getY() + 0.5*entity.getHeight();
				
				
				int hashCodes[] = currentWorld.getCeilings().genHashCodesFromKeysX(entity.getX() - 0.5*entity.getFeetWidth(),  entity.getX() + 0.5*entity.getFeetWidth());				
			
			
				for (int a = 0; a < hashCodes.length; a++) {
					
					int thisHashCode = hashCodes[a];
					ArrayList<Interval> bucketOfCeilings = currentWorld.getCeilings().getIntervalList(thisHashCode);
					if (bucketOfCeilings == null) {
						continue;
					}
					
					for (int v = 0; v < bucketOfCeilings.size(); v++) {
						
						ArrayList<Integer> intervalModes = new ArrayList<Integer>();
						intervalModes.add(1);
						if (bucketOfCeilings.get(v).isLeftCliff()) {
							intervalModes.add(2);
						}
						if (bucketOfCeilings.get(v).isLeftCliff()) {
							intervalModes.add(3);
						}
						
						for (int m = 0; m < intervalModes.size(); m++) {
							Interval aCeiling = bucketOfCeilings.get(v);
							int anIntervalMode = intervalModes.get(m);
							double x1 = aCeiling.getX1(entity,anIntervalMode),   x2 = aCeiling.getX2(entity,anIntervalMode);
							double ceilingPosition = aCeiling.getLandingPosition(entity,anIntervalMode);
							
							if ( (footX > x1 || aCeiling.isPositionAtX1(footX,entity,anIntervalMode))   &&   (footX < x2 || aCeiling.isPositionAtX2(footX,entity,anIntervalMode)) ) {
	
								if (ceilingInterval == null) {
	
									if (headY < ceilingPosition || Math.abs(headY - ceilingPosition) <= landingPrecision) {
										ceilingInterval = aCeiling;
										ceilingIntervalMode = anIntervalMode;
									}
								}
	
								else {
	
									if ((entity.getY() + 0.5 * entity.getHeight() < bucketOfCeilings
											.get(v).getLandingPosition(entity, anIntervalMode)
											|| Math.abs(entity.getY() + 0.5 * entity.getHeight()
													- bucketOfCeilings.get(v).getLandingPosition(
															entity, anIntervalMode)) <= landingPrecision)
											&& (bucketOfCeilings.get(v)
													.getLandingPosition(entity, anIntervalMode) < ceilingInterval
															.getLandingPosition(entity, ceilingIntervalMode)
													|| Math.abs(bucketOfCeilings.get(v)
															.getLandingPosition(entity, anIntervalMode)
															- ceilingInterval.getLandingPosition(
																	entity, ceilingIntervalMode)) <= landingPrecision)) {
											ceilingInterval = bucketOfCeilings.get(v);
											ceilingIntervalMode = anIntervalMode;
										
									}
	
								}
	
							}
						}
					}
				}
				
				double landingPosition = 0.0;
				if (ceilingInterval != null) {

					landingPosition = ceilingInterval.getLandingPosition(entity, ceilingIntervalMode);

					if (entity.getY() + 0.5 * entity.getHeight() + displacement < landingPosition
							|| Math.abs(entity.getY() + 0.5 * entity.getHeight() + displacement
									- landingPosition) <= landingPrecision) {

						entity.setDX(0.0);
						entity.setDY(displacement);
						entity.move(3);
						entity.setJumping(true);
					} else {

						entity.setDX(0.0);
						entity.setDY(landingPosition - (entity.getY() + 0.5 * entity.getHeight()));
						entity.move(3);

						entity.setJumpTime(0.48);
						entity.setFallTime(0.0);
						entity.setJumping(false);
						entity.setFalling(true);

					}

				}

				else {

					entity.setDX(0.0);
					entity.setDY(displacement);
					entity.move(3);
					entity.setJumping(true);
				}

			}

  		}
		
		
		/**********************
		****    FALLING    ****
		**********************/
		else {	
			boolean doneFalling = false;
			boolean firstWhileLoopIteration = true;
			double amountToFall = 0.0;        // FOR FALLING, AMOUNTTOFALL DEPENDS ON ENTITY's INTERVAL LOCATION AND STATE, SO IT'S DETERMINED IN THE FIRST WHILE LOOP ITERATION AFTER SEARCHING INTERVALS
			double dxToMove = 0.0, dyToMove = 0.0;

			//
			boolean exactMode = true;
			//
			
			/*
			 * 
			 */
			Interval lastIntervalTraversed = null;
			
			// USING DONEFALLING INSTEAD OF AMOUNTTOFALL AS CONDITIONAL BECAUSE IF ENTITY IS NOT ON PLATFORM, MOVES THE EXACT FALL AMOUNT REQUESTED 
			// AND ENDS UP ON PLATFORM, NEED TO LOOP BACK TO SET FALLING TO FALSE AND CRUCIALLY, RESET FALL VELOCITY 
			while (!doneFalling) {
				System.out.println("   !!!JumpOrFall: While Loop BEGIN");
				
				double footX = entity.getX() + dxToMove;
				double footY = entity.getY() - 0.5*entity.getHeight() + dyToMove;
				Interval creaturesInterval = null;
				int intervalMode = 1;
				double x1Value = 0.0, x2Value = 0.0, startingPositionValue = 0.0, endingPositionValue = 0.0;
				double closestWallTerrainIntersectionX = 0.0;
				double closestWallTerrainIntersectionY = 0.0;
				boolean onPlatform = false, onWall = false;
				

				
				/**************************************************
				 CHECK IF ENTITY IS WITHIN A SLOPED WALL INTERVAL				 
				 **************************************************/
				
				int hashCodes[] = currentWorld.getSlopedWalls().genHashCodesFromKeysX(footX - 0.5*entity.getFeetWidth(),  footX + 0.5*entity.getFeetWidth());
				
				for (int a = 0; a < hashCodes.length; a++) {
						
					int thisHashCode = hashCodes[a];
					ArrayList<Interval> bucketOfWalls = currentWorld.getSlopedWalls().getIntervalList(thisHashCode);
					if (bucketOfWalls == null) {
						continue;
					}
					
					for (int v = 0; v < bucketOfWalls.size(); v++) {
						Interval aSlopedWall = bucketOfWalls.get(v);
						double x1 = aSlopedWall.getX1(entity, 1),   x2 = aSlopedWall.getX2(entity, 1);
						double wallPosition = aSlopedWall.getLandingPositionFromSpecificPosition(entity, footX, 1);
						double wallAngle = aSlopedWall.getPlatformAngle(1);
						
						
						
						
						if ( (wallAngle > 0.0  &&  (  
								footX > x1  && (footX < x2 || aSlopedWall.isPositionAtX2(footX,entity, 1)) && !(aSlopedWall.isPositionAtX1(footX,entity, 1)) )   )  
								||  
							(wallAngle < 0.0  &&  (  
								(footX > x1 || aSlopedWall.isPositionAtX1(footX,entity, 1))  &&  footX < x2   && !(aSlopedWall.isPositionAtX2(footX,entity, 1)) )   )
							) 
						{
							System.out.println("jumpOrFall: POTENTIAL WALL FOUND");
							
								if (creaturesInterval == null) {
									if (footY > wallPosition || Math.abs(footY - wallPosition) <= landingPrecision) {
										creaturesInterval = aSlopedWall;
									}
									
								}
								else {
									double creaturesIntervalWallPosition = creaturesInterval.getLandingPositionFromSpecificPosition(entity, footX, 1);
									if ((footY > wallPosition || Math.abs(footY - wallPosition) <= landingPrecision)  &&
									((wallPosition > creaturesIntervalWallPosition || Math.abs(wallPosition - creaturesIntervalWallPosition) <= landingPrecision)) 
									) {
										System.out.println("jumpOrFall: WALL SELECTED");
										creaturesInterval = aSlopedWall;
									}
								}
								
								
						}
						
					}
				}
				
				/* 
				 * If the last sloped wall interval *traversed* is the same as the new interval,
				 * then entity must be at the very end of the interval but is very slightly still in the original interval.
				 * When this happens, set creaturesInterval to the appropriate neighboring interval instead
				 */
				/*if (amountToFall != 0.0 && creaturesInterval != null && lastIntervalTraversed != null) {
					if (creaturesInterval.getType() == 2 && Interval.areIntervalsSame(entity, creaturesInterval, lastIntervalTraversed)) {

						if (creaturesInterval.getPlatformAngle(1) > 0.0) {
							if (creaturesInterval.getLeftInterval() == null) {
								creaturesInterval = null;
							}
							else {
								if (creaturesInterval.getLeftInterval().getType() != 3) {
									creaturesInterval = creaturesInterval.getLeftInterval();
								}
							}
						}
						
						else if (creaturesInterval.getPlatformAngle(1) <= 0.0) {
							if (creaturesInterval.getRightInterval() == null) {
								creaturesInterval = null;
							}
							else {
								if (creaturesInterval.getRightInterval().getType() != 3) {
									creaturesInterval = creaturesInterval.getRightInterval();
								}
							}
						}
						
						
						
					}
				}*/
				
				if (creaturesInterval != null) {
					System.out.println("jumpOrFall: AFTER WALL SEARCH, creaturesInterval != null");
				}
				
				
				
				
				if (creaturesInterval != null) {
					// IF ENTITY IS FOUND WITHIN A WALL THESE VALUES ARE VALID
					closestWallTerrainIntersectionX = (creaturesInterval.getPlatformAngle(1) > 0.0 ? creaturesInterval.getX1(entity,1) : creaturesInterval.getX2(entity,1));
					closestWallTerrainIntersectionY = (creaturesInterval.getPlatformAngle(1) > 0.0 ? creaturesInterval.getY1(entity,1) : creaturesInterval.getY2(entity,1));
				}
				//		System.out.println("After wall check, closestInterX = "+closestWallTerrainIntersectionX);
				/*****************************************************
				 THEN CHECK IF ENTITY IS WITHIN A TERRAIN INTERVAL 
				*****************************************************/
				
				hashCodes = currentWorld.getTerrain().genHashCodesFromKeysX(footX - 0.5*entity.getFeetWidth(),  footX + 0.5*entity.getFeetWidth());	

				for (int a = 0; a < hashCodes.length; a++) {
						
					int thisHashCode = hashCodes[a];
					ArrayList<Interval> bucketOfTerrain = currentWorld.getTerrain().getIntervalList(thisHashCode);
					if (bucketOfTerrain== null) {
						continue;
					}
					
					for (int v = 0; v < bucketOfTerrain.size(); v++) {
						
						ArrayList<Integer> intervalModes = new ArrayList<Integer>(); // mode == 1: standard, mode == 2: leftCliff, mode == 3: rightCliff
						intervalModes.add(1);
						if (bucketOfTerrain.get(v).isLeftCliff()) {
							intervalModes.add(2);
						}
						if (bucketOfTerrain.get(v).isRightCliff()) {
							intervalModes.add(3);
						}
						
						for (int m = 1; m <= intervalModes.size(); m++) {
							Interval anInterval = bucketOfTerrain.get(v);
							int anIntervalMode = intervalModes.get(m - 1);
							double x1 = anInterval.getX1(entity, anIntervalMode),   x2 = anInterval.getX2(entity, anIntervalMode);
							double landingPosition = anInterval.getLandingPositionFromSpecificPosition(entity, footX, anIntervalMode);
							double startingPosition = anInterval.getY1(entity,anIntervalMode),   endingPosition = anInterval.getY2(entity,anIntervalMode);
	
							if ( ( (footX > x1 || anInterval.isPositionAtX1(footX,entity, anIntervalMode))  &&  (footX < x2 || anInterval.isPositionAtX2(footX,entity, anIntervalMode)) )  ) 
							{
									if (creaturesInterval == null) {
										if (footY > landingPosition || Math.abs(footY-landingPosition) <= landingPrecision) {
												creaturesInterval = anInterval;
												intervalMode = anIntervalMode;
										}
									}
									else {
										double creaturesIntervalLandingPosition = creaturesInterval.getLandingPositionFromSpecificPosition(entity, footX, intervalMode);									
										
										if ((footY > landingPosition || Math.abs(footY - landingPosition) <= landingPrecision) &&
											(landingPosition > creaturesIntervalLandingPosition || Math.abs(landingPosition - creaturesIntervalLandingPosition) <= landingPrecision)) {
										// THIS TERRAIN IS CLOSER THAN CURRENT TERRAIN OR WALL AT THIS FOOT POINT
												creaturesInterval = anInterval;
												intervalMode = anIntervalMode;
										}
										// THIS TERRAIN IS NOT CLOSER THAN CURRENT TERRAIN OR WALL AT THIS FOOT POINT, CHECK FOR INTERSECTION 
										// IF CREATURES INTERVAL IS A WALL, CHECK FOR INTERSECTING TERRAIN BECAUSE IT CAN CUT THE SLIDING MOVEMENT SHORT
										else if (creaturesInterval.getType() == 2) {
											double intersectionTest[] = lineIntersection(x1,startingPosition, x2,endingPosition,   
													creaturesInterval.getX1(entity, intervalMode),creaturesInterval.getY1(entity, intervalMode), creaturesInterval.getX2(entity, intervalMode),creaturesInterval.getY2(entity, intervalMode));
											if (intersectionTest != null) {
												if ( (intersectionTest[1] > closestWallTerrainIntersectionY || Math.abs(intersectionTest[1] - closestWallTerrainIntersectionY) <= landingPrecision) 
												  && (footY > intersectionTest[1] || Math.abs(footY - intersectionTest[1]) <= landingPrecision)) {
														closestWallTerrainIntersectionX = intersectionTest[0];
														closestWallTerrainIntersectionY = intersectionTest[1];
												}
												
											}
										}
										
									}
							}
							else {
								// NOT WITHIN TERRAIN INTERVAL, BUT STILL CHECK FOR INTERSECTION WITH CREATURES INTERVAL IF IT'S A WALL
								if (creaturesInterval != null) {
									if (creaturesInterval.getType() == 2) {
										double intersectionTest[] = lineIntersection(x1,startingPosition, x2,endingPosition,   
												creaturesInterval.getX1(entity, 1),creaturesInterval.getY1(entity, 1), creaturesInterval.getX2(entity, 1),creaturesInterval.getY2(entity, 1));
										if (intersectionTest != null) {
											if ( (intersectionTest[1] > closestWallTerrainIntersectionY || Math.abs(intersectionTest[1] - closestWallTerrainIntersectionY) <= landingPrecision) 
											  && (footY > intersectionTest[1] || Math.abs(footY - intersectionTest[1]) <= landingPrecision)) {
													closestWallTerrainIntersectionX = intersectionTest[0];
													closestWallTerrainIntersectionY = intersectionTest[1];
											}
											
										}
									}
								}
							}
							
							
								
						}
					}
				}
					
				
		
				
				
				
				// For testing
				if (creaturesInterval == null) {
					System.out.println("jumpOrFall: creaturesInterval == null");
				}

				
				// IF FINAL ENTITIES INTERVAL IS A WALL, UPDATE APPROPRIATE END POINTS TO WALL/TERRAIN INTERSECTION VALUES
				if (creaturesInterval != null) {
					if (creaturesInterval.getType() == 2) {
						System.out.println(" JumpOrFall: creaturesInterval is a WALL");
						// Endpoint values only need to be set here, when within a wall, because that's the only situation in which horizontal movement happens within the falling section
						if (creaturesInterval.getPlatformAngle(1) > 0.0) {
							x1Value = closestWallTerrainIntersectionX;
							startingPositionValue = closestWallTerrainIntersectionY;
							x2Value = creaturesInterval.getX2(entity,1);
							endingPositionValue = creaturesInterval.getY2(entity,1);
							
						} else {
							x1Value = creaturesInterval.getX1(entity,1);
							startingPositionValue = creaturesInterval.getY1(entity,1);
							x2Value = closestWallTerrainIntersectionX;
							endingPositionValue = closestWallTerrainIntersectionY;
						}
					}
				}
								
				
				// CONFIGURE ENTITY STATE BASED ON INTERVAL LOCATION AND CURRENT ENTITY STATUS
				// SET FALL STATUS AND ALONG WITH IT RESET FALL VELOCITY, SET ONPLATFORM, ONWALL
				// DETECTS TRANSITION (BETWEEN FALLING AND NOT FALLING, & VICE VERSA) STATES
				// NOTE: ENTITY.JUMPING WILL NOT BE TRUE IF THE FALLING SECTION IS EVER TAKEN SO IT REALLY DOESNT NEED TO BE CHECKED FOR HERE
				if (creaturesInterval == null && !entity.getFalling()) {
						entity.setFalling(true);
						entity.setFallVelocity(entity.getInitialFallVelocity());
				}
				else if (creaturesInterval != null) {
						double creaturesIntervalLandingPosition = creaturesInterval.getLandingPositionFromSpecificPosition(entity, footX, intervalMode);
						if (Math.abs(footY - creaturesIntervalLandingPosition) <= landingPrecision) {
							if (creaturesInterval.getType() == 1) {
									onPlatform = true;   // landed on a terrain, ENTITY-->NOT_FALLING
									onWall = false;
									entity.setFalling(false);
									entity.setSlidingOnWall(false);
							} else if (creaturesInterval.getType() == 2) {
									onPlatform = false;   // landed on a sloped wall, ENTITY-->FALLING
									onWall = true;
									if ( !entity.getSlidingOnWall() ) {  
										entity.setFallVelocity(0.35*entity.getFallVelocity());   // If: not on sloped wall --> on sloped wall, reduce and adjust fall velocity
									}
								
									entity.setEntitiesSlopedWallAngle(creaturesInterval.getPlatformAngle(intervalMode));
									entity.setFalling(true);
									entity.setSlidingOnWall(true);
							}
						} 
						else {
							if (creaturesInterval.getType() == 1) {
								onPlatform = false;  // ENTITY-->FALLING in a terrain interval
								onWall = false;
								entity.setFalling(true);
							} else if (creaturesInterval.getType() == 2) {
								onPlatform = false;   // ENTITY-->FALLING in a sloped wall interval in AIR
								onWall = false;
								entity.setFalling(true);
							}
							entity.setSlidingOnWall(false);
						}						
				}
				
		System.out.println("JumpOrFall: After analysis, onPlatform = "+onPlatform+" and onWall = "+onWall);
		if (creaturesInterval != null) {
			System.out.println("JumpOrFall: Before any motion, footX = "+footX);
			System.out.println("JumpOrFall: Before any motion, x1 = "+creaturesInterval.getX1(entity, intervalMode)+", x2 ="+creaturesInterval.getX2(entity, intervalMode));
			System.out.println("JumpOrFall: Before any motion, footY = "+ footY+" and lP = "+(creaturesInterval.getLandingPositionFromSpecificPosition(entity, footX, intervalMode)));
		}
				
				//System.out.println("JUMPORFALL: FALLING: ONWALL = "+onWall);
				//System.out.println("JUMPORFALL: FALLING: ONPLATFORM = "+onPlatform);
				/****************************
				 *  COMPUTE AMOUNT TO FALL  * 
				 ****************************/
				if (firstWhileLoopIteration) {
						double dt = 0.0167;
						double slopeScalar = 1.0;
						double friction = 0.0;
						double velocity = 0.0;
						if (onWall) {
							// WARNING: friction can create a negative ammountToFall, which can cause falling through the world
								double minWallAcceleration = 2.0;
								double minWallVelocity = 2.0;
								double maxWallVelocity = 20.0;
								slopeScalar = Math.abs(Math.sin(creaturesInterval.getPlatformAngle(intervalMode)));
								friction = 0.40*gravityAcceleration;
								velocity = Math.max(entity.getFallVelocity() + Math.max(slopeScalar*gravityAcceleration*dt - friction*dt,minWallAcceleration*dt),   minWallVelocity);
								velocity = Math.abs(Math.sin(creaturesInterval.getPlatformAngle(intervalMode))) * Math.min(velocity, maxWallVelocity);
								//velocity = 6.0;
						}
						else {
								velocity = entity.getFallVelocity() + gravityAcceleration*dt;
						}
						
						if (entity.getFalling()) {
							entity.setFallVelocity(velocity);
							amountToFall = setZeroIfThreshold(0.5*(entity.getFallVelocity())*dt,landingPrecision);
						}
						firstWhileLoopIteration = false;
				}
				/********
				 * MOVE * 
				 *******/
				// THESE ARE THE ONLY TWO CONDITIONS FOR WHICH THE FALLING METHOD IS FINISHED RUNNING
				if (onPlatform || amountToFall == 0.0) {
						doneFalling = true;  // THIS IS THE ONLY PLACE DONEFALLING SHOULD BE SET TO TRUE WITHIN THE LOOP
						continue;
				}
				else if (!onWall) {
					System.out.println("!ONWALL SECTION");
					lastIntervalTraversed = null;
						if (creaturesInterval == null) {
							dxToMove = dxToMove;
							dyToMove = dyToMove - amountToFall;
							amountToFall = 0.0;
							continue;
						}
						else {
							double landingPosition = creaturesInterval.getLandingPositionFromSpecificPosition(entity, footX, intervalMode);
							dxToMove = dxToMove;
							dyToMove = dyToMove - Math.min(Math.abs(footY-landingPosition), amountToFall);
							if (creaturesInterval.getType() == 1) {
								amountToFall = 0.0;
							} else {
								amountToFall = setZeroIfThreshold(amountToFall - Math.min(Math.abs(footY-landingPosition), amountToFall),landingPrecision);
							}
							continue;
						}
				}
				else if (onWall) {
						/****************************
						    SLOPED WALL NAVIGATION
						 ****************************/
					
					System.out.println("_______ SLOPED WALL SECTION BEGIN ________");
					System.out.println(" x1Value = "+x1Value+ " and x2Value = "+x2Value);
					System.out.println(" y1Value = "+startingPositionValue+ " and y2Value = "+endingPositionValue);
					
					System.out.println(" test: isAtX1: "+creaturesInterval.isPositionAtX1(footX, entity, intervalMode));
					
					System.out.println("footX = "+footX);
					System.out.println("x1 = "+creaturesInterval.getX1(entity, intervalMode));
					System.out.println("x == x1ToUse = "+(footX == creaturesInterval.getX1(entity, intervalMode)));
					//System.out.println("Math.abs(getLandingPositionFromSpecificPosition(entity, x, intervalMode) - y1ToUse ) <= landingPrecision = "+(Math.abs(getLandingPositionFromSpecificPosition(entity, x, intervalMode) - y1ToUse ) <= landingPrecision));
					
					System.out.println(" /// Left Neighbor");
					System.out.println(" //// x2 = "+creaturesInterval.getLeftInterval().getX2(entity, intervalMode));
					
						lastIntervalTraversed = creaturesInterval;
						exactMode = false;
						double wallAngle = creaturesInterval.getPlatformAngle(intervalMode);
						double wallMotionVector = wallAngle >= 0.0 ? -1.0 : 1.0;
						double relevantEndpointX = wallAngle > 0.0 ? x1Value : x2Value;
						double relevantEndpointY = wallAngle > 0.0 ? startingPositionValue : endingPositionValue;
						double wallDistanceToEndOfWallInterval = Math.sqrt( (footX - relevantEndpointX)*(footX - relevantEndpointX) + (footY - relevantEndpointY)*(footY - relevantEndpointY) );
						System.out.println(" wallAngle = "+wallAngle);
						System.out.println(" wallDTE = "+wallDistanceToEndOfWallInterval);
						
						dxToMove = dxToMove + wallMotionVector*Math.cos(wallAngle)*Math.min(amountToFall, wallDistanceToEndOfWallInterval);
						dyToMove = dyToMove - Math.abs(Math.sin(wallAngle))*Math.min(amountToFall, wallDistanceToEndOfWallInterval);
						System.out.println(" computed distanced moved = "+(Math.sqrt(dxToMove*dxToMove + dyToMove*dyToMove)));
						System.out.println(" new footY would be " + (entity.getY() - 0.5*entity.getHeight() + dyToMove));
						amountToFall = setZeroIfThreshold(amountToFall - Math.min(amountToFall, wallDistanceToEndOfWallInterval),landingPrecision);
						System.out.println("_______ SLOPED WALL SECTION END ________");
						continue;
				}

			}
			entity.setDX(dxToMove);
			entity.setDY(dyToMove);
			entity.move(3);	
		}
				
	}
	
	

	
	
	/*
	 * The method clampEntityToIntervalEndpoint adjust's entity's position to be completely at the endpoint, if entity
	 * is close enough to endpoint within a threshold (landingPrecision).
	 * This fixes rounding errors in which the player is "close enough" to be considered at an end point in one interval, but not considered at that endpoint
	 * in the corresponding neighboring interval of some other angle (slight difference in interpolated landing position because of different angles) 
	 */ 
	public void clampEntityToIntervalEndpoint(Entity entity) {
		
	}
	
	public void panCameraVertically(Camera camera, Entity entity) {
		
		double entityBefore = entity.getY();
		
		//System.out.println("Camera.isPanningUp and Camera.isPanningDown are " + camera.isPanningUp() + " and " + camera.isPanningDown());
		double verticalThresholdDistance = 4.0;
		
		if (camera.isPanningUp() || camera.isPanningDown()) {
			if (Math.abs(camera.getEyeY() - camera.getTargetVerticalPosition()) <= landingPrecision) {
				camera.setPanningUp(false);
				camera.setPanningDown(false);
			}
		}

		// Determine the vertical distance between the camera and entity
		
		double verticalDistanceBetweenCameraAndEntity = entity.getY() - camera.getEyeY();
		
		// Check to see if a vertical camera pan should be triggered
		
		if (verticalDistanceBetweenCameraAndEntity >= verticalThresholdDistance && !camera.isPanningUp()) {
			camera.setTargetVerticalPosition(entity.getY() + 2.0);
			camera.setPanningUp(true);
			camera.setPanningDown(false);
		}
		
		if (verticalDistanceBetweenCameraAndEntity <= -verticalThresholdDistance && !camera.isPanningDown()) {
			camera.setTargetVerticalPosition(entity.getY() - 3.0);
			camera.setPanningUp(false);
			camera.setPanningDown(true);
		}
	
		

		// Do the vertical camera panning
		
		if (camera.isPanningUp()) {
			double cameraDVertical = 0.04*(camera.getTargetVerticalPosition() - camera.getEyeY() ) + 0.009;
			
			if (camera.getEyeY() + cameraDVertical > camera.getTargetVerticalPosition() || Math.abs(camera.getEyeY() + cameraDVertical - camera.getTargetVerticalPosition()) <= landingPrecision) {
				camera.setEyeDX(0.0);
				camera.setEyeDY(camera.getTargetVerticalPosition() - camera.getEyeY());
				camera.setEyeDZ(0.0);
				camera.setVPointDX(0.0);
				camera.setVPointDY(camera.getTargetVerticalPosition() - camera.getEyeY());
				camera.setVPointDZ(0.0);
				
				camera.moveEye();
				camera.moveVPoint();
			}
			else {
				camera.setEyeDX(0.0);
				camera.setEyeDY(cameraDVertical);
				camera.setEyeDZ(0.0);
				camera.setVPointDX(0.0);
				camera.setVPointDY(cameraDVertical);
				camera.setVPointDZ(0.0);
				camera.moveEye();
				camera.moveVPoint();
			}
		}

		if (camera.isPanningDown()) {
			System.out.println("Camera Target is " + camera.getTargetVerticalPosition());
			System.out.println("Camera Eye Y is " + camera.getEyeY());
			double cameraDVertical = 0.04*(camera.getTargetVerticalPosition() - camera.getEyeY() ) - 0.009;
			System.out.println("CameraDVertical is " + cameraDVertical);
			
			if (camera.getEyeY() + cameraDVertical < camera.getTargetVerticalPosition() || Math.abs(camera.getEyeY() + cameraDVertical - camera.getTargetVerticalPosition()) <= landingPrecision) {
				camera.setEyeDX(0.0);
				camera.setEyeDY(camera.getTargetVerticalPosition() - camera.getEyeY());
				camera.setEyeDZ(0.0);
				camera.setVPointDX(0.0);
				camera.setVPointDY(camera.getTargetVerticalPosition() - camera.getEyeY());
				camera.setVPointDZ(0.0);
				
				camera.moveEye();
				camera.moveVPoint();
			}
			else {
				camera.setEyeDX(0.0);
				camera.setEyeDY(cameraDVertical);
				camera.setEyeDZ(0.0);
				camera.setVPointDX(0.0);
				camera.setVPointDY(cameraDVertical);
				camera.setVPointDZ(0.0);
				camera.moveEye();
				camera.moveVPoint();
			}

		}

}
	
	public ArrayList<Creature> getCreatures() {
		return creatures;
	}

	public void setCreatures(ArrayList<Creature> creatures) {
		this.creatures = creatures;
	}
	
	public double setZeroIfThreshold(double amountToMove, double threshold) {
		if (Math.abs(amountToMove) <= threshold) {
			return 0.0;
		}
		else {
			return amountToMove;
		}
	}
	
	/*****************************************
	  FOR DEBUGGING FALLING THROUGH THE WORLD
	 *****************************************/
	public boolean isBelowPlatform(Entity entity, double footX, double footY) {
		
		double belowPlatformDistanceThreshold = 0.1*entity.getHeight();
		double entityLP = 0.0;
		boolean landedWALL = false;
		boolean landedTERRAIN = false;
	
		double fellThruIntervalX1, fellThruIntervalX2, fellThruIntervalY1, fellThruIntervalY2, fellThruLP, fellThruAngle;
		Interval fellThruIntervalLeftNeighbor, fellThruIntervalRightNeighbor;
				
		// TERRAIN CHECK
		int hashCodes[] = currentWorld.getTerrain().genHashCodesFromKeysX(entity.getX() - 0.5*entity.getFeetWidth(),  entity.getX() + 0.5*entity.getFeetWidth());	

		for (int j = 0; j < hashCodes.length; j++) {
				
			int thisHashCode = hashCodes[j];
			ArrayList<Interval> bucketOfTerrain = currentWorld.getTerrain().getIntervalList(thisHashCode);
			if (bucketOfTerrain == null) {
				continue;
			}
			
			for (int p = 0; p < bucketOfTerrain.size(); p++) {
				
				// Check if this interval is a left and/or right cliff
				ArrayList<Integer> intervalModes = new ArrayList<Integer>(); // mode == 1: standard, mode == 2: leftCliff, mode == 3: rightCliff
				intervalModes.add(1);
				if (bucketOfTerrain.get(p).isLeftCliff()) {
					intervalModes.add(2);
				}
				if (bucketOfTerrain.get(p).isRightCliff()) {
					intervalModes.add(3);
				}
			
				for (int m = 0; m < intervalModes.size(); m++) {
					int anIntervalMode = intervalModes.get(m);
					Interval aTerrain = bucketOfTerrain.get(p);
					double x1 = aTerrain.getX1(entity, anIntervalMode );
					double x2 = aTerrain.getX2(entity, anIntervalMode );
					double sP = aTerrain.getY1(entity, anIntervalMode );
					double eP = aTerrain.getY2(entity, anIntervalMode );
					double lP = aTerrain.getLandingPositionFromSpecificPosition(entity, footX, anIntervalMode);
					double angle = aTerrain.getPlatformAngle(anIntervalMode);
					double footYToLPDistance = Math.abs(lP-footY);
					
					if ( ( (aTerrain.isPositionAtX1(footX,entity, anIntervalMode ) || footX > x1)   &&   (aTerrain.isPositionAtX2(footX,entity, anIntervalMode ) || footX < x2) ) 
						&& (!(Math.abs(footY-lP)<=landingPrecision) && footY < lP) 
						&& footYToLPDistance <= belowPlatformDistanceThreshold) {
						System.out.println("xx xx xx xx xx xx xx xx xx xx");
						System.out.println("isBelowPlatform: TERRAIN, footX = "+footX+", footY = "+footY+", lP = "+lP);
						System.out.println("isBelowPlatform: TERRAIN, x1 = "+x1+", x2 ="+x2);
						System.out.println("isBelowPlatform: TERRAIN, y1 = "+sP+", y2 ="+eP);
						System.out.println("isBelowPlatform: TERRAIN, angle = "+angle);
						System.out.println("isBelowPlatform: TERRAIN, intervalMode = "+ anIntervalMode);
						return true;
					}
					else if ( ( (aTerrain.isPositionAtX1(footX,entity, anIntervalMode) || footX > x1)   &&   (aTerrain.isPositionAtX2(footX,entity, anIntervalMode ) || footX < x2) ) 
							&& ((Math.abs(footY-lP)<=landingPrecision)) ) {
							landedTERRAIN = true;
							entityLP = lP;
	
					}
				}
			}
		}
		// SLOPED WALL CHECK
		hashCodes = currentWorld.getSlopedWalls().genHashCodesFromKeysX(entity.getX() - 0.5*entity.getFeetWidth(),  entity.getX() + 0.5*entity.getFeetWidth());	

		for (int j = 0; j < hashCodes.length; j++) {
							
			int thisHashCode = hashCodes[j];
			ArrayList<Interval> bucketOfWalls = currentWorld.getSlopedWalls().getIntervalList(thisHashCode);
			if (bucketOfWalls == null) {
				continue;
			}
			
			for (int p = 0; p < bucketOfWalls.size(); p++) {
				
					Interval aWall= bucketOfWalls.get(p);
					double x1 = aWall.getX1(entity, 1);
					double x2 = aWall.getX2(entity, 1);
					double sP = aWall.getY1(entity,1);
					double eP = aWall.getY2(entity,1);
					double lP = aWall.getLandingPositionFromSpecificPosition(entity, footX, 1);
					double angle = aWall.getPlatformAngle(1);
					double footYToLPDistance = Math.abs(lP-footY);
					
					if ( (    ( ( aWall.isPositionAtX1(footX,entity, 1) || footX > x1) ) &&							
							 ( (aWall.isPositionAtX2(footX,entity, 1)   || footX < x2) ) 
						&& (!(Math.abs(footY-lP)<=landingPrecision) && footY < lP) 
						&& footYToLPDistance <= belowPlatformDistanceThreshold) ) {
						
						if ( ( (aWall.isPositionAtX1(footX,entity, 1) && aWall.getLeftInterval().getType() != 2)
								||
								(aWall.isPositionAtX2(footX,entity, 1) && aWall.getRightInterval().getType() != 2) )
								&& !landedWALL
								) {
							  continue;
						}
						System.out.println("xx xx xx xx xx xx xx xx xx xx");
						System.out.println("isBelowPlatform: WALL, footX = "+footX+", footY = "+footY+", lP = "+lP);
						System.out.println("isBelowPlatform: WALL, x1 = "+x1+", x2 ="+x2);
						System.out.println("isBelowPlatform: WALL, y1 = "+sP+", y2 ="+eP);
						System.out.println("isBelowPlatform: WALL, angle = "+ angle);
						System.out.println("  isBelowPlatform: WALL LEFT NEIGHBOR, type = "+aWall.getLeftInterval().getType() +", angle = "+ aWall.getLeftInterval().getPlatformAngle(1));
						System.out.println("  isBelowPlatform: WALL LEFT NEIGHBOR, x1 = "+aWall.getLeftInterval().getX1(entity, 1)+", x2 = "+aWall.getLeftInterval().getX2(entity, 1));
						System.out.println("  isBelowPlatform: WALL LEFT NEIGHBOR, y1 = "+aWall.getLeftInterval().getY1(entity, 1)+", y2 = "+aWall.getLeftInterval().getY2(entity, 1));
						System.out.println("	isBelowPlatform: WALL RIGHT NEIGHBOR, type = "+aWall.getRightInterval().getType() +", angle = "+ aWall.getRightInterval().getPlatformAngle(1));
						System.out.println("	isBelowPlatform: WALL RIGHT NEIGHBOR, x1 = "+aWall.getRightInterval().getX1(entity, 1)+", x2 = "+aWall.getRightInterval().getX2(entity, 1));
						System.out.println("	isBelowPlatform: WALL RIGHT NEIGHBOR, y1 = "+aWall.getRightInterval().getY1(entity, 1)+", y2 = "+aWall.getRightInterval().getY2(entity, 1));
						System.out.println("isBelowPlatform: WALL, intervalMode = "+ 1);
						
						
						
						return true;
					}
					else if ( ( (aWall.isPositionAtX1(footX,entity, 1) || footX > x1)   &&   (aWall.isPositionAtX2(footX,entity, 1) || footX < x2) ) 
							&& ((Math.abs(footY-lP)<=landingPrecision)) ) {
							landedWALL = true;
							entityLP = lP;
	
					}
				
			}
		}
		
		
		
		if (landedTERRAIN) {
			//System.out.println("isBelowPlatform: landedTERRAIN = true and lP = "+entityLP+" and footY = "+footY);
		}
		if (landedWALL) {
			//System.out.println("isBelowPlatform: landedWALL = true and lP = "+entityLP+" and footY = "+footY);
		}
		return false;
		
	}
	
	public boolean isWithinBoundsInclusive(double pointX, double pointY,  double x1, double y1,  double x2, double y2) {
		if ( ( (pointX > Math.min(x1, x2) || Math.abs(pointX - Math.min(x1, x2)) <= landingPrecision)   &&   (pointX < Math.max(x1, x2)) || Math.abs(pointX - Math.max(x1, x2)) <= landingPrecision)  &&
			( (pointY > Math.min(y1, y2) || Math.abs(pointY - Math.min(y1, y2)) <= landingPrecision)   &&   (pointY < Math.max(y1, y2)) || Math.abs(pointY - Math.max(y1, y2)) <= landingPrecision)
			)
		{
			return true;
		}
		return false;
	}
	
	public boolean isWithinBoundsExclusive(double pointX, double pointY,  double x1, double y1,  double x2, double y2) {
		
		if ( (pointX > Math.min(x1, x2) || Math.abs(pointX - Math.min(x1, x2)) <= landingPrecision)  &&   (pointX < Math.max(x1, x2) || Math.abs(pointX - Math.max(x1, x2)) <= landingPrecision)  &&
			 (pointY > Math.min(y1, y2) || Math.abs(pointY - Math.min(y1, y2)) <= landingPrecision)   &&   (pointY < Math.max(y1, y2) || Math.abs(pointY - Math.max(y1, y2)) <= landingPrecision) 
			&& !(Math.abs(pointX - x1) <= landingPrecision && Math.abs(pointY - y1) <= landingPrecision)
			&& !(Math.abs(pointX - x2) <= landingPrecision && Math.abs(pointY - y2) <= landingPrecision)
			)
		{
			return true;
		}
		return false;
	}
	
}

