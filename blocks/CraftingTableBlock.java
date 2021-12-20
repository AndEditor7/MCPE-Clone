package com.andedit.arcubit.blocks;

import static com.andedit.arcubit.Assets.getBlockReg;
import static com.andedit.arcubit.TheGame.game;
import static com.andedit.arcubit.ui.Crafting.crafting;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.Steve;
import com.andedit.arcubit.blocks.models.CubeModel;
import com.andedit.arcubit.blocks.utils.CubeTex;
import com.andedit.arcubit.blocks.utils.BlockType;
import com.andedit.arcubit.items.tools.ToolType;
import com.andedit.arcubit.ui.Crafting;
import com.andedit.arcubit.utils.BlockPos;

public class CraftingTableBlock extends Block {

	CraftingTableBlock() {
		this.model = new CubeModel(this, new CubeTex(getBlockReg(11, 2), getBlockReg(11, 3), getBlockReg(12, 3), getBlockReg(4, 0)));
		setTexture(Assets.getItemReg(9, 4));
		this.setMining(ToolType.AXE, 0, 2.5f);
		this.blockType = BlockType.WOOD;
	}

	@Override
	public String getName(int data) {
		return "Work Bench";
	}
	
	@Override
	public boolean onClick(BlockPos pos, Steve steve) {
		if (game.isSurvival) {
			crafting.is3x3Grid(true);
			game.manager.setUI(Crafting.class);
			return true;
		}
		return false;
	}
}
