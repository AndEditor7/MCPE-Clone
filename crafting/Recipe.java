package com.andedit.arcubit.crafting;

import com.andedit.arcubit.items.Item;

public final class Recipe {
	public Item[] ingredients;
	public Item returns;
	public boolean is3x3, shareType;
	
	public Recipe setRecipe(Item... items) {
		ingredients = items;
		return this;
	}
	
	public Recipe setReturn(Item item) {
		returns = item;
		return this;
	}
	
	public Recipe set3x3() {
		is3x3 = true;
		return this;
	}
	
	public Recipe shareType() {
		shareType = true;
		return this;
	}
}
