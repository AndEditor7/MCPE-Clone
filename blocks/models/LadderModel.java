package com.andedit.arcubit.blocks.models;

import static com.andedit.arcubit.blocks.LadderBlock.comp;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.blocks.Block;
import com.andedit.arcubit.graphics.quad.QuadBuilder;
import com.andedit.arcubit.graphics.quad.QuadNode;
import com.andedit.arcubit.utils.BlockPos;
import com.andedit.arcubit.utils.Facing;
import com.andedit.arcubit.utils.Facing.Axis;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;

public final class LadderModel extends BlockModel {
	
	private final TextureRegion tex = Assets.getBlockReg(3, 5);
	
	private final QuadNode south, east;

	public LadderModel(Block block) {
		super(block);
		boxes.add(new BoundingBox());
		
		final float a = 1/16f;
		south = new QuadNode();
		south.p1.set(1, 0, a);
		south.p2.set(1, 1, a);
		south.p3.set(0, 1, a);
		south.p4.set(0, 0, a);
		south.face = Facing.SOUTH;
		south.noLit();
		
		east = new QuadNode();
		east.p1.set(a, 0, 0);
		east.p2.set(a, 1, 0);
		east.p3.set(a, 1, 1);
		east.p4.set(a, 0, 1);
		east.face = Facing.EAST;
		east.noLit();
	}

	@Override
	public void build(QuadBuilder build, BlockPos pos) {
		build.region = getDefaultTex(0);
		Facing face = comp.getFace(pos);
		final float a = 15/16f;
		
		if (face.axis == Axis.Z) {
			build.set(south);
			if (face == Facing.NORTH) {
				build.p1.z = a;
				build.p2.z = a;
				build.p3.z = a;
				build.p4.z = a;
				
				build.p1.x = 0;
				build.p2.x = 0;
				build.p3.x = 1;
				build.p4.x = 1;
				
				build.face = face;
			}
			build.flush(pos);
		} 
		
		if (face.axis == Axis.X) {
			build.set(east);
			if (face == Facing.WEST) {
				build.p1.x = a;
				build.p2.x = a;
				build.p3.x = a;
				build.p4.x = a;
				
				build.p1.z = 1;
				build.p2.z = 1;
				build.p3.z = 0;
				build.p4.z = 0;
				
				build.face = face;
			}
			build.flush(pos);
		}
	}

	@Override
	public Array<BoundingBox> getBoundingBoxes(BlockPos pos) {
		final float a = 0.5f;
		final BoundingBox box = boxes.first();
		switch (comp.getFace(pos)) {
		case NORTH:
			box.set(MIN.set(0, 0, 1-a), MAX.set(1, 1, 1));
			break;
		case EAST:
			box.set(MIN.set(0, 0, 0), MAX.set(a, 1, 1));
			break;
		case SOUTH:
			box.set(MIN.set(0, 0, 0), MAX.set(1, 1, a));
			break;
		case WEST:
			box.set(MIN.set(1-a, 0, 0), MAX.set(1, 1, 1));
			break;
		default: box.set(MIN.setZero(), MAX.set(1, 1, 1)); break;
		}
		return boxes;
	}

	@Override
	public TextureRegion getDefaultTex(int type) {
		return tex;
	}

	@Override
	public boolean use3D() {
		return false;
	}
}
