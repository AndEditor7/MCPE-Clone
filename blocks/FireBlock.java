package com.andedit.arcubit.blocks;

import static com.andedit.arcubit.world.World.world;

import com.andedit.arcubit.blocks.materials.Material;
import com.andedit.arcubit.blocks.models.FireModel;
import com.andedit.arcubit.blocks.utils.BlockType;
import com.andedit.arcubit.blocks.utils.UpdateState;
import com.andedit.arcubit.items.Item;
import com.andedit.arcubit.utils.BlockPos;
import com.andedit.arcubit.utils.Facing;
import com.andedit.arcubit.utils.PlaceType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class FireBlock extends Block {
	
	private static final int SIZE = 3;
	private static final int MASK = SIZE/2;
	private static final Array<BlockPos> LIST = new Array<>(64);
	
	FireBlock() {
		setName("Fire");
		blockType = BlockType.FIRE;
		material = Material.DIAGONAL;
		model = new FireModel(this);
		hardness = 0;
		lightLevel = 15;
	}
	
	@Override
	public boolean onPlace(BlockPos pos, int type, PlaceType place) {
		if (!isSuitable(pos)) return false;
		
		if (super.onPlace(pos, type, place)) {
			world.fireTick.add(pos.clone());
			return true;
		}
		
		return false;
	}

	@Override
	public void onNeighbourUpdate(BlockPos pos, BlockPos secondary, Facing face, UpdateState state) {
		if (isSuitable(pos)) return;
		pos.setAir(false);
	}
	
	@Override
	public void onRandonTickUpdate(BlockPos pos) {
		
	}
	
	@Override
	public Array<Item> getDropItem(BlockPos pos) {
		return noDrop();
	}
	
	public static boolean update(BlockPos pos) {
		BlockPos offset = BlockPos.newBlockPos(), select;
		boolean remove = false;
		if (MathUtils.randomBoolean(0.6f)) {
			LIST.size = 0;
			for (int i = 0; i < Facing.SIZE; i++) {
				offset = pos.offset(Facing.get(i));
				
				if (offset.getBlock().isFlamable(offset)) {
					LIST.add(offset);
				}
			}
			
			select = LIST.random();
			if (select != null) {
				select.setAir(false);
			}
			
			if (LIST.size <= 1) {
				pos.setAir(false);
				remove = true;
			}
			
			if (MathUtils.randomBoolean(0.6f)) {
				pos.setAir(false);
				return true;
			}
		}
		
		LIST.size = 0;
		for (int x = -MASK; x < SIZE; x++)
		for (int y = -MASK; y < 6; y++)
		for (int z = -MASK; z < SIZE; z++) {
			if (world.getBlock(offset.set(pos.x+x, pos.y+y, pos.z+z)).isFlamable(offset)) {
				LIST.add(pos.offset(x, y, z));
			}
		}
		
		select = LIST.random();
		if (select == null) {
			pos.setAir(false);
			return true;
		}
		
		LIST.size = 0;
		for (int i = 0; i < Facing.SIZE; i++) {
			offset = select.offset(Facing.get(i));
			
			if (offset.isAir()) {
				LIST.add(offset);
			}
		}
		
		select = LIST.random();
		if (select == null) {
			return remove;
		}
		
		world.setBlock(select, Blocks.FIRE, PlaceType.SET);
		return remove;
	}

	private static boolean isSuitable(BlockPos pos) {
		if (pos.offset(Facing.DOWN).getBlock().isFaceSolid(pos.offset(Facing.DOWN), Facing.UP)) {
			return true;
		}

		BlockPos offset;
		if ((offset = pos.offset(Facing.NORTH)).getBlock().isFlamable(offset) ||
			(offset = pos.offset(Facing.EAST)).getBlock().isFlamable(offset) ||
			(offset = pos.offset(Facing.SOUTH)).getBlock().isFlamable(offset) ||
			(offset = pos.offset(Facing.WEST)).getBlock().isFlamable(offset) ||
			(offset = pos.offset(Facing.UP)).getBlock().isFlamable(offset)) {
			return true;
		}
		
		return false;
	}
}
