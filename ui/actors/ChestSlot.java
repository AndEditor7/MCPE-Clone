package com.andedit.arcubit.ui.actors;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.items.Item;
import com.badlogic.gdx.graphics.g2d.Batch;

public class ChestSlot extends SlotBase {
	private Item[] items;
	private int index;
	
	public ChestSlot() {
		setSize(24, 24);
	}
	
	public void setItems(Item[] items, int index) {
		this.items = items;
		this.index = index;
	}
	
	public void setItem(Item item) {
		items[index] = item;
	}
	
	public Item getItem() {
		return items[index];
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(Assets.SLOT, getX(), getY(), getWidth(), getHeight());
		
		draw(batch, getItem(), 2, 1);
	}
}
