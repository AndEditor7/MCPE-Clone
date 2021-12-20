package com.andedit.arcubit.blocks.furnace;

import com.andedit.arcubit.items.Item;

public interface FurnaceRecipe {
	
	default FurnaceUses getFurnUses() {
		return FurnaceUses.NONE;
	}
	
	default int getBurnTime() {
		return 0;
	}
	
	/** Must be cache. */
	default Item getResult() {
		return null;
	}
}
