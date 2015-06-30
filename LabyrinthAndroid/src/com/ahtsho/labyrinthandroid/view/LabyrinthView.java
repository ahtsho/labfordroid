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
import android.view.View;

public class LabyrinthView extends View {

	private Labyrinth labyrinth;
	private Paint paint;
	private Paint paintOpen;

	private static int CELL_WIDTH = 200;
	private static int CELL_HEIGHT = 200;
	private static int LEFT_PADDING = 50;
	private static int TOP_PADDING = 100;
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		canvas.drawColor(Color.BLACK);
		ArrayList<Cell> cells = labyrinth.getCells();
		for(int i = 0; i < cells.size(); i++){
			drawCell(canvas, cells.get(i));
//			canvas.drawRect(cell, paint);
		}
	}

	private void drawCell(Canvas canvas, Cell cell){
		if(cell.isWest()){
			canvas.drawLine(cell.getCol()*CELL_WIDTH,
					cell.getRow()*CELL_HEIGHT,
					cell.getCol()*CELL_WIDTH,
					(cell.getRow()+1)*CELL_HEIGHT, paint);//west
		}else {
			canvas.drawLine(cell.getCol()*CELL_WIDTH,
					cell.getRow()*CELL_HEIGHT,
					cell.getCol()*CELL_WIDTH,
					(cell.getRow()+1)*CELL_HEIGHT, paintOpen);
		}
		if(cell.isNorth()){
			canvas.drawLine(cell.getCol()*CELL_WIDTH,
					cell.getRow()*CELL_HEIGHT,
					(cell.getCol()+1)*CELL_WIDTH,
					cell.getRow()*CELL_HEIGHT, paint);//north
		}else {
			canvas.drawLine(cell.getCol()*CELL_WIDTH,
					cell.getRow()*CELL_HEIGHT,
					(cell.getCol()+1)*CELL_WIDTH,
					cell.getRow()*CELL_HEIGHT, paintOpen);
		}
		if(cell.isSouth()){
			canvas.drawLine(cell.getCol()*CELL_WIDTH,
					(cell.getRow()+1)*CELL_HEIGHT,
					(cell.getCol()+1)*CELL_WIDTH,
					(cell.getRow()+1)*CELL_HEIGHT, paint);//south
		}else{
			canvas.drawLine(cell.getCol()*CELL_WIDTH,
					(cell.getRow()+1)*CELL_HEIGHT,
					(cell.getCol()+1)*CELL_WIDTH,
					(cell.getRow()+1)*CELL_HEIGHT, paintOpen);
		}
		if(cell.isEast()){
			canvas.drawLine((cell.getCol()+1)*CELL_WIDTH,
					cell.getRow()*CELL_HEIGHT,
					(cell.getCol()+1)*CELL_WIDTH,
					(cell.getRow()+1)*CELL_HEIGHT, paint);//east
		}else {
			canvas.drawLine((cell.getCol()+1)*CELL_WIDTH,
					cell.getRow()*CELL_HEIGHT,
					(cell.getCol()+1)*CELL_WIDTH,
					(cell.getRow()+1)*CELL_HEIGHT, paintOpen);
		}
	}

	public LabyrinthView(Context context, Paint aPaint,Paint anotherPaint, Labyrinth aLab) {
		super(context);
		paint = aPaint;
		paintOpen = anotherPaint;
		labyrinth = aLab;
	}

}
