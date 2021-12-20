package com.andedit.arcubit.particle;

import static com.andedit.arcubit.world.World.world;

import com.andedit.arcubit.blocks.utils.BlockUtils;
import com.andedit.arcubit.graphics.vertex.Vertex;
import com.andedit.arcubit.graphics.vertex.VoxelTerrain;
import com.andedit.arcubit.utils.Camera;
import com.andedit.arcubit.utils.Util;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public final class ParticleSystem {
	
	private static final Pool<Particle> pool = new Pool<Particle>(256) {
		protected Particle newObject() {
			return new Particle();
		}
	};
	
	private static final Array<Particle> array = new Array<>(false, 256);
	private static final float[] buffer = new float[VoxelTerrain.floatSize * 4000];
	private static Vertex vertex;
	private static int idx;
	
	private static final Vector3 right = new Vector3();
	private static final Vector3 up = new Vector3();
	private static final Vector3 down = new Vector3();
	
	// particles
	public static Particle newPart(PartComp comp) {
		Particle part = pool.obtain();
		array.add(part.setComp(comp));
		return part;
	}
	
	public static void ints() {
		vertex = Vertex.newVa(VoxelTerrain.context, Util.BUFFER);
	}
	
	public static void update() {
		for (int i = 0; i < array.size; i++) {
			final Particle part = array.get(i);
			part.update();
			if (part.isDead()) {
				pool.free(array.removeIndex(i--));
			}
		}
	}
	
	public static void render(Camera camera) {
		if (array.isEmpty()) return; 
		
		right.set(camera.direction).crs(camera.up);
		up.set(right).add(camera.up);
		down.set(right).sub(camera.up);
		
		vertex.bind();
		
		for (Particle part : array) {
			if (idx / VoxelTerrain.byteSize >= 1000) {
				flush();
			}
			
			final Vector3 pos = part.getPos();
			final TextureRegion tex = part.tex;
			final float size = part.size;
			final int data = world.getData(pos);
			final float lit = Float.intBitsToFloat((((17*BlockUtils.toSunLight(data))<<16) | ((17*BlockUtils.toSrcLight(data))<<8) | 255));
			
			final int i = idx;
			
			buffer[i]    = pos.x + (down.x * size);
			buffer[i+1]  = pos.y + (down.y * size);
			buffer[i+2]  = pos.z + (down.z * size);
			buffer[i+3]  = lit;
			buffer[i+4]  = tex.getU2();
			buffer[i+5]  = tex.getV2();
			
			buffer[i+6]  = pos.x + (up.x * size);
			buffer[i+7]  = pos.y + (up.y * size);
			buffer[i+8]  = pos.z + (up.z * size);
			buffer[i+9]  = lit;
			buffer[i+10] = tex.getU2();
			buffer[i+11] = tex.getV();
			
			buffer[i+12] = pos.x - (down.x * size);
			buffer[i+13] = pos.y - (down.y * size);
			buffer[i+14] = pos.z - (down.z * size);
			buffer[i+15] = lit;
			buffer[i+16] = tex.getU();
			buffer[i+17] = tex.getV();
			
			buffer[i+18] = pos.x - (up.x * size);
			buffer[i+19] = pos.y - (up.y * size);
			buffer[i+20] = pos.z - (up.z * size);
			buffer[i+21] = lit;
			buffer[i+22] = tex.getU();
			buffer[i+23] = tex.getV2();
			
			idx = i + 24;
		}
		
		if (idx != 0) {
			flush();
		}
		
		vertex.unbind();
	}
	
	private static void flush() {
		vertex.setVertices(buffer, idx, 0);
		Gdx.gl.glDrawElements(GL20.GL_TRIANGLES, (idx / VoxelTerrain.byteSize) * 6, GL20.GL_UNSIGNED_SHORT, 0);
		idx = 0;
	}
	
	public static void clear() {
		pool.freeAll(array);
	}
	
	public static void dispose() {
		vertex.dispose();
	}
}
