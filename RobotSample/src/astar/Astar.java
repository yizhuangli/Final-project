package astar;

import sim.util.Bag;
import sim.util.Double2D;
import lsystem.Segment;

/**
 * A* algorithm
 * @author Zhuangli
 *
 */
public class Astar {

	/**
	 * using a* calculate the closest point between two points
	 * @param start
	 * @param end
	 * @param neighbor  neighbors around given start point
	 * @param drivedSeg  segments that robots have passed
	 * @return
	 */
	public Double2D calculateNextPoint(Double2D start, Double2D end, Bag neighbor){
		double w = ((Segment)neighbor.get(0)).w;
		double h = ((Segment)neighbor.get(0)).h;  //scale of screen
		double startx = start.x*w;
		double starty = start.y*h;
		double endx = end.x*w;
		double endy = end.y*h;
		double Fn,Gn,Hn;
		double temp=Double.MAX_VALUE;
		Segment segment = null;
		
		for(int i=0;i<neighbor.size();i++){
			double x = ((Segment)neighbor.get(i)).x1;
			double y = ((Segment)neighbor.get(i)).y1;
			Gn = Math.abs(x-startx)+Math.abs(y-starty);
			Hn = Math.abs(x-endx)+Math.abs(y-endy);
			Fn = Gn+Hn;
			
			Segment s = (Segment) neighbor.get(i);
			Double2D tempoint = new Double2D(s.x1,s.y1);
//			if(drivedPoint.size()>0){
//				for(int j=0;j<drivedPoint.size();j++){
//					if(Fn < temp && !tempoint.equals(drivedPoint.get(j)))
//						segment = (Segment) neighbor.get(i);
//				}
//			}
			
			if(Fn < temp)
				segment = (Segment) neighbor.get(i);
			
			
			temp = Fn;
			
		}
		Double2D nextpoint = new Double2D(segment.x1,segment.y1);
		
		System.out.println("start x,y:"+startx/6+starty/6);
		System.out.println("next x,y"+nextpoint.x/6+nextpoint.y/6);
		return nextpoint;
		
	}
}
