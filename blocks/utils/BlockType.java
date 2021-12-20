package com.andedit.arcubit.blocks.utils;

import com.andedit.arcubit.Sounds;
import com.andedit.arcubit.handles.Audio;
import com.andedit.arcubit.items.tools.ToolType;

public enum BlockType {
	AIR(ToolType.NONE), 
	STONE(ToolType.PICKAXE), 
	DIRT(ToolType.SHOVEL, Sounds.DIRT), 
	GRASS(ToolType.SHOVEL, Sounds.GRASS), 
	PLANT(ToolType.SHEARS, Sounds.GRASS), 
	SAND(ToolType.SHOVEL, Sounds.SAND), 
	GRAVEL(ToolType.SHOVEL, Sounds.GRAVEL), 
	CLAY(ToolType.SHOVEL, Sounds.DIRT), 
	LIQUID(ToolType.NONE), 
	METAL(ToolType.PICKAXE), 
	LEAVES(ToolType.SHEARS, Sounds.GRASS), 
	WOOD(ToolType.AXE, Sounds.WOOD), 
	WOOL(ToolType.SHEARS, Sounds.WOOL),
	GLASS(ToolType.PICKAXE),
	SNOW(ToolType.SHOVEL, Sounds.WOOL), 
	ICE(ToolType.PICKAXE),
	CACTUS(ToolType.NONE, Sounds.WOOL), 
	FIRE(ToolType.NONE);
	
	public final Audio audio;
	public final ToolType toolType;
	
	private BlockType(ToolType toolType, Audio audio) {
		this.audio = audio;
		this.toolType = toolType;
	}
	
	private BlockType(ToolType toolType) {
		this.toolType = toolType;
		audio = Sounds.STONE;
	}
	
	public boolean isSoil() {
		return this == DIRT || this == GRASS;
	}

	public boolean isFlamable() {
		return this == PLANT || this == LEAVES || this == WOOD || this == WOOL;
	}
	
	public Audio getBreakSound() {
		if (this == FIRE) {
			return Sounds.FIZZ;
		}
		if (this == GLASS || this == ICE) {
			return Sounds.GLASS;
		}
		return audio;
	}
}
