package com.andedit.arcubit.blocks;

import static com.andedit.arcubit.world.World.world;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.blocks.data.SlabComp;
import com.andedit.arcubit.blocks.materials.Material;
import com.andedit.arcubit.blocks.models.SlabModel;
import com.andedit.arcubit.blocks.utils.BlockType;
import com.andedit.arcubit.handles.RayContext;
import com.andedit.arcubit.items.BlockItem;
import com.andedit.arcubit.items.Item;
import com.andedit.arcubit.items.tools.ToolType;
import com.andedit.arcubit.utils.ArrayDir;
import com.andedit.arcubit.utils.BlockPos;
import com.andedit.arcubit.utils.Facing;
import com.andedit.arcubit.utils.Facing.Axis;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class SlabBlock extends Block {
	
	public static final int
	STONE = 0,
	COBBLESONE = 1,
	WOOD = 2,
	BRICK = 3,
	SANDSTONE = 4,
	STONEBRICK = 5,
	QUARTZ = 6;
	
	public static final SlabComp comp = new SlabComp();

	SlabBlock() {
		this.material = Material.SLAB;
		this.hardness = 2.0f;
		this.newTypeComponent(6);
		this.manager.addCompoment(comp);
		this.model = new SlabModel(this);
		
		textures = new ArrayDir<>(TextureRegion.class, 7);
		textures.put(STONE, Assets.getItemReg(12, 1));
		textures.put(COBBLESONE, Assets.getItemReg(13, 1));
		textures.put(WOOD, Assets.getItemReg(14, 1));
		textures.put(BRICK, Assets.getItemReg(15, 1));
		textures.put(SANDSTONE, Assets.getItemReg(0, 2));
		textures.put(STONEBRICK, Assets.getItemReg(1, 2));
		textures.put(QUARTZ, Assets.getItemReg(2, 2));
	}
	
	@Override
	public boolean onPlace(RayContext context, int type) {
		
		BlockPos pos = null;
		if (context.blockHit == this && !comp.isFullBlock(context.in) && type == getType(context.in)) {
			pos = context.in;
		} if (world.getBlock(context.out) == this) {
			if (!(pos != null && context.face1.axis == Axis.Y)) {
				pos = context.out;
			}
		}
		
		if (pos != null && type == getType(pos) && !comp.isFullBlock(pos)) {
			if (pos == context.in) {
				if (comp.isUpper(pos)) {
					if (context.face1 == Facing.DOWN) {
						comp.setFullBlock(pos);
						return true;
					}
				} else {
					if (context.face1 == Facing.UP) {
						comp.setFullBlock(pos);
						return true;
					}
				}
			} else {
				comp.setFullBlock(pos);
				return true;
			}
		}
		
		if (super.onPlace(context, type)) {
			comp.setUpper(context.out, context.isUpper);
			return true;
		}
		
		return false;
	}
	
	@Override
	public Array<Item> getDropItem(BlockPos pos) {
		return dropList(new BlockItem(pos).size(comp.isFullBlock(pos)?2:1));
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
	public String getName(int type) {
		switch (type) {
		case STONE: return "Stone Slab";
		case COBBLESONE: return "Cobblestone Slab";
		case WOOD: return "Wooden Slab";
		case BRICK: return "Brick Slab";
		case SANDSTONE: return "Sandstone Slab";
		case STONEBRICK: return "Stonebrick Slab";
		default: return "unknown";
		}
	}
	
	public static Item getMaterial(int type) {
		switch (type) {
		case STONE:      return new BlockItem(Blocks.STONE);
		case COBBLESONE: return new BlockItem(Blocks.COBBLESTONE);
		case WOOD:       return new BlockItem(Blocks.PLANK);
		case BRICK:      return new BlockItem(Blocks.BRICK);
		case SANDSTONE:  return new BlockItem(Blocks.SANDSTONE);
		case STONEBRICK: return new BlockItem(Blocks.STONEBRICK);
		default: return null;
		}
	}
}
