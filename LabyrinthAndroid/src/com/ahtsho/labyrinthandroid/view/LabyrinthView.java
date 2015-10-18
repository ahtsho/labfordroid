package com.ahtsho.labyrinthandroid.view;

import game.Level;
import infrastructure.*;

import java.util.ArrayList;
import java.util.HashMap;

import com.ahtsho.labyrinthandroid.service.AnimationManager;
import com.ahtsho.labyrinthandroid.service.MetricsService;
import com.ahtsho.labyrinthandroid.service.SoundSource;
import com.ahtsho.labyrinthandroid.util.ErrorLogger;
import com.ahtsho.labyrinthandroid.view.painters.BitmapPainter;
import com.ahtsho.labyrinthandroid.view.painters.CanvasPainter;
import com.ahtsho.labyrinthandroid.view.painters.Painter;

import creatures.Player;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class LabyrinthView extends View {

	
	private static final int VIBRATION_TIME_MILLI = 100;
	private Labyrinth labyrinth;
	private Player player = null;
	private ArrayList<Cell> cells = null;
	
	private int level = 1;
	private boolean sameLevel = true;
	
	private char exitDirection = ' ';
	
	private float xdown = 0;
	private float ydown = 0;
	private float xup = 0;
	private float yup = 0;
	
	private Activity mainActivity;
	private Vibrator vb;
	
	private boolean hasNotRoared = true;

	public LabyrinthView(Context context, HashMap<String, Paint> paints, Integer playerRes, Labyrinth aLab) {
		super(context);
		mainActivity = (Activity) this.getContext();
		labyrinth = aLab;
		cells = labyrinth.getCells();
		player = labyrinth.getPlayer();

		BitmapPainter.setPlayerBitmap(Bitmapper.getBitmap(labyrinth.getPlayer(), this));

		new Painter(mainActivity, this,paints);
		MetricsService.initializeCellDimension(context);
		MetricsService.centerSmallLabyrinths(labyrinth.getDimension());
		vb = (Vibrator) mainActivity.getSystemService(Context.VIBRATOR_SERVICE);
		// SoundSource.off();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		UICommunicationManager.updateActionBar(this, labyrinth.getPlayer().getLife());
		super.onDraw(canvas);
		canvas.drawColor(Color.BLACK);

		for (int i = 0; i < cells.size(); i++) {
			if (labyrinth.getPlayer() != null) {
				MetricsService.shiftPlayerOnScreen(labyrinth.getPlayer().getPosition());
			}
			drawCell(canvas, cells.get(i), MetricsService.getStartingX(), MetricsService.getStartingY(), false);
			transitToNextLevelWithAnimation();
		}

		if (player.getLife() == 0) {
			UICommunicationManager.showGameOverAlertDialog(mainActivity);
		} else {
			invalidate();
		}
	}

	private void transitToNextLevelWithAnimation() {
		AnimationManager.animate(exitDirection);
		if(AnimationManager.animationEnded()) {
			AnimationManager.reset();
			try {
				goToNextLevel();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
	}


	private synchronized void drawCell(Canvas canvas, Cell cell, float xOffset, float yOffset, boolean showCoords) {		
		if(isTutorial()){
			System.out.println("TUTORIAL_LEVEL = "+level);
			CanvasPainter.drawCell(canvas, cell, xOffset, yOffset, showCoords);
			CanvasPainter.drawTools(canvas, cell, xOffset, yOffset);
			CanvasPainter.drawCreatures(canvas, cell, xOffset, yOffset, AnimationManager.xAnimiate, AnimationManager.yAnimiate, AnimationManager.zoom);
			CanvasPainter.drawPlayer(canvas, cell, labyrinth.getPlayer(), xOffset, yOffset, AnimationManager.xAnimiate, AnimationManager.yAnimiate, AnimationManager.zoom, showCoords);
		} else {
			System.out.println("REAL_LEVEL = "+level);
			BitmapPainter.drawCell(canvas, cell, xOffset, yOffset, showCoords);
			BitmapPainter.drawCreatures(canvas, cell, player, xOffset, yOffset, AnimationManager.xAnimiate, AnimationManager.yAnimiate, hasNotRoared);
			BitmapPainter.drawTools(canvas, cell, player, xOffset, yOffset);
		}
		CanvasPainter.drawCoords(canvas, cell, xOffset, yOffset, showCoords);
	}


	private boolean isTutorial() {
		if(level == 1) return true;
		return false;
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
//			if (true) {
				try {
					Cell playerPosition = labyrinth.getPlayer().getPosition();
					exitDirection = MetricsService.getDirectionFromPosition(xdown, ydown, xup, yup);
					sameLevel = labyrinth.move(labyrinth.getPlayer(), exitDirection);
					if (!sameLevel) {
						AnimationManager.startAnimation();
						exitDirection = labyrinth.getExitCellWall();
						new SoundSource(labyrinth.getPlayer(), SoundSource.EXIT, mainActivity);
					} else {
						if (labyrinth.getPlayer().getPosition().equals(playerPosition)) {

							vb.vibrate(VIBRATION_TIME_MILLI);
							new SoundSource(labyrinth.getPlayer(), SoundSource.BUMP, mainActivity);
							new SoundSource(labyrinth.getPlayer(), SoundSource.PAIN, mainActivity);
						}
					}
				} catch (Exception e) {
					ErrorLogger.log(this, e, "");
				}
				return true;
			}
//		}
		return false;
	}

	private void goToNextLevel() throws Exception {
		level = Level.next();
		CharSequence msg = "LEVEL " + level;
		Toast toast = Toast.makeText(this.getContext(), msg, Toast. LENGTH_LONG);
		toast.show();

		labyrinth = Level.genLabyrinth();
		cells = labyrinth.getCells();
		player.setPosition(labyrinth.getEntrance());
		labyrinth.setPlayer(player);
		MetricsService.centerSmallLabyrinths(labyrinth.getDimension());
	}


}
