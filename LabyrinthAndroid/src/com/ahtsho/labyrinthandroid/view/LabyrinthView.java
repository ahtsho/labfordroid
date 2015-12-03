package com.ahtsho.labyrinthandroid.view;

import game.Level;
import infrastructure.*;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import com.ahtsho.labyrinthandroid.service.AnimationService;
import com.ahtsho.labyrinthandroid.service.GameService;
import com.ahtsho.labyrinthandroid.service.MetricsService;
import com.ahtsho.labyrinthandroid.service.SoundSource;
import com.ahtsho.labyrinthandroid.service.VibriationService;
import com.ahtsho.labyrinthandroid.util.ErrorLogger;
import com.ahtsho.labyrinthandroid.view.painters.BitmapPainter;
import com.ahtsho.labyrinthandroid.view.painters.CanvasPainter;
import com.ahtsho.labyrinthandroid.view.painters.Painter;
import creatures.Player;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class LabyrinthView extends View {
	private Labyrinth labyrinth;
	private Player player = null;
	private CopyOnWriteArrayList<Cell> cells = null;	
	private boolean sameLevel = true;
	private char exitDirection = ' ';
	private float xdown = 0;
	private float ydown = 0;
	private float xup = 0;
	private float yup = 0;
	private Activity mainActivity;

	public LabyrinthView(Context context, HashMap<String, Paint> paints, Integer playerRes, Labyrinth aLab) {
		super(context);
		mainActivity = (Activity) this.getContext();
		new Painter(mainActivity, this,paints);// constructor for Bitmap and Canvas painters
		new VibriationService(mainActivity);
		labyrinth = aLab;
		cells = labyrinth.getCells();
		player = labyrinth.getPlayer();
		player.setAction(new SoundSource(mainActivity));
		BitmapPainter.setPlayerBitmap(Bitmapper.getBitmap(labyrinth.getPlayer(), this));
		MetricsService.initializeCellDimension(context);
		MetricsService.centerSmallLabyrinths(labyrinth.getDimension());
	}

	@Override
	protected void onDraw(Canvas canvas) {
		UICommunicationManager.updateActionBar(this, labyrinth.getPlayer().getLife());
		super.onDraw(canvas);
		CanvasPainter.drawBackGround(canvas);
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
		AnimationService.animate(exitDirection);
		if(AnimationService.animationEnded()) {
			AnimationService.reset();
			try {
				goToNextLevel();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
	}


	private synchronized void drawCell(Canvas canvas, Cell cell, float xOffset, float yOffset, boolean showCoords) {		
		if(GameService.isTutorial()){
			CanvasPainter.drawCell(canvas, cell, xOffset, yOffset, showCoords);
			CanvasPainter.drawTools(canvas, cell, xOffset, yOffset);
			CanvasPainter.drawCreatures(canvas, cell, xOffset, yOffset, AnimationService.xAnimiate, AnimationService.yAnimiate, AnimationService.zoom);
			CanvasPainter.drawPlayer(canvas, cell, labyrinth.getPlayer(), xOffset, yOffset, AnimationService.xAnimiate, AnimationService.yAnimiate, AnimationService.zoom, showCoords);
		} else {
			BitmapPainter.drawCell(canvas, cell, xOffset, yOffset, showCoords);
			BitmapPainter.drawCreatures(canvas, cell, player, xOffset, yOffset, AnimationService.xAnimiate, AnimationService.yAnimiate);
			BitmapPainter.drawTools(canvas, cell, player, xOffset, yOffset);
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
				try {
					Cell playerPosition = labyrinth.getPlayer().getPosition();
					exitDirection = MetricsService.getDirectionFromPosition(xdown, ydown, xup, yup);
					sameLevel = labyrinth.move(labyrinth.getPlayer(), exitDirection);
					SoundSource.creatureHasProducedSound = false;
					SoundSource.toolHasProducedSound = false;
					if (!sameLevel) {
						AnimationService.startAnimation();
						exitDirection = labyrinth.getExitCellWall();
						new SoundSource(labyrinth.getPlayer(), SoundSource.EXIT, mainActivity);
					} else {
						if (labyrinth.getPlayer().getPosition().equals(playerPosition)) {
							VibriationService.vibrate();
							new SoundSource(labyrinth.getPlayer(), SoundSource.BUMP, mainActivity);
//							new SoundSource(labyrinth.getPlayer(), SoundSource.PAIN, mainActivity);
						}
							
					}
				} catch (Exception e) {
					ErrorLogger.log(this, e, "");
				}
				return true;
			}
		return false;
	}

	private void goToNextLevel() throws Exception {
		GameService.setLevel(Level.next());
		UICommunicationManager.showLevelChangedMessage(mainActivity);
		labyrinth = Level.genLabyrinth();
		cells = labyrinth.getCells();
		player.setPosition(labyrinth.getEntrance());
		labyrinth.setPlayer(player);
		MetricsService.centerSmallLabyrinths(labyrinth.getDimension());
	}

}
