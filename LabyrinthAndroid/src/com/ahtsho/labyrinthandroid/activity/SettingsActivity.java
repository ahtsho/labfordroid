package com.ahtsho.labyrinthandroid.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ahtsho.labyrinthandroid.R;
import com.ahtsho.labyrinthandroid.service.SoundSource;

public class SettingsActivity extends Activity {
	private int chosenPlayer = 0;
	private String name = "";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.activity_settings);
		SoundSource.playBackgroundMusic(this.getApplicationContext());
		handlePlayerChoice(R.id.imageButton1,R.drawable.face1,R.id.editText_player_1);
		handlePlayerChoice(R.id.imageButton2,R.drawable.face2,R.id.editText_player_2);
		handlePlayerChoice(R.id.imageButton3,R.drawable.face3,R.id.editText_player_3);
		handlePlayerChoice(R.id.imageButton4,R.drawable.face4,R.id.editText_player_4);
		
		Button play = (Button) findViewById(R.id.button1);
		play.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent gameIntent = new Intent(SettingsActivity.this,GameActivity.class);
				
				gameIntent.putExtra("player", chosenPlayer);//player_1_name.getText()
				gameIntent.putExtra("name", name);
				
				startActivity(gameIntent);
				finish();
			}
		});

	}

	private void handlePlayerChoice(int imagebutton, int face, final int editTextPlayer) {
		final ImageButton player_1 = (ImageButton) findViewById(imagebutton);
		player_1.setTag(face);
		player_1.setOnClickListener(new View.OnClickListener() {
		final EditText player_1_name = (EditText) findViewById(editTextPlayer);
		
			@Override
			public void onClick(View v) {
				Log.d("SettingsActiviy", player_1.getTag().toString());
				chosenPlayer = (Integer) player_1.getTag();
				Toast toast = Toast.makeText(getApplicationContext(), "you have chosen "+player_1_name.getText().toString(),
						Toast.LENGTH_SHORT);
				toast.show();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

}
