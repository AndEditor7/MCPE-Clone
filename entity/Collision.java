package com.andedit.arcubit.entity;

import static com.andedit.arcubit.world.World.world;
import static com.andedit.arcubit.utils.maths.CollisionBox.POOL;

import com.andedit.arcubit.utils.BlockPos;
import com.andedit.arcubit.utils.maths.CollisionBox;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

interface Collision<T extends Entity> {
	static final BlockPos POS = new BlockPos();
	static final Array<CollisionBox> BOXES = new Array<>();
	static final CollisionBox INTERSECT = new CollisionBox();
	static final Vector3 MOVE = new Vector3();
	
	static final Collision<Entity> HANDLE = new Collision<Entity>() {};
	
	static void handle(Entity e) {
		Collision.HANDLE.move(e);
	}
	
	default Array<CollisionBox> getBox(T entity, CollisionBox intersect) {
		final int xMin = MathUtils.floor(intersect.xMin);
		final int yMin = MathUtils.floor(intersect.yMin);
		final int zMin = MathUtils.floor(intersect.zMin);
		final int xMax = MathUtils.ceil(intersect.xMax);
		final int yMax = MathUtils.ceil(intersect.yMax);
		final int zMax = MathUtils.ceil(intersect.zMax);
		
		BOXES.size = 0;
		for (int x = xMin; x < xMax; x++)
		for (int y = yMin; y < yMax; y++)
		for (int z = zMin; z < zMax; z++) {
			blockHandle(entity, POS.set(x, y, z));
		}
		
		return BOXES;
	}

	default void blockHandle(T entity, BlockPos pos) {
		world.getBlock(pos).addCollisions(pos, BOXES, POOL);
	}

	default void collideY(T entity, Vector3 move) {
		final CollisionBox box = entity.box;
		for (CollisionBox block : BOXES) {
			move.y = block.collideY(box, move.y);
		}
		box.yMin += move.y;
		box.yMax += move.y;
	}

	default void collideX(T entity, Vector3 move) {
		final CollisionBox box = entity.box;
		for (CollisionBox block : BOXES) {
			move.x = block.collideX(box, move.x);
		}
		box.xMin += move.x;
		box.xMax += move.x;
	}

	default void collideZ(T entity, Vector3 move) {
		final CollisionBox box = entity.box;
		for (CollisionBox block : BOXES) {
			move.z = block.collideZ(box, move.z);
		}
		box.zMin += move.z;
		box.zMax += move.z;
	}

	default void move(T entity) {
		MOVE.set(entity.vel);
		final CollisionBox box = entity.box;
		
		box.expand(MOVE.x, MOVE.y, MOVE.z, INTERSECT);
		getBox(entity, INTERSECT);
		
		collideY(entity, MOVE);
		collideX(entity, MOVE);
		collideZ(entity, MOVE);
		
		moveHandle(entity, MOVE);
		
		POOL.freeAll(BOXES);
		BOXES.size = 0;
	}

	default void moveHandle(T entity, Vector3 move) {
		final Vector3 vel = entity.vel;
		
		entity.onGround = (vel.y != move.y) && vel.y < 0f;
		entity.onSide = vel.x != move.x || vel.z != move.z;
		
		if (vel.x != move.x) {
			vel.x = 0;
		}
		
		if (vel.y != move.y) {
			vel.y = 0;
		}
		
		if (vel.z != move.z) {
			vel.z = 0;
		}
	}
}
