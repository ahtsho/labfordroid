package com.ahtsho.labyrinthandroid.view;

import game.Level;
import infrastructure.*;
import java.util.ArrayList;
import java.util.HashMap;
import com.ahtsho.labyrinthandroid.R;
import com.ahtsho.labyrinthandroid.service.MetricsService;
import com.ahtsho.labyrinthandroid.service.SoundSource;
import com.ahtsho.labyrinthandroid.view.painters.BitmapPainter;
import com.ahtsho.labyrinthandroid.view.painters.CanvasPainter;
import com.ahtsho.labyrinthandroid.view.painters.Painter;
import creatures.Player;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class LabyrinthView extends View {

	private Labyrinth labyrinth;
	// private Paint paintCell;
	// private Paint paintEmptyWall;
	// private Paint paintPlayer;
	// private Paint paintCreature;
//	private Paint paintText;
	private int level = 1;

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
	private float zoom = 0;

	private Texture texture = null;
	private ArrayList<Texture> textures = new ArrayList<Texture>();
	private boolean animate = false;
	private char exitDirection = ' ';
	private Player player = null;
	private Activity mainActivity;
	private Vibrator vb;
	private ArrayList<Cell> cells = null;
	private boolean hasNotRoared = true;

	public LabyrinthView(Context context, HashMap<String, Paint> paints, Integer playerRes, Labyrinth aLab) {
		super(context);
		mainActivity = (Activity) this.getContext();
		labyrinth = aLab;
		cells = labyrinth.getCells();
		player = labyrinth.getPlayer();

		BitmapPainter.setPlayerBitmap(Bitmapper.getBitmap(labyrinth.getPlayer(), this));

		new Painter(paints);
		MetricsService.initializeCellDimension(context);
		centerSmallLabyrinths();

		textures.add(new Texture(R.drawable.hedge_h, R.drawable.hedge_v, R.drawable.grass_9));

		vb = (Vibrator) mainActivity.getSystemService(Context.VIBRATOR_SERVICE);
		// SoundSource.off();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		UICommunicationManager.updateActionBar(this, labyrinth.getPlayer().getLife());

		super.onDraw(canvas);
		canvas.drawColor(Color.BLACK);

		for (int i = 0; i < cells.size(); i++) {
			shiftPlayerOnScreen();
			drawCell(canvas, cells.get(i), xOffset - leftScreenPadding, yOffset - topScreenPadding, false);
			transitToNextLevelWithAnimation();
		}

		if (player.getLife() == 0) {
			UICommunicationManager.showGameOverAlertDialog(mainActivity);
		} else {
			invalidate();
		}
	}

	private void transitToNextLevelWithAnimation() {
		if (animate) {
			animate(exitDirection);
			zoom = (float) (zoom - 0.3);

			if (xAnimiate >= MetricsService.PLAYER_EXIT_ANIMATION_X_AXIS_MAX_VALUE
					|| yAnimiate >= MetricsService.PLAYER_EXIT_ANIMATION_Y_AXIS_MAX_VALUE
					|| xAnimiate <= -MetricsService.PLAYER_EXIT_ANIMATION_X_AXIS_MAX_VALUE
					|| yAnimiate <= -MetricsService.PLAYER_EXIT_ANIMATION_X_AXIS_MAX_VALUE) {
				animate = false;
				xAnimiate = 0;
				yAnimiate = 0;
				zoom = 0;
				try {
					goToNextLevel();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}
	}

	private void centerSmallLabyrinths() {
		if (labyrinth.getDimension() * MetricsService.CELL_HEIGHT < MetricsService.screenHeight) {
			topScreenPadding = (MetricsService.screenHeight - labyrinth.getDimension() * MetricsService.CELL_HEIGHT) / 2 - 60;
		}
		if (labyrinth.getDimension() * MetricsService.CELL_WIDTH < MetricsService.screenWidth) {
			leftScreenPadding = (MetricsService.screenWidth - labyrinth.getDimension() * MetricsService.CELL_WIDTH) / 2;
		}
	}

	private void shiftPlayerOnScreen() {
		if (labyrinth.getPlayer() != null) {
			float playerX = MetricsService.getXOfCellCenter(labyrinth.getPlayer().getPosition(), 0);
			float playerY = MetricsService.getYOfCellCenter(labyrinth.getPlayer().getPosition(), 0);
			xOffset = 0;
			yOffset = 0;
			if (playerX >= MetricsService.screenWidth - MetricsService.CELL_WIDTH) {
				xOffset = playerX - (MetricsService.screenWidth - MetricsService.CELL_WIDTH);
			}
			if (playerY >= MetricsService.screenHeight - 1.5f * MetricsService.CELL_HEIGHT) {
				yOffset = playerY - (MetricsService.screenHeight - 1.5f * MetricsService.CELL_HEIGHT);
			}
			if (playerX < MetricsService.CELL_WIDTH) {
				xOffset = playerX - MetricsService.CELL_WIDTH;
			}
			if (playerY < MetricsService.CELL_HEIGHT) {
				yOffset = playerY - MetricsService.CELL_HEIGHT;
			}
		}
	}

	private synchronized void drawCell(Canvas canvas, Cell cell, float xOffset, float yOffset, boolean showCoords) {
		if (texture == null) {
			CanvasPainter.drawCell(canvas, cell, xOffset, yOffset, showCoords);
			CanvasPainter.drawTools(canvas, cell, xOffset, yOffset);
			CanvasPainter.drawCreatures(canvas, cell, xOffset, yOffset, xAnimiate, yAnimiate, zoom);
			CanvasPainter.drawPlayer(canvas, cell, labyrinth.getPlayer(), xOffset, yOffset, xAnimiate, yAnimiate, zoom, showCoords);
		} else {
			BitmapPainter.drawCell(canvas, cell, xOffset, yOffset, showCoords);
			BitmapPainter.drawCreatures(this, mainActivity, canvas, cell, player, xOffset, yOffset, xAnimiate, yAnimiate, hasNotRoared);
			BitmapPainter.drawTools(this, mainActivity, canvas, cell, player, xOffset, yOffset);
		}
		CanvasPainter.drawCoords(canvas, cell, xOffset, yOffset, showCoords);
	}


	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);

		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			xdown = event.getX();
			ydown = event.getY();
			return true;
		}

		if (event.getAction() == MotionEvent.ACTION_UP) {
			xup = event.getX();
			yup = event.getY();
			if (true) {
				try {
					Cell playerPosition = labyrinth.getPlayer().getPosition();
					exitDirection = getDirectionFromPosition(xdown, ydown, xup, yup);
					sameLevel = labyrinth.move(labyrinth.getPlayer(), exitDirection);
					if (!sameLevel) {
						animate = true;
						exitDirection = labyrinth.getExitCellWall();
						new SoundSource(labyrinth.getPlayer(), SoundSource.EXIT, mainActivity);
					} else {
						if (labyrinth.getPlayer().getPosition().equals(playerPosition)) {

							vb.vibrate(100);
							new SoundSource(labyrinth.getPlayer(), SoundSource.BUMP, mainActivity);
							new SoundSource(labyrinth.getPlayer(), SoundSource.PAIN, mainActivity);
						}
					}
				} catch (Exception e) {
					Log.e("DEBUG", "Exception: " + e.getMessage());
				}
				return true;
			}
		}
		return false;
	}

	private void animate(char direction) {
		if (direction == Cell.NORTH)
			yAnimiate--;
		if (direction == Cell.SOUTH)
			yAnimiate++;
		if (direction == Cell.WEST)
			xAnimiate--;
		if (direction == Cell.EAST)
			xAnimiate++;
	}

	private void goToNextLevel() throws Exception {
		level = Level.next();
		CharSequence msg = "LEVEL " + level;
		Toast toast = Toast.makeText(this.getContext(), msg, Toast.LENGTH_LONG);
		toast.show();
		texture = textures.get(0);
		BitmapPainter.setTexture(texture);
		texture.setImgs(BitmapFactory.decodeResource(getResources(), texture.getHorizontal()),
				BitmapFactory.decodeResource(getResources(), texture.getVertical()), BitmapFactory.decodeResource(getResources(), texture.getFloor()));

		labyrinth = Level.genLabyrinth();
		cells = labyrinth.getCells();
		player.setPosition(labyrinth.getEntrance());
		labyrinth.setPlayer(player);
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

	private synchronized boolean belongstoPlayerPosition(float x, float y) {
		Cell c = labyrinth.getPlayer().getPosition();
		float cellTopLeftX = MetricsService.getXFromCell(c, xOffset + leftScreenPadding);
		Log.d("belongs to player position", "cellTopLeftX=" + cellTopLeftX);
		float cellTopLeftY = MetricsService.getYFromCell(c, yOffset + topScreenPadding);
		Log.d("belongs to player position", "cellTopLefty=" + cellTopLeftX);
		float cellTopRightX = MetricsService.getXFromCell(c, xOffset) + MetricsService.CELL_WIDTH + leftScreenPadding;
		float cellBottomLeftY = MetricsService.getYFromCell(c, yOffset) + MetricsService.CELL_HEIGHT + topScreenPadding;

		if ((x < cellTopRightX) && (x > cellTopLeftX) && (y < cellBottomLeftY) && (y > cellTopLeftY)) {
			return true;
		}
		return false;
	}

}
