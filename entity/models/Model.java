package com.andedit.arcubit.entity.models;

import static com.andedit.arcubit.Assets.FIRE;

import com.andedit.arcubit.entity.Entity;
import com.andedit.arcubit.graphics.EntityBatch;
import com.badlogic.gdx.math.Vector3;

public interface Model<T extends Entity> {
	/** Optional static Vectors for creating bounding boxes. */
	static final Vector3 MIN = new Vector3(), MAX = new Vector3();
	static final Vector3 out = new Vector3();
	
	void render(EntityBatch batch, T entity);
	
	static void drawFire(EntityBatch batch) {
		final float a = 0.4f;
		batch.pos(-a, -a, 0);
		batch.light();
		batch.tex(FIRE.getU2(), FIRE.getV2());
		
		batch.pos(-a, a, 0);
		batch.light();
		batch.tex(FIRE.getU2(), FIRE.getV());
		
		batch.pos(a, a, 0);
		batch.light();
		batch.tex(FIRE.getU(), FIRE.getV());
		
		batch.pos(a, -a, 0);
		batch.light();
		batch.tex(FIRE.getU(), FIRE.getV2());
	}
}
