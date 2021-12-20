package com.andedit.arcubit.blocks.data;

import com.andedit.arcubit.utils.BlockPos;

public class DoorComp extends DataComp {
	
	public static final String KEY = "door";

	// isOpen is a first bit.
	// isUpper is a second bit.
	public DoorComp() {
		super(2, true);
	}
	
	public boolean isOpen(BlockPos pos) {
		return (getData(pos)&1) != 0;
	}
	
	public boolean isUpper(BlockPos pos) {
		return (getData(pos)&2) != 0;
	}
	
	public void setIsOpen(BlockPos pos, boolean isOpen) {
		setData(pos, isOpen?1:0, 2);
	}
	
	public void setIsUpper(BlockPos pos, boolean isUpper) {
		setData(pos, isUpper?2:0, 1);
	}

	@Override
	public String getKey() {
		return KEY;
	}

}
