package myrobot;

import sim.engine.Schedule;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Double2D;

public class Light implements Steppable {

	Robots sim;
	Double2D location;
	
	public int signal = 0; //0 is red, 1 is green
	public Light(Robots robots) {
		
	}
	
	public int getSignal(){
		return signal;
	}
	public void setSignal(int val){
		signal = val;
	}

	public void setLightLocation(Double2D loc) {
		location = loc;
	}
	public Double2D getLightLocation(){
		return location;
	}
	
	@Override
	public void step(SimState state) {
		sim = (Robots) state;
		Schedule schedule = sim.schedule;
		int time = (int) schedule.getTime();
		System.out.println("time: "+time);
		
		if(time%100 ==0){
			if(this.getSignal()==0)
				this.setSignal(1); //green
			else
				this.setSignal(0); //red
		}
	}

	
	

}
