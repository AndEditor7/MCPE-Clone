package com.andedit.arcubit.world.gen;

import static com.andedit.arcubit.world.World.world;

import java.util.Random;

import com.andedit.arcubit.blocks.Block;
import com.andedit.arcubit.blocks.Blocks;
import com.andedit.arcubit.blocks.LogBlock;
import com.andedit.arcubit.utils.BlockPos;
import com.andedit.arcubit.utils.PlaceType;

public final class CommonFeatures {
	private CommonFeatures() {};
	
	private static final BlockPos blockPos = new BlockPos();
	private static final Random rand = new Random();
	
	public static void tree(boolean isOak, int x, int y, int z, PlaceType placeType) {
		final int len = 5+rand.nextInt(3)-(isOak?1:0);
		
		boolean notSafe = false;
		for (int i = 0; i < len; i++) {
			if (isSafe(x, y+i, z)) continue;
			notSafe = true;
			break;
		}
		
		if (notSafe) return;
		
		for (int xx = -2; xx < 3; xx++)
		for (int zz = -2; zz < 3; zz++) {
			setLeaves(isOak, x+xx, y+len-2, z+zz, placeType);
			setLeaves(isOak, x+xx, y+len-3, z+zz, placeType);
		}
		
		final int off = len - 3;
		for (int i = 0; i < 2; i++) {
			if (rand.nextFloat() < 0.5f) removeLeaves(x-2, y+i+off, z-2, placeType);
			if (rand.nextFloat() < 0.5f) removeLeaves(x-2, y+i+off, z+2, placeType);
			if (rand.nextFloat() < 0.5f) removeLeaves(x+2, y+i+off, z-2, placeType);
			if (rand.nextFloat() < 0.5f) removeLeaves(x+2, y+i+off, z+2, placeType);
			
			setLeaves(isOak, x-1, y+len+i-1, z, placeType);
			setLeaves(isOak, x, y+len+i-1, z-1, placeType);
			setLeaves(isOak, x+1, y+len+i-1, z, placeType);
			setLeaves(isOak, x, y+len+i-1, z+1, placeType);
		}
		
		setLeaves(isOak, x, y+len, z, placeType);
		
		for (int i = 0; i < len; i++) {
			world.setBlock(blockPos.set(x, y+i, z), Blocks.LOG, isOak?LogBlock.OAK:LogBlock.BIRCH, placeType);
		}
	}
	
	private static void setLeaves(boolean isOak, int x, int y, int z, PlaceType type) {
		if (world.getBlock(blockPos.set(x, y, z)).isAir()) 
			world.setBlock(blockPos, Blocks.LEAVES, isOak?LogBlock.OAK:LogBlock.BIRCH, type);
	}
	
	private static void removeLeaves(int x, int y, int z, PlaceType type) {
		if (world.getBlock(blockPos.set(x, y, z)) == Blocks.LEAVES) 
			world.setAir(blockPos, type, false);
	}
	
	private static boolean isSafe(int x, int y, int z) {
		final Block block = world.getBlock(blockPos.set(x, y, z));
		return block.isAir() || !block.getMaterial().hasCollision() || block == Blocks.LEAVES;
	}
}
