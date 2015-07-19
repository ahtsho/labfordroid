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
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

public class LabyrinthView extends View {

	private Labyrinth labyrinth;
	private Paint paint;
	private Paint paintOpen;
	private Paint playerPaint;
	private int level = 1;
	private float screenWidth = 0;
	private float screenHeight = 0;
	private static int CELL_WIDTH = 200;
	private static int CELL_HEIGHT = 200;
	private static int LEFT_PADDING = 50;
	private static int TOP_PADDING = 100;
	private boolean touched = false;

	private boolean startingFromPlayerCell = false;
	private float xdown = 0;
	private float ydown = 0;
	private float xup = 0;
	private float yup = 0;

	private float x = 0;
	private int countx = 0;
	private float y = 0;
	private int county = 0;
	private boolean sameLevel = true;

	private float xOffset = 0;
	private float yOffset = 0;
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		float playerX = getXFromCell(labyrinth.getPlayer().getPosition(), 0);
		float playerY = getYFromCell(labyrinth.getPlayer().getPosition(), 0);
		
		canvas.drawColor(Color.BLACK);
		ArrayList<Cell> cells = labyrinth.getCells();
		for (int i = 0; i < cells.size(); i++) {
			if (playerX+CELL_WIDTH/2 > screenWidth) {
//				Log.d("DEBUG", "player x position =" + playerX + " screen w="
//						+ screenWidth + "--- shift left "
//						+ (playerX - screenWidth));
				xOffset =  (playerX +CELL_WIDTH/2 )- screenWidth;
				yOffset = 0;
				drawCell(canvas, cells.get(i),xOffset, yOffset);
			}
			if (playerY+CELL_HEIGHT/2 > screenHeight) {
//				Log.d("DEBUG", "player y position =" + playerY + " screen h="
//						+ screenHeight + "--- shift up "
//						+ (playerY - screenHeight));
				xOffset = 0;
				yOffset = (playerY+CELL_HEIGHT/2 )- screenHeight;
				drawCell(canvas, cells.get(i), xOffset, yOffset);
			}
			if (playerX - CELL_WIDTH/2 < 0) {
//				Log.d("DEBUG", "player x position =" + playerX + " screen w="
//						+ screenWidth + "--- shift right " + (-playerX));
				xOffset = -(CELL_WIDTH/2-playerX);
				yOffset = 0;
				drawCell(canvas, cells.get(i), xOffset, yOffset);
			}
			if (playerY -CELL_HEIGHT/2<0) {
//				Log.d("DEBUG", "player y position =" + playerY + " screen h="
//						+ screenHeight + "--- shift down " + (-playerY));
				xOffset =0;
				yOffset = -(CELL_HEIGHT/2-playerY);
				drawCell(canvas, cells.get(i), xOffset, yOffset);
			}

			if (playerX-CELL_WIDTH/2 >=0  
					&& playerX+CELL_WIDTH/2 <= screenWidth 
					&& playerY-CELL_HEIGHT/2 >=0 
					&& playerY+CELL_HEIGHT/2 <= screenHeight) {
//				Log.d("DEBUG", "player on screen");
				drawCell(canvas, cells.get(i), 0, 0);
			}
			// canvas.drawRect(cell, paint);
		}

		invalidate();

	}

	private void drawCell(Canvas canvas, Cell cell, float xOffset, float yOffset) {
		if (cell.isWest()) {
			canvas.drawLine(getXFromCell(cell, xOffset),
					getYFromCell(cell, yOffset), getXFromCell(cell, xOffset),
					getYFormNextCell(cell, yOffset), paint);// west
		} else {
			canvas.drawLine(getXFromCell(cell, xOffset),
					getYFromCell(cell, yOffset), getXFromCell(cell, xOffset),
					getYFormNextCell(cell, yOffset), paintOpen);
		}
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
		if (labyrinth.getPlayer().getPosition().equals(cell)) {
			Log.d("DEBUG","player should be in ["+getXFromCell(cell, xOffset)+", "+getYFromCell(cell,yOffset)+"]   Drawing player=["+getXOfCellCenter(cell, xOffset)+","+getYOfCellCenter(cell, yOffset)+"]");
			canvas.drawCircle(getXOfCellCenter(cell, xOffset),
					getYOfCellCenter(cell, yOffset), getRadiusOfCircle(),
					playerPaint);
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
			Paint aPlayerPaint, Labyrinth aLab) {
		super(context);
		paint = aPaint;
		paintOpen = anotherPaint;
		playerPaint = aPlayerPaint;
		labyrinth = aLab;

		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		screenWidth = display.getWidth();
		screenHeight = display.getHeight();
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
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			x = x + event.getX();
			y = y + event.getY();
			countx++;
			county++;

			// Log.d("DEBUG", "Move:x=" + event.getX() + ", x media="
			// + (x / countx) + ",y=" + event.getY() + ", y media ="
			// + (y / county));
			return true;
		}

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
