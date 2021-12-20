package com.andedit.arcubit.handles;

import com.andedit.arcubit.blocks.Block;
import com.andedit.arcubit.blocks.LiquidBlock;
import com.andedit.arcubit.entity.Entity;
import com.andedit.arcubit.utils.BlockPos;
import com.andedit.arcubit.utils.Facing;
import com.badlogic.gdx.math.collision.BoundingBox;

public final class RayContext {
	public static final RayContext CONTEXT = new RayContext();
	private RayContext() {}
	
	/** Outside the block. */
	public final BlockPos out = new BlockPos();
	/** Inside the block. */
	public final BlockPos in  = new BlockPos();
	/** Inside the liquid block. */
	public final BlockPos inl  = new BlockPos();
	
	/** Facing outward the block */
	public Facing face1;
	
	/** Facing toward the player */
	public Facing face2;
	
	public BoundingBox boxHit;
	public Block blockHit;
	public LiquidBlock liquidHit;
	public Entity entityHit;
	
	public float blockDst, entityDst;
	public boolean isUpper;
	
	public boolean hasHit() {
		return blockHit != null || entityHit != null; 
	}
	
	public HitType getHitType() {
		if (!hasHit()) {
			return HitType.NONE;
		}
		
		if (blockHit != null && entityHit != null) {
			return blockDst < entityDst ? HitType.BLOCK : HitType.ENTITY;
		}
		
		return blockHit != null ? HitType.BLOCK : HitType.ENTITY;
	}
	
	public RayContext clear() {
		blockHit  = null;
		liquidHit = null;
		entityHit = null;
		blockDst  = Float.MAX_VALUE;
		entityDst = Float.MAX_VALUE;
		return this;
	}
	
	public static enum HitType {
		BLOCK, ENTITY, NONE
	}
}
