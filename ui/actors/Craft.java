package com.andedit.arcubit.ui.actors;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.Sounds;
import com.andedit.arcubit.items.Item;
import com.andedit.arcubit.ui.Crafting;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FlushablePool;

public class Craft extends Button {
	
	private static final FlushablePool<Ingredient> POOL = new FlushablePool<Ingredient>() {
		protected Ingredient newObject() {
			return new Ingredient();
		}
	};
	
	private Column column;
	private final Array<Ingredient> array = new Array<>(9);
	
	public Craft(Crafting crafting) {
		super(new ButtonStyle(
			new NinePatchDrawable(new NinePatch(new TextureRegion(Assets.SPRITES, 112, 0, 8, 67), 2, 2, 2, 2)),
			new NinePatchDrawable(new NinePatch(new TextureRegion(Assets.SPRITES, 120, 0, 8, 67), 2, 2, 2, 2)), null
		));
		
		addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				final Column column = getColumn();
				if (column == null) return;
				
				if (column.isCraftable) {
					
					Item item = column.craft();
					if (item == null) return;
					Sounds.click();
					
					crafting.inventory.addItem(item); // TODO: Add the fail safe.
					crafting.refresh();
					for (Ingredient ingredient : array) {
						ingredient.text.clear();
						final int size = column.getSize(ingredient.item);
						ingredient.text.append(size).append('/').append(ingredient.item.size);
						ingredient.isWhite = size >= ingredient.item.size;
					}
				}
			}
		});
	}

	public void setColumn(Column column) {
		clearStuff();
		this.column = column;
		
		int i = 0;
		for (Item item : column.getIngredients()) {
			Ingredient ingredient = POOL.obtain();
			ingredient.item = item;
			ingredient.text.clear();
			final int size = column.getSize(item);
			ingredient.text.append(size).append('/').append(item.size);
			ingredient.isWhite = size >= item.size;
			add(ingredient).size(16, 24).pad(3);
			if (i++ % 3 == 3) {
				row();
			}
			array.add(ingredient);
		}
		
		Sounds.click();
	}
	
	public Column getColumn() {
		return column;
	}
	
	public void clearStuff() {
		clearChildren();
		column = null;
		POOL.flush();
		array.clear();
	}
}
