package com.andedit.arcubit.blocks.data;

import com.andedit.arcubit.utils.BlockPos;

public class SlabComp extends DataComp {
	
	public static final String KEY = "slab";

	public SlabComp() {
		super(2, true);
	}
	
	public boolean isUpper(BlockPos pos) {
		return getData(pos) == 1;
	}
	
	public boolean isFullBlock(BlockPos pos) {
		return getData(pos) == 2;
	}
	
	public void setUpper(BlockPos pos, boolean isUpper) {
		setData(pos, isUpper?1:0);
	}
	
	public void setFullBlock(BlockPos pos) {
		setData(pos, 2);
	}

	@Override
	public String getKey() {
		return KEY;
	}
}
