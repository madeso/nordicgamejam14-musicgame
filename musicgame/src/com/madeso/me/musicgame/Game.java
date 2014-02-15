package com.madeso.me.musicgame;

public class Game {
	
	int[] level = new int[Constants.BANKCOUNT];

	public void onhit(int index) {
		if( index >= Constants.BANKCOUNT ) {
			System.out.println("Error - bad programmer");
			return;
		}
		System.out.print("Hit bank ");
		System.out.println(index);
	}

	public void update(float dt) {
		// TODO Auto-generated method stub
		
	}
}
