package com.andedit.arcubit.items;

import static com.andedit.arcubit.Assets.GUI_ITEMS;

import com.andedit.arcubit.blocks.furnace.FurnaceRecipe;
import com.andedit.arcubit.blocks.furnace.FurnaceUses;
import com.andedit.arcubit.utils.ArrayDir;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class BasicItem extends Item implements FurnaceRecipe {
	
	private static int a = 0;
	
	public static final int
	COAL = a++, // f
	IRON = a++,
	GOLD = a++,
	DIAMOND = a++,
	LAPIS = a++,
	CLAY = a++, // s
	BRICK = a++,
	NETHER_BRICK = a++,
	STICK = a++, // f
	STRING = a++,
	ARROW = a++,
	FEATHER = a++,
	FLINT = a++,
	BOWL = a++,
	QUARTZ = a++,
	BONE = a++,
	PAPER = a++,
	BOOK = a++,
	GLOWSTONE = a++,
	LEATHER = a++,
	GUNPOWDER = a++,
	WHEAT = a++,
	SUGER = a++;
	
	private static final ArrayDir<String> NAMES = new ArrayDir<>(String.class, a);
	private static final ArrayDir<TextureRegion> TEXS = new ArrayDir<>(TextureRegion.class, a);
	
	static {
		NAMES.put(COAL, "Coal");
		NAMES.put(IRON, "Iron Ingot");
		NAMES.put(GOLD, "Gold Ingot");
		NAMES.put(DIAMOND, "Diamond");
		NAMES.put(LAPIS, "Lapis Lazuli");
		NAMES.put(CLAY, "Clay");
		NAMES.put(BRICK, "Brick");
		NAMES.put(NETHER_BRICK, "Nether Brick");
		NAMES.put(STICK, "Stick");
		NAMES.put(STRING, "String");
		NAMES.put(ARROW, "Arrow");
		NAMES.put(FEATHER, "Feather");
		NAMES.put(FLINT, "Flint");
		NAMES.put(BOWL, "Bowl");
		NAMES.put(QUARTZ, "Nether Quartz");
		NAMES.put(BONE, "Bone");
		NAMES.put(PAPER, "Paper");
		NAMES.put(BOOK, "Book");
		NAMES.put(GLOWSTONE, "Glowstone Dust");
		NAMES.put(LEATHER, "Leather");
		NAMES.put(GUNPOWDER, "Gun Powder");
		NAMES.put(WHEAT, "Wheat");
		NAMES.put(SUGER, "Suger");
		
		TEXS.put(COAL, new TextureRegion(GUI_ITEMS, 1, 448, 16, 16));
		TEXS.put(IRON, new TextureRegion(GUI_ITEMS, 496, 432, 16, 16));
		TEXS.put(GOLD, new TextureRegion(GUI_ITEMS, 480, 432, 16, 16));
		TEXS.put(DIAMOND, new TextureRegion(GUI_ITEMS, 32, 448, 16, 16));
		TEXS.put(LAPIS, new TextureRegion(GUI_ITEMS, 160, 463, 16, 16));
		TEXS.put(CLAY, new TextureRegion(GUI_ITEMS, 112, 448, 16, 16));
		TEXS.put(BRICK, new TextureRegion(GUI_ITEMS, 128, 448, 16, 16));
		TEXS.put(NETHER_BRICK, new TextureRegion(GUI_ITEMS, 64, 448, 16, 16));
		TEXS.put(STICK, new TextureRegion(GUI_ITEMS, 225, 448, 16, 16));
		TEXS.put(STRING, new TextureRegion(GUI_ITEMS, 97, 464, 16, 16));
		TEXS.put(ARROW, new TextureRegion(GUI_ITEMS, 112, 464, 16, 16));
		TEXS.put(FEATHER, new TextureRegion(GUI_ITEMS, 126, 464, 16, 16));
		TEXS.put(FLINT, new TextureRegion(GUI_ITEMS, 145, 464, 16, 16));
		TEXS.put(BOWL, new TextureRegion(GUI_ITEMS, 192, 464, 16, 16));
		TEXS.put(QUARTZ, new TextureRegion(GUI_ITEMS, 224, 464, 16, 16));
		TEXS.put(BONE, new TextureRegion(GUI_ITEMS, 128, 480, 16, 16));
		TEXS.put(PAPER, new TextureRegion(GUI_ITEMS, 144, 480, 16, 16));
		TEXS.put(BOOK, new TextureRegion(GUI_ITEMS, 160, 480, 16, 16));
		TEXS.put(GLOWSTONE, new TextureRegion(GUI_ITEMS, 192, 480, 16, 16));
		TEXS.put(LEATHER, new TextureRegion(GUI_ITEMS, 256, 464, 16, 16));
		TEXS.put(GUNPOWDER, new TextureRegion(GUI_ITEMS, 208, 480, 16, 16));
		TEXS.put(WHEAT, new TextureRegion(GUI_ITEMS, 224, 480, 16, 16));
		TEXS.put(SUGER, new TextureRegion(GUI_ITEMS, 256, 480, 16, 16));
	}
	
	public BasicItem() {
	}
	
	public BasicItem(int type) {
		this.type = (short)type;
	}
	
	@Override
	public TextureRegion getTexture() {
		return TEXS.get(type);
	}
	
	@Override
	public String getName() {
		return NAMES.get(type);
	}
	
	@Override
	public BasicItem clone() {
		BasicItem item = new BasicItem();
		item.set(this);
		return item;
	}
	
	// Furnace
	
	public FurnaceUses getFurnUses() {
		
		if (type == COAL || type == STICK) {
			return FurnaceUses.FUEL;
		} else
		if (type == CLAY) {
			return FurnaceUses.SMELTABLE;
		}
		
		return FurnaceUses.NONE;
	}
	
	public int getBurnTime() {
		
		if (type == COAL) {
			return 80;
		} else
		if (type == STICK) {
			return 5;
		}
		
		return 0;
	}
	
	private static final Item BRICK_ITEM = new BasicItem(BRICK);
	public Item getResult() {
		if (type == CLAY) {
			return BRICK_ITEM;
		}
		
		return null;
	}
}
