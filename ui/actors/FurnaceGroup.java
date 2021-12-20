package com.andedit.arcubit.ui.actors;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.blocks.furnace.FurnaceContent;
import com.andedit.arcubit.ui.FurnaceUI;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;

public class FurnaceGroup extends Group {
	
	private static final TextureRegion FIRE_OFF = new TextureRegion(Assets.TEXTURE, 41, 405, 13, 13);
	private static final TextureRegion FIRE_ON  = new TextureRegion(Assets.TEXTURE, 57, 405, 13, 13);
	
	private static final TextureRegion ARROW_OFF = new TextureRegion(Assets.TEXTURE, 73, 404, 22, 16);
	private static final TextureRegion ARROW_ON  = new TextureRegion(Assets.TEXTURE, 73, 420, 22, 16);
	
	private final FurnaceSlot smelt, fuel, result;
	private final TextureRegion temp = new TextureRegion();
	private final FurnaceUI gui;
	
	public FurnaceGroup(final FurnaceUI gui) {
		setSize(85, 68);
		setTransform(false);
		this.gui = gui;
		
		smelt = new FurnaceSlot(gui, FurnaceSlot.SMELT);
		smelt.setY(43);
		addActor(smelt);
		
		fuel  = new FurnaceSlot(gui, FurnaceSlot.FUEL);
		addActor(fuel);
		
		result  = new FurnaceSlot(gui, FurnaceSlot.RESULT);
		result.setPosition(60, 21);
		addActor(result);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		final FurnaceContent content = gui.content;
		batch.setColor(Color.WHITE);
		
		// Fire
		batch.draw(FIRE_OFF, getX()+5, getY()+27);
		
		if (content.isSmelting()) {
			temp.setRegion(FIRE_ON);
			final int v = (int) (13f * (content.burnTimer / (float)content.maxBurnTimer));
			temp.setRegionY(temp.getRegionY()+(12-v));
			batch.draw(temp, getX()+5, getY()+27);
		}
		
		// Arrow
		batch.draw(ARROW_OFF, getX()+30, getY()+25);
		
		temp.setRegion(ARROW_ON);
		temp.setRegionWidth((int)(22f * (content.process / (float)FurnaceContent.MAX_PROCESS)));
		batch.draw(temp, getX()+30, getY()+25);
		
		super.draw(batch, parentAlpha);
	}
}
