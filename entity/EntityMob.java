package com.andedit.arcubit.entity;

import static com.andedit.arcubit.TheGame.game;
import static com.andedit.arcubit.options.Options.autoJump;

import com.andedit.arcubit.Sounds;
import com.andedit.arcubit.blocks.Block;
import com.andedit.arcubit.blocks.Blocks;
import com.andedit.arcubit.file.Properties;
import com.andedit.arcubit.utils.BlockPos;
import com.andedit.arcubit.utils.Util;
import com.andedit.arcubit.world.MobManager.MobType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public abstract class EntityMob extends Entity {
	public static final float AIR_SCALE = 0.15f;
	
	private static final Vector2 VEC = new Vector2();
	
	// Rigging
	public float headYaw, bodyYaw, swing, tilt;
	
	public int health = getMaxHealth();
	public final Vector2 move = new Vector2();
	
	short fireTimer;
	boolean swiming = true, isFlying;
	float fallAtDst;
	
	private byte hitTimer, fireDam;
	private boolean forceDead, inLadder;
	private short soundTimer;
	private float length;
	
	@Override
	public void update() {
		// Idl sound
		if (--soundTimer < 0 && !isDying()) {
			soundTimer = (short)MathUtils.random(300, 800);
			playIdl();
		}
		
		// Water flow movement.
		waterFlow();
		
		// fire damage
		if (inWater) {
			fireTimer = 0;
		}
		
		if (fireTimer > 0) {
			fireTimer--;
			if (--fireDam < 0) {
				fireDam = 60;
				damage(1);
			}
		}
		
		// fall damage
		getPos();
		if (onGround) {
			damage(Math.max(0, MathUtils.floor(fallAtDst - 3f - pos.y)));
			fallAtDst = pos.y;
		} else {
			fallAtDst = Math.max(fallAtDst, pos.y);
			if (inLiquid) {
				if (inWater && fallAtDst - 3f - pos.y > 0f) {
					Sounds.SPLASH.play3d(game.getCamDst(pos), 16, 0.4f);
				}
				fallAtDst = 0;
			}
			if (isFlying || inLadder) {
				fallAtDst = 0;
			}
		}
		
		inLadder = false;
		movement();
		
		
		if (!MathUtils.isZero(vel.x, 0.01f) || !MathUtils.isZero(vel.z, 0.01f)) {
			swing += getSwingSpeed();
			if (swing > MathUtils.PI2) {
				swing -= MathUtils.PI2;
			}
		} else {
			swing = MathUtils.lerp(swing, swing > MathUtils.PI ? MathUtils.PI2 : 0f, 0.1f);
		}
		
		if (isDying()) {
			tilt += 0.05f;
		}
		
		// timers
		if (hitTimer > 0) hitTimer--;
	}
	
	float getBodyRotSpd() {
		return 6;
	}
	
	private void movement() {
		
		if (!onGround) {
			length = 2f; // Fix walk sound when jumping
		}
		
		// Walking sound
		if (onGround) {
			length += Math.sqrt(vel.x * vel.x + vel.z * vel.z) * 0.62f;
			if (length > 1f) {
				length = 0;
				BlockPos pos = BlockPos.newBlockPos().set(getPos().add(0, -0.1f, 0));
				pos.getBlock().getStep(pos).audio.play3d(game.getCamDst(getCenter()), 16, 0.2f);
			}
		}
		
		if (!move.isZero()) {
			float speed = getWalkSpeed();
			vel.x += move.x * speed;
			vel.z += move.y * speed;
			
			headYaw = Util.lerp(headYaw, 0, 0.9f, 3f);
			
			float angle = MathUtils.atan2(move.x, move.y) * MathUtils.radiansToDegrees;
			bodyYaw = Util.lerpAngle(bodyYaw, angle + 180f, 0.9f, getBodyRotSpd());
			
			move.setZero();
		}
		
		// Auto jump
		if (onGround && onSide) {
			if (getClass() == Player.class) {
				if (autoJump.value) {
					jump();
				}
			} else {
				jump();
			}
		}
		
		// Air drag
		float air = onGround ? 0.2f : 0.06f;
		if (inLiquid) {
			air = inWater ? 0.3f : 0.4f;
		}
		
		float yMove = ((inLiquid && swiming) ? 0.005f : -0.008f);
		
		float a = 0.01f;
		if (inLiquid) {
			a = inWater ? 0.13f : 0.2f;
		}
		
		if (isFlying) {
			yMove = 0;
			air = 0.1f;
			a = 0.1f;
		}
		
		// Water jump boost.
		if (!inLiquid && wasLiquid) {
			if (onSide) jump(); else vel.y += 0.025f;
		}
		
		// Velocity drag
		vel.x = MathUtils.lerp(vel.x, 0, air);
		vel.y = MathUtils.lerp(vel.y + yMove, 0, a);
		vel.z = MathUtils.lerp(vel.z, 0, air);
		vel.x += MathUtils.clamp(xFlow, -1, 1) * 0.004f;
		vel.z += MathUtils.clamp(zFlow, -1, 1) * 0.004f;
		
		if (inLadder) {
			vel.y = onSide ? 0.05f : -0.05f;
		}
	}
	
	@Override
	void handleInBlock(Block block, BlockPos pos) {
		inLadder = block == Blocks.LADDER;
	}
	
	void killed() {
	}
	
	public boolean hit(EntityMob source, int damage) {
		if (health <= 0 || hitTimer != 0) return false; 
		
		Vector3 sourcePos = source.getPos();
		Vector3 thisPos = getPos();
		
		VEC.set(thisPos.x, thisPos.z).sub(sourcePos.x, sourcePos.z).nor();
		vel.add(VEC.x*0.15f, 0, VEC.y*0.15f);
		vel.y = vel.y < 0 ? vel.y + 0.12f: 0.12f;
		
		hitTimer = 28;
		damage(damage);
		return true;
	}
	
	public void damage(int point) {
		if (isDying() || point <= 0) return;
		health = Math.max(health-point, 0);
		swing = MathUtils.HALF_PI;
		if (isDying()) {
			killed();
			playDeath();
		} else {
			playHurt();
		}
	}
	
	public void heal(int point) {
		health = Math.min(health+point, getMaxHealth());
	}
	
	public int getMaxHealth() {
		return 20;
	}
	
	public MobType getMobType() {
		return MobType.NONE;
	}
	
	@Override
	public final boolean isDead() {
		return forceDead || tilt > 2.0f;
	}
	
	public final void forceDead() {
		forceDead = true;
	}
	
	public final boolean isDying() {
		return forceDead || health <= 0;
	}
	
	float getSwingSpeed() {
		return 0.15f;
	}
	
	public void playIdl() {
		
	}
	
	public void playHurt() {
		
	}
	
	public void playDeath() {
		
	}
	
	public final boolean isOnFire() {
		return fireTimer > 0;
	}
	
	public float getEyeHeight() {
		return 0.5f;
	}
	
	final void jump() {
		if (!isDying() && !inLiquid) vel.y = 0.15f;
	}
	
	public float getWalkSpeed() {
		final float speed = 0.008f;
		float move = speed;
		if (!onGround) {
			move *= EntityMob.AIR_SCALE;
		} if (inLiquid) {
			move = speed;
		}
		return move;
	}
	
	@Override
	void move() {
		CollisionMob.handle(this);
	}
	
	@Override
	public void save(Properties props) {
		super.save(props);
		props.put("health", health);
		props.put("fallAtDst", fallAtDst);
		props.put("bodyYaw", bodyYaw);
		props.put("fireTimer", fireTimer);
	}
	
	@Override
	public void load(Properties props) {
		super.load(props);
		health = props.got("health");
		fallAtDst = props.got("fallAtDst");
		bodyYaw = props.got("bodyYaw");
		fireTimer = props.got("fireTimer", (short)0);
	}
}
