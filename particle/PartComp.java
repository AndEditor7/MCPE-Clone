package com.andedit.arcubit.particle;

public interface PartComp {
	void ints(Particle part);
	void update(Particle part);
	boolean isDead(Particle part);
}
