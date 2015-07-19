package com.ahtsho.labyrinthandroid.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import android.widget.NumberPicker.OnValueChangeListener;

import com.ahtsho.labyrinthandroid.R;
import com.ahtsho.labyrinthandroid.view.LabyrinthView;

import core.Game;
import core.Labyrinth;
import core.Levels;
import core.Player;

@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
public class GameActivity extends Activity {
	private static int CELL_WIDTH = 200;
	private static int CELL_HEIGHT = 200;
	private static int LEFT_PADDING = 50;
	private static int TOP_PADDING = 100;
	private static int LAB_DIMENSION = 3;
	int level = 1;
	Object test = new Object();
	Paint paint = new Paint();
	Paint paintBroken = new Paint();
	Paint playerPaint = new Paint();
	Paint textPaint = new Paint();
	Game game = new Game(false, false, false);
	Player p = new Player();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		final Context gameActivity = this;
		paint.setColor(Color.GREEN);
		paint.setStrokeWidth(10);
		paint.setStyle(Paint.Style.STROKE);

		paintBroken.setColor(Color.GRAY);
		paintBroken.setStrokeWidth(3);
//		paintBroken
//				.setPathEffect(new DashPathEffect(new float[] { 20, 20 }, 0));
		paintBroken.setStyle(Paint.Style.FILL_AND_STROKE);

		playerPaint.setColor(Color.YELLOW);
		playerPaint.setStrokeWidth(10);
		playerPaint.setStyle(Paint.Style.FILL);

		textPaint.setColor(Color.RED);
		textPaint.setTextSize(25);
		textPaint.setStyle(Paint.Style.STROKE);
		
		p.setName("F");

		// while (!Levels.isLast(level)) {

		Labyrinth lab = null;
		try {
			lab = Levels.genLabyrinth(level + 2, p);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// if(Levels.levelChanged){
		// Levels.levelChanged = false;
		// Console.printLevel(level);
		View labView = new LabyrinthView(gameActivity, paint, paintBroken,
				playerPaint, textPaint,lab);
		setContentView(labView);

		// console.printMoveMsg();
		// }

		// }
	}

}