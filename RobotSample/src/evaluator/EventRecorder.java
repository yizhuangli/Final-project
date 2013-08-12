package evaluator;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import generator.Robots;

/**
 * record the event of each generated situation
 * @author Zhuangli Yi
 *
 */
public class EventRecorder {

	Robots sim;
	public EventRecorder(Robots robots) {
		sim = robots;
	}
	
	
	public void setLogFile(){
		try {
			OutputStream outputstream = new FileOutputStream("log.txt", true);
			PrintStream printstream = new PrintStream(outputstream);
			printstream.print(sim.seed()+","+sim.numLights+","+sim.numRobots+","+sim.l.code.length+","+sim.intersection.numObjs+",");
			
			for(int i=0;i<sim.l.code.length;i++){
				printstream.print(new String(new byte[]{sim.l.code.b[i]}));
			}
			
			printstream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		
	}


	

}
