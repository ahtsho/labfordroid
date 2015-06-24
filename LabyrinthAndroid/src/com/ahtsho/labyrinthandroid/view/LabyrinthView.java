package com.ahtsho.labyrinthandroid.view;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

public class LabyrinthView extends View {

	private ArrayList<Rect> labyrinth;
	private Paint paint;

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		canvas.drawColor(Color.BLACK);
		for(Rect cell: labyrinth){
			canvas.drawRect(cell, paint);
//			Log.d("MY", "left="+cell.left+", top="+cell.top+", left="+cell.left+", right="+cell.right);
		}
	}

	

	public LabyrinthView(Context context, Paint aPaint, ArrayList<Rect> aLab) {
		super(context);
		paint = aPaint;
		labyrinth = aLab;
	}

}
