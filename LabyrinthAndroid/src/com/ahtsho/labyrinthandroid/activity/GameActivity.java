package com.ahtsho.labyrinthandroid.activity;

import java.util.HashMap;

import game.*;
import infrastructure.*;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Build;
import android.os.Bundle;

import com.ahtsho.labyrinthandroid.R;
import com.ahtsho.labyrinthandroid.service.SoundSource;
import com.ahtsho.labyrinthandroid.util.ErrorLogger;
import com.ahtsho.labyrinthandroid.view.LabyrinthView;


@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
public class GameActivity extends Activity {
	private Labyrinth lab = null;
	private HashMap<String, Paint> paints = new HashMap<String, Paint>();
	private Context base;
	
	public void restartGame(){
		Intent i = new Intent(GameActivity.this, SplashActivity.class);
		startActivity(i);
		finish();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		base = getBaseContext();
		setContentView(R.layout.activity_game);
		SoundSource.playBackgroundMusic(this.getApplicationContext());
		setPaints();		
		try {
			lab = Level.genLabyrinth();
		} catch (Exception e) {
			ErrorLogger.log(this, e, "");
		}
		
		setContentView(new LabyrinthView(this, paints,R.drawable.face1,lab,Level.getPath()));
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
		paintBroken.setStrokeWidth(1);
//		paintBroken.setAntiAlias(true);
//		paintBroken.setDither(true);
		paintBroken.setStyle(Paint.Style.STROKE);
		paintBroken.setPathEffect(new DashPathEffect(new float[]{ 20, 30, 20}, 0));
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