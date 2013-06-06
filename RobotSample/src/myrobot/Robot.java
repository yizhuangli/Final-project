package myrobot;

import java.text.DecimalFormat;

import lsystem.Segment;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.field.continuous.Continuous2D;
import sim.util.Bag;
import sim.util.Double2D;
import sim.util.IntBag;


public class Robot implements Steppable{
	
	
	Segment segment;
	double segsize;
	Robots sim;
	Bag inter; //bag of intersection at specified location
	Bag intersection; //all intersections 
	int direction = 0; //1,2,3,4 -- up,right,down,left
	boolean reverseDrive = false;
	
	public Robot(Robots state){
		segsize = state.segsize;
		intersection = state.intersection; //all intersection in the map
	}

	@Override
	public void step(SimState state) {
		sim = (Robots) state;
		Continuous2D environment = sim.robots;
		Double2D me = environment.getObjectLocation(this); //current location
		
		
		
		double newx = me.x;
		double newy = me.y;
		if(isAtIntersection(me)){ //robot at the intersection
			
			inter = sim.drawEnvironment.getObjectsAtLocation(me);
			int rand; //make a random direction
			
			switch(inter.numObjs){
			case 1: direction = makeRandomDirection(1,me); break;
			case 2: direction = makeRandomDirection(2,me); break;
			case 3: direction = makeRandomDirection(3,me); break;
			case 4: direction = makeRandomDirection(4,me); break;
			}
			
		}
		
		if(reverseDrive==false){
			if(isAtRoadEnd(me)){ //robot at the end
				int direc = this.getCurrentDirection();
				direction = -direc; //change to opposite direction
				System.out.println("at the end"+this.getCurrentIntersection()+", "+direc);
				
			}
		}

		
		
		switch(direction){
		case 1: newy -= 0.1; break; //up
		case -1: newy += 0.1; break;//down
		case 2: newx -= 0.1; break;//left
		case -2: newx += 0.1; break;//right
		}
		
		
		
	
		//little bugs here, location coordinate's format should be specified
		DecimalFormat df = new DecimalFormat("#.#");
		Double2D newloc = new Double2D(Double.parseDouble(df.format(newx)),Double.parseDouble(df.format(newy)));
		sim.robots.setObjectLocation(this, newloc);
		
		
	}
	
	

	/**
	 * make a random direction when robot is at intersection
	 * @param numobj
	 * @param loc 
	 * @return
	 */
	public int makeRandomDirection(int numobj, Double2D loc){
		int rand = sim.random.nextInt(numobj+1);
		double x,y,x1,y1;
//		Double2D currentloc = new Double2D(origin.x1,origin.y1);
		double w = this.getCurrentIntersection().w;
		double h = this.getCurrentIntersection().h;
		Double2D currentloc = new Double2D(loc.x*w,loc.y*h);
		
		
		if(rand<numobj){
			reverseDrive = false;
			this.setCurrentIntersection((Segment)inter.get(rand));
			System.out.println("normal"+(Segment)inter.get(rand));
			x=((Segment)inter.get(rand)).x1;
			y=((Segment)inter.get(rand)).y1;
			x1=((Segment)inter.get(rand)).x3;
			y1=((Segment)inter.get(rand)).y3;
			
			if(y1<y){ return 1;} //up
			else if(y1>y){ return -1;} //down 
			else if(x1<x){ return 2;} //left
			else if(x1>x){ return -2;}//right
			
		}
		else{
			reverseDrive = true ;
			Segment s = findSegment(currentloc);
			System.out.println("reverse"+s);
			if(s!=null)
				this.setCurrentIntersection(s);
			
			x = s.x1;
			y = s.y1;
			x1 = s.x3;
			y1 = s.y3;
			if(y1<y){ return -1; }
			else if(y1>y){ return 1;}
			else if(x1<x){ return -2;}
			else if(x1>x){ return 2;}
			
		}
		
		
		return 0; //error
	}
	
	/**
	 * find another segment (reserve direction)
	 * @param loc
	 * @return
	 */
	public Segment findSegment(Double2D loc){
		Segment s;
		for(int i=0;i<intersection.size();i++){
			Double2D end = new Double2D( ((Segment)intersection.get(i)).x3,((Segment)intersection.get(i)).y3);
			if(loc.equals(end)){
				return (Segment) intersection.get(i);
			}
		}
		
		return null ;
		
	}
	
	/**
	 * if robot is at the intersection return true
	 * @param loc
	 * @return
	 */
	public boolean isAtIntersection(Double2D loc){
		if(sim.drawEnvironment.numObjectsAtLocation(loc)>0)
			return true;
		
		return false;
	}
	
	/**
	 * if robot is at the end of road, return true;
	 * @param loc
	 * @return
	 */
	public boolean isAtRoadEnd(Double2D loc) {
		Segment current = this.getCurrentIntersection();
		double w = this.getCurrentIntersection().w;
		double h = this.getCurrentIntersection().h;
		
		double x,y,x1,y1;
		x = loc.x;
		y = loc.y;
		x1 = current.x3;
		y1 = current.y3;
		
		if(x*w==x1 && y*h==y1) //multipy the scale size of screen
			return true;
		
		return false;
	}
	
	
	public void setCurrentIntersection(Segment segment){
		this.segment = segment;
	}
	public Segment getCurrentIntersection(){
		return segment;
	}
	public int getCurrentDirection(){
		return direction;
	}


}
