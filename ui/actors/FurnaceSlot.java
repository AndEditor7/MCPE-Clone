package com.andedit.arcubit.ui.actors;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.items.Item;
import com.andedit.arcubit.ui.FurnaceUI;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class FurnaceSlot extends SlotBase {
	
	private static final NinePatch BACKGROUND = new NinePatch(new TextureRegion(Assets.TEXTURE, 0, 416, 8, 7), 1, 1, 1, 1);
	
	public static final int FUEL = 0, SMELT = 1, RESULT = 2;
	
	private final FurnaceUI gui;
	private final int role;
	
	public FurnaceSlot(FurnaceUI gui, int role) {
		this.gui = gui;
		this.role = role;
		
		setSize(24, 24);
		addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				final Item item = getItem();
				if (item != null && !item.isEmpty()) {
					if (gui.inventory.addItem(item)) {
						item.remove();
					}
				}
			}
		});
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		BACKGROUND.draw(batch, getX(), getY(), getWidth(), getHeight());
		draw(batch, getItem(), 2, 1);
	}
	
	public Item getItem() {
		switch (role) {
		case FUEL: return gui.content.fuel;
		case SMELT: return gui.content.smelt;
		case RESULT: return gui.content.result;
		default: return null;
		}
	}
}
