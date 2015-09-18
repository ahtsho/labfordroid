package com.ahtsho.labyrinthandroid.activity;

import game.*;
import infrastructure.*;
import creatures.*;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.ahtsho.labyrinthandroid.R;
import com.ahtsho.labyrinthandroid.view.LabyrinthView;


@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
public class GameActivity extends Activity {
	
//	private int level = 1;
	private Paint paint = new Paint();
	private Paint paintBroken = new Paint();
	private Paint playerPaint = new Paint();
	private Paint creaturePaint = new Paint();
	private Paint textPaint = new Paint();
	private Player player = new Player("F", 3);

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

		creaturePaint.setColor(Color.RED);
		creaturePaint.setStrokeWidth(10);
		creaturePaint.setStyle(Paint.Style.FILL);
		
		textPaint.setColor(Color.RED);
		textPaint.setTextSize(25);
		textPaint.setStyle(Paint.Style.STROKE);
		

		Labyrinth lab = null;
		try {
			lab = Level.genLabyrinth();
			player.setPosition(lab.getEntrance());
			lab.setPlayer(player);
			
			//lab = Level.genLabyrinth(level + 2, p);
		} catch (Exception e) {
			Log.e("GameActivity","Error:"+e.getMessage());
		}
		setContentView(new LabyrinthView(gameActivity, paint, paintBroken,playerPaint,textPaint,creaturePaint,(Integer)getIntent().getExtras().get("player"),lab));

		
		ActionBar bar = getActionBar(); 
//		bar.setTitle("Life: "+player.getLife());
		bar.setDisplayShowHomeEnabled(false);
		bar.setDisplayShowTitleEnabled(false);
		
		View abLay =  getLayoutInflater().inflate(R.layout.maintitlebar, null);
		TextView tv = (TextView) abLay.findViewById(R.id.title_bar_text);
		tv.setText(" Life "+player.getLife());
		
		bar.setCustomView(abLay);
		bar.setDisplayShowCustomEnabled(true);
	}

}