package com.andedit.arcubit.items;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.Steve;
import com.andedit.arcubit.handles.RayContext;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class LootBag extends Item {
	private static final TextureRegion BAG = new TextureRegion(Assets.GUI_ITEMS, 369, 480, 16, 16);
	
	@Override
	public TextureRegion getTexture() {
		return BAG;
	}
	
	@Override
	public String getName() {
		return "Loot Bag";
	}
	
	@Override
	public void onUse(Steve steve, RayContext context, boolean onTap) {
	}
	
	@Override
	public LootBag clone() {
		LootBag item = new LootBag();
		item.set(this);
		return item;
	}
}
