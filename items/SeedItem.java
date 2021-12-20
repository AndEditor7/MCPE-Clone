package com.andedit.arcubit.items;

import static com.andedit.arcubit.world.World.world;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.Steve;
import com.andedit.arcubit.blocks.Blocks;
import com.andedit.arcubit.handles.RayContext;
import com.andedit.arcubit.handles.RayContext.HitType;
import com.andedit.arcubit.utils.PlaceType;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SeedItem extends Item {
	
	private static final TextureRegion TEX = new TextureRegion(Assets.TEXTURE, 785, 432, 16, 16);
	
	public SeedItem() {
		
	}
	
	@Override
	public TextureRegion getTexture() {
		return TEX;
	}
	
	@Override
	public String getName() {
		return "Seeds";
	}
	
	@Override
	public void onUse(Steve steve, RayContext context, boolean onTap) {
		if (onTap && context.getHitType() == HitType.BLOCK && world.setBlock(context.out, Blocks.CROP, PlaceType.PLACE)) {
			size--;
		}
	}
	
	@Override
	public SeedItem clone() {
		SeedItem item = new SeedItem();
		item.set(this);
		return item;
	}
}
