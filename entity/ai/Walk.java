package com.andedit.arcubit.entity.ai;

import com.andedit.arcubit.entity.EntityMob;
import com.andedit.arcubit.entity.EntityMonster;
import com.badlogic.gdx.math.MathUtils;

public class Walk implements Ai {
	public float angle;
	
	private final EntityMob entity;
	private int timer1, timer2;
	
	public Walk(EntityMob entity) {
		this.entity = entity;
	}

	@Override
	public void start() {
		angle = MathUtils.random(-180f, 180f);
		timer1 = MathUtils.random(50, 160);
		timer2 = timer1 + 140;
	}

	@Override
	public boolean update() {
		if (entity instanceof EntityMonster) {
			if (!((EntityMonster)entity).inMoveRadius) return true;
		}
			
		if (entity.inLiquid) {
			timer1--;
		} else if (entity.onGround || !entity.onSide) {
			timer1--;
		}
		
		if (timer1 < 0 || --timer2 < 0) {
			return true;
		}
		
		entity.move.set(MathUtils.sinDeg(angle), MathUtils.cosDeg(angle));
		return false;
	}

	@Override
	public int finish() {
		return 30;
	}
}
