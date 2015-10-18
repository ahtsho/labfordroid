package com.ahtsho.labyrinthandroid.util;

import android.util.Log;

public class ErrorLogger {
	public static void log(Object ob, Exception e, String other) {
		Log.e(ob.getClass().getName(), "Error:" + e.getMessage()+" "+other);
	}
}
