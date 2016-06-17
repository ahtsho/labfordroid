package com.ahtsho.labyrinthandroid.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import model.creatures.Player;
import model.game.Level;
import model.infrastructure.Cell;
import model.infrastructure.Labyrinth;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;

import com.ahtsho.labyrinthandroid.service.BombAnimationService;
import com.ahtsho.labyrinthandroid.service.GameService;
import com.ahtsho.labyrinthandroid.service.HandAnimationService;
import com.ahtsho.labyrinthandroid.service.MetricsService;
import com.ahtsho.labyrinthandroid.service.PlayerAnimationService;
import com.ahtsho.labyrinthandroid.service.SoundSource;
import com.ahtsho.labyrinthandroid.service.ToolAnimationService;
import com.ahtsho.labyrinthandroid.service.VibriationService;
import com.ahtsho.labyrinthandroid.util.ErrorLogger;
import com.ahtsho.labyrinthandroid.view.painters.BitmapPainter;
import com.ahtsho.labyrinthandroid.view.painters.Bitmapper;
import com.ahtsho.labyrinthandroid.view.painters.CanvasPainter;
import com.ahtsho.labyrinthandroid.view.painters.Painter;

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
	private ArrayList<Cell> path = null;
	private int pathCurrCellIdx = 0;
	private boolean tutorialFinished = false;
	private boolean showEffects = false;
	
