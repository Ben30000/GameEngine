import java.util.ArrayList;
import org.joml.Matrix4f;
import org.joml.Matrix4d;
import org.joml.Vector3f;
import org.joml.Vector3d;
import org.joml.Quaternionf;
import org.joml.Vector2d;

public class WorldUpdater {

	private ArrayList<Background> backgrounds;
	private ArrayList<Background> activeBackgrounds;
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
	boolean eventHappened = false;
	private double fallConstant1, fallConstant2;
	private double jumpConstant1, jumpConstant2;
	public Interval emptySpaceInterval;
	public Interval creaturesIntervalInWallRegion;

	
	
	public WorldUpdater(int initialScrollCounter, Player p, double landingPrecision) {
		backgrounds = new ArrayList<Background>();
		activeBackgrounds = new ArrayList<Background>();
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
		/*System.out.println("TESTANGLE IS "+ testAngle);
		System.out.println("TESTRESULT IS "+ testResult);
		System.out.println("TESTRESULTMETHOD2 IS "+ testResultMethod2);
		System.out.println("  TESTMOVEPOSX = "+ testMovePositionX);
		System.out.println("  TESTMOVEPOSY = "+ testMovePositionY);*/
		
		activeBackgrounds = backgrounds;

		
		

		for (Background b : getBackgrounds()) {
			b.setPreviousX(b.getX());
			b.setPreviousY(b.getY());
		}

		player.setPreviousX(player.getX());
		player.setPreviousY(player.getY());
		busy = true;
	
		
		// Camera Work
		
		mainCamera.panHorizontally(player);
		//this.panCameraVertically(mainCamera, player);

		double pYB42 = player.getY() - 0.5 * player.getHeight();
		double bgYB42 = activeBackgrounds.get(0).getY();
	

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
		this.jumpOrFall(player);
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
		
		this.navigate(player, motionDirectionPlayer, amntToMove, 1);
		
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

	// returns amountToMoveRemaining
	public double checkForCeilingsAndWallsThenMove(Entity entity, double totalAmountToMove, double intervalDistanceToMove, double intervalDXToMoveInterval, double intervalDYToMoveInterval, Interval entitiesInterval, int entitiesIntervalMode, Vector2d motionVector) {

		boolean leftwardMovement = false, rightwardMovement = false;
		
		if (motionVector.x < 0.0) {
			leftwardMovement = true;
		}
		else if (motionVector.x > 0.0) {
			rightwardMovement = true;
		}
		
		
		// Check if a ceiling will stop movement while on a terrain platforrm, or if it will alter trajectory while in air
		if (isBelowPlatform(entity, entity.getX(), entity.getY() - 0.5*entity.getHeight())) {
			System.out.println("IS BELOW PLATFORM: BEFORE checkForCeiling");
			System.exit(0);
		}
		double[] deltasAndAmountToMove = this.checkForCeiling(entity, totalAmountToMove, Math.abs(intervalDistanceToMove), entitiesInterval, entitiesIntervalMode, motionVector);
		double footX = entity.getX(),   footY = entity.getY() - 0.5*entity.getHeight();
		double newFootX = entity.getX() + deltasAndAmountToMove[0],   newFootY = entity.getY() - 0.5*entity.getHeight() + deltasAndAmountToMove[1];
		double tenativeDX = deltasAndAmountToMove[0],   tenativeDY = deltasAndAmountToMove[1];
		double finalDX = tenativeDX, finalDY = tenativeDY;
		double finalAmountMoved = deltasAndAmountToMove[2];
		// Finally, Check if a sloped wall will intercept the final movement
		System.out.println("    initialFootX = "+footX+" initialFootY = "+footY);
		System.out.println("	newFootX = "+newFootX+" newFootY = "+newFootY);
		Double closestIntersectionDistance = Double.MAX_VALUE;
		
		
		for (int k = 0; k < activeBackgrounds.size(); k++) {
			for (int r = 0; r < activeBackgrounds.get(k).getSlopedWalls().size(); r++ ) {
				Interval aWall = activeBackgrounds.get(k).getSlopedWalls().get(r);
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
									finalAmountMoved = Math.sqrt(finalDX*finalDX + finalDY*finalDY);
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
										finalAmountMoved = Math.sqrt(finalDX*finalDX + finalDY*finalDY);												
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
		
		if (isBelowPlatform(entity, entity.getX(), entity.getY() - 0.5*entity.getHeight())) {
			System.out.println("IS BELOW PLATFORM: AFTER checkForCeiling and wall tests");
			System.exit(0);
		}
		return finalAmountMoved;


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

	public ArrayList<Background> getBackgrounds() {
		return backgrounds;
	}

	public void setBackgrounds(ArrayList<Background> b) {
		this.backgrounds = b;
	}

	public ArrayList<Background> getActiveBackgrounds() {
		return activeBackgrounds;
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
		System.out.println("-----------------------navigate BEGIN-------------------------------");
		
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


		while (amountToMove > 0.0) {
			System.out.println("  NAVIGATE WHILE LOOP START: amountToMove = "+amountToMove);
			double footX = entity.getX();
			double footY = entity.getY() - 0.5*entity.getHeight();
			System.out.println("NAVIGATE: footX = "+footX+ " footY = "+footY);
			Interval creaturesInterval = null;
			Interval closestInterval = null;
			int intervalMode = 1;
			int closestIntervalMode = 1;
					
			for (int a = 0; a < activeBackgrounds.size(); a++) {
				for (int v = 0; v < activeBackgrounds.get(a).getIntervals().size(); v++) {
					
					// Check if this interval is a left and/or right cliff
					ArrayList<Integer> intervalModes = new ArrayList<Integer>(); // mode == 1: standard, mode == 2: leftCliff, mode == 3: rightCliff
					intervalModes.add(1);
					if (activeBackgrounds.get(a).getIntervals().get(v).isLeftCliff()) {
						intervalModes.add(2);
					}
					if (activeBackgrounds.get(a).getIntervals().get(v).isRightCliff()) {
						intervalModes.add(3);
					}
				
					
					
					for (int m = 1; m <= intervalModes.size(); m++) {
						
						Interval anInterval = activeBackgrounds.get(a).getIntervals().get(v);
						int anIntervalMode = intervalModes.get(m - 1);
						double x1 = anInterval.getX1(entity, anIntervalMode),   x2 = anInterval.getX2(entity, anIntervalMode);
						double startingPosition = anInterval.getY1(entity, anIntervalMode),   endingPosition = anInterval.getY2(entity, anIntervalMode);
						double landingPosition = anInterval.getLandingPosition(entity, anIntervalMode);
						double angle = anInterval.getPlatformAngle(anIntervalMode);
						
						if ( 
						(rightwardMovement && (footX > x1 ||  anInterval.isPositionAtX1(footX,entity, anIntervalMode)) && (footX < x2) && !(anInterval.isPositionAtX2(footX,entity,anIntervalMode)) )
						||
						 (leftwardMovement && (footX > x1) && (footX < x2 ||  anInterval.isPositionAtX2(footX,entity,anIntervalMode)) && !(anInterval.isPositionAtX1(footX,entity,anIntervalMode)) )      
						 ) {
							System.out.println("NAVIGATE: WITHIN BOUNDS OF A TERRAIN");
								if (creaturesInterval == null) {
		
									if (footY > landingPosition || Math.abs(footY - landingPosition) <= landingPrecision) {
												creaturesInterval = anInterval;
												intervalMode = anIntervalMode;
									}
									
								}
								else {
									
									
									if ((footY > landingPosition || Math.abs(footY - landingPosition) <= landingPrecision)
										&& (Math.abs(landingPosition - creaturesInterval.getLandingPosition(entity,intervalMode)) <= landingPrecision)
										&& ( (leftwardMovement && angle <= creaturesInterval.getPlatformAngle(intervalMode)) ||
												(rightwardMovement && angle >= creaturesInterval.getPlatformAngle(intervalMode))  )
										) {
										// This case is for terrain that intersects other terrain, when feet are at the intersection point
												System.out.println("TERRAIN-TERRAIN INTERSECTION!");
										//		System.exit(0);
												creaturesInterval = anInterval;
												intervalMode = anIntervalMode;
									}
									else if ((footY > landingPosition || Math.abs(footY - landingPosition) <= landingPrecision)
											&& (landingPosition > creaturesInterval.getLandingPosition(entity,intervalMode))) {
										// Otherwise, just pick the terrain with landing position closest to feet
										creaturesInterval = anInterval;
										intervalMode = anIntervalMode;
									}
										
										
								}
						}
						else {
			
								if (closestInterval == null) {
		
									if ( ( rightwardMovement && (footY > startingPosition  || Math.abs(footY - startingPosition) <= landingPrecision)
										&& (x1 > footX || Math.abs(x1 - footX) <= landingPrecision) )
											||
										( leftwardMovement && (footY > endingPosition  || Math.abs(footY - endingPosition) <= landingPrecision)
										&& (x2 < footX || Math.abs(x2 - footX) <= landingPrecision) )	) {
												closestInterval = anInterval;
												closestIntervalMode = anIntervalMode;
									}
		
								}
								else {
		
									if ( ( rightwardMovement && ( ( (footY > startingPosition || Math.abs(footY - startingPosition) <= landingPrecision)
											&& (x1 > footX || Math.abs(x1 - footX) <= landingPrecision) && (x1 < closestInterval.getX1(entity,closestIntervalMode)) )
											||
											( (footY > startingPosition || Math.abs(footY - startingPosition) <= landingPrecision) && (x1 > footX || Math.abs(x1 - footX) <= landingPrecision)
											&& (Math.abs(x1 - closestInterval.getX1(entity,closestIntervalMode)) <= landingPrecision) && (startingPosition > closestInterval.getY1(entity, closestIntervalMode))  )  ) ) 
										||
											(leftwardMovement && ( ( (footY > endingPosition || Math.abs(footY - endingPosition) <= landingPrecision)
											&& (x2 < footX || Math.abs(x2 - footX) <= landingPrecision) && (x2 > closestInterval.getX2(entity,closestIntervalMode)) )
											||
											( (footY > endingPosition || Math.abs(footY - endingPosition) <= landingPrecision) && (x2 < footX || Math.abs(x2 - footX) <= landingPrecision)
											&& (Math.abs(x2 - closestInterval.getX2(entity,closestIntervalMode)) <= landingPrecision) && (endingPosition > closestInterval.getY2(entity, closestIntervalMode))  )  ) )
											) {
												closestInterval = anInterval;
												closestIntervalMode = anIntervalMode;
									}
								}
									
						}
				
					}
				}
			}

			
			double x1Value = 0.0, x2Value = 0.0, startingPosition = 0.0, endingPosition = 0.0, landingPosition = 0.0, angle = 0.0;
			boolean onPlatform = false, hasLandingPosition = false;
			
			activeBackgrounds.get(0).setRecentInterval(null);
			
			/*
			 *  See if another terrain intersects this interval before the relevant end point
			 */
			double closestIntersectionDistance = Double.MAX_VALUE;
			double closestIntersectionX = 0.0, closestIntersectionY = 0.0;
			if (creaturesInterval != null) {
				
				for (int a = 0; a < activeBackgrounds.size(); a++) {
					for (int v = 0; v < activeBackgrounds.get(a).getIntervals().size(); v++) {
					
						// Check if this interval is a left and/or right cliff
						ArrayList<Integer> intervalModes = new ArrayList<Integer>(); // mode == 1: standard, mode == 2: leftCliff, mode == 3: rightCliff
						intervalModes.add(1);
						if (activeBackgrounds.get(a).getIntervals().get(v).isLeftCliff()) {
							intervalModes.add(2);
						}
						if (activeBackgrounds.get(a).getIntervals().get(v).isRightCliff()) {
							intervalModes.add(3);
						}
						
						for (int m = 0; m < intervalModes.size(); m++) {
							Interval anInterval = activeBackgrounds.get(a).getIntervals().get(v);
							int anIntervalMode = intervalModes.get(m);
							double x1 = anInterval.getX1(entity, anIntervalMode),   x2 = anInterval.getX2(entity, anIntervalMode);
							double y1 = anInterval.getY1(entity, anIntervalMode),   y2 = anInterval.getY2(entity, anIntervalMode);
							double creaturesIntervalX1 = creaturesInterval.getX1(entity, intervalMode), creaturesIntervalX2 = creaturesInterval.getX2(entity, intervalMode);
							double creaturesIntervalY1 = creaturesInterval.getY1(entity, intervalMode), creaturesIntervalY2 = creaturesInterval.getY2(entity, intervalMode);
							double landingPositionAtFootX = creaturesInterval.getLandingPositionFromSpecificPosition(entity, footX, intervalMode);
							
							double[] terrainTerrainIntersectionResult = lineIntersection(x1,y1,  x2,y2,    creaturesIntervalX1,creaturesIntervalY1,  creaturesIntervalX2,creaturesIntervalY2);
							if (terrainTerrainIntersectionResult != null) {
							//	System.out.println("~ Terrain-terrain intersection ~");
							//	System.out.println("(x1, y1) = "+x1+", "+y1+" and (x2,y2) = "+x2+", "+ y2);
							//	System.out.println("(creaturesX1, creaturesY1) = "+creaturesIntervalX1+", "+creaturesIntervalY1+" and (creaturesX2,creaturesY2) = "+creaturesIntervalX2+", "+ creaturesIntervalY2);
							//	System.out.println("Entity bounding point: (x,y) = "+footX+", "+landingPositionAtFootX);
						//		System.out.println("rightwardMovement bounding point: (x,y) = "+creaturesIntervalX2+", "+creaturesIntervalY2);
					//			System.out.println("intersectionPoint: (x,y) = "+terrainTerrainIntersectionResult[0]+", "+terrainTerrainIntersectionResult[1]);
								if ((leftwardMovement && isWithinBoundsExclusive(terrainTerrainIntersectionResult[0],terrainTerrainIntersectionResult[1], creaturesIntervalX1, creaturesIntervalY1, footX, landingPositionAtFootX))
									||
									(rightwardMovement && isWithinBoundsExclusive(terrainTerrainIntersectionResult[0],terrainTerrainIntersectionResult[1], footX, landingPositionAtFootX, creaturesIntervalX2, creaturesIntervalY2))
								) {
									System.out.println("It intercepts terrain!");
									double intersectionDistance = Math.sqrt((terrainTerrainIntersectionResult[0] - footX)*(terrainTerrainIntersectionResult[0] - footX) +
											(terrainTerrainIntersectionResult[1] - landingPositionAtFootX)*(terrainTerrainIntersectionResult[1] - landingPositionAtFootX) );
									if (intersectionDistance < closestIntersectionDistance) {
										closestIntersectionDistance = intersectionDistance;
										closestIntersectionX = terrainTerrainIntersectionResult[0];
										closestIntersectionY = terrainTerrainIntersectionResult[1];											
									}
									
								}
							}
				
						}
					
					}
				}
				
			}
			
			
			
			if (creaturesInterval != null && closestInterval != null) {
				System.out.println("NAVIGATE: creaturesInterval != null && closestInterval != null");
				hasLandingPosition = true;
				activeBackgrounds.get(0).setRecentInterval(creaturesInterval);

				startingPosition = creaturesInterval.getY1(entity, intervalMode);
				endingPosition = creaturesInterval.getY2(entity, intervalMode);
				
				if (leftwardMovement && (creaturesInterval.getX1(entity,intervalMode) < closestInterval.getX2(entity,closestIntervalMode) || Math.abs(creaturesInterval.getX1(entity,intervalMode) - closestInterval.getX2(entity, closestIntervalMode)) <= landingPrecision) ) {
					x1Value = closestInterval.getX2(entity, closestIntervalMode);
					startingPosition = creaturesInterval.getLandingPositionFromSpecificPosition(entity, closestInterval.getX2(entity, closestIntervalMode), intervalMode);
				} else {
					x1Value = creaturesInterval.getX1(entity, intervalMode);
				}
				if (rightwardMovement && (creaturesInterval.getX2(entity, intervalMode) > closestInterval.getX1(entity, closestIntervalMode) || Math.abs(creaturesInterval.getX2(entity, intervalMode) - closestInterval.getX1(entity, closestIntervalMode)) <= landingPrecision) ) {
					x2Value = closestInterval.getX1(entity, closestIntervalMode);
					endingPosition = creaturesInterval.getLandingPositionFromSpecificPosition(entity, closestInterval.getX1(entity, closestIntervalMode), intervalMode);
				} else {
					x2Value = creaturesInterval.getX2(entity, intervalMode);
				}
				
				angle = creaturesInterval.getPlatformAngle(intervalMode);
				onPlatform = false;

				landingPosition = creaturesInterval.getLandingPosition(entity, intervalMode);
				if (Math.abs((footY - landingPosition)) <= landingPrecision && !entity.getJumping()) {		
						entity.setFalling(false);
						onPlatform = true;
						//entity.setY(landingPosition + 0.5*entity.getHeight());						
				}
				else if (!entity.getJumping() && !entity.getFalling()) {
					
					//entity.setFalling(true);
				}

			}
			else if (creaturesInterval != null && closestInterval == null) {
				System.out.println("NAVIGATE: creaturesInterval != null && closestInterval == null");
				hasLandingPosition = true;
				activeBackgrounds.get(0).setRecentInterval(creaturesInterval);
				x1Value = creaturesInterval.getX1(entity, intervalMode);
				x2Value = creaturesInterval.getX2(entity, intervalMode);
				angle = creaturesInterval.getPlatformAngle(intervalMode);
				onPlatform = false;

				landingPosition = creaturesInterval.getLandingPosition(entity, intervalMode);
				startingPosition = creaturesInterval.getY1(entity, intervalMode);
				endingPosition = creaturesInterval.getY2(entity, intervalMode);

				if (Math.abs((footY - landingPosition)) <= landingPrecision && !entity.getJumping()) {
						onPlatform = true;
						entity.setFalling(false);
						//entity.setY(landingPosition + 0.5*entity.getHeight());
				}
				else if (!entity.getJumping() && !entity.getFalling()) {

				}

			}
			else if (creaturesInterval == null && closestInterval != null){
				System.out.println("NAVIGATE: creaturesInterval == null && closestInterval != null");
				hasLandingPosition = false;
				
				if (leftwardMovement) {
					x1Value = closestInterval.getX2(entity, closestIntervalMode);
					x2Value = 10.0;
				} else if (rightwardMovement){
					x1Value = 10.0;
					x2Value = closestInterval.getX1(entity, closestIntervalMode);
				}
				angle = 0.0;
				onPlatform = false;
			}
			
			else if (creaturesInterval == null && closestInterval == null) {
				System.out.println("NAVIGATE: creaturesInterval == null && closestInterval == null");
				hasLandingPosition = false;
				if (leftwardMovement) {
					x1Value = footX - 1000.0;
					x2Value = 10.0;					
				} else if (rightwardMovement) {
					x1Value = 10.0;
					x2Value = footX + 1000.0;	
				}
				angle = 0.0;
				onPlatform = false;
			}
			
			/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
			 * FINALLY, UPDATE LEFT OR RIGHT ENDPOINT IF A TERRAIN INTERSECTS BEFORE ENDPOINT  *
			 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
			if (closestIntersectionDistance <= 10.0) {
		//		System.out.println("closestIntersectionDistance <= 10.0");
		//		System.exit(0);
			}
			if (creaturesInterval != null) {
				double landingPositionAtFootX = creaturesInterval.getLandingPositionFromSpecificPosition(entity, footX, intervalMode);
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
			
		//	System.out.println("NAVIGATE: onPlatform is "+onPlatform);
		//	System.out.println("NAVIGATE: x1Value, x2Value: "+x1Value+", "+x2Value);

			
			double intervalEndpointX = 0.0;
			double intervalEndpointY = 0.0;
			
			if (rightwardMovement) {
				intervalEndpointX = x2Value;
				intervalEndpointY = endingPosition;
			} else if (leftwardMovement) {
				intervalEndpointX = x1Value;
				intervalEndpointY = startingPosition;
			}
	
			if (hasLandingPosition && !onPlatform) {
				
					double airDistanceToEndOfInterval = Math.abs(intervalEndpointX - footX);
					/*
					 * * * * * * * * * * * * * * * * * * * *
					 * Check if horizontal movement in air * 
					 *      will intersect a platform      *
					 * * * * * * * * * * * * * * * * * * * *
					 */
					if (startingPosition + (footX + motionVectorX*Math.min(airDistanceToEndOfInterval, amountToMove) - x1Value)*Math.tan(angle) > footY) {
						// Don't need to multiply by motionVector to account for direction here
						System.out.println("NAVIGATE: L CASE 1");
						double l = x1Value + (footY - startingPosition)/Math.tan(angle) - footX;
						//System.out.println("NAVIGATE: L CASE 1, l = "+l);
						//System.out.println("NAVIGATE SECTION: l CASE 1 with l = "+l);
							requestedDistanceToMove = requestedDistanceToMove + Math.abs(l); 
							double newAmountToMove = checkForCeilingsAndWallsThenMove(entity, amountToMove, l, l, 0.0, creaturesInterval, intervalMode, motionVector);
							amountToMove = setZeroIfThreshold(newAmountToMove,landingPrecision);

					    if (entity.getJumping()) {
							amountToMove = 0.0;
							continue;
						}

					    continue;
					}
					else {
						System.out.println("NAVIGATE: L CASE 2");
						double l = motionVectorX*Math.min(airDistanceToEndOfInterval, amountToMove);
						//System.out.println("NAVIGATE SECTION: l CASE 2 with l = "+l);
						//System.out.println("NAVIGATE: L CASE 2, l = "+l);
						requestedDistanceToMove = requestedDistanceToMove + Math.abs(l);
						double newAmountToMove = checkForCeilingsAndWallsThenMove(entity, amountToMove, l, l, 0.0, creaturesInterval, intervalMode, motionVector);
						amountToMove = setZeroIfThreshold(newAmountToMove,landingPrecision);
						continue;
					    
					}
					
			}
			
			System.out.println("NAVIGATE: NOT L CASE 1 OR 2");
			double platformDistanceToEndOfInterval = 0.0;
			if (creaturesInterval != null) {
				platformDistanceToEndOfInterval = Math.sqrt( (intervalEndpointX - footX)*(intervalEndpointX - footX) + 
															(intervalEndpointY - landingPosition)*(intervalEndpointY - landingPosition)
															);
			}
			else {
				platformDistanceToEndOfInterval = Math.abs(intervalEndpointX - footX);
			}
			/*
			 * * * * * * * * * * * * * * * * * * * *
			 * Movement on platform, or in air but * 
			 *       not within an interval        *
			 * * * * * * * * * * * * * * * * * * * *
			 */
			
			requestedDistanceToMove = requestedDistanceToMove + Math.abs(Math.min(platformDistanceToEndOfInterval,amountToMove));
			double newAmountToMove = checkForCeilingsAndWallsThenMove(entity, amountToMove, Math.min(platformDistanceToEndOfInterval,amountToMove),
										motionVectorX*Math.abs(Math.cos(angle)*Math.min(platformDistanceToEndOfInterval,amountToMove)),
										motionVectorX*Math.sin(angle)*Math.min(platformDistanceToEndOfInterval,amountToMove),
					creaturesInterval, intervalMode, motionVector);
			amountToMove = setZeroIfThreshold(newAmountToMove,landingPrecision);
			continue;
			
			
			/*if (creaturesInterval != null) {
				entityDepth = creaturesInterval.getZ2()
						* ((entity.getX() - creaturesInterval.getX1(0.5 * entity.getFeetWidth()))
								/ (creaturesInterval.getX2(0.5 * entity.getFeetWidth())
										- creaturesInterval.getX1(0.5 * entity.getFeetWidth())))
						+ creaturesInterval.getZ1()
								* (1 - ((entity.getX() - creaturesInterval.getX1(0.5 * entity.getFeetWidth()))
										/ (creaturesInterval.getX2(0.5 * entity.getFeetWidth())
												- creaturesInterval.getX1(0.5 * entity.getFeetWidth())))); }
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
		System.out.println("-----------------------navigate END-------------------------------");

		
	}
	
	// checkForCeiling paces an entity within an interval through ceiling intervals, if any
	// returns (dxToMove, dyToMove, amountToMove) where amountToMove is the new total amountToMove remaining
	public double[] checkForCeiling(Entity entity, double totalAmountToMove, double moveAmount, Interval entitiesInterval, int entitiesIntervalMode, Vector2d motionVector) {

		System.out.println("^^^^^^^^^^^^^^^^^^^^^checkForCeiling BEGIN^^^^^^^^^^^^^^^^^^^^^");
		
		
		double amountToMove = totalAmountToMove;
		double amountToMoveCeiling = moveAmount;
		System.out.println("amountToMoveCeiling = "+amountToMoveCeiling);
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
		
		double entitiesIntervalAngleSign, entitiesIntervalAngle;
		  if (entitiesInterval != null) {	
			if (entitiesInterval.getPlatformAngle(entitiesIntervalMode) > 0.0
					|| Math.abs(entitiesInterval.getPlatformAngle(entitiesIntervalMode)) <= 0.00000000001) {
				entitiesIntervalAngleSign = 1.0;
			} else {
				entitiesIntervalAngleSign = -1.0;
			}
			
			entitiesIntervalAngle = entitiesInterval.getPlatformAngle(entitiesIntervalMode);
		  }
		  else {
			  entitiesIntervalAngleSign = 1.0;
			  entitiesIntervalAngle = 0.0;
		  }
		
		
		while (amountToMoveCeiling > 0.0) {

			double footX = entity.getX() + dxToMove;
			double footY = entity.getY() - 0.5*entity.getHeight() + dyToMove;
			double headY = entity.getY() + 0.5*entity.getHeight() + dyToMove;
			// Need to check onPlatform status every iteration of the while loop here because
			// ceiling can change trajectory while in air and cause a platform intersection
			boolean onPlatform = false;
			   if (entitiesInterval != null) {
				if (Math.abs(footY - entitiesInterval.getLandingPositionFromSpecificPosition(entity, footX, entitiesIntervalMode)) <= landingPrecision) {
							onPlatform = true;
				}
			   }
			
			Interval creaturesCeiling = null;
			int creaturesCeilingIntervalMode = 1;
			Interval closestCeiling = null;
			int closestCeilingIntervalMode = 1;
			
			for (int a = 0; a < activeBackgrounds.size(); a++) {
				for (int v = 0; v < activeBackgrounds.get(a).getCeilings().size(); v++) {
					
					ArrayList<Integer> intervalModes = new ArrayList<Integer>();
					intervalModes.add(1);
					if (activeBackgrounds.get(a).getCeilings().get(v).isLeftCliff()) {
						intervalModes.add(2);
					}
					if (activeBackgrounds.get(a).getCeilings().get(v).isLeftCliff()) {
						intervalModes.add(3);
					}
				
					for (int m = 0; m < intervalModes.size(); m++) {
						Interval aCeiling = activeBackgrounds.get(a).getCeilings().get(v);
						int aCeilingIntervalMode = intervalModes.get(m);
						double x1 = aCeiling.getX1(entity, aCeilingIntervalMode),  x2 = aCeiling.getX2(entity, aCeilingIntervalMode);
						double startingCeilingPosition = aCeiling.getY1(entity, aCeilingIntervalMode),  endingCeilingPosition = aCeiling.getY2(entity, aCeilingIntervalMode);
						double angle = aCeiling.getPlatformAngle(aCeilingIntervalMode);
						// Have to compute ceilingPosition manually here because entity position is not actually updated until the while loop in navigate finishes
						double ceilingPosition = aCeiling.getLandingPositionFromSpecificPosition(entity, footX, aCeilingIntervalMode);
						
						
						// Previously used just the x coordinate of an interval to determine if entity was at interval endpoint, can't do this for nearly perpendicular intervals
						double distanceBetweenHeadAndX1EndPoint = Math.sqrt((footX-x1)*(footX-x1) + (headY-startingCeilingPosition)*(headY-startingCeilingPosition));
						double distanceBetweenHeadAndX2EndPoint = Math.sqrt((footX-x2)*(footX-x2) + (headY-endingCeilingPosition)*(headY-endingCeilingPosition));
	
						
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
									amountToMoveCeiling = 0.0;
									amountToMove = 0.0;
									return new double[] {dxToMove, dyToMove, amountToMove};
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
										relevantCeilingPosition = endingCeilingPosition;
									} else if (rightwardMovement) {
										relevantCeilingPosition = startingCeilingPosition;
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
														( Math.abs(x1 - closestCeiling.getX1(entity, closestCeilingIntervalMode)) <= landingPrecision && (x1 > footX || Math.abs(x1 - footX) <= landingPrecision) && startingCeilingPosition < closestCeiling.getY1(entity, closestCeilingIntervalMode)  )  )
														&& (Math.abs(angle) <= landingPrecision || angle < 0.0)) 
													||
														(leftwardMovement && ( ( (x2 < footX || Math.abs(x2 - footX) <= landingPrecision) && (x2 > closestCeiling.getX2(entity, closestCeilingIntervalMode)) )
														||
														( Math.abs(x2 - closestCeiling.getX2(entity, closestCeilingIntervalMode)) <= landingPrecision && (x2 < footX || Math.abs(x2 - footX) <= landingPrecision) && endingCeilingPosition < closestCeiling.getY2(entity, closestCeilingIntervalMode)  )  )
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

			
			double x1Value = 0.0, x2Value = 0.0, startingCeilingPosition = 0.0, endingCeilingPosition = 0.0, ceilingPosition = 0.0, angle = 0.0;
			boolean hasCeilingPosition = false, onCeiling = false;
			
			activeBackgrounds.get(0).setRecentInterval(null);
			
			if (creaturesCeiling != null && closestCeiling != null) {
				//System.out.println("	CEILING: creaturesCeiling != null && closestCeiling != null");
				hasCeilingPosition = true;
				activeBackgrounds.get(0).setRecentInterval(creaturesCeiling);


				startingCeilingPosition = creaturesCeiling.getY1(entity, creaturesCeilingIntervalMode);
				endingCeilingPosition = creaturesCeiling.getY2(entity, creaturesCeilingIntervalMode);
				
				if (leftwardMovement && (creaturesCeiling.getX1(entity, creaturesCeilingIntervalMode) < closestCeiling.getX2(entity, closestCeilingIntervalMode) || Math.abs(creaturesCeiling.getX1(entity, creaturesCeilingIntervalMode) - closestCeiling.getX2(entity, closestCeilingIntervalMode)) <= landingPrecision) ) {
					x1Value = closestCeiling.getX2(entity, closestCeilingIntervalMode);
					startingCeilingPosition = creaturesCeiling.getLandingPositionFromSpecificPosition(entity, closestCeiling.getX2(entity, closestCeilingIntervalMode), creaturesCeilingIntervalMode);
				} else {
					x1Value = creaturesCeiling.getX1(entity, creaturesCeilingIntervalMode);
				}
				if (rightwardMovement && (creaturesCeiling.getX2(entity, creaturesCeilingIntervalMode) > closestCeiling.getX1(entity, closestCeilingIntervalMode) || Math.abs(creaturesCeiling.getX2(entity, creaturesCeilingIntervalMode) - closestCeiling.getX1(entity, closestCeilingIntervalMode)) <= landingPrecision) ) {
					x2Value = closestCeiling.getX1(entity, closestCeilingIntervalMode);
					endingCeilingPosition = creaturesCeiling.getLandingPositionFromSpecificPosition(entity, closestCeiling.getX1(entity, closestCeilingIntervalMode), creaturesCeilingIntervalMode);
				} else { 
					x2Value = creaturesCeiling.getX2(entity, creaturesCeilingIntervalMode);
				}
				
				angle = creaturesCeiling.getPlatformAngle(creaturesCeilingIntervalMode);
				onCeiling = false;

				ceilingPosition = creaturesCeiling.getLandingPositionFromSpecificPosition(entity, footX, creaturesCeilingIntervalMode);
				//System.out.println("cP = "+ceilingPosition);
				// new LP calculation method
				//System.out.println("headY = "+headY);
				if (Math.abs((headY - ceilingPosition)) <= landingPrecision) {
					onCeiling = true;
				} else if (Math.abs((headY - ceilingPosition)) > landingPrecision) {
					onCeiling = false;
				}

			}
			else if (creaturesCeiling != null && closestCeiling == null) {
				//System.out.println("	CEILING: creaturesCeiling != null && closestCeiling == null");
				hasCeilingPosition = true;
				activeBackgrounds.get(0).setRecentInterval(creaturesCeiling);
				x1Value = creaturesCeiling.getX1(entity,creaturesCeilingIntervalMode);
				x2Value = creaturesCeiling.getX2(entity,creaturesCeilingIntervalMode);
				angle = creaturesCeiling.getPlatformAngle(creaturesCeilingIntervalMode);
				onCeiling = false;

				startingCeilingPosition = creaturesCeiling.getY1(entity, creaturesCeilingIntervalMode);
				endingCeilingPosition = creaturesCeiling.getY2(entity, creaturesCeilingIntervalMode);

				ceilingPosition = creaturesCeiling.getLandingPositionFromSpecificPosition(entity, footX, creaturesCeilingIntervalMode);
				
				if (Math.abs((headY - ceilingPosition)) <= landingPrecision) {
					onCeiling = true;
				} else if (Math.abs((headY - ceilingPosition)) > landingPrecision) {
					onCeiling = false;
				}
			}
			else if (creaturesCeiling == null && closestCeiling != null){
				//System.out.println("CEILING: creaturesCeiling == null && closestCeiling != null");
				hasCeilingPosition = false;
				
				if (leftwardMovement) {
					x1Value = closestCeiling.getX2(entity,closestCeilingIntervalMode);
					x2Value = 10.0;
				} else if (rightwardMovement){
					x1Value = 10.0;
					x2Value = closestCeiling.getX1(entity,closestCeilingIntervalMode);
				}
				angle = 0.0;
				onCeiling = false;
			}
			
			else if (creaturesCeiling == null && closestCeiling == null) {
				//System.out.println("CEILING: creaturesCeiling == null && closestCeiling == null");
				hasCeilingPosition = false;
				if (leftwardMovement) {
					x1Value = footX - 1000.0;
					x2Value = footX + 1000.0;					
				} else if (rightwardMovement) {
					x1Value = footX - 1000.0;
					x2Value = footX + 1000.0;	
				}
				angle = 0.0;
				onCeiling = false;
			}
			
	
			double ceilingIntervalEndpointX = 0.0;
			double ceilingIntervalEndpointY = 0.0;
			
			if (rightwardMovement) {
				ceilingIntervalEndpointX = x2Value;
				ceilingIntervalEndpointY = endingCeilingPosition;
			} else if (leftwardMovement) {
				ceilingIntervalEndpointX = x1Value;
				ceilingIntervalEndpointY = startingCeilingPosition;
			}
	
			
			if (onPlatform) {
				//System.out.println("^ ^ ^ CEILING: onPlatform");
				/*
				 * In this section, entity is onPlatform, 
				 * check if onPlatform motion will cause an intersection between entity head and a ceiling
				 * NOTE:
				 * Passed into this method is a baseline terrain DX (moveAmountDX) and baseline terrain DY (moveAmountDY)
				 * These values can shrink here because the entity must pace movement through CEILING intervals while moving on TERRAIN intervals
				 */
					// onPlatform == true, onCeiling == true OR false (head could intersect ceiling)
					// The terrain deltas can be modified because terrain movement must now be paced through ceiling intervals
					
					double newDXToMove = motionVectorX*Math.min(Math.abs(Math.cos(entitiesIntervalAngle))*amountToMoveCeiling, Math.abs(Math.cos(entitiesIntervalAngle))*Math.abs(ceilingIntervalEndpointX - footX));
					double newDYToMove = motionVectorX*entitiesIntervalAngleSign*Math.min(Math.abs(Math.sin(entitiesIntervalAngle))*amountToMoveCeiling, Math.abs(Math.sin(entitiesIntervalAngle))*Math.abs(ceilingIntervalEndpointX - footX));
					
					double intersectionTest[] = lineIntersection(footX, headY,   footX + newDXToMove, headY + newDYToMove,     x1Value, startingCeilingPosition,   x2Value, endingCeilingPosition);
					if (intersectionTest != null) {
						// ENTITY HEAD MOVEMENT WILL INTERSECT CEILING
						if (Math.abs(footX - intersectionTest[0]) <= landingPrecision) {
							// Entity is already at the ceiling wall
							dxToMove = dxToMove;
							dyToMove = dyToMove;
						}
						else {
							// Move entity the remaining distance to the ceiling wall
							dxToMove = dxToMove + intersectionTest[0] - footX;
							dyToMove = dyToMove + intersectionTest[1] - headY;
						}
						amountToMove = 0.0;
						amountToMoveCeiling = 0.0;
					} 
					else {
						// ENTITY HEAD MOVEMENT WILL NOT INTERSECT CEILING
						double modifiedAmountMoved = Math.sqrt(newDXToMove*newDXToMove + newDYToMove*newDYToMove);	
						dxToMove = dxToMove + newDXToMove;
						dyToMove = dyToMove + newDYToMove;
						amountToMove = amountToMove - modifiedAmountMoved;
						amountToMoveCeiling = setZeroIfThreshold(amountToMoveCeiling - modifiedAmountMoved,landingPrecision); 
						
					}
					
					continue;
				
			} 
			
			
			else if ( ! onPlatform) {
				if (hasCeilingPosition && !onCeiling) {
					//System.out.println("CEILING SECTION: hasCeilingPosition && !onCeiling");
					/*
					 * IN CEILING INTERVAL,
					 * IN AIR
					 * COULD BE OR COULD NOT BE IN TERRAIN INTERVAL
					 */
					// 	onPlatform == false and onCeiling == false
						double newDXToMove = motionVectorX*Math.min(amountToMoveCeiling, Math.abs(ceilingIntervalEndpointX - footX));
						/*
						 * * * * * * * * * * * * * * * * * * * *
						 * Check if horizontal movement in air * 
						 *      will intersect a ceiling       *
						 * * * * * * * * * * * * * * * * * * * *
						 */
						double[] lIntersectionResult = lineIntersection(footX, headY, footX+newDXToMove, headY,
								x1Value,startingCeilingPosition, x2Value, endingCeilingPosition);

						if (lIntersectionResult != null) {
							// CASE: HEAD IN AIR, PURELY HORIZONTAL MOVEMENT WILL CAUSE INTERSECTION WITH CEILING
							double lDX = lIntersectionResult[0] - footX;
							double lDY = lIntersectionResult[1] - headY;
							double lAmntToMove = Math.sqrt(lDX*lDX + lDY*lDY);
							System.out.println("lDX = "+lDX);
							amountToMoveCeiling = setZeroIfThreshold(amountToMoveCeiling - lAmntToMove, landingPrecision);
							amountToMove = amountToMove- lAmntToMove;
							dxToMove = dxToMove + lDX;
							dyToMove = dyToMove ;
							continue;					
						}
						else {
							// CASE: HEAD IN AIR, PURELY HORIZONTAL MOVEMENT WILL NOT CAUSE INTERSECTION WITH CEILING							
							double lDX = newDXToMove;
							dxToMove = dxToMove + lDX;
							dyToMove = dyToMove;
							amountToMoveCeiling = setZeroIfThreshold(amountToMoveCeiling - Math.abs(lDX),landingPrecision);
							amountToMove = amountToMove - Math.abs(lDX);
							continue;
						}
						
						
						
				}
				else if (hasCeilingPosition && onCeiling) {
						System.out.println("CEILING CASE: hasCeilingPosition && onCeiling");
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
						
						double newDXToMove = motionVectorX*Math.abs(Math.cos(angle))*Math.min(amountToMoveCeiling, ceilingDistanceToEndOfCeiling);
						double newDYToMove = motionVectorX*Math.sin(angle)*Math.min(amountToMoveCeiling, ceilingDistanceToEndOfCeiling);
						System.out.println("newDXToMove, newDYToMove = "+newDXToMove+", "+newDYToMove);
						if (entitiesInterval != null) {
								double intersectionTest[] = lineIntersection(footX, footY,   footX + newDXToMove, footY + newDYToMove,
									entitiesInterval.getX1(entity,entitiesIntervalMode), entitiesInterval.getY1(entity,entitiesIntervalMode),   entitiesInterval.getX2(entity,entitiesIntervalMode), entitiesInterval.getY2(entity,entitiesIntervalMode));
								if (intersectionTest != null) {
											// Movement along ceiling while in air WILL cause intersection with platform here
									System.out.println("Movement on ceiling WILL intercept terrain");
											double remainingXToMove = intersectionTest[0] - footX;
											double remainingYToMove = intersectionTest[1] - footY;
											
											dxToMove = dxToMove + remainingXToMove;
											dyToMove = dyToMove + remainingYToMove;
											double dDistanceToMove = Math.sqrt(remainingXToMove*remainingXToMove + remainingYToMove*remainingYToMove);
											amountToMoveCeiling = setZeroIfThreshold(amountToMoveCeiling - dDistanceToMove,landingPrecision);
											amountToMove = amountToMove - dDistanceToMove;
								}
								else {
									// Movement along ceiling while in air will NOT cause intersection with platform here
									System.out.println("Movement on ceiling will NOT intercept terrain");
									dxToMove = dxToMove + newDXToMove;
									dyToMove = dyToMove + newDYToMove;
									System.out.println("dxToMove = "+dxToMove);
									double dDistanceToMove = Math.sqrt(newDXToMove*newDXToMove + newDYToMove*newDYToMove);
									amountToMoveCeiling = setZeroIfThreshold(amountToMoveCeiling - dDistanceToMove,landingPrecision);
									amountToMove = amountToMove - dDistanceToMove;
								}
						}
						else {

								
								// Not within a TERRAIN interval
								dxToMove = dxToMove + newDXToMove;
								dyToMove = dyToMove + newDYToMove;
								double dDistanceToMove = Math.sqrt(newDXToMove*newDXToMove + newDYToMove*newDYToMove);
								amountToMoveCeiling = setZeroIfThreshold(amountToMoveCeiling - dDistanceToMove,landingPrecision);
								amountToMove = amountToMove - dDistanceToMove;
						}
						continue;
						
						
				}
				else if (!hasCeilingPosition) {
					// REMEMBER, NOT ON PLATFORM HERE
					
					double newDXToMove = motionVectorX*Math.min(Math.abs(amountToMoveCeiling), Math.abs(ceilingIntervalEndpointX - footX));
					double newDYToMove = 0.0;
					dxToMove = dxToMove + newDXToMove;
					dyToMove = dyToMove + newDYToMove;
					double dDistanceToMove = Math.sqrt(newDXToMove*newDXToMove + newDYToMove*newDYToMove);
					amountToMoveCeiling = setZeroIfThreshold(amountToMoveCeiling - dDistanceToMove,landingPrecision);
					amountToMove = amountToMove - dDistanceToMove;
					continue;
					
				}
				
				
			}
			
			
		
		}
		System.out.println("^^^^^^^^^^^^^^^^^^^^^checkForCeiling END^^^^^^^^^^^^^^^^^^^^^, finishing with amountToMove = "+amountToMove);
		return new double[] {dxToMove,dyToMove,amountToMove};
		
		
		
	
		
	}

	
	/* * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	    METHOD FOR HANDLING MOTION DUE TO FALLING OR JUMPING,
	    INCLUDING SLIPPING ALONG SLOPED WALLS                 
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	public void jumpOrFall(Entity entity) {
//System.out.println("***************jumpOrFall START****************");
//System.out.println("FOOTY IS "+(entity.getY()-0.5*entity.getHeight()));
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
				
				for (int a = 0; a < activeBackgrounds.size(); a++) {

					for (int v = 0; v < activeBackgrounds.get(a).getCeilings().size(); v++) {
						
						ArrayList<Integer> intervalModes = new ArrayList<Integer>();
						intervalModes.add(1);
						if (activeBackgrounds.get(a).getCeilings().get(v).isLeftCliff()) {
							intervalModes.add(2);
						}
						if (activeBackgrounds.get(a).getCeilings().get(v).isLeftCliff()) {
							intervalModes.add(3);
						}
						
						for (int m = 0; m < intervalModes.size(); m++) {
							Interval aCeiling = activeBackgrounds.get(a).getCeilings().get(v);
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
	
									if ((entity.getY() + 0.5 * entity.getHeight() < activeBackgrounds.get(a).getCeilings()
											.get(v).getLandingPosition(entity, anIntervalMode)
											|| Math.abs(entity.getY() + 0.5 * entity.getHeight()
													- activeBackgrounds.get(a).getCeilings().get(v).getLandingPosition(
															entity, anIntervalMode)) <= landingPrecision)
											&& (activeBackgrounds.get(a).getCeilings().get(v)
													.getLandingPosition(entity, anIntervalMode) < ceilingInterval
															.getLandingPosition(entity, ceilingIntervalMode)
													|| Math.abs(activeBackgrounds.get(a).getCeilings().get(v)
															.getLandingPosition(entity, anIntervalMode)
															- ceilingInterval.getLandingPosition(
																	entity, ceilingIntervalMode)) <= landingPrecision)) {
											ceilingInterval = activeBackgrounds.get(a).getCeilings().get(v);
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
				for (int a = 0; a < activeBackgrounds.size(); a++) {
					for (int v = 0; v < activeBackgrounds.get(a).getSlopedWalls().size(); v++) {
						Interval aSlopedWall = activeBackgrounds.get(a).getSlopedWalls().get(v);
						double x1 = aSlopedWall.getX1(entity, 1),   x2 = aSlopedWall.getX2(entity, 1);
						double wallPosition = aSlopedWall.getLandingPositionFromSpecificPosition(entity, footX, 1);
						double wallAngle = aSlopedWall.getPlatformAngle(1);
						
						if ( (wallAngle > 0.0  &&  footX > x1  &&  (footX < x2 || aSlopedWall.isPositionAtX2(footX,entity, 1)) && !(aSlopedWall.isPositionAtX1(footX,entity, 1)))  
								||  
							(wallAngle < 0.0  &&  (footX > x1 || aSlopedWall.isPositionAtX1(footX,entity, 1))  &&  footX < x2   && !(aSlopedWall.isPositionAtX2(footX,entity, 1))) 
							) 
						{
							
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
										creaturesInterval = aSlopedWall;
									}
								}
								
								
						}
						
					}
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
				for (int a = 0; a < activeBackgrounds.size(); a++) {
					for (int v = 0; v < activeBackgrounds.get(a).getIntervals().size(); v++) {
						
						ArrayList<Integer> intervalModes = new ArrayList<Integer>(); // mode == 1: standard, mode == 2: leftCliff, mode == 3: rightCliff
						intervalModes.add(1);
						if (activeBackgrounds.get(a).getIntervals().get(v).isLeftCliff()) {
							intervalModes.add(2);
						}
						if (activeBackgrounds.get(a).getIntervals().get(v).isRightCliff()) {
							intervalModes.add(3);
						}
						
						for (int m = 1; m <= intervalModes.size(); m++) {
							Interval anInterval = activeBackgrounds.get(a).getIntervals().get(v);
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
					
				

				
				// IF FINAL ENTITIES INTERVAL IS A WALL, UPDATE APPROPRIATE END POINTS TO WALL/TERRAIN INTERSECTION VALUES
				if (creaturesInterval != null) {
					if (creaturesInterval.getType() == 2) {
						//System.out.println(" JumpOrFall: creaturesInterval is a WALL");
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
		
		double belowPlatformDistanceThreshold = 0.05*entity.getHeight();
		double entityLP = 0.0;
		boolean landedWALL = false;
		boolean landedTERRAIN = false;
	
		double fellThruIntervalX1, fellThruIntervalX2, fellThruIntervalY1, fellThruIntervalY2, fellThruLP, fellThruAngle;
		Interval fellThruIntervalLeftNeighbor, fellThruIntervalRightNeighbor;
				
		// TERRAIN CHECK
		for (int j = 0; j < activeBackgrounds.size(); j++) {
			for (int p = 0; p < activeBackgrounds.get(j).getIntervals().size(); p++) {
				
				// Check if this interval is a left and/or right cliff
				ArrayList<Integer> intervalModes = new ArrayList<Integer>(); // mode == 1: standard, mode == 2: leftCliff, mode == 3: rightCliff
				intervalModes.add(1);
				if (activeBackgrounds.get(j).getIntervals().get(p).isLeftCliff()) {
					intervalModes.add(2);
				}
				if (activeBackgrounds.get(j).getIntervals().get(p).isRightCliff()) {
					intervalModes.add(3);
				}
			
				for (int m = 0; m < intervalModes.size(); m++) {
					int anIntervalMode = intervalModes.get(m);
					Interval aTerrain = activeBackgrounds.get(j).getIntervals().get(p);
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
		for (int j = 0; j < activeBackgrounds.size(); j++) {
			for (int p = 0; p < activeBackgrounds.get(j).getSlopedWalls().size(); p++) {
				
					Interval aWall= activeBackgrounds.get(j).getSlopedWalls().get(p);
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

