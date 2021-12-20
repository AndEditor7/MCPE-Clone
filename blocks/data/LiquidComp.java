package com.andedit.arcubit.blocks.data;

import com.andedit.arcubit.utils.BlockPos;

public class LiquidComp extends DataComp {
	
	public static final String KEY = "liquid";
	
	public final int maxLevel;

	public LiquidComp(int maxLevel) {
		super(4, true);
		this.maxLevel = maxLevel;
	}
	
	public boolean isSource(BlockPos pos) {
		return (getData(pos) & 1) == 1;
	}
	
	public boolean isFalling(BlockPos pos) {
		return !isSource(pos) && getLevel(pos) == maxLevel;
	}
	
	public int getLevel(BlockPos pos) {
		return Math.min(getData(pos) >>> 1, maxLevel); 
	}
	
	public void setSource(BlockPos pos, boolean isSource) {
		setData(pos, isSource?1:0, 14);
	}
	
	public void setLevel(BlockPos pos, int level) {
		setData(pos, level<<1, 1);
	}

	@Override
	public String getKey() {
		return KEY;
	}
}
