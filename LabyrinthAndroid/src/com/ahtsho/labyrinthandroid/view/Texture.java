package com.ahtsho.labyrinthandroid.view;

import android.graphics.Bitmap;

public class Texture {

	public Texture(int hRes, int vRes, int floorRes) {
		horizontal = hRes;
		vertical = vRes;
		floor = floorRes;
	}
	private int horizontal;
	private int vertical;
	private int floor;
	public int getFloor() {
		return floor;
	}
	public void setFloor(int floor) {
		this.floor=floor;
	}
	
	public int getHorizontal() {
		return horizontal;
	}
	public void setHorizontal(int horizontal) {
		this.horizontal = horizontal;
	}
	public int getVertical() {
		return vertical;
	}
	public void setVertical(int vertical) {
		this.vertical = vertical;
	}
	private Bitmap hImg;
	private Bitmap vImg;
	private Bitmap fImg;
	
	public void setImgs(Bitmap h, Bitmap v, Bitmap f){
		hImg = h;
		vImg = v;
		fImg = f;
	}
	public Bitmap getHBitmap() {
		return hImg;		
	}
	public Bitmap getVBitmap() {
		return vImg;		
	}
	public Bitmap getFloorBitmap() {
		return fImg;		
	}
}
