package com.andedit.arcubit.blocks.models;

import static com.andedit.arcubit.world.World.world;
import static com.andedit.arcubit.Assets.getBlockReg;

import com.andedit.arcubit.blocks.Block;
import com.andedit.arcubit.blocks.Blocks;
import com.andedit.arcubit.blocks.utils.CubeTex;
import com.andedit.arcubit.utils.ArrayDir;
import com.andedit.arcubit.utils.BlockPos;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GrassModel extends CubeModel {
	
	private final CubeTex grass = new CubeTex(getBlockReg(0, 0), getBlockReg(3, 0), getBlockReg(2, 0));
	private final CubeTex snow = new CubeTex(getBlockReg(0, 0), getBlockReg(4, 4), getBlockReg(2, 0));

	public GrassModel(Block block) {
		super(block, (ArrayDir<CubeTex>)null);
	}

	@Override
	protected CubeTex getTex(BlockPos pos) {
		if (world.getBlock(pos.x, pos.y+1, pos.z) == Blocks.SNOW_LAYER) {
			return snow;
		}
		return grass;
	}
	
	@Override
	public TextureRegion getDefaultTex(int data) {
		return grass.north;
	}
}
