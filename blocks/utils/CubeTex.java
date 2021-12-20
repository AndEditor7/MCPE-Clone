package com.andedit.arcubit.blocks.utils;

import com.andedit.arcubit.Assets;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class CubeTex {
	
	public TextureRegion top, bottom, north, east, south, west;
	
	public CubeTex() {
	}
	
	public CubeTex(TextureRegion all) {
		set(all);
	}
	
	public CubeTex(TextureRegion topAndBottom, TextureRegion side) {
		top = bottom = topAndBottom;
		north = east = south = west = side;
	}
	
	public CubeTex(TextureRegion top, TextureRegion side, TextureRegion bottom) {
		this.top = top;
		this.bottom = bottom;
		north = east = south = west = side;
	}
	
	public CubeTex(TextureRegion top, TextureRegion side1, TextureRegion side2, TextureRegion bottom) {
		this.top = top;
		this.bottom = bottom;
		north = south = side1;
		east = west = side2;
	}
	
	public CubeTex(TextureRegion top, TextureRegion bottom, TextureRegion north, TextureRegion east, TextureRegion south, TextureRegion west) {
		this.top = top;
		this.bottom = bottom;
		this.north = north;
		this.east = east;
		this.south = south;
		this.west = west;
	}
	
	public CubeTex set(TextureRegion all) {
		top = bottom = north = east = south = west = all;
		return this;
	}
	
	public TextureRegion texTop(int x, int y, int w, int h) {
		return top = new TextureRegion(Assets.TEXTURE, x, y, w, h);
	}
	
	public TextureRegion texBottom(int x, int y, int w, int h) {
		return bottom = new TextureRegion(Assets.TEXTURE, x, y, w, h);
	}
	
	public TextureRegion texNorth(int x, int y, int w, int h) {
		return north = new TextureRegion(Assets.TEXTURE, x, y, w, h);
	}
	
	public TextureRegion texEast(int x, int y, int w, int h) {
		return east = new TextureRegion(Assets.TEXTURE, x, y, w, h);
	}
	
	public TextureRegion texSouth(int x, int y, int w, int h) {
		return south = new TextureRegion(Assets.TEXTURE, x, y, w, h);
	}
	
	public TextureRegion texWest(int x, int y, int w, int h) {
		return west = new TextureRegion(Assets.TEXTURE, x, y, w, h);
	}
	
	public void set(CubeTex texs) {
		top.setRegion(texs.top);
		bottom.setRegion(texs.bottom);
		north.setRegion(texs.north);
		east.setRegion(texs.east);
		south.setRegion(texs.south);
		west.setRegion(texs.west);
	}
}
