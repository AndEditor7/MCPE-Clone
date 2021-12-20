package com.andedit.arcubit.blocks.utils;

public final class BlockUtils {
	public static final int   fullLight = 15;
	public static final float lightScl  = fullLight;
	
	// DO NOT TOUCH! Unless you know what you doing.
	public static final int 
	SUN_SHIFT = 28,
	SRC_SHIFT = 24,
	DATA_SHIFT = 8,
	SRC_AND = 0xF,
	DATA_AND = 0xFFFF,
	ID_AND = 0xFF,
	SUN_INV = ~(SRC_AND<<SUN_SHIFT),
	SRC_INV = ~(SRC_AND<<SRC_SHIFT),
	DATA_INV = ~(DATA_AND<<DATA_SHIFT),
	ID_INV = ~ID_AND,
	NODATA = (SRC_AND<<SUN_SHIFT)|(SRC_AND<<SRC_SHIFT);
	
	public static int toSunLight(int data) {
	    return data >>> SUN_SHIFT;
	}
	
	public static int toSrcLight(int data) {
	    return (data >>> SRC_SHIFT) & SRC_AND;
	}
	
	public static int toBlockData(int data) {
		return (data >>> DATA_SHIFT) & DATA_AND;
	}
	
	public static int toBlockID(int data) {
		return data & ID_AND;
	}
	
	public static int maxLight(int data) {
		return Math.max(toSrcLight(data), toSunLight(data));
	}
}
