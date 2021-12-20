package com.andedit.arcubit.entity.ai;

import static com.andedit.arcubit.TheGame.game;

import com.andedit.arcubit.entity.EntityMonster;
import com.andedit.arcubit.entity.Player;
import com.andedit.arcubit.handles.RayContext;
import com.andedit.arcubit.handles.VoxelRay;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Target implements Ai {
	
	public static final float DIST = 13;
	
	private final Vector2 target = new Vector2();
	
	private final EntityMonster entity;
	private int timer, hitTimer;
	
	public Target(EntityMonster entity) {
		this.entity = entity;
	}

	@Override
	public void start() {
		timer = 40;
		setTarget();
	}

	@Override
	public boolean update() {
		final Player player = game.player;
		Vector3 playerPos = player.getPos();
		Vector3 thisPos = entity.getPos();
		
		if (--timer < 0) {
			timer = 40;
			final float dst = playerPos.dst(thisPos);
			if (dst > DIST || player.isDying()) {
				return true;
			}
			
			setTarget();
		}
		
		
		final boolean intersect = player.box.intersects(entity.box);
		
		if (!intersect) {
			entity.move.set(target);
		}
		
		if (hitTimer > 0) hitTimer--;
		if (hitTimer <= 0 && intersect) {
			player.hit(entity, 4);
			hitTimer = 60;
		}
		
		return false;
	}
	
	private void setTarget() {
		Vector3 playerPos = game.player.getPos();
		Vector3 thisPos = entity.getPos();
		target.set(playerPos.x - thisPos.x, playerPos.z - thisPos.z).nor();
	}
	
	@Override
	public int finish() {
		return 60;
	}

	
	@Override
	public boolean isRandomPick() {
		return false;
	}
	
	@Override
	public boolean check() {
		final Player player = game.player;
		final Vector3 playerPos = player.getPos();
		final Vector3 thisPos = entity.getPos();
		
		if (game.isSurvival && playerPos.dst(thisPos) < Target.DIST && !player.isDying()) {
			if (!VoxelRay.shot(thisPos.add(0, entity.getEyeHeight(), 0), playerPos.add(0, player.getEyeHeight(), 0), RayContext.CONTEXT)) {
				return true;
			}
		}
		
		return false;
	}
}
