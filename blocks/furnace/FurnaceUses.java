package com.andedit.arcubit.blocks.furnace;

public enum FurnaceUses {
	SMELTABLE, FUEL, BOTH, NONE;
	
	public boolean isSmeltable() {
		return this == SMELTABLE || this == BOTH;
	}
	
	public boolean isFuel() {
		return this == FUEL || this == BOTH;
	}
}
