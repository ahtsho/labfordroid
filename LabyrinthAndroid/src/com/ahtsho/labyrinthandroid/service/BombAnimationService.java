package com.ahtsho.labyrinthandroid.service;

public class BombAnimationService extends AnimationService {

	public static float xAnimiate = 0;
	public static float yAnimiate = 0;
	public static float zoom = 0;
	private static boolean animate = false;
	public static class Type {
		public static String EXPLOSION = "explosion";
	}
	public static void explode() {
		if (animate) {
			zoom+=MetricsService.EXPLOSION_SPEED;
			System.out.println("zoom "+zoom);
		}
		
	}
	public static boolean animationEnded(String eXPLOSION) {
		if (zoom > MetricsService.EXPLOSTION_ANIMATION_MAX_VALUE) {
			return true;
		}
		return false;
	}
	public static void reset(String eXPLOSION) {
		animate = false;
		xAnimiate = 0;
		yAnimiate = 0;
		zoom = 0;		
	}
	public static void startAnimation() {
		animate = true;
	}

}
