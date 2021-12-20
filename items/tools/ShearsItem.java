package com.andedit.arcubit.items.tools;

import static com.andedit.arcubit.world.World.world;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.Sounds;
import com.andedit.arcubit.Steve;
import com.andedit.arcubit.blocks.Blocks;
import com.andedit.arcubit.entity.ItemEnitiy;
import com.andedit.arcubit.entity.Sheep;
import com.andedit.arcubit.handles.RayContext;
import com.andedit.arcubit.handles.RayContext.HitType;
import com.andedit.arcubit.items.BlockItem;
import com.andedit.arcubit.items.Item;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class ShearsItem extends Item implements Durability {

	private static final TextureRegion TEX = new TextureRegion(Assets.TEXTURE, 528, 464, 16, 16);
	
	private short damage = (short)getMaxDamage();
	
	public ShearsItem() {
		
	}
	
	private ShearsItem(ShearsItem item) {
		damage = item.damage;
	}
	
	@Override
	public int getDamage() {
		return damage;
	}

	@Override
	public int getMaxDamage() {
		return 238;
	}

	@Override
	public void takeDamage() {
		damage--;
		if (damage < 0) {
			Sounds.BREAK.play();
		}
	}
	
	@Override
	public void onUse(Steve steve, RayContext context, boolean onTap) {
		if (!onTap && context.getHitType() == HitType.ENTITY) {
			if (context.entityHit instanceof Sheep) {
				Sheep sheep = (Sheep)context.entityHit;
				if (sheep.hasWool) {
					sheep.hasWool = false;
					ItemEnitiy item = 
					new ItemEnitiy(sheep.getCenter(), 
					new BlockItem(Blocks.WOOL).size(MathUtils.random(1, 3)));
					item.setVel();
					
					world.addEntity(item);
					takeDamage();
				}
			}
		}
	}
	
	@Override
	public boolean isEmpty() {
		return damage < 0 || super.isEmpty();
	}
	
	@Override
	public int getStackSize() {
		return 1;
	}

	@Override
	public String getName() {
		return "Shears";
	}

	@Override
	public TextureRegion getTexture() {
		return TEX;
	}

	@Override
	public Item clone() {
		return new ShearsItem(this);
	}

}
