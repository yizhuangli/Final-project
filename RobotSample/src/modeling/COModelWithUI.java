package modeling;
import sim.display.*;
import sim.engine.*;
import sim.portrayal.DrawInfo2D;
import sim.portrayal.Inspector;
import sim.portrayal.continuous.*;
import sim.portrayal.simple.*;
import javax.swing.*;
import java.awt.Color;
import java.awt.Graphics2D;

import sim.portrayal.grid.FastValueGridPortrayal2D;

/**
 * A class for running a simulation with a UI, run to see a simulation with a UI
 * showing it running.
 * 
 * @author Robert Lee
 */
public class COModelWithUI extends GUIState
{	
	protected COModelBuilder sBuilder; // = new COModelBuilder((COModel) state);
	
	

	
	
	public Display2D display;
	public JFrame displayFrame;
	ContinuousPortrayal2D environmentPortrayal = new ContinuousPortrayal2D();
	FastValueGridPortrayal2D obstaclesPortrayal = new FastValueGridPortrayal2D("Obstacle", true);  // immutable
	FastValueGridPortrayal2D terrainPortrayal = new FastValueGridPortrayal2D("Terrain", true);  // immutable
	FastValueGridPortrayal2D wallPortrayal = new FastValueGridPortrayal2D("Wall", true);  // immutable
	    
	
   
    public COModelWithUI() 
    { 
    	
    	super(new COModel( System.nanoTime(), 100, 100, true)); 
    	System.out.println("COModelWithUI is being called!"+ "it's state(model)is: "+ state.toString());
    	sBuilder = new COModelBuilder((COModel) state);
    }
    
    
    public COModelWithUI(SimState state) {super(state); }    
    
    public static String getName() { return "Robot-Testing-Sim"; } //[TODO] rename this
    
    
    
  
    //code for the portraying of the field

 
	public void start()
	{
		System.out.println("COModelWithUI.start is called  "+ sBuilder.sim);
		sBuilder.sim.reset();
		//sBuilder.testSim();
		sBuilder.generateSimulation(((COModel) state).noObstacles,((COModel) state).noCars);
		
		super.start();
		setupPortrayals();
		
		
	}

	/**
	 * I do not know if this method is required by MASON at all, and the simulation
	 * with UI appears to run correctly even when it is removed, however all of 
	 * the example simulations that MASON comes with include a load method in the
	 * with UI class so I have done as well even though I have not found a reason
	 * as to if it is important to have one.
	 */
	public void load(SimState state)
	{
		sBuilder.sim.reset();
		//sBuilder.testSim();
		sBuilder.generateSimulation(((COModel) state).noObstacles,((COModel) state).noCars);
		super.load(state);
		setupPortrayals();
	}

	
	
	/**
	 * A method which sets up the portrayals of the different layers in the UI,
	 * this is where details of the simulation are coloured and set to different
	 * parts of the UI
	 */
	public void setupPortrayals()
	{		
		COModel simulation = (COModel) state;
		
		// tell the portrayals what to portray and how to portray them
		environmentPortrayal.setField( simulation.environment );	
		environmentPortrayal.setPortrayalForClass(Car.class, new RectanglePortrayal2D(2)
		{
			public void draw(Object object, Graphics2D graphics, DrawInfo2D info)
			{
				if(((Car)object).isActive==true)
				{
					paint = new Color(255, 0, 0x00);
				}
				else
				{
					paint = new Color(0,0,0);
				}
								
			    super.draw(object, graphics, info);
			}
		});
		
		environmentPortrayal.setPortrayalForClass(Target.class, new LabelledPortrayal2D( new HexagonalPortrayal2D()
		{
			public void draw(Object object, Graphics2D graphics, DrawInfo2D info)
			{
				paint = new Color(0x0E, 0xEC, 0xF0);			
			    super.draw(object, graphics, info);
			}
		}, "T", new Color(0, 0, 0), false) 
				
		
		);
		
		
		obstaclesPortrayal.setField(simulation.obstacleMap);
		obstaclesPortrayal.setMap(new sim.util.gui.SimpleColorMap(
				0,
				1,
				new Color(0,0,0,0),
				new Color(0,0,255,255)
				));

		terrainPortrayal.setField(simulation.terrainMap);
		terrainPortrayal.setMap(new sim.util.gui.SimpleColorMap(
				0,
				Constants.GRAVEL,
				new Color(0,0,0,0),
				new Color(0,255,0,255)
				));
        
		wallPortrayal.setField(simulation.wallMap);
		wallPortrayal.setMap(new sim.util.gui.SimpleColorMap(
				0,
				1,
				new Color(0,0,0,0),
				new Color(255,0,0,255)
				));		
		
		// reschedule the displayer
		display.reset();
		// redraw the display
		display.repaint();
	}
	
	

    public void init(Controller c)
        {
        super.init(c);

        // make the displayer
        display = new Display2D(600,600,this);
        // turn off clipping
        display.setClipping(false);

        displayFrame = display.createFrame();
        displayFrame.setTitle("Environment Display");
        c.registerFrame(displayFrame);   // register the frame so it appears in the "Display" list
        displayFrame.setVisible(true);
		
		//adding the different layers to the display
		display.attach(terrainPortrayal,"Terrain");
		display.attach(obstaclesPortrayal,"Obstacles");		
        display.attach(environmentPortrayal, "Environment" );
        display.attach(wallPortrayal,"Wall");	
        
        System.out.println("COModelWithUI.init is called!");
        }
    
    
    

    public void quit()
        {
        super.quit();

        if (displayFrame!=null) displayFrame.dispose();
        displayFrame = null;
        display = null;
        }
    
    
    
    public Object getSimulationInspectedObject(){return state;}
    
    public Inspector getInspector()
    {
    	Inspector i = super.getInspector();
    	i.setVolatile(true);
    	return i;
    }   

//	
//    public static void main(String[] args)
//    {
//    	COModelWithUI vid = new COModelWithUI();
//    	Console c = new Console(vid);
//		c.setVisible(true);
//
//    }
	
}
