package com.andedit.arcubit.utils.maths;

import static java.lang.Math.min;
import static java.lang.Math.max;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Pool;

public class CollisionBox {
	
	public static final Pool<CollisionBox> POOL = new Pool<CollisionBox>() {
		protected CollisionBox newObject() {
			return new CollisionBox();
		}
	};

	public float xMin, yMin, zMin;
	public float xMax, yMax, zMax;

	public CollisionBox() {
	}
	
	public CollisionBox(float[] array) {
		set(array[0], array[1], array[2], array[3], array[4], array[5]);
	}

	public void expand(float x, float y, float z, CollisionBox out) {
		out.set(min(xMin, xMin+x), min(yMin, yMin+y), min(zMin, zMin+z), max(xMax, xMax+x), max(yMax, yMax+y), max(zMax, zMax+z));
	}
	
	public void grow(float x, float y, float z, CollisionBox out) {
		out.set(xMin-x, yMin-y, zMin-z, xMax+x, yMax+y, zMax+z);
	}

	public CollisionBox set(BoundingBox box) {
		return set(box.min.x, box.min.y, box.min.z, box.max.x, box.max.y, box.max.z);
	}

	public CollisionBox set(CollisionBox box) {
		return set(box.xMin, box.yMin, box.zMin, box.xMax, box.yMax, box.zMax);
	}

	public CollisionBox move(float x, float y, float z) {
		return set(xMin+x, yMin+y, zMin+z, xMax+x, yMax+y, zMax+z);
	}

	public CollisionBox set(float xMin, float yMin, float zMin, float xMax, float yMax, float zMax) {
		this.xMin = xMin;
		this.yMin = yMin;
		this.zMin = zMin;
		this.xMax = xMax;
		this.yMax = yMax;
		this.zMax = zMax;
		return this;
	}
	
	public void set(float x, float y, float z, float size) {
		set(x, y, z, size, size);
	}
	
	public void set(float x, float y, float z, float w, float h) {
		w *= 0.5f; h *= 0.5f;
		set(x-w, y-h, z-w, x+w, y+h, z+w);
	}

	public float collideX(CollisionBox box, float x) {
		if (box.yMax <= yMin || box.yMin >= yMax || box.zMax <= zMin || box.zMin >= zMax) {
			return x;
		}
		return box.xMax<=xMin ? min(xMin-box.xMax,x) : box.xMin>=xMax ? max(xMax-box.xMin,x) : x;
	}

	public float collideY(CollisionBox box, float y) {
		if (box.xMax <= xMin || box.xMin >= xMax || box.zMax <= zMin || box.zMin >= zMax) {
			return y;
		}
		return box.yMax<=yMin ? min(yMin-box.yMax,y) : box.yMin>=yMax ? max(yMax-box.yMin,y) : y;
	}

	public float collideZ(CollisionBox box, float z) {
		if (box.xMax <= xMin || box.xMin >= xMax || box.yMax <= yMin || box.yMin >= yMax) {
			return z;
		}
		return box.zMax<=zMin ? min(zMin-box.zMax,z) : box.zMin>=zMax ? max(zMax-box.zMin,z) : z;
	}
	
	public boolean intersects(CollisionBox box) {
		return xMin < box.xMax && xMax > box.xMin && yMin < box.yMax && yMax > box.yMin && zMin < box.zMax && zMax > box.zMin;
	}
	
	@Override
	public CollisionBox clone() {
		return new CollisionBox().set(xMin, yMin, zMin, xMax, yMax, zMax);
	}
	
	public boolean intersect(final Ray ray) {
		final Vector3 direction = ray.direction;
		final Vector3 origin = ray.origin;
		final float divX = 1f / direction.x;
		final float divY = 1f / direction.y;
		final float divZ = 1f / direction.z;
		float t;
		
		float minx = (xMin - origin.x) * divX;
		float maxx = (xMax - origin.x) * divX;
		if (minx > maxx) {
			t = minx;
			minx = maxx;
			maxx = t;
		}

		float miny = (yMin - origin.y) * divY;
		float maxy = (yMax - origin.y) * divY;
		if (miny > maxy) {
			t = miny;
			miny = maxy;
			maxy = t;
		}

		float minz = (zMin - origin.z) * divZ;
		float maxz = (zMax - origin.z) * divZ;
		if (minz > maxz) {
			t = minz;
			minz = maxz;
			maxz = t;
		}

		final float min = max(max(minx, miny), minz);
		final float max = min(min(maxx, maxy), maxz);

		return max >= 0f && max >= min;
	}
}
