package com.andedit.arcubit.items;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.Sounds;
import com.andedit.arcubit.Steve;
import com.andedit.arcubit.blocks.furnace.FurnaceRecipe;
import com.andedit.arcubit.blocks.furnace.FurnaceUses;
import com.andedit.arcubit.entity.Player;
import com.andedit.arcubit.handles.RayContext;
import com.andedit.arcubit.utils.ArrayDir;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.IntArray;

public class FoodItem extends Item implements FurnaceRecipe {
	
	private static int a;
	
	public static final int
	RAW_PORK = a++,
	COOK_PORK = a++,
	BREAD = a++;
	
	private static final ArrayDir<String> NAMES = new ArrayDir<>(String.class, a);
	private static final ArrayDir<TextureRegion> TEXS = new ArrayDir<>(TextureRegion.class, a);
	private static final IntArray POINT = new IntArray(a);
	
	static {
		NAMES.put(RAW_PORK, "Raw Porkchop");
		NAMES.put(COOK_PORK, "Cooked Porkchop");
		NAMES.put(BREAD, "Bread");
		
		TEXS.put(RAW_PORK, new TextureRegion(Assets.GUI_ITEMS, 80, 448, 16, 16));
		TEXS.put(COOK_PORK, new TextureRegion(Assets.TEXTURE, 608, 448, 16, 16));
		TEXS.put(BREAD, new TextureRegion(Assets.TEXTURE, 816, 480, 16, 16));
		
		POINT.size = a;
		POINT.set(RAW_PORK, 3);
		POINT.set(COOK_PORK, 8);
		POINT.set(BREAD, 5);
	}
	
	public FoodItem() {
		
	}
	
	public FoodItem(int type) {
		this.type = (short) type;
	}
	
	@Override
	public TextureRegion getTexture() {
		return TEXS.get(type);
	}
	
	@Override
	public String getName() {
		return NAMES.get(type);
	}
	
	public int getFoodPoint() {
		return POINT.get(type);
	}
	
	@Override
	public void onUse(Steve steve, RayContext context, boolean onTap) {
		if (!onTap) return;
		final Player player = steve.player;
		if (player.health >= player.getMaxHealth()) return;
		player.heal(getFoodPoint());
		size--;
		Sounds.EAT.play();
	}
	
	@Override
	public FoodItem clone() {
		final FoodItem food = new FoodItem();
		food.set(this);
		return food;
	}
	
	// Furnaces
	
	public FurnaceUses getFurnUses() {
		if (type == RAW_PORK) {
			return FurnaceUses.SMELTABLE;
		}
		
		return FurnaceUses.NONE;
	}
	
	private static final Item COOK_PORK_ITEM = new FoodItem(COOK_PORK);
	public Item getResult() {
		return COOK_PORK_ITEM;
	}
}
