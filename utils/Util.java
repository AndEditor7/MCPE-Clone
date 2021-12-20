package com.andedit.arcubit.utils;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import com.andedit.arcubit.graphics.quad.QuadIndexBuffer;
import com.andedit.arcubit.graphics.vertex.VoxelTerrain;
import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.TimeUtils;
import com.esotericsoftware.kryo.unsafe.UnsafeUtil;

public final class Util {
	private Util() {
	}
	
	/** 1,572,864 bytes of data, or 1.57MB. Main thread only! */
	public static final ByteBuffer BUFFER = BufferUtils.newByteBuffer(QuadIndexBuffer.maxVertex*VoxelTerrain.byteSize);
	public static final FloatBuffer FLOATBUFF = BUFFER.asFloatBuffer();

	/** A temporally int buffer. */
	public static final IntBuffer intBuf = BufferUtils.newIntBuffer(1);

	public static int createANDbits(final int bitSize) {
		return -1 >>> 32 - bitSize;
	}
	
	public static boolean isDesktop() {
		return Gdx.app.getType() == ApplicationType.Desktop;
	}
	
	public static int getW() {
		return Gdx.graphics.getWidth();
	}
	
	public static int getH() {
		return Gdx.graphics.getHeight();
	}
	
	public static boolean isCatched() {
		return Gdx.input.isCursorCatched();
	}

	public static boolean isGL30() {
		return Gdx.gl30 != null;
	}
	
	public static float delta() {
		return Gdx.graphics.getDeltaTime();
	}
	
	public static void glClear() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
	}
	
	public static void disposes(Disposable... list) {
		for (Disposable dis : list) {
			if (dis != null) dis.dispose();
		}
	}
	
	public static int mod(int a, int b) {
		a %= b;
		return a < 0 ? a + b : a;
	}
	
	public static float mod(float a, float b) {
		a %= b;
		return a < 0f ? a + b : a;
	}
	
	public static float clampVal(float value, float min, float max) {
		if (value < min) {
			return value - min;
		}
		if (value > max) {
			return value - max;
		}
		return 0.0f;
	}
	
	public static float lerpAngle (float fromDegrees, float toDegrees, float progress, float clamp) {
		final float delta = ((toDegrees - fromDegrees + 360f + 180f) % 360f) - 180f;
		return (fromDegrees + MathUtils.clamp(delta, -clamp, clamp) * progress + 360f) % 360f;
	}
	
	static public float lerp (float fromValue, float toValue, float progress, float clamp) {
		final float delta = (toValue - fromValue) * progress;
		return fromValue + MathUtils.clamp(delta, -clamp, clamp);
	}
	
	private static final Vector3 VEC = new Vector3();
	public static void rotate(BoundingBox box, Matrix4 matrix) {
		final Vector3 min = box.min, max = box.max;
		final float x0 = min.x, y0 = min.y, z0 = min.z, x1 = max.x, y1 = max.y, z1 = max.z;
		final float a = 0.5f;
		
		box.inf();
		synchronized (VEC) {
			box.ext(VEC.set(x0-a, y0-a, z0-a).mul(matrix));
			box.ext(VEC.set(x1-a, y1-a, z1-a).mul(matrix));
		}
		
		min.add(a);
		max.add(a);
	}
	
	public static void rotateX(final Matrix4 mat, float rad, float scl) {
		final float sin = MathUtils.sin(rad) * scl;
		final float cos = MathUtils.cos(rad) * scl;
		mat.val[Matrix4.M11] = cos;
		mat.val[Matrix4.M12] = -sin;
		mat.val[Matrix4.M21] = sin;
		mat.val[Matrix4.M22] = cos;
	}
	
	public static void rotateY(final Matrix4 mat, float rad, float scl) {
		final float sin = MathUtils.sin(rad) * scl;
		final float cos = MathUtils.cos(rad) * scl;
		mat.val[Matrix4.M00] = cos;
		mat.val[Matrix4.M02] = sin;
		mat.val[Matrix4.M20] = -sin;
		mat.val[Matrix4.M22] = cos;
	}
	
	public static void rotateZ(final Matrix4 mat, float rad, float scl) {
		final float sin = MathUtils.sin(rad) * scl;
		final float cos = MathUtils.cos(rad) * scl;
		mat.val[Matrix4.M00] = cos;
		mat.val[Matrix4.M01] = -sin;
		mat.val[Matrix4.M10] = sin;
		mat.val[Matrix4.M11] = cos;
	}

	public static long millis;
	public static void startMillis() {
		millis = TimeUtils.millis();
	}

	public static void endMillis() {
		millis = TimeUtils.millis()-millis;
	}

	public static float sqrt(double a) {
		return (float)Math.sqrt(a);
	}
	
	public static boolean hasUnsafe() {
		return UnsafeUtil.unsafe != null;
	}
}
