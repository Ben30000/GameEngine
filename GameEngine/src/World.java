import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.util.ArrayList;

import javax.swing.JFrame;



import java.awt.Graphics2D;



import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;
import java.util.ArrayList;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;







// package org.joml.lwjgl; 
 
 
 import org.joml.Matrix4f; 
 import org.joml.Matrix4d;
 import org.joml.Vector3f;
 import org.joml.Vector3d;
 import org.joml.Quaternionf; 


public class World {
	
	private double x;
	private double y;
	private double z;
	private double previousX;
	private double previousY;
	private double currentX;
	private double currentY;;
	private double dx;
	private double dy;
	private boolean lDeadEnd;
	private boolean rDeadEnd;
	private double height;
	private double width;
	//private ArrayList<Boundary> boundaries;
	private boolean onScreen;
	private ArrayList<Object> mobs;
	private Image background;
    private int bgMovementThreshold;
    
    private IntervalHashMap terrain;
    private IntervalHashMap ceilings;
    private IntervalHashMap slopedWalls;
    
    private ArrayList<CameraInterval> cameraIntervals;
    
    private ArrayList<StageItemPOM> stageItems;
    private ArrayList<Interval> items;
    
    
    private boolean playerFalls;
    private boolean bgFalls;
    //private ArrayList<FallInterval> fallIntervals;
    private ArrayList<DeadEndInterval> deadEndIntervals;
    private Light directionalLight;
    private ArrayList<Water> bodiesOfWater;
    private Player p;
    
    private Interval recentInterval;                 // Used for Vertical Deead End in Backup Camera System
    
    private double landingPrecision = 0.00001;
    
	
	public World(Player p) {
		x = 7.0;
		//y = -1.41;
		y = 18.0;
		z = 0.0;
		//y = 0.0;
		dx = 0.0;
		dy = 0.0;
		
		this.p = p;
		width = 4000;
		height = 1200;
	   // boundaries = new ArrayList<Boundary>();
	    mobs = new ArrayList<Object>();
	    onScreen = false;
	    
	    items = new ArrayList<Interval>();
	    
	    
	    deadEndIntervals = new ArrayList<DeadEndInterval>();
	    
	    
	    //intervals = new ArrayList<Interval>();
	    
	    
	    
	   stageItems = new ArrayList<StageItemPOM>();
	    
	cameraIntervals = new ArrayList<CameraInterval>();
		
	bodiesOfWater = new ArrayList<Water>();
	
	}
	
	public void setOnScreen(boolean s) {
		onScreen = s;
	}
	
	public boolean getOnScreen() {
		return onScreen;
	}
	
	public void setBgMovementThreshold(int bgMovementThreshold) {
		this.bgMovementThreshold = bgMovementThreshold;
	}
	
	public int getBgMovementThreshold() {
		return bgMovementThreshold;
	}
	/*
	public ArrayList<Boundary> getBoundaries() {
		return boundaries;
	}
	*/
	
	public ArrayList<Object> getMobs() {
		return mobs;
	}
	
	public IntervalHashMap getTerrain() {
		return terrain;
	}
	
	public void setTerrainHashMap(ArrayList<Interval> terrain, double minX1, double mapChunkSize) {
		this.terrain = new IntervalHashMap(terrain, minX1, 100.0, mapChunkSize);
	}
	
