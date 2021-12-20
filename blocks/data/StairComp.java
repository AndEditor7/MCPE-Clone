package com.andedit.arcubit.blocks.data;

import com.andedit.arcubit.utils.BlockPos;
import com.badlogic.gdx.math.MathUtils;

public class StairComp extends DataComp {
	
	public static final String KEY = "stair";

	public StairComp() {
		super(4, true);
	}
	
	public boolean isFlip(BlockPos pos) {
		return (getData(pos) & 1) == 1;
	}
	
	public Shape getState(BlockPos pos) {
		return Shape.get(getData(pos)>>>1);
	}
	
	public void setFlip(BlockPos pos, boolean isFlip) {
		setData(pos, isFlip?1:0, ~1);
	}
	
	public void setState(BlockPos pos, Shape state) {
		setData(pos, state.ordinal()<<1, 1);
	}

	@Override
	public String getKey() {
		return KEY;
	}
	
	public static enum Shape {
		STRAIGHT,
		INNER_LEFT, INNER_RIGHT,
		OUTER_LEFT, OUTER_RIGHT;
		
		public boolean isLeft() {
			return this == INNER_LEFT || this == OUTER_LEFT;
		}
		
		public boolean isRight() {
			return this == INNER_RIGHT || this == OUTER_RIGHT;
		}
		
		public boolean isInner() {
			return this == INNER_LEFT || this == INNER_RIGHT;
		}
		
		public boolean isOuter() {
			return this == OUTER_LEFT || this == OUTER_RIGHT;
		}
		
		private static final Shape[] ARRAY = values();
		public static Shape get(int ordinal) {
			return ARRAY[MathUtils.clamp(ordinal, 0, 4)];
		}
		
		public static Shape get(boolean isInner, boolean isRight) {
			if (isInner) {
				if (isRight) {
					return INNER_RIGHT;
				} else {
					return INNER_LEFT;
				}
			} else {
				if (isRight) {
					return OUTER_RIGHT;
				} else {
					return OUTER_LEFT;
				}
			}
		}
	}
}
