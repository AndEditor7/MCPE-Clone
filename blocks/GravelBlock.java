package com.andedit.arcubit.blocks;

import static com.andedit.arcubit.world.World.world;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.blocks.materials.Material;
import com.andedit.arcubit.blocks.models.CubeModel;
import com.andedit.arcubit.blocks.utils.CubeTex;
import com.andedit.arcubit.blocks.utils.UpdateState;
import com.andedit.arcubit.entity.FallingBlock;
import com.andedit.arcubit.blocks.utils.BlockType;
import com.andedit.arcubit.items.BasicItem;
import com.andedit.arcubit.items.Item;
import com.andedit.arcubit.items.tools.ToolType;
import com.andedit.arcubit.utils.BlockPos;
import com.andedit.arcubit.utils.Facing;
import com.andedit.arcubit.utils.PlaceType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class GravelBlock extends Block {

	GravelBlock() {
		this.model = new CubeModel(this, new CubeTex(Assets.getBlockReg(3, 1)));
		this.material = Material.BLOCK;
		this.setTexture(Assets.getItemReg(15, 0));
		this.setMining(ToolType.SHOVEL, 0, 0.6f);
		this.blockType = BlockType.GRAVEL;
	}
	
	@Override
	public String getName(int type) {
		return "Gravel";
	}
	
	@Override
	public void onNeighbourUpdate(BlockPos primaray, BlockPos secondary, Facing face, UpdateState state) {
		world.addUpdateQue(primaray.clone());
	}
	
	@Override
	public boolean onPlace(BlockPos pos, int type, PlaceType place) {
		if (super.onPlace(pos, type, place)) {
			world.addUpdateQue(pos.clone());
			return true;
		}
		return false;
	}
	
	@Override
	public Array<Item> getDropItem(BlockPos pos) {
		return MathUtils.randomBoolean(0.1f) ? dropList(new BasicItem(BasicItem.FLINT)) : super.getDropItem(pos);
	}
	
	@Override
	public void onUpdateTick(BlockPos pos) {
		if (FallingBlock.isFallable(pos.offset(Facing.DOWN))) {
			world.addEntity(FallingBlock.newEntity(pos));
			world.setAir(pos, PlaceType.SET, false);
		}
	}
}
