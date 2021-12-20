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

public class LavaBlock extends LiquidBlock {

	public static final LiquidComp comp = new LiquidComp(4);
	
	LavaBlock() {
		setName("Lava");
		lightLevel = 15;
		model = new LiquidModel(this, Assets.getBlockReg(13, 14));
		material = Material.LAVA;
		manager.addCompoment(comp);
		setTexture(Assets.getBlockReg(13, 14));
		setMining(ToolType.NONE, -1, 10f);
	}
	
	@Override
	public boolean onPlace(BlockPos pos, int type, PlaceType place) {
		if (super.onPlace(pos, type, place)) {
			checkTurn(pos);
			return true;
		}
		
		return false;
	}
	
	private void checkTurn(BlockPos pos) {
		if (isTouch(pos.offset(Facing.UP))) {
			world.setBlock(pos, getTurn(pos), PlaceType.SET);
			return;
		}
		for (int i = 2; i < Facing.SIZE; i++) {
			if (isTouch(pos.offset(Facing.get(i)))) {
				world.setBlock(pos, getTurn(pos), PlaceType.SET);
				return;
			}
		}
	}
	
	private boolean isTouch(BlockPos pos) {
		Block block = pos.getBlock();
		return block instanceof LiquidBlock && block != this;
	}
	
	private Block getTurn(BlockPos pos) {
		return isFull(pos) ? Blocks.OBSIDIAN : Blocks.COBBLESTONE;
	}
	
	@Override
	public boolean isFull(BlockPos pos) {
		return comp.isSource(pos);
	}
	
	@Override
	public void onNeighbourUpdate(BlockPos primaray, BlockPos secondary, Facing face, UpdateState state) {
		if (secondary.getBlock() == Blocks.WATER) {
			world.setBlock(primaray, isFull(primaray) ? Blocks.OBSIDIAN : Blocks.COBBLESTONE, PlaceType.SET);
			return;
		}
		addQue(primaray.clone());
	}

	@Override
	public LiquidComp getComp() {
		return comp;
	}

	@Override
	public int getMaxFlow() {
		return comp.maxLevel;
	}

	@Override
	public void addQue(BlockPos pos) {
		world.addLavaQue(pos);
	}
	
	private static final BlockPos pos = new BlockPos();
	public static void intsBlock(int x, int y, int z) {
		world.setBlock(x, y, z, Blocks.LAVA);
		comp.setLevel(pos.set(x, y, z), Blocks.LAVA.getMaxFlow());
		comp.setSource(pos, true);
	}
}
