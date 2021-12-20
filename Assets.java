package com.andedit.arcubit;

import com.andedit.arcubit.utils.Util;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public final class Assets {
	
	public static final String DEFAULT = "default";
	
	public static Texture TERRAIN;
	public static Texture CLOUDS;
	public static Texture TEXTURE;
	public static Texture BACKGROND;
	
	public static TextureRegion UP_ARROW;
	public static TextureRegion DOWN_ARROW;
	public static TextureRegion LEFT_ARROW;
	public static TextureRegion RIGHT_ARROW;
	public static TextureRegion CENTER_ARROW;
	public static TextureRegion HOT_START;
	public static TextureRegion HOT_END;
	public static TextureRegion HOT_SELECT;
	public static TextureRegion DOTS;
	public static TextureRegion CROSS;
	public static TextureRegion SLOT;
	public static TextureRegion STEVE;
	public static TextureRegion BLANK;
	public static TextureRegion GREEN, GRAY;
	public static TextureRegion MISSING;
	public static TextureRegion WATER;
	public static TextureRegion FIRE;
	public static TextureRegion KNOB;
	
	// Atlas
	public static TextureRegion SPRITES;
	public static TextureRegion TOUCHGUI, GUI_ITEMS;
	public static TextureRegion GUI, MOBS, ICONS;
	
	public static final TextureRegion[] BREAKS = new TextureRegion[10];
	
	public static NinePatch ITEM_FRAME;
	public static NinePatch FRAME;
	
	public static BitmapFont FONT;
	
	public static final Skin SKIN = new Skin();
	
	public static void loadAssets(AssetManager asset) {
		final FileHandleResolver resolver = asset.getFileHandleResolver();
		asset.setLoader(BitmapFont.class, new BitmapFontLoader(resolver));
		asset.setLoader(Texture.class, new TextureLoader(resolver));
		TextureParameter param = new TextureParameter();
		
		if (Util.isGL30()) {
			param.genMipMaps = true;
			param.minFilter = TextureFilter.MipMapNearestLinear;
		}
		
		asset.load("textures/terrain.png", Texture.class, param);
		asset.load("textures/clouds.png", Texture.class);
		asset.load("textures/font.fnt", BitmapFont.class);
		
		param = new TextureParameter();
		param.wrapU = TextureWrap.Repeat;
		param.wrapV = TextureWrap.Repeat;
		asset.load("textures/background.png", Texture.class, param);
	}
	
	public static void getAssets(AssetManager asset) {
		CLOUDS = asset.get("textures/clouds.png");
		CLOUDS.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		FONT = asset.get("textures/font.fnt");
		TEXTURE = FONT.getRegion().getTexture();
		TERRAIN = asset.get("textures/terrain.png");
		BACKGROND = asset.get("textures/background.png");
		
		FONT.setUseIntegerPositions(false);
		
		if (Util.isGL30()) {
			TERRAIN.bind();
			Gdx.gl.glTexParameteri(TERRAIN.glTarget, GL30.GL_TEXTURE_MAX_LEVEL, 4);
		}
		
		// Atlas
		SPRITES = new TextureRegion(TEXTURE, 0, 384, 128, 128);
		TOUCHGUI = new TextureRegion(TEXTURE, 0, 59, 256, 256);
		GUI = new TextureRegion(TEXTURE, 248, 0, 256, 162);
		MOBS = new TextureRegion(TEXTURE, 162, 414, 256, 98);
		ICONS = new TextureRegion(TEXTURE, 163, 0, 84, 32);
		GUI_ITEMS = new TextureRegion(TEXTURE, 512, 0, 512, 512);
		
		UP_ARROW = new TextureRegion(GUI, 2, 109, 22, 22);
		DOWN_ARROW = new TextureRegion(GUI, 54, 109, 22, 22);
		LEFT_ARROW = new TextureRegion(GUI, 28, 109, 22, 22);
		RIGHT_ARROW = new TextureRegion(GUI, 80, 109, 22, 22);
		CENTER_ARROW = new TextureRegion(GUI, 108, 111, 18, 18);
		HOT_START = new TextureRegion(GUI, 0, 0, 21, 22);
		HOT_END = new TextureRegion(GUI, 161, 0, 21, 22);
		HOT_SELECT = new TextureRegion(GUI, 0, 22, 24, 24);
		DOTS = new TextureRegion(GUI_ITEMS, 498, 508, 14, 4);
		CROSS = new TextureRegion(ICONS, 2, 2, 11, 11);
		SLOT = new TextureRegion(GUI, 200, 46, 16, 16);
		STEVE = new TextureRegion(MOBS, 130, 66, 64, 32);
		GREEN = new TextureRegion(GUI_ITEMS, 0, 511, 1, 1);
		GRAY = new TextureRegion(GUI_ITEMS, 1, 511, 1, 1);
		MISSING = getBlockReg(9, 9);
		BLANK = new TextureRegion(TEXTURE, 469, 420, 1, 1);
		FIRE = new TextureRegion(TEXTURE, 240, 235, 16, 16);
		KNOB = new TextureRegion(TEXTURE, 225, 184, 11, 17);
		
		ITEM_FRAME = new NinePatch(new TextureRegion(TEXTURE, 129, 384, 32, 128), 7, 7, 8, 7);
		FRAME = new NinePatch(new TextureRegion(TEXTURE, 28, 426, 3, 3), 1, 1, 1, 1);
		
		WATER = getBlockReg(13, 12);
		WATER.setRegionHeight(1);
		WATER.setRegionWidth(1);
		
		for (int i = 0; i < 10; i++) {
			BREAKS[i] = getBlockReg(i, 15);
		}
		
		getSkins();
	}
	
	private static void getSkins() {
		TextButtonStyle tbs = new TextButtonStyle();
		tbs.up   = new NinePatchDrawable(new NinePatch(new TextureRegion(TOUCHGUI, 0,  0, 66, 26), 3, 3, 3, 3));
		tbs.down = new NinePatchDrawable(new NinePatch(new TextureRegion(TOUCHGUI, 66, 0, 66, 26), 3, 3, 3, 3));
		tbs.font = FONT;
		SKIN.add(DEFAULT, tbs);
		
		ButtonStyle bt = new ButtonStyle();
		bt.up   = new TextureRegionDrawable(new TextureRegion(SPRITES, 60, 0, 18, 18));
		bt.down = new TextureRegionDrawable(new TextureRegion(SPRITES, 78, 0, 18, 18));
		SKIN.add("back", bt);
		
		bt = new ButtonStyle();
		bt.up   = new NinePatchDrawable(new NinePatch(new TextureRegion(SPRITES, 8, 32, 8, 8), 1, 1, 2, 1));
		bt.down = new NinePatchDrawable(new NinePatch(new TextureRegion(SPRITES, 0, 32, 8, 8), 1, 1, 1, 2));
		bt.disabled = bt.down;
		SKIN.add("blank", bt);
		
		bt = new ButtonStyle();
		bt.checked   = new TextureRegionDrawable(new TextureRegion(TEXTURE, 198, 265, 38, 19));
		bt.up = new TextureRegionDrawable(new TextureRegion(TEXTURE, 160, 265, 38, 19));
		SKIN.add("switch", bt);
		
		bt = new ButtonStyle();
		bt.up   = new NinePatchDrawable(new NinePatch(new TextureRegion(SPRITES, 20, 32, 8, 8), 1, 1, 1, 1));
		bt.down = new NinePatchDrawable(new NinePatch(new TextureRegion(SPRITES, 28, 32, 8, 8), 1, 1, 1, 1));
		bt.disabled = bt.down;
		SKIN.add("bar", bt); 
		
		LabelStyle lt = new LabelStyle(FONT, Color.WHITE);
		SKIN.add(DEFAULT, lt);
		
		TextFieldStyle fieldStyle = new TextFieldStyle(
		FONT, Color.WHITE, new TextureRegionDrawable(BLANK), 
		new TextureRegionDrawable(new TextureRegion(TEXTURE, 29, 431, 1, 1)), 
		new NinePatchDrawable(FRAME));
		
		SKIN.add(DEFAULT, fieldStyle);
	}

	public static TextureRegion getBlockReg(int x, int y) {
		return new TextureRegion(TERRAIN, x << 4, y << 4, 16, 16);
	}
	
	public static TextureRegion getItemReg(int x, int y) {
		return new TextureRegion(GUI_ITEMS, x << 5, y << 5, 32, 32);
	}
}
