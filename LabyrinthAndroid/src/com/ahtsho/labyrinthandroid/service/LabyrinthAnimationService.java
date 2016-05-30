package com.ahtsho.labyrinthandroid.service;


public class LabyrinthAnimationService extends AnimationService{

//	public static float xAnimiate = 0;
//	public static float yAnimiate = 0;
	public static float zoom = 0;
	private static boolean animate = false;
	
	public static void animate(char direction) {
		if (animate) {
//			if (direction == Cell.NORTH)
//				yAnimiate--;
//			if (direction == Cell.SOUTH)
//				yAnimiate++;
//			if (direction == Cell.WEST)
//				xAnimiate--;
//			if (direction == Cell.EAST)
//				xAnimiate++;
			if(direction==IN){
//				zoom = (float) (zoom + MetricsService.ZOOM_FACTOR);
				zoom++;
			} else if(direction==AnimationService.OUT){
//				zoom = (float) (zoom - MetricsService.ZOOM_FACTOR);
				zoom--;
			}
			
		}
	}
	
	
	public static void reset() {
		animate = false;
//		xAnimiate = 0;
//		yAnimiate = 0;
		zoom = 0;
	}

	public static boolean animationEnded() {
		if (zoom >= MetricsService.PLAYER_MAX_ZOOM)
			return true;
		return false;
	}
	public static void startAnimation() {
		animate = true;		
	}
	

}
