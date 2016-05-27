package com.ahtsho.labyrinthandroid.service;

import infrastructure.Cell;

public class HandAnimationService extends AnimationService {
	private static final float HAND_ANIMIATION_STEP = 3;
	public static float xAnimiateHand = 0;
	public static float yAnimiateHand = 0;
	private static boolean animateHand = false;
	
	public static void animate(char direction) {
		if (animateHand) {
			if (direction == Cell.NORTH)
				yAnimiateHand -=HAND_ANIMIATION_STEP;
			if (direction == Cell.SOUTH)
				yAnimiateHand +=HAND_ANIMIATION_STEP;
			if (direction == Cell.WEST)
				xAnimiateHand -=HAND_ANIMIATION_STEP;
			if (direction == Cell.EAST)
				xAnimiateHand +=HAND_ANIMIATION_STEP;
		}
	}
	public static void reset() {
		animateHand = false;
		xAnimiateHand = 0;
		yAnimiateHand = 0;
	}
	
	
	public static boolean animationEnded() {
		if (xAnimiateHand >= MetricsService.CELL_WIDTH || yAnimiateHand >= MetricsService.CELL_HEIGHT
				|| xAnimiateHand <= -MetricsService.CELL_WIDTH
				|| yAnimiateHand <= -MetricsService.CELL_HEIGHT)
			return true;
		return false;
	}
	
	
	public static void starthandAnimation() {
		animateHand = true;		
	}
}
