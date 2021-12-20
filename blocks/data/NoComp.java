package com.andedit.arcubit.blocks.data;

import com.andedit.arcubit.utils.BlockPos;

public class NoComp extends TypeComp {

	public NoComp() {
		super(1);
	}

	@Override
	public int getType(BlockPos pos) {
		return 0;
	}
	
	@Override
	public void setType(BlockPos pos, int type) {
	}
}
