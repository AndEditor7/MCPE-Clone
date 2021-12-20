package com.andedit.arcubit.items.tools;

import com.andedit.arcubit.blocks.Blocks;
import com.andedit.arcubit.items.BasicItem;
import com.andedit.arcubit.items.BlockItem;
import com.andedit.arcubit.items.Item;

public enum ToolMaterial {
	WOOD("Wooden", 59, 1, 2.0f),
	STONE("Stone", 131, 2, 4.0f),
	IRON("Iron", 250, 3, 6.0f),
	GOLD("Golden", 32, 1, 12.0f),
	DIAMOND("Diamond", 1561, 4, 8.0f);
	
	public final int durability, level;
	public final String name;
	public final float speed;
	
	private ToolMaterial(String name, int durability, int level, float speed) {
		this.name = name;
		this.durability = durability;
		this.level = level;
		this.speed = speed;
	}
	
	public Item getItem() {
		switch (this) {
		case WOOD:    return new BlockItem(Blocks.PLANK);
		case STONE:   return new BlockItem(Blocks.COBBLESTONE);
		case IRON:    return new BasicItem(BasicItem.IRON);
		case GOLD:    return new BasicItem(BasicItem.GOLD);
		case DIAMOND: return new BasicItem(BasicItem.DIAMOND);
		default: return null;
		}
	}
}
