package com.andedit.arcubit.blocks.data;

import static com.andedit.arcubit.world.World.world;

import com.andedit.arcubit.utils.BlockPos;
import com.andedit.arcubit.utils.Util;
import com.badlogic.gdx.math.MathUtils;

/** Data Component.  */
public abstract class DataComp {
	/** The size of bits allocated. */
	final int size;
	
	/**  */
	private int offset, dataAnd, dataInv;
	
	/** @param bitSets true for bits size allocation. Else false for number size for allocation.  */
	DataComp(int size, boolean bitSets) {
		if (bitSets) {
			this.size = size;
		} else {
			final int pow = MathUtils.nextPowerOfTwo(size);
			int result = 0;
			for (int i = 0; i < 16; i++) {
				if (pow == 1<<i) {
					result = i+1;
					break;
				}
			}
			if (result == 0) throw new IllegalArgumentException();
			this.size = result;
		}
	}
	
	final void genData(int offset) {
		this.offset = offset;
		dataAnd = Util.createANDbits(size)<<offset;
		dataInv = ~dataAnd;
	}
	
	final int getData(BlockPos pos) {
		return (world.getData(pos) & dataAnd) >>> offset;
	}
	
	final void setData(BlockPos pos, int data) {
		world.setData(pos, (world.getData(pos) & dataInv) | (data << offset));
	}
	
	/** @param bits to select not to override. */
	final void setData(BlockPos pos, int data, int bits) {
		world.setData(pos, (world.getData(pos) & (dataInv | (bits << offset))) | (data << offset));
	}
	
	/** Get default key. */
	public abstract String getKey();
}
