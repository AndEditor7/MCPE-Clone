package com.andedit.arcubit.particle;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class BlockBreak implements PartComp {
	public static final PartComp comp = new BlockBreak();
			
	private BlockBreak() {}

	@Override
	public void ints(Particle part) {
		part.vel.setToRandomDirection().add(0, 0.5f, 0).scl(0.07f);
		part.size = 0.075f;
		part.timer = (short)MathUtils.random(50, 80);
	}

	@Override
	public void update(Particle part) {
		part.vel.x = MathUtils.lerp(part.vel.x, 0, part.onGround ? 0.15f : 0.03f);
		part.vel.z = MathUtils.lerp(part.vel.z, 0, part.onGround ? 0.15f : 0.03f);
		part.vel.y = Math.max(part.vel.y-0.005f, -0.7f);
		
		part.move();
	}

	@Override
	public boolean isDead(Particle part) {
		return part.timer-- < 0;
	}
	
	public static TextureRegion getTex(TextureRegion texture) {
		final TextureRegion newTex = new TextureRegion(texture);
		newTex.setRegion(newTex.getRegionX() + (MathUtils.random(3)*4), newTex.getRegionY() + (MathUtils.random(3)*4), 4, 4);
		return newTex;
	}
}
