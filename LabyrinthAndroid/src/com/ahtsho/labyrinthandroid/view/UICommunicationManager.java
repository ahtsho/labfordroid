package com.ahtsho.labyrinthandroid.view;

import game.Level;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ahtsho.labyrinthandroid.R;
import com.ahtsho.labyrinthandroid.service.GameService;
import com.ahtsho.labyrinthandroid.service.SoundSource;

public class UICommunicationManager {

	private static AlertDialog.Builder alertDialogBuilder = null;
	private static Activity activity = null;

	private static android.content.DialogInterface.OnClickListener quitClickListener = null;
	private static android.content.DialogInterface.OnClickListener restartClickListener = null;

	private static android.content.DialogInterface.OnClickListener createQuitDialog() {
		if (quitClickListener == null) {
			System.out.println("quitClickListener creating");
			quitClickListener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					new SoundSource(SoundSource.SYSTEM_STATUS_END_GAME, activity);
					activity.finish();
				}
			};
		}
		return quitClickListener;
	}

	@SuppressLint("NewApi")
	private static android.content.DialogInterface.OnClickListener createRestartDialog() {
		if (restartClickListener == null) {
			System.out.println("restartClickListener creating");
			restartClickListener = new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					Level.goTo(-(Level.currentLevel - 3));
					new SoundSource(SoundSource.SYSTEM_STATUS_RESTART_GAME, activity);
					activity.recreate();
				}
			};
		}
		return restartClickListener;
	}

	private static AlertDialog alertDialog = null;

	public static void showGameOverAlertDialog(Activity anActivity) {
		if (activity == null) {
			activity = anActivity;
		}
		if (alertDialogBuilder == null) {
			System.out.println("alertDialogBuilder created");
			alertDialogBuilder = new AlertDialog.Builder(activity);
			alertDialogBuilder.setMessage("GAME OVER");
			alertDialogBuilder.setPositiveButton("Restart", createRestartDialog());
			alertDialogBuilder.setNegativeButton("Quit", createQuitDialog());
		}
		alertDialogBuilder.create();
		if (alertDialog == null) {
			alertDialog = alertDialogBuilder.create();
		}
		alertDialog.show();
		SoundSource.stopBackgoundMusic();
		SoundSource.play(SoundSource.SYSTEM_STATUS_GAME_OVER, activity);

	}

	public static void updateActionBar(View view, float playerLife) {
		try {
			((TextView) view.getRootView().findViewById(R.id.title_bar_text)).setText(" " + playerLife);
		} catch (Exception e) {
			System.out.println("Call requires API level 11");
		}
	}
	public static void showLevelChangedMessage(Activity anActivity) {
		if (activity == null) {
			activity = anActivity;
		}
		CharSequence msg = "LEVEL " + GameService.getLevel();
		Toast toast = Toast.makeText(activity, msg, Toast.LENGTH_LONG);
		toast.show();
	}
}
