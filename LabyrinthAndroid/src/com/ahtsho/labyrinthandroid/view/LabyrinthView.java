package com.ahtsho.labyrinthandroid.view;

import java.util.ArrayList;

import core.Cell;
import core.Labyrinth;
import core.Levels;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

public class LabyrinthView extends View {
private String LOG_TAG = "LabyrinthView";
	private Labyrinth labyrinth;
	private Paint paint;
	private Paint paintOpen;
	private Paint playerPaint;
	private Paint textPaint;
	private int level = 1;
	private float screenWidth = 0;
	private float screenHeight = 0;
	private static int CELL_WIDTH = 200;
	private static int CELL_HEIGHT = 200;
	private static int MARGIN = 50;
	
	private boolean touched = false;

	private boolean startingFromPlayerCell = false;
	private float xdown = 0;
	private float ydown = 0;
	private float xup = 0;
	private float yup = 0;

//	private float countDraw=0;
	private boolean sameLevel = true;

	private float xOffset = 0;
	private float yOffset = 0;

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawColor(Color.BLACK);
		ArrayList<Cell> cells = labyrinth.getCells();
		for (int i = 0; i < cells.size(); i++) {
			shiftPlayerOnScreen();
			drawCell(canvas, cells.get(i), xOffset, yOffset, true);
		}
//		countDraw++;
		invalidate();

	}

	private void shiftPlayerOnScreen() {
		float playerX = getXOfCellCenter(labyrinth.getPlayer().getPosition(), 0);
		float playerY = getYOfCellCenter(labyrinth.getPlayer().getPosition(), 0);
		String caso="";
		if (playerX + CELL_WIDTH / 2 > screenWidth) {
			xOffset = (playerX + CELL_WIDTH / 2) - screenWidth;
			yOffset = 0;
			caso="E";
		}
		if (playerY + CELL_HEIGHT / 2 > screenHeight) {
			xOffset = 0;
			yOffset = (playerY + CELL_HEIGHT / 2) - screenHeight;
			caso="S";
		}
		if (playerX - CELL_WIDTH / 2 < 0) {
			xOffset = - (playerX-CELL_WIDTH / 2);
			yOffset = 0;
			caso="C";
		}
		if (playerY - CELL_HEIGHT / 2 < 0) {
			xOffset = 0;
			yOffset = -(playerY-CELL_HEIGHT / 2);
			caso="D";
		}

		if (playerX - CELL_WIDTH / 2 >= 0
				&& playerX + CELL_WIDTH / 2 <= screenWidth
				&& playerY - CELL_HEIGHT / 2 >= 0
				&& playerY + CELL_HEIGHT / 2 <= screenHeight) {
			xOffset = 0;
			yOffset = 0;
			caso ="E";
		}
		Log.d(LOG_TAG, "playerX="+playerX+", playerY"+playerY+", screenHeight="+screenHeight+", yOffset="+yOffset+", case="+caso);
	}

	private void drawCell(Canvas canvas, Cell cell, float xOffset,
			float yOffset, boolean showCoords) {
		drawWestWall(canvas, cell, xOffset, yOffset, showCoords);
		drawNorthWall(canvas, cell, xOffset, yOffset, showCoords);
		drawSouthWall(canvas, cell, xOffset, yOffset, showCoords);
		drawEastWall(canvas, cell, xOffset, yOffset, showCoords);

		if (labyrinth.getPlayer().getPosition().equals(cell)) {
			canvas.drawCircle(getXOfCellCenter(cell, xOffset),
					getYOfCellCenter(cell, yOffset), getRadiusOfCircle(),
					playerPaint);
			if (showCoords) {
				canvas.drawText(getXOfCellCenter(cell, xOffset) + ", "
						+ getYOfCellCenter(cell, yOffset),
						getXOfCellCenter(cell, 70+xOffset),
						getYOfCellCenter(cell, -35+yOffset), textPaint);
			}
		}
		canvas.drawText("[" + cell.getRow() + "," + cell.getCol() + "]",
				getXOfCellCenter(cell, 25+xOffset), getYOfCellCenter(cell, -5+yOffset),
				textPaint);
//		canvas.drawText("sw="+screenWidth+",sh="+screenHeight, screenWidth/3, 1000, textPaint);

	}

	private void drawEastWall(Canvas canvas, Cell cell, float xOffset,
			float yOffset, boolean showCoords) {
		if (cell.isEast()) {
			canvas.drawLine(getXFromNextCell(cell, xOffset),
					getYFromCell(cell, yOffset),
					getXFromNextCell(cell, xOffset),
					getYFormNextCell(cell, yOffset), paint);// east
		} else {
			canvas.drawLine(getXFromNextCell(cell, xOffset),
					getYFromCell(cell, yOffset),
					getXFromNextCell(cell, xOffset),
					getYFormNextCell(cell, yOffset), paintOpen);
		}
		if (showCoords) {
		}
	}

	private void drawSouthWall(Canvas canvas, Cell cell, float xOffset,
			float yOffset, boolean showCoords) {
		if (cell.isSouth()) {
			canvas.drawLine(getXFromCell(cell, xOffset),
					getYFormNextCell(cell, yOffset),
					getXFromNextCell(cell, xOffset),
					getYFormNextCell(cell, yOffset), paint);// south
		} else {
			canvas.drawLine(getXFromCell(cell, xOffset),
					getYFormNextCell(cell, yOffset),
					getXFromNextCell(cell, xOffset),
					getYFormNextCell(cell, yOffset), paintOpen);
		}
		if (showCoords) {
		}
	}

	private void drawNorthWall(Canvas canvas, Cell cell, float xOffset,
			float yOffset, boolean showCoords) {
		if (cell.isNorth()) {
			canvas.drawLine(getXFromCell(cell, xOffset),
					getYFromCell(cell, yOffset),
					getXFromNextCell(cell, xOffset),
					getYFromCell(cell, yOffset), paint);// north
		} else {
			canvas.drawLine(getXFromCell(cell, xOffset),
					getYFromCell(cell, yOffset),
					getXFromNextCell(cell, xOffset),
					getYFromCell(cell, yOffset), paintOpen);
		}
		if (showCoords) {
			// canvas.drawText("N(" + getXFromCell(cell, xOffset) + ","
			// + getYFromCell(cell, yOffset) + ")",
			// getXFromCell(cell, -10), getYFromCell(cell, -30),
			// textPaint);
			// canvas.drawText("(" + getXFromCell(cell, xOffset) + ","
			// + getYFormNextCell(cell, yOffset) + ")",
			// getXFromCell(cell, -10), getYFormNextCell(cell, 20),
			// textPaint);
		}
	}

	private void drawWestWall(Canvas canvas, Cell cell, float xOffset,
			float yOffset, boolean showCoords) {
		if (cell.isWest()) {
			canvas.drawLine(getXFromCell(cell, xOffset),
					getYFromCell(cell, yOffset), getXFromCell(cell, xOffset),
					getYFormNextCell(cell, yOffset), paint);

		} else {
			canvas.drawLine(getXFromCell(cell, xOffset),
					getYFromCell(cell, yOffset), getXFromCell(cell, xOffset),
					getYFormNextCell(cell, yOffset), paintOpen);
		}
		if (showCoords) {
			canvas.drawText("W(" + getXFromCell(cell, xOffset) + ","
					+ getYFromCell(cell, yOffset) + ")",
					getXFromCell(cell, -10+xOffset), getYFromCell(cell, -30+yOffset), textPaint);
			canvas.drawText("(" + getXFromCell(cell, xOffset) + ","
					+ getYFormNextCell(cell, yOffset) + ")",
					getXFromCell(cell, -10+xOffset), getYFormNextCell(cell, 20+yOffset),
					textPaint);
		}
	}

	private int getRadiusOfCircle() {
		return CELL_WIDTH * 3 / 8;
	}

	private float getYOfCellCenter(Cell cell, float offset) {
		return getYFromCell(cell, offset) + CELL_HEIGHT / 2;
	}

	private float getXOfCellCenter(Cell cell, float offset) {
		return getXFromCell(cell, offset) + CELL_WIDTH / 2;
	}

	private float getXFromNextCell(Cell cell, float offset) {
		return (cell.getCol() + 1) * CELL_WIDTH - offset;
	}

	private float getYFormNextCell(Cell cell, float offset) {
		return (cell.getRow() + 1) * CELL_HEIGHT - offset;
	}

	private float getYFromCell(Cell cell, float offset) {
		return cell.getRow() * CELL_HEIGHT - offset;
	}

	private float getXFromCell(Cell cell, float offset) {
		return cell.getCol() * CELL_WIDTH - offset;
	}

	public LabyrinthView(Context context, Paint aPaint, Paint anotherPaint,
			Paint aPlayerPaint, Paint aTextPaint, Labyrinth aLab) {
		super(context);
		paint = aPaint;
		paintOpen = anotherPaint;
		playerPaint = aPlayerPaint;
		labyrinth = aLab;
		textPaint = aTextPaint;
		DisplayMetrics displaymetrics = new DisplayMetrics();
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(displaymetrics);
		screenHeight = displaymetrics.heightPixels;
		screenWidth = displaymetrics.widthPixels;
		// WindowManager wm = (WindowManager) context
		// .getSystemService(Context.WINDOW_SERVICE);
		// Display display = wm.getDefaultDisplay();
		// screenWidth = display.getWidth();
		// screenHeight = display.getHeight();
		Log.d("DEBUG", "windowWidth=" + screenWidth);
		Log.d("DEBUG", "windowHeight=" + screenHeight);
	}

	private Cell getCellFromPosition(float x, float y) {
		int col, row;
		Cell cell = null;
		col = Math.round(x / CELL_WIDTH);
		row = Math.round(y / CELL_HEIGHT);

		ArrayList<Cell> cells = labyrinth.getCells();
		for (Cell c : cells) {
			if (c.getCol() == col && c.getRow() == row) {
				cell = c;
			}
		}
		return cell;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);

		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			xdown = event.getX();
			ydown = event.getY();
			// Log.d("DEBUG", "Down: x=" + event.getX() + ", y=" +
			// event.getY());
			if (belongstoPlayerPosition(event.getX(), event.getY())) {
				startingFromPlayerCell = true;
				return true;
			}
			startingFromPlayerCell = false;
			return false;
		}
