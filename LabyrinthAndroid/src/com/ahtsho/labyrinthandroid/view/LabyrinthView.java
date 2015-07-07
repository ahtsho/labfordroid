package com.ahtsho.labyrinthandroid.view;

import java.util.ArrayList;

import core.Cell;
import core.Labyrinth;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class LabyrinthView extends View {

	private Labyrinth labyrinth;
	private Paint paint;
	private Paint paintOpen;
	private Paint playerPaint;

	private static int CELL_WIDTH = 200;
	private static int CELL_HEIGHT = 200;
	private static int LEFT_PADDING = 50;
	private static int TOP_PADDING = 100;
	private boolean touched = false;

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		canvas.drawColor(Color.BLACK);
		ArrayList<Cell> cells = labyrinth.getCells();
		for (int i = 0; i < cells.size(); i++) {
			drawCell(canvas, cells.get(i));
			// canvas.drawRect(cell, paint);
		}

		invalidate();

	}

	private void drawCell(Canvas canvas, Cell cell) {
		if (cell.isWest()) {
			canvas.drawLine(cell.getCol() * CELL_WIDTH, cell.getRow()
					* CELL_HEIGHT, cell.getCol() * CELL_WIDTH,
					(cell.getRow() + 1) * CELL_HEIGHT, paint);// west
		} else {
			canvas.drawLine(cell.getCol() * CELL_WIDTH, cell.getRow()
					* CELL_HEIGHT, cell.getCol() * CELL_WIDTH,
					(cell.getRow() + 1) * CELL_HEIGHT, paintOpen);
		}
		if (cell.isNorth()) {
			canvas.drawLine(cell.getCol() * CELL_WIDTH, cell.getRow()
					* CELL_HEIGHT, (cell.getCol() + 1) * CELL_WIDTH,
					cell.getRow() * CELL_HEIGHT, paint);// north
		} else {
			canvas.drawLine(cell.getCol() * CELL_WIDTH, cell.getRow()
					* CELL_HEIGHT, (cell.getCol() + 1) * CELL_WIDTH,
					cell.getRow() * CELL_HEIGHT, paintOpen);
		}
		if (cell.isSouth()) {
			canvas.drawLine(cell.getCol() * CELL_WIDTH, (cell.getRow() + 1)
					* CELL_HEIGHT, (cell.getCol() + 1) * CELL_WIDTH,
					(cell.getRow() + 1) * CELL_HEIGHT, paint);// south
		} else {
			canvas.drawLine(cell.getCol() * CELL_WIDTH, (cell.getRow() + 1)
					* CELL_HEIGHT, (cell.getCol() + 1) * CELL_WIDTH,
					(cell.getRow() + 1) * CELL_HEIGHT, paintOpen);
		}
		if (cell.isEast()) {
			canvas.drawLine((cell.getCol() + 1) * CELL_WIDTH, cell.getRow()
					* CELL_HEIGHT, (cell.getCol() + 1) * CELL_WIDTH,
					(cell.getRow() + 1) * CELL_HEIGHT, paint);// east
		} else {
			canvas.drawLine((cell.getCol() + 1) * CELL_WIDTH, cell.getRow()
					* CELL_HEIGHT, (cell.getCol() + 1) * CELL_WIDTH,
					(cell.getRow() + 1) * CELL_HEIGHT, paintOpen);
		}
		if (labyrinth.getPlayer().getPosition().equals(cell)) {
			canvas.drawCircle(cell.getCol() * CELL_WIDTH + CELL_WIDTH / 2,
					cell.getRow() * CELL_HEIGHT + CELL_HEIGHT / 2,
					CELL_WIDTH * 3 / 8, playerPaint);
		}
	}

	public LabyrinthView(Context context, Paint aPaint, Paint anotherPaint,
			Paint aPlayerPaint, Labyrinth aLab) {
		super(context);
		paint = aPaint;
		paintOpen = anotherPaint;
		playerPaint = aPlayerPaint;
		labyrinth = aLab;
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

	private boolean startingFromPlayerCell = false;
	float xdown = 0;
	float ydown = 0;
	float xup = 0;
	float yup = 0;
	
	float x =0;
	int countx=0;
	float y=0;
	int county=0;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
	
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			xdown = event.getX();
			ydown = event.getY();
			Log.d("DEBUG", "Down: x=" + event.getX() + ", y=" + event.getY());
			if (belongstoPlayerPosition(event.getX(), event.getY())) {
//				Log.d("DEBUG","START BELOGINS TO PLAYER POSITION");
				startingFromPlayerCell = true;
				return true;
			}
//			Log.d("DEBUG","START DON'T belong to player position");
			startingFromPlayerCell = false;
			return false;
		}
		if (event.getAction() == MotionEvent.ACTION_MOVE) {
			x = x+event.getX();
			y = y+event.getY();
			countx++;
			county++;
			
			Log.d("DEBUG", "Move:x="+event.getX()+", x media=" + (x/countx) + ",y="+event.getY()+", y media =" + (y/county));
			 // TODO Auto-generated catch block
		
			 
			return true;
		}

		// Cell c = getCellFromPosition(event.getX(), event.getY());
		// if (c != null) {
		// labyrinth.getPlayer().setPosition(c);
		// touched = true;
		// return true;
		// }
		if (event.getAction() == MotionEvent.ACTION_UP) {
			Log.d("DEBUG", "Up: x=" + event.getX() + ", y=" + event.getY());
			xup = event.getX();
			yup = event.getY();
			if (startingFromPlayerCell) {
				try {
					labyrinth.move(labyrinth.getPlayer(),
							getDirectionFromPosition(xdown,ydown,xup,yup));
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

//		Log.d("DEBUG", "POINTS:[x0=" + x0 + ", y0=" + y0 + "], [xf=" + xf
//				+ ", yf=" + yf + "]");

		// horizontal move delta x > delta y
		if (Math.abs((xf - x0)) > Math.abs((yf - y0))) {
//			Log.d("DEBUG", "HORIZONTAL");
			if (x0 < xf) {
//				Log.d("DEBUG", "EAST");
				direction = 'E';
				// direction = Cell.EAST;
			} else if (x0 > xf) {
//				Log.d("DEBUG", "WEST");
				direction = 'W';
				// direction = Cell.WEST;
			}
		} else if (Math.abs((xf - x0)) < Math.abs((yf - y0))) {
//			Log.d("DEBUG", "VERTICAL");
			// vertical move delta y > delta x
			if (y0 > yf) {
//				Log.d("DEBUG", "NORTH");
				direction = 'N';
				// direction = Cell.NORTH;
			} else if (y0 < yf) {
//				Log.d("DEBUG", "SOUTH");
				direction = 'S';
				// direction = Cell.SOUTH;
			}
		}

//		Log.d("DEBUG", "Direction=" + String.valueOf(direction));
		return direction;
	}

	private boolean belongstoPlayerPosition(float x, float y) {

		Cell c = labyrinth.getPlayer().getPosition();
		int cellTopLeftX = c.getCol() * CELL_WIDTH;
		int cellTopLeftY = c.getRow() * CELL_HEIGHT;
		int cellTopRightX = c.getCol() * CELL_WIDTH + CELL_WIDTH;
		int cellBottomLeftY = c.getRow() * CELL_HEIGHT + CELL_HEIGHT;

		if ((x < cellTopRightX) && (x > cellTopLeftX) && (y < cellBottomLeftY)
				&& (y > cellTopLeftY)) {
			return true;
		}
		return false;
	}

}
