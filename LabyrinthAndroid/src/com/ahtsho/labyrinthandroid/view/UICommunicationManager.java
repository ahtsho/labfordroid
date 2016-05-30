package com.ahtsho.labyrinthandroid.view;

import model.game.Level;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ahtsho.labyrinthandroid.R;
import com.ahtsho.labyrinthandroid.activity.GameActivity;
import com.ahtsho.labyrinthandroid.service.GameService;
import com.ahtsho.labyrinthandroid.service.SoundSource;

public class UICommunicationManager {

	private static AlertDialog.Builder alertDialogBuilder = null;
	private static Activity activity = null;

	private static android.content.DialogInterface.OnClickListener quitClickListener = null;
	private static android.content.DialogInterface.OnClickListener restartClickListener = null;

	private static android.content.DialogInterface.OnClickListener createQuitDialog() {
		if (quitClickListener == null) {
			quitClickListener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					new SoundSource(SoundSource.SYSTEM_STATUS_END_GAME,
							activity);
//					activity.finish();
//					Process.killProcess(Process.myPid());
					System.exit(2);
				}
			};
		}
		return quitClickListener;
	}

	@SuppressLint("NewApi")
	private static android.content.DialogInterface.OnClickListener createRestartDialog() {
		if (restartClickListener == null) {
			restartClickListener = new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					Level.goTo(-(Level.currentLevel - 3));
					new SoundSource(SoundSource.SYSTEM_STATUS_RESTART_GAME, activity);
					Intent myIntent = new Intent(activity, GameActivity.class);
					myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);	
					activity.startActivity(myIntent);
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
		if (activity != null) {
			if (alertDialogBuilder == null) {
				alertDialogBuilder = new AlertDialog.Builder(activity);
				alertDialogBuilder.setMessage(R.string.alert_game_over);
				alertDialogBuilder.setPositiveButton(R.string.alert_restart,
						createRestartDialog());
				alertDialogBuilder
						.setNegativeButton(R.string.alert_quit, createQuitDialog());
			}
			alertDialogBuilder.create();
			if (alertDialog == null) {
				alertDialog = alertDialogBuilder.create();
			}
			alertDialog.show();
			SoundSource.stopBackgoundMusic();
			SoundSource.play(SoundSource.SYSTEM_STATUS_GAME_OVER, activity);
		}

	}

	public static void updateActionBar(View view, float playerLife) {
		try {
			//replace text with R.string.toolbar_text_level
			((TextView) view.getRootView().findViewById(R.id.title_bar_text))
					.setText(" " + playerLife);
		} catch (Exception e) {
			System.out.println("Call requires API level 11");
		}
		
		((TextView) view.getRootView().findViewById(R.id.title_bar_level))
		.setText(GameService.getLevelName());
	}

	public static void showLevelChangedMessage(Activity anActivity) {
		if (activity == null) {
			activity = anActivity;
		}
		CharSequence msg = GameService.getLevelName();
		Toast toast = Toast.makeText(activity, msg, Toast.LENGTH_LONG);
		toast.show();
	}
}
