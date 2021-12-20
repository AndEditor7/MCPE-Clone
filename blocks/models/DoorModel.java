package com.andedit.arcubit.blocks.models;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.blocks.DoorBlock;
import com.andedit.arcubit.graphics.quad.QuadBuilder;
import com.andedit.arcubit.graphics.quad.QuadNode;
import com.andedit.arcubit.utils.BlockPos;
import com.andedit.arcubit.utils.Facing;
import com.andedit.arcubit.utils.Util;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;

public class DoorModel extends BlockModel {
	
	private static final float LEN = 2f/16f;
	
	private final TextureRegion upper = Assets.getBlockReg(1, 5);
	private final TextureRegion lower = Assets.getBlockReg(1, 6);
	
	private final QuadNode quad1, quad2, quad3, quad4, quad5, quad6;
	
	private final DoorBlock door;

	public DoorModel(DoorBlock door) {
		super(door);
		this.door = door;
		
		quad1 = new QuadNode();
		quad1.p1.set(1, 1, 0);
		quad1.p2.set(0, 1, 0);
		quad1.p3.set(0, 1, LEN);
		quad1.p4.set(1, 1, LEN);
		quad1.face = Facing.UP;
		
		quad2 = new QuadNode();
		quad2.p1.set(0, 0, 0);
		quad2.p2.set(1, 0, 0);
		quad2.p3.set(1, 0, LEN);
		quad2.p4.set(0, 0, LEN);
		quad2.face = Facing.DOWN;
		
		quad3 = new QuadNode();
		quad3.p1.set(0, 0, 0);
		quad3.p2.set(0, 1, 0);
		quad3.p3.set(1, 1, 0);
		quad3.p4.set(1, 0, 0);
		quad3.face = Facing.NORTH;
		
		quad4 = new QuadNode();
		quad4.p1.set(1, 0, 0);
		quad4.p2.set(1, 1, 0);
		quad4.p3.set(1, 1, LEN);
		quad4.p4.set(1, 0, LEN);
		quad4.face = Facing.EAST;
		
		quad5 = new QuadNode();
		quad5.p1.set(1, 0, LEN);
		quad5.p2.set(1, 1, LEN);
		quad5.p3.set(0, 1, LEN);
		quad5.p4.set(0, 0, LEN);
		quad5.face = Facing.SOUTH;
		
		quad6 = new QuadNode();
		quad6.p1.set(0, 0, LEN);
		quad6.p2.set(0, 1, LEN);
		quad6.p3.set(0, 1, 0);
		quad6.p4.set(0, 0, 0);;
		quad6.face = Facing.WEST;
		
		quads.add(quad1, quad2, quad3);
		quads.add(quad5, quad6, quad4);
	}

	@Override
	public void build(QuadBuilder build, BlockPos pos) {
		final boolean isUpper = door.isUpper(pos);
		final boolean isOpen = door.isOpen(pos);
		final TextureRegion region = isUpper ? upper : lower;
		final Matrix4 matrix = build.matrix;
		matrix.set(isOpen ? OPEN : NONE);
		
		final Facing face = door.horizontalComponent.getFace(pos);
		final Matrix4 mat;
		switch (face) {
		case EAST:  mat = MUL_EAST; break;
		case SOUTH: mat = MUL_SOUTH; break;
		case WEST:  mat = MUL_WEST; break;
		default: mat = NONE; break;
		}
		
		build.region().setRegion(region);
		for (QuadNode quad : quads) {
			if (isUpper) {
				if (quad == quad2) continue;
			} else {
				if (quad == quad1) continue;
			}
			
			build.set(quad).mul(matrix).mul(mat);
			
			if (quad == quad1 || quad == quad2) {
				build.region.setRegion(region, 0, 0, 1, 1);;
			}
			
			if (quad == quad3) {
				build.simpleLight = isOpen;
			}
			
			if (quad == quad4) {
				build.region.setRegionWidth(1);
			}
			
			if (quad == quad5) {
				build.region.flip(true, false);
				build.simpleLight = !isOpen;
			}
			
			if (quad == quad6) {
				build.region.setRegion(region, 15, 0, 1, 16);
			}
			
			build.face = isOpen ? quad.face.rotateLeft() : quad.face;
			build.face = build.face.rotate(face.getRotateValue());
			build.flush(pos);
			
			build.region.setRegion(region);
		}
	}

	@Override
	public TextureRegion getDefaultTex(int data) {
		return upper;
	}
	
	public static final BoundingBox NORTH;
	private static final Array<BoundingBox> TEMP = new Array<>(1);
	
	static {
		NORTH = new BoundingBox(MIN.set(0, 0, 0), MAX.set(1, 1, LEN));
		TEMP.add(new BoundingBox());
	}
	
	@Override
	public Array<BoundingBox> getBoundingBoxes(BlockPos pos) {
		BoundingBox tmp = TEMP.get(0).set(NORTH);
		final boolean isOpen = door.isOpen(pos);
		
		Util.rotate(tmp, isOpen ? OPEN : NONE);
		
		final Matrix4 matrix;
		switch (door.horizontalComponent.getFace(pos)) {
		case EAST:  matrix = MUL_EAST; break;
		case SOUTH: matrix = MUL_SOUTH; break;
		case WEST:  matrix = MUL_WEST; break;
		default: matrix = NONE; break;
		}
		
		Util.rotate(tmp, matrix);
		return TEMP;
	}
	
	@Override
	public boolean use3D() {
		return false;
	}
	
	public Array<QuadNode> getQuads() {
		return CubeModel.QUADS;
	}
	
	private static final Matrix4 NONE = new Matrix4();
	private static final Matrix4 OPEN = new Matrix4().setFromEulerAngles(90f, 0, 0).trn(1f-LEN, 0f, 0f);
	
	static final Matrix4 MUL_EAST  = new Matrix4().setFromEulerAngles(270f, 0, 0);
	static final Matrix4 MUL_SOUTH = new Matrix4().setFromEulerAngles(180f, 0, 0);
	static final Matrix4 MUL_WEST  = new Matrix4().setFromEulerAngles(90f, 0, 0);
}
