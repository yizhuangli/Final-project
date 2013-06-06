package myrobot;

import lsystem.LSystemData;
import lsystem.LSystemDrawer;
import lsystem.Segment;
import myrobot.Obstacle;
import sim.engine.SimState;
import sim.field.continuous.Continuous2D;
import sim.field.grid.DoubleGrid2D;
import sim.field.grid.IntGrid2D;
import sim.field.grid.SparseGrid2D;
import sim.util.Bag;
import sim.util.Double2D;
import sim.util.Int2D;

public class Robots extends SimState{

	public int numRobots = 1;
	public int numStreets =5;
	public int numObstacles =10;
	
	public Continuous2D robots;
	public IntGrid2D walls;
	public IntGrid2D streets;
	public IntGrid2D lstreets; //L system street
	public IntGrid2D obstacles;
	
	public LSystemData l = new LSystemData();
	LSystemDrawer ld;
	double segsize;
	
    public Continuous2D drawEnvironment;
	
	
	Bag bagObstacle = new Bag();
	Bag intersection; //store the segment of  roads
	
	public int gridWidth = 100;
    public int gridHeight = 100;
    public double xMin = 0;
    public double xMax = 100;
    public double yMin = 0;
    public double yMax = 100;
    public int numExpansion = 1; //L system street rewrite time
    
    public int getNumOfRobots(){ return numRobots; }
    public void setNumOfRobots(int val){ if(val>0) numRobots = val;}
    public int getNumOfStreets(){return numStreets;}
    public void setNumOfStreets(int val){ if(val>0) numStreets = val;}
    
	public Robots(long seed) {
		super(seed);
	}
	
	public void start(){
		super.start();
		robots = new Continuous2D(1.0,gridWidth,gridHeight);
		walls = new IntGrid2D(gridWidth,gridHeight);
		streets = new IntGrid2D(gridWidth,gridHeight);
		lstreets = new IntGrid2D(gridWidth,gridHeight);
		obstacles = new IntGrid2D(gridWidth,gridHeight);
		
		drawEnvironment = new Continuous2D(5, (xMax - xMin), (yMax - yMin));
		reset();
		
		buildWallMap();
		buildStreet();
		buildObstacle();
		buildLSystemStreet();
//		buildRadialStreet();
		
		ld = new LSystemDrawer(l);
        ld.stopper = schedule.scheduleRepeating(ld);
		
		
			
		
		
	}
	
	/**
	 * reset the bag when a new simulation is started
	 */
	private void reset() {
		bagObstacle.clear();
	}
	
	/**
	 * add robots on the road after drawing the road
	 */
	public void addRobot(){
		intersection = ld.intersection;
		segsize = ld.getSegSize();
		int numIntersection = ld.intersection.numObjs;
		System.out.println("Adding robot... Intersection num: "+ld.intersection.numObjs);
		

		Robot r;
		for(int i=0; i< numRobots; i++){
			r = new Robot(this);
			int index = random.nextInt(numIntersection);
			r.setCurrentIntersection((Segment) intersection.get(index));
//			System.out.println(drawEnvironment.numObjectsAtLocation(new Double2D( ((Segment)intersection.get(index)).x ,((Segment)intersection.get(index)).y )));
//			Bag test = drawEnvironment.getObjectsAtLocation(new Double2D(r.getCurrentIntersection().x,r.getCurrentIntersection().y));
//			System.out.println("bag: "+test.numObjs);
			
			schedule.scheduleRepeating(r);
			robots.setObjectLocation(r,
	                new Double2D( ((Segment)intersection.get(index)).x ,((Segment)intersection.get(index)).y )); // random location
		}
		
	}
	
	
	
