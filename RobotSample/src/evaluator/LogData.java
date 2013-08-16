package evaluator;

public class LogData {
	
	private String seed,rule;
	private int light,robot,rulelength,intersection,forward,left,right;

	public LogData(String seed, int light, int robot, int rulelength, int intersection,int forward, int left, int right, String rule){
		this.seed = seed;
		this.light = light;
		this.robot = robot;
		this.rulelength = rulelength;
		this.intersection = intersection;
		this.rule = rule;
		this.forward = forward;
		this.left = left;
		this.right = right;
	}
	
	public String getSeedNum(){
		return seed;
	}
	public int getLightNum(){
		return light;
	}
	public int getRulelength(){
		return rulelength;
	}
	public int getIntersectionNum(){
		return intersection;
	}
	public int getForwardNum(){
		return forward;
	}
	public int getLeftTurnNum(){
		return left;
	}
	public int getRightTurnNum(){
		return right;
	}
	public String getRule(){
		return rule;
	}

}
