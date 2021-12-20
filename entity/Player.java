package com.andedit.arcubit.entity;

import com.andedit.arcubit.Sounds;
import com.andedit.arcubit.Steve;
import com.andedit.arcubit.TheGame;
import com.andedit.arcubit.file.Properties;
import com.andedit.arcubit.graphics.EntityBatch;
import com.andedit.arcubit.handles.Controllor;
import com.andedit.arcubit.handles.Inputs;
import com.andedit.arcubit.ui.DeathScreen;
import com.andedit.arcubit.utils.Camera;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.MathUtils;

public class Player extends EntityMob {
	public final Steve steve;
	
	private final Controllor controllor;
	private final Camera camera;
	private final TheGame game;
	
	public Player(Steve steve, boolean isLoad) {
		this.steve = steve;
		this.camera = steve.camera;
		this.controllor = steve.controllor;
		this.game = steve.game;
	}
	
	@Override
	public Player setPos(float x, float y, float z) {
		final float w = 0.3f;
		final float h = 1.8f;
		box.set(x - w, y, z - w, x + w, y + h, z + w);
		fallAtDst = y;
		return this;
	}
	
	@Override
	public float getEyeHeight() {
		return 1.6f;
	}
	
	@Override
	public int getMaxHealth() {
		return 20;
	}
	
	public boolean isWalking() {
		return onGround;
	}

	public void update() {
		if (Inputs.isKeyJustPressed(Keys.F)) {
			isFlying = !isFlying;
		}
		
		swiming = controllor.isJump || controllor.isForward;
		
		process();
		super.update();
		move();
		
		camera.position.set(getPos());
		camera.position.y += 1.6f;
		
		box.grow(1f, 1f, 1f, ItemEnitiy.BOX);
	}
	
	@Override
	protected void killed() {
		game.manager.setUI(DeathScreen.class);
	}
	
	@Override
	public void playHurt() {
		Sounds.HURT.play();
	}
	
	@Override
	public void playDeath() {
		playHurt();
	}

	public void process() {
		if (game.isPlaying() && !isDying()) {
			// Get forward angel
			
			move.setZero();
			float angle = camera.yaw;
			if (controllor.isForward) {
				move.add(MathUtils.sinDeg(angle), MathUtils.cosDeg(angle));
			} if (controllor.isBack) {
				move.sub(MathUtils.sinDeg(angle), MathUtils.cosDeg(angle));
			} if (controllor.isRight) {
				move.sub(MathUtils.sinDeg(angle+90f), MathUtils.cosDeg(angle+90f));
			} if (controllor.isLeft) {
				move.add(MathUtils.sinDeg(angle+90f), MathUtils.cosDeg(angle+90f));
			}
			move.nor();
			
			float yMove = 0;
			if (isFlying) {
				if (controllor.isJump) {
					yMove += 0.02f;
				}
				if (controllor.isDown) {
					yMove -= 0.02f;
				}
			}

			vel.y += yMove;
		}
		
		if (game.isPlaying() && !isFlying && !isDying()) {
			if (controllor.isJump && onGround) {
				jump();
			}
		}
	}
	
	@Override
	public float getWalkSpeed() {
		// Ground speed
		float speed = 0.017f;
		
		// Air speed
		if (!onGround) {
			speed *= AIR_SCALE;
		}
		
		// Water speed
		if (inLiquid) {
			speed = 0.01f;
		}
		
		// Flying speed
		if (isFlying) {
			speed = 0.04f; // 0.03f
		}
		
		return speed;
	}

	@Override
	public void render(EntityBatch batch) {
	}
	
	@Override
	public void save(Properties props) {
		super.save(props);
		props.put("isFlying", isFlying);
	}
	
	@Override
	public void load(Properties props) {
		super.load(props);
		isFlying = props.got("isFlying", false);
	}
}
