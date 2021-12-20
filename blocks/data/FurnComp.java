package com.andedit.arcubit.blocks.data;

import com.andedit.arcubit.utils.BlockPos;

public class FurnComp extends DataComp {
	
	public static final String KEY = "furn";

	public FurnComp() {
		super(1, true);
	}
	
	public boolean isActive(BlockPos pos) {
		return getData(pos) == 1;
	}
	
	public void setActive(BlockPos pos, boolean bool) {
		setData(pos, bool?1:0);
	}
	
	@Override
	public String getKey() {
		return KEY;
	}

}
