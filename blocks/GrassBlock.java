package com.andedit.arcubit.blocks;

import static com.andedit.arcubit.world.World.world;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.blocks.materials.Material;
import com.andedit.arcubit.blocks.models.GrassModel;
import com.andedit.arcubit.blocks.utils.BlockType;
import com.andedit.arcubit.blocks.utils.BlockUtils;
import com.andedit.arcubit.items.BlockItem;
import com.andedit.arcubit.items.Item;
import com.andedit.arcubit.items.tools.ToolType;
import com.andedit.arcubit.utils.BlockPos;
import com.andedit.arcubit.utils.Facing;
import com.andedit.arcubit.utils.PlaceType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class GrassBlock extends Block {
	
	private static final Array<BlockPos> ARRAY = new Array<>();

	GrassBlock() {
		this.model = new GrassModel(this);
		this.material = Material.BLOCK;
		this.setTexture(Assets.getItemReg(9, 0));
		this.setMining(ToolType.SHOVEL, 0, 0.6f);
		this.blockType = BlockType.GRASS;
	}
	
	@Override
	public String getName(int type) {
		return "Grass";
	}
	
	@Override
	public Array<Item> getDropItem(BlockPos pos) {
		return dropList(new BlockItem(Blocks.DIRT));
	}
	
	@Override
	public void onRandonTickUpdate(BlockPos pos) {
		final BlockPos offset = pos.offset(Facing.UP);
		
		if (world.getBlock(offset).getMaterial().isFullCube()) {
			world.setBlock(pos, Blocks.DIRT, 0, PlaceType.SET);
			return;
		}
		
		ARRAY.size = 0;
		for (int x = -1; x < 2; x++)
		for (int y = -1; y < 2; y++)
		for (int z = -1; z < 2; z++) {
			if (Blocks.get(world.getData(pos.x+x, pos.y+y, pos.z+z)) == Blocks.DIRT) {
				final int data = world.getData(pos.x+x, pos.y+y+1, pos.z+z);
				if (!Blocks.get(data).getMaterial().isFullCube() && BlockUtils.maxLight(data) >= 9) {
					ARRAY.add(pos.offset(x, y, z));
				}
			}
		}
		
		if (ARRAY.notEmpty() && MathUtils.randomBoolean(0.5f)) {
			world.setBlock(ARRAY.random(), Blocks.GRASS, 0, PlaceType.SET);
		}
	}
}
