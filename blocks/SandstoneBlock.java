package com.andedit.arcubit.blocks;

import static com.andedit.arcubit.Assets.getBlockReg;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.blocks.materials.Material;
import com.andedit.arcubit.blocks.models.CubeModel;
import com.andedit.arcubit.blocks.utils.CubeTex;
import com.andedit.arcubit.items.tools.ToolType;
import com.andedit.arcubit.utils.ArrayDir;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SandstoneBlock extends Block {
	
	public static final int
	NORMAL = 0,
	CREEPER = 1,
	SMOOTH = 2;

	SandstoneBlock() {
		this.material = Material.BLOCK;
		this.setMining(ToolType.PICKAXE, 1, 0.8f);
		
		final TextureRegion smooth = getBlockReg(0, 11);
		final ArrayDir<CubeTex> texs = new ArrayDir<>(CubeTex.class, 3);
		texs.put(NORMAL, new CubeTex(smooth, getBlockReg(0, 12), getBlockReg(0, 13)));
		texs.put(CREEPER, new CubeTex(smooth, getBlockReg(5, 14)));
		texs.put(SMOOTH, new CubeTex(smooth, getBlockReg(6, 14)));
		this.model = new CubeModel(this, texs);
		
		textures = new ArrayDir<TextureRegion>(TextureRegion.class, 3);
		textures.put(NORMAL, Assets.getItemReg(11, 0));
		textures.put(CREEPER, Assets.getItemReg(12, 0));
		textures.put(SMOOTH, Assets.getItemReg(13, 0));
		
		newTypeComponent(2);
	}

	@Override
	public String getName(int type) {
		return "Sandstone";
	}
}
