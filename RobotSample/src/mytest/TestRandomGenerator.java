package mytest;

import ec.util.MersenneTwisterFast;

public class TestRandomGenerator {
	MersenneTwisterFast random = new MersenneTwisterFast();
	
	public void print(){
		long time = System.currentTimeMillis();
		System.out.println("time: "+time);
		
		long seed = Long.valueOf("1375093530000");
		random.setSeed(time);
		int rand = random.nextInt();
		int length = String.valueOf(rand).length();
		int length1 = String.valueOf(Math.abs(rand)).length();
		System.out.println(rand);
		System.out.println(Math.abs(rand));
		System.out.println(length);
		System.out.println(length1);
		
		String a = String.valueOf(rand).substring(0,1);
		System.out.println(a);
//		System.out.println(random.nextGaussian());
//		System.out.println(random.nextChar());
//		System.out.println(random.nextInt(12));
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		TestRandomGenerator trg = new TestRandomGenerator();
		
		trg.print();
		
	}

}
