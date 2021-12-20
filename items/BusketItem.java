package com.andedit.arcubit.items;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.Steve;
import com.andedit.arcubit.blocks.Blocks;
import com.andedit.arcubit.blocks.LiquidBlock;
import com.andedit.arcubit.handles.RayContext;
import com.andedit.arcubit.handles.RayContext.HitType;
import com.andedit.arcubit.utils.ArrayDir;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class BusketItem extends Item {
	
	public static final int EMPTY = 0;
	public static final int WATER = 1;
	public static final int LAVA = 2;
	
	private static final ArrayDir<String> NAMES = new ArrayDir<>(String.class, 3);
	private static final ArrayDir<TextureRegion> TEXS = new ArrayDir<>(TextureRegion.class, 3);
	private static final ArrayDir<LiquidBlock> LIQUIDS = new ArrayDir<>(LiquidBlock.class, 3);
	
	static {
		NAMES.put(0, "Busket");
		NAMES.put(1, "Water Busket");
		NAMES.put(2, "Lava Busket");
		
		TEXS.put(0, new TextureRegion(Assets.TEXTURE, 560, 432, 16, 16));
		TEXS.put(1, new TextureRegion(Assets.TEXTURE, 576, 432, 16, 16));
		TEXS.put(2, new TextureRegion(Assets.TEXTURE, 592, 432, 16, 16));
		
		LIQUIDS.put(1, Blocks.WATER);
		LIQUIDS.put(2, Blocks.LAVA);
	}
	
	public BusketItem() {
		
	}
	
	public BusketItem(int type) {
		type(type);
	}
	
	public BusketItem(LiquidBlock liquid) {
		setLiquid(liquid);
	}

	@Override
	public String getName() {
		return NAMES.get(type);
	}

	@Override
	public TextureRegion getTexture() {
		return TEXS.get(type);
	}
	
	@Override
	public void onUse(Steve steve, RayContext context, boolean onTap) {
		if (!onTap) return;
		if (type == 0) {
			if (context.liquidHit != null && context.liquidHit.isFull(context.inl)) {
				context.inl.setAir(false);
				if (size == 1) {
					setLiquid(context.liquidHit);
				} else {
					steve.addItem(new BusketItem(context.liquidHit));
					size--;
				}
				
			}
		} else {
			if (context.getHitType() == HitType.BLOCK) {
				if (getLiquid().intsLiquid(context.out)) {
					if (size == 1) {
						setLiquid(null);
					} else {
						steve.addItem(new BusketItem());
						size--;
					}
				}
			}
		}
	}
	
	@Override
	public int getStackSize() {
		return 16;
	}
	
	public LiquidBlock getLiquid() {
		return LIQUIDS.get(type);
	}
	
	public void setLiquid(LiquidBlock liquid) {
		if (liquid == null) {
			type = 0;
			return;
		}
		type(liquid == Blocks.WATER ? 1 : 2);
	}

	@Override
	public BusketItem clone() {
		BusketItem item = new BusketItem();
		item.set(this);
		return item;
	}

}
