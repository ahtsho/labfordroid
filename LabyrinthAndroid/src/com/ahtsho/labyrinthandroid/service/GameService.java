package com.ahtsho.labyrinthandroid.service;

public class GameService {

	private static int level = 1;
	public static boolean isTutorial() {
		if(level == 1) return true;
		return false;
	}
	public static void setLevel(int next) {
		level = next;
	}
	public static int getLevel() {
		return level;
	}
}
