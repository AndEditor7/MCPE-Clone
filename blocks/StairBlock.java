package com.andedit.arcubit.blocks;

import static com.andedit.arcubit.world.World.world;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.blocks.data.HoriComp;
import com.andedit.arcubit.blocks.data.StairComp;
import com.andedit.arcubit.blocks.data.StairComp.Shape;
import com.andedit.arcubit.blocks.materials.Material;
import com.andedit.arcubit.blocks.models.StairModel;
import com.andedit.arcubit.blocks.utils.BlockType;
import com.andedit.arcubit.blocks.utils.UpdateState;
import com.andedit.arcubit.handles.RayContext;
import com.andedit.arcubit.items.BlockItem;
import com.andedit.arcubit.items.Item;
import com.andedit.arcubit.items.tools.ToolType;
import com.andedit.arcubit.utils.ArrayDir;
import com.andedit.arcubit.utils.BlockPos;
import com.andedit.arcubit.utils.Facing;
import com.andedit.arcubit.utils.Facing.Axis;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.andedit.arcubit.utils.PlaceType;

public class StairBlock extends Block {
	
	public static final int
	COBBLESONE = 0,
	WOOD = 1,
	BRICK = 2,
	SANDSTONE = 3,
	STONEBRICK = 4,
	NETHERBRICK = 5,
	QUARTZ = 6;
	
	public static final HoriComp horiComp = new HoriComp();
	public static final StairComp stairComp = new StairComp();

	StairBlock() {
		this.newTypeComponent(6);
		this.manager.addCompoment(horiComp);
		this.manager.addCompoment(stairComp);
		this.material = Material.SLAB;
		this.hardness = 2.0f;
		this.model = new StairModel(this);
		
		textures = new ArrayDir<>(TextureRegion.class, 7);
		textures.put(COBBLESONE, Assets.getItemReg(5, 1));
		textures.put(WOOD, Assets.getItemReg(6, 1));
		textures.put(BRICK, Assets.getItemReg(7, 1));
		textures.put(SANDSTONE, Assets.getItemReg(8, 1));
		textures.put(STONEBRICK, Assets.getItemReg(9, 1));
		textures.put(NETHERBRICK, Assets.getItemReg(10, 1));
		textures.put(QUARTZ, Assets.getItemReg(11, 1));
	}
	
	@Override
	public BlockType getBlockType(int type) {
		return type == WOOD ? BlockType.WOOD : BlockType.STONE;
	}
	
	@Override
	public int getMiningLevel(int type) {
		return type == WOOD ? 0 : 1;
	}
	
	@Override
	public ToolType getToolType(int type) {
		return type == WOOD ? ToolType.AXE : ToolType.PICKAXE;
	}
	
	@Override
	public void onNeighbourUpdate(BlockPos primaray, BlockPos secondary, Facing face, UpdateState state) {
		final Facing frontFace = horiComp.getFace(primaray);
		final Facing backFace  = frontFace.invert();
		final BlockPos frontPos = primaray.offset(frontFace);
		final BlockPos backPos  = primaray.offset(backFace);
		final Block frontBlock = world.getBlock(frontPos);
		final Block backBlock = world.getBlock(backPos);
		
		if (frontBlock != this && backBlock != this) {
			if (stairComp.getState(primaray) != Shape.STRAIGHT) {
				stairComp.setState(primaray, Shape.STRAIGHT);
				updateNearByBlocks(primaray, state);
			}
			return;
		}
		
		final boolean isFlip = stairComp.isFlip(primaray);
		final boolean isInner = frontBlock == this;
		
		if (isInner?(isFlip != stairComp.isFlip(frontPos)):(isFlip != stairComp.isFlip(backPos))) {
			return;
		}
		
		final Facing facing;
		if (isInner) {
			facing = horiComp.getFace(frontPos).rotate(-frontFace.getRotateValue());
		} else {
			facing = horiComp.getFace(backPos).rotate(-backFace.getRotateValue());
		}
		
		if (facing.axis == Axis.X) {
			Shape stair = Shape.get(isInner, facing == Facing.WEST);
			if (stair != stairComp.getState(primaray)) {
				stairComp.setState(primaray, stair);
				updateNearByBlocks(primaray, state);
			}
		}
	}
	
	@Override
	public boolean onPlace(RayContext context, int type) {
		if (onPlace(context.out, type, PlaceType.PLACE)) {
			if (context.face2.isSide())  {
				horiComp.setFace(context.out, context.face2);
			} else if (context.face1.isSide()) {
				horiComp.setFace(context.out, context.face1);
			}
			stairComp.setFlip(context.out, context.isUpper);
			onNeighbourUpdate(context.out, context.in, horiComp.getFace(context.out), UpdateState.ON_PLACE);
			return true;
		}
		return false;
	}

	@Override
	public String getName(int type) {
		switch (type) {
		case COBBLESONE: return "Cobblestone Stair";
		case WOOD: return "Wooden Stair";
		case BRICK: return "Brick Stair";
		case SANDSTONE: return "Sandstone Stair";
		case STONEBRICK: return "Stonebrick Stair";
		case NETHERBRICK: return "Nertherbrick Stair";
		case QUARTZ: return "sad";
		default: return "unknown";
		}
	}
	
	public static Item getMaterial(int type) {
		switch (type) {
		case COBBLESONE: return new BlockItem(Blocks.COBBLESTONE);
		case WOOD:       return new BlockItem(Blocks.PLANK);
		case BRICK:      return new BlockItem(Blocks.BRICK);
		case SANDSTONE:  return new BlockItem(Blocks.SANDSTONE);
		case STONEBRICK: return new BlockItem(Blocks.STONEBRICK);
		default: return null;
		}
	}
}
