package com.andedit.arcubit.world.gen;

import static com.andedit.arcubit.world.World.world;

import java.util.Random;

import com.andedit.arcubit.blocks.Block;
import com.andedit.arcubit.blocks.Blocks;
import com.andedit.arcubit.blocks.LavaBlock;
import com.andedit.arcubit.blocks.WaterBlock;
import com.andedit.arcubit.blocks.materials.Material;
import com.andedit.arcubit.utils.BlockPos;
import com.andedit.arcubit.utils.PlaceType;
import com.andedit.arcubit.utils.maths.FastNoise;
import com.andedit.arcubit.utils.maths.FastNoiseOctaves;
import com.andedit.arcubit.world.World;
import com.andedit.arcubit.world.lights.LightHandle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.utils.Array;

public class DefaultGen {
	
	private static final Random seedRand = new Random();
	
	private final RandomXS128 rand;
	
	private final FastNoiseOctaves noise2, noise3;
	private final Biome biomes;
	
	public DefaultGen() {
		this(seedRand.nextLong());
	}
	
	public DefaultGen(long seed) {
		rand = new RandomXS128(seed);
		noise2 = new FastNoiseOctaves(4, rand).setGain(0.5f);
		noise3 = new FastNoiseOctaves(4, rand).setGain(0.6f); // 0.45f
		biomes = new Biome(rand);
	}
	
	private float getNoise2(float x, float z) {
		float result, temp;
		result = (noise2.getPerlin(x/130f, z/130f)*15f)+69f; // +70f
		/*
		temp = FastNoise.getNoise(seed1, x*0.03f, z*0.03f) + (FastNoise.getNoise(seed2, x*0.015f, z*0.015f)*0.5f);
		if (temp > 0.2f) {
			temp = smoother.apply(temp-0.2f)*200f;
			temp = temp > 30f ? MathUtils.lerp(temp, 30f, 0.98f) : temp;
			result += temp;
		} */
		return result;
	}
	
	private float getNoise3(int x, int y, int z) {
		return ((noise3.getPerlin(x/110f, y/130f, z/110f)+0.0f)*100f)+45f; // +50f
	}
	
