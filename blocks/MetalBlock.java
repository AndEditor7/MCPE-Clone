package com.andedit.arcubit.blocks;

import static com.andedit.arcubit.Assets.getBlockReg;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.blocks.materials.Material;
import com.andedit.arcubit.blocks.models.CubeModel;
import com.andedit.arcubit.blocks.utils.CubeTex;
import com.andedit.arcubit.blocks.utils.BlockType;
import com.andedit.arcubit.items.tools.ToolType;
import com.andedit.arcubit.utils.ArrayDir;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MetalBlock extends Block {
	
	public static final int
	IRON = 0,
	GOLD = 1,
	DIAMOND = 2;

	MetalBlock() {
		final ArrayDir<CubeTex> blockTexs = new ArrayDir<>(CubeTex.class, 3);
		blockTexs.put(IRON, new CubeTex(getBlockReg(6, 1)));
		blockTexs.put(GOLD, new CubeTex(getBlockReg(7, 1)));
		blockTexs.put(DIAMOND, new CubeTex(getBlockReg(8, 1)));
		this.model = new CubeModel(this, blockTexs);
		
		this.textures = new ArrayDir<>(TextureRegion.class, 3);
		textures.put(IRON, Assets.getItemReg(13, 2));
		textures.put(GOLD, Assets.getItemReg(12, 2));
		textures.put(DIAMOND, Assets.getItemReg(14, 2));
		
		newTypeComponent(2);
		this.material = Material.BLOCK;
		this.blockType = BlockType.METAL;
		this.setMining(ToolType.PICKAXE, 2, 3);
	}
	
	@Override
	public String getName(int data) {
		switch (data) {
		case IRON: return "Iron Block";
		case GOLD: return "Gold Block";
		case DIAMOND: return "Diamond Block";
		default: return "Unknown Block";
		}
	}
}
