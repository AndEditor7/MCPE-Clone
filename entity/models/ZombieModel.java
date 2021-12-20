package com.andedit.arcubit.entity.models;

import static com.andedit.arcubit.world.World.world;
import static com.andedit.arcubit.TheGame.game;
import static com.andedit.arcubit.blocks.utils.BlockUtils.toSrcLight;
import static com.andedit.arcubit.blocks.utils.BlockUtils.toSunLight;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.blocks.utils.BlockUtils;
import com.andedit.arcubit.blocks.utils.CubeTex;
import com.andedit.arcubit.entity.Zombie;
import com.andedit.arcubit.graphics.EntityBatch;
import com.andedit.arcubit.utils.Util;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public class ZombieModel implements Model<Zombie> {
	
	private final Box body, head, hand, leg;
	
	public ZombieModel() {
		final TextureRegion zombie = new TextureRegion(Assets.MOBS, 65, 0, 64, 32);
		float w = 8/32f;
		float h = 12/32f;
		float l = 4/32f;
		
		BoundingBox box = new BoundingBox(MIN.set(-w, -h, -l), MAX.set(w, h, l));
		CubeTex texs = new CubeTex(
			new TextureRegion(zombie, 20, 16, 8, 4),  new TextureRegion(zombie, 28, 16, 8, 4), new TextureRegion(zombie, 20, 20, 8, 12),
			new TextureRegion(zombie, 16, 20, 4, 12), new TextureRegion(zombie, 32, 20, 8, 12), new TextureRegion(zombie, 28, 20, 4, 12)
		);
		
		body = new Box(texs, box);
		
		w = 8/32f;
		h = 8/16f;
		l = 8/32f;
		box = new BoundingBox(MIN.set(-w, 0, -l), MAX.set(w, h, l));
		texs = new CubeTex(
			new TextureRegion(zombie, 8, 0, 8, 8),  new TextureRegion(zombie, 16, 0, 8, 8), new TextureRegion(zombie, 8, 8, 8, 8),
			new TextureRegion(zombie, 0, 8, 8, 8), new TextureRegion(zombie, 8, 0, 8, 8), new TextureRegion(zombie, 16, 8, 8, 8)
		);
		
		head = new Box(texs, box);
		
		w = 4/32f;
		h = 8/16f;
		l = 4/32f;
		box = new BoundingBox(MIN.set(-w, -10/16f, -l), MAX.set(w, 2/16f, l));
		texs = new CubeTex(
			new TextureRegion(zombie, 44, 16, 4, 4),  new TextureRegion(zombie, 48, 16, 4, 4), new TextureRegion(zombie, 44, 20, 4, 12),
			new TextureRegion(zombie, 40, 20, 4, 12), new TextureRegion(zombie, 52, 20, 4, 12), new TextureRegion(zombie, 48, 20, 4, 12)
		);
		
		hand = new Box(texs, box);
		
		box = new BoundingBox(MIN.set(-w, -12/16f, -l), MAX.set(w, 0, l));
		texs = new CubeTex(
			new TextureRegion(zombie, 44-40, 16, 4, 4),  new TextureRegion(zombie, 48-40, 16, 4, 4), new TextureRegion(zombie, 44-40, 20, 4, 12),
			new TextureRegion(zombie, 40-40, 20, 4, 12), new TextureRegion(zombie, 52-40, 20, 4, 12), new TextureRegion(zombie, 48-40, 20, 4, 12)
		);
		
		leg = new Box(texs, box);
	}

	@Override
	public void render(EntityBatch batch, Zombie entity) {
		
		int data = world.getData(entity.getCenter());
		batch.setLight(toSunLight(data), entity.isOnFire() ? 15 : toSrcLight(data));
		batch.setTexture(Assets.TEXTURE);
		
		// body
		Matrix4 mat = batch.pushMatrix();
		mat.trn(entity.getPos());
		
		mat = batch.pushMatrix();
		mat.rotate(Vector3.Y, entity.bodyYaw);
		mat.rotate(Vector3.Z, MathUtils.lerp(0f, 90f, Math.min(1f, entity.tilt)));
		mat.trn(0, 5/16f, 0);
		
		mat = batch.pushMatrix();
		mat.trn(0, 13/16f, 0);
		body.render(batch);
		
		// head
		mat = batch.pushMatrix();
		mat.rotate(Vector3.Y, entity.headYaw);		
		mat.trn(0, 6/16f, 0);
		head.render(batch);
		batch.popMatrix();
		
		// hand
		mat = batch.pushMatrix();
		Util.rotateX(mat, MathUtils.HALF_PI, 1);
		mat.setTranslation(-6/16f, 4/16f, 0);
		hand.render(batch);
		mat.setTranslation(6/16f, 4/16f, 0);
		hand.render(batch);
		batch.popMatrix();
		
		// leg
		mat = batch.pushMatrix();
		Util.rotateX(mat, MathUtils.sin(entity.swing) * 0.7f, 1);
		mat.setTranslation(-2/16f, -6/16f, 0);
		leg.render(batch);
		Util.rotateX(mat, -MathUtils.sin(entity.swing) * 0.7f, 1);
		mat.setTranslation(2/16f, -6/16f, 0);
		leg.render(batch);
		batch.popMatrix();
		
		batch.clearMatrix();
		mat = batch.pushMatrix();
		mat.setTranslation(entity.getPos());
		
		if (entity.isOnFire()) {
			Vector3 pos = game.getCamera().position;
			float x = pos.x - mat.val[Matrix4.M03];
			float z = pos.z - mat.val[Matrix4.M23];
			float angle = (MathUtils.PI - MathUtils.atan2(z, x)) + MathUtils.HALF_PI;
			mat.trn(-MathUtils.sin(angle) * 0.2f, 0.4f, -MathUtils.cos(angle) * 0.2f);
			Util.rotateY(mat, angle, 1);
			
			for (int i = 0; i < 3; i++) {
				Model.drawFire(batch);
				mat.val[Matrix4.M13] += 0.65f;
			}
		}
		
		batch.popMatrix();
	}

}
