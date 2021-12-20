package com.andedit.arcubit.entity;

import static com.andedit.arcubit.world.World.world;

import com.andedit.arcubit.blocks.Block;
import com.andedit.arcubit.utils.BlockPos;
import com.andedit.arcubit.utils.maths.CollisionBox;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

class CollisionMob implements Collision<EntityMob> {
	private static final float STEP = 0.5f;
	
	private static final CollisionMob HANDLE = new CollisionMob();
	private static final CollisionBox CHECK = new CollisionBox();
	
	static void handle(EntityMob e) {
		HANDLE.move(e);
	}
	
	@Override
	public void blockHandle(EntityMob entity, BlockPos pos) {
		if (!entity.isFlying)
		Collision.super.blockHandle(entity, pos);
	}
	
	@Override
	public void collideX(EntityMob entity, Vector3 move) {
		final CollisionBox box = entity.box;
		for (int i = 0; i < BOXES.size; i++) {
			final float lastX = move.x;
			move.x = BOXES.get(i).collideX(box, move.x);
			if (move.x != lastX && entity.onGround) { // check if collide and on ground.
				final float step = BOXES.get(i).yMax - box.yMin;
				if (step <= STEP) { // check if 0.5 step distant.
					CHECK.set(box);
					CHECK.yMin += step;
					CHECK.yMax += step;
					CHECK.xMin += lastX;
					CHECK.xMax += lastX;
					if (checkValidSpot()) { // check if it valid spot to teleport.
						box.yMin += step;
						box.yMax += step;
						move.x = lastX;
					}
				}
			}
		}
		box.xMin += move.x;
		box.xMax += move.x;
	}
	
	@Override
	public void collideZ(EntityMob entity, Vector3 move) {
		final CollisionBox box = entity.box;
		for (int i = 0; i < BOXES.size; i++) {
			final float lastZ = move.z;
			move.z = BOXES.get(i).collideZ(box, move.z);
			if (move.z != lastZ && entity.onGround) { // check if collide and on ground.
				final float step = BOXES.get(i).yMax - box.yMin;
				if (step <= STEP) { // check if 0.5 step distant.
					CHECK.set(box);
					CHECK.yMin += step;
					CHECK.yMax += step;
					CHECK.zMin += lastZ;
					CHECK.zMax += lastZ;
					if (checkValidSpot()) { // check if it valid spot to teleport.
						box.yMin += step;
						box.yMax += step;
						move.z = lastZ;
					}
				}
			}
			
		}
		box.zMin += move.z;
		box.zMax += move.z;
	}
	
	private static boolean checkValidSpot() {
		final int xMin = MathUtils.floor(CHECK.xMin);
		final int yMin = MathUtils.floor(CHECK.yMin);
		final int zMin = MathUtils.floor(CHECK.zMin);
		final int xMax = MathUtils.ceil(CHECK.xMax);
		final int yMax = MathUtils.ceil(CHECK.yMax);
		final int zMax = MathUtils.ceil(CHECK.zMax);
		
		for (int x = xMin; x < xMax; x++)
		for (int y = yMin; y < yMax; y++)
		for (int z = zMin; z < zMax; z++) {
			final Block block = world.getBlock(POS.set(x, y, z));
			if (!block.getMaterial().hasCollision()) continue;
			
			if (block.intersects(POS, CHECK)) {
				return true;
			}
		}
		return true;
	}
}
