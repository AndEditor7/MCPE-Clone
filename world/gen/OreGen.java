package com.andedit.arcubit.world.gen;

import static com.andedit.arcubit.world.World.world;

import com.andedit.arcubit.blocks.Blocks;
import com.andedit.arcubit.blocks.OreBlock;
import com.andedit.arcubit.world.World;
import com.badlogic.gdx.math.RandomXS128;

public final class OreGen {
	
	public static void gen(RandomXS128 rand) {
		for (int xIndex = 0; xIndex < World.SIZE>>4; xIndex++)
		for (int zIndex = 0; zIndex < World.SIZE>>4; zIndex++) {
			int x, y, z;
			
			// Bedrock
			for (y = 0; y < 4; y++) {
				
				final float chance;
				switch (y) {
				case 0: chance = 2.0f; break;
				case 1: chance = 0.75f; break;
				case 2: chance = 0.5f; break;
				case 3: chance = 0.25f; break;
				default: chance = 0;
				}
				
				for (x = 0; x < 16; x++)
				for (z = 0; z < 16; z++) {
					if (world.getBlock(x+(xIndex<<4), y, z+(zIndex<<4)) == Blocks.STONE && rand.nextFloat() < chance) {
						world.setBlock(x+(xIndex<<4), y, z+(zIndex<<4), Blocks.BEDROCK);
					}
				}
			}
			
			// Diamond ores
			x = rand.nextInt(15);
			y = rand.nextInt(12)+3;
			z = rand.nextInt(15);
			
			for (int xx = 0; xx < 2; xx++) 
			for (int yy = 0; yy < 2; yy++) 
			for (int zz = 0; zz < 2; zz++) {
				if (world.getBlock(xx+x+(xIndex<<4), yy+y, zz+z+(zIndex<<4)) == Blocks.STONE && rand.nextFloat() < 0.8f) {
					world.setBlock(xx+x+(xIndex<<4), yy+y, zz+z+(zIndex<<4), Blocks.ORE, OreBlock.DIAMOND);
				}
			}
			
			// Gold ores
			for (int i = 0; i < 2; i++) {
				for (int yIndex = 0; yIndex < 2; yIndex++) {
					x = rand.nextInt(15);
					y = i == 0 ? rand.nextInt(12)+3 : rand.nextInt(15);
					z = rand.nextInt(15);
					for (int xx = 0; xx < 2; xx++) 
					for (int yy = 0; yy < 2; yy++) 
					for (int zz = 0; zz < 2; zz++) {
						if (world.getBlock(xx+x+(xIndex<<4), yy+y+(yIndex<<4), zz+z+(zIndex<<4)) == Blocks.STONE && rand.nextFloat() < 0.8f) {
							world.setBlock(xx+x+(xIndex<<4), yy+y+(yIndex<<4), zz+z+(zIndex<<4), Blocks.ORE, OreBlock.GOLD);
						}
					}
				}
			}
			
			// Iron ores
			for (int i = 0; i < 3; i++) {
				for (int yIndex = 0; yIndex < 4; yIndex++) {
					x = rand.nextInt(15);
					y = rand.nextInt(15);
					z = rand.nextInt(15);
					for (int xx = 0; xx < 2; xx++) 
					for (int yy = 0; yy < 2; yy++) 
					for (int zz = 0; zz < 2; zz++) {
						if (world.getBlock(xx+x+(xIndex<<4), yy+y+(yIndex<<4), zz+z+(zIndex<<4)) == Blocks.STONE && rand.nextFloat() < 0.8f) {
							world.setBlock(xx+x+(xIndex<<4), yy+y+(yIndex<<4), zz+z+(zIndex<<4), Blocks.ORE, OreBlock.IRON);
						}
					}
				}
			}
			
			// Coal ores
			for (int i = 0; i < 3; i++) {
				for (int yIndex = 1; yIndex < 7; yIndex++) {
					x = rand.nextInt(14);
					y = rand.nextInt(14);
					z = rand.nextInt(14);
					for (int xx = 0; xx < 3; xx++) 
					for (int yy = 0; yy < 3; yy++) 
					for (int zz = 0; zz < 3; zz++) {
						if (world.getBlock(xx+x+(xIndex<<4), yy+y+(yIndex<<4), zz+z+(zIndex<<4)) == Blocks.STONE && rand.nextFloat() < 0.8f) {
							world.setBlock(xx+x+(xIndex<<4), yy+y+(yIndex<<4), zz+z+(zIndex<<4), Blocks.ORE, OreBlock.COAL);
						}
					}
				}
			}
			
		}
	}
}
