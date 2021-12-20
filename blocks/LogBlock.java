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

public class LogBlock extends Block {
	
	public static final int 
	OAK = 0,
	SPRUCE = 1,
	BIRCH = 2;
	
	LogBlock() {
		this.material = Material.BLOCK;
		this.setMining(ToolType.AXE, 0, 2f);
		
		final TextureRegion top = getBlockReg(5, 1);
		final ArrayDir<CubeTex> texs = new ArrayDir<>(CubeTex.class, 3);
		texs.put(OAK, new CubeTex(top, getBlockReg(4, 1)));
		texs.put(SPRUCE, new CubeTex(top, getBlockReg(4, 7)));
		texs.put(BIRCH, new CubeTex(top, getBlockReg(5, 7)));
		this.model = new CubeModel(this, texs);
		
		textures = new ArrayDir<TextureRegion>(TextureRegion.class, 3);
		textures.put(OAK, Assets.getItemReg(0, 1));
		textures.put(SPRUCE, Assets.getItemReg(1, 1));
		textures.put(BIRCH, Assets.getItemReg(2, 1));
		
		newTypeComponent(2);
		this.blockType = BlockType.WOOD;
	}
	
	@Override
	public String getName(int type) {
		switch (type) {
		case OAK: return "Oak Log";
		case SPRUCE: return "Spruce Log";
		case BIRCH: return "Birch Log";
		default: return "Log";
		}
	}
}