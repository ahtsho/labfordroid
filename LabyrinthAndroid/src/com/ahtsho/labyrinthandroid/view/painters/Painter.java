package com.ahtsho.labyrinthandroid.view.painters;

import java.util.HashMap;

import android.graphics.Paint;

public class Painter {

	protected static Paint paintCell;
	protected static Paint paintEmptyWall;
	protected static Paint paintPlayer;
	protected static Paint paintCreature;
	protected static Paint paintText;

	public Painter(HashMap<String, Paint> paints) {
		paintCell = paints.get("cell");
		paintEmptyWall = paints.get("empry_wall");
		paintPlayer = paints.get("player");
		paintCreature = paints.get("creature");
		paintText = paints.get("text");
	}
}
