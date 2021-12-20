package com.andedit.arcubit.utils;

import static com.andedit.arcubit.world.World.world;

import com.andedit.arcubit.blocks.Block;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

public final class BlockPos {
	/** Max buffer size. */
	private static final int MAX_BUFFER_SIZE = 1<<11;
	
	/** BlockPos buffer. */
	private static final BlockPos[] TABLE = new BlockPos[MAX_BUFFER_SIZE];
	
	/** Position of the current index. */
	private static int position = 0;
	
	/** Create a unsafe BlockPos. Use {@link #copy()} if you need to store object. */
	public static BlockPos newBlockPos() {
		if (position >= MAX_BUFFER_SIZE) return new BlockPos();
		BlockPos blockPos = TABLE[position];
		if (blockPos == null) {
			blockPos = new BlockPos();
			TABLE[position] = blockPos;
		}
		++position;
		return blockPos;
	}
	
	public static void reset() {
		//if (position >= MAX_BUFFER_SIZE) LOG.info("All pool objects has been used and was creating new objects."); // Performances warning
		position = 0;
	}
	
	public int x, y, z;

	public BlockPos() {
	}

	public BlockPos(int x, int y, int z) {
		set(x, y, z);
	}

	public BlockPos(BlockPos pos) {
		set(pos);
	}
	
	public BlockPos(Vector3 pos) {
		set(pos);
	}
	
	public BlockPos set(Vector3 pos) {
		this.x = MathUtils.floor(pos.x);
		this.y = MathUtils.floor(pos.y);
		this.z = MathUtils.floor(pos.z);
		return this;
	}
	
	public BlockPos set(BlockPos pos) {
		this.x = pos.x;
		this.y = pos.y;
		this.z = pos.z;
		return this;
	}
	
	public Block getBlock() {
		return world.getBlock(this);
	}
	
	public void setAir(boolean dropItem) {
		world.setAir(this, PlaceType.SET, dropItem);
	}
	
	public boolean isAir() {
		return world.getBlock(this).isAir();
	}
	
	public BlockPos set(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}
	
	/** Create a offset BlockPos from BlockPos's pool. Use {@link #copy()} to store the BlockPos. */
	public BlockPos offset(Facing face) {
		return offset(face.offset);
	}
	
	/** Create a offset BlockPos from BlockPos's pool. Use {@link #copy()} to store the BlockPos. */
	public BlockPos offset(Facing face, int num) {
		final BlockPos offset = face.offset;
		return offset(offset.x*num, offset.y*num, offset.z*num);
	}

	/** Create a offset BlockPos from BlockPos's pool. Use {@link #copy()} to store the BlockPos. */
	public BlockPos offset(BlockPos pos) {
		return offset(pos.x, pos.y, pos.z);
	}

	/** Create a offset BlockPos from BlockPos's pool. Use {@link #copy()} to store the BlockPos. */
	public BlockPos offset(int x, int y, int z) {
		return newBlockPos().set(this.x + x, this.y + y, this.z + z);
	}
	
	public BlockPos add(BlockPos pos) {
		return add(pos.x, pos.y, pos.z);
	}

	public BlockPos add(int x, int y, int z) {
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}
	
	public BlockPos sub(BlockPos pos) {
		return sub(pos.x, pos.y, pos.z);
	}

	public BlockPos sub(int x, int y, int z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
		return this;
	}
	
	@Override
	public int hashCode() {
		return 29 * z + 1721 * x + 95713 * y;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null) return false;
		if (obj.getClass() == BlockPos.class) {
			final BlockPos p = (BlockPos)obj;
			return p.x == x && p.y == y && p.z == z;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "("+x+", "+y+", "+z+")";
	}
	
	@Override
	public BlockPos clone() {
		return new BlockPos(this);
	}
}
