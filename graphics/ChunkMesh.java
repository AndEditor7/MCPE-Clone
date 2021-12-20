package com.andedit.arcubit.graphics;

import static com.andedit.arcubit.options.Options.dist;

import com.andedit.arcubit.graphics.quad.QuadBuilder;
import com.andedit.arcubit.graphics.vertex.Vertex;
import com.andedit.arcubit.graphics.vertex.VoxelTerrain;
import com.andedit.arcubit.utils.Util;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.GridPoint3;
import com.badlogic.gdx.math.Plane;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool.Poolable;

public class ChunkMesh implements Disposable, Poolable {
	
	private final Vertex vertex = Vertex.newVbo(VoxelTerrain.context, GL20.GL_STREAM_DRAW);
	private int countOpaqe, countTrans;
	
	public byte x, y, z;
	
	public boolean isVisible(final Plane[] planes) {
		final int s = planes.length;
		final float x, y, z;
		x = (this.x<<4)+8;
		y = (this.y<<4)+8;
		z = (this.z<<4)+8;


		for (int i = 2; i < s; i++) {
			final Plane plane = planes[i];
			final Vector3 normal = plane.normal;
			final float dist = normal.dot(x, y, z) + plane.d;
			
			final float radius = 
			8f * Math.abs(normal.x) +
			8f * Math.abs(normal.y) +
			8f * Math.abs(normal.z);

			if (dist < radius && dist < -radius) {
				return false;
			}
		}
		return true;
	}
	
	public void renderOpaqe() {
		if (countOpaqe == 0) return; 
		vertex.bind();
		Gdx.gl.glDrawElements(GL20.GL_TRIANGLES, countOpaqe, GL20.GL_UNSIGNED_SHORT, 0);
		if (!Util.isGL30()) vertex.unbind();
	}
	
	public void renderTrans() {
		if (countTrans == 0) return; 
		vertex.bind();
		Gdx.gl.glDrawElements(GL20.GL_TRIANGLES, countTrans, GL20.GL_UNSIGNED_SHORT, countOpaqe<<1);
		if (!Util.isGL30()) vertex.unbind();
	}
	
	public void setVertices(QuadBuilder opaqe, QuadBuilder trans) {
		countOpaqe = (opaqe.size() / VoxelTerrain.byteSize) * 6;
		countTrans = (trans.size() / VoxelTerrain.byteSize) * 6;
		opaqe.add(trans);
		vertex.setVertices(opaqe.getItem(), opaqe.size(), 0);
	}

	@Override
	public void dispose() {
		vertex.dispose();
	}

	@Override
	public void reset() {
		countOpaqe = 0;
		countTrans = 0;
	}

	public boolean isEmpty() {
		return countOpaqe == 0 && countTrans == 0;
	}
	
	public boolean isTransEmpty() {
		return countTrans == 0;
	}
	
	public boolean pass(GridPoint3 pos, int offset) {
		final int rad = dist.value + offset;
		return x < (-rad)+pos.x || y < (-rad)+pos.y || z < (-rad)+pos.z || x > rad+pos.x || y > rad+pos.y || z > rad+pos.z;
	}
	
	public void setDirty() {
		Renderer.DIRTS[x][y][z] = true;
	}
}
