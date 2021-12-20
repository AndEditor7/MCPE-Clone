package com.andedit.arcubit.entity.models;

import static com.andedit.arcubit.world.World.world;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.blocks.utils.CubeTex;
import com.andedit.arcubit.entity.Sheep;
import com.andedit.arcubit.graphics.EntityBatch;
import com.andedit.arcubit.handles.Inputs;
import com.andedit.arcubit.utils.Util;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public class SheepModel implements Model<Sheep> {
	
	private Box body, head, leg, bodyWool, headWool, legWool;
	
	{
		create();
	}
	
	private void create() {
		BoundingBox box;
		CubeTex tex;
		float w, h, l;
		
		tex = new CubeTex();
		tex.texNorth(326, 455, 8, 6);
		tex.texSouth(334, 455, 8, 6);
		tex.texEast(320, 461, 6, 16);
		tex.texWest(334, 461, 6, 16);
		tex.texTop(340, 461, 8, 16);
		tex.texBottom(326, 461, 8, 16);
		
		w = 8/32f;
		h = 6/32f;
		l = 0.5f;
		box = new BoundingBox(MIN.set(-w, -h, -l), MAX.set(w, h, l));
		body = new Box(tex, box) {
			protected void west(TextureRegion tex) {
				leftUV(tex);
			}
			protected void east(TextureRegion tex) {
				rightUV(tex);
			}
		};
		
		tex = new CubeTex();
		tex.texNorth(300, 455, 6, 6);
		tex.texSouth(314, 455, 6, 6);
		tex.texEast(292, 455, 8, 6);
		tex.texWest(306, 455, 8, 6);
		tex.texTop(300, 447, 6, 8);
		tex.texBottom(306, 447, 6, 8);
		
		w = 6/32f;
		h = 6/32f;
		l = 8/16f;
		box = new BoundingBox(MIN.set(-w, -h, -l), MAX.set(w, h, 0));
		head = new Box(tex, box);
		
		tex = new CubeTex();
		tex.texNorth(296, 467, 4, 12);
		tex.texSouth(304, 467, 4, 12);
		tex.texEast(292, 467, 4, 12);
		tex.texWest(300, 467, 4, 12);
		tex.texTop(296, 463, 4, 4);
		tex.texBottom(300, 463, 4, 4);
		
		w = 4/32f;
		h = 12/16f;
		l = 4/32f;
		box = new BoundingBox(MIN.set(-w, -h, -l), MAX.set(w, 0, l));
		leg = new Box(tex, box);
		
		
		final int a = -33;
		tex = new CubeTex();
		tex.texNorth(326, 455+a, 8, 7);
		tex.texSouth(334, 455+a, 8, 7);
		tex.texEast(320, 461+a, 7, 16);
		tex.texWest(334, 461+a, 7, 16);
		tex.texTop(340, 461+a, 8, 16);
		tex.texBottom(326, 461+a, 8, 16);
		
		w = 8/32f;
		h = 6/32f;
		l = 0.5f;
		box = new BoundingBox(MIN.set(-w, -h, -l), MAX.set(w, h, l));
		bodyWool = new Box(tex, box) {
			protected void west(TextureRegion tex) {
				leftUV(tex);
			}
			protected void east(TextureRegion tex) {
				rightUV(tex);
			}
		};
		
		tex = new CubeTex();
		tex.texNorth(300, 455+a, 6, 6);
		tex.texSouth(312, 455+a, 6, 6);
		tex.texEast(294, 455+a, 6, 6);
		tex.texWest(304, 455+a, 6, 6);
		tex.texTop(300, 449+a, 6, 6);
		tex.texBottom(306, 449+a, 6, 6);
		
		w = 6/32f;
		h = 6/32f;
		l = 6/16f;
		box = new BoundingBox(MIN.set(-w, -h, -l), MAX.set(w, h, 0));
		headWool = new Box(tex, box);
		tex = new CubeTex();
		tex.texNorth(296, 467+a, 4, 6);
		tex.texSouth(304, 467+a, 4, 6);
		tex.texEast(292, 467+a, 4, 6);
		tex.texWest(300, 467+a, 4, 6);
		tex.texTop(296, 463+a, 4, 4);
		tex.texBottom(300, 463+a, 4, 4);
		
		w = 4/32f;
		h = 6/16f;
		l = 4/32f;
		box = new BoundingBox(MIN.set(-w, -h, -l), MAX.set(w, 0, l));
		legWool = new Box(tex, box);
	}

	@Override
	public void render(EntityBatch batch, Sheep entity) {
		
		if (Inputs.isKeyJustPressed(Keys.P)) {
			create();
		}
		
		final boolean hasWool = entity.hasWool;
		final float tilt = Math.min(1f, entity.tilt);
		final Vector3 pos = entity.getPos();
		pos.y += MathUtils.lerp(15f, 6f, tilt)/16f; // 8
		
		batch.setLight(world.getData(pos));
		batch.setTexture(Assets.TEXTURE);
		
		// body
		Matrix4 mat = batch.pushMatrix();
		mat.trn(pos);
		Util.rotateY(mat, entity.bodyYaw * MathUtils.degreesToRadians, 1);
		mat.rotate(Vector3.Z, MathUtils.lerp(0f, 90f, tilt));
		body.render(batch);
		
		if (hasWool) {
			batch.pushMatrix().scl(1.4f, 1.7f, 1.2f);
			bodyWool.render(batch);
			batch.popMatrix();
		}
		
		// head
		mat = batch.pushMatrix();
		mat.rotate(Vector3.Y, entity.headYaw);		
		mat.trn(0, 4/16f, -6/16f);
		head.render(batch);
		
		if (hasWool) {
			batch.pushMatrix().scl(1.2f).trn(0, 0, 0.01f);
			headWool.render(batch);
			batch.popMatrix();
		}
		
		batch.popMatrix();
		
		//rightLeg
		float swing = MathUtils.sin(entity.swing) * 0.7f;
		mat = batch.pushMatrix();
		mat.setToTranslation(3/16f, -3/16f, -5/16f);
		Util.rotateX(mat, -swing, 1);
		leg.render(batch);
		
		if (hasWool) {
			batch.pushMatrix().scl(1.2f);
			legWool.render(batch);
			batch.popMatrix();
		}
		
		mat.setToTranslation(3/16f, -3/16f, 7/16f);
		Util.rotateX(mat, swing, 1);
		leg.render(batch);
		
		if (hasWool) {
			batch.pushMatrix().scl(1.2f);
			legWool.render(batch);
			batch.popMatrix();
		}
		
		//leftLeg
		mat.setToTranslation(-3/16f, -3/16f, -5/16f);
		Util.rotateX(mat, swing, 1);
		leg.render(batch);
		
		if (hasWool) {
			batch.pushMatrix().scl(1.2f);
			legWool.render(batch);
			batch.popMatrix();
		}
		
		mat.setToTranslation(-3/16f, -3/16f, 7/16f);
		Util.rotateX(mat, -swing, 1);
		leg.render(batch);
		
		if (hasWool) {
			batch.pushMatrix().scl(1.2f);
			legWool.render(batch);
			batch.popMatrix();
		}
		
		batch.clearMatrix();
	}

}
