package com.andedit.arcubit.world;

import static com.andedit.arcubit.world.World.world;

import com.andedit.arcubit.blocks.Block;
import com.andedit.arcubit.blocks.WaterBlock;
import com.andedit.arcubit.blocks.LiquidBlock;
import com.andedit.arcubit.blocks.data.LiquidComp;
import com.andedit.arcubit.blocks.utils.UpdateState;
import com.andedit.arcubit.utils.BlockPos;
import com.andedit.arcubit.utils.Facing;
import com.andedit.arcubit.utils.PlaceType;
import com.badlogic.gdx.utils.Array;

public final class LiquidFall {
	private static final BlockPos offset = new BlockPos();
	private static final boolean[] bools = new boolean[4];
	private static final byte[] dst = new byte[4];
	
	public static void update(Array<BlockPos> liquidExe, Array<BlockPos> liquidQue, LiquidBlock type) {
		while (liquidExe.notEmpty()) {
			final BlockPos pos = liquidExe.pop();
			Block block = world.getBlock(pos);
			if (block != type) continue;
			
			final int maxFlow = type.getMaxFlow();
			final LiquidComp comp = type.getComp();
			final int level = comp.getLevel(pos);
			final boolean isSource = comp.isSource(pos);
			
			// Check none water source block de-flowable.
			if (!isSource) {
				boolean isSafe = false;
				
				// check the water above if
				offset.set(pos).add(0, 1, 0);
				block = world.getBlock(offset);
				if (block == type) {
					isSafe = true;
				}
				
				if (!isSafe) // 
				for (int i = 0; i < 4; i++) {
					final BlockPos offset = pos.offset(Facing.get(i+2));
					block = world.getBlock(offset);
					if (block == type && comp.getLevel(offset) > level) {
						isSafe = true;
					}
				}
				
				if (!isSafe) {
					if (level == 0) {
						world.setAir(pos, PlaceType.SET, false);
					} else {
						comp.setLevel(pos, level-1);
						world.dirty(pos.x, pos.y, pos.z);
					}
					type.updateNearByBlocks(pos, UpdateState.ON_CHANGE);
					liquidQue.add(pos.clone());
					continue;
				}
			}
			
			// Turn water into water source block, AKA infinite water!
			if (level == maxFlow-1 && type instanceof WaterBlock) {
				int num = 0;
				
				for (int i = 0; i < 4; i++) {
					final BlockPos offset = pos.offset(Facing.get(i+2));
					block = world.getBlock(offset);
					if (block == type && comp.isSource(offset)) {
						num++;
					}
				}
				
				// Check there's two water source block requirement.
				if (num >= 2) {
					comp.setLevel(pos, maxFlow);
					comp.setSource(pos, true);
					type.updateNearByBlocks(pos, UpdateState.ON_CHANGE);
					world.dirty(pos.x, pos.y, pos.z);	
					liquidQue.add(pos.clone());
					continue;
				}
			}
			
			offset.set(pos).add(0, -1, 0);
			block = world.getBlock(offset); // Check below if water can be flow to.
			if (block.getMaterial().destoryByWater() || (block == type && comp.getLevel(offset) != maxFlow)) {
				world.setBlock(offset, type, 0, PlaceType.SET);
				comp.setLevel(offset, maxFlow);
				liquidQue.add(offset.clone());
			} else if (level != 0 && !(block == type && !isSource)) {
				
				// Reset flow direction.
				for (int i = 0; i < bools.length; i++) {
					bools[i] = false;
				}
				
				// max flow distant.
				int dist = maxFlow+1; 
				
				dist = check(pos, Facing.EAST, level, dist, type);
				dist = check(pos, Facing.WEST, level, dist, type);
				dist = check(pos, Facing.SOUTH, level, dist, type);
				dist = check(pos, Facing.NORTH, level, dist, type);
				
				// has
				boolean hasFound = false;
				for (boolean bool : bools) {
					if (bool) {
						hasFound = true;
						break;
					}
				}
				
				dist = maxFlow+2;
				for (int i = 0; i < bools.length; i++) {
					if (bools[i] && dst[i] < dist) {
						dist = dst[i];
					}
				}
				
				// start flowing.
				for (int i = 0; i < bools.length; i++) {
					final Facing face = Facing.get(i+2);
					if ((bools[i] && dst[i] <= dist) || !hasFound) {
						BlockPos offset = pos.offset(face);
						block = world.getBlock(offset);
						if (block.getMaterial().destoryByWater() || (block == type && comp.getLevel(offset)+1 < level)) {
							world.setBlock(offset, type, 0, PlaceType.SET);
							comp.setLevel(offset, level-1);
							liquidQue.add(offset.clone());
						}
					}
				}
			}
		}
	}
	
	private static int check(BlockPos pos, Facing face, int level, int dist, LiquidBlock type) {
		Block block = world.getBlock(pos.offset(face.invert()));
		if (level == type.getMaxFlow() || block.getMaterial().destoryByWater() || block == type)
		for (int i = 1; i < level; i++) {
			BlockPos offset = pos.offset(face, i);
			block = world.getBlock(offset);
			if (block.getMaterial().destoryByWater() || block == type) {
				if (block == type && type.getComp().getLevel(offset) == type.getMaxFlow()) {
					break;
				}
				block = world.getBlock(offset.offset(Facing.DOWN));
				if (block.getMaterial().destoryByWater() || block == type) {
					if (i <= dist) {
						bools[face.ordinal()-2] = true;
					} if (i < dist) {
						dist = i;
					}
					dst[face.ordinal()-2] = (byte)dist;
				}
			} else {
				break;
			}
		}
		
		return dist;
	}
}
