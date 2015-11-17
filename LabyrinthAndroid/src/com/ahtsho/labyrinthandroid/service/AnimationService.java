package com.ahtsho.labyrinthandroid.service;

import infrastructure.Cell;

public class AnimationService {
	public static float xAnimiate = 0;
	public static float yAnimiate = 0;
	public static float zoom = 0;
	private static boolean animate = false;

	public static void animate(char direction) {
		if (animate) {
			if (direction == Cell.NORTH)
				yAnimiate--;
			if (direction == Cell.SOUTH)
				yAnimiate++;
			if (direction == Cell.WEST)
				xAnimiate--;
			if (direction == Cell.EAST)
				xAnimiate++;
			zoom = (float) (zoom - MetricsService.ZOOM_FACTOR);
		}
	}

	public static void reset() {
		animate = false;
		xAnimiate = 0;
		yAnimiate = 0;
		zoom = 0;
	}

	public static boolean animationEnded() {
		if (xAnimiate >= MetricsService.PLAYER_EXIT_ANIMATION_X_AXIS_MAX_VALUE || yAnimiate >= MetricsService.PLAYER_EXIT_ANIMATION_Y_AXIS_MAX_VALUE
				|| xAnimiate <= -MetricsService.PLAYER_EXIT_ANIMATION_X_AXIS_MAX_VALUE
				|| yAnimiate <= -MetricsService.PLAYER_EXIT_ANIMATION_X_AXIS_MAX_VALUE)
			return true;
		return false;
	}

	public static void startAnimation() {
		animate = true;		
	}
}
