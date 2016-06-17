package com.ahtsho.labyrinthandroid.service;

import model.infrastructure.Cell;

public class PlayerAnimationService extends AnimationService {
	public static float xAnimiate = 0;
	public static float yAnimiate = 0;
	public static float zoom = 0;
	private static boolean animate = false;

	public static class Type {
		public static String SLIDE = "slide";
		public static String ZOOM = "zoom";
	}

	public static class Animate {

		static void slideNorth() {
			yAnimiate--;
		}

		static void slideSouth() {
			yAnimiate++;
		}

		static void slideEast() {
			xAnimiate++;
		}

		static void slideWest() {
			xAnimiate--;
		}

	}

	public static void slide(char direction) {
		if (animate) {
			if (direction == Cell.NORTH)
				Animate.slideNorth();
			if (direction == Cell.SOUTH)
				Animate.slideSouth();
			if (direction == Cell.WEST)
				Animate.slideWest();
			if (direction == Cell.EAST)
				Animate.slideEast();
		}
	}

	public static void zoom(char direction) {
		if (animate) {
			resetSlide();
			if (direction == IN) {
				zoom+=MetricsService.ZOOM_FACTOR;
			} else if (direction == OUT) {
				zoom-=MetricsService.ZOOM_FACTOR;
			}
		}
	}

	public static void resetSlide(){
		xAnimiate = 0;
		yAnimiate = 0;
	}
	public static void reset(String animation) {
		animate = false;
		if (animation.equals(Type.SLIDE)) {
			xAnimiate = 0;
			yAnimiate = 0;
			zoom = 0;
		} else if (animation.equals(Type.ZOOM)){
			zoom = 0;
			xAnimiate = 0;
			yAnimiate = 0;
		}
	}

	public static boolean animationEnded(String type) {
		if (type.equals(Type.ZOOM)) {
			if (zoom >= MetricsService.PLAYER_MAX_ZOOM || zoom <= -MetricsService.PLAYER_MAX_ZOOM) {
				return true;
			}
		} else if (type.equals(Type.SLIDE)) {
			if (xAnimiate >= MetricsService.PLAYER_EXIT_ANIMATION_X_AXIS_MAX_VALUE
					|| yAnimiate >= MetricsService.PLAYER_EXIT_ANIMATION_Y_AXIS_MAX_VALUE
					|| xAnimiate <= -MetricsService.PLAYER_EXIT_ANIMATION_X_AXIS_MAX_VALUE
					|| yAnimiate <= -MetricsService.PLAYER_EXIT_ANIMATION_X_AXIS_MAX_VALUE) {
				return true;
			}
		}
		return false;
	}

	public static void startAnimation() {
		animate = true;
	}
}
