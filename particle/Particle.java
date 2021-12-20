package com.andedit.arcubit.particle;

import static com.andedit.arcubit.world.World.world;

import com.andedit.arcubit.utils.BlockPos;
import com.andedit.arcubit.utils.maths.CollisionBox;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public final class Particle {
	private static final CollisionBox bintersector = new CollisionBox();
	private static final BlockPos blockPos = new BlockPos();
	private static final Array<CollisionBox> boxes = new Array<>();
	private static final Vector3 pos = new Vector3();
	
	public final Vector3 vel = new Vector3();
	public TextureRegion tex;
	public float size = 1;
	
	final CollisionBox box = new CollisionBox();
	
	short timer;
	boolean onGround;
	
	private PartComp comp;
	
	Particle() {
	}
	
	public Vector3 getPos() {
		return pos.set((box.xMin+box.xMax)*0.5f, (box.yMin+box.yMax)*0.5f, (box.zMin+box.zMax)*0.5f);
	}
	
	public void setPos(float x, float y, float z) {
		box.set(x, y, z, size);
	}
	
	public Particle setComp(PartComp comp) {
		this.comp = comp;
		vel.setZero();
		size = 1;
		timer = 0;
		onGround = false;
		comp.ints(this);
		return this;
	}
	
	void update() {
		comp.update(this);
	}
	
	boolean isDead() {
		return comp.isDead(this);
	}
	
	void move() {
		
		float
		xMove = vel.x,
		yMove = vel.y,
		zMove = vel.z;
		
		
		box.expand(xMove, yMove, zMove, bintersector);

		final int xMin = MathUtils.floor(bintersector.xMin);
		final int yMin = MathUtils.floor(bintersector.yMin);
		final int zMin = MathUtils.floor(bintersector.zMin);
		final int xMax = MathUtils.ceil(bintersector.xMax);
		final int yMax = MathUtils.ceil(bintersector.yMax);
		final int zMax = MathUtils.ceil(bintersector.zMax);
		
		for (int x = xMin; x < xMax; x++)
		for (int y = yMin; y < yMax; y++)
		for (int z = zMin; z < zMax; z++) {
			world.getBlock(blockPos.set(x, y, z)).addCollisions(blockPos, boxes, CollisionBox.POOL);
		}
		
		final float 
		xLastMove = xMove,
		yLastMove = yMove,
		zLastMove = zMove;

		final int size = boxes.size;
		for (int i = 0; i < size; i++) {
			yMove = boxes.get(i).collideY(box, yMove);
		}
		box.yMin += yMove;
		box.yMax += yMove;
		
		onGround = (yLastMove != yMove) && yLastMove < 0f;
		
		for (int i = 0; i < size; i++) {
			xMove = boxes.get(i).collideX(box, xMove);
		}
		box.xMin += xMove;
		box.xMax += xMove;
		
		for (int i = 0; i < size; i++) {
			zMove = boxes.get(i).collideZ(box, zMove);
		}
		box.zMin += zMove;
		box.zMax += zMove;
		
				
		if (xLastMove != xMove) {
			vel.x = 0f;
		}
		if (yLastMove != yMove) {
			vel.y = 0f;
		}
		if (zLastMove != zMove) {
			vel.z = 0f;
		}

		CollisionBox.POOL.freeAll(boxes);
		boxes.size = 0;
	}
}
