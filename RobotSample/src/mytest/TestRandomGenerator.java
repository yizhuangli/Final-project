package mytest;

import ec.util.MersenneTwisterFast;

public class TestRandomGenerator {
	MersenneTwisterFast random = new MersenneTwisterFast(1);
	
	public void print(){
		System.out.println(random.nextInt(12));
		System.out.println("hello");
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		TestRandomGenerator trg = new TestRandomGenerator();
		trg.print();
		
		System.out.println(100/3);
	}

}
