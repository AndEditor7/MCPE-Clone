package com.andedit.arcubit.blocks.models;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.blocks.Block;
import com.andedit.arcubit.graphics.quad.QuadBuilder;
import com.andedit.arcubit.graphics.quad.QuadNode;
import com.andedit.arcubit.utils.BlockPos;
import com.andedit.arcubit.utils.Facing;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;

public class CropModel extends BlockModel {
	
	private final TextureRegion[] textures = new TextureRegion[8];
	private final QuadNode quad1, quad2, quad3, quad4;

	public CropModel(Block block) {
		super(block);
		boxes.add(new BoundingBox(MIN.setZero(), MAX.set(1, 0.3f, 1)));
		
		for (int i = 0; i < 8; i++) {
			textures[i] = Assets.getBlockReg(8+i, 5);
		}
		
		final float a = 0.5f;
		quad1 = new QuadNode();
		quad1.p1.set(0, 0, a);
		quad1.p2.set(0, 1, a);
		quad1.p3.set(1, 1, a);
		quad1.p4.set(1, 0, a);
		quad1.face = Facing.NORTH;
		quad1.ambLight = false;
		quad1.simpleLight = true;
		
		quad2 = new QuadNode();
		quad2.p1.set(a, 0, 0);
		quad2.p2.set(a, 1, 0);
		quad2.p3.set(a, 1, 1);
		quad2.p4.set(a, 0, 1);
		quad2.face = Facing.EAST;
		quad2.ambLight = false;
		quad2.simpleLight = true;
		
		quad3 = new QuadNode();
		quad3.p1.set(1, 0, a);
		quad3.p2.set(1, 1, a);
		quad3.p3.set(0, 1, a);
		quad3.p4.set(0, 0, a);
		quad3.face = Facing.SOUTH;
		quad3.ambLight = false;
		quad3.simpleLight = true;
		
		quad4 = new QuadNode();
		quad4.p1.set(a, 0, 1);
		quad4.p2.set(a, 1, 1);
		quad4.p3.set(a, 1, 0);
		quad4.p4.set(a, 0, 0);
		quad4.face = Facing.WEST;
		quad4.ambLight = false;
		quad4.simpleLight = true;
		
		quads.add(quad1, quad2, quad3, quad4);
	}

	@Override
	public void build(QuadBuilder build, BlockPos pos) {
		build.region = getDefaultTex(pos);
		for (int i = 0; i < quads.size; i++ ) {
			build.set(quads.get(i)).flush(pos);
		}
	}

	@Override
	public Array<BoundingBox> getBoundingBoxes(BlockPos pos) {
		return boxes;
	}

	@Override
	public TextureRegion getDefaultTex(int data) {
		return textures[MathUtils.clamp(data, 0, 7)];
	}

}
