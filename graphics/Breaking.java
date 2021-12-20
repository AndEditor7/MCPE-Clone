package com.andedit.arcubit.graphics;

import static com.andedit.arcubit.world.World.world;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.blocks.Blocks;
import com.andedit.arcubit.blocks.models.BlockModel;
import com.andedit.arcubit.graphics.quad.QuadNode;
import com.andedit.arcubit.graphics.vertex.VoxelTerrain;
import com.andedit.arcubit.utils.BlockPos;
import com.andedit.arcubit.utils.Camera;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public final class Breaking {
	public int cooldown;
	public float process;
	public final BlockPos pos = new BlockPos();
	
	private Camera bCam = new Camera();
	
	@SuppressWarnings("unused")
	private static float
	uOffset = 0f,
	vOffset = 0f,
	uScale = 1f,
	vScale = 1f;
	
	@SuppressWarnings("unused")
	private static void setUVRange (float u1, float v1, float u2, float v2) {
		uOffset = u1;
		vOffset = v1;
		uScale = u2 - u1;
		vScale = v2 - v1;
	}
	
	public void render(EntityBatch batch, Camera cam) {
		if (process == 0) return;
		BlockModel model = world.getBlock(pos).getBlockModel();
		if (model == null) return;
		batch.flush();
		batch.setTexture(Assets.TERRAIN);
		
		bCam.position.set(cam.position);
		bCam.direction.set(cam.direction);
		bCam.up.set(cam.up);
		bCam.fieldOfView = cam.fieldOfView;
		bCam.viewportWidth = cam.viewportWidth;
		bCam.viewportHeight = cam.viewportHeight;
		bCam.near = cam.near / 0.998f;
		bCam.far = cam.far / 0.998f;
		bCam.update(false);
		Gdx.gl.glBlendFunc(GL20.GL_DST_COLOR, GL20.GL_ZERO);
		VoxelTerrain.shader.setUniformMatrix("projTrans", bCam.combined);
		
		final Matrix4 mat = batch.pushMatrix();
		mat.trn(pos.x, pos.y, pos.z);
		
		//final int data = world.getData(floor(cam.position.x), floor(cam.position.y), floor(cam.position.z));
		batch.setLight(15, 15);
		
		uOffset = 0f;
		vOffset = 0f;
		uScale = 1f;
		vScale = 1f;
		
		final TextureRegion breaks = Assets.BREAKS[MathUtils.clamp((int)(9*process), 0, 9)];
		if (model.useFullCube()) {
			model = Blocks.STONE.getBlockModel();
		}
		
		for (QuadNode quad : model.getQuads()) {
			final Vector3 p1 = quad.p1, p2 = quad.p2, p3 = quad.p3, p4 = quad.p4;
			
			batch.pos(p1);
			batch.light();
			batch.tex(breaks.getU2(), breaks.getV2());
			
			batch.pos(p2);
			batch.light();
			batch.tex(breaks.getU2(), breaks.getV());
			
			batch.pos(p3);
			batch.light();
			batch.tex(breaks.getU(), breaks.getV());
			
			batch.pos(p4);
			batch.light();
			batch.tex(breaks.getU(), breaks.getV2());
		}
		
		batch.popMatrix();
		batch.flush();
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	}
}
