package com.andedit.arcubit.entity;

import static com.andedit.arcubit.TheGame.game;

import com.andedit.arcubit.Sounds;
import com.andedit.arcubit.entity.ai.Brain;
import com.andedit.arcubit.entity.ai.Looking;
import com.andedit.arcubit.entity.ai.Nothing;
import com.andedit.arcubit.entity.ai.Walk;
import com.andedit.arcubit.file.Properties;
import com.andedit.arcubit.graphics.EntityBatch;

public class Sheep extends EntityAnimal {
	
	public boolean hasWool = true;
	
	public final Brain brain = new Brain();

	public Sheep() {
		brain.add(new Nothing(70), 1);
		brain.add(new Walk(this), 1);
		brain.add(new Looking(this), 2);
	}

	@Override
	public Entity setPos(float x, float y, float z) {
		float s = 0.9f/2f;
		box.set(x-s, y, z-s, x+s, y+1.3f, z+s);
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
	public void render(EntityBatch batch) {
		Entities.SHEEP_MODEL.render(batch, this);
	}
	
	@Override
	public void playDeath() {
		playIdl();
	}
	
	@Override
	public void playIdl() {
		Sounds.SHEEP.play3d(game.getCamDst(getCenter()), 16, 1);
	}
	
	@Override
	public void playHurt() {
		playIdl();
	}
	
	@Override
	public void save(Properties props) {
		super.save(props);
		props.put("hasWool", hasWool);
	}
	
	@Override
	public void load(Properties props) {
		super.load(props);
		hasWool = props.got("hasWool", true);
	}
}
