package com.andedit.arcubit.entity;

import static com.andedit.arcubit.world.World.world;

import com.andedit.arcubit.blocks.Block;
import com.andedit.arcubit.blocks.Blocks;
import com.andedit.arcubit.blocks.utils.BlockType;
import com.andedit.arcubit.file.Properties;
import com.andedit.arcubit.file.Serial;
import com.andedit.arcubit.graphics.EntityBatch;
import com.andedit.arcubit.utils.BlockPos;
import com.andedit.arcubit.utils.maths.CollisionBox;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

public abstract class Entity implements Serial {
	public final CollisionBox box = new CollisionBox();
	public final Vector3 vel = new Vector3();
	public boolean onGround, onSide, inWater, inLiquid;
	
	public byte xFlow, zFlow;
	
	boolean wasWater, wasLiquid;
	
	final Vector3 pos = new Vector3();
	public Vector3 getPos() {
		return pos.set((box.xMin+box.xMax)*0.5f, box.yMin, (box.zMin+box.zMax)*0.5f);
	}
	
	public final Vector3 getCenter() {
		return pos.set((box.xMin+box.xMax)*0.5f, (box.yMin+box.yMax)*0.5f, (box.zMin+box.zMax)*0.5f);
	}
	
	public abstract Entity setPos(float x, float y, float z);
	
	public abstract void update();
	
	void waterFlow() {
		final int xMin = MathUtils.floor(box.xMin);
		final int yMin = MathUtils.floor(box.yMin);
		final int zMin = MathUtils.floor(box.zMin);
		final int xMax = MathUtils.ceil(box.xMax);
		final int yMax = MathUtils.ceil(box.yMax);
		final int zMax = MathUtils.ceil(box.zMax);
		
		xFlow = 0;
		zFlow = 0;
		
		wasWater = inWater;
		wasLiquid = inLiquid;
		inWater = false;
		inLiquid = false;
		for (int x = xMin; x < xMax; x++)
		for (int y = yMin; y < yMax; y++)
		for (int z = zMin; z < zMax; z++) {
			final Block block = world.getBlock(blockPos.set(x, y, z));
			if (block.intersects(blockPos, box)) {
				handleInBlock(block, blockPos);
				block.inBlockHandle(blockPos, this);
				if (block == Blocks.WATER) {
					inWater = true;
				}
				if (block.getBlockType(blockPos) == BlockType.LIQUID) {
					inLiquid = true;
				}
			}
		}
	}
	
	
	
	void handleInBlock(Block block, BlockPos pos) {
		
	}
	
	public abstract void render(EntityBatch batch);
	
	public abstract boolean isDead();
	
	static final BlockPos blockPos = new BlockPos();
	void move() {
		Collision.handle(this);
	}
	
	@Override
	public void save(Properties props) {
		props.put("box", box);
		props.put("vel", vel);
		props.put("onGround", onGround);
		props.put("inWater", inWater);
		props.put("inLiquid", inLiquid);
	}
	
	@Override
	public void load(Properties props) {
		box.set((CollisionBox)props.get("box"));
		vel.set((Vector3)props.get("vel"));
		onGround = props.got("onGround", false);
		inLiquid = props.got("inLiquid", false);
		inWater = props.got("inWater", false);
		wasWater = inWater;
		wasLiquid = inLiquid;
	}
}