//		if (event.getAction() == MotionEvent.ACTION_MOVE) {
//			x = x + event.getX();
//			y = y + event.getY();
//			countx++;
//			county++;
//
//			// Log.d("DEBUG", "Move:x=" + event.getX() + ", x media="
//			// + (x / countx) + ",y=" + event.getY() + ", y media ="
//			// + (y / county));
//			return true;
//		}

		if (event.getAction() == MotionEvent.ACTION_UP) {
			// Log.d("DEBUG", "Up: x=" + event.getX() + ", y=" + event.getY());
			xup = event.getX();
			yup = event.getY();
			if (startingFromPlayerCell) {
				try {
					sameLevel = labyrinth.move(labyrinth.getPlayer(),
							getDirectionFromPosition(xdown, ydown, xup, yup));
					if (!sameLevel) {
						level = Levels.next(level);
						CharSequence msg = "LEVEL "+level;
						Toast toast = Toast.makeText(this.getContext(),msg , Toast.LENGTH_LONG);
						toast.show();
						labyrinth = Levels.genLabyrinth(level + 2,
								labyrinth.getPlayer());
						// // this.setVisibility(INVISIBLE);
						// ViewGroup parentView = (ViewGroup) this.getParent();
						// parentView.removeView(this);

					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Log.e("DEBUG", e.getMessage());
				}
				return true;
			}
			return false;
		}
		return false;
	}

	private char getDirectionFromPosition(float x0, float y0, float xf, float yf) {
		char direction = '*';
		// horizontal move delta x > delta y
		if (Math.abs((xf - x0)) > Math.abs((yf - y0))) {
			if (x0 < xf) {
				direction = 'E';
				// direction = Cell.EAST;
			} else if (x0 > xf) {
				direction = 'W';
				// direction = Cell.WEST;
			}
		} else if (Math.abs((xf - x0)) < Math.abs((yf - y0))) {
			// vertical move delta y > delta x
			if (y0 > yf) {
				direction = 'N';
				// direction = Cell.NORTH;
			} else if (y0 < yf) {
				direction = 'S';
				// direction = Cell.SOUTH;
			}
		}
		return direction;
	}

	private boolean belongstoPlayerPosition(float x, float y) {

		Cell c = labyrinth.getPlayer().getPosition();
		float cellTopLeftX = getXFromCell(c, xOffset);
		float cellTopLeftY = getYFromCell(c, yOffset);
		float cellTopRightX = getXFromCell(c, xOffset) + CELL_WIDTH;
		float cellBottomLeftY = getYFromCell(c, yOffset) + CELL_HEIGHT;

		if ((x < cellTopRightX) && (x > cellTopLeftX) && (y < cellBottomLeftY)
				&& (y > cellTopLeftY)) {
			return true;
		}
		return false;
	}

}
