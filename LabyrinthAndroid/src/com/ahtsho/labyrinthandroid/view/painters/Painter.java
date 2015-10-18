package com.ahtsho.labyrinthandroid.view.painters;

import java.util.HashMap;

import android.app.Activity;
import android.graphics.Paint;
import android.view.View;

public class Painter {

	protected static Activity activity;
	protected static View view;
	protected static Paint paintCell;
	protected static Paint paintEmptyWall;
	protected static Paint paintPlayer;
	protected static Paint paintCreature;
	protected static Paint paintText;

	public Painter(Activity anActivity, View aView, HashMap<String, Paint> paints) {
		activity = anActivity;
		view = aView;
		paintCell = paints.get("cell");
		paintEmptyWall = paints.get("empry_wall");
		paintPlayer = paints.get("player");
		paintCreature = paints.get("creature");
		paintText = paints.get("text");
	}
}
