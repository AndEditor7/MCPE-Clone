package com.andedit.arcubit.entity;

import static com.badlogic.gdx.math.MathUtils.random;

import static com.andedit.arcubit.TheGame.game;

import static com.andedit.arcubit.world.World.world;

import com.andedit.arcubit.blocks.Blocks;
import com.andedit.arcubit.blocks.utils.BlockUtils;
import com.andedit.arcubit.world.MobManager.MobType;

public abstract class EntityAnimal extends EntityMob {
	
	short despawnTimer = 30 * 60;

	@Override
	public void update() {
		final Player player = game.player;
		if ((BlockUtils.toSunLight(world.getData(getCenter())) == 0)  && 
		   (world.getBlock(getPos().sub(0, 0.1f, 0)) != Blocks.GRASS) &&
		   (player.getCenter().dst(getCenter()) > 20f)) {
			if (despawnTimer >= 0) {
				if (random.nextInt(1000) == 0) {
					forceDead();
				}
			} else despawnTimer--;
		} else {
			despawnTimer = 30 * 60;
		}
		super.update();
	}
	
	@Override
	public MobType getMobType() {
		return MobType.ANIMAL;
	}
	
	boolean canUpdate() {
		return game.player.getCenter().dst(getCenter()) < 100f;
	}
}
