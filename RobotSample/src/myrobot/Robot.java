package myrobot;

import java.text.DecimalFormat;

import astar.Astar;

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
	Astar astar;
	Bag inter; //bag of intersection at specified location
	Bag intersection; //all intersections 
	int direction = 0; //1,-1,2,-2 -- up,down,left,right
	boolean reverseDrive = false;
	boolean isActive = true;
	
	public Robot(Robots state){
		segsize = state.segsize;
		intersection = state.intersection; //all intersection in the map
		astar = new Astar();
	}

	@Override
	public void step(SimState state) {
		sim = (Robots) state;
		Continuous2D environment = sim.robots;
		Double2D me = environment.getObjectLocation(this); //current location of robot
		
		Target t = sim.t;
		Double2D targetloc = environment.getObjectLocation(t); //location of target
		
		double newx = me.x;
		double newy = me.y;
		
//		randomWalk(me); //random walk mode
		
		if(this.isActive ==true){
			
			if(isAtIntersection(me)){ 
				if(targetloc.equals(me)){
					System.out.println("Reach target!!!");
					this.isActive = false;
				}
				else{
					Bag b =	getAvailableNeighbor(me);
					System.out.println("neighbor size: "+b.size());
					Double2D nextpoint = astar.calculateNextPoint(me, targetloc, b);
					Segment currentSeg = findSegmentByStartAndEnd(me,nextpoint);
					this.setCurrentIntersection(currentSeg);
					System.out.println(currentSeg);
					direction = this.getDirection();
					
				}
				
			}

			switch(direction){
			case 1: newy -= 0.1; break; //up
			case -1: newy += 0.1; break;//down
			case 2: newx -= 0.1; break;//left
			case -2: newx += 0.1; break;//right
			}
			//little bugs here, location coordinate's format should be specified like 0.0
			DecimalFormat df = new DecimalFormat("#.#");
			Double2D newloc = new Double2D(Double.parseDouble(df.format(newx)),Double.parseDouble(df.format(newy)));
			sim.robots.setObjectLocation(this, newloc);
			
		} //end of if clause, robot is active
		
		
		
	}
	
	/**
	 * get neighbors except the current coordinate
	 * @param loc
	 * @return
	 */
	public Bag getAvailableNeighbor(Double2D loc){
		Continuous2D drawRoad = sim.drawEnvironment;
		Bag allneighbor = drawRoad.getObjectsExactlyWithinDistance(loc, segsize);
		Bag available = new Bag();
		for(int i=0;i<allneighbor.size();i++){
			double w = this.getCurrentIntersection().w;
			double h = this.getCurrentIntersection().h;
			Double2D currentloc = new Double2D(loc.x*w,loc.y*h);
			Double2D startloc = new Double2D( ((Segment)allneighbor.get(i)).x1, ((Segment)allneighbor.get(i)).y1);
			if(!currentloc.equals(startloc))
				available.add(allneighbor.get(i));
		}
		
		return available;
		
	}
	
	
	
	
	/**
	 * random walk mode
	 * @param me
	 */
	public void randomWalk(Double2D me){
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
			if(isAtRoadEnd(me)){ //robot at the road end
				int direc = this.getCurrentDirection();
				direction = -direc; //change to opposite direction
				System.out.println("at the end"+this.getCurrentIntersection()+", "+direc);
				
			}
		}
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
		Double2D startPoint = new Double2D(((Segment)intersection.get(0)).x1,((Segment)intersection.get(0)).y1); //coordinate of start point
		
		if(rand<numobj){
			reverseDrive = false;
			this.setCurrentIntersection((Segment)inter.get(rand));
			System.out.println("normal"+(Segment)inter.get(rand));
			x=((Segment)inter.get(rand)).x1;
			y=((Segment)inter.get(rand)).y1;
			x1=((Segment)inter.get(rand)).x3;
			y1=((Segment)inter.get(rand)).y3;
			
			System.out.println("normal mode: "+x+y+x1+y1);
			
			if(y1<y){ return 1;} //up
			else if(y1>y){ return -1;} //down 
			else if(x1<x){ return 2;} //left
			else if(x1>x){ return -2;}//right
			
		}
		else{
			reverseDrive = true ;
			Segment s = findSegment(currentloc);
			System.out.println("reverse:"+s);
			if(s!=null){
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
				
			
//			else{  //robot at the start point  and  will turn around
//				reverseDrive = false;
//				Segment start = (Segment) intersection.get(0);
//				this.setCurrentIntersection(start);
//				x = start.x1;
//				y = start.y1;
//				x1 = start.x3;
//				y1 = start.y3;
//				if(y1<y){ return 1;} //up
//				else if(y1>y){ return -1;} //down 
//				else if(x1<x){ return 2;} //left
//				else if(x1>x){ return -2;}//right
//			}
		
		}
		
		
		return 0; //error
	}
	
	/**
	 * find another segment (reserve direction in random walk mode)
	 * @param loc
	 * @return
	 */
	public Segment findSegment(Double2D loc){
		Segment s;
//		System.out.println("findSegment:"+loc.x+loc.y);
		
		for(int i=0;i<intersection.size();i++){
			
			Double2D end = new Double2D( ((Segment)intersection.get(i)).x3,((Segment)intersection.get(i)).y3);
			if(loc.equals(end)){
				return (Segment) intersection.get(i);
			}
		}
		
		return null ;
		
	}
	
	/**
	 * find the segment by given coordinate of start and end point
	 * @param start
	 * @param end
	 * @return
	 */
	public Segment findSegmentByStartAndEnd(Double2D start, Double2D end){
		double w = ((Segment)intersection.get(0)).w;
		double h = ((Segment)intersection.get(0)).h;
		Double2D newstart = new Double2D(start.x*w,start.y*h);
	    Segment seg = null;
		for(int i=0;i<intersection.size();i++){
			seg = (Segment) intersection.get(i);
			Double2D s = new Double2D( ((Segment)intersection.get(i)).x1,((Segment)intersection.get(i)).y1);
			Double2D e = new Double2D( ((Segment)intersection.get(i)).x3,((Segment)intersection.get(i)).y3);
			if(newstart.equals(s) && end.equals(e)){
				reverseDrive = false;
				return seg;
			}
			else if(newstart.equals(e) && end.equals(s)){
				System.out.println("return a reverse segment");
				reverseDrive = true;
				return seg;
			}
		}
		return null;
		
	}
	
	/**
	 * calculate the current direction
	 * @return
	 */
	public int getDirection(){
		Segment s = this.getCurrentIntersection();
		double x=s.x1;
		double y=s.y1;
		double x1=s.x3;
		double y1=s.y3;
		
		if(reverseDrive ==false){
			if(y1<y){ return 1;} //up
			else if(y1>y){ return -1;} //down 
			else if(x1<x){ return 2;} //left
			else if(x1>x){ return -2;}//right
		}
		else{
			if(y1<y){ return -1; }
			else if(y1>y){ return 1;}
			else if(x1<x){ return -2;}
			else if(x1>x){ return 2;}
		}
		
		return 0;
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
