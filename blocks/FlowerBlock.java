package com.andedit.arcubit.blocks;

import static com.andedit.arcubit.world.World.world;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.blocks.materials.Material;
import com.andedit.arcubit.blocks.models.DiagonalModel;
import com.andedit.arcubit.blocks.utils.BlockType;
import com.andedit.arcubit.blocks.utils.UpdateState;
import com.andedit.arcubit.items.tools.ToolType;
import com.andedit.arcubit.utils.BlockPos;
import com.andedit.arcubit.utils.Facing;
import com.andedit.arcubit.utils.PlaceType;

public class FlowerBlock extends Block {

	FlowerBlock() {
		this.model = new DiagonalModel(this, Assets.getBlockReg(12, 0));
		this.material = Material.DIAGONAL;
		this.setMining(ToolType.NONE, 0, 0f);
		this.blockType = BlockType.PLANT;
	}
	
	@Override
	public boolean onPlace(BlockPos pos, int type, PlaceType place) {
		final BlockPos offset = pos.offset(Facing.DOWN);
		if (world.getBlock(offset).getBlockType(offset).isSoil()) {
			return super.onPlace(pos, type, place);
		}
		return false;
	}

	@Override
	public void onNeighbourUpdate(BlockPos primaray, BlockPos secondary, Facing face, UpdateState state) {
		final BlockPos offset = primaray.offset(Facing.DOWN);
		if (!world.getBlock(offset).getBlockType(offset).isSoil()) {
			world.setAir(primaray, PlaceType.SET, true);
		}
	}
	
	@Override
	public String getName(int type) {
		return "Flower";
	}
}
