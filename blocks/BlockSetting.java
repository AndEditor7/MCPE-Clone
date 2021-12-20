package com.andedit.arcubit.blocks;

import com.andedit.arcubit.blocks.materials.Material;
import com.andedit.arcubit.blocks.models.BlockModel;
import com.andedit.arcubit.blocks.utils.BlockType;
import com.andedit.arcubit.items.tools.ToolType;
import com.andedit.arcubit.utils.ArrayDir;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

final class BlockSetting {
	
	int lightLevel;
	Material material;
	BlockType blockType;
	ToolType toolType;
	int miningLevel;
	float hardness;
	BlockModel model;
	ArrayDir<TextureRegion> textures;
	ArrayDir<String> names;
	int typeSize;
		
	{reset();}
	void reset() {
		lightLevel = 0;
		material = Material.BLOCK;
		blockType = BlockType.STONE;
		toolType = ToolType.NONE;
		miningLevel = 0;
		hardness = 1;
		model = null;
		textures = null;
		names = null;
		typeSize = 0;
	}
	
	void setBlockType(BlockType blockType) {
		this.blockType = blockType;
		this.toolType = blockType.toolType;
	}
	
	void setTexture(TextureRegion texture) {
		textures = new ArrayDir<>(TextureRegion.class, 1);
		textures.put(0, texture);
	}
	
	void setName(String name) {
		names = new ArrayDir<>(String.class, 1);
		names.put(0, name);
	}
	
	void setMining(int miningLevel, float hardness) {
		this.miningLevel = miningLevel;
		this.hardness = hardness;
	}

	void setMining(ToolType toolType, int miningLevel, float hardness) {
		this.toolType = toolType;
		this.miningLevel = miningLevel;
		this.hardness = hardness;
	}
}
