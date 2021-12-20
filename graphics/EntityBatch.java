package com.andedit.arcubit.graphics;

import com.andedit.arcubit.blocks.utils.BlockUtils;
import com.andedit.arcubit.graphics.vertex.Vertex;
import com.andedit.arcubit.graphics.vertex.VoxelTerrain;
import com.andedit.arcubit.utils.Util;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.Pool;

public class EntityBatch implements Disposable {
	private static final Pool<Matrix4> POOL = new Pool<Matrix4>() {
		protected Matrix4 newObject() {
			return new Matrix4();
		}
	};
	
	private static final int QUAD = VoxelTerrain.floatSize *  4;
	
	private int index;
	private final float[] buffer;
	private final Vertex vertex = Vertex.newVa(VoxelTerrain.context, Util.BUFFER);
	private final Array<Matrix4> matrixs = new Array<>(Matrix4.class);
	private final Matrix4[] mats = matrixs.items;
	private Texture texture;
	private float light;
	
	public EntityBatch() {
		this(1000);
	}
	
	public EntityBatch(int size) {
		buffer = new float[size * QUAD];
	}
	
	public void begin() {
		index = 0;
		vertex.bind();
		clearMatrix();
	}
	
	public void flush() {
		if (index == 0) return;
		texture.bind();
		vertex.setVertices(buffer, index, 0);
		Gdx.gl.glDrawElements(GL20.GL_TRIANGLES, (index / VoxelTerrain.byteSize) * 6, GL20.GL_UNSIGNED_SHORT, 0);
		index = 0;
	}
	
	public void end() {
		flush();
		vertex.unbind();
	}
	
	public void pos(Vector3 vec) {
		pos(vec.x, vec.y, vec.z);
	}
	
	public void pos(float x, float y, float z) {
		for (int i = matrixs.size-1; i >= 0; --i) {
			final float xx, yy, zz;
			final float[] val = mats[i].val;
			xx = x * val[Matrix4.M00] + y * val[Matrix4.M01] + z * val[Matrix4.M02] + val[Matrix4.M03];
			yy = x * val[Matrix4.M10] + y * val[Matrix4.M11] + z * val[Matrix4.M12] + val[Matrix4.M13];
			zz = x * val[Matrix4.M20] + y * val[Matrix4.M21] + z * val[Matrix4.M22] + val[Matrix4.M23];
			x = xx; y = yy; z = zz;
		}
		
		buffer[index]   = x;
		buffer[index+1] = y;
		buffer[index+2] = z;
		index += 3;
	}
	
	public void light() {
		buffer[index++] = light;
	}
	
	public void setLight(int data) {
		setLight(BlockUtils.toSunLight(data), BlockUtils.toSrcLight(data));
	}
	
	public void setLight(int data, float amb) {
		setLight(BlockUtils.toSunLight(data), BlockUtils.toSrcLight(data), amb);
	}
	
	public void setLight(int sun, int src) {
		light = Float.intBitsToFloat((((17*sun)<<16) | ((17*src)<<8) | 255));
	}
	
	public void setLight(int sun, int src, float amb) {
		light = Float.intBitsToFloat((((17*sun)<<16) | ((17*src)<<8) | ((int) (255*amb))));
	}
	
	public void tex(float u, float v) {
		buffer[index]   = u;
		buffer[index+1] = v;
		index += 2;
		
		if (index+1 >= buffer.length) {
			flush();
		}
	}
	
	public void setTexture(Texture texture) {
		if (this.texture != texture) {
			flush();
			this.texture = texture;
		}
	}
	
	public void add(FloatArray floatArray) {
		final int size = floatArray.size;
		final float[] array = floatArray.items;
		for (int i = 0; i < size; i += 5) {
			pos(array[i], array[i+1], array[i+2]);
			light();
			tex(array[i+3], array[i+4]);
		}
	}
	
	public Matrix4 pushMatrix() {
		final Matrix4 mat = POOL.obtain();
		matrixs.add(mat);
		return mat;
	}
	
	public Matrix4 peekMatrix() {
		return matrixs.peek();
	}
	
	public void popMatrix() {
		if (matrixs.notEmpty()) {
			POOL.free(matrixs.pop().idt());
		}
	}
	
	public void clearMatrix() {
		while (matrixs.notEmpty()) {
			POOL.free(matrixs.pop().idt());
		}
	}

	@Override
	public void dispose() {
		vertex.dispose();
	}
}
