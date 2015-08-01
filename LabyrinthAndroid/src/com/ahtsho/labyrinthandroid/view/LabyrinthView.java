package com.ahtsho.labyrinthandroid.view;

import java.util.ArrayList;

import com.ahtsho.labyrinthandroid.R;

import core.Cell;
import core.Labyrinth;
import core.Levels;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

public class LabyrinthView extends View {
	private static final int WALL_THICKNESS_BOTTOM = 25;
	private static final int WALL_THICKNESS_RIGHT = 25;
	private static final int WALL_THICKNESS_TOP = 25;
	private static final int WALL_THICKNESS_LEFT = 25;
	private Labyrinth labyrinth;
	private Paint paint;
	private Paint paintOpen;
	private Paint playerPaint;
	private Paint textPaint;
	private int level = 1;
	private float screenWidth = 0;
	private float screenHeight = 0;
	private  float CELL_WIDTH = 300;// make di
	private  float CELL_HEIGHT = 300;

	private boolean startingFromPlayerCell = false;
	private float xdown = 0;
	private float ydown = 0;
	private float xup = 0;
	private float yup = 0;
	private boolean sameLevel = true;
	private float xOffset = 0;
	private float yOffset = 0;
	private float xAnimiate =0;
	private float yAnimiate =0;
	private float rAnimiate =0;
	
	private Texture texture=null;
	private ArrayList<Texture> textures = new ArrayList<Texture>();
	int landscape = 0;
	boolean animate = false;
//	boolean animationEnded = false;
	Bitmap playerBitmap = null;

	public LabyrinthView(Context context, Paint aPaint, Paint anotherPaint,
			Paint aPlayerPaint, Paint aTextPaint, Integer playerRes, Labyrinth aLab) {
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
		CELL_WIDTH = screenWidth/3;
		CELL_HEIGHT = CELL_WIDTH;
		
		setTestures(playerRes);
//		texture = BitmapFactory.decodeResource(getResources(), R.drawable.hedge);
	}

