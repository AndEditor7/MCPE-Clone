package com.andedit.arcubit.ui.actors;

import static com.andedit.arcubit.TheGame.game;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.items.Item;
import com.andedit.arcubit.ui.Inventory;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class Slot extends SlotBase {
	public Item item;
	public final short index;
	
	public Slot(final Hotbar hotbar, int i) {
		setSize(24, 24);
		index = (short) i;
		
		if (hotbar != null) 
		addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				if (game.manager.IsUI(Inventory.class)) hotbar.findItem(item, index);
			}
		});
	}
	
	public Slot setItem(Item item) {
		this.item = item;
		return this;
	}
	
	public void update() {
		if (item != null && item.isEmpty()) {
			item = null;
		}
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(Assets.SLOT, getX(), getY(), getWidth(), getHeight());
		
		draw(batch, item, 2, 1);
	}

	public boolean isEmpty() {
		return item == null || item.isEmpty();
	}
	
	public int size() {
		return item == null ? 0 : item.size;
	}
	
	public void removeItem() {
		if (item != null) {
			item.remove();
			item = null;
		}
	}
}
