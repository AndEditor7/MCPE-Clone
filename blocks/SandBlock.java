package com.andedit.arcubit.blocks;

import static com.andedit.arcubit.world.World.world;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.blocks.materials.Material;
import com.andedit.arcubit.blocks.models.CubeModel;
import com.andedit.arcubit.blocks.utils.CubeTex;
import com.andedit.arcubit.blocks.utils.BlockType;
import com.andedit.arcubit.blocks.utils.UpdateState;
import com.andedit.arcubit.entity.FallingBlock;
import com.andedit.arcubit.items.tools.ToolType;
import com.andedit.arcubit.utils.BlockPos;
import com.andedit.arcubit.utils.Facing;
import com.andedit.arcubit.utils.PlaceType;

public class SandBlock extends Block {

	SandBlock() {
		this.model = new CubeModel(this, new CubeTex(Assets.getBlockReg(2, 1)));
		this.material = Material.BLOCK;
		this.setTexture(Assets.getItemReg(14, 0));
		this.setMining(ToolType.SHOVEL, 0, 0.5f);
		this.blockType = BlockType.SAND;
	}
	
	@Override
	public String getName(int type) {
		return "Sand";
	}
	
	@Override
	public void onNeighbourUpdate(BlockPos primaray, BlockPos secondary, Facing face, UpdateState state) {
		world.addUpdateQue(primaray.clone());
	}
	
	@Override
	public boolean onPlace(BlockPos pos, int type, PlaceType place) {
		if (super.onPlace(pos, type, place)) {
			world.addUpdateQue(pos.clone());
			return true;
		}
		return false;
	}
	
	@Override
	public void onUpdateTick(BlockPos pos) {
		if (FallingBlock.isFallable(pos.offset(Facing.DOWN))) {
			world.addEntity(FallingBlock.newEntity(pos));
			world.setAir(pos, PlaceType.SET, false);
		}
	}
}
