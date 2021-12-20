package com.andedit.arcubit.entity.ai;

import com.andedit.arcubit.entity.EntityMob;
import com.andedit.arcubit.utils.Util;
import com.badlogic.gdx.math.MathUtils;

public class Looking implements Ai {
	
	private final EntityMob entity;
	private boolean isLeft;
	private int timer;
	
	public Looking(EntityMob entity) {
		this.entity = entity;
	}

	@Override
	public void start() {
		isLeft = MathUtils.randomBoolean();
		timer = MathUtils.random(15, 50);
	}

	@Override
	public boolean update() {
		if (--timer < 0) {
			return true;
		}
		
		entity.headYaw += isLeft ? -3f : 3f;
		
		if (entity.headYaw < -180f) {
			entity.headYaw += 360f;
		} else if (entity.headYaw > 180f) {
			entity.headYaw -= 360f;
		}
		
		entity.bodyYaw += Util.clampVal(entity.headYaw, -60f, 60);
		entity.headYaw = MathUtils.clamp(entity.headYaw, -60f, 60f);
		
		if (entity.bodyYaw < -180f) {
			entity.bodyYaw += 360f;
		} else if (entity.bodyYaw > 180f) {
			entity.bodyYaw -= 360f;
		}
		
		return false;
	}

	@Override
	public int finish() {
		return 15;
	}

}
