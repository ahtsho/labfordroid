package com.ahtsho.labyrinthandroid.view.painters;

import java.util.HashMap;

import tools.Tool;
import infrastructure.Cell;
import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import com.ahtsho.labyrinthandroid.service.MetricsService;

import creatures.Creature;
import creatures.Player;

public class CanvasPainter extends Painter{
	
	public CanvasPainter(Activity anActivity, View aView, HashMap<String, Paint> paints) {
		super(anActivity, aView, paints);
	}
	
	public static void drawCell(Canvas canvas, Cell cell, float xOffset, float yOffset, boolean showCoords){
		drawWestWall(canvas, cell, xOffset, yOffset, showCoords);
		drawNorthWall(canvas, cell, xOffset, yOffset, showCoords);
		drawSouthWall(canvas, cell, xOffset, yOffset, showCoords);
		drawEastWall(canvas, cell, xOffset, yOffset, showCoords);
	}
	public static void drawWestWall(Canvas canvas, Cell cell, float xOffset, float yOffset, boolean showCoords) {
		if (cell.isWest()) {
			canvas.drawLine(MetricsService.getXFromCell(cell, xOffset), MetricsService.getYFromCell(cell, yOffset),
					MetricsService.getXFromCell(cell, xOffset), MetricsService.getYFormNextCell(cell, yOffset), paintCell);

		} else {
			canvas.drawLine(MetricsService.getXFromCell(cell, xOffset), MetricsService.getYFromCell(cell, yOffset),
					MetricsService.getXFromCell(cell, xOffset), MetricsService.getYFormNextCell(cell, yOffset), paintEmptyWall);
		}
		if (showCoords) {
			canvas.drawText("W(" + MetricsService.getXFromCell(cell, xOffset) + "," + MetricsService.getYFromCell(cell, yOffset) + ")",
					MetricsService.getXFromCell(cell, -10 + xOffset), MetricsService.getYFromCell(cell, -30 + yOffset), paintText);
			canvas.drawText("(" + MetricsService.getXFromCell(cell, xOffset) + "," + MetricsService.getYFormNextCell(cell, yOffset) + ")",
					MetricsService.getXFromCell(cell, -10 + xOffset), MetricsService.getYFormNextCell(cell, 20 + yOffset), paintText);
		}
	}

	public static void drawNorthWall(Canvas canvas, Cell cell, float xOffset, float yOffset, boolean showCoords) {
		if (cell.isNorth()) {
			canvas.drawLine(MetricsService.getXFromCell(cell, xOffset), MetricsService.getYFromCell(cell, yOffset),
					MetricsService.getXFromNextCell(cell, xOffset), MetricsService.getYFromCell(cell, yOffset), paintCell);// north
		} else {
			canvas.drawLine(MetricsService.getXFromCell(cell, xOffset), MetricsService.getYFromCell(cell, yOffset),
					MetricsService.getXFromNextCell(cell, xOffset), MetricsService.getYFromCell(cell, yOffset), paintEmptyWall);
		}
		if (showCoords) {
		}
	}

	public static void drawSouthWall(Canvas canvas, Cell cell, float xOffset, float yOffset, boolean showCoords) {
		if (cell.isSouth()) {
			canvas.drawLine(MetricsService.getXFromCell(cell, xOffset), MetricsService.getYFormNextCell(cell, yOffset),
					MetricsService.getXFromNextCell(cell, xOffset), MetricsService.getYFormNextCell(cell, yOffset), paintCell);// south
		} else {
			canvas.drawLine(MetricsService.getXFromCell(cell, xOffset), MetricsService.getYFormNextCell(cell, yOffset),
					MetricsService.getXFromNextCell(cell, xOffset), MetricsService.getYFormNextCell(cell, yOffset), paintEmptyWall);
		}
		if (showCoords) {
		}
	}

	public static void drawEastWall(Canvas canvas, Cell cell, float xOffset, float yOffset, boolean showCoords) {
		if (cell.isEast()) {
			canvas.drawLine(MetricsService.getXFromNextCell(cell, xOffset), MetricsService.getYFromCell(cell, yOffset),
					MetricsService.getXFromNextCell(cell, xOffset), MetricsService.getYFormNextCell(cell, yOffset), paintCell);// east
		} else {
			canvas.drawLine(MetricsService.getXFromNextCell(cell, xOffset), MetricsService.getYFromCell(cell, yOffset),
					MetricsService.getXFromNextCell(cell, xOffset), MetricsService.getYFormNextCell(cell, yOffset), paintEmptyWall);
		}
		if (showCoords) {
		}
	}

	public static void drawPlayer(Canvas canvas, Cell cell,Player player, float xOffset, float yOffset, float xAnimiate, float yAnimiate,float zoom, boolean showCoords) {
		if (player != null) {
			if (player.getPosition().equals(cell)) {
				canvas.drawCircle(MetricsService.getXOfCellCenter(cell, xOffset) + xAnimiate, MetricsService.getYOfCellCenter(cell, yOffset)
						+ yAnimiate, MetricsService.getRadiusOfCircle() + zoom, paintPlayer);
				if (showCoords) {
					canvas.drawText(MetricsService.getXOfCellCenter(cell, xOffset) + ", " + MetricsService.getYOfCellCenter(cell, yOffset),
							MetricsService.getXOfCellCenter(cell, 70 + xOffset), MetricsService.getYOfCellCenter(cell, -35 + yOffset), paintText);
				}
			}
		}
	}

	public static void drawCreatures(Canvas canvas, Cell cell, float xOffset, float yOffset,float xAnimiate, float yAnimiate,float zoom) {
		if (cell.getHosts().size() == 0 && cell.getTools().size() == 0) {
		} else if (cell.getHosts().size() > 0) {
			for (Creature p : cell.getHosts()) {
				if (p instanceof Player) {
					canvas.drawCircle(MetricsService.getXOfCellCenter(cell, xOffset) + xAnimiate, MetricsService.getYOfCellCenter(cell, yOffset)
							+ yAnimiate, MetricsService.getRadiusOfCircle() + zoom, paintPlayer);
				} else {
					canvas.drawCircle(MetricsService.getXOfCellCenter(cell, xOffset) + xAnimiate, MetricsService.getYOfCellCenter(cell, yOffset)
							+ yAnimiate, MetricsService.getRadiusOfCircle() + zoom, paintCreature);
				}
			}
		}
	}

	public static void drawTools(Canvas canvas, Cell cell, float xOffset, float yOffset) {
		if (cell.getTools().size() > 0) {
			for (Tool t : cell.getTools()) {
				canvas.drawRect(
						new Rect((int) (MetricsService.getXFromCell(cell, xOffset) + MetricsService.CELL_WIDTH / 3), (int) (MetricsService
								.getYFromCell(cell, yOffset) + MetricsService.CELL_HEIGHT * 1 / 4), (int) (MetricsService.getXFromNextCell(cell,
								xOffset) - MetricsService.CELL_WIDTH / 3),
								(int) (MetricsService.getYFormNextCell(cell, yOffset) - MetricsService.CELL_HEIGHT * 1 / 4)), paintCreature);
			}
		}
	}
	
	public static void drawCoords(Canvas canvas, Cell cell, float xOffset, float yOffset, boolean showCoords) {
		if (showCoords) {
			canvas.drawText("[" + cell.getRow() + "," + cell.getCol() + "]", MetricsService.getXOfCellCenter(cell, 25 + xOffset),
					MetricsService.getYOfCellCenter(cell, -5 + yOffset), paintText);
		}
	}
}
