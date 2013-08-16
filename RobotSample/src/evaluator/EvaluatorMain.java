package evaluator;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import clustering.implementations.UPGMATreeConstructor;

public class EvaluatorMain {
	
	ArrayList<String> log = new ArrayList<String>();
	Map<String, LogData> logmap = new TreeMap<String,LogData>();
	ArrayList<LogData> logdata = new ArrayList<LogData>();
	double[][] dist;
	
	/**
	 * load the event log
	 */
	private void loadLogFile(){
		
		try {
			InputStream inputstream = new FileInputStream("log.txt");
			BufferedReader rd = new BufferedReader(new InputStreamReader(inputstream));
			String s = null;
			while((s=rd.readLine())!=null){
				log.add(s);
			}
			rd.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	/**
	 * read data from the log
	 */
	private void readData() {
		for(int i=0;i<log.size();i++){
			String seed = log.get(i).split(",")[0];
			String numLight = log.get(i).split(",")[1];
			String numRobot = log.get(i).split(",")[2];
			String ruleLength = log.get(i).split(",")[3];
			String numIntersection = log.get(i).split(",")[4];
			String rule = log.get(i).split(",")[5];
			
			int forward = 0,leftturn = 0,rightturn = 0;
			for(int j=0;j<Integer.valueOf(ruleLength);j++){
//				System.out.println(rule.charAt(j));
				if(String.valueOf(rule.charAt(j)).equalsIgnoreCase("F"))
					forward++;
				else if(String.valueOf(rule.charAt(j)).equals("+"))
					leftturn++;
				else if(String.valueOf(rule.charAt(j)).equals("-"))
					rightturn++;	
			}
			
			System.out.println(forward+","+leftturn+","+rightturn);
			
			
			LogData log = new LogData(seed, Integer.valueOf(numLight), Integer.valueOf(numRobot), Integer.valueOf(ruleLength),Integer.valueOf(numIntersection),forward,leftturn,rightturn,rule);
			logdata.add(log);
		}
		
		
	}
	
	private void calculate() {
		System.out.println("log size: "+logdata.size());
		dist = new double[logdata.size()][logdata.size()];
		for(int i=0;i<logdata.size();i++){
			for(int j=0;j<logdata.size();j++){
				if(j!=i)
					dist[i][j] = this.calEuclideanDist(logdata.get(i), logdata.get(j));
				else
					dist[i][j] = -1;
			}
		}
		
		UPGMATreeConstructor upgma = new UPGMATreeConstructor();
		try {
			String  XMLTree = upgma.ConstructXMLTree(dist);
			System.out.println(XMLTree);
			WriteXml(XMLTree);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	private void WriteXml(String xMLTree) {
		// TODO Auto-generated method stub
		BufferedWriter out;
		try {
			out = new BufferedWriter(new FileWriter("tree.xml"));
			out.write(xMLTree);
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
   
	}

	private int calEuclideanDist(LogData a,LogData b){
		
		System.out.println(a.getForwardNum()+","+b.getForwardNum());
		int distance = (int) Math.sqrt( Math.pow(a.getForwardNum()-b.getForwardNum(), 2)+Math.pow(a.getIntersectionNum()-b.getIntersectionNum(), 2)+Math.pow(a.getLeftTurnNum()-b.getLeftTurnNum(), 2)+Math.pow(a.getRightTurnNum()-b.getRightTurnNum(), 2)+Math.pow(a.getRulelength()-b.getRulelength(), 2));
		return distance;
		
	}
	
	private void showDistTable() {
		for(int i=0;i<logdata.size();i++){
			for(int j=0;j<logdata.size();j++){
				System.out.print(dist[i][j]+"\t");
			}
			System.out.println();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		EvaluatorMain em = new EvaluatorMain();
		em.loadLogFile();
		em.readData();
		em.calculate();
		em.showDistTable();
		

	}

	

	

	

}
