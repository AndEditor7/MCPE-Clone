package com.andedit.arcubit.blocks.models;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.blocks.Block;
import com.andedit.arcubit.blocks.data.FurnComp;
import com.andedit.arcubit.blocks.utils.CubeTex;
import com.andedit.arcubit.utils.BlockPos;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class FurnaceModel extends ChestModel {
	
	private final TextureRegion frontLit = Assets.getBlockReg(13, 3);
	private final TextureRegion frontTex = Assets.getBlockReg(12, 2);
	
	private final FurnComp component;

	public FurnaceModel(Block block) {
		super(block, new CubeTex(Assets.getBlockReg(14, 3), Assets.getBlockReg(13, 2)));
		component = block.getData().getComponent(FurnComp.KEY);
	}

	@Override
	protected TextureRegion getFrontTex(BlockPos pos) {
		if (pos == null) return frontTex;
		return component.isActive(pos) ? frontLit : frontTex;
	}
}
