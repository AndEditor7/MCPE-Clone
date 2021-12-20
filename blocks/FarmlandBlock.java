package com.andedit.arcubit.blocks;

import static com.andedit.arcubit.Assets.getBlockReg;
import static com.andedit.arcubit.world.World.world;

import com.andedit.arcubit.blocks.models.CubeModel;
import com.andedit.arcubit.blocks.utils.CubeTex;
import com.andedit.arcubit.blocks.utils.BlockType;
import com.andedit.arcubit.blocks.utils.UpdateState;
import com.andedit.arcubit.items.BlockItem;
import com.andedit.arcubit.items.Item;
import com.andedit.arcubit.items.tools.ToolType;
import com.andedit.arcubit.utils.ArrayDir;
import com.andedit.arcubit.utils.BlockPos;
import com.andedit.arcubit.utils.Facing;
import com.andedit.arcubit.utils.PlaceType;
import com.badlogic.gdx.utils.Array;

public class FarmlandBlock extends Block {

	FarmlandBlock() {
		final ArrayDir<CubeTex> blockTexs = new ArrayDir<>(CubeTex.class, 2);
		blockTexs.put(0, new CubeTex(getBlockReg(7, 5), getBlockReg(2, 0), getBlockReg(2, 0)));
		blockTexs.put(1, new CubeTex(getBlockReg(6, 5), getBlockReg(2, 0), getBlockReg(2, 0)));
		this.model = new CubeModel(this, blockTexs);
		
		newTypeComponent(1);
		this.blockType = BlockType.DIRT;
		this.setMining(ToolType.SHOVEL, 0, 0.6f);
	}
	
	@Override
	public Array<Item> getDropItem(BlockPos pos) {
		return dropList(new BlockItem(Blocks.DIRT));
	}

	@Override
	public String getName(int type) {
		return "Farmland";
	}
	
	@Override
	public void onNeighbourUpdate(BlockPos primaray, BlockPos secondary, Facing face, UpdateState state) {
		final BlockPos offset = primaray.offset(Facing.UP);
		final Block block = world.getBlock(offset);
		if (block != Blocks.AIR && block.getBlockType(offset) != BlockType.PLANT) {
			world.setBlock(primaray, Blocks.DIRT, PlaceType.SET);
		}
	}
	
	@Override
	public void onRandonTickUpdate(BlockPos pos) {
		final BlockPos off = BlockPos.newBlockPos();
		for (int x = -4; x < 5; x++)
		for (int z = -4; z < 5; z++) {
			if (world.getBlock(off.set(pos.x+x, pos.y, pos.z+z)) == Blocks.WATER) {
				if (getType(pos) == 0) world.setBlock(pos, this, 1, PlaceType.SET);
				return;
			}
		}
		
		if (world.getBlock(pos.offset(Facing.UP)) != Blocks.CROP) {
			world.setBlock(pos, Blocks.DIRT, PlaceType.SET);
		}
	}
}
