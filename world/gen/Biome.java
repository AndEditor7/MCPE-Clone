package com.andedit.arcubit.world.gen;

import java.util.Random;

import com.andedit.arcubit.utils.maths.FastNoise;

class Biome {
	static final float OFFSET = 150;
	static final float SIZE = 0.4f;
	
	private final FastNoise base, warp;
	
	Biome() {
		this(new Random());
	}
	
	Biome(Random rand) {
		base = new FastNoise(rand.nextInt());
		warp = new FastNoise(rand.nextInt());
	}
	
	float getNoise(int x, int z) {
		float num = base.getPerlinOld(x/OFFSET, z/OFFSET);
		num += warp.getPerlin(x/24f, z/24f) * 0.08f;
		return num;
	}
}
