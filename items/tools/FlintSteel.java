package com.andedit.arcubit.items.tools;

import static com.andedit.arcubit.world.World.world;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.Sounds;
import com.andedit.arcubit.Steve;
import com.andedit.arcubit.blocks.Blocks;
import com.andedit.arcubit.file.Properties;
import com.andedit.arcubit.handles.RayContext;
import com.andedit.arcubit.handles.RayContext.HitType;
import com.andedit.arcubit.items.Item;
import com.andedit.arcubit.utils.PlaceType;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class FlintSteel extends Item implements Durability {
	
	private static final TextureRegion TEX = new TextureRegion(Assets.TEXTURE, 800, 480, 16, 16);

	private short damage = (short)getMaxDamage();

	@Override
	public int getDamage() {
		return damage;
	}

	@Override
	public int getMaxDamage() {
		return 64;
	}
	
	@Override
	public int getStackSize() {
		return 1;
	}

	@Override
	public void takeDamage() {
		damage--;
		if (damage < 0) {
			Sounds.BREAK.play();
		}
	}
	
	@Override
	public boolean isEmpty() {
		return damage < 0 || super.isEmpty();
	}

	@Override
	public String getName() {
		return "Flint And Steel";
	}

	@Override
	public TextureRegion getTexture() {
		return TEX;
	}
	
	@Override
	public void onUse(Steve steve, RayContext context, boolean onTap) {
		if (onTap && context.getHitType() == HitType.BLOCK) {
			if (world.setBlock(context.out, Blocks.FIRE, PlaceType.PLACE)) {
				takeDamage();
				Sounds.IGNITE.play();
			}
		}
	}

	@Override
	public FlintSteel clone() {
		FlintSteel item = new FlintSteel();
		item.damage = damage;
		return item;
	}

	@Override
	public void save(Properties props) {
		props.put("damage", damage);
	}
	
	@Override
	public void load(Properties props) {
		damage = props.got("damage");
	}
}
