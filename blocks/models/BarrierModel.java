package com.andedit.arcubit.blocks.models;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.blocks.Block;
import com.andedit.arcubit.graphics.quad.QuadBuilder;
import com.andedit.arcubit.utils.BlockPos;
import com.andedit.arcubit.utils.Facing;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;

public class BarrierModel extends BlockModel {
	
	public BarrierModel() {
	}

	public BarrierModel(Block block) {
		super(block);
	}

	@Override
	public void build(QuadBuilder builder, BlockPos pos) {
	}

	@Override
	public TextureRegion getDefaultTex(int data) {
		return Assets.MISSING;
	}
	
	public Array<BoundingBox> getBoundingBoxes(BlockPos pos) {
		return CubeModel.CUBE_BOX;
	}

	@Override
	public boolean isFaceSolid(BlockPos pos, Facing face) {
		return true;
	}
}
