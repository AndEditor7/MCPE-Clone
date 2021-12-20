package com.andedit.arcubit.utils.maths;

import java.util.Random;

public class FastNoiseOctaves {
	private final int[] seeds;
	private float gain = 0.5f;
	
	public FastNoiseOctaves(int count) {
		this(count, new Random());
	}
	
	public FastNoiseOctaves(int count, long seed) {
		this(count, new Random(seed));
	}

	public FastNoiseOctaves(int count, Random rng) {
		seeds = new int[count];

		for (int i = 0; i < count; i++) {
			seeds[i] = rng.nextInt();
		}
	}
	
	public FastNoiseOctaves setGain(float gain) {
		this.gain = gain;
		return this;
	}
	
	public float getPerlin(float x) {
		float result = FastNoise.getPerlin(seeds[0], x), amp = 1;

		for (int i = 1; i < seeds.length; i++) {
			x *= 2f;
			
			amp *= gain;
			result += FastNoise.getPerlin(seeds[i], x) * amp;
		}

		return result;
	}

	public float getPerlin(float x, float y) {
		float result = FastNoise.getPerlin(seeds[0], x, y), amp = 1;

		for (int i = 1; i < seeds.length; i++) {
			x *= 2f; y *= 2f;
			
			amp *= gain;
			result += FastNoise.getPerlin(seeds[i], x, y) * amp;
		}

		return result;
	}
	
	public float getPerlinOld(float x, float y) {
		float result = FastNoise.getPerlinOld(seeds[0], x, y), amp = 1;

		for (int i = 1; i < seeds.length; i++) {
			x *= 2f; y *= 2f;
			
			amp *= gain;
			result += FastNoise.getPerlinOld(seeds[i], x, y) * amp;
		}

		return result;
	}

	public float getPerlin(float x, float y, float z) {
		float result = FastNoise.getPerlin(seeds[0], x, y, z), amp = 1;

		for (int i = 1; i < seeds.length; i++) {
			x *= 2f; y *= 2f; z *= 2f;
			
			amp *= gain;
			result += FastNoise.getPerlin(seeds[i], x, y, z) * amp;
		}

		return result;
	}
}
