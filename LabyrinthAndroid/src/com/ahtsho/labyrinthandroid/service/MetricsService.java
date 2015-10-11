package com.ahtsho.labyrinthandroid.service;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import infrastructure.Cell;

public class MetricsService {
	public static final int PLAYER_EXIT_ANIMATION_Y_AXIS_MAX_VALUE = 600;
	public static final int PLAYER_EXIT_ANIMATION_X_AXIS_MAX_VALUE = 600;
	public static final int WALL_THICKNESS_BOTTOM = 25;
	public static final int WALL_THICKNESS_RIGHT = 25;
	public static final int WALL_THICKNESS_TOP = 25;
	public static final int WALL_THICKNESS_LEFT = 25;
	public static float screenWidth = 0;
	public static float screenHeight = 0;
	public static float CELL_WIDTH = 300;// make di
	public static float CELL_HEIGHT = 300;
	public static float LEFT_MARGIN = 30;
	public static float TOP_MARGIN = 0;
	static DisplayMetrics displaymetrics = new DisplayMetrics();
	static WindowManager wm = null;

	public static void initializeCellDimension(Context context) {
		if (wm == null) {
			wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
			wm.getDefaultDisplay().getMetrics(displaymetrics);
		}
		screenHeight = displaymetrics.heightPixels;
		screenWidth = displaymetrics.widthPixels;
		CELL_WIDTH = screenWidth / 3;
		CELL_HEIGHT = CELL_WIDTH;
	}

	public static float getRadiusOfCircle() {
		return CELL_WIDTH * 3 / 8;
	}

	public static float getYOfCellCenter(Cell cell, float offset) {
		return getYFromCell(cell, offset) + CELL_HEIGHT / 2;
	}

	public static float getXOfCellCenter(Cell cell, float offset) {
		return getXFromCell(cell, offset) + CELL_WIDTH / 2;
	}

	public static float getXFromNextCell(Cell cell, float offset) {
		return (cell.getCol() + 1) * CELL_WIDTH - offset;
	}

	public static float getYFormNextCell(Cell cell, float offset) {
		return (cell.getRow() + 1) * CELL_HEIGHT - offset;
	}

	public static float getYFromCell(Cell cell, float offset) {
		return cell.getRow() * CELL_HEIGHT - offset;
	}

	public static float getXFromCell(Cell cell, float offset) {
		return cell.getCol() * CELL_WIDTH - offset;
	}

}
