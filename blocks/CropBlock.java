package com.andedit.arcubit.blocks;

import static com.andedit.arcubit.world.World.world;

import com.andedit.arcubit.blocks.materials.Material;
import com.andedit.arcubit.blocks.models.CropModel;
import com.andedit.arcubit.blocks.utils.BlockType;
import com.andedit.arcubit.items.BasicItem;
import com.andedit.arcubit.items.Item;
import com.andedit.arcubit.items.SeedItem;
import com.andedit.arcubit.items.tools.ToolType;
import com.andedit.arcubit.utils.BlockPos;
import com.andedit.arcubit.utils.Facing;
import com.andedit.arcubit.utils.PlaceType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class CropBlock extends Block {

	CropBlock() {
		this.model = new CropModel(this);
		this.newTypeComponent(7);
		this.material = Material.DIAGONAL;
		this.setMining(ToolType.NONE, 0, 0f);
		this.blockType = BlockType.PLANT;
	}
	
	@Override
	public boolean onPlace(BlockPos pos, int type, PlaceType place) {
		if (place != PlaceType.PLACE) {
			return super.onPlace(pos, type, place);
		}
		
		if (world.getBlock(pos.offset(Facing.DOWN)) == Blocks.FARMLAND) {
			return super.onPlace(pos, type, place);
		}
		
		return false;
	}
	
	@Override
	public void onRandonTickUpdate(BlockPos pos) {
		if (world.getBlock(pos.offset(Facing.DOWN)) == Blocks.FARMLAND) {
			int stage = getType(pos);
			if (stage < 7) {
				setType(pos, stage+1);
				world.dirty(pos.x, pos.y, pos.z);
			}
		} else {
			world.setAir(pos, PlaceType.SET, true);
		}
	}
	
	@Override
	public Array<Item> getDropItem(BlockPos pos) {
		final Array<Item> list = dropList(new SeedItem().size(MathUtils.random(1, Math.max(getType(pos)/2, 1))));
		if (getType(pos) == 7) list.add(new BasicItem(BasicItem.WHEAT));
		return list;
	}

	@Override
	public String getName(int type) {
		return "Crop";
	}

}
