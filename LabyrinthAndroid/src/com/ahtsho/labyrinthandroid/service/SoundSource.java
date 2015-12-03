package com.ahtsho.labyrinthandroid.service;

import interfaces.Performer;
import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.ahtsho.labyrinthandroid.R;

import creatures.Creature;
import creatures.Guard;
import creatures.Player;

public class SoundSource implements Performer {
	public static final int EXIT = 5;
	public static final int BUMP = 6;
	public final static int ANGRY = 3;

	public final static int SYSTEM_STATUS_GAME_OVER = 10;
	public final static int SYSTEM_STATUS_END_GAME = 11;
	public final static int SYSTEM_STATUS_RESTART_GAME = 12;

	private static boolean audioOn = true;

	private static MediaPlayer backgroubdMP = null;
	public static boolean creatureHasProducedSound = false;
	public static boolean toolHasProducedSound = false;
	private Activity activity;

	public static void switchAudio() {
		audioOn = !audioOn;
	}

	public SoundSource(Creature creature, int type, Activity activity) {
		if (audioOn) {
			MediaPlayer mediaPlayer = null;
			if (creature != null) {
				if (creature instanceof Guard) {
					if (type == ANGRY) {
						if (creature.getName() == "G3") {
							mediaPlayer = MediaPlayer.create(
									activity.getApplicationContext(),
									R.raw.monster_hiss);
						} else if (creature.getName() == "G5") {
							mediaPlayer = MediaPlayer.create(
									activity.getApplicationContext(),
									R.raw.monster_hiss);
						} else if (creature.getName() == "G8") {
							mediaPlayer = MediaPlayer.create(
									activity.getApplicationContext(),
									R.raw.monster_hiss);
						} else if (creature.getName() == "G9") {
							mediaPlayer = MediaPlayer.create(
									activity.getApplicationContext(),
									R.raw.monster_hiss);
						}
					}
				} else if (creature instanceof Player) {
					switch (type) {
					case EXIT:
						mediaPlayer = MediaPlayer.create(
								activity.getApplicationContext(), R.raw.exit);
						break;
					case DAMAGE:
						mediaPlayer = MediaPlayer.create(
								activity.getApplicationContext(), R.raw.ouch);
						break;
					case BUMP:
						mediaPlayer = MediaPlayer.create(
								activity.getApplicationContext(), R.raw.bump);
						break;
					case HEAL:
						mediaPlayer = MediaPlayer.create(
								activity.getApplicationContext(),
								R.raw.cracking_up);
						break;
					default:
						break;
					}
				}
			}
			if (mediaPlayer != null) {
				mediaPlayer.start();
			}
		}
	}

	// public SoundSource(Tool tool, Activity activity) {
	// if (audioOn) {
	// MediaPlayer mediaPlayer = null;
	// if (tool instanceof Plaster) {
	// mediaPlayer = MediaPlayer.create(activity.getApplicationContext(),
	// R.raw.bump);
	// } else if (tool instanceof Medicine) {
	// mediaPlayer = MediaPlayer.create(activity.getApplicationContext(),
	// R.raw.bump);
	// } else if (tool instanceof Box) {
	// mediaPlayer = MediaPlayer.create(activity.getApplicationContext(),
	// R.raw.bump);
	// } else if (tool instanceof Bomb) {
	// mediaPlayer = MediaPlayer.create(activity.getApplicationContext(),
	// R.raw.bump);
	// } else if (tool instanceof Heart) {
	// mediaPlayer = MediaPlayer.create(activity.getApplicationContext(),
	// R.raw.bump);
	// } else if (tool instanceof Hole) {
	// mediaPlayer = MediaPlayer.create(activity.getApplicationContext(),
	// R.raw.bump);
	// }
	// if (mediaPlayer != null) {
	// mediaPlayer.start();
	// }
	// }
	// }

	public SoundSource(int systemStatus, Activity mainActivity) {

	}

	public SoundSource(Activity mainActivity) {
		activity = mainActivity;
	}

	public static void playBackgroundMusic(Context context) {
		if (audioOn) {
			backgroubdMP = MediaPlayer.create(context,
					R.raw.basic_im_wearing_my_dancing_pants);
			backgroubdMP.setLooping(true);
			float volume = ((AudioManager) context
					.getSystemService(Context.AUDIO_SERVICE))
					.getStreamVolume(AudioManager.STREAM_MUSIC) * 0.03f;
			backgroubdMP.setVolume(volume, volume);
			backgroubdMP.start();
		}
	}

	public static void stopBackgoundMusic() {
		if (backgroubdMP.isPlaying()) {
			backgroubdMP.stop();
		}
	}

	public static void play(int systemStatusGameOver, Activity mainActivity) {
		if (audioOn) {

		}
	}

	public static void play(Creature creature, int type, Activity activity) {
		if (audioOn) {
			MediaPlayer mediaPlayer = null;
			if (creature != null) {
				if (creature instanceof Guard) {
					if (type == ANGRY) {
						if (creature.getName() == "G3") {
							mediaPlayer = MediaPlayer.create(
									activity.getApplicationContext(),
									R.raw.monster_hiss);
						} else if (creature.getName() == "G5") {
							mediaPlayer = MediaPlayer.create(
									activity.getApplicationContext(),
									R.raw.monster_hiss);
						} else if (creature.getName() == "G8") {
							mediaPlayer = MediaPlayer.create(
									activity.getApplicationContext(),
									R.raw.monster_hiss);
						} else if (creature.getName() == "G9") {
							mediaPlayer = MediaPlayer.create(
									activity.getApplicationContext(),
									R.raw.monster_hiss);
						}
					}
				} else if (creature instanceof Player) {
					switch (type) {
					case EXIT:
						mediaPlayer = MediaPlayer.create(
								activity.getApplicationContext(), R.raw.exit);
						break;
					case DAMAGE:
						mediaPlayer = MediaPlayer.create(
								activity.getApplicationContext(), R.raw.ouch);
						break;
					case BUMP:
						mediaPlayer = MediaPlayer.create(
								activity.getApplicationContext(), R.raw.bump);
						break;
					case HEAL:
						mediaPlayer = MediaPlayer.create(
								activity.getApplicationContext(),
								R.raw.cracking_up);
						break;
					default:
						break;
					}
				}
			}
			if (mediaPlayer != null) {
				mediaPlayer.start();
			}
		}
	}

	public static void off() {
		audioOn = false;
		stopBackgoundMusic();
	}

	public static void on() {
		audioOn = true;
	}

	@Override
	public synchronized void perform(int state) {
		if (audioOn) {
			MediaPlayer mediaPlayer = null;
			switch (state) {
			case DAMAGE:
				mediaPlayer = MediaPlayer.create(
						activity.getApplicationContext(), R.raw.ouch);
				break;
			case HEAL:
				mediaPlayer = MediaPlayer.create(
						activity.getApplicationContext(), R.raw.healing);
				break;
			default:
				break;
			}
			if (mediaPlayer != null) {
				mediaPlayer.start();
			}
		}
	}

}