	private void setTestures(Integer playerRes){
		textures.add(new Texture(R.drawable.hedge_h,R.drawable.hedge_v, R.drawable.grass_9));
//		textures.add(new Texture(R.drawable.brick_h,R.drawable.brick_v, R.drawable.ciottoli));
		playerBitmap =BitmapFactory.decodeResource(getResources(), playerRes);
	}	

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawColor(Color.BLACK);
		ArrayList<Cell> cells = labyrinth.getCells();
		for (int i = 0; i < cells.size(); i++) {
			shiftPlayerOnScreen();
			drawCell(canvas, cells.get(i), xOffset, yOffset, false);
			if(animate){
				xAnimiate ++;
				yAnimiate ++;
				rAnimiate = (float) (rAnimiate - 0.3);
				
				if(xAnimiate >= 600){
					animate = false;
					xAnimiate = 0;
					yAnimiate = 0;
					rAnimiate = 0;
					try {
						goToNextLevel();
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}
		}
		invalidate();
	}

	private void shiftPlayerOnScreen() {
		float playerX = getXOfCellCenter(labyrinth.getPlayer().getPosition(), 0);
		float playerY = getYOfCellCenter(labyrinth.getPlayer().getPosition(), 0);
		xOffset = 0;
		yOffset = 0;
		if (playerX + CELL_WIDTH / 2 > screenWidth) {
			xOffset = (playerX + CELL_WIDTH / 2) - screenWidth+CELL_WIDTH;
		}
		if (playerY + CELL_HEIGHT / 2 > screenHeight) {
			yOffset = (playerY + CELL_HEIGHT) - screenHeight+CELL_HEIGHT;
		}
		if (playerX - CELL_WIDTH / 2 < 0) {
			xOffset = -(playerX - CELL_WIDTH / 2 +CELL_WIDTH);
		}
		if (playerY - CELL_HEIGHT / 2 < 0) {
			yOffset = -(playerY - CELL_HEIGHT / 2 +CELL_HEIGHT);
		}

		if (playerX - CELL_WIDTH / 2 >= 0
				&& playerX + CELL_WIDTH / 2 <= screenWidth
				&& playerY - CELL_HEIGHT / 2 >= 0
				&& playerY + CELL_HEIGHT / 2 <= screenHeight) {
			xOffset = 0;
			yOffset = 0;
		}
	}

	private void drawCell(Canvas canvas, Cell cell, float xOffset,
			float yOffset, boolean showCoords) {
		if(texture==null){
			drawWestWall(canvas, cell, xOffset, yOffset, showCoords);
			drawNorthWall(canvas, cell, xOffset, yOffset, showCoords);
			drawSouthWall(canvas, cell, xOffset, yOffset, showCoords);
			drawEastWall(canvas, cell, xOffset, yOffset, showCoords);
			if (labyrinth.getPlayer().getPosition().equals(cell)) {
				canvas.drawCircle(getXOfCellCenter(cell, xOffset)+xAnimiate,
						getYOfCellCenter(cell, yOffset)+yAnimiate, getRadiusOfCircle()+rAnimiate,
						playerPaint);
				if (showCoords) {
					canvas.drawText(getXOfCellCenter(cell, xOffset) + ", "
							+ getYOfCellCenter(cell, yOffset),
							getXOfCellCenter(cell, 70 + xOffset),
							getYOfCellCenter(cell, -35 + yOffset), textPaint);
				}
			}
			
		} else {
			drawBitmapCellFloor(canvas,cell,xOffset,yOffset);
			drawBitmapWestWall(canvas, cell, xOffset, yOffset, showCoords);
			drawBitmapNorthWall(canvas, cell, xOffset, yOffset, showCoords);
			drawBitmapSouthWall(canvas, cell, xOffset, yOffset, showCoords);
			drawBitmapEastWall(canvas, cell, xOffset, yOffset, showCoords);
			if (labyrinth.getPlayer().getPosition().equals(cell)) {
				drawBitmapPlayer(canvas,cell, xOffset,yOffset);
			}
		}	

		
		if (showCoords) {
			canvas.drawText("[" + cell.getRow() + "," + cell.getCol() + "]",
					getXOfCellCenter(cell, 25 + xOffset),
					getYOfCellCenter(cell, -5 + yOffset), textPaint);
		}
	}

	private void drawBitmapPlayer(Canvas canvas, Cell cell, float xOffset, float yOffset) {
//		canvas.drawCircle(getXOfCellCenter(cell, xOffset)+xAnimiate,
//				getYOfCellCenter(cell, yOffset)+yAnimiate, getRadiusOfCircle()+rAnimiate,
//				playerPaint);
		canvas.drawBitmap(playerBitmap, getXFromCell(cell, xOffset)+xAnimiate, getYFromCell(cell, yOffset)+yAnimiate, playerPaint);
		
	}

	private void drawBitmapCellFloor(Canvas canvas, Cell cell, float xOffset2,
			float yOffset2) {
		Rect dst = new Rect((int)getXFromCell(cell, xOffset),
				(int) getYFromCell(cell, yOffset), 
				(int)getXFromNextCell(cell, xOffset),
				(int) getYFormNextCell(cell, yOffset));
		canvas.drawBitmap(texture.getFloorBitmap(), null, dst, paint);		
	}

	private void drawBitmapEastWall(Canvas canvas, Cell cell, float xOffset,
			float yOffset, boolean showCoords) {
		if (cell.isEast()) {
			Rect dst = new Rect((int)getXFromNextCell(cell, xOffset)-WALL_THICKNESS_LEFT,
					(int) getYFromCell(cell, yOffset)-WALL_THICKNESS_TOP, 
					(int)getXFromNextCell(cell, xOffset)+WALL_THICKNESS_RIGHT,
					(int) getYFormNextCell(cell, yOffset)+WALL_THICKNESS_BOTTOM);
			
			canvas.drawBitmap(texture.getVBitmap(), null, dst, paint);
		} 
		if (showCoords) {
		}
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

	private void drawBitmapSouthWall(Canvas canvas, Cell cell, float xOffset,
			float yOffset, boolean showCoords) {
		if (cell.isSouth()) {
			Rect dst = new Rect((int)getXFromCell(cell, xOffset)-WALL_THICKNESS_LEFT,
					(int) getYFormNextCell(cell, yOffset)-WALL_THICKNESS_TOP, 
					(int) getXFromNextCell(cell, xOffset)+WALL_THICKNESS_RIGHT,
					(int) getYFormNextCell(cell, yOffset)+WALL_THICKNESS_BOTTOM);
			
			canvas.drawBitmap(texture.getHBitmap(), null, dst, paint);
			
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

	private void drawBitmapNorthWall(Canvas canvas, Cell cell, float xOffset, float yOffset, boolean showCoords) {
		if (cell.isNorth()) {
			Rect dst = new Rect(
					(int)getXFromCell(cell, xOffset)-WALL_THICKNESS_LEFT,
					(int) getYFromCell(cell, yOffset)-WALL_THICKNESS_TOP, 
					(int)getXFromNextCell(cell, xOffset)+WALL_THICKNESS_RIGHT,
					(int) getYFromCell(cell, yOffset)+WALL_THICKNESS_BOTTOM);
			canvas.drawBitmap(texture.getHBitmap(), null, dst, paint);
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
		}
	}

	private void drawBitmapWestWall(Canvas canvas, Cell cell, float xOffset,
			float yOffset, boolean showCoords) {
		if (cell.isWest()) {
			
			Rect dst = new Rect(
					(int)getXFromCell(cell, xOffset)-WALL_THICKNESS_LEFT,
					(int) getYFromCell(cell, yOffset)-WALL_THICKNESS_TOP, 
					(int)getXFromCell(cell, xOffset)+WALL_THICKNESS_RIGHT,
					(int) getYFormNextCell(cell, yOffset)+WALL_THICKNESS_BOTTOM);
			
			canvas.drawBitmap(texture.getVBitmap(), null, dst, paint);
		} 
		if (showCoords) {
			canvas.drawText("W(" + getXFromCell(cell, xOffset) + ","
					+ getYFromCell(cell, yOffset) + ")",
					getXFromCell(cell, -10 + xOffset),
					getYFromCell(cell, -30 + yOffset), textPaint);
			canvas.drawText("(" + getXFromCell(cell, xOffset) + ","
					+ getYFormNextCell(cell, yOffset) + ")",
					getXFromCell(cell, -10 + xOffset),
					getYFormNextCell(cell, 20 + yOffset), textPaint);
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
					getXFromCell(cell, -10 + xOffset),
					getYFromCell(cell, -30 + yOffset), textPaint);
			canvas.drawText("(" + getXFromCell(cell, xOffset) + ","
					+ getYFormNextCell(cell, yOffset) + ")",
					getXFromCell(cell, -10 + xOffset),
					getYFormNextCell(cell, 20 + yOffset), textPaint);
		}
	}

	private float getRadiusOfCircle() {
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

	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);

		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			xdown = event.getX();
			ydown = event.getY();
			if (belongstoPlayerPosition(event.getX(), event.getY())) {
				startingFromPlayerCell = true;
				return true;
			}
			startingFromPlayerCell = false;
			return false;
		}

		if (event.getAction() == MotionEvent.ACTION_UP) {
			xup = event.getX();
			yup = event.getY();
			if (startingFromPlayerCell) {
				try {
					Cell playerPosition = labyrinth.getPlayer().getPosition();
					char exitDirection = getDirectionFromPosition(xdown, ydown, xup, yup);
					sameLevel = labyrinth.move(labyrinth.getPlayer(),exitDirection);
					if (!sameLevel) {		
						animate = true;
						
						
					}else{
						if(labyrinth.getPlayer().getPosition().equals(playerPosition)){
							CharSequence msg = "Ouch, don't slam me on the wall!";
							Toast toast = Toast.makeText(this.getContext(), msg,
									Toast.LENGTH_SHORT);
							toast.show();
						}
					}
				} catch (Exception e) {
					Log.e("DEBUG", "Exception: " + e.getMessage());
				}
				return true;
			}
			return false;
		}
		return false;
	}

	private void goToNextLevel() throws Exception {
		level = Levels.next(level);
		CharSequence msg = "LEVEL " + level;
		Toast toast = Toast.makeText(this.getContext(), msg,Toast.LENGTH_LONG);
		toast.show();
		
//		Log.d(LOG_TAG, "level="+level+" landscape="+landscape);
		
//		if(level%2==0){	
		texture = textures.get(0);
//			texture = textures.get(landscape++);
			texture.setImgs(BitmapFactory.decodeResource(getResources(),texture.getHorizontal()), 
					BitmapFactory.decodeResource(getResources(),texture.getVertical()),
					BitmapFactory.decodeResource(getResources(),texture.getFloor()));
//		}
		
		labyrinth = Levels.genLabyrinth(level + 2,labyrinth.getPlayer());
	}

	private char getDirectionFromPosition(float x0, float y0, float xf, float yf) {
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