	private void buildRadialStreet() {
		int x = gridWidth;
		int y = gridHeight;
		
		streets.field[49][49] = 1;
		
		for (int i = 0; i < x; i++) {
			for (int j = 0; j < y; j++)
			{
				if(i>49 && j>49 && (j-49)/(i-49) == 1  )
					streets.field[i][j] = 1;
			}
		}
		
		System.out.println(Math.sin(Math.PI/180*30));
	}
	
	
	private boolean buildCircles(int x,int y) {
		if((y-49)/(x-49) == Math.tan(30))
			return true;
		
		return false;
		
		
	}
	private void buildLSystemStreet() {
		int x = gridWidth;
		int y = gridHeight;
		int rand = random.nextInt(99);
		
		for(int i=0;i<numExpansion;i++){
		
			for(int j=0;j<x;j++){
				for(int k=0;k<y;k++){
					if(j==rand)
						lstreets.field[j][k]=2;
					else if(j>rand)
						lstreets.field[j][k]=1;
					
				}
			}
		}
	}
	
	
	/**
	 * build square random obstacle
	 */
	private void buildObstacle() {
		int xInt = gridWidth;
		int yInt = gridHeight;
		Double2D coordinate;
//		System.out.println("Obstacles are building!");
		for(int i=0;i<numObstacles;i++){
			Obstacle ob = new Obstacle(random.nextInt(10)+1,random.nextInt(10)+1);
			
			ob.setLocation(new Double2D(random.nextDouble()*xInt,random.nextDouble()*yInt));
			bagObstacle.add(ob);
//			obstacles.field[random.nextInt(gridWidth)][random.nextInt(gridHeight)] = 1;
		}
		
		for (int i = 0; i < xInt; i++) {
			for (int j = 0; j < yInt; j++)
			{
				coordinate = new Double2D((double) i , (double) j);
				//loop over all coordinates and check if there is an obstacle there
				if (obstacleAtPoint(coordinate, bagObstacle))
				{
					//System.out.println("setting the point at x:" + Integer.toString(i) + " y:" + Integer.toString(j) + " to 1");
					obstacles.field[i][j] = 1;
				}				
			}
		}
		
	}
	public boolean obstacleAtPoint(Double2D coord, Bag obstacles)
	{
		for (int i = 0; i < obstacles.size(); i++)
		{
			//for all of the obstacles check if the provided point is in it
//			if (((Obstacle) (obstacles.get(i))).inShape(coord)) //[TODO] this might not work it depends on if whatever the object is will override inShape
//			{
//				return true;
//			}
			int w = ((Obstacle) obstacles.get(i)).getW();
			int h = ((Obstacle) obstacles.get(i)).getH();
			int x = (int)coord.getX();
			int y = (int)coord.getY();
			int x1 = (int)((Obstacle) obstacles.get(i)).getLocation().getX();
			int y1 = (int)((Obstacle) obstacles.get(i)).getLocation().getY();
			if(Math.pow(x-x1, 2)<=Math.pow(w, 2) && Math.pow(y-y1, 2)<=Math.pow(h, 2)){
				return true;
			}
		}
		//at this point no cross over has been detected so false should be returned
		return false;
	}
	
	
	public void buildWallMap()
	{
		int xInt = gridWidth;
		int yInt = gridHeight;
		
		for (int i = 0; i < xInt; i++) 
		{
			
			walls.field[0][i]=1;
			walls.field[yInt-1][i]=1;			
		}
		
		for (int j = 0; j < yInt; j++) 
		{
			
			walls.field[j][0]=1;
			walls.field[j][xInt-1]=1;			
		}
		
	}
	
	/**
	 * build squre street
	 */
	public void buildStreet(){
		int x = gridWidth;
		int y = gridHeight;
		
			
		for(int j=0;j<numStreets;j++){
			
			for(int i=0;i<x;i++){
				
				streets.field[x/numStreets*(j+1)-1][i]=1;
			}
			
			for(int k=0;k<y;k++){
				streets.field[k][(int)((y/numStreets)*(j+1))-1] = 1;
				
			}
		}
	}
	

	public static void main(String[] args) {
		doLoop(Robots.class, args);
		System.exit(0);

	}

}