	public boolean isLDeadEnd() {
		return lDeadEnd;
	}
	public boolean isRDeadEnd() {
		return rDeadEnd;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public void setY(double y) {
	      this.y = y;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public void setPreviousX(double x) {
		this.previousX = x;
	}
	
	public void setPreviousY(double y) {
		this.previousY = y;
	}
	
	public double getPreviousX() {
		return previousX;
	}
	
	public double getPreviousY() {
		return previousY;
	}
	
	
	
	public double getCurrentX() {
		return currentX;
	}
	
	public double getCurrentY() {
		return currentY;
	}
	
	public void setCurrentX(double x) {
		this.currentX = x;
	}
	
	public void setCurrentY(double y) {
		this.currentY = y;
	}
	
	public void setDX(double dx) {
		this.dx = dx;
	}
	
	public double getDX() {
		return dx;
	}
	
	public void setDY(double dy) {
		this.dy = dy;
	}
	
	public double getDY() {
		return dy;
	}
    
	public double getWidth() {
	    return width;
	}
	public double getHeight() {
		return height;
	}
	/*
	public void move(double dxToMove, double dyToMove, int cameraHorizontalMovementType, int cameraVerticalMovementType) {
		
	//	System.out.println("@ Beginning of bg.move(), bg.dx = " + dx + " and bg.dy = " + dy);
		
		// cameraHorizontalMovementType:
		//   1 : camera updating from player GOING left
		//   2 : camera updating from player GOING right
		//   3 : camera remains static
		
		double targetTHold = 1.0;
		boolean deFound = false;
		double distToTargetTHold = 1.0;
		
		
		// cameraHorizontalMovementType 1 if left camera shift, 2 if right camera shift, 3 if player progression, 0 if no horizontal movement
		
		
		
		
		// Horizontal Word Movement
		
		
		
		
		
		
		
		if (cameraHorizontalMovementType == 1) {
			for (int j = 0; j < this.getDeadEndIntervals().size(); j++) {
				if ((p.getX() > this.getDeadEndIntervals().get(j).getX1() || Math.abs(p.getX() - this.getDeadEndIntervals().get(j).getX1()) <= landingPrecision)    &&     (p.getX() < this.getDeadEndIntervals().get(j).getX2() ||  Math.abs(p.getX() - this.getDeadEndIntervals().get(j).getX2()) <= landingPrecision)    &&    (p.getY() + 0.5*p.getHeight() > this.getDeadEndIntervals().get(j).getY1() || Math.abs(p.getY() + 0.5*p.getHeight() - this.getDeadEndIntervals().get(j).getY1()) <= landingPrecision)    &&    (p.getY() - 0.5*p.getHeight() < this.getDeadEndIntervals().get(j).getY2() || Math.abs(p.getY() -0.5*p.getHeight() - this.getDeadEndIntervals().get(j).getY2()) <= landingPrecision)    &&      this.getDeadEndIntervals().get(j).getType() == 1) {
		
					deFound = true;
					targetTHold = this.getDeadEndIntervals().get(j).getTargetX();
				    break;
				}
			}
			
			if (deFound) {
				

				distToTargetTHold = Math.abs(targetTHold - this.getX());
			if ( this.getX() < targetTHold || Math.abs(this.getX() - targetTHold) <= landingPrecision) {	
				if (this.getX() + dx > targetTHold || Math.abs(this.getX() + dx - targetTHold) <= landingPrecision) {
					
					x = x + distToTargetTHold;
			//		y = y + dy;
					x = targetTHold;
					
				p.setDX(distToTargetTHold);
				p.setDY(0.0);
				p.move(1);
				
				}
				else {
		
					x = x + dx;
			//		y = y + dy;
					p.setDX(dx);
					p.setDY(0.0);
					p.move(1);
				}
			}
			else {
				x = x;
				y = y;
				
			  }
			
			
			}
			
			
			
			
			
			else {
				x = x + dx;
		//		y = y + dy;
				p.setDX(dx);
				p.setDY(0.0);
				p.move(1);
			}
			
		}
		
		else if (cameraHorizontalMovementType == 2) {
			
		
		
		
		
		
			for (int j = 0; j < this.getDeadEndIntervals().size(); j++) {
				if ((p.getX() > this.getDeadEndIntervals().get(j).getX1() || Math.abs(p.getX() - this.getDeadEndIntervals().get(j).getX1()) <= landingPrecision)    &&     (p.getX() < this.getDeadEndIntervals().get(j).getX2() ||  Math.abs(p.getX() - this.getDeadEndIntervals().get(j).getX2()) <= landingPrecision)    &&    (p.getY() + 0.5*p.getHeight() > this.getDeadEndIntervals().get(j).getY1() || Math.abs(p.getY() + 0.5*p.getHeight() - this.getDeadEndIntervals().get(j).getY1()) <= landingPrecision)    &&    (p.getY() - 0.5*p.getHeight() < this.getDeadEndIntervals().get(j).getY2() || Math.abs(p.getY() -0.5*p.getHeight() - this.getDeadEndIntervals().get(j).getY2()) <= landingPrecision)    &&      this.getDeadEndIntervals().get(j).getType() == 2) {
		
					deFound = true;
					targetTHold = this.getDeadEndIntervals().get(j).getTargetX();
				    break;
				}
			}
			
			if (deFound) {
				
				distToTargetTHold = Math.abs(this.getX() - targetTHold);
		    if (this.getX() > targetTHold || Math.abs(this.getX() - targetTHold) <= landingPrecision) {		
				if (this.getX() + dx < distToTargetTHold || Math.abs(this.getX() + dx - distToTargetTHold) <= landingPrecision) {
					
					x = x - distToTargetTHold;
		//			y = y + dy;
					
				}
				else {
					x = x + dx;
		//			y = y + dy;
				}
			}
			
			
			else {
				
				x = x;
				y = y;
				
			}
			}
			else {
				
			//	System.out.println("THE   BG   DX    WAS   " + dx);
				x = x + dx;
	//			y = y + dy;
			}
		
		
		
		
		
		
		
		
		
		
		}
		
		else if (cameraHorizontalMovementType == 3) {
			
			if (p.isGoingRight()) {
				if (dy < 0 ) {
					System.out.println("Repair it");
				// 	System.exit(0);
				}
				

				for (int j = 0; j < this.getDeadEndIntervals().size(); j++) {
					if ((p.getX() > this.getDeadEndIntervals().get(j).getX1() || Math.abs(p.getX() - this.getDeadEndIntervals().get(j).getX1()) <= landingPrecision)    &&     (p.getX() < this.getDeadEndIntervals().get(j).getX2() ||  Math.abs(p.getX() - this.getDeadEndIntervals().get(j).getX2()) <= landingPrecision)    &&    (p.getY() + 0.5*p.getHeight() > this.getDeadEndIntervals().get(j).getY1() || Math.abs(p.getY() + 0.5*p.getHeight() - this.getDeadEndIntervals().get(j).getY1()) <= landingPrecision)    &&    (p.getY() - 0.5*p.getHeight() < this.getDeadEndIntervals().get(j).getY2() || Math.abs(p.getY() -0.5*p.getHeight() - this.getDeadEndIntervals().get(j).getY2()) <= landingPrecision)    &&      this.getDeadEndIntervals().get(j).getType() == 2) {
			
						deFound = true;
						targetTHold = this.getDeadEndIntervals().get(j).getTargetX();
					    break;
					}
				}
				
				if (deFound) {
					
					distToTargetTHold = Math.abs(this.getX() - targetTHold);
				  if (this.getX() > targetTHold || Math.abs(this.getX() - targetTHold) <= landingPrecision) {
					  
				  
					if (this.getX() + dx > targetTHold || Math.abs(this.getX() + dx - targetTHold) <= landingPrecision) {
						
						x = x - distToTargetTHold;
				//		y = y + dy;
						
						this.setX(targetTHold);
						p.setDX(Math.abs(dx) - distToTargetTHold);
						p.setDY(0.0);
						p.move(3);
					}
					else {
						x = x + dx;
				//		y = y + dy;
					}
				}
				else {
					p.setX(-1.0*dx);
					p.setDY(0.0);
					p.move(3);
					
				}
				
				}
				
				else {
					x = x + dx;
			//		y = y + dy;
				}
			
			
				
			}
			
			
			
			
			
			
			
			
			
			else if (p.isGoingLeft()) {
				
		//		System.out.println("IN BG MOVE METHOD, CAMERA HORIZONTAL MOVE = 3, P GOING LEFT, START, P.X IS  " + p.getX());
				
			
				for (int j = 0; j < this.getDeadEndIntervals().size(); j++) {
					if ((p.getX() > this.getDeadEndIntervals().get(j).getX1() || Math.abs(p.getX() - this.getDeadEndIntervals().get(j).getX1()) <= landingPrecision)    &&     (p.getX() < this.getDeadEndIntervals().get(j).getX2() ||  Math.abs(p.getX() - this.getDeadEndIntervals().get(j).getX2()) <= landingPrecision)    &&    (p.getY() + 0.5*p.getHeight() > this.getDeadEndIntervals().get(j).getY1() || Math.abs(p.getY() + 0.5*p.getHeight() - this.getDeadEndIntervals().get(j).getY1()) <= landingPrecision)    &&    (p.getY() - 0.5*p.getHeight() < this.getDeadEndIntervals().get(j).getY2() || Math.abs(p.getY() -0.5*p.getHeight() - this.getDeadEndIntervals().get(j).getY2()) <= landingPrecision)    &&      this.getDeadEndIntervals().get(j).getType() == 1) {
			
						deFound = true;
						targetTHold = this.getDeadEndIntervals().get(j).getTargetX();
					    break;
					}
				}
				
				if (deFound) {
					
					
		//			System.out.println("IN BG MOVE METHOD, CAMERA HORIZONTAL MOVE = 3, P GOING LEFT, DE FOUND, P.X IS  " + p.getX());
					
					distToTargetTHold = Math.abs(this.getX() - targetTHold);
				  if (this.getX() < targetTHold || Math.abs(this.getX() - targetTHold) <= landingPrecision) {
					  
				  
					if (this.getX() + dx > targetTHold || Math.abs(this.getX() +  dx - targetTHold) <= landingPrecision) {
				//System.out.println("The distance to deThold is " + distToTargetTHold);		
						x = x + distToTargetTHold;
				//		y = y + dy;
						
				//		this.setX(targetTHold);
						
						//System.out.println("SaUcE!!!!!!!!!!!!:    " + (Math.abs(dx) - distToTargetTHold));
						//System.out.println("Bazooka : distance to de target " + distToTargetTHold);
						//System.out.println("THAT DX IS " + dx);
						p.setDX(-1.0*(Math.abs(dx) - distToTargetTHold));
						p.setDY(0.0);
						p.move(3);
					}
					else {
						x = x + dx;
			//			y = y + dy;
					}
				}
				else {
					p.setDX(-1.0*dx);
					p.setDY(0.0);
					p.move(3);
					p.setDX(0.0);
					p.setDY(0.0);
					
				}
				
				}
				
				else {
					 
				//	System.out.println("IN BG MOVE METHOD, CAMERA HORIZONTAL MOVE = 3, P GOING LEFT, DE WAS NOT FOUND, P.X IS  " + p.getX());
					
					x = x + dx;
					//y = y + dy;
				}
		
			}
			     
			//System.out.println("IN BG MOVE METHOD, CAMERA HORIZONTAL MOVE = 3, P GOING LEFT, FINISH, P.X IS  " + p.getX());
			
		}
		
		
		
		
		
		
		
		
		
		
		
		
		// Vertical World Movement
		
		// camraVerticalMovementType, 1 = panningD, 2 = panningU, 3 = player progression
		
		
		if (cameraVerticalMovementType == 1) {
		
			y = y + dy;
			p.setDX(0.0);
			p.setDY(dy);
			p.move(1);
			
		}
		else if (cameraVerticalMovementType == 2) {
			

			  
			y = y + dy;
			
			
			

			p.setDX(0.0);
			p.setDY(dy);
			p.move(1);
			
			
			
			
			
			
			
			
		}
		else if (cameraVerticalMovementType == 3) {
			
			y = y + dy;
		}
		
		
		
		
		
		
		
		
		
		
		
		
		
	}
	*/
	/*
	public ArrayList<FallInterval> getFallIntervals() {
		return fallIntervals;
	}

	public void setFallIntervals(ArrayList<FallInterval> fallIntervals) {
		this.fallIntervals = fallIntervals;
	}
	*/
	public void animate(Graphics2D g, double interpolation, WorldUpdater s, JFrame c) {
	//	System.out.println("ANIMATING                                                     BACKGROUND");
		//g.drawImage(background, (int)x, (int)y, c);
		AffineTransform originalTransform = g.getTransform();
		AffineTransform t = new AffineTransform();
		//g.setColor(new Color(0,0,0,20));
		g.drawRect(0, 0, 1920, 1080);
		//g.setColor(new Color(0,0,0,255));
		//t.translate(x + interpolation*s.getTotalBGDX(), y + interpolation*s.getTotalBGDY());
		t.translate((x - previousX)*interpolation + previousX, (y - previousY)*interpolation + previousY);
		//t.translate(currentX*(interpolation) + previousX*(1.0 - interpolation), currentY*(interpolation) + previousY*(1.0 - interpolation));
	//t.translate(x,y);
		t.scale(1,1);
		g.drawImage(background, t, null);
		g.setTransform(originalTransform);
		
	}
	
	
	public void setStageItems(ArrayList<StageItemPOM> stageItems) {
		this.stageItems = stageItems;
	}
	
	public ArrayList<StageItemPOM> getStageItems() {
		return stageItems;
	}

	public ArrayList<CameraInterval> getCameraIntervals() {
		return cameraIntervals;
	}

	public void setCameraIntervals(ArrayList<CameraInterval> cameraIntervals) {
		this.cameraIntervals = cameraIntervals;
	}

	public ArrayList<DeadEndInterval> getDeadEndIntervals() {
		return deadEndIntervals;
	}

	public void setDeadEndIntervals(ArrayList<DeadEndInterval> deadEndIntervals) {
		this.deadEndIntervals = deadEndIntervals;
	}
/*
	public ArrayList<Wall> getWalls() {
		return walls;
	}
	*/
/*
	public void setWalls(ArrayList<Wall> walls) {
		this.walls = walls;
	}
	*/
	/*
	public void addWall(Wall wall) {
		this.walls.add(wall);
	}
*/
	public ArrayList<Water> getBodiesOfWater() {
		return bodiesOfWater;
	}

	public void setBodiesOfWater(ArrayList<Water> bodiesOfWater) {
		this.bodiesOfWater = bodiesOfWater;
	}

	public IntervalHashMap getSlopedWalls() {
		return slopedWalls;
	}

	public void setSlopedWallsHashMap(ArrayList<Interval> slopedWalls, double minX1, double mapChunkSize) {
		this.slopedWalls = new IntervalHashMap(slopedWalls, minX1, 100.0, mapChunkSize);
	}
	

	public IntervalHashMap getCeilings() {
		return ceilings;
	}

	public void setCeilingsHashMap(ArrayList<Interval> ceilings, double minX1, double mapChunkSize) {
		this.ceilings = new IntervalHashMap(ceilings, minX1, 100.0, mapChunkSize);
	}
	
	
	public void setRecentInterval(Interval interval) {
		this.recentInterval = interval;
	}
	public Interval getRecentInterval() {
		return recentInterval;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	
}
