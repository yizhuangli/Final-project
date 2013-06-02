package myrobot;

import sim.util.Double2D;

public class Obstacle {

	public int w,h;
	public Double2D location;
	/**
	 * make a random square obstacle
	 * @param w
	 * @param h
	 */
	public Obstacle(int w, int h){
		this.w = w;
		this.h = h;
	}

	public boolean inShape(Double2D coord) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public int getW(){return w;}
	public int getH(){return h;};
	
	public Double2D getLocation() {
		return location;
	}


	public void setLocation(Double2D location) {
		this.location = location;
	}

}
