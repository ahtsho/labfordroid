package com.ahtsho.labyrinthandroid.service;

import android.app.Activity;
import android.content.Context;
import android.os.Vibrator;

public class VibriationService {
	private static final int VIBRATION_TIME_MILLI = 100;
	private static Vibrator vibrator;
	private static Activity activity;

	public VibriationService(Activity a) {
		activity = a;
		if (vibrator == null) {
			vibrator = (Vibrator) activity.getSystemService(Context.VIBRATOR_SERVICE);
		}
	}

	public static void vibrate() {
		vibrator.vibrate(VIBRATION_TIME_MILLI);
	}
}
