package com.ahtsho.labyrinthandroid.view.painters;

import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import model.creatures.Creature;
import model.creatures.Player;
import model.infrastructure.Cell;
import model.tools.Bomb;
import model.tools.Tool;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import android.view.animation.AnimationSet;

import com.ahtsho.labyrinthandroid.service.BombAnimationService;
import com.ahtsho.labyrinthandroid.service.MetricsService;
import com.ahtsho.labyrinthandroid.service.SoundSource;

public class BitmapPainter extends Painter {
	public BitmapPainter(Activity anActivity, View aView,
			HashMap<String, Paint> paints) {
		super(anActivity, aView, paints);
	}

	private static Texture texture = null;
	private static Bitmap playerBitmap = null;

	public static void setTexture(Texture t) {
		texture = t;
	}

	public static void setPlayerBitmap(Bitmap b) {
		playerBitmap = b;
	}

	public static void drawCell(Canvas canvas, Cell cell, float xOffset,
			float yOffset, float zoom) {
		drawBitmapCellFloor(canvas, cell, xOffset, yOffset,zoom);
		drawBitmapWestWall(canvas, cell, xOffset, yOffset, zoom);
		drawBitmapNorthWall(canvas, cell, xOffset, yOffset, zoom);
		drawBitmapSouthWall(canvas, cell, xOffset, yOffset, zoom);
		drawBitmapEastWall(canvas, cell, xOffset, yOffset, zoom);
	}

	public static void drawBitmapCellFloor(Canvas canvas, Cell cell,
			float xOffset, float yOffset, float zoom) {
		Rect dst = new Rect((int) MetricsService.getXFromCell(cell, xOffset,-zoom),
				(int) MetricsService.getYFromCell(cell, yOffset,-zoom),
				(int) MetricsService.getXFromNextCell(cell, xOffset,zoom),
				(int) MetricsService.getYFormNextCell(cell, yOffset,zoom));
		canvas.drawBitmap(
				Bitmapper.getBitmap(cell, Bitmapper.CELL_FLOOR, view), null,
				dst, paintCell);
	}

	public static void drawBitmapEastWall(Canvas canvas, Cell cell,
			float xOffset, float yOffset, float zoom) {
		if (cell.isEast()) {
			Rect dst = new Rect(
					(int) MetricsService.getXFromNextCell(cell,xOffset,zoom) 
							- MetricsService.WALL_THICKNESS_LEFT,
					(int) MetricsService.getYFromCell(cell, yOffset,-zoom)
							- MetricsService.WALL_THICKNESS_TOP,
					(int) MetricsService.getXFromNextCell(cell, xOffset,zoom)
							+ MetricsService.WALL_THICKNESS_RIGHT,
					(int) MetricsService.getYFormNextCell(cell, yOffset,zoom)
							+ MetricsService.WALL_THICKNESS_BOTTOM);

			canvas.drawBitmap(Bitmapper.getBitmap(cell, Cell.EAST, view), null,
					dst, paintCell);
		}
		
	}

	public static void drawBitmapSouthWall(Canvas canvas, Cell cell,
			float xOffset, float yOffset, float zoom) {
		if (cell.isSouth()) {
			Rect dst = new Rect(
					(int) MetricsService.getXFromCell(cell, xOffset,-zoom)
							- MetricsService.WALL_THICKNESS_LEFT,
					(int) MetricsService.getYFormNextCell(cell, yOffset,zoom)
							- MetricsService.WALL_THICKNESS_TOP,
					(int) MetricsService.getXFromNextCell(cell, xOffset,zoom)
							+ MetricsService.WALL_THICKNESS_RIGHT,
					(int) MetricsService.getYFormNextCell(cell, yOffset,zoom)
							+ MetricsService.WALL_THICKNESS_BOTTOM);

			canvas.drawBitmap(Bitmapper.getBitmap(cell, Cell.SOUTH, view),
					null, dst, paintCell);

		}
		
	}

