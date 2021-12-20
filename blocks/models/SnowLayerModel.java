package com.andedit.arcubit.blocks.models;

import static com.andedit.arcubit.world.World.world;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.blocks.Block;
import com.andedit.arcubit.blocks.Blocks;
import com.andedit.arcubit.graphics.EntityBatch;
import com.andedit.arcubit.graphics.quad.QuadBuilder;
import com.andedit.arcubit.graphics.quad.QuadNode;
import com.andedit.arcubit.utils.BlockPos;
import com.andedit.arcubit.utils.Facing;
import com.andedit.arcubit.utils.ModelUtil;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;

public class SnowLayerModel extends BlockModel {
	
	private final TextureRegion texture = Assets.getBlockReg(2, 4);
	private final Box box;

	public SnowLayerModel(Block block) {
		super(block);
		
		boxes.add(new BoundingBox(MIN.setZero(), MAX.set(1, 2f/16f, 1)));
		
		final float a = 2f/16f;
		box = new Box(new BoundingBox(MIN.setZero(), MAX.set(1, a, 1)));
	}

	@Override
	public void build(QuadBuilder build, BlockPos pos) {
		final int x = pos.x, y = pos.y, z = pos.z;
		final BlockPos offset = build.offset;
		
		build.region = texture;
		build.ambLight = true;
		final BoundingBox box = this.box.box;
		
		build.simpleLight = true;
		Box.buildUp(build, box).flush(pos);
		build.simpleLight = false;
		
		if (canAddFace(pos, offset.set(x, y-1, z), Facing.DOWN)) {
			Box.buildDown(build, box).flush(pos);
		}
		if (canAddFace(pos, offset.set(x, y, z-1), Facing.NORTH)) {
			Box.buildNorth(build, box).flush(pos);
		}
		if (canAddFace(pos, offset.set(x+1, y, z), Facing.EAST)) {
			Box.buildEast(build, box).flush(pos);
		}
		if (canAddFace(pos, offset.set(x, y, z+1), Facing.SOUTH)) {
			Box.buildSouth(build, box).flush(pos);
		}
		if (canAddFace(pos, offset.set(x-1, y, z), Facing.WEST)) {
			Box.buildWest(build, box).flush(pos);
		}
	}
	
	@Override
	public boolean canAddFace(BlockPos primaray, BlockPos secondary, Facing face) {
		if (!super.canAddFace(primaray, secondary, face)) {
			return false;
		}
		
		if (face == Facing.DOWN) {
			return true;
		}
		
		if (world.getBlock(secondary) == Blocks.SNOW_LAYER) {
			return false;
		}
		
		return true;
	}
	
	@Override
	public void draw(EntityBatch batch, int type) {
		batch.setTexture(Assets.TERRAIN);
		ModelUtil.build(batch, box.mesh, texture);
	}
	
	@Override
	public boolean isFaceSolid(BlockPos pos, Facing face) {
		return face == Facing.DOWN;
	}

	@Override
	public Array<BoundingBox> getBoundingBoxes(BlockPos pos) {
		return boxes;
	}

	@Override
	public TextureRegion getDefaultTex(int data) {
		return texture;
	}

}
