package com.andedit.arcubit.blocks;

import static com.andedit.arcubit.world.World.world;

import com.andedit.arcubit.blocks.materials.Material;
import com.andedit.arcubit.blocks.models.CactusModel;
import com.andedit.arcubit.blocks.utils.BlockType;
import com.andedit.arcubit.blocks.utils.UpdateState;
import com.andedit.arcubit.handles.RayContext;
import com.andedit.arcubit.utils.BlockPos;
import com.andedit.arcubit.utils.Facing;
import com.andedit.arcubit.utils.PlaceType;

public class CactusBlock extends Block {
	CactusBlock() {
		setName("Cactus");
		blockType = BlockType.CACTUS;
		model = new CactusModel(this);
		material = Material.CACTUS;
		hardness = 0.4f;
	}
	
	@Override
	public boolean onPlace(RayContext context, int type) {
		if (isSuitable(context.out)) {
			return onPlace(context.out, 0, PlaceType.PLACE);
		}
		return false;
	}
	
	@Override
	public void onRandonTickUpdate(BlockPos pos) {
		if (!isSuitable(pos)) {
			pos.setAir(true);
			return;
		}
		
		for (int i = 1; i < 3; i++) {
			if (world.getBlock(pos.x, pos.y-i, pos.z) != this) {
				world.setBlock(pos.offset(Facing.UP), this, PlaceType.SET);
				return;
			}
		}
	}
	
	@Override
	public void onNeighbourUpdate(BlockPos primaray, BlockPos secondary, Facing face, UpdateState state) {
		if (!isSuitable(primaray)) {
			primaray.setAir(true);
		}
	}
	
	private boolean isSuitable(BlockPos pos) {
		BlockPos offset = pos.offset(Facing.DOWN);
		Block block = offset.getBlock();
		if (block.getBlockType(offset) == BlockType.SAND || block == this) {
			return pos.offset(Facing.NORTH).getBlock().isAir() &&
				pos.offset(Facing.EAST).getBlock().isAir() &&
				pos.offset(Facing.SOUTH).getBlock().isAir() &&
				pos.offset(Facing.WEST).getBlock().isAir();
		}
		return false;
	}
}
