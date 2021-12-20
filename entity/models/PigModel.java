package com.andedit.arcubit.entity.models;

import static com.andedit.arcubit.world.World.world;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.blocks.utils.CubeTex;
import com.andedit.arcubit.entity.Pig;
import com.andedit.arcubit.graphics.EntityBatch;
import com.andedit.arcubit.utils.Util;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public class PigModel implements Model<Pig>{
	
	public final Box body, head, rightLeg, leftLeg, nose;
	
	public PigModel() {
		float w = 10/32f;
		float h = 8/32f;
		float l = 16/32f;
		
		final TextureRegion pig = new TextureRegion(Assets.MOBS, 192, 33, 64, 32);
		BoundingBox box = new BoundingBox(MIN.set(-w, -h, -l), MAX.set(w, h, l));
		CubeTex texs = new CubeTex(
			new TextureRegion(pig, 54, 16, 10, 16), new TextureRegion(pig, 36, 16, 10, 16), new TextureRegion(pig, 36, 8, 10, 8),
			new TextureRegion(pig, 28, 16, 8, 16), new TextureRegion(pig, 46, 8, 10, 8), new TextureRegion(pig, 46, 16, 8, 16)
		);
		texs.south.flip(true, false);
		
		body = new Box(texs, box) {
			protected void west(TextureRegion tex) {
				leftUV(tex);
			}
			protected void east(TextureRegion tex) {
				rightUV(tex);
			}
		};
		
		w = 8/32f;
		h = 8/32f;
		l = 8/16f;
		
		box = new BoundingBox(MIN.set(-w, -h, -l), MAX.set(w, h, 0));
		texs = new CubeTex(
			new TextureRegion(pig, 8, 0, 8, 8), new TextureRegion(pig, 24, 8, 8, 8), new TextureRegion(pig, 8, 8, 8, 8),
			new TextureRegion(pig, 0, 8, 8, 8), new TextureRegion(pig, 16, 0, 8, 8), new TextureRegion(pig, 16, 8, 8, 8)
		);
		
		head = new Box(texs, box);
		
		w = 4/32f;
		h = 6/16f;
		l = 4/32f;
		
		box = new BoundingBox(MIN.set(-w, -h, -l), MAX.set(w, 0, l));
		texs = new CubeTex(
			new TextureRegion(pig, 4, 16, 4, 4), new TextureRegion(pig, 8, 16, 4, 4), new TextureRegion(pig, 4, 20, 4, 6),
			new TextureRegion(pig, 0, 20, 4, 6), new TextureRegion(pig, 8, 20, 4, 6), new TextureRegion(pig, 0, 20, 4, 6)
		);
		texs.west.flip(true, false);
		rightLeg = new Box(texs, box);
		
		box = new BoundingBox(MIN.set(-w, -h, -l), MAX.set(w, 0, l));
		texs = new CubeTex(
			new TextureRegion(pig, 4, 16, 4, 4), new TextureRegion(pig, 8, 16, 4, 4), new TextureRegion(pig, 4, 20, 4, 6),
			new TextureRegion(pig, 0, 20, 4, 6), new TextureRegion(pig, 8, 20, 4, 6), new TextureRegion(pig, 0, 20, 4, 6)
		);
		texs.west.flip(true, false);
		texs.south.flip(true, false);
		texs.north.flip(true, false);
		texs.bottom.flip(true, false);
		leftLeg = new Box(texs, box);
		
		w = 4/32f;
		h = 3/16f;
		l = 1/16f;
		
		box = new BoundingBox(MIN.set(-w, 0, -l), MAX.set(w, h, 0));
		texs = new CubeTex(
			new TextureRegion(pig, 17, 16, 4, 1), new TextureRegion(pig, 17, 16, 4, 1), new TextureRegion(pig, 17, 17, 4, 3),
			new TextureRegion(pig, 16, 17, 1, 3), new TextureRegion(pig, 17, 17, 4, 3), new TextureRegion(pig, 16, 17, 1, 3)
		);
		
		nose = new Box(texs, box);
	}
	
	@Override
	public void render(EntityBatch batch, Pig pig) {
		final float tilt = Math.min(1f, pig.tilt);
		final Vector3 pos = pig.getPos();
		pos.y += MathUtils.lerp(10f, 6f, tilt)/16f; // 8
		
		batch.setLight(world.getData(pos));
		batch.setTexture(Assets.TEXTURE);
		
		// body
		Matrix4 mat = batch.pushMatrix();
		mat.trn(pos);
		
		Util.rotateY(mat, pig.bodyYaw * MathUtils.degreesToRadians, 1);
		mat.rotate(Vector3.Z, MathUtils.lerp(0f, 90f, tilt));
		body.render(batch);
		
		// head
		mat = batch.pushMatrix();
		mat.rotate(Vector3.Y, pig.headYaw);		
		mat.trn(0, 2/16f, -6/16f);
		head.render(batch);
		batch.popMatrix();
		
		// nose
		mat = batch.pushMatrix();
		mat.rotate(Vector3.Y, pig.headYaw);
		mat.trn(0, 2/16f, -6/16f);
		mat = batch.pushMatrix();
		mat.trn(0, -3/16f, -8/16f);
		nose.render(batch);
		batch.popMatrix();
		batch.popMatrix();
		
		final float swing = MathUtils.sin(pig.swing) * 0.9f;
		//rightLeg
		mat = batch.pushMatrix();
		mat.setToTranslation(3/16f, -4/16f, -5/16f);
		Util.rotateX(mat, -swing, 1);
		rightLeg.render(batch);
		mat.setToTranslation(3/16f, -4/16f, 7/16f);
		Util.rotateX(mat, swing, 1);
		rightLeg.render(batch);
		
		//leftLeg
		mat.setToTranslation(-3/16f, -4/16f, -5/16f);
		Util.rotateX(mat, swing, 1);
		leftLeg.render(batch);
		mat.setToTranslation(-3/16f, -4/16f, 7/16f);
		Util.rotateX(mat, -swing, 1);
		leftLeg.render(batch);
		
		batch.clearMatrix();
	}
}
