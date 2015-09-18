package com.ahtsho.labyrinthandroid.view;

import game.Level;
import infrastructure.*;

import java.util.ArrayList;

import tools.Tool;
import view.Console;

import com.ahtsho.labyrinthandroid.R;
import com.ahtsho.labyrinthandroid.activity.GameActivity;
import com.ahtsho.labyrinthandroid.activity.SettingsActivity;

import creatures.Creature;
import creatures.Player;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.os.Vibrator;
import android.text.AndroidCharacter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class LabyrinthView extends View {
	private static final int WALL_THICKNESS_BOTTOM = 25;
	private static final int WALL_THICKNESS_RIGHT = 25;
	private static final int WALL_THICKNESS_TOP = 25;
	private static final int WALL_THICKNESS_LEFT = 25;
	private Labyrinth labyrinth;
	private Paint paint;
	private Paint paintOpen;
	private Paint playerPaint;
	private Paint creaturePaint;
	private Paint textPaint;
	private int level = 1;
	private float screenWidth = 0;
	private float screenHeight = 0;
	private static float CELL_WIDTH = 300;// make di
	private static float CELL_HEIGHT = 300;
	private static float LEFT_MARGIN = 30;
	private static float TOP_MARGIN = 0;

	private boolean startingFromPlayerCell = false;
	private float xdown = 0;
	private float ydown = 0;
	private float xup = 0;
	private float yup = 0;
	private boolean sameLevel = true;
	private float xOffset = 0;
	private float yOffset = 0;
	private float topScreenPadding = 0;
	private float leftScreenPadding = 0;
	private float xAnimiate = 0;
	private float yAnimiate = 0;
	private float rAnimiate = 0;

	private Texture texture = null;
	private ArrayList<Texture> textures = new ArrayList<Texture>();
	int landscape = 0;
	boolean animate = false;
	Bitmap playerBitmap = null;
	Player player = null;
	Activity mainActivity;
	Vibrator vb;
	
	public LabyrinthView(Context context, Paint aPaint, Paint anotherPaint, Paint aPlayerPaint, Paint aTextPaint, Paint aCreaturePaint,
			Integer playerRes, Labyrinth aLab) {
		super(context);
		paint = aPaint;
		paintOpen = anotherPaint;
		playerPaint = aPlayerPaint;
		creaturePaint = aCreaturePaint;
		labyrinth = aLab;
		textPaint = aTextPaint;
		player = labyrinth.getPlayer();
		DisplayMetrics displaymetrics = new DisplayMetrics();
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(displaymetrics);
		screenHeight = displaymetrics.heightPixels;
		screenWidth = displaymetrics.widthPixels;
		CELL_WIDTH = screenWidth / 3;
		CELL_HEIGHT = CELL_WIDTH;
		centerSmallLabyrinths();
		setTextures(playerRes);
		mainActivity = (Activity) this.getContext();
		vb = (Vibrator) mainActivity.getSystemService(Context.VIBRATOR_SERVICE);
		// texture = BitmapFactory.decodeResource(getResources(),
		// R.drawable.hedge);
	}

	private void setTextures(Integer playerRes) {
		textures.add(new Texture(R.drawable.hedge_h, R.drawable.hedge_v, R.drawable.grass_9));
		// textures.add(new Texture(R.drawable.brick_h,R.drawable.brick_v,
		// R.drawable.ciottoli));
		if (playerRes > 0) {
			playerBitmap = BitmapFactory.decodeResource(getResources(), playerRes);
		} else {
			playerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.face1);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		updateActionBar();

		super.onDraw(canvas);
		canvas.drawColor(Color.BLACK);
		ArrayList<Cell> cells = labyrinth.getCells();
		// printToolsOfTheLevel(cells);
		for (int i = 0; i < cells.size(); i++) {
			shiftPlayerOnScreen();
			drawCell(canvas, cells.get(i), xOffset - leftScreenPadding, yOffset - topScreenPadding, false);
			if (animate) {
				xAnimiate++;
				yAnimiate++;
				rAnimiate = (float) (rAnimiate - 0.3);

				if (xAnimiate >= 600) {
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
		if (player.getLife() == 0) {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mainActivity);
			alertDialogBuilder.setMessage("GAME OVER");
			alertDialogBuilder.setPositiveButton("Restart", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Level.goTo(-(Level.currentLevel-3));
					mainActivity.recreate();
				}
			});
			alertDialogBuilder.setNegativeButton("Quit", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mainActivity.finish();
				}
			});
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
		} else {
			invalidate();
		}
	}

	private void updateActionBar() {
		try {
			// Activity host = (Activity) this.getContext();
			// ActionBar bar = host.getActionBar();
			((TextView) this.getRootView().findViewById(R.id.title_bar_text)).setText(" " + labyrinth.getPlayer().getLife());
			// RelativeLayout barLayout = (RelativeLayout)
			// this.getRootView().findViewById(R.id.main_title_bar_layout);

			// for(int li=0;li<player.getLife();li++){
			//
			// ImageView lifeImg = new ImageView(host);
			// lifeImg.setImageResource(R.drawable.heart);
			// lifeImg.setId(li);
			// barLayout.addView(lifeImg);
			// }

			// bar.setCustomView(abLay);
			// bar.setDisplayShowCustomEnabled(true);
			// bar.setIcon(R.drawable.heart);
		} catch (Exception e) {
			System.out.println("Call requires API level 11");
		}
	}

	private void centerSmallLabyrinths() {
		Log.d("Labview", "lab height = " + labyrinth.getDimension() * CELL_HEIGHT + ", screenHeight " + screenHeight);
		if (labyrinth.getDimension() * CELL_HEIGHT < screenHeight) {
			topScreenPadding = (screenHeight - labyrinth.getDimension() * CELL_HEIGHT) / 2 - 60;
		}
		if (labyrinth.getDimension() * CELL_WIDTH < screenWidth) {
			leftScreenPadding = (screenWidth - labyrinth.getDimension() * CELL_WIDTH) / 2;
		}
	}

	private void shiftPlayerOnScreen() {
		if (labyrinth.getPlayer() != null) {
			float playerX = getXOfCellCenter(labyrinth.getPlayer().getPosition(), 0);
			float playerY = getYOfCellCenter(labyrinth.getPlayer().getPosition(), 0);
			xOffset = 0;
			yOffset = 0;
			if (playerX + CELL_WIDTH / 2 > screenWidth) {
				xOffset = (playerX + CELL_WIDTH / 2) - screenWidth + CELL_WIDTH;
			}
			if (playerY + CELL_HEIGHT / 2 > screenHeight) {
				yOffset = (playerY + CELL_HEIGHT) - screenHeight + CELL_HEIGHT;
			}
			if (playerX - CELL_WIDTH / 2 < 0) {
				xOffset = -(playerX - CELL_WIDTH / 2 + CELL_WIDTH);
			}
			if (playerY - CELL_HEIGHT / 2 < 0) {
				yOffset = -(playerY - CELL_HEIGHT / 2 + CELL_HEIGHT);
			}

			if (playerX - CELL_WIDTH / 2 >= 0 && playerX + CELL_WIDTH / 2 <= screenWidth && playerY - CELL_HEIGHT / 2 >= 0
					&& playerY + CELL_HEIGHT / 2 <= screenHeight) {
				xOffset = 0;
				yOffset = 0;
			}
		}
	}

	private synchronized void drawCell(Canvas canvas, Cell cell, float xOffset, float yOffset, boolean showCoords) {
		if (texture == null) {
			// if(true){
			drawWestWall(canvas, cell, xOffset, yOffset, showCoords);
			drawNorthWall(canvas, cell, xOffset, yOffset, showCoords);
			drawSouthWall(canvas, cell, xOffset, yOffset, showCoords);
			drawEastWall(canvas, cell, xOffset, yOffset, showCoords);

			if (cell.getHosts().size() == 0 && cell.getTools().size() == 0) {
			} else if (cell.getHosts().size() > 0) {
				for (Creature p : cell.getHosts()) {
					if (p instanceof Player) {
						canvas.drawCircle(getXOfCellCenter(cell, xOffset) + xAnimiate, getYOfCellCenter(cell, yOffset) + yAnimiate,
								getRadiusOfCircle() + rAnimiate, playerPaint);
					} else {
						canvas.drawCircle(getXOfCellCenter(cell, xOffset) + xAnimiate, getYOfCellCenter(cell, yOffset) + yAnimiate,
								getRadiusOfCircle() + rAnimiate, creaturePaint);
					}
				}
			}
			if (cell.getTools().size() > 0) {
				for (Tool t : cell.getTools()) {
					canvas.drawRect(new Rect((int) (getXFromCell(cell, xOffset) + CELL_WIDTH / 3),
							(int) (getYFromCell(cell, yOffset) + CELL_HEIGHT * 1 / 4), (int) (getXFromNextCell(cell, xOffset) - CELL_WIDTH / 3),
							(int) (getYFormNextCell(cell, yOffset) - CELL_HEIGHT * 1 / 4)), creaturePaint);
				}
			}

			if (labyrinth.getPlayer() != null) {
				if (labyrinth.getPlayer().getPosition().equals(cell)) {
					canvas.drawCircle(getXOfCellCenter(cell, xOffset) + xAnimiate, getYOfCellCenter(cell, yOffset) + yAnimiate, getRadiusOfCircle()
							+ rAnimiate, playerPaint);
					if (showCoords) {
						canvas.drawText(getXOfCellCenter(cell, xOffset) + ", " + getYOfCellCenter(cell, yOffset),
								getXOfCellCenter(cell, 70 + xOffset), getYOfCellCenter(cell, -35 + yOffset), textPaint);
					}
				}
			}
		} else {
			drawBitmapCellFloor(canvas, cell, xOffset, yOffset);
			drawBitmapWestWall(canvas, cell, xOffset, yOffset, showCoords);
			drawBitmapNorthWall(canvas, cell, xOffset, yOffset, showCoords);
			drawBitmapSouthWall(canvas, cell, xOffset, yOffset, showCoords);
			drawBitmapEastWall(canvas, cell, xOffset, yOffset, showCoords);

			if (cell.getHosts().size() == 0) {
			} else if (cell.getHosts().size() > 0) {
				ArrayList<Creature> hosts = cell.getHosts();
				try {
					synchronized (hosts) {
						for (Creature p : hosts) {
							if (p instanceof Player) {
								drawBitmapPlayer(canvas, cell, xOffset, yOffset);
							} else {
								drawBitmapCreature(p, canvas, cell, xOffset, yOffset);
							}
						}
					}
				} catch (Exception e) {
					System.out.println("HOSTS=" + hosts.toString() + "-" + e.getMessage());
				}

			}
			if (cell.getTools().size() == 0) {

			} else if (cell.getTools().size() > 0) {
				for (Tool t : cell.getTools()) {
					System.out.println("cell " + cell.getName() + " TOOLS: " + cell.getTools().toString());
					drawBitmapTool(t, canvas, cell, xOffset, yOffset);
				}
			}

		}

		if (showCoords) {
			canvas.drawText("[" + cell.getRow() + "," + cell.getCol() + "]", getXOfCellCenter(cell, 25 + xOffset),
					getYOfCellCenter(cell, -5 + yOffset), textPaint);
		}
	}

	private void drawBitmapTool(Tool t, Canvas canvas, Cell cell, float xOffset, float yOffset) {
		canvas.drawBitmap(Bitmapper.getBitmap(t, this), getXFromCell(cell, xOffset) + LEFT_MARGIN, getYFromCell(cell, yOffset) + TOP_MARGIN,
				playerPaint);

	}

	private void drawBitmapCreature(Creature c, Canvas canvas, Cell cell, float xOffset, float yOffset) {
		canvas.drawBitmap(Bitmapper.getBitmap(c, this), getXFromCell(cell, xOffset) + LEFT_MARGIN, getYFromCell(cell, yOffset) + TOP_MARGIN,
				playerPaint);

	}

	private void drawBitmapPlayer(Canvas canvas, Cell cell, float xOffset, float yOffset) {
		canvas.drawBitmap(playerBitmap, getXFromCell(cell, xOffset) + xAnimiate + LEFT_MARGIN, getYFromCell(cell, yOffset) + yAnimiate + TOP_MARGIN,
				playerPaint);

	}

	private void drawBitmapCellFloor(Canvas canvas, Cell cell, float xOffset, float yOffset) {
		Rect dst = new Rect((int) getXFromCell(cell, xOffset), (int) getYFromCell(cell, yOffset), (int) getXFromNextCell(cell, xOffset),
				(int) getYFormNextCell(cell, yOffset));
		canvas.drawBitmap(texture.getFloorBitmap(), null, dst, paint);
	}

	private void drawBitmapEastWall(Canvas canvas, Cell cell, float xOffset, float yOffset, boolean showCoords) {
		if (cell.isEast()) {
			Rect dst = new Rect((int) getXFromNextCell(cell, xOffset) - WALL_THICKNESS_LEFT, (int) getYFromCell(cell, yOffset) - WALL_THICKNESS_TOP,
					(int) getXFromNextCell(cell, xOffset) + WALL_THICKNESS_RIGHT, (int) getYFormNextCell(cell, yOffset) + WALL_THICKNESS_BOTTOM);

			canvas.drawBitmap(texture.getVBitmap(), null, dst, paint);
		}
		if (showCoords) {
		}
	}

	private void drawEastWall(Canvas canvas, Cell cell, float xOffset, float yOffset, boolean showCoords) {
		if (cell.isEast()) {
			canvas.drawLine(getXFromNextCell(cell, xOffset), getYFromCell(cell, yOffset), getXFromNextCell(cell, xOffset),
					getYFormNextCell(cell, yOffset), paint);// east
		} else {
			canvas.drawLine(getXFromNextCell(cell, xOffset), getYFromCell(cell, yOffset), getXFromNextCell(cell, xOffset),
					getYFormNextCell(cell, yOffset), paintOpen);
		}
		if (showCoords) {
		}
	}

	private void drawBitmapSouthWall(Canvas canvas, Cell cell, float xOffset, float yOffset, boolean showCoords) {
		if (cell.isSouth()) {
			Rect dst = new Rect((int) getXFromCell(cell, xOffset) - WALL_THICKNESS_LEFT, (int) getYFormNextCell(cell, yOffset) - WALL_THICKNESS_TOP,
					(int) getXFromNextCell(cell, xOffset) + WALL_THICKNESS_RIGHT, (int) getYFormNextCell(cell, yOffset) + WALL_THICKNESS_BOTTOM);

			canvas.drawBitmap(texture.getHBitmap(), null, dst, paint);

		}
		if (showCoords) {
		}
	}

	private void drawSouthWall(Canvas canvas, Cell cell, float xOffset, float yOffset, boolean showCoords) {
		if (cell.isSouth()) {
			canvas.drawLine(getXFromCell(cell, xOffset), getYFormNextCell(cell, yOffset), getXFromNextCell(cell, xOffset),
					getYFormNextCell(cell, yOffset), paint);// south
		} else {
			canvas.drawLine(getXFromCell(cell, xOffset), getYFormNextCell(cell, yOffset), getXFromNextCell(cell, xOffset),
					getYFormNextCell(cell, yOffset), paintOpen);
		}
		if (showCoords) {
		}
	}

	private void drawBitmapNorthWall(Canvas canvas, Cell cell, float xOffset, float yOffset, boolean showCoords) {
		if (cell.isNorth()) {
			Rect dst = new Rect((int) getXFromCell(cell, xOffset) - WALL_THICKNESS_LEFT, (int) getYFromCell(cell, yOffset) - WALL_THICKNESS_TOP,
					(int) getXFromNextCell(cell, xOffset) + WALL_THICKNESS_RIGHT, (int) getYFromCell(cell, yOffset) + WALL_THICKNESS_BOTTOM);
			canvas.drawBitmap(texture.getHBitmap(), null, dst, paint);
		}
		if (showCoords) {
		}
	}

	private void drawNorthWall(Canvas canvas, Cell cell, float xOffset, float yOffset, boolean showCoords) {
		if (cell.isNorth()) {
			canvas.drawLine(getXFromCell(cell, xOffset), getYFromCell(cell, yOffset), getXFromNextCell(cell, xOffset), getYFromCell(cell, yOffset),
					paint);// north
		} else {
			canvas.drawLine(getXFromCell(cell, xOffset), getYFromCell(cell, yOffset), getXFromNextCell(cell, xOffset), getYFromCell(cell, yOffset),
					paintOpen);
		}
		if (showCoords) {
		}
	}

	private void drawBitmapWestWall(Canvas canvas, Cell cell, float xOffset, float yOffset, boolean showCoords) {
		if (cell.isWest()) {

			Rect dst = new Rect((int) getXFromCell(cell, xOffset) - WALL_THICKNESS_LEFT, (int) getYFromCell(cell, yOffset) - WALL_THICKNESS_TOP,
					(int) getXFromCell(cell, xOffset) + WALL_THICKNESS_RIGHT, (int) getYFormNextCell(cell, yOffset) + WALL_THICKNESS_BOTTOM);

			canvas.drawBitmap(texture.getVBitmap(), null, dst, paint);
		}
		if (showCoords) {
			canvas.drawText("W(" + getXFromCell(cell, xOffset) + "," + getYFromCell(cell, yOffset) + ")", getXFromCell(cell, -10 + xOffset),
					getYFromCell(cell, -30 + yOffset), textPaint);
			canvas.drawText("(" + getXFromCell(cell, xOffset) + "," + getYFormNextCell(cell, yOffset) + ")", getXFromCell(cell, -10 + xOffset),
					getYFormNextCell(cell, 20 + yOffset), textPaint);
		}
	}

	private void drawWestWall(Canvas canvas, Cell cell, float xOffset, float yOffset, boolean showCoords) {
		if (cell.isWest()) {
			canvas.drawLine(getXFromCell(cell, xOffset), getYFromCell(cell, yOffset), getXFromCell(cell, xOffset), getYFormNextCell(cell, yOffset),
					paint);

		} else {
			canvas.drawLine(getXFromCell(cell, xOffset), getYFromCell(cell, yOffset), getXFromCell(cell, xOffset), getYFormNextCell(cell, yOffset),
					paintOpen);
		}
		if (showCoords) {
			canvas.drawText("W(" + getXFromCell(cell, xOffset) + "," + getYFromCell(cell, yOffset) + ")", getXFromCell(cell, -10 + xOffset),
					getYFromCell(cell, -30 + yOffset), textPaint);
			canvas.drawText("(" + getXFromCell(cell, xOffset) + "," + getYFormNextCell(cell, yOffset) + ")", getXFromCell(cell, -10 + xOffset),
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
					sameLevel = labyrinth.move(labyrinth.getPlayer(), exitDirection);
					if (!sameLevel) {
						animate = true;

					} else {
						if (labyrinth.getPlayer().getPosition().equals(playerPosition)) {
							
				            vb.vibrate(100);
							CharSequence msg = "Ouch, don't slam me on the wall!";
							Toast toast = Toast.makeText(this.getContext(), msg, Toast.LENGTH_SHORT);
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
		level = Level.next();
		// level = Level.next(level);
		CharSequence msg = "LEVEL " + level;
		Toast toast = Toast.makeText(this.getContext(), msg, Toast.LENGTH_LONG);
		toast.show();

		// Log.d(LOG_TAG, "level="+level+" landscape="+landscape);

		// if(level%2==0){
		texture = textures.get(0);
		// texture = textures.get(landscape++);
		texture.setImgs(BitmapFactory.decodeResource(getResources(), texture.getHorizontal()),
				BitmapFactory.decodeResource(getResources(), texture.getVertical()), BitmapFactory.decodeResource(getResources(), texture.getFloor()));
		// }

		labyrinth = Level.genLabyrinth();
		player.setPosition(labyrinth.getEntrance());
		labyrinth.setPlayer(player);
		// labyrinth = Level.genLabyrinth(level + 2,labyrinth.getPlayer());
		centerSmallLabyrinths();
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

}