	public static void drawBitmapNorthWall(Canvas canvas, Cell cell,
			float xOffset, float yOffset, float zoom) {
		if (cell.isNorth()) {
			Rect dst = new Rect(
					(int) MetricsService.getXFromCell(cell, xOffset,-zoom)
							- MetricsService.WALL_THICKNESS_LEFT,
					(int) MetricsService.getYFromCell(cell, yOffset,-zoom)
							- MetricsService.WALL_THICKNESS_TOP,
					(int) MetricsService.getXFromNextCell(cell, xOffset,zoom)
							+ MetricsService.WALL_THICKNESS_RIGHT,
					(int) MetricsService.getYFromCell(cell, yOffset,-zoom)
							+ MetricsService.WALL_THICKNESS_BOTTOM);
			canvas.drawBitmap(Bitmapper.getBitmap(cell, Cell.NORTH, view),
					null, dst, paintCell);
		}
		
	}

	public static void drawBitmapWestWall(Canvas canvas, Cell cell,
			float xOffset, float yOffset, float zoom) {
		if (cell.isWest()) {

			Rect dst = new Rect(
					(int) MetricsService.getXFromCell(cell, xOffset,-zoom)
							- MetricsService.WALL_THICKNESS_LEFT,
					(int) MetricsService.getYFromCell(cell, yOffset,-zoom)
							- MetricsService.WALL_THICKNESS_TOP,
					(int) MetricsService.getXFromCell(cell, xOffset,-zoom)
							+ MetricsService.WALL_THICKNESS_RIGHT,
					(int) MetricsService.getYFormNextCell(cell, yOffset,zoom)
							+ MetricsService.WALL_THICKNESS_BOTTOM);
			canvas.drawBitmap(Bitmapper.getBitmap(cell, Cell.WEST, view), null,
					dst, paintCell);
		}
	
	}

	public static void drawBitmapTool(View v, Tool t, Canvas canvas, Cell cell,
			float xOffset, float yOffset, float xAnimiate, float yAnimiate, float zoom,boolean sufferedExplosion) {
		
		int left = (int) (MetricsService.getXFromCell(cell, xOffset,0) + xAnimiate  - zoom + MetricsService.LEFT_MARGIN);//
		int top = (int) (MetricsService.getYFromCell(cell, yOffset,0) + yAnimiate  - zoom + MetricsService.TOP_MARGIN);// 
		int right= (int) (MetricsService.getXFromNextCell(cell, xOffset,0) + xAnimiate  +zoom - MetricsService.LEFT_MARGIN);//
		int bottom=(int) (MetricsService.getYFormNextCell(cell, yOffset,0) + yAnimiate +zoom - MetricsService.TOP_MARGIN);//
		if(t instanceof Bomb){
			canvas.drawBitmap(Bitmapper.getBitmap(t, v, sufferedExplosion), null, new Rect(left,top,right,bottom), null);
		} else {
			canvas.drawBitmap(Bitmapper.getBitmap(t, v,false),
				MetricsService.getXFromCell(cell, xOffset,0)
						+ MetricsService.LEFT_MARGIN_TOOL,
				MetricsService.getYFromCell(cell, yOffset,0)
						+ MetricsService.TOP_MARGIN_TOOL, paintPlayer);
		}

	}

	public static void drawBitmapCreature(View v, Creature c, Canvas canvas,
			Cell cell, float xOffset, float yOffset, float zoom) {
		canvas.drawBitmap(Bitmapper.getBitmap(c, v),
				MetricsService.getXFromCell(cell, xOffset,+zoom) + MetricsService.LEFT_MARGIN,
				MetricsService.getYFromCell(cell, yOffset,-zoom) + MetricsService.TOP_MARGIN, paintPlayer);

	}

	public static void drawBitmapPlayer(Canvas canvas, Cell cell,
			float xOffset, float yOffset, float xAnimiate, float yAnimiate, float zoom) {
		
		int left = (int) (MetricsService.getXFromCell(cell, xOffset,0) + xAnimiate  - zoom + MetricsService.LEFT_MARGIN);//
		int top = (int) (MetricsService.getYFromCell(cell, yOffset,0) + yAnimiate  - zoom + MetricsService.TOP_MARGIN);// 
		int right= (int) (MetricsService.getXFromNextCell(cell, xOffset,0) + xAnimiate  +zoom - MetricsService.LEFT_MARGIN);//
		int bottom=(int) (MetricsService.getYFormNextCell(cell, yOffset,0) + yAnimiate +zoom - MetricsService.TOP_MARGIN);//
		canvas.drawBitmap(playerBitmap, null, new Rect(left,top,right,bottom), null);

	}

