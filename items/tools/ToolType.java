package com.andedit.arcubit.items.tools;

public enum ToolType {
	SWORD("Sword", 3), 
	PICKAXE("Pickaxe", 1), 
	SHOVEL("Shovel", 1), 
	AXE("Axe", 2), 
	HOE("Hoe", 1),
	SHEARS("Shears", 0),
	NONE("Unknown", 0);
	
	public final String name;
	public final int damage;
	
	private ToolType (String name, int damage) {
		this.name = name;
		this.damage = damage;
	}
	
	public int getAmountTool() {
		switch (this) {
		case SWORD: case HOE: return 2;
		case AXE: case PICKAXE: return 3;
		default: return 1;
		}
	}
}
