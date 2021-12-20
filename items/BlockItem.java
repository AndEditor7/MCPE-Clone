package com.andedit.arcubit.items;

import static com.andedit.arcubit.world.World.world;

import com.andedit.arcubit.Steve;
import com.andedit.arcubit.blocks.Block;
import com.andedit.arcubit.blocks.Blocks;
import com.andedit.arcubit.blocks.LogBlock;
import com.andedit.arcubit.blocks.OreBlock;
import com.andedit.arcubit.blocks.furnace.FurnaceRecipe;
import com.andedit.arcubit.blocks.furnace.FurnaceUses;
import com.andedit.arcubit.file.Properties;
import com.andedit.arcubit.handles.RayContext;
import com.andedit.arcubit.handles.RayContext.HitType;
import com.andedit.arcubit.utils.BlockPos;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ObjectMap;

public class BlockItem extends Item implements FurnaceRecipe {
	
	private static final ObjectMap<Block, FurnaceUses> furnMap = new ObjectMap<>();
	
	static {
		furnMap.put(Blocks.PLANK, FurnaceUses.FUEL);
		furnMap.put(Blocks.CRAFTING, FurnaceUses.FUEL);
		furnMap.put(Blocks.CHEST, FurnaceUses.FUEL);
		furnMap.put(Blocks.SABLING, FurnaceUses.FUEL);
		furnMap.put(Blocks.DOOR, FurnaceUses.FUEL);
		
		furnMap.put(Blocks.LOG, FurnaceUses.BOTH);
		
		furnMap.put(Blocks.ORE, FurnaceUses.SMELTABLE);
		furnMap.put(Blocks.COBBLESTONE, FurnaceUses.SMELTABLE);
		furnMap.put(Blocks.SAND, FurnaceUses.SMELTABLE);
	}
	
	public Block block;
	
	public BlockItem() {
	}
	
	public BlockItem(Block block, int type) {
		this.type = (short) type;
		this.block = block;
	}
	
	public BlockItem(Block block) {
		this.block = block;
	}

	public BlockItem(BlockPos pos) {
		this.block = world.getBlock(pos);
		this.type = (short)block.getType(pos);
	}
	
	@Override
	public TextureRegion getTexture() {
		return block.getItemTexture(type);
	}
	
	@Override
	public String getName() {
		return block.getName(type);
	}
	
	@Override
	public void onUse(Steve steve, RayContext context, boolean onTap) {
		if (onTap && context.getHitType() == HitType.BLOCK && Block.place(context, this)) {
			block.getBlockType(type).audio.play(1, 0.8f);
		}
	}
	
	@Override
	public String getShareType() {
		if (block.getClass() == LogBlock.class) {
			return "log";
		}
		return null;
	}
	
	@Override
	public int hashCode() {
		return block.hashCode() * ((type+1) * 86);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BlockItem) {
			final BlockItem item = (BlockItem) obj;
			return item.block == block && item.type == type;
		}
		return false;
	}
	
	@Override
	public BlockItem clone() {
		BlockItem item = new BlockItem();
		item.set(this);
		item.block = block;
		return item;
	}
	
	
	// Furnaces
	public FurnaceUses getFurnUses() {
		return furnMap.get(block, FurnaceUses.NONE);
	}
	
	public int getBurnTime() {
		if (block == Blocks.LOG || block == Blocks.PLANK || block == Blocks.CRAFTING || block == Blocks.CHEST) {
			return 15;
		} else
		if (block == Blocks.DOOR) {
			return 10;
		} else
		if (block == Blocks.SABLING) {
			return 5;
		}
		
		return 0;
	}
	
	private static final Item COAL  = new BasicItem(BasicItem.COAL);
	private static final Item IRON  = new BasicItem(BasicItem.IRON);
	private static final Item GOLD  = new BasicItem(BasicItem.GOLD);
	private static final Item STONE = new BlockItem(Blocks.STONE);
	private static final Item GLASS = new BlockItem(Blocks.GLASS);
	public Item getResult() {
		if (block == Blocks.LOG) {
			return COAL;
		} else if (block == Blocks.ORE) {
			if (type == OreBlock.IRON) {
				return IRON;
			}
			if (type == OreBlock.GOLD) {
				return GOLD;
			}
		} else
		if (block == Blocks.COBBLESTONE) {
			return STONE;
		} else
		if (block == Blocks.SAND) {
			return GLASS;
		}
		
		return null;
	}
	
	@Override
	public void save(Properties props) {
		super.save(props);
		props.put("block", block);
	}
	
	@Override
	public void load(Properties props) {
		super.load(props);
		block = props.got("block");
	}
}
