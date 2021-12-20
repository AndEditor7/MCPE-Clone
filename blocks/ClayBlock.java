package com.andedit.arcubit.blocks;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.blocks.materials.Material;
import com.andedit.arcubit.blocks.models.CubeModel;
import com.andedit.arcubit.blocks.utils.CubeTex;
import com.andedit.arcubit.blocks.utils.BlockType;
import com.andedit.arcubit.items.BasicItem;
import com.andedit.arcubit.items.Item;
import com.andedit.arcubit.items.tools.ToolType;
import com.andedit.arcubit.utils.BlockPos;
import com.badlogic.gdx.utils.Array;

public class ClayBlock extends Block {

	ClayBlock() {
		this.model = new CubeModel(this, new CubeTex(Assets.getBlockReg(8, 4)));
		this.material = Material.BLOCK;
		this.setTexture(Assets.getItemReg(10, 0));
		this.setMining(ToolType.SHOVEL, 0, 0.6f);
		this.blockType = BlockType.CLAY;
	}
	
	@Override
	public Array<Item> getDropItem(BlockPos pos) {
		return dropList(new BasicItem(BasicItem.CLAY));
	}
	
	@Override
	public String getName(int type) {
		return "Clay";
	}
}
