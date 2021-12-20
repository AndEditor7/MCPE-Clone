package com.andedit.arcubit.world.gen;

import static com.andedit.arcubit.world.World.world;

import com.andedit.arcubit.blocks.Block;
import com.andedit.arcubit.blocks.Blocks;
import com.andedit.arcubit.utils.maths.FastNoiseOctaves;
import com.andedit.arcubit.world.World;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.RandomXS128;

public final class Land {
	
	private static final Surface NORMAL = new Surface() {
		public Block block(int stage) {
			switch(stage) {
				case 0: return Blocks.GRASS;
				case 1:
				case 2:
				case 3: return Blocks.DIRT;
				default: return Blocks.STONE;
			}
		}
	};
	
	private static final Surface SAND = new Surface() {
		public Block block(int stage) {
			switch(stage) {
				case 0:
				case 1:
				case 2:
				case 3: return Blocks.SAND;
				case 4:
				case 5:
				case 6: return Blocks.SANDSTONE;
				default: return Blocks.STONE;
			}
		}
	};
	
	public static void gen(Biome biomes, RandomXS128 rand) {
		final FastNoiseOctaves noise = new FastNoiseOctaves(2);
		for (int x = 0; x < World.SIZE; x++)
		for (int z = 0; z < World.SIZE; z++) {
			
			int stage = 0;
			boolean isFinal = false;
			Surface surface = null;
			if (biomes.getNoise(x, z) < -Biome.SIZE) {
				isFinal = true;
				surface = SAND;
			}
			
			int level = MathUtils.roundPositive(64f+(noise.getPerlin(x/80f, z/80f)*4f));
			
			for (int y = World.HEIGHT-1; y >= 0; y--) {
				if (world.getBlock(x, y, z) == Blocks.STONE) {
					if (surface == null) {
						surface = y < level ? SAND : NORMAL;
					}
					world.setBlock(x, y, z, surface.block(stage++));
				} else {
					stage = 0;
					if (!isFinal) surface = null;
				}
			}
		}
	}
	
	
}
