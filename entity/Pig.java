package com.andedit.arcubit.entity;

import static com.andedit.arcubit.TheGame.game;
import static com.andedit.arcubit.world.World.world;

import com.andedit.arcubit.Sounds;
import com.andedit.arcubit.entity.ai.Brain;
import com.andedit.arcubit.entity.ai.Looking;
import com.andedit.arcubit.entity.ai.Nothing;
import com.andedit.arcubit.entity.ai.Walk;
import com.andedit.arcubit.graphics.EntityBatch;
import com.andedit.arcubit.items.FoodItem;
import com.badlogic.gdx.math.MathUtils;

public class Pig extends EntityAnimal {
	
	public final Brain brain = new Brain();

	public Pig() {
		brain.add(new Nothing(70), 1);
		brain.add(new Walk(this), 1);
		brain.add(new Looking(this), 2);
	}

	@Override
	public Pig setPos(float x, float y, float z) {
		final float s = 0.45f;
		box.set(x - s, y, z - s, x + s, y + (s*2f), z + s);
		return this;
	}

	@Override
	public void update() {
		if (!canUpdate()) return;
		super.update();
		if (!isDying()) brain.update();
		move();
	}
	
	@Override
	public float getEyeHeight() {
		return 0.7f;
	}
	
	@Override
	public int getMaxHealth() {
		return 10;
	}
	
	@Override
	public void playDeath() {
		Sounds.PIG_DEATH.play3d(game.getCamDst(getCenter()), 16, 1);
	}
	
	@Override
	public void playIdl() {
		Sounds.PIG.play3d(game.getCamDst(getCenter()), 16, 1);
	}
	
	@Override
	public void playHurt() {
		playIdl();
	}
	
	@Override
	protected void killed() {
		final int s =  MathUtils.random(2)+1;
		for (int i = 0; i < s; i++) {
			ItemEnitiy enitiy = new ItemEnitiy(getPos().add(0, 0.4f, 0), new FoodItem(FoodItem.RAW_PORK));
			enitiy.setVel();
			world.addEntity(enitiy);
		}
	}

	@Override
	public void render(EntityBatch batch) {
		Entities.PIG_MODEL.render(batch, this);
	}
}
