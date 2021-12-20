package com.andedit.arcubit.blocks;

import static com.andedit.arcubit.world.World.world;

import com.andedit.arcubit.blocks.data.LiquidComp;
import com.andedit.arcubit.blocks.utils.BlockType;
import com.andedit.arcubit.blocks.utils.UpdateState;
import com.andedit.arcubit.entity.Entity;
import com.andedit.arcubit.utils.BlockPos;
import com.andedit.arcubit.utils.Facing;
import com.andedit.arcubit.utils.PlaceType;

public abstract class LiquidBlock extends Block {
	{
		blockType = BlockType.LIQUID;
	}
	
	@Override
	public void inBlockHandle(BlockPos pos, Entity entity) {
		final int level = getComp().getLevel(pos);
		for (int i = 0; i < 4; i++) {
			Facing face = Facing.get(i+2);
			BlockPos offset = pos.offset(face);
			
			if (world.getBlock(offset) == this) {
				int lvl = getComp().getLevel(offset);
				BlockPos value = face.offset;
				if (lvl < level) {
					entity.xFlow += value.x;
					entity.zFlow += value.z;
				} else if (lvl > level) {
					entity.xFlow -= value.x;
					entity.zFlow -= value.z;
				}
			}
		}
	}
	
	public boolean intsLiquid(BlockPos pos) {
		if (pos.getBlock().getMaterial().hasCollision()) return false;
		world.setBlock(pos, this, PlaceType.PLACE);
		getComp().setLevel(pos, getMaxFlow());
		getComp().setSource(pos, true);
		addQue(pos.clone());
		return true;
	}
	
	public abstract boolean isFull(BlockPos pos);
	
	public abstract LiquidComp getComp();
	
	public abstract int getMaxFlow();
	
	public abstract void addQue(BlockPos pos);
}
