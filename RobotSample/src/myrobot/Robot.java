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
	Robots sim;
	Bag inter; //bag of intersection at specified location
	int direction = 0; //1,2,3,4 -- up,right,down,left
	
	public Robot(Robots state){
		
	}

	@Override
	public void step(SimState state) {
		sim = (Robots) state;
		Continuous2D environment = sim.robots;
		Double2D me = environment.getObjectLocation(this); //current location
		
		Bag intersection = sim.intersection; //all intersection in the map
		
		
		double newx = me.x;
		double newy = me.y;
		System.out.println("newx,newy: "+newx+" "+newy);
		if(isAtIntersection(me)){ //robot at the intersection
			inter = sim.drawEnvironment.getObjectsAtLocation(me);
			int rand; //make a random direction
			
			
//			if(inter.numObjs==1){
//				//rarely happen
//			}
//			else if(inter.numObjs==2){
//				direction = makeRandomDirection(2);
//			}
//			else if(inter.numObjs==3){
//				direction = makeRandomDirection(3);
//			}
//			else if(inter.numObjs==4){
//				direction = makeRandomDirection(4);
//			}
			
//			System.out.println("inter obj num--"+inter.numObjs);
			switch(inter.numObjs){
			case 1: direction = makeRandomDirection(1); break;
			case 2: direction = makeRandomDirection(2); break;
			case 3: direction = makeRandomDirection(3); break;
			case 4: direction = makeRandomDirection(4); break;
			}
			System.out.println("dire"+direction);
		}
		
		
		switch(direction){
		case 1: newy -= 0.1*1.0; break;
		case 2: newx += 0.1*1.0; break;
		case 3: newy += 0.1*1.0; break;
		case 4: newx -= 0.1*1.0; break;
		}
		
		
//		double newx = me.x+1;
//		double newy = me.y;
		
	
		DecimalFormat df = new DecimalFormat("#.#");
		
		Double2D newloc = new Double2D(Double.parseDouble(df.format(newx)),Double.parseDouble(df.format(newy)));
		sim.robots.setObjectLocation(this, newloc);
		
		
	}
	
	/**
	 * make a random direction when robot is at intersection
	 * @param numobj
	 * @return
	 */
	public int makeRandomDirection(int numobj){
		int rand = sim.random.nextInt(numobj);
		System.out.println("rand--"+rand);
		double x,y,x1,y1;
		x=((Segment)inter.get(rand)).x1;
		y=((Segment)inter.get(rand)).y1;
		x1=((Segment)inter.get(rand)).x3;
		y1=((Segment)inter.get(rand)).y3;
		
//		System.out.println("x,y,x1,y1:"+x+" "+y+" "+x1+" "+y1);
		if(y1<y){
			return 1;
		}
		else if(x1>x){
			return 2;
		}
		else if(y1>y){
			return 3;
		}
		else if(x1<x){
			return 4;
		}
		
		return -1; //error
	}
	
	/**
	 * if robot is at the intersection return true
	 * @param loc
	 * @return
	 */
	public Boolean isAtIntersection(Double2D loc){
		if(sim.drawEnvironment.numObjectsAtLocation(loc)>0)
			return true;
		
		return false;
		
	}
	
	
	public void setCurrentIntersection(Segment segment){
		this.segment = segment;
	}
	public Segment getCurrentIntersection(){
		return segment;
		
	}


}
