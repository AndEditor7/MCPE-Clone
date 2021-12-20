package com.andedit.arcubit.blocks.models;

import com.andedit.arcubit.graphics.quad.QuadBuilder;
import com.andedit.arcubit.utils.Facing;
import com.andedit.arcubit.utils.Util;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public class Box {
	private static final Vector3 VEC = new Vector3();
	
	public final BoundingBox box, mesh, main;
	
	public Box(BoundingBox box) {
		this.box = box;
		this.mesh = new BoundingBox(box);
		this.main = new BoundingBox(box);
	}
	
	public static QuadBuilder buildUp(QuadBuilder build, BoundingBox box) {
		box.getCorner111(build.p1);
		box.getCorner110(build.p2);
		box.getCorner010(build.p3);
		box.getCorner011(build.p4);
		build.face = Facing.UP;
		build.calcRegion(build.region);
		return build;
	}
	
	public static QuadBuilder buildDown(QuadBuilder build, BoundingBox box) {
		box.getCorner100(build.p1);
		box.getCorner101(build.p2);
		box.getCorner001(build.p3);
		box.getCorner000(build.p4);
		build.face = Facing.DOWN;
		build.calcRegion(build.region);
		return build;
	}
	
	public static QuadBuilder buildNorth(QuadBuilder build, BoundingBox box) {
		box.getCorner000(build.p1);
		box.getCorner010(build.p2);
		box.getCorner110(build.p3);
		box.getCorner100(build.p4);
		build.face = Facing.NORTH;
		build.calcRegion(build.region);
		return build;
	}
	
	public static QuadBuilder buildEast(QuadBuilder build, BoundingBox box) {
		box.getCorner100(build.p1);
		box.getCorner110(build.p2);
		box.getCorner111(build.p3);
		box.getCorner101(build.p4);
		build.face = Facing.EAST;
		build.calcRegion(build.region);
		return build;
	}
	
	public static QuadBuilder buildSouth(QuadBuilder build, BoundingBox box) {
		box.getCorner101(build.p1);
		box.getCorner111(build.p2);
		box.getCorner011(build.p3);
		box.getCorner001(build.p4);
		build.face = Facing.SOUTH;
		build.calcRegion(build.region);
		return build;
	}
	
	public static QuadBuilder buildWest(QuadBuilder build, BoundingBox box) {
		box.getCorner001(build.p1);
		box.getCorner011(build.p2);
		box.getCorner010(build.p3);
		box.getCorner000(build.p4);
		build.face = Facing.WEST;
		build.calcRegion(build.region);
		return build;
	}

	public void offset(float x, float y, float z, boolean isMesh) {
		final BoundingBox mod = getBox(isMesh);
		mod.min.set(box.min).add(x, y, z);
		mod.max.set(box.max).add(x, y, z);
	}
	
	public void add(float x, float y, float z, boolean isMesh) {
		final BoundingBox mod = getBox(isMesh);
		mod.min.add(x, y, z);
		mod.max.add(x, y, z);
	}
	
	public void face(Facing face, boolean reset, boolean isMesh) {
		final BoundingBox mod = getBox(isMesh);
		switch (face) {
		case EAST: mul(DoorModel.MUL_EAST, reset, isMesh); break;
		case SOUTH: mul(DoorModel.MUL_SOUTH, reset, isMesh); break;
		case WEST: mul(DoorModel.MUL_WEST, reset, isMesh); break;
		default: if (reset) mod.set(box);
		}
	}
	
	public void mul(Matrix4 mat, boolean reset, boolean isMesh) {
		final BoundingBox mod = getBox(isMesh);
		Util.rotate(reset ? mod.set(box) : mod, mat);
	}
	
	public BoundingBox reset(boolean isMesh) {
		return getBox(isMesh).set(box);
	}
	
	public BoundingBox getBox(boolean isMesh) {
		return isMesh ? mesh : main;
	}
}
