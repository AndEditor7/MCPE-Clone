package com.andedit.arcubit.blocks;

import static com.andedit.arcubit.world.World.world;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.blocks.data.LiquidComp;
import com.andedit.arcubit.blocks.materials.Material;
import com.andedit.arcubit.blocks.models.LiquidModel;
import com.andedit.arcubit.blocks.utils.UpdateState;
import com.andedit.arcubit.items.tools.ToolType;
import com.andedit.arcubit.utils.BlockPos;
import com.andedit.arcubit.utils.Facing;
import com.andedit.arcubit.utils.PlaceType;

public class WaterBlock extends LiquidBlock {
	
	public static final LiquidComp comp = new LiquidComp(7);

	WaterBlock() {
		setName("Water");
		model = new LiquidModel(this, Assets.getBlockReg(13, 12));
		material = Material.WATER;
		manager.addCompoment(comp);
		setTexture(Assets.getBlockReg(13, 12));
		setMining(ToolType.NONE, -1, 10f);
	}
	
	private static final BlockPos pos = new BlockPos();
	public static void intsBlock(int x, int y, int z) {
		world.setBlock(x, y, z, Blocks.WATER);
		comp.setLevel(pos.set(x, y, z), 7);
		comp.setSource(pos, true);
	}

	@Override
	public boolean isFull(BlockPos pos) {
		return comp.isSource(pos);
	}
	
	@Override
	public void onNeighbourUpdate(BlockPos primaray, BlockPos secondary, Facing face, UpdateState state) {
		if (secondary.getBlock() == Blocks.LAVA && face == Facing.DOWN) {
			world.setBlock(primaray, Blocks.STONE, PlaceType.SET);
			return;
		}
		addQue(primaray.clone());
	}
	
	@Override
	public int getMaxFlow() {
		return comp.maxLevel;
	}
	
	@Override
	public void addQue(BlockPos pos) {
		world.addWaterQue(pos);
	}
	
	@Override
	public LiquidComp getComp() {
		return comp;
	}
} 