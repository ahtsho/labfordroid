package com.ahtsho.labyrinthandroid.activity;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;

import com.ahtsho.labyrinthandroid.R;
import com.ahtsho.labyrinthandroid.view.LabyrinthView;

import core.Labyrinth;
import core.Levels;
import core.Player;

public class GameActivity extends Activity {
	private static int CELL_WIDTH = 200;
	private static int CELL_HEIGHT = 200;
	private static int LEFT_PADDING = 50;
	private static int TOP_PADDING = 100;
	private static int LAB_DIMENSION = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);

		Paint paint = new Paint();
		paint.setColor(Color.GREEN);
		paint.setStrokeWidth(10);
		paint.setStyle(Paint.Style.STROKE);

		Paint paintBroken = new Paint();
		paintBroken.setColor(Color.GRAY);
		paintBroken.setStrokeWidth(3);
		paintBroken.setPathEffect(new DashPathEffect(new float[] { 20, 20 }, 0));
		paintBroken.setStyle(Paint.Style.FILL_AND_STROKE);

		Player p = new Player();
		p.setName("F");

		int level = 1;

		
		Labyrinth lab = null;
		try {
			lab = Levels.genLabyrinth(level+2, p);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		setContentView(new LabyrinthView(this, paint, paintBroken, lab));
	}
}