	public static void drawTools(Canvas canvas, Cell cell, Player player,
			float xOffset, float yOffset, float xAnimiate, float yAnimiate, float zoom) {
		if (cell.getTools().size() == 0) {
		} else if (cell.getTools().size() > 0) {
			for (Tool t : cell.getTools()) {
				drawBitmapTool(view, t, canvas, cell, xOffset, yOffset, xAnimiate, yAnimiate, zoom,player.sufferedExplosion);
			}
		}
	}

	public static synchronized void drawCreatures(Canvas canvas, Cell cell,
			Player player, float xOffset, float yOffset, float xAnimiate,
			float yAnimiate, float zoom) {
		if (cell.getHosts().size() == 0) {
		} else if (cell.getHosts().size() > 0) {
			CopyOnWriteArrayList<Creature> hosts = cell.getHosts();
			for (Creature p : hosts) {
				if (p instanceof Player) {
					BitmapPainter.drawBitmapPlayer(canvas, cell, xOffset,
							yOffset, xAnimiate, yAnimiate,zoom);
				} else {
					drawBitmapCreature(view, p, canvas, cell, xOffset, yOffset,0);
					if (!SoundSource.creatureHasProducedSound) {
						if (p.getPosition().equals(player.getPosition())) {
							SoundSource.play(p, SoundSource.ANGRY, activity);
//							new SoundSource(player, SoundSource.DAMAGE, activity);
						}
						SoundSource.creatureHasProducedSound = true;
					}
				}
			}
		}
	}
	
	
	public static void drawGuideHand(Canvas canvas, Cell cell,
			float xOffset, float yOffset, float xAnimiate,
			float yAnimiate, boolean finished) {
		if(!finished){
			canvas.drawBitmap(Bitmapper.getBitmap(true, finished, view),
				MetricsService.getXOfCellCenter(cell, xOffset)-40+xAnimiate,
				MetricsService.getYOfCellCenter(cell, yOffset)+yAnimiate, paintPlayer);
		} else {
			canvas.drawBitmap(Bitmapper.getBitmap(true, finished, view),
					MetricsService.getXOfScreenCenter()-(MetricsService.getXOfScreenCenter()/3),
					MetricsService.getYOfScreenCenter()-(MetricsService.getYOfScreenCenter()/3), paintPlayer);
		}

	}

	

	public static void drawExplosion(Canvas canvas, Cell cell, Player player,
			float xOffset, float yOffset, float xAnimiate, float yAnimiate,
			float zoom) {
		int left = (int) (MetricsService.getXFromCell(cell, xOffset,0) + xAnimiate  - zoom + MetricsService.LEFT_MARGIN);//
		int top = (int) (MetricsService.getYFromCell(cell, yOffset,0) + yAnimiate  - zoom + MetricsService.TOP_MARGIN);// 
		int right= (int) (MetricsService.getXFromNextCell(cell, xOffset,0) + xAnimiate  +zoom - MetricsService.LEFT_MARGIN);//
		int bottom=(int) (MetricsService.getYFormNextCell(cell, yOffset,0) + yAnimiate +zoom - MetricsService.TOP_MARGIN);//
		
		canvas.drawBitmap(Bitmapper.getBitmap(new Bomb(1f),view, true), null, new Rect(left,top,right,bottom), null);

		
//		canvas.drawBitmap(Bitmapper.getBitmap(new Bomb(1f),view, true),
//				MetricsService.getXOfCellCenter(cell, xOffset)-40+xAnimiate,
//				MetricsService.getYOfCellCenter(cell, yOffset)+yAnimiate, paintPlayer);			
	}
	


}
