package com.ahtsho.labyrinthandroid.service;

abstract class AnimationService {
	public static char OUT = 'O';
	public static char IN = 'I';
	
	public static void animate(char direction) {};
	
	public static void reset(){};

	public static void startAnimation(){};

	public static boolean animationEnded(){
		return false;
	}	
}