//	private static boolean animateHand = true;
	
	public LabyrinthView(Context context, HashMap<String, Paint> paints, Integer playerRes, Labyrinth aLab, ArrayList<Cell> path) {
		super(context);
		mainActivity = (Activity) this.getContext();
		new Painter(mainActivity, this,paints);// constructor for Bitmap and Canvas painters
		new VibriationService(mainActivity);
		labyrinth = aLab;
		cells = labyrinth.getCells();
		player = labyrinth.getPlayer();
		this.path = path;
		player.setAction(new SoundSource(mainActivity));
		BitmapPainter.setPlayerBitmap(Bitmapper.getBitmap(labyrinth.getPlayer(), this));
		MetricsService.initializeCellDimension(context);
		MetricsService.centerSmallLabyrinths(labyrinth.getDimension());
		HandAnimationService.starthandAnimation();
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
			drawCell(canvas, cells.get(i), MetricsService.getStartingX(), MetricsService.getStartingY(), 0);
			transitToNextLevelWithAnimation();
			if(Player.fell){
				try {
					transitToPrevLevelWithAnimation();
				} catch (Exception e) {
					System.out.println(e.getCause());
					
				}
			}
			if(Player.sufferedExplosion){
				showExplosion();
			}
			
		}
		
		if(GameService.isTutorial() && pathCurrCellIdx < path.size()){
			CanvasPainter.drawGuideHand(canvas, labyrinth.getPlayer().getPosition(), labyrinth.getPlayer(), MetricsService.getStartingX(), MetricsService.getStartingY(),
				HandAnimationService.xAnimiateHand, HandAnimationService.yAnimiateHand,tutorialFinished);
			
			if(!tutorialFinished){
				animateHand(getDoorToNextCell(path.get(pathCurrCellIdx)));
			}
		}
		
		if (player.getLife() == 0) {
			UICommunicationManager.showGameOverAlertDialog(mainActivity);
		} else {
			invalidate();
		}
	}

	private char getDoorToNextCell(Cell currCell) {
		char direction = 0;
		int pathNextCellIdx = pathCurrCellIdx+1;
		if(labyrinth.getCellForDirection(currCell, Cell.NORTH)!=null && labyrinth.getCellForDirection(currCell, Cell.NORTH).equals(path.get(pathNextCellIdx))){
			direction = Cell.NORTH;
		}else if(labyrinth.getCellForDirection(currCell, Cell.EAST)!=null && labyrinth.getCellForDirection(currCell, Cell.EAST).equals(path.get(pathNextCellIdx))){
			direction = Cell.EAST;
		}else if(labyrinth.getCellForDirection(currCell, Cell.SOUTH)!=null && labyrinth.getCellForDirection(currCell, Cell.SOUTH).equals(path.get(pathNextCellIdx))){
			direction = Cell.SOUTH;
		}else if(labyrinth.getCellForDirection(currCell, Cell.WEST)!= null && labyrinth.getCellForDirection(currCell, Cell.WEST).equals(path.get(pathNextCellIdx))){
			direction = Cell.WEST;
		}
		return direction;
	}
	private void animateHand(char wall) {
		HandAnimationService.animate(wall);
		if(HandAnimationService.animationEnded()) {
			HandAnimationService.reset();
		}
		HandAnimationService.starthandAnimation();
		HandAnimationService.animate(wall);
		if(HandAnimationService.animationEnded()) {
			HandAnimationService.reset();
		}
	}
	private void showExplosion() {
		showEffects = true;
		BombAnimationService.explode();
		if(BombAnimationService.animationEnded(BombAnimationService.Type.EXPLOSION)) {
			BombAnimationService.reset(BombAnimationService.Type.EXPLOSION);
			Player.sufferedExplosion=false;
			showEffects = false;
		}		
	}
	private void transitToNextLevelWithAnimation() {
		PlayerAnimationService.slide(exitDirection);
		if(PlayerAnimationService.animationEnded(PlayerAnimationService.Type.SLIDE)) {
			PlayerAnimationService.reset(PlayerAnimationService.Type.SLIDE);
			try {
				goToNextLevel();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
	}

	private void transitToPrevLevelWithAnimation() throws Exception {
		PlayerAnimationService.zoom(PlayerAnimationService.OUT);
		if(PlayerAnimationService.animationEnded(PlayerAnimationService.Type.ZOOM)) {
			PlayerAnimationService.reset(PlayerAnimationService.Type.ZOOM);
			goToPrevLevel();
		}		
	}
	
	private synchronized void drawCell(Canvas canvas, Cell cell, float xOffset, float yOffset, float zoom) {		
		if(GameService.isTutorial()){
			CanvasPainter.drawCell(canvas, cell, xOffset, yOffset, zoom);
			CanvasPainter.drawTools(canvas, cell, xOffset, yOffset);
			CanvasPainter.drawCreatures(canvas, cell, xOffset, yOffset, 
					PlayerAnimationService.xAnimiate, PlayerAnimationService.yAnimiate, PlayerAnimationService.zoom);
			CanvasPainter.drawPlayer(canvas, cell, labyrinth.getPlayer(), xOffset, yOffset, 
					PlayerAnimationService.xAnimiate, PlayerAnimationService.yAnimiate, PlayerAnimationService.zoom, false);
		} else {
			BitmapPainter.drawCell(canvas, cell, xOffset, yOffset, zoom);
			BitmapPainter.drawCreatures(canvas, cell, player, xOffset, yOffset,
					PlayerAnimationService.xAnimiate, PlayerAnimationService.yAnimiate,PlayerAnimationService.zoom);			
			BitmapPainter.drawTools(canvas, cell, player, xOffset, yOffset,ToolAnimationService.xAnimiate,ToolAnimationService.yAnimiate,ToolAnimationService.zoom);
			
			if(showEffects){
				BitmapPainter.drawExplosion(canvas, player.getPosition(), player, 
						MetricsService.getStartingX(), MetricsService.getStartingY(),
						BombAnimationService.xAnimiate,BombAnimationService.yAnimiate, BombAnimationService.zoom);
			}

		}
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
						tutorialFinished = true;
						PlayerAnimationService.startAnimation();
						exitDirection = labyrinth.getExitCellWall();
						new SoundSource(labyrinth.getPlayer(), SoundSource.EXIT, mainActivity);
					} else {
						if (labyrinth.getPlayer().getPosition().equals(playerPosition)) {
							VibriationService.vibrate();
							new SoundSource(labyrinth.getPlayer(), SoundSource.BUMP, mainActivity);
						} else {
							pathCurrCellIdx++;// has moved 
						}
						if(Player.fell){
							new SoundSource(labyrinth.getPlayer(), SoundSource.FALL, mainActivity);
							PlayerAnimationService.startAnimation();
						}
						if(Player.sufferedExplosion){
							BombAnimationService.startAnimation();
							new SoundSource(labyrinth.getPlayer(),SoundSource.EXPLOSION,mainActivity);
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
	
	private void goToPrevLevel() throws Exception {
		Player.fell=false;
		GameService.setLevel(Level.goTo(-2));
		UICommunicationManager.showLevelChangedMessage(mainActivity);
		labyrinth = Level.genLabyrinth();
		cells = labyrinth.getCells();
		player.setPosition(labyrinth.getEntrance());
		labyrinth.setPlayer(player);
		MetricsService.centerSmallLabyrinths(labyrinth.getDimension());
	}

}
