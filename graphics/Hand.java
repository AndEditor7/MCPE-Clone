package com.andedit.arcubit.graphics;

import static com.andedit.arcubit.Assets.STEVE;
import static com.andedit.arcubit.world.World.world;

import com.andedit.arcubit.blocks.utils.CubeTex;
import com.andedit.arcubit.entity.models.Box;
import com.andedit.arcubit.graphics.quad.QuadIndexBuffer;
import com.andedit.arcubit.graphics.vertex.VoxelTerrain;
import com.andedit.arcubit.handles.Static;
import com.andedit.arcubit.items.BlockItem;
import com.andedit.arcubit.items.Item;
import com.andedit.arcubit.ui.actors.Hotbar;
import com.andedit.arcubit.utils.Camera;
import com.andedit.arcubit.utils.Util;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public class Hand {
	private final Camera camera = new Camera();
	private final EntityBatch batch = Static.BATCH1;
	private final Matrix4 mat = new Matrix4();
	private final Box hand;
	
	public Hotbar hotbar;
	
	public Hand() {
		camera.far = 10.0f;
		camera.near = 0.05f;
		camera.fieldOfView = 70;
		
		final float w, h, l;
		w = 4/32f;
		h = 12/32f;
		l = 4/32f;
		
		final BoundingBox box = new BoundingBox(new Vector3(-w, -h, -l), new Vector3(w, h, l));
		final CubeTex texs = new CubeTex(
			new TextureRegion(STEVE, 44, 16, 4, 4), new TextureRegion(STEVE, 48, 16, 4, 4), new TextureRegion(STEVE, 40, 20, 4, 12),
			new TextureRegion(STEVE, 52, 20, 4, 12), new TextureRegion(STEVE, 48, 20, 4, 12), new TextureRegion(STEVE, 44, 20, 4, 12)
		);
		hand = new Box(texs, box);
	}
	
	public void resize(int width, int height) {
		camera.viewportWidth = width;
		camera.viewportHeight = height;
	}
	
	private void draw(Item item) {
		if (item == null) {
			Matrix4 mat = batch.pushMatrix();
			
			if (swing <= 0) {
				mat.trn(0.0f, -0.2f, 0.0f);
				mat.scl(2f);
				mat = batch.pushMatrix();
				mat.setFromEulerAngles(-20, 140, 40);
			} else {
				mat.trn(0.0f, -0.2f, 0.0f);
				mat.scl(1.8f);
				mat = batch.pushMatrix();
				mat.setFromEulerAngles(10, 170, 15);
			}
			
			batch.setTexture(STEVE.getTexture());
			hand.render(batch);		
			return;
		}
		
		if (item instanceof BlockItem && ((BlockItem)item).block.getBlockModel().use3D()) {
			((BlockItem)item).block.getBlockModel().draw(batch, item.type);
		} else {
			final TextureRegion reg = item.getTexture();
			
			batch.setTexture(reg.getTexture());
			
			Matrix4 mat = batch.pushMatrix();
			mat.setFromEulerAngles(49, 0, 156);
			
			mat = batch.pushMatrix();
			mat.trn(0.67f, -0.85f, -0.75f);
			
			ItemModel.draw(item, batch);
			
			batch.clearMatrix();
		}
	}
	
	float swing = 0;
	
	public void render(Vector3 pos, float swing2, float delta) {
		final float speed = swing < MathUtils.HALF_PI-0.3f ? 9f : 11f;
		swing = Math.max(swing-(speed*delta), 0.0f); // 0.15f
		//if (hotbar.getItem() == null) return;
		
		final Item item = hotbar.getItem();
		
		// swinging
		float itemSwing = 0.45f;
		float s = item == null ? 1.3f : itemSwing;
		boolean isItem = item == null ? false: true;
		
		if (item instanceof BlockItem && ((BlockItem)item).block.getBlockModel().use3D()) {
			isItem = false;
			s = 1f;
		}
		
		mat.idt();
		if (isItem) {
			mat.trn(1.4f-(MathUtils.sin(swing)*s), -1.3f-Math.min((MathUtils.sin(swing*2f)*0.3f), 0.2f), -1.8f);
			mat.rotate(Vector3.Y, 45.0f);
			mat.rotateRad(1, -0.2f, 0.2f, MathUtils.sin(-swing)*(s==itemSwing?1.3f:1f));
		} else {
			mat.trn(1.4f-(MathUtils.sin(swing)*s), -1.3f-Math.min((MathUtils.sin(swing*2f)*0.6f), 0.2f), -1.8f);
			mat.rotate(Vector3.Y, 45.0f);
			mat.rotateRad(Vector3.X, MathUtils.sin(-swing)*(s == 0.2f ? 1.3f : 1f));
		}
		
		// cam bobble
		camera.position.y = MathUtils.cos(swing2/2f)*(isItem ? 0.15f : 0.2f)-0.2f;
		camera.position.x = -swing2 * (isItem ? 0.03f : 0.05f);
		camera.update(false);
		camera.combined.mul(mat);
		
		// render
		QuadIndexBuffer.preBind();
		VoxelTerrain.begin(camera);
		batch.setLight(world.getData(pos));
		batch.begin();
		draw(item);
		batch.end();
		VoxelTerrain.end();
	}
	
	public void swing() {
		if (swing < MathUtils.HALF_PI-0.3f) {
			swing = MathUtils.PI;
		}
	}
	
	public void breaking() {
		swing = MathUtils.PI;
	}
}
