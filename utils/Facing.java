package com.andedit.arcubit.utils;

import com.badlogic.gdx.math.MathUtils;

public enum Facing {
	/** Facing Y+ */
	UP  (-2, Axis.Y, new BlockPos(0, 1, 0)),
	/** Facing Y- */
	DOWN(-2, Axis.Y, new BlockPos(0, -1, 0)),
	/** Facing Z- */
	NORTH(0, Axis.Z, new BlockPos(0, 0, -1)),
	/** Facing X+ */
	EAST (1, Axis.X, new BlockPos(1, 0, 0)),
	/** Facing Z+ */
	SOUTH(2, Axis.Z, new BlockPos(0, 0, 1)), 
	/** Facing X- */
	WEST(-1, Axis.X, new BlockPos(-1, 0, 0));
	
	private final int num;
	
	public final Axis axis;
	public final BlockPos offset;
	
	private Facing(int num, Axis axis, BlockPos offset) {
		this.num = num;
		this.offset = offset;
		this.axis = axis;
	}
	
	public Facing rotateRight() {
		switch (this) {
		case NORTH: return EAST;
		case EAST:  return SOUTH;
		case SOUTH: return WEST;
		case WEST:  return NORTH;
		default:    return this;
		}
	}
	
	public Facing rotateLeft() {
		switch (this) {
		case NORTH: return WEST;
		case EAST:  return NORTH;
		case SOUTH: return EAST;
		case WEST:  return SOUTH;
		default:    return this;
		}
	}
	
	public Facing rotate(int rotate) {
		if (num == -2) return this;
		switch ((num+rotate)&3) {
		case 0:  return NORTH;
		case 1:  return EAST;
		case 2:  return SOUTH;
		case 3:  return WEST;
		default: return this;
		}
	}
	
	public boolean isSide() {
		return axis != Axis.Y;
	}
	
	public int getRotateValue() {
		return num;
	}
	
	public Facing invert() {
		switch (this) {
		case UP:    return DOWN;
		case DOWN:  return UP;
		case NORTH: return SOUTH;
		case EAST:  return WEST;
		case SOUTH: return NORTH;
		case WEST:  return EAST;
		default:    return this;
		}
	}
	
	private static final Facing[] ARRAY = values();
	public static final int SIZE = ARRAY.length;
	public static Facing get(int ordinal) {
		return ARRAY[MathUtils.clamp(ordinal, 0, 5)];
	}
	
	public static enum Axis {
		X, Y, Z;
	}
}