	public void generate() {
		final LightHandle handle = new LightHandle(false);
		for (int x = 0; x < World.SIZE; x++) {
			for (int z = 0; z < World.SIZE; z++) {
				float noize2 = getNoise2(x, z);
				for (int y = World.HEIGHT-1; y >= 0; y--) {
					world.setData(x, y, z, 0);
					float height = noize2 < y ? getNoise3(x, y, z) : noize2;
					if (height > 115f) height = MathUtils.lerp(height, 115f, 0.6f);
					if (height > y) {
						world.setData(x, y, z, Blocks.STONE.id);
					} else {
						if (y < World.SEA_LEVEL) {
							WaterBlock.intsBlock(x, y, z);
						}
					}
				}
			}
		}
		
		Land.gen(biomes, rand);
		OreGen.gen(rand);
		
		// Caves
		final Array<Worm> worms = new Array<>();
		for (int x = 0; x < World.SIZE/16; x++)
		for (int y = 0; y < World.HEIGHT/16; y++)
		for (int z = 0; z < World.SIZE/16; z++) {
			if (rand.nextInt(8) == 0) {
				int xx = (x << 4) + 8, yy = (y << 4) + 8, zz = (z << 4) + 8;
				if (world.getBlock(xx, yy, zz) != Blocks.AIR) {
					worms.add(new Worm(rand, xx, yy, zz));
				}
			}
		}
		Worm.updateAll(worms);
		
		FastNoise patch = new FastNoise(rand.nextInt());
		for (int x = 0; x < World.SIZE; x++)
		for (int z = 0; z < World.SIZE; z++) {
			for (int y = World.HEIGHT-1; y >= 0; y--) {
				if (world.getBlock(x, y, z) == Blocks.STONE) {
					final float noise = patch.getPerlin(x/14f, y/14f, z/14f);
					final float size = 0.5f;
					if (noise > size) {
						world.setData(x, y, z, Blocks.DIRT.id);
					}
					if (noise < -size) {
						world.setData(x, y, z, Blocks.GRAVEL.id);
					}
				}
			}
		}
		
		for (int i = 0; i < 200 * World.AMOUNT; i++) {
			final int x, z;
			x = rand.nextInt(World.SIZE);
			z = rand.nextInt(World.SIZE);
			Block block = null;
			int y = 0;
			for (int y1 = World.HEIGHT-1; y1 >= 0; y1--) {
				block = world.getBlock(x, y1, z);
				if (block == Blocks.GRASS || block == Blocks.SAND) {
					y = y1+1;
					break;
				}
			}
			if (y > 66) {
				if (block == Blocks.SAND) {
					int len = rand.nextInt(3)+1;
					for (int j = 0; j < len; j++) {
						world.setBlock(x, y+j, z, Blocks.CACTUS);
					}
				} else {
					CommonFeatures.tree(rand.nextFloat() > 0.2f, x, y, z, PlaceType.NONE);
				}
			}
		}
		
		final int lavaLight = Blocks.LAVA.getLightLevel();
		for (int x = 0; x < World.SIZE; x++)
		for (int z = 0; z < World.SIZE; z++) {
			for (int y = 0; y < 10; y++) {
				if (world.getBlock(x, y, z) == Blocks.AIR) {
					LavaBlock.intsBlock(x, y, z);
					world.setSrcLight(x, y, z, lavaLight);
				}
				if (world.getBlock(x, 9, z) == Blocks.LAVA) {
					handle.newSrclightAt(x, 9, z, lavaLight);
				}
			}
		}
		
		for (int i = 0; i < 1200 * World.AMOUNT; i++) {
			int x = rand.nextInt(World.SIZE);
			int y = rand.nextInt(World.HEIGHT);
			int z = rand.nextInt(World.SIZE);
			
			if (world.getBlock(x, y, z) == Blocks.STONE) {
				if (world.getBlock(x+1, y, z) == Blocks.AIR ||
					world.getBlock(x-1, y, z) == Blocks.AIR ||
					world.getBlock(x, y, z+1) == Blocks.AIR ||
					world.getBlock(x, y, z-1) == Blocks.AIR) {
					WaterBlock.intsBlock(x, y, z);
					world.addWaterQue(new BlockPos(x, y, z));
				}
			}
		}
		
		for (int i = 0; i < 1000 * World.AMOUNT; i++) {
			int x = rand.nextInt(World.SIZE);
			int y = rand.nextInt(World.HEIGHT);
			int z = rand.nextInt(World.SIZE);
			
			if (world.getBlock(x, y, z) == Blocks.STONE) {
				if (world.getBlock(x+1, y, z) == Blocks.AIR ||
					world.getBlock(x-1, y, z) == Blocks.AIR ||
					world.getBlock(x, y, z+1) == Blocks.AIR ||
					world.getBlock(x, y, z-1) == Blocks.AIR) {
					LavaBlock.intsBlock(x, y, z);
					world.addLavaQue(new BlockPos(x, y, z));
					handle.newSrclightAt(x, y, z, lavaLight);
				}
			}
		}
		
		
		for (int i = 0; i < 50 * World.AMOUNT; i++) {
			final int x, z;
			x = rand.nextInt(World.SIZE);
			z = rand.nextInt(World.SIZE);
			
			for (int xx = -6; xx < 7; xx++) 
			for (int zz = -6; zz < 7; zz++) {
				if (rand.nextFloat() > 0.03f) continue;
				int y = 0;
				for (int y1 = World.HEIGHT-1; y1 >= 0; y1--) {
					if (world.getBlock(x+xx, y1, z+zz) == Blocks.GRASS) {
						y = y1+1;
						break;
					}
				}
				if (y > 66 && world.getBlock(x+xx, y, z+zz) == Blocks.AIR) {
					world.setBlock(x+xx, y, z+zz, Blocks.FLOWER);
				}
			}
		}
		
		for (int i = 0; i < 400 * World.AMOUNT; i++) {
			final int x, z;
			x = rand.nextInt(World.SIZE);
			z = rand.nextInt(World.SIZE);
			
			for (int xx = -5; xx < 6; xx++) 
			for (int zz = -5; zz < 6; zz++) {
				if (rand.nextFloat() > 0.03f) continue;
				int y = 0;
				for (int y1 = World.HEIGHT-1; y1 >= 0; y1--) {
					if (world.getBlock(x+xx, y1, z+zz) == Blocks.GRASS) {
						y = y1+1;
						break;
					}
				}
				if (y > 66 && world.getBlock(x+xx, y, z+zz) == Blocks.AIR) {
					world.setBlock(x+xx, y, z+zz, Blocks.TALLGRASS);
				}
			}
		}
		
		for (int x = 0; x < World.SIZE; x++)
		for (int z = 0; z < World.SIZE; z++) {
			if (biomes.getNoise(x, z) < Biome.SIZE) continue;
			
			for (int y = World.HEIGHT-1; y >= 0; y--) {
				Block block = world.getBlock(x, y, z);
				if (block.getMaterial().isFullCube()) {
					if (world.getBlock(x, y+1, z) == Blocks.AIR) world.setBlock(x, y+1, z, Blocks.SNOW_LAYER);
					break;
				} else if (block == Blocks.WATER) {
					world.setBlock(x, y, z, Blocks.ICE);
					break;
				}
			}
		}
		
		

		// lighting
		lighting(handle);
	}
	
	private void lighting(LightHandle handle) {
		
		for (int x = 0; x < World.SIZE; x++)
		for (int z = 0; z < World.SIZE; z++) {
			world.shadowMap[x][z] = 0;
			for (int y = World.HEIGHT-1; y >= 0; y--) {
				if (Blocks.get(world.getData(x, y, z)).getMaterial().canBlockSunRay()) {
					world.shadowMap[x][z] = (short)y;
					break;
				}
				world.setSunLight(x, y, z, 15);
			}
		}
		
		for (int x = 0; x < World.SIZE; x++)
		for (int z = 0; z < World.SIZE; z++)
		for (int y = world.shadowMap[x][z]; y >= 0; y--) {
			final Material material = Blocks.get(world.getData(x, y, z)).getMaterial();
			
			if (material.canBlockSunRay() && material.canBlockLights()) {
				continue;
			}
			
			if (world.getShadow(x+1, z) < y || world.getShadow(x, z+1) < y ||
				world.getShadow(x-1, z) < y || world.getShadow(x, z-1) < y || world.getShadow(x, z) < y+1) {
	
				handle.newSunlightAt(x, y, z, 14);
			}
			
			continue;
		}
		
		handle.calculateLights();
	}
}
