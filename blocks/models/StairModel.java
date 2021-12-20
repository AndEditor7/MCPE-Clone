package com.andedit.arcubit.blocks.models;

import static com.andedit.arcubit.blocks.StairBlock.*;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.blocks.Block;
import com.andedit.arcubit.blocks.data.StairComp.Shape;
import com.andedit.arcubit.graphics.EntityBatch;
import com.andedit.arcubit.graphics.quad.QuadBuilder;
import com.andedit.arcubit.utils.ArrayDir;
import com.andedit.arcubit.utils.BlockPos;
import com.andedit.arcubit.utils.Facing;
import com.andedit.arcubit.utils.ModelUtil;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;

public class StairModel extends BlockModel {
	
	private final ArrayDir<TextureRegion> texs = new ArrayDir<>(TextureRegion.class, 7);
	
	private final Box slab, piece1, piece2;

	public StairModel(Block block) {
		super(block);
		
		texs.put(COBBLESONE, Assets.getBlockReg(0, 1));
		texs.put(WOOD, Assets.getBlockReg(4, 0));
		texs.put(BRICK, Assets.getBlockReg(7, 0));
		texs.put(SANDSTONE, Assets.getBlockReg(0, 12));
		texs.put(STONEBRICK, Assets.getBlockReg(6, 3));
		texs.put(NETHERBRICK, Assets.getBlockReg(0, 14));
		texs.put(QUARTZ, Assets.getBlockReg(2, 14));
		
		slab = new Box(new BoundingBox(MIN.setZero(), MAX.set(1, 0.5f, 1)));
		
		BoundingBox bound = new BoundingBox(MIN.setZero(), MAX.set(1, 0.5f, 0.5f));
		bound.min.add(0, 0.5f, 0.5f);
		bound.max.add(0, 0.5f, 0.5f);
		piece1 = new Box(bound);
		
		bound = new BoundingBox(MIN.setZero(), MAX.set(0.5f, 0.5f, 0.5f));
		bound.min.add(0, 0.5f, 0);
		bound.max.add(0, 0.5f, 0);
		piece2 = new Box(bound);
		
		quads.addAll(CubeModel.QUADS);
	}

	@Override
	public void build(QuadBuilder build, BlockPos pos) {
		build.ambLight = true;
		build.simpleLight = false;
		build.region = getDefaultTex(pos);
		Shape state = stairComp.getState(pos);
		
		buildBox(pos, build.isThread());
		BoundingBox box = slab.getBox(build.isThread());
		
		Box.buildUp(build, box).flush(pos);
		Box.buildDown(build, box).flush(pos);
		Box.buildNorth(build, box).flush(pos);
		Box.buildEast(build, box).flush(pos);
		Box.buildSouth(build, box).flush(pos);
		Box.buildWest(build, box).flush(pos);
		
		if (state.isInner() || state == Shape.STRAIGHT) {
			box = piece1.getBox(build.isThread());
			Box.buildUp(build, box).flush(pos);
			Box.buildDown(build, box).flush(pos);
			Box.buildNorth(build, box).flush(pos);
			Box.buildEast(build, box).flush(pos);
			Box.buildSouth(build, box).flush(pos);
			Box.buildWest(build, box).flush(pos);
		}
		
		if (state != Shape.STRAIGHT) {
			box = piece2.getBox(build.isThread());
			Box.buildUp(build, box).flush(pos);
			Box.buildDown(build, box).flush(pos);
			Box.buildNorth(build, box).flush(pos);
			Box.buildEast(build, box).flush(pos);
			Box.buildSouth(build, box).flush(pos);
			Box.buildWest(build, box).flush(pos);
		}
	}
	
	private void buildBox(BlockPos pos, boolean isMesh) {
		final Facing face = horiComp.getFace(pos);
		Shape state = stairComp.getState(pos);
		final boolean isFlip = stairComp.isFlip(pos);
		
		slab.offset(0, isFlip?0.5f:0, 0, isMesh);
		if (state.isInner() || state == Shape.STRAIGHT) {
			piece1.offset(0, isFlip?-0.5f:0, 0, isMesh);
			piece1.face(face, false, isMesh);
		}
		
		if (state.isInner()) {
			piece2.offset((state == Shape.INNER_RIGHT)?0.5f:0, isFlip?-0.5f:0, 0, isMesh);
		} else if (state.isOuter()) {
			piece2.offset((state == Shape.OUTER_RIGHT)?0:0.5f, isFlip?-0.5f:0, 0.5f, isMesh);
		}
		
		if (state != Shape.STRAIGHT) {
			piece2.face(face, false, isMesh);
		}
	}

	@Override
	public Array<BoundingBox> getBoundingBoxes(BlockPos pos) {
		boxes.size = 0;
		buildBox(pos, false);
		Shape state = stairComp.getState(pos);
		
		boxes.add(slab.main);
		
		if (state.isInner() || state == Shape.STRAIGHT) {
			boxes.add(piece1.main);
		}
		
		if (state != Shape.STRAIGHT) {
			boxes.add(piece2.main);
		}
		
		return boxes;
	}
	
	@Override
	public void draw(EntityBatch batch, int type) {
		batch.setTexture(Assets.TERRAIN);
		ModelUtil.build(batch, slab.box, getDefaultTex(type));
		ModelUtil.build(batch, piece1.box, getDefaultTex(type));
	}

	@Override
	public TextureRegion getDefaultTex(int type) {
		return texs.get(type);
	}
	
	@Override
	public boolean isFaceSolid(BlockPos pos, Facing face) {
		final Facing facing = horiComp.getFace(pos);
		if (facing.invert() == face) {
			return true;
		}
		
		final boolean isFlip = stairComp.isFlip(pos);
		if (isFlip && face == Facing.UP) {
			return true;
		}
		
		if (!isFlip && face == Facing.DOWN) {
			return true;
		}
		
		return false;
	}

	@Override
	public boolean useFullCube() {
		return true;
	}
}
