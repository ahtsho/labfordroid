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
		paintBroken
				.setPathEffect(new DashPathEffect(new float[] { 20, 20 }, 0));
		paintBroken.setStyle(Paint.Style.FILL_AND_STROKE);

//		ArrayList<Cell> labyrinth = new ArrayList<Cell>();
//
//		labyrinth.add(new Cell(true, false, false, false, "F", 0, 0));
//		labyrinth.add(new Cell(true, false, false, true, "F", 0, 1));
//		labyrinth.add(new Cell(true, true, true, true, "F", 0, 2));
//
//		labyrinth.add(new Cell(false, false, true, true, "F", 1, 0));
//		labyrinth.add(new Cell(false, true, true, false, "F", 1, 1));
//		labyrinth.add(new Cell(true, false, false, true, "F", 1, 2));
//
//		labyrinth.add(new Cell(false, true, true, true, "F", 2, 0));
//		labyrinth.add(new Cell(true, true, true, true, "F", 2, 1));
//		labyrinth.add(new Cell(false, false, true, true, "F", 2, 2));
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