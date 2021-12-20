package com.andedit.arcubit.graphics;

import static com.andedit.arcubit.TheGame.game;
import static com.andedit.arcubit.options.Options.clouds;
import static com.andedit.arcubit.options.Options.dist;
import static com.andedit.arcubit.world.World.world;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.blocks.Blocks;
import com.andedit.arcubit.blocks.LiquidBlock;
import com.andedit.arcubit.entity.Entity;
import com.andedit.arcubit.graphics.quad.QuadIndexBuffer;
import com.andedit.arcubit.graphics.vertex.VoxelTerrain;
import com.andedit.arcubit.handles.ChunkHandle;
import com.andedit.arcubit.handles.Static;
import com.andedit.arcubit.particle.ParticleSystem;
import com.andedit.arcubit.utils.Camera;
import com.andedit.arcubit.utils.SimplePool;
import com.andedit.arcubit.utils.Util;
import com.andedit.arcubit.world.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.GridPoint3;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class Renderer implements Disposable {

	private final MeshPool meshPool = new MeshPool();
	private class MeshPool extends SimplePool<ChunkMesh> {
		MeshPool() {
			super(3000);
		}
		
		void freeAndDispose(Array<ChunkMesh> meshes) {
			freeAll(meshes);
			for (ChunkMesh mesh : objects) {
				mesh.dispose();
			}
		}

		protected ChunkMesh newObject() {
			return new ChunkMesh();
		}
	};
	
	private static final int SIZE = World.SIZE>>4, HEIGHT = World.HEIGHT>>4;
	static final boolean[][][] DIRTS = new boolean[SIZE][HEIGHT][SIZE];
	
	private final Array<ChunkMesh> meshes = new Array<>(false, 1500);
	private final GridPoint3 chunkPos = new GridPoint3();
	
	private final Array<ChunkMesh> trans = new Array<>(64);
	private final ChunkHandle handle = Static.HANDLE;
	
	// Renderers
	private final EntityBatch batch = Static.BATCH2;
	private final SkyBox skyBox = Static.SKYBOX;
	
	public Renderer() {
		for (int x = 0; x < SIZE; x++)
		for (int y = 0; y < HEIGHT; y++)
		for (int z = 0; z < SIZE; z++) {
			DIRTS[x][y][z] = true;
		}
		
		dist.addConsumer(t->{
			for (int i = 0; i < meshes.size; i++) {
				ChunkMesh mesh = meshes.get(i);
				
				if (mesh.pass(chunkPos, 1)) {
					meshes.removeIndex(i--);
					mesh.setDirty();
					mesh.dispose();
					continue;
				}
			}
		});
	}
	
	public void chunkUpdate(Camera camera) {
		world.light.calculateLights();
		final Vector3 camPos = camera.position;
		chunkPos.set(MathUtils.floor(camPos.x)>>4, MathUtils.floor(camPos.y)>>4, MathUtils.floor(camPos.z)>>4);
		
		handle.build(meshPool, meshes);
		
		final int radius = dist.value;
		loop :
		for (int x = chunkPos.x-radius; x <= chunkPos.x+radius; x++)
		for (int y = chunkPos.y-radius; y <= chunkPos.y+radius; y++)
		for (int z = chunkPos.z-radius; z <= chunkPos.z+radius; z++) {
			if (isOutBound(x, y, z)) continue;
			
			if (DIRTS[x][y][z]) {
				if (handle.isReady()) {
					handle.add(x, y, z);
					DIRTS[x][y][z] = false;
				} else break loop;
			}
		}

		handle.start();
	}
	
	public void render(Camera camera) {
		Gdx.gl.glDisable(GL20.GL_BLEND);
		skyBox.renderSky(camera);
		
		Assets.TERRAIN.bind();
		VoxelTerrain.begin(camera);
		QuadIndexBuffer.preBind();
		
		renderTerrain(camera.frustum.planes);
		renderEntity(camera);
		
		if (clouds.value && camera.position.y < World.HEIGHT) {
			skyBox.renderClouds(camera);
			VoxelTerrain.shader.bind();
		}
		renderWater(camera);
		if (clouds.value && camera.position.y > World.HEIGHT) {
			skyBox.renderClouds(camera);
		}
		
		VoxelTerrain.end();
	}
	
	private void renderTerrain(Plane[] planes) {
		for (int i = 0; i < meshes.size; i++) {
			ChunkMesh mesh = meshes.get(i);
			
			if (mesh.pass(chunkPos, 1)) {
				meshPool.free(meshes.removeIndex(i--));
				mesh.setDirty();
				continue;
			}
			
			if (mesh.isEmpty()) {
				meshPool.free(meshes.removeIndex(i--));
				continue;
			}
			
			if (!mesh.pass(chunkPos, 0) && !mesh.isEmpty() && mesh.isVisible(planes)) {
				mesh.renderOpaqe();
				if (!mesh.isTransEmpty()) trans.add(mesh);
			}
		}
		if (Util.isGL30()) Gdx.gl30.glBindVertexArray(0);
	}
	
	private void renderEntity(Camera camera) {
		ParticleSystem.render(camera);
		batch.begin();
		for (final Entity entity : world.entities) {
			final Vector3 pos = entity.getCenter();
			if (pos.dst2(camera.position) < 7000f && camera.frustum.sphereInFrustumWithoutNearFar(pos, 1f)) {
				entity.render(batch);
			}
		}
		for (final Entity entity : world.items) {
			final Vector3 pos = entity.getCenter();
			if (pos.dst2(camera.position) < 6000f && camera.frustum.sphereInFrustumWithoutNearFar(pos, 0.5f)) {
				entity.render(batch);
			}
		}
		batch.flush();
		Gdx.gl.glEnable(GL20.GL_BLEND);
		game.steve.breaking.render(batch, camera);
		batch.end();
	}
	
	private void renderWater(Camera camera) {
		if (trans.isEmpty()) return;
		Assets.TERRAIN.bind();
		if (!(world.getBlock(camera.position) instanceof LiquidBlock)) Gdx.gl.glEnable(GL20.GL_CULL_FACE);
		for (ChunkMesh mesh : trans) {
			mesh.renderTrans();
		}
		Gdx.gl.glDisable(GL20.GL_CULL_FACE);
		if (Util.isGL30()) Gdx.gl30.glBindVertexArray(0);
		trans.size = 0;
	}

	public void clearMeshes() {
		for (ChunkMesh mesh : meshes) {
			mesh.setDirty();
			meshPool.free(mesh);
		}
		meshes.clear();
	}
	
	public static boolean isOutBound(int x, int y, int z) {
		return x < 0 || y < 0 || z < 0 || x >= SIZE || y >= HEIGHT || z >= SIZE;
	}
	
	public void dirty(int x, int y, int z) {
		final int xAnd = x & 15, yAnd = y & 15, zAnd = z & 15;
		x >>= 4; y >>= 4; z >>= 4;
		
		if (!isOutBound(x, y, z)) {
			DIRTS[x][y][z] = true;
		}
		
		if (xAnd == 0) {
			if (!isOutBound(x-1, y, z)) DIRTS[x-1][y][z] = true;
		} else if (xAnd == 15) {
			if (!isOutBound(x+1, y, z)) DIRTS[x+1][y][z] = true;
		}
		
		if (yAnd == 0) {
			if (!isOutBound(x, y-1, z)) DIRTS[x][y-1][z] = true;
		} else if (yAnd == 15) {
			if (!isOutBound(x, y+1, z)) DIRTS[x][y+1][z] = true;
		}
		
		if (zAnd == 0) {
			if (!isOutBound(x, y, z-1)) DIRTS[x][y][z-1] = true;
		} else if (zAnd == 15) {
			if (!isOutBound(x, y, z+1)) DIRTS[x][y][z+1] = true;
		}
	}
	
	@Override
	public void dispose() {
		meshPool.freeAndDispose(meshes);
		handle.clear();
	}
}
