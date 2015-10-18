package com.ahtsho.labyrinthandroid.view;

import infrastructure.Cell;
import tools.*;

import com.ahtsho.labyrinthandroid.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import creatures.Creature;
import creatures.Guard;
import creatures.Player;

public class Bitmapper {
	
	public static final int CELL_FLOOR = 0;
	
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
	private static Bitmap westWall = null;
	private static Bitmap eastWall = null;
	private static Bitmap southWall = null;
	private static Bitmap northWall = null;
	private static Bitmap cellFloor = null;
	public static Bitmap getBitmap(Cell cell, char direction, View view) {
		Bitmap wall = null;
		
		if(westWall==null){
			westWall = BitmapFactory.decodeResource(view.getResources(),R.drawable.hedge_v);
		}
		if(eastWall==null){
			eastWall = BitmapFactory.decodeResource(view.getResources(),R.drawable.hedge_v);
		}
		if(southWall==null){
			southWall = BitmapFactory.decodeResource(view.getResources(),R.drawable.hedge_h);
		}
		if(northWall==null){
			northWall = BitmapFactory.decodeResource(view.getResources(),R.drawable.hedge_h);
		}
		
		if(direction==Cell.WEST){
			wall = westWall;
		}else if(direction==Cell.EAST){
			wall = eastWall;
		}else if(direction==Cell.NORTH){
			wall = northWall;
		}else if(direction==Cell.SOUTH){
			wall = southWall;
		}
		
		return wall;
	}
	
	public static Bitmap getBitmap(Cell cell, int part, View view) {
		Bitmap image = null;
		
		if(cellFloor==null){
			cellFloor = BitmapFactory.decodeResource(view.getResources(),R.drawable.grass_9);
		}
		
		switch (part) {
		case 0:
			image = cellFloor;
			break;
		
		
		default:
			break;
		}
		
		
		return image;
	}
	
	
	

}
