package com.andedit.arcubit.blocks.data;

import com.andedit.arcubit.utils.BlockPos;
import com.andedit.arcubit.utils.Facing;

public class HoriComp extends DataComp {
	
	public static final String KEY = "horizon";

	public HoriComp() {
		super(2, true);
	}
	
	public Facing getFace(BlockPos pos) {
		switch (getData(pos)) {
		case 0: return Facing.NORTH;
		case 1: return Facing.EAST;
		case 2: return Facing.SOUTH;
		case 3: return Facing.WEST;
		default: return Facing.NORTH;
		}
	}
	
	public void setFace(BlockPos pos, Facing face) {
		switch (face) {
		case NORTH: setData(pos, 0); return;
		case EAST:  setData(pos, 1); return;
		case SOUTH: setData(pos, 2); return;
		case WEST:  setData(pos, 3); return;
		default: throw new IllegalArgumentException("Invailed face: " + face);
		}
	}

	@Override
	public String getKey() {
		return KEY;
	}
}
