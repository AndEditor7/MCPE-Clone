package com.andedit.arcubit.ui.actors;

import static com.andedit.arcubit.Assets.FONT;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.crafting.Recipe;
import com.andedit.arcubit.items.Item;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.ObjectMap.Entry;

public class Column extends Actor {
	private static final ButtonStyle STYLE = Assets.SKIN.get("bar", ButtonStyle.class);
	
	private final ArrayMap<Item, Array<Slot>> itemMap = new ArrayMap<>(8);
	private final Recipe recipe;
	private final BitmapFontCache title  = new BitmapFontCache(FONT);
	private final BitmapFontCache amount = new BitmapFontCache(FONT);
	private final TextureRegion texture;
	private final ClickListener listener;
	private int currently;
	
	public boolean isCraftable;
	
	public Column(final Craft craft, Recipe recipe) {
		this.recipe = recipe;
		
		final Item item = recipe.returns;
		title.addText(item.getName(), 0, 0);
		texture = item.getTexture();
		
		final Column column = this;
		addListener(listener = new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				craft.setColumn(column);
			}
		});
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		(listener.isPressed()?STYLE.disabled:STYLE.up).draw(batch, getX(), getY(), getWidth(), getHeight());
		
		batch.draw(texture, getX() + getWidth()-18f, getY()+3f, 16, 16);
		title.setPosition(getX()+5f, getY()+15f);
		title.draw(batch);
		
		amount.setPosition(getX() + getWidth() - 23f, getY() + 10f);
		amount.draw(batch);
	}
	
	public Item[] getIngredients() {
		return recipe.ingredients;
	}
	
	public Item getReturn() {
		return recipe.returns;
	}
	
	public boolean is3x3() {
		return recipe.is3x3;
	}
	
	public void checkItem(Slot slot) {
		if (slot.isEmpty()) return;
		
		if (slot.item.equals(getReturn())) {
			currently += slot.item.size;
		}
		
		for (Item item : getIngredients()) {
			if (match(item, slot.item)) {
				Array<Slot> slots = itemMap.get(item);
				if (slots == null) {
					slots = new Array<>(false, 16);
					itemMap.put(item, slots);
				}
				slots.add(slot);
			}
		}
	}
	
	private boolean match(Item item1, Item item2) {
		return recipe.shareType ? item1.match(item2) : item1.equals(item2);
	}
	
	public void begin() {
		currently = 0;
		for (Entry<Item, Array<Slot>> entry : itemMap) {
			if (entry.value != null) {
				entry.value.clear();
			}
		}
	} 
	
	public void end() {
		isCraftable = true;
		for (Item item : getIngredients()) {
			Array<Slot> slots = itemMap.get(item);
			
			if (slots == null || slots.isEmpty()) {
				isCraftable = false;
				break;
			}
			
			int amount = 0;
			for (Slot slot : slots) {
				amount += slot.size();
			}
			
			if (amount < item.size) {
				isCraftable = false;
				break;
			}
		}
		
		title.setColors(isCraftable ? Color.WHITE : Color.DARK_GRAY);
		amount.setColor(isCraftable ? Color.WHITE : Color.DARK_GRAY);
		amount.setText(Integer.toString(currently), 0, 0);
	}
	
	private static final ArrayMap<Item, Integer> sizeMap = new ArrayMap<>(9);
	public Item craft() {
		sizeMap.clear();
		
		// Check total amounts of ingredients.
		for (Item item : getIngredients()) {
			sizeMap.put(item, 0);
			
			final Array<Slot> slots = itemMap.get(item);
			
			if (slots == null || slots.isEmpty()) {
				return null;
			}
			
			for (int i = 0; i < slots.size; i++) {
				final Slot slot = slots.get(i);
				if (slot.isEmpty() || !match(slot.item, item)) {
					slots.removeIndex(i--);
					continue;
				}
				sizeMap.put(item, sizeMap.get(item)+slot.size());
			}
		}
		
		// Check if it fully craftable.
		for (Item item : getIngredients()) {
			if (sizeMap.get(item) < item.size) {
				return null;
			}
			sizeMap.put(item, (int)item.size);
		}
		
		// Removing items
		loop : for (Item item : getIngredients()) {
			final Array<Slot> slots = itemMap.get(item);
			
			int left = sizeMap.get(item);
			for (Slot slot : slots) {
				int num = slot.size() - left;
				if (num >= 0) {
					slot.item.size -= left;
					continue loop;
				} else {
					left -= slot.size();
					slot.removeItem();
				}
			}
		}
			
		return getReturn().clone();
	}
	
	public int getSize(Item item) {
		final Array<Slot> slots = itemMap.get(item);
		
		if (slots == null || slots.isEmpty()) {
			return 0;
		}
		
		int num = 0;
		for (Slot slot : slots) {
			num += slot.size();
		}
		
		return num;
	}
}
