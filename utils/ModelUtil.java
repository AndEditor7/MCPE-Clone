package com.andedit.arcubit.utils;

import com.andedit.arcubit.blocks.utils.CubeTex;
import com.andedit.arcubit.graphics.EntityBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public final class ModelUtil {
	private static final CubeTex TEXS = new CubeTex();
	
	private static final Vector3 
	P1 = new Vector3(),
	P2 = new Vector3(),
	P3 = new Vector3(),
	P4 = new Vector3();
	
	private static final Vector2 
	R1 = new Vector2(),
	R2 = new Vector2(),
	R3 = new Vector2(),
	R4 = new Vector2();
	
	public static void build(EntityBatch batch, BoundingBox box, TextureRegion reg) {
		build(batch, box, TEXS.set(reg));
	}
	
	public static void build(EntityBatch batch, BoundingBox box, CubeTex texs) {
		box.getCorner111(P1);
		box.getCorner110(P2);
		box.getCorner010(P3);
		box.getCorner011(P4);
		calc(texs.top, Facing.UP);
		rect(batch);
		
		box.getCorner100(P1);
		box.getCorner101(P2);
		box.getCorner001(P3);
		box.getCorner000(P4);
		calc(texs.bottom, Facing.DOWN);
		rect(batch);
		
		box.getCorner000(P1);
		box.getCorner010(P2);
		box.getCorner110(P3);
		box.getCorner100(P4);
		calc(texs.north, Facing.NORTH);
		rect(batch);
		
		box.getCorner100(P1);
		box.getCorner110(P2);
		box.getCorner111(P3);
		box.getCorner101(P4);
		calc(texs.east, Facing.EAST);
		rect(batch);
		
		box.getCorner101(P1);
		box.getCorner111(P2);
		box.getCorner011(P3);
		box.getCorner001(P4);
		calc(texs.south, Facing.SOUTH);
		rect(batch);
		
		box.getCorner001(P1);
		box.getCorner011(P2);
		box.getCorner010(P3);
		box.getCorner000(P4);
		calc(texs.west, Facing.EAST);
		rect(batch);
	}
	
	private static void rect(EntityBatch batch) {
		final float a = 0.5f;
		batch.pos(P1.sub(a));
		batch.light();
		batch.tex(R1.x, R1.y);
		
		batch.pos(P2.sub(a));
		batch.light();
		batch.tex(R2.x, R2.y);
		
		batch.pos(P3.sub(a));
		batch.light();
		batch.tex(R3.x, R3.y);
		
		batch.pos(P4.sub(a));
		batch.light();
		batch.tex(R4.x, R4.y);
	}
	
	private static void calc(TextureRegion reg, Facing face) {
		final float a = 1;
		switch (face) {
		case UP:
			R1.set(P1.x, P1.z);
			R2.set(P2.x, P2.z);
			R3.set(P3.x, P3.z);
			R4.set(P4.x, P4.z);
			break;
		case DOWN:
			R1.set(a-P1.x, a-P1.z);
			R2.set(a-P2.x, a-P2.z);
			R3.set(a-P3.x, a-P3.z);
			R4.set(a-P4.x, a-P4.z);
			break;
		case NORTH:
			R1.set(a-P1.x, a-P1.y);
			R2.set(a-P2.x, a-P2.y);
			R3.set(a-P3.x, a-P3.y);
			R4.set(a-P4.x, a-P4.y);
			break;
		case EAST:
			R1.set(a-P1.z, a-P1.y);
			R2.set(a-P2.z, a-P2.y);
			R3.set(a-P3.z, a-P3.y);
			R4.set(a-P4.z, a-P4.y);
			break;
		case SOUTH:
			R1.set(P1.x, a-P1.y);
			R2.set(P2.x, a-P2.y);
			R3.set(P3.x, a-P3.y);
			R4.set(P4.x, a-P4.y);
			break;
		case WEST:
			R1.set(P1.z, a-P1.y);
			R2.set(P2.z, a-P2.y);
			R3.set(P3.z, a-P3.y);
			R4.set(P4.z, a-P4.y);
			break;
		}
		
		final float
		uOffset = reg.getU(),
		vOffset = reg.getV(),
		uScale = reg.getU2() - uOffset,
		vScale = reg.getV2() - vOffset;
		
		R1.set(uOffset + uScale * R1.x, vOffset + vScale * R1.y);
		R2.set(uOffset + uScale * R2.x, vOffset + vScale * R2.y);
		R3.set(uOffset + uScale * R3.x, vOffset + vScale * R3.y);
		R4.set(uOffset + uScale * R4.x, vOffset + vScale * R4.y);
	}
}
