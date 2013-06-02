package myrobot;

import java.awt.Color;

import javax.swing.JFrame;

import sim.app.particles3d.Particles3D;
import sim.display.Console;
import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.display3d.Display3D;
import sim.engine.SimState;
import sim.portrayal3d.continuous.ContinuousPortrayal3D;
import sim.portrayal3d.grid.ValueGrid2DPortrayal3D;
import sim.portrayal3d.simple.ConePortrayal3D;
import sim.portrayal3d.simple.SpherePortrayal3D;
import sim.portrayal3d.simple.TransformedPortrayal3D;
import sim.portrayal3d.simple.WireFrameBoxPortrayal3D;

public class Robot3DWithUI extends GUIState{

	public JFrame displayFrame; 
	public Display3D display;
	
	ValueGrid2DPortrayal3D obstaclePort = new ValueGrid2DPortrayal3D(); 
	 ContinuousPortrayal3D robotPortrayal = new ContinuousPortrayal3D();
	 WireFrameBoxPortrayal3D wireFramePortrayal; 
	
	public Robot3DWithUI(SimState state) {super(state);}
	public Robot3DWithUI() { super(new Robots(System.currentTimeMillis()));	}
	public static String getName(){ return "Robot simulation 3D"; }

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Robot3DWithUI r = new Robot3DWithUI();
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
	 
	public void setupPortrayals() {
		robotPortrayal.setField(((Robots)state).robots);
		 
		 TransformedPortrayal3D p = new TransformedPortrayal3D(new SpherePortrayal3D(Color.green));
	     p.rotateX(90.0);
	     robotPortrayal.setPortrayalForAll(p);
	     
	     
	     display.reset();        
	     display.createSceneGraph();
	}
	
	 public void init(Controller c)
     {
     super.init(c);
     
     wireFramePortrayal =  new WireFrameBoxPortrayal3D(0,0,0, 50, 50, 50);
     
//     Robots ro = (Robots)state;
//     robotPortrayal.setField(ro.robots);
     
     
     display = new Display3D(600,600,this); // at 400x400, we've got 4x4 per array position
     displayFrame = display.createFrame();
     c.registerFrame(displayFrame);   // register the frame so it appears in the "Display" list
     displayFrame.setVisible(true);

     display.translate(-25, 
             -25, 
             -25); 
     display.scale(1.0/Math.max(100,100));
     // specify the backdrop color  -- what gets painted behind the displays
//     display.setBackdrop(Color.white);

     // attach the portrayals
     display.attach(robotPortrayal,"Robots");
     display.attach(wireFramePortrayal,"Wire Frame"); 
//     display.attach(wallPortrayal,"Wall");	
////     display.attach(streetPortrayal,"Street");
//     display.attach(obstaclePortrayal,"Obstacles");
     }

}
