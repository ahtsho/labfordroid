package com.ahtsho.labyrinthandroid.activity;

import java.util.HashMap;

import game.*;
import infrastructure.*;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import com.ahtsho.labyrinthandroid.R;
import com.ahtsho.labyrinthandroid.view.LabyrinthView;


@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
public class GameActivity extends Activity {
	private Labyrinth lab = null;
	private HashMap<String, Paint> paints = new HashMap<String, Paint>();

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		setPaints();		
		try {
			lab = Level.genLabyrinth();
			System.out.println( "Player "+lab.getPlayer().getName());
		} catch (Exception e) {
			Log.e("GameActivity","Error:"+e.getMessage());
		}

		setContentView(new LabyrinthView(this, paints,(Integer)getIntent().getExtras().get("player"),lab));
		setActionBar();
	}

	private void setActionBar() {
		ActionBar bar = getActionBar(); 
		bar.setDisplayShowHomeEnabled(false);
		bar.setDisplayShowTitleEnabled(false);
		bar.setCustomView(getLayoutInflater().inflate(R.layout.maintitlebar, null));
		bar.setDisplayShowCustomEnabled(true);
	}

	private void setPaints() {
		Paint paint = new Paint();	
		paint.setColor(Color.GREEN);
		paint.setStrokeWidth(10);
		paint.setStyle(Paint.Style.STROKE);
		paints.put("cell", paint);
		
		Paint paintBroken = new Paint();
		paintBroken.setColor(Color.GRAY);
		paintBroken.setStrokeWidth(3);
		paintBroken.setStyle(Paint.Style.FILL_AND_STROKE);
		paints.put("empry_wall", paintBroken);
		
		Paint playerPaint = new Paint();
		playerPaint.setColor(Color.YELLOW);
		playerPaint.setStrokeWidth(10);
		playerPaint.setStyle(Paint.Style.FILL);
		paints.put("player", playerPaint);

		Paint creaturePaint = new Paint();
		creaturePaint.setColor(Color.RED);
		creaturePaint.setStrokeWidth(10);
		creaturePaint.setStyle(Paint.Style.FILL);
		paints.put("creature", creaturePaint);
		
		Paint textPaint = new Paint();
		textPaint.setColor(Color.RED);
		textPaint.setTextSize(25);
		textPaint.setStyle(Paint.Style.STROKE);
		paints.put("text", textPaint);
	}

}