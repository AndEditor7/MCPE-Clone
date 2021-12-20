package com.andedit.arcubit.blocks;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.blocks.models.CubeModel;
import com.andedit.arcubit.blocks.utils.CubeTex;
import com.andedit.arcubit.items.BlockItem;
import com.andedit.arcubit.items.Item;
import com.andedit.arcubit.items.tools.ToolType;
import com.andedit.arcubit.utils.BlockPos;
import com.badlogic.gdx.utils.Array;

public class StoneBlock extends Block {

	StoneBlock() {
		setName("Stone");
		model = new CubeModel(this, new CubeTex(Assets.getBlockReg(1, 0)));
		setTexture(Assets.getItemReg(7, 0));
		setMining(ToolType.PICKAXE, 1, 1.5f);
	}
	
	@Override
	public Array<Item> getDropItem(BlockPos pos) {
		return dropList(new BlockItem(Blocks.COBBLESTONE));
	}
}