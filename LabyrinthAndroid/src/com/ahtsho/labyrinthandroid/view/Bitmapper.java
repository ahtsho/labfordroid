package com.ahtsho.labyrinthandroid.view;

import tools.*;

import com.ahtsho.labyrinthandroid.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import creatures.Creature;
import creatures.Guard;
import creatures.Player;

public class Bitmapper {
	public static Bitmap getBitmap(Player p, View v){
		Bitmap playerBitmap = null;
		if(p != null){
			switch (p.getId()) {
			case 1:
				playerBitmap = BitmapFactory.decodeResource(v.getResources(), R.drawable.face1);
				break;
			
			case 2:
				playerBitmap = BitmapFactory.decodeResource(v.getResources(), R.drawable.face2);
				break;

			case 3:
				playerBitmap = BitmapFactory.decodeResource(v.getResources(), R.drawable.face3);
				break;
	
			case 4:
				playerBitmap = BitmapFactory.decodeResource(v.getResources(), R.drawable.face4);
				break;

			default:
				break;
			}
			
		}
		return playerBitmap;
	}
	public static Bitmap getBitmap(Creature c, View v) {
		Bitmap creatureBitmap = null;
		if(c instanceof Guard){
			if(c.getName()=="G3"){
				creatureBitmap=BitmapFactory.decodeResource(v.getResources(),R.drawable.guard1);
			} else if(c.getName()=="G5"){
				creatureBitmap=BitmapFactory.decodeResource(v.getResources(),R.drawable.guard4);
			} else if(c.getName()=="G8"){
				creatureBitmap=BitmapFactory.decodeResource(v.getResources(),R.drawable.guard2);
			} else if(c.getName()=="G9"){
				creatureBitmap=BitmapFactory.decodeResource(v.getResources(),R.drawable.guard3);
			} 
		}
		return creatureBitmap;
	}

	public static Bitmap getBitmap(Tool t, View v) {
		Bitmap creatureBitmap = null;
		if(t instanceof Plaster){
			creatureBitmap=BitmapFactory.decodeResource(v.getResources(),R.drawable.plaster1);
		}else if(t instanceof Medicine){
			creatureBitmap=BitmapFactory.decodeResource(v.getResources(),R.drawable.medicine);
		}else if(t instanceof Box){
			creatureBitmap=BitmapFactory.decodeResource(v.getResources(),R.drawable.closed_box1);
		}else if(t instanceof Bomb){
			creatureBitmap=BitmapFactory.decodeResource(v.getResources(),R.drawable.bomb1);
		}else if(t instanceof Heart){
			creatureBitmap=BitmapFactory.decodeResource(v.getResources(),R.drawable.heart);
		}else if(t instanceof Hole){
			creatureBitmap=BitmapFactory.decodeResource(v.getResources(),R.drawable.hole);
		}
		return creatureBitmap;
	}

}
