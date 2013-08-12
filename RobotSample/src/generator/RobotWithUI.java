package generator;

import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import lsystem.DrawUI;
import lsystem.LSystemData;
import lsystem.Rule;
import lsystem.RuleUI;

import sim.display.Console;
import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.engine.SimState;
import sim.portrayal.DrawInfo2D;
import sim.portrayal.Inspector;
import sim.portrayal.continuous.ContinuousPortrayal2D;
import sim.portrayal.grid.FastValueGridPortrayal2D;
import sim.portrayal.grid.SparseGridPortrayal2D;
import sim.portrayal.simple.OvalPortrayal2D;
import sim.portrayal.simple.RectanglePortrayal2D;

public class RobotWithUI extends GUIState{
	
	public Display2D display;
	public JFrame displayFrame;
	
	ContinuousPortrayal2D robotsPortrayal = new ContinuousPortrayal2D();
	FastValueGridPortrayal2D wallPortrayal = new FastValueGridPortrayal2D("Wall", true);  // immutable
	FastValueGridPortrayal2D streetPortrayal = new FastValueGridPortrayal2D("Street", true); 
	FastValueGridPortrayal2D lstreetPortrayal = new FastValueGridPortrayal2D("L-Street", true); 
	FastValueGridPortrayal2D obstaclePortrayal = new FastValueGridPortrayal2D("Obstacle", true); 
	
	ContinuousPortrayal2D systemPortrayal = new ContinuousPortrayal2D();
	
	public RobotWithUI(SimState state) {super(state); }
	public RobotWithUI() { super(new Robots(System.currentTimeMillis()));	}
	public static String getName(){ return "Robot simulation"; }
	
	public Object getSimulationInspectedObject(){return state;}
    public Inspector getInspector()
    {
     	Inspector i = super.getInspector();
     	i.setVolatile(true);
     	return i;
    }  

	public static void main(String[] args) {
		RobotWithUI r = new RobotWithUI();
		Console c = new Console(r);
        c.setVisible(true);

	}
	
	 public void quit()
     {
		 super.quit();
		 if (displayFrame!=null) displayFrame.dispose();
		 displayFrame = null;  // let gc
		 display = null;       // let gc
     }

	 public void start()
     {
		 super.start();
		 // set up our portrayals
		 setupPortrayals();
     }
 

 
	 public void load(SimState state)
     {
		 super.load(state);
		 // we now have new grids.  Set up the portrayals to reflect that
		 setupPortrayals();
     }
	 
	 public void setupPortrayals()
     {
     // tell the portrayals what to
     // portray and how to portray them
	 systemPortrayal.setField(((Robots)state).drawEnvironment);	 
		 
     robotsPortrayal.setField(((Robots)state).robotEnvironment);
//     robotsPortrayal.setPortrayalForAll( new sim.portrayal.simple.OvalPortrayal2D(Color.black) );
     robotsPortrayal.setPortrayalForClass(Target.class, new sim.portrayal.simple.OvalPortrayal2D(Color.black));
     robotsPortrayal.setPortrayalForClass(Robot.class, new RectanglePortrayal2D(1)
		{
			public void draw(Object object, Graphics2D graphics, DrawInfo2D info)
			{
					paint = new Color(0, 0, 255);  //blue
								
			    super.draw(object, graphics, info);
			}
		});
     
     robotsPortrayal.setPortrayalForClass(Light.class, new OvalPortrayal2D(1){
    	 public void draw(Object object, Graphics2D graphics, DrawInfo2D info)
    	 {
    		 if(((Light)object).getSignal()==1)//green light
    			 paint = new Color(0,204,0);
    		 else
    			 paint = new Color(255,0,0);
    		
    		 super.draw(object, graphics, info);
    	 }
     });
     
     wallPortrayal.setField(((Robots)state).walls);
	 wallPortrayal.setMap(new sim.util.gui.SimpleColorMap(
				0,
				1,
				new Color(0,0,0,0),
				new Color(0,0,0,255)
				));		
                
	 streetPortrayal.setField(((Robots)state).streets);
	 streetPortrayal.setMap(new sim.util.gui.SimpleColorMap(
				0,
				2,
				new Color(0,0,0,0),
				new Color(0,0,0,255)
				));		
	 lstreetPortrayal.setField(((Robots)state).lstreets);
	 lstreetPortrayal.setMap(new sim.util.gui.SimpleColorMap(
				0,
				2,
				new Color(0,0,0,0),
				new Color(0,0,0,255)
				));	
	 obstaclePortrayal.setField(((Robots)state).obstacles);
	 obstaclePortrayal.setMap(new sim.util.gui.SimpleColorMap(
				0,
				2,
				new Color(0,0,0,0),
				new Color(0,0,0,255)
				));
     // reschedule the displayer
     display.reset();
             
     // redraw the display
     display.repaint();
     }
	
	 public void init(Controller c)
     {
     super.init(c);
     
     // Make the Display2D.  We'll have it display stuff later.
     display = new Display2D(600,600,this); 
     displayFrame = display.createFrame();
     c.registerFrame(displayFrame);   // register the frame so it appears in the "Display" list
     displayFrame.setVisible(true);

     display.setClipping(false);
     display.setBackdrop(Color.white);

     // attach the portrayals
     display.attach(robotsPortrayal,"Robots");
//     display.attach(wallPortrayal,"Wall");	
//     display.attach(streetPortrayal,"Street");
//     display.attach(lstreetPortrayal,"L-Street");
//     display.attach(obstaclePortrayal,"Obstacles");
     display.attach(systemPortrayal,"LSystem-street");
     
     Robots ls = (Robots)state;
     LSystemData.setVector(ls.l.code, "F");
     ls.l.seed = "F";
     
     ls.l.rules.add(new Rule((byte)'F', "F[+F]F[-F]F"));
     ((Console)c).getTabPane().removeTabAt(3);
     // add drawUI as tab
     DrawUI draw = new DrawUI(this);
     ((Console)c).getTabPane().addTab("Draw", new JScrollPane(draw));
     // add rulesUI as tab
     ((Console)c).getTabPane().addTab("Rules", new RuleUI(this, draw));
     }

}
