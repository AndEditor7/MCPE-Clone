package com.andedit.arcubit.blocks;

import static com.andedit.arcubit.world.World.world;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.blocks.data.StageComp;
import com.andedit.arcubit.blocks.materials.Material;
import com.andedit.arcubit.blocks.models.DiagonalModel;
import com.andedit.arcubit.blocks.utils.BlockType;
import com.andedit.arcubit.items.tools.ToolType;
import com.andedit.arcubit.utils.BlockPos;
import com.andedit.arcubit.utils.Facing;
import com.andedit.arcubit.utils.PlaceType;
import com.andedit.arcubit.world.gen.CommonFeatures;

public class SablingBlock extends Block {

	public final StageComp component = new StageComp(1);
	
	SablingBlock() {
		this.model = new DiagonalModel(this, Assets.getBlockReg(15, 0));
		this.material = Material.DIAGONAL;
		this.setMining(ToolType.NONE, 0, 0);
		this.manager.addCompoment(component);
		this.blockType = BlockType.PLANT;
	}
	
	@Override
	public String getName(int type) {
		return "Sabling";
	}
	
	@Override
	public void onRandonTickUpdate(BlockPos pos) {
		if (!isSutiable(pos.offset(Facing.DOWN))) {
			world.setAir(pos, PlaceType.SET, true);
			return;
		}
		
		if (component.getStage(pos) == 1) {
			CommonFeatures.tree(true, pos.x, pos.y, pos.z, PlaceType.SET);
		} else {
			component.setStage(pos, 1);
		}
	}
	
	@Override
	public boolean onPlace(BlockPos pos, int type, PlaceType place) {
		if (place == PlaceType.PLACE && !isSutiable(pos.offset(Facing.DOWN))) {
			return false;
		}
		
		return super.onPlace(pos, type, place);
	}
	
	private static boolean isSutiable(BlockPos pos) {
		final Block block = world.getBlock(pos);
		return block.material.isFullCube() && block.getBlockType(pos).isSoil();
	}
}
