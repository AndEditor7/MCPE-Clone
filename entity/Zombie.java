package com.andedit.arcubit.entity;

import static com.andedit.arcubit.TheGame.game;
import static com.andedit.arcubit.world.World.world;
import static com.badlogic.gdx.math.MathUtils.random;

import com.andedit.arcubit.Sounds;
import com.andedit.arcubit.entity.ai.Looking;
import com.andedit.arcubit.entity.ai.Nothing;
import com.andedit.arcubit.entity.ai.Target;
import com.andedit.arcubit.entity.ai.Walk;
import com.andedit.arcubit.graphics.EntityBatch;
import com.andedit.arcubit.items.LootBag;

public class Zombie extends EntityMonster {

	public Zombie() {
		brain.add(new Nothing(70), 1);
		brain.add(new Walk(this), 1);
		brain.add(new Looking(this), 2);
		brain.add(new Target(this));
	}

	@Override
	public Zombie setPos(float x, float y, float z) {
		final float w = 0.3f;
		final float h = 1.8f;
		box.set(x - w, y, z - w, x + w, y + h, z + w);
		return this;
	}

	@Override
	public void update() {
		super.update();
		if (!isDying()) brain.update();
		move();
	}
	
	@Override
	public float getEyeHeight() {
		return 1.6f;
	}
	
	@Override
	public int getMaxHealth() {
		return 14;
	}
	
	@Override
	public void playIdl() {
		Sounds.ZOMBIE.play3d(game.getCamDst(getCenter()), 16, 1);
	}
	
	@Override
	public void playHurt() {
		Sounds.ZOMBIE_HURT.play3d(game.getCamDst(getCenter()), 16, 1);
	}
	
	@Override
	public void playDeath() {
		Sounds.ZOMBIE_DEATH.play3d(game.getCamDst(getCenter()), 16, 1);
	}
	
	@Override
	protected void killed() {
		if (random.nextInt(10) == 0)  {
			ItemEnitiy enitiy = new ItemEnitiy(getPos().add(0, 0.4f, 0), new LootBag());
			enitiy.setVel();
			world.addEntity(enitiy);
		}
	}

	@Override
	public void render(EntityBatch batch) {
		Entities.ZOMBIE_MODEL.render(batch, this);
	}
	
	@Override
	protected float getSwingSpeed() {
		return 0.1f;
	}
}
