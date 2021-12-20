package com.andedit.arcubit.utils.maths;

import static com.badlogic.gdx.math.Interpolation.smooth;
import static com.badlogic.gdx.math.Interpolation.smoother;

import java.util.Random;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public final class FastNoise {
	
	private static final Vector2[] GRAD_OLD = {
		new Vector2(-1, -1), new Vector2(1, -1), new Vector2(-1, 1), new Vector2(1, 1),
		new Vector2(0, -1), new Vector2(-1, 0), new Vector2(0, 1), new Vector2(1, 0),
	};
	
	private static final float[] GRAD_2D = {
         0.130526192220052f,  0.99144486137381f,   0.38268343236509f,   0.923879532511287f,  0.608761429008721f,  0.793353340291235f,  0.793353340291235f,  0.608761429008721f,
         0.923879532511287f,  0.38268343236509f,   0.99144486137381f,   0.130526192220051f,  0.99144486137381f,  -0.130526192220051f,  0.923879532511287f, -0.38268343236509f,
         0.793353340291235f, -0.60876142900872f,   0.608761429008721f, -0.793353340291235f,  0.38268343236509f,  -0.923879532511287f,  0.130526192220052f, -0.99144486137381f,
        -0.130526192220052f, -0.99144486137381f,  -0.38268343236509f,  -0.923879532511287f, -0.608761429008721f, -0.793353340291235f, -0.793353340291235f, -0.608761429008721f,
        -0.923879532511287f, -0.38268343236509f,  -0.99144486137381f,  -0.130526192220052f, -0.99144486137381f,   0.130526192220051f, -0.923879532511287f,  0.38268343236509f,
        -0.793353340291235f,  0.608761429008721f, -0.608761429008721f,  0.793353340291235f, -0.38268343236509f,   0.923879532511287f, -0.130526192220052f,  0.99144486137381f,
         0.130526192220052f,  0.99144486137381f,   0.38268343236509f,   0.923879532511287f,  0.608761429008721f,  0.793353340291235f,  0.793353340291235f,  0.608761429008721f,
         0.923879532511287f,  0.38268343236509f,   0.99144486137381f,   0.130526192220051f,  0.99144486137381f,  -0.130526192220051f,  0.923879532511287f, -0.38268343236509f,
         0.793353340291235f, -0.60876142900872f,   0.608761429008721f, -0.793353340291235f,  0.38268343236509f,  -0.923879532511287f,  0.130526192220052f, -0.99144486137381f,
        -0.130526192220052f, -0.99144486137381f,  -0.38268343236509f,  -0.923879532511287f, -0.608761429008721f, -0.793353340291235f, -0.793353340291235f, -0.608761429008721f,
        -0.923879532511287f, -0.38268343236509f,  -0.99144486137381f,  -0.130526192220052f, -0.99144486137381f,   0.130526192220051f, -0.923879532511287f,  0.38268343236509f,
        -0.793353340291235f,  0.608761429008721f, -0.608761429008721f,  0.793353340291235f, -0.38268343236509f,   0.923879532511287f, -0.130526192220052f,  0.99144486137381f,
         0.130526192220052f,  0.99144486137381f,   0.38268343236509f,   0.923879532511287f,  0.608761429008721f,  0.793353340291235f,  0.793353340291235f,  0.608761429008721f,
         0.923879532511287f,  0.38268343236509f,   0.99144486137381f,   0.130526192220051f,  0.99144486137381f,  -0.130526192220051f,  0.923879532511287f, -0.38268343236509f,
         0.793353340291235f, -0.60876142900872f,   0.608761429008721f, -0.793353340291235f,  0.38268343236509f,  -0.923879532511287f,  0.130526192220052f, -0.99144486137381f,
        -0.130526192220052f, -0.99144486137381f,  -0.38268343236509f,  -0.923879532511287f, -0.608761429008721f, -0.793353340291235f, -0.793353340291235f, -0.608761429008721f,
        -0.923879532511287f, -0.38268343236509f,  -0.99144486137381f,  -0.130526192220052f, -0.99144486137381f,   0.130526192220051f, -0.923879532511287f,  0.38268343236509f,
        -0.793353340291235f,  0.608761429008721f, -0.608761429008721f,  0.793353340291235f, -0.38268343236509f,   0.923879532511287f, -0.130526192220052f,  0.99144486137381f,
         0.130526192220052f,  0.99144486137381f,   0.38268343236509f,   0.923879532511287f,  0.608761429008721f,  0.793353340291235f,  0.793353340291235f,  0.608761429008721f,
         0.923879532511287f,  0.38268343236509f,   0.99144486137381f,   0.130526192220051f,  0.99144486137381f,  -0.130526192220051f,  0.923879532511287f, -0.38268343236509f,
         0.793353340291235f, -0.60876142900872f,   0.608761429008721f, -0.793353340291235f,  0.38268343236509f,  -0.923879532511287f,  0.130526192220052f, -0.99144486137381f,
        -0.130526192220052f, -0.99144486137381f,  -0.38268343236509f,  -0.923879532511287f, -0.608761429008721f, -0.793353340291235f, -0.793353340291235f, -0.608761429008721f,
        -0.923879532511287f, -0.38268343236509f,  -0.99144486137381f,  -0.130526192220052f, -0.99144486137381f,   0.130526192220051f, -0.923879532511287f,  0.38268343236509f,
        -0.793353340291235f,  0.608761429008721f, -0.608761429008721f,  0.793353340291235f, -0.38268343236509f,   0.923879532511287f, -0.130526192220052f,  0.99144486137381f,
         0.130526192220052f,  0.99144486137381f,   0.38268343236509f,   0.923879532511287f,  0.608761429008721f,  0.793353340291235f,  0.793353340291235f,  0.608761429008721f,
         0.923879532511287f,  0.38268343236509f,   0.99144486137381f,   0.130526192220051f,  0.99144486137381f,  -0.130526192220051f,  0.923879532511287f, -0.38268343236509f,
         0.793353340291235f, -0.60876142900872f,   0.608761429008721f, -0.793353340291235f,  0.38268343236509f,  -0.923879532511287f,  0.130526192220052f, -0.99144486137381f,
        -0.130526192220052f, -0.99144486137381f,  -0.38268343236509f,  -0.923879532511287f, -0.608761429008721f, -0.793353340291235f, -0.793353340291235f, -0.608761429008721f,
        -0.923879532511287f, -0.38268343236509f,  -0.99144486137381f,  -0.130526192220052f, -0.99144486137381f,   0.130526192220051f, -0.923879532511287f,  0.38268343236509f,
        -0.793353340291235f,  0.608761429008721f, -0.608761429008721f,  0.793353340291235f, -0.38268343236509f,   0.923879532511287f, -0.130526192220052f,  0.99144486137381f,
         0.38268343236509f,   0.923879532511287f,  0.923879532511287f,  0.38268343236509f,   0.923879532511287f, -0.38268343236509f,   0.38268343236509f,  -0.923879532511287f,
        -0.38268343236509f,  -0.923879532511287f, -0.923879532511287f, -0.38268343236509f,  -0.923879532511287f,  0.38268343236509f,  -0.38268343236509f,   0.923879532511287f,
    };
	
	private static final float[] GRAD_3D = {
        0, 1, 1, 0,  0,-1, 1, 0,  0, 1,-1, 0,  0,-1,-1, 0,
        1, 0, 1, 0, -1, 0, 1, 0,  1, 0,-1, 0, -1, 0,-1, 0,
        1, 1, 0, 0, -1, 1, 0, 0,  1,-1, 0, 0, -1,-1, 0, 0,
        0, 1, 1, 0,  0,-1, 1, 0,  0, 1,-1, 0,  0,-1,-1, 0,
        1, 0, 1, 0, -1, 0, 1, 0,  1, 0,-1, 0, -1, 0,-1, 0,
        1, 1, 0, 0, -1, 1, 0, 0,  1,-1, 0, 0, -1,-1, 0, 0,
        0, 1, 1, 0,  0,-1, 1, 0,  0, 1,-1, 0,  0,-1,-1, 0,
        1, 0, 1, 0, -1, 0, 1, 0,  1, 0,-1, 0, -1, 0,-1, 0,
        1, 1, 0, 0, -1, 1, 0, 0,  1,-1, 0, 0, -1,-1, 0, 0,
        0, 1, 1, 0,  0,-1, 1, 0,  0, 1,-1, 0,  0,-1,-1, 0,
        1, 0, 1, 0, -1, 0, 1, 0,  1, 0,-1, 0, -1, 0,-1, 0,
        1, 1, 0, 0, -1, 1, 0, 0,  1,-1, 0, 0, -1,-1, 0, 0,
        0, 1, 1, 0,  0,-1, 1, 0,  0, 1,-1, 0,  0,-1,-1, 0,
        1, 0, 1, 0, -1, 0, 1, 0,  1, 0,-1, 0, -1, 0,-1, 0,
        1, 1, 0, 0, -1, 1, 0, 0,  1,-1, 0, 0, -1,-1, 0, 0,
        1, 1, 0, 0,  0,-1, 1, 0, -1, 1, 0, 0,  0,-1,-1, 0
    };
	
	private final static int X_PRIME = 501125321; // 1619
	private final static int Y_PRIME = 1136930381; // 31337
	private final static int Z_PRIME = 1720413743; // 6971
	
	public int seed;
	
	public FastNoise() {
		this(new Random().nextInt());
	}
	
	public FastNoise(int seed) {
		this.seed = seed;
	}
	
	private static int hash(int seed, int xPrimed, int yPrimed) {
        int hash = seed ^ xPrimed ^ yPrimed;
        return hash * 0x27d4eb2d;
    }

    private static int hash(int seed, int xPrimed, int yPrimed, int zPrimed) {
        int hash = seed ^ xPrimed ^ yPrimed ^ zPrimed;
        return hash * 0x27d4eb2d;
    }
	
	private static float gradCoord1D(int hash, int x, float xd) {
		hash ^= Y_PRIME * x;

		hash = hash * hash * hash * 60493;
		hash = (hash >> 13) ^ hash;

		return xd * (float) ((hash & 3) - 1);
	}
	
	private static float gradCoord2D(int hash, int x, int y, float xd, float yd) {
		hash = hash(hash, x, y);
        hash ^= hash >> 15;
        hash &= 127 << 1;

        float xg = GRAD_2D[hash];
        float yg = GRAD_2D[hash | 1];

        return xd * xg + yd * yg;
	}
	
	private static float gradCoordOld(int hash, int x, int y, float xd, float yd) {
		hash ^= X_PRIME * x;
		hash ^= Y_PRIME * y;

		hash = hash * hash * hash * 60493;
		hash = (hash >> 13) ^ hash;

		Vector2 g = GRAD_OLD[hash & 7];

		return xd * g.x + yd * g.y;
	}
	
	private static float gradCoord3D(int hash, int x, int y, int z, float xd, float yd, float zd) {
		hash = hash(hash, x, y, z);
        hash ^= hash >> 15;
        hash &= 63 << 2;

        float xg = GRAD_3D[hash];
        float yg = GRAD_3D[hash | 1];
        float zg = GRAD_3D[hash | 2];

        return xd * xg + yd * yg + zd * zg;
	}
	
	public float getPerlin(float x) {
		return FastNoise.getPerlin(seed, x);
	}

	public float getPerlin(float x, float y) {
		return FastNoise.getPerlin(seed, x, y);
	}
	
	public float getPerlinOld(float x, float y) {
		return FastNoise.getPerlinOld(seed, x, y);
	}
	
	public float getPerlin(float x, float y, float z) {
		return FastNoise.getPerlin(seed, x, y, z);
	}
	
	public static float getPerlin(int seed, float x) 
	{
		final int x0 = MathUtils.floor(x);
		final float xd0 = x - x0;
		return MathUtils.lerp(gradCoord1D(seed, x0, xd0), gradCoord1D(seed, x0 + 1, xd0 - 1f), smoother.apply(xd0));
	}
	
	public static float getPerlin(int seed, float x, float y) 
	{
		return getPerlinBase(seed, x, y, false) * 1.4247691104677813f;
	}
	
	public static float getPerlinOld(int seed, float x, float y) 
	{
		return getPerlinBase(seed, x, y, true);
	}
	
	private static float getPerlinBase(int seed, float x, float y, boolean isOld) 
	{
		int x0 = MathUtils.floor(x);
		int y0 = MathUtils.floor(y);

		final float xd0 = x - x0;
		final float yd0 = y - y0;
		final float xd1 = xd0 - 1f;
		final float yd1 = yd0 - 1f;
		
		final float xs, ys;
		xs = smoother.apply(xd0);
		ys = smoother.apply(yd0);
		
		x0 *= X_PRIME;
        y0 *= Y_PRIME;
        final int x1 = x0 + X_PRIME;
		final int y1 = y0 + Y_PRIME;

		final float xf0, xf1;
		if (isOld) {
			xf0 = MathUtils.lerp(gradCoordOld(seed, x0, y0, xd0, yd0), gradCoordOld(seed, x1, y0, xd1, yd0), xs);
			xf1 = MathUtils.lerp(gradCoordOld(seed, x0, y1, xd0, yd1), gradCoordOld(seed, x1, y1, xd1, yd1), xs);
		} else {
			xf0 = MathUtils.lerp(gradCoord2D(seed, x0, y0, xd0, yd0), gradCoord2D(seed, x1, y0, xd1, yd0), xs);
			xf1 = MathUtils.lerp(gradCoord2D(seed, x0, y1, xd0, yd1), gradCoord2D(seed, x1, y1, xd1, yd1), xs);
		}

		return MathUtils.lerp(xf0, xf1, ys);
	}
	
	public static float getPerlin(int seed, float x, float y, float z) {
		int x0 = MathUtils.floor(x);
		int y0 = MathUtils.floor(y);
		int z0 = MathUtils.floor(z);
		
		final float xd0 = x - x0;
		final float yd0 = y - y0;
		final float zd0 = z - z0;
		final float xd1 = xd0 - 1f;
		final float yd1 = yd0 - 1f;
		final float zd1 = zd0 - 1f;

		final float xs, ys, zs;
		xs = smooth.apply(xd0);
		ys = smooth.apply(yd0);
		zs = smooth.apply(zd0);
		
		x0 *= X_PRIME;
        y0 *= Y_PRIME;
        z0 *= Z_PRIME;
		final int x1 = x0 + X_PRIME;
		final int y1 = y0 + Y_PRIME;
		final int z1 = z0 + Z_PRIME;

		final float xf00 = MathUtils.lerp(gradCoord3D(seed, x0, y0, z0, xd0, yd0, zd0), gradCoord3D(seed, x1, y0, z0, xd1, yd0, zd0), xs);
		final float xf10 = MathUtils.lerp(gradCoord3D(seed, x0, y1, z0, xd0, yd1, zd0), gradCoord3D(seed, x1, y1, z0, xd1, yd1, zd0), xs);
		final float xf01 = MathUtils.lerp(gradCoord3D(seed, x0, y0, z1, xd0, yd0, zd1), gradCoord3D(seed, x1, y0, z1, xd1, yd0, zd1), xs);
		final float xf11 = MathUtils.lerp(gradCoord3D(seed, x0, y1, z1, xd0, yd1, zd1), gradCoord3D(seed, x1, y1, z1, xd1, yd1, zd1), xs);

		final float yf0 = MathUtils.lerp(xf00, xf10, ys);
		final float yf1 = MathUtils.lerp(xf01, xf11, ys);

		return MathUtils.lerp(yf0, yf1, zs) * 0.964921414852142333984375f;
	}
}
