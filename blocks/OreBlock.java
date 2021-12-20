package com.andedit.arcubit.blocks;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.blocks.materials.Material;
import com.andedit.arcubit.blocks.models.CubeModel;
import com.andedit.arcubit.blocks.utils.CubeTex;
import com.andedit.arcubit.items.BasicItem;
import com.andedit.arcubit.items.Item;
import com.andedit.arcubit.items.tools.ToolType;
import com.andedit.arcubit.utils.ArrayDir;
import com.andedit.arcubit.utils.BlockPos;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class OreBlock extends Block {
	
	public static final int
	COAL = 0,
	IRON = 1,
	GOLD = 2,
	DIAMOND = 3,
	LAPIS = 4,
	REDSTONE = 5;

	OreBlock() {
		this.material = Material.BLOCK;
		this.setMining(ToolType.PICKAXE, 0, 3);
		
		final ArrayDir<CubeTex> blocks = new ArrayDir<>(CubeTex.class, 6);
		textures = new ArrayDir<>(TextureRegion.class, 6);
		textures.put(COAL, Assets.getItemReg(6, 2));
		textures.put(IRON, Assets.getItemReg(7, 2));
		textures.put(GOLD, Assets.getItemReg(8, 2));
		textures.put(DIAMOND, Assets.getItemReg(9, 2));
		textures.put(LAPIS, Assets.getItemReg(10, 2));
		textures.put(REDSTONE, Assets.getItemReg(11, 2));
		
		blocks.put(COAL, new CubeTex(Assets.getBlockReg(2, 2)));
		blocks.put(IRON, new CubeTex(Assets.getBlockReg(1, 2)));
		blocks.put(GOLD, new CubeTex(Assets.getBlockReg(0, 2)));
		blocks.put(DIAMOND, new CubeTex(Assets.getBlockReg(2, 3)));
		blocks.put(LAPIS, new CubeTex(Assets.getBlockReg(0, 10)));
		blocks.put(REDSTONE, new CubeTex(Assets.getBlockReg(3, 3)));
		
		this.model = new CubeModel(this, blocks);
		
		newTypeComponent(5);
	}

	@Override
	public String getName(int data) {
		switch (data) {
		case COAL: return "Coal Ore";
		case IRON: return "Iron Ore";
		case GOLD: return "Goal Ore";
		case DIAMOND: return "Diamond Ore";
		case LAPIS: return "Lapis Lazuli Ore";
		case REDSTONE: return "Redstone Ore";
		default: return null; 
		}
	}
	
	@Override
	public int getMiningLevel(int type) {
		switch (type) {
		case COAL: return 1;
		case IRON: return 2;
		case GOLD: return 3;
		case DIAMOND: return 3;
		case LAPIS: return 2;
		case REDSTONE: return 3;
		default: return 0; 
		}
	}
	
	@Override
	public Array<Item> getDropItem(BlockPos pos) {
		switch (getType(pos)) {
		case COAL: return dropList(new BasicItem(BasicItem.COAL));
		case DIAMOND: return dropList(new BasicItem(BasicItem.DIAMOND));
		default: return super.getDropItem(pos);
		}
	}
}
