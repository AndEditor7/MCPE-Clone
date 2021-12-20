package com.andedit.arcubit.blocks;

import com.andedit.arcubit.blocks.materials.Material;
import com.andedit.arcubit.blocks.models.SnowLayerModel;
import com.andedit.arcubit.blocks.utils.BlockType;

public class SnowLayerBlock extends Block {
	SnowLayerBlock() {
		this.hardness = 0.1f;
		this.material = Material.SNOW;
		this.blockType = BlockType.SNOW;
		this.model = new SnowLayerModel(this);
	}
	
	@Override
	public String getName(int type) {
		return "Snow";
	}
}
