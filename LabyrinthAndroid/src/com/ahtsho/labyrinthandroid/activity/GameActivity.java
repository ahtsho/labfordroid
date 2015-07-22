package com.ahtsho.labyrinthandroid.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import com.ahtsho.labyrinthandroid.R;
import com.ahtsho.labyrinthandroid.view.LabyrinthView;
import core.Labyrinth;
import core.Levels;
import core.Player;

@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
public class GameActivity extends Activity {
	
	private int level = 1;
	private Paint paint = new Paint();
	private Paint paintBroken = new Paint();
	private Paint playerPaint = new Paint();
	private Paint textPaint = new Paint();
	private Player p = new Player();

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
		paintBroken.setStyle(Paint.Style.FILL_AND_STROKE);

		playerPaint.setColor(Color.YELLOW);
		playerPaint.setStrokeWidth(10);
		playerPaint.setStyle(Paint.Style.FILL);

		textPaint.setColor(Color.RED);
		textPaint.setTextSize(25);
		textPaint.setStyle(Paint.Style.STROKE);
		
		p.setName("F");

		Labyrinth lab = null;
		try {
			lab = Levels.genLabyrinth(level + 2, p);
		} catch (Exception e) {
			Log.e("GameActivity","Error:"+e.getMessage());
		}
		
		setContentView(new LabyrinthView(gameActivity, paint, paintBroken,
				playerPaint, textPaint,lab));
	}

}