package com.andedit.arcubit.blocks.models;

import static com.andedit.arcubit.blocks.models.CubeModel.quad1;
import static com.andedit.arcubit.blocks.models.CubeModel.quad2;
import static com.andedit.arcubit.Assets.getBlockReg;

import com.andedit.arcubit.blocks.Block;
import com.andedit.arcubit.blocks.utils.CubeTex;
import com.andedit.arcubit.graphics.quad.QuadBuilder;
import com.andedit.arcubit.graphics.quad.QuadNode;
import com.andedit.arcubit.utils.BlockPos;
import com.andedit.arcubit.utils.Facing;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;

public class CactusModel extends BlockModel {
	
	private final QuadNode quad3, quad4, quad5, quad6;
	private final CubeTex texs = new CubeTex(getBlockReg(5, 4), getBlockReg(6, 4), getBlockReg(7, 4));
	
	public CactusModel(Block block) {
		super(block);
		final float a = 1/16f;
		boxes.add(new BoundingBox(MIN.set(a, 0, a), MAX.set(1-a, 1, 1-a)));
		
		quad3 = new QuadNode();
		quad3.p1.set(0, 0, a);
		quad3.p2.set(0, 1, a);
		quad3.p3.set(1, 1, a);
		quad3.p4.set(1, 0, a);
		quad3.face = Facing.NORTH;
		
		quad4 = new QuadNode();
		quad4.p1.set(1-a, 0, 0);
		quad4.p2.set(1-a, 1, 0);
		quad4.p3.set(1-a, 1, 1);
		quad4.p4.set(1-a, 0, 1);
		quad4.face = Facing.EAST;
		
		quad5 = new QuadNode();
		quad5.p1.set(1, 0, 1-a);
		quad5.p2.set(1, 1, 1-a);
		quad5.p3.set(0, 1, 1-a);
		quad5.p4.set(0, 0, 1-a);
		quad5.face = Facing.SOUTH;
		
		quad6 = new QuadNode();
		quad6.p1.set(a, 0, 1);
		quad6.p2.set(a, 1, 1);
		quad6.p3.set(a, 1, 0);
		quad6.p4.set(a, 0, 0);
		quad6.face = Facing.WEST;
	}

	@Override
	public void build(QuadBuilder build, BlockPos pos) {
		final int x = pos.x, y = pos.y, z = pos.z;
		final BlockPos offset = build.offset;
		
		if (canAddFace(pos, offset.set(x, y+1, z), Facing.UP)) {
			build.region = texs.top;
			build.set(quad1).flush(pos);
		}
		
		if (canAddFace(pos, offset.set(x, y-1, z), Facing.DOWN)) {
			build.region = texs.bottom;
			build.set(quad2).flush(pos);
		}
		
		build.region = texs.north;
		build.set(quad3).flush(pos);
		build.set(quad4).flush(pos);
		build.set(quad5).flush(pos);
		build.set(quad6).flush(pos);
	}
	
	@Override
	public boolean use3D() {
		return false;
	}

	@Override
	public Array<BoundingBox> getBoundingBoxes(BlockPos pos) {
		return boxes;
	}

	@Override
	public TextureRegion getDefaultTex(int type) {
		return texs.north;
	}
	
	@Override
	public CubeTex getCubeTex(int type) {
		return texs;
	}

	@Override
	public boolean isFaceSolid(BlockPos pos, Facing face) {
		return false;
	}
}
