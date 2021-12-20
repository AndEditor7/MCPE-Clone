package com.andedit.arcubit.blocks;

import com.andedit.arcubit.blocks.materials.Material;
import com.andedit.arcubit.blocks.utils.BlockType;
import com.andedit.arcubit.blocks.utils.UpdateState;
import com.andedit.arcubit.utils.BlockPos;
import com.andedit.arcubit.utils.Facing;

public class AirBlock extends Block {
	
	AirBlock() {
		this.material = Material.AIR;
		this.blockType = BlockType.AIR;
	}
	
	@Override
	public boolean isAir() {
		return true;
	}

	@Override
	public String getName(int type) {
		return "Air";
	}
	
	@Override
	public void onNeighbourUpdate(BlockPos primaray, BlockPos secondary, Facing face, UpdateState state) {
	}
}
