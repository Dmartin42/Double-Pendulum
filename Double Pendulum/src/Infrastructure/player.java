package Infrastructure;

import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

public class player extends Rectangle{
	private double x,y;
	private double width = 50;
	private int height = 75; 
	private int rotation;
	private double acceleration; 
	private int velX, velY;
	public player(double x, double y) {
		this.x = x;
		this.y = y;
		this.rotation = 0;
		super.setSize(50,75);
	}
	public double getWidth() {
		return this.width;
	}
	public void move() {
		int rot =(int) Math.toRadians(getRotation());
		
		double ew = (((x*Math.cos(rot)) - (y * Math.sin(rot))));
		double ee = (((y * Math.cos(rot)) + (x * Math.sin(rot))));
		velX+=(int) (x* Math.cos(rot) - y * Math.sin(rot));
		velY+= (int) ee;
		x+=velX*acceleration;
		y+=velY*acceleration;
		
	}
	public double getHeight() {
		return height;
		
	}
	public void moveUp() {
		//x' = x cos f - y sin f
		//y' = y cos f + x sin f
	}
	

	/**
	 * @return the x
	 */
	public double getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(double x) {
		this.x = x;
	}


	/**
	 * @return the y
	 */
	public double getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(double y) {
		this.y = y;
	}
	/**
	 * @return the rotation
	 */
	public int getRotation() {
		return rotation;
	}
	/**
	 * @param rotation the rotation to set
	 */
	public void setRotation(int rotation) {
		this.rotation = rotation;
	}
	
	public void goesOffScreen(int screenW, int screenH) {
		if(this.getX()>screenW) {
			this.getBounds().setLocation(screenW-((int)this.getWidth()*2), (int)this.getY());
		}if(this.getY()>screenH) {
			this.getBounds().setLocation((int)getX(), screenH - ((int)this.getHeight()*2));
		}if(this.getX()<0) {
			this.getBounds().setLocation((int)this.getX() + ((int)this.getWidth()*2), (int)this.getY());
		}if(this.getY()<0) {
			this.getBounds().setLocation((int)this.getX(),(int)getY()+((int)this.getHeight()*2));
		}
	}
	/**
	 * @return the acceleration
	 */
	public double getAccel() {
		return acceleration;
	}
	/**
	 * @param acceleration the acceleration to set
	 */
	public void setAccel(double acceleration) {
		this.acceleration = acceleration;
		
		
/*		Line2D line = new Line2D.Double();
		double x0,y0,x1,y1,x2,y2;
		x0 = 0;
		y0 = 0;
		x1 = 0;
		y1 = 50;
		double length;
		double dx = x0 - x1;
		double dy = y0 - y1;
		length = Math.sqrt(Math.pow(dx, 2)+Math.pow(dy, 2));
		double angle = player.getRotation();
		
		double xChange = length * Math.sin(Math.toRadians(angle));
		double yChange = length * Math.cos(Math.toRadians(angle));
		
		x2 = x0 + xChange;
		y2 = y0 + yChange;*/
	}
	/**
	 * @return the velY
	 */
	public int getVelY() {
		return velY;
	}
	/**
	 * @param velY the velY to set
	 */
	public void setVelY(int velY) {
		this.velY = velY;
	}
	/**
	 * @return the velX
	 */
	public int getVelX() {
		return velX;
	}
	/**
	 * @param velX the velX to set
	 */
	public void setVelX(int velX) {
		this.velX = velX;
	}
}
