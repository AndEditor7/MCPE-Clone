package com.andedit.arcubit.items;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class DyesItem extends Item {
	
	private static final String[] names = new String[16];
	private static final TextureRegion[] texs = new TextureRegion[16];
	
	private static int a;
	public static final int
	BONEMEAL = a++,
	ORANGE = a++,
	MAGENTA = a++,
	LIGHT_BLUE = a++,
	YELLOW = a++,
	LIME = a++,
	PINK = a++,
	GRAY = a++,
	LIGHT_GRAY = a++,
	CYAN = a++,
	PURPLE = a++,
	BLUE = a++,
	BROWN = a++,
	GREEN = a++,
	RED = a++,
	BLACK = a;
	
	static {
		names[BONEMEAL] = "";
	}

	@Override
	public String getName() {
		return names[type];
	}

	@Override
	public TextureRegion getTexture() {
		return texs[type];
	}

	@Override
	public Item clone() {
		return new DyesItem().set(this);
	}
}
