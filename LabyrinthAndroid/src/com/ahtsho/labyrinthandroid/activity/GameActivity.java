package com.ahtsho.labyrinthandroid.activity;

import java.util.ArrayList;

import com.ahtsho.labyrinthandroid.R;
import com.ahtsho.labyrinthandroid.view.LabyrinthView;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;

public class GameActivity extends Activity {
	private static int CELL_WIDTH = 200;
	private static int CELL_HEIGHT = 200;
	private static int LEFT_PADDING = 50;
	private static int TOP_PADDING = 100;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);

		Paint paint = new Paint();
		paint.setColor(Color.GREEN);
		paint.setStrokeWidth(5);
		paint.setStyle(Paint.Style.STROKE);

		ArrayList<Rect> labyrinth = new ArrayList<Rect>();
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 3; col++) {
				labyrinth.add(new Rect(row * CELL_WIDTH + LEFT_PADDING, col
						* CELL_HEIGHT + TOP_PADDING, (row + 1) * CELL_WIDTH
						+ LEFT_PADDING, (col + 1) * CELL_HEIGHT + TOP_PADDING));
			}
		}

		setContentView(new LabyrinthView(this, paint, labyrinth));
	}
}