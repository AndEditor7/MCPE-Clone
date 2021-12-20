package com.andedit.arcubit.blocks.data;

import com.andedit.arcubit.utils.BlockPos;
import com.badlogic.gdx.math.MathUtils;

public class TypeComp extends DataComp {
	
	public static final String KEY = "type";
	
	private final int length;
	
	public TypeComp(int size) {
		super(size, false);
		length = size;
	}
	
	public int getType(BlockPos pos) {
		return MathUtils.clamp(getData(pos), 0, length);
	}
	
	public void setType(BlockPos pos, int type) {
		setData(pos, MathUtils.clamp(type, 0, length));
	}

	@Override
	public String getKey() {
		return KEY;
	}
}
