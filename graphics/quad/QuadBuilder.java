package com.andedit.arcubit.graphics.quad;

import static com.andedit.arcubit.world.World.world;

import com.andedit.arcubit.blocks.utils.BlockUtils;
import com.andedit.arcubit.graphics.vertex.VertInfo;
import com.andedit.arcubit.graphics.vertex.Vertex;
import com.andedit.arcubit.graphics.vertex.VoxelTerrain;
import com.andedit.arcubit.utils.BlockPos;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.FloatArray;

public class QuadBuilder extends QuadNode {
	private static final int maxFloats = QuadIndexBuffer.maxVertex*VoxelTerrain.floatSize;

	private final FloatArray vertices = new FloatArray(8192) {
		protected float[] resize (int newSize) {
			if (items.length == maxFloats) throw new IllegalStateException("Max vertex size has been reached!");
			return super.resize(Math.min(newSize, maxFloats));	
		}
	};
	
	public TextureRegion region;
	
	public final BlockPos offset = new BlockPos();
	public final Matrix4 matrix = new Matrix4();
	
	private final VertInfo
	v1 = new VertInfo(),
	v2 = new VertInfo(),
	v3 = new VertInfo(),
	v4 = new VertInfo();
	
	private float 
	cu1, cv1,
	cu2, cv2,
	cu3, cv3,
	cu4, cv4;
	
//  v3-----v2
//  |       |
//  |       |
//  v4-----v1
	public void flush(BlockPos pos) {
		final int x = pos.x, y = pos.y, z = pos.z;
		final boolean useWhat = this.useWhat;
		
		final int data;
		setAmb(ambLight ? getAmbLit() : lightHigh);
		if (simpleLight) {
			data = world.getData(x, y, z);
		} else {
			switch (face) {
			case UP:    data = world.getData(x, y+1, z); break;
			case DOWN:  data = world.getData(x, y-1, z); break;
			case NORTH: data = world.getData(x, y, z-1); break;
			case WEST:  data = world.getData(x-1, y, z); break;
			case SOUTH: data = world.getData(x, y, z+1); break;
			case EAST:  data = world.getData(x+1, y, z); break;
			default: data = 0;
			}
		}
		
		setSrc(BlockUtils.toSrcLight(data) / BlockUtils.lightScl);
		setSun(BlockUtils.toSunLight(data) / BlockUtils.lightScl);
		
		final TextureRegion region = this.region;
		final float xf = x, yf = y, zf = z;
		vertices.add(p1.x+xf, p1.y+yf, p1.z+zf, v1.packData());
		vertices.add(useWhat ? cu1 : region.getU2(), useWhat ? cv1 : region.getV2());

		vertices.add(p2.x+xf, p2.y+yf, p2.z+zf, v2.packData());
		vertices.add(useWhat ? cu2 : region.getU2(), useWhat ? cv2 : region.getV());

		vertices.add(p3.x+xf, p3.y+yf, p3.z+zf, v3.packData());
		vertices.add(useWhat ? cu3 : region.getU(), useWhat ? cv3 : region.getV());

		vertices.add(p4.x+xf, p4.y+yf, p4.z+zf, v4.packData());
		vertices.add(useWhat ? cu4 : region.getU(), useWhat ? cv4 : region.getV2());
	}
	
	public QuadBuilder set(QuadNode node) {
		super.set(node);
		return this;
	}
	
	private void setSun(float sun) {
		v1.sunLit = sun;
		v2.sunLit = sun;
		v3.sunLit = sun;
		v4.sunLit = sun;
	}

	private void setSrc(float src) {
		v1.srcLit = src;
		v2.srcLit = src;
		v3.srcLit = src;
		v4.srcLit = src;
	}

	public void setAmb(float amb) {
		v1.ambLit = amb;
		v2.ambLit = amb;
		v3.ambLit = amb;
		v4.ambLit = amb;
	}
	
	public QuadBuilder mul(Matrix4 matrix) {
		p1.sub(0.5f).mul(matrix).add(0.5f);
		p2.sub(0.5f).mul(matrix).add(0.5f);
		p3.sub(0.5f).mul(matrix).add(0.5f);
		p4.sub(0.5f).mul(matrix).add(0.5f);
		return this;
	}
	
	private final TextureRegion temReg = new TextureRegion();
	public TextureRegion region() {
		return region = temReg;
	}
	
	public void calcRegion(TextureRegion tex) {
		switch (face) {
		case UP:
			cu1 = p1.x;
			cv1 = p1.z;
			cu2 = p2.x;
			cv2 = p2.z;
			cu3 = p3.x;
			cv3 = p3.z;
			cu4 = p4.x;
			cv4 = p4.z;
			break;
		case DOWN:
			cu1 = 1f-p1.x;
			cv1 = 1f-p1.z;
			cu2 = 1f-p2.x;
			cv2 = 1f-p2.z;
			cu3 = 1f-p3.x;
			cv3 = 1f-p3.z;
			cu4 = 1f-p4.x;
			cv4 = 1f-p4.z;
			break;
		case NORTH:
			cu1 = 1f-p1.x;
			cv1 = 1f-p1.y;
			cu2 = 1f-p2.x;
			cv2 = 1f-p2.y;
			cu3 = 1f-p3.x;
			cv3 = 1f-p3.y;
			cu4 = 1f-p4.x;
			cv4 = 1f-p4.y;
			break;
		case EAST:
			cu1 = 1f-p1.z;
			cv1 = 1f-p1.y;
			cu2 = 1f-p2.z;
			cv2 = 1f-p2.y;
			cu3 = 1f-p3.z;
			cv3 = 1f-p3.y;
			cu4 = 1f-p4.z;
			cv4 = 1f-p4.y;
			break;
		case SOUTH:
			cu1 = p1.x;
			cv1 = 1f-p1.y;
			cu2 = p2.x;
			cv2 = 1f-p2.y;
			cu3 = p3.x;
			cv3 = 1f-p3.y;
			cu4 = p4.x;
			cv4 = 1f-p4.y;
			break;
		case WEST:
			cu1 = p1.z;
			cv1 = 1f-p1.y;
			cu2 = p2.z;
			cv2 = 1f-p2.y;
			cu3 = p3.z;
			cv3 = 1f-p3.y;
			cu4 = p4.z;
			cv4 = 1f-p4.y;
			break;
		}
		
		final float
		uOffset = tex.getU(),
		vOffset = tex.getV(),
		uScale = tex.getU2() - uOffset,
		vScale = tex.getV2() - vOffset;
		
		cu1 = uOffset + uScale * cu1;
		cu2 = uOffset + uScale * cu2;
		cu3 = uOffset + uScale * cu3;
		cu4 = uOffset + uScale * cu4;
		
		cv1 = vOffset + vScale * cv1;
		cv2 = vOffset + vScale * cv2;
		cv3 = vOffset + vScale * cv3;
		cv4 = vOffset + vScale * cv4;
		
		useWhat = true;
	}
	
	public boolean isThread() {
		return true;
	}
	
	public int size() {
		return vertices.size;
	}
	
	public float[] getItem() {
		return vertices.items;
	}
	
	public boolean isEmpty() {
		return vertices.isEmpty();
	}

	public void begin() {
		vertices.clear();
		useWhat = false;
	}
	
	public void end(Vertex vertex) {
		vertex.setVertices(vertices.items, vertices.size, 0);
	}

	public void add(QuadBuilder trans) {
		vertices.addAll(trans.vertices);
	}
}