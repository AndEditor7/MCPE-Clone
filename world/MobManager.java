package com.andedit.arcubit.world;

import static com.andedit.arcubit.world.World.world;
import static com.badlogic.gdx.math.MathUtils.random;
import static com.andedit.arcubit.options.Options.peaceful;

import com.andedit.arcubit.blocks.Block;
import com.andedit.arcubit.blocks.Blocks;
import com.andedit.arcubit.blocks.utils.BlockType;
import com.andedit.arcubit.blocks.utils.BlockUtils;
import com.andedit.arcubit.entity.Entity;
import com.andedit.arcubit.entity.EntityMob;
import com.andedit.arcubit.entity.Pig;
import com.andedit.arcubit.entity.Player;
import com.andedit.arcubit.entity.Zombie;
import com.andedit.arcubit.utils.BlockPos;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.IntArray;

public class MobManager {
	private final BlockPos blockPos = new BlockPos();
	private final Vector2 spawnSpot = new Vector2();
	private final IntArray array = new IntArray();
	
	private int hostiles, passive;
	private int hostTimer, passTimer;
	
	public void ints() {
		hostTimer = 180;
		for (int i = 0; i < 30 * World.AMOUNT; i++) spawnPig();
	}

	public void update(Player player) {
		Vector3 pos = player.getPos();
		pos.y += 0.4f;
		
		checkMobs();
		if (hostiles < 30 && !peaceful.value) {
			if (--hostTimer < 0) {
				hostTimer = 60;
				
				int xPos, zPos, failTems = 0, level = -1;
				do {
					spawnSpot.setToRandomDirection().scl(MathUtils.random(20f, 48f)); // 20f, 48f
					xPos = MathUtils.floor(spawnSpot.x+pos.x);
					zPos = MathUtils.floor(spawnSpot.y+pos.z);
					
					level = getLevel(xPos, zPos);
					if (level == -1) {
						failTems += 5;
						continue; 
					}
				} while (++failTems < 30 && (xPos < 0 || zPos < 0 || xPos >= World.SIZE || zPos >= World.SIZE));
				
				if (level != -1)  {
					final int data = world.getData(xPos, level+1, zPos);
					final int sunlit = Math.max(BlockUtils.toSunLight(data)+world.level, 0);
					final int srclit = BlockUtils.toSrcLight(data);
					
					if (sunlit < 4 && srclit < 4) {
						final float x, y, z;
						x = xPos;
						z = zPos;
						y = (float)level + 1;
						
						world.addEntity(new Zombie().setPos(x+0.5f, y+0.1f, z+0.5f));
					}
				}
			}
		}
		
		if (passive < 30 * World.AMOUNT) {
			if (--passTimer < 0) {
				passTimer = 600;
				spawnPig();
			}
		}
	}
	
	private void spawnPig() {
		int xPos, zPos, level = -1, failTems = 0;
		do {
			xPos = random(World.SIZE);
			zPos = random(World.SIZE);
		} while (failTems++ < 50 && (level = getLevelPass(xPos, zPos)) == -1);

		if (level != -1)  {
			final float x, y, z;
			x = xPos + 0.5f;
			z = zPos + 0.5f;
			y = (float)level + 1;

			world.addEntity(new Pig().setPos(x, y+0.1f, z));
		}
	}
	
	private void checkMobs() {
		hostiles = 0; passive = 0;
		for (Entity entity : world.entities) {
			if (entity instanceof EntityMob) {
				final MobType type = ((EntityMob) entity).getMobType();
				if (type == MobType.MONSTER) {
					hostiles++;
				} else if (type == MobType.ANIMAL) {
					passive++;
				}
			}
		}
	}
	
	private int getLevel(int x, int z) {
		array.clear();
		for (int y = World.HEIGHT; y >= 0; y--) {
			if (world.getBlock(blockPos.set(x, y, z)).getMaterial().spawnable() &&
			   !world.getBlock(blockPos.set(x, y+2, z)).getMaterial().hasCollision()) {
				final Block block = world.getBlock(blockPos.set(x, y+1, z));
				if (block != Blocks.WATER && !block.getMaterial().hasCollision()) {
					array.add(y);
				}
			}
		}
		
		if (array.isEmpty()) {
			return -1;
		}
		
		if (MathUtils.randomBoolean(0.3f)) {
			if (Math.max(BlockUtils.toSunLight(world.getData(x, array.first()+1, z)), 0) < 4) {
				return -1;
			}
			return array.first();
		} else {
			return array.random();
		}
	}
	
	private int getLevelPass(int x, int z) {
		array.clear();
		for (int y = World.HEIGHT; y >= 0; y--) {
			if (world.getBlock(blockPos.set(x, y, z)).getBlockType(blockPos) == BlockType.GRASS &&
			   !world.getBlock(blockPos.set(x, y+2, z)).getMaterial().hasCollision()) {
				final Block block = world.getBlock(blockPos.set(x, y+1, z));
				if (block != Blocks.WATER && !block.getMaterial().hasCollision()) {
					array.add(y);
				}
			}
		}
		
		if (array.isEmpty()) {
			return -1;
		}
		
		if (MathUtils.randomBoolean(0.3f)) {
			return array.get(0);
		} else {
			return array.random();
		}
	}
	
	public enum MobType {
		ANIMAL, MONSTER, NONE
	}
}
