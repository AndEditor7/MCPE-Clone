package com.andedit.arcubit.blocks.data;

import com.andedit.arcubit.utils.BlockPos;

public class StageComp extends DataComp {

	public static final String KEY = "stage";
	
	public StageComp(int stages) {
		super(stages, false);
	}
	
	public int getStage(BlockPos pos) {
		return getData(pos);
	}
	
	public void setStage(BlockPos pos, int stages) {
		setData(pos, stages);
	}

	@Override
	public String getKey() {
		return KEY;
	}

}
