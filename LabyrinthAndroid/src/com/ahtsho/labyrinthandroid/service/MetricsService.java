package com.ahtsho.labyrinthandroid.service;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
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
	public static float LEFT_MARGIN_TOOL = 60;
	public static float TOP_MARGIN_TOOL = 50;
	public static final double ZOOM_FACTOR = 0.3;
	
	public static float xOffset = 0;
	public static float yOffset = 0;
	public static float topScreenPadding = 0;
	public static float leftScreenPadding = 0;
	
	
	
	static DisplayMetrics displaymetrics = new DisplayMetrics();
	static WindowManager wm = null;
	
	public static float getStartingX(){
		return xOffset - leftScreenPadding;
	}

	public static float getStartingY(){
		return yOffset - topScreenPadding;
	}
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
	
	public static char getDirectionFromPosition(float x0, float y0, float xf, float yf) {
		char direction = '*';
		// horizontal move delta x > delta y
		if (Math.abs((xf - x0)) > Math.abs((yf - y0))) {
			if (x0 < xf) {
				direction = Cell.EAST;
			} else if (x0 > xf) {
				direction = Cell.WEST;
			}
		} else if (Math.abs((xf - x0)) < Math.abs((yf - y0))) {
			// vertical move delta y > delta x
			if (y0 > yf) {
				direction = Cell.NORTH;
			} else if (y0 < yf) {
				direction = Cell.SOUTH;
			}
		}
		return direction;
	}

	public static synchronized boolean coordsBelongtoCell(Cell c, float x, float y) {
		float cellTopLeftX = getXFromCell(c, xOffset + leftScreenPadding);
		Log.d("belongs to player position", "cellTopLeftX=" + cellTopLeftX);
		float cellTopLeftY = getYFromCell(c, yOffset + topScreenPadding);
		Log.d("belongs to player position", "cellTopLefty=" + cellTopLeftX);
		float cellTopRightX = getXFromCell(c, xOffset) + CELL_WIDTH + leftScreenPadding;
		float cellBottomLeftY = getYFromCell(c, yOffset) + CELL_HEIGHT + topScreenPadding;

		if ((x < cellTopRightX) && (x > cellTopLeftX) && (y < cellBottomLeftY) && (y > cellTopLeftY)) {
			return true;
		}
		return false;
	}

	public static void shiftPlayerOnScreen(Cell position) {
		float playerX = getXOfCellCenter(position, 0);
		float playerY = getYOfCellCenter(position, 0);
		xOffset = 0;
		yOffset = 0;
		if (playerX >= screenWidth - CELL_WIDTH) {
			xOffset = playerX - (screenWidth - CELL_WIDTH);
		}
		if (playerY >= screenHeight - 1.5f * CELL_HEIGHT) {
			yOffset = playerY - (screenHeight - 1.5f * CELL_HEIGHT);
		}
		if (playerX < CELL_WIDTH) {
			xOffset = playerX - CELL_WIDTH;
		}
		if (playerY < CELL_HEIGHT) {
			yOffset = playerY - CELL_HEIGHT;
		}
	}
	
	public static void centerSmallLabyrinths(int labyrinthDimension) {
		if (labyrinthDimension* CELL_HEIGHT < screenHeight) {
			topScreenPadding = (screenHeight - labyrinthDimension * CELL_HEIGHT) / 2 - 60;
		}
		if (labyrinthDimension * CELL_WIDTH < screenWidth) {
			leftScreenPadding = (screenWidth - labyrinthDimension * CELL_WIDTH) / 2;
		}
	}

}
