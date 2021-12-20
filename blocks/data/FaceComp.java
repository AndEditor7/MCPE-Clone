package com.andedit.arcubit.blocks.data;

import com.andedit.arcubit.utils.BlockPos;
import com.andedit.arcubit.utils.Facing;

public class FaceComp extends DataComp {
	
	public static final String KEY = "face";
	
	public FaceComp() {
		super(5, false);
	}
	
	public Facing getFace(BlockPos pos) {
		return Facing.get(getData(pos));
	}
	
	public void setFace(BlockPos pos, Facing face) {
		setData(pos, face.ordinal());
	}

	@Override
	public String getKey() {
		return KEY;
	}

}
