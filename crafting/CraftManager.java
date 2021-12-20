package com.andedit.arcubit.crafting;

import com.andedit.arcubit.blocks.Blocks;
import com.andedit.arcubit.blocks.MetalBlock;
import com.andedit.arcubit.blocks.SlabBlock;
import com.andedit.arcubit.blocks.StairBlock;
import com.andedit.arcubit.items.BasicItem;
import com.andedit.arcubit.items.BlockItem;
import com.andedit.arcubit.items.BusketItem;
import com.andedit.arcubit.items.FoodItem;
import com.andedit.arcubit.items.tools.FlintSteel;
import com.andedit.arcubit.items.tools.ShearsItem;
import com.andedit.arcubit.items.tools.ToolItem;
import com.andedit.arcubit.items.tools.ToolMaterial;
import com.andedit.arcubit.items.tools.ToolType;
import com.badlogic.gdx.utils.Array;

public final class CraftManager {
	public static final Array<Recipe> BUILD = new Array<>();
	public static final Array<Recipe> TOOLS = new Array<>();
	public static final Array<Recipe> ARMOR = new Array<>();
	public static final Array<Recipe> MISC  = new Array<>();
	
	static {
		Recipe recipe;
		
		recipe = newRecipe(BUILD); // Log to 4 Planks.
		recipe.setRecipe(new BlockItem(Blocks.LOG)).shareType();
		recipe.setReturn(new BlockItem(Blocks.PLANK).size(4));
		
		recipe = newRecipe(BUILD); // 2 Planks to 4 Sticks.
		recipe.setRecipe(new BlockItem(Blocks.PLANK).size(2));
		recipe.setReturn(new BasicItem(BasicItem.STICK).size(4));
		
		recipe = newRecipe(BUILD); // 4 Planks to Crafting Table.
		recipe.setRecipe(new BlockItem(Blocks.PLANK).size(4));
		recipe.setReturn(new BlockItem(Blocks.CRAFTING));
		
		recipe = newRecipe(BUILD); // 4 Sands to 4 Sandstones.
		recipe.setRecipe(new BlockItem(Blocks.SAND).size(4));
		recipe.setReturn(new BlockItem(Blocks.SANDSTONE).size(4));
		
		recipe = newRecipe(BUILD); // 6 Planks to Door.
		recipe.setRecipe(new BlockItem(Blocks.PLANK).size(6));
		recipe.setReturn(new BlockItem(Blocks.DOOR).size(1)).set3x3();
		
		recipe = newRecipe(BUILD); // 8 Planks to Chest.
		recipe.setRecipe(new BlockItem(Blocks.PLANK).size(8));
		recipe.setReturn(new BlockItem(Blocks.CHEST)).set3x3();
		
		recipe = newRecipe(BUILD); // 8 Cobblestone to Furnace.
		recipe.setRecipe(new BlockItem(Blocks.COBBLESTONE).size(8));
		recipe.setReturn(new BlockItem(Blocks.FURNACE)).set3x3();
		
		recipe = newRecipe(BUILD); // 4 Clay to Clay Block.
		recipe.setRecipe(new BasicItem(BasicItem.CLAY).size(4));
		recipe.setReturn(new BlockItem(Blocks.CLAY));
		
		recipe = newRecipe(BUILD); // 4 Bricks to Brick Block.
		recipe.setRecipe(new BasicItem(BasicItem.BRICK).size(4));
		recipe.setReturn(new BlockItem(Blocks.BRICK));
		
		recipe = newRecipe(BUILD); // 4 Stone to 4 Stonebrick.
		recipe.setRecipe(new BlockItem(Blocks.STONE).size(4));
		recipe.setReturn(new BlockItem(Blocks.STONEBRICK).size(4));
		
		
		recipe = newRecipe(BUILD); // 3 Books and 6 Planks to 3 Bookshelf.
		recipe.setRecipe(new BasicItem(BasicItem.BOOK).size(3), new BlockItem(Blocks.PLANK).size(6));
		recipe.setReturn(new BlockItem(Blocks.BOOKSHELF).size(3)).set3x3();
		
		recipe = newRecipe(BUILD);
		recipe.setRecipe(new BasicItem(BasicItem.STICK).size(7));
		recipe.setReturn(new BlockItem(Blocks.LADDER).size(3)).set3x3();
		
		for (int i = 0; i < 6; i++) {
			recipe = newRecipe(BUILD).set3x3();
			recipe.setRecipe(SlabBlock.getMaterial(i).size(3));
			recipe.setReturn(new BlockItem(Blocks.SLAB, i).size(6));
		}
		
		for (int i = 0; i < 5; i++) {
			recipe = newRecipe(BUILD).set3x3();
			recipe.setRecipe(StairBlock.getMaterial(i).size(6));
			recipe.setReturn(new BlockItem(Blocks.STAIR, i).size(6));
		}
		
		recipe = newRecipe(ARMOR);
		recipe.setRecipe(new BasicItem(BasicItem.WHEAT).size(3));
		recipe.setReturn(new FoodItem(FoodItem.BREAD)).set3x3();
		
		recipe = newRecipe(MISC);
		recipe.setRecipe(new BasicItem(BasicItem.IRON).size(9)).set3x3();
		recipe.setReturn(new BlockItem(Blocks.METAL, MetalBlock.IRON));
		
		recipe = newRecipe(MISC);
		recipe.setRecipe(new BasicItem(BasicItem.GOLD).size(9)).set3x3();
		recipe.setReturn(new BlockItem(Blocks.METAL, MetalBlock.GOLD));
		
		recipe = newRecipe(MISC);
		recipe.setRecipe(new BasicItem(BasicItem.DIAMOND).size(9)).set3x3();
		recipe.setReturn(new BlockItem(Blocks.METAL, MetalBlock.DIAMOND));
		
		recipe = newRecipe(MISC);
		recipe.setRecipe(new BlockItem(Blocks.METAL, MetalBlock.IRON));
		recipe.setReturn(new BasicItem(BasicItem.IRON).size(9));
		
		recipe = newRecipe(MISC);
		recipe.setRecipe(new BlockItem(Blocks.METAL, MetalBlock.GOLD));
		recipe.setReturn(new BasicItem(BasicItem.GOLD).size(9));
		
		recipe = newRecipe(MISC);
		recipe.setRecipe(new BlockItem(Blocks.METAL, MetalBlock.DIAMOND));
		recipe.setReturn(new BasicItem(BasicItem.DIAMOND).size(9));
		
		recipe = newRecipe(TOOLS);
		recipe.setRecipe(new BasicItem(BasicItem.COAL), new BasicItem(BasicItem.STICK));
		recipe.setReturn(new BlockItem(Blocks.TORCH).size(4));
		
		recipe = newRecipe(TOOLS);
		recipe.setRecipe(new BasicItem(BasicItem.FLINT), new BasicItem(BasicItem.IRON));
		recipe.setReturn(new FlintSteel());
		
		recipe = newRecipe(TOOLS).set3x3();
		recipe.setRecipe(new BasicItem(BasicItem.IRON).size(3));
		recipe.setReturn(new BusketItem());
		
		recipe = newRecipe(TOOLS);
		recipe.setRecipe(new BasicItem(BasicItem.IRON).size(2));
		recipe.setReturn(new ShearsItem());
		
		for (ToolMaterial material : ToolMaterial.values()) {
			for (ToolType type : ToolType.values()) {
				if (type == ToolType.NONE || type == ToolType.SHEARS) continue;
				int sticks = type == ToolType.SWORD ? 1 : 2;
				
				recipe = newRecipe(TOOLS);
				recipe.setRecipe(material.getItem().size(type.getAmountTool()), new BasicItem(BasicItem.STICK).size(sticks));
				recipe.setReturn(new ToolItem(type, material)).set3x3();
			}
		}
	}
	
	private static Recipe newRecipe(Array<Recipe> tab) {
		Recipe recipe = new Recipe();
		tab.add(recipe);
		return recipe;
	}
}
