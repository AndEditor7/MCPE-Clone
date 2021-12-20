package com.andedit.arcubit.entity;

import static com.andedit.arcubit.TheGame.game;
import static com.andedit.arcubit.options.Options.peaceful;
import static com.andedit.arcubit.world.World.world;
import static com.badlogic.gdx.math.MathUtils.random;

import com.andedit.arcubit.blocks.utils.BlockUtils;
import com.andedit.arcubit.entity.ai.Brain;
import com.andedit.arcubit.entity.ai.Target;
import com.andedit.arcubit.world.MobManager.MobType;
import com.badlogic.gdx.math.Vector3;

public abstract class EntityMonster extends EntityMob {
	
	boolean target;
	Brain brain = new Brain();
	
	// MobManager
	public boolean inMoveRadius = true;
	public short despawnTimer = 30 * 60;
	
	byte burnTimer;
	
	@Override
	public void update() {
		if (peaceful.value) forceDead();
		final Player player = game.player;
		if (despawnTimer > 0) despawnTimer--;
		
		// get positions.
		Vector3 playerPos = player.getPos();
		Vector3 thisPos = getPos();
		
		// calculate distance.
		final float a = thisPos.x - playerPos.x;
		final float b = thisPos.z - playerPos.z;
		final float dst = (float)Math.sqrt(a * a + b * b);
		
		// Check if is in far distance and it may need to despawn.
		if (dst > 80f) {
			forceDead();
		}
		
		// Check if mob can move in that radius.
		inMoveRadius = dst < 48f;
		
		if (inMoveRadius) {
			despawnTimer = 30 * 60; // set 30 second cooldown before randomly despawn.
		} else if (despawnTimer <= 0 && random.nextInt(2000) == 0) {
			forceDead(); // start randomly despawn.
		}
		
		thisPos.y += 0.1f;
		if (BlockUtils.toSunLight(world.getData(thisPos))+world.level >= 15) {
			fireTimer = 240;
		}
		super.update();
	}
	
	@Override
	public boolean hit(EntityMob source, int damge) {
		if (super.hit(source, damge)) {
			final float a = random.nextBoolean()?70f:-70f;
			bodyYaw = (bodyYaw - a) % 360f;
			headYaw = (headYaw + a) % 360f;
			return true;
		}
		
		return false;
	}
	
	public MobType getMobType() {
		return MobType.MONSTER;
	}
	
	float getBodyRotSpd() {
		return isTarget() ? 4 : 6;
	}
	
	boolean isTarget() {
		return brain.hasAi(Target.class);
	}
}
