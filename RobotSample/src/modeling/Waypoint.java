package modeling;
import sim.util.*;

/**
 *
 * @author rl576
 */
public class Waypoint extends Entity
{
	private int nextPoint; //the id of the point to go to after this waypoint
	
	/** Constructor for Waypoint
	 * 
	 * @param ID the id of the waypoint
	 * @param next the id of the point that should be travelled to after this waypoint is reached
	 */
	public Waypoint(int ID, int next)
	{
		super(ID, TWAYPOINT);
		nextPoint = next;
	}
	
	/**
	 * A method which returns the id of the point to go to after this waypoint has
	 * been reached
	 * 
	 * @return the id of the next point for the car to travel to
	 */
	public int getNextPoint() {return nextPoint;}
}
