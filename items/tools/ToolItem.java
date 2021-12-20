package com.andedit.arcubit.items.tools;

import static com.andedit.arcubit.world.World.world;

import java.util.EnumMap;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.Sounds;
import com.andedit.arcubit.Steve;
import com.andedit.arcubit.blocks.Blocks;
import com.andedit.arcubit.blocks.furnace.FurnaceRecipe;
import com.andedit.arcubit.blocks.furnace.FurnaceUses;
import com.andedit.arcubit.entity.ItemEnitiy;
import com.andedit.arcubit.file.Properties;
import com.andedit.arcubit.handles.RayContext;
import com.andedit.arcubit.handles.RayContext.HitType;
import com.andedit.arcubit.items.Item;
import com.andedit.arcubit.items.SeedItem;
import com.andedit.arcubit.utils.BlockPos;
import com.andedit.arcubit.utils.PlaceType;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public final class ToolItem extends Item implements FurnaceRecipe, Durability {
	
	private static final EnumMap<ToolType, EnumMap<ToolMaterial, TextureRegion>> TEXS = new EnumMap<>(ToolType.class);
	
	static {
		EnumMap<ToolMaterial, TextureRegion> map = new EnumMap<>(ToolMaterial.class);
		map.put(ToolMaterial.WOOD,    new TextureRegion(Assets.GUI_ITEMS, 32, 464, 16, 16));
		map.put(ToolMaterial.STONE,   new TextureRegion(Assets.GUI_ITEMS, 48, 464, 16, 16));
		map.put(ToolMaterial.IRON,    new TextureRegion(Assets.GUI_ITEMS, 368, 432, 16, 16));
		map.put(ToolMaterial.GOLD,    new TextureRegion(Assets.GUI_ITEMS, 80, 464, 16, 16));
		map.put(ToolMaterial.DIAMOND, new TextureRegion(Assets.GUI_ITEMS, 64, 464, 16, 16));
		TEXS.put(ToolType.SWORD, map);
		
		map = new EnumMap<>(ToolMaterial.class);
		map.put(ToolMaterial.WOOD,    new TextureRegion(Assets.GUI_ITEMS, 209, 448, 16, 16));
		map.put(ToolMaterial.STONE,   new TextureRegion(Assets.GUI_ITEMS, 289, 448, 16, 16));
		map.put(ToolMaterial.IRON,    new TextureRegion(Assets.GUI_ITEMS, 353, 448, 16, 16));
		map.put(ToolMaterial.GOLD,    new TextureRegion(Assets.GUI_ITEMS, 465, 448, 16, 16));
		map.put(ToolMaterial.DIAMOND, new TextureRegion(Assets.GUI_ITEMS, 401, 448, 16, 16));
		TEXS.put(ToolType.PICKAXE, map);
		
		map = new EnumMap<>(ToolMaterial.class);
		map.put(ToolMaterial.WOOD,    new TextureRegion(Assets.GUI_ITEMS, 241, 448, 16, 16));
		map.put(ToolMaterial.STONE,   new TextureRegion(Assets.GUI_ITEMS, 305, 448, 16, 16));
		map.put(ToolMaterial.IRON,    new TextureRegion(Assets.GUI_ITEMS, 369, 448, 16, 16));
		map.put(ToolMaterial.GOLD,    new TextureRegion(Assets.GUI_ITEMS, 481, 448, 16, 16));
		map.put(ToolMaterial.DIAMOND, new TextureRegion(Assets.GUI_ITEMS, 417, 448, 16, 16));
		TEXS.put(ToolType.SHOVEL, map);
		
		map = new EnumMap<>(ToolMaterial.class);
		map.put(ToolMaterial.WOOD,    new TextureRegion(Assets.GUI_ITEMS, 256, 448, 16, 16));
		map.put(ToolMaterial.STONE,   new TextureRegion(Assets.GUI_ITEMS, 320, 448, 16, 16));
		map.put(ToolMaterial.IRON,    new TextureRegion(Assets.GUI_ITEMS, 384, 448, 16, 16));
		map.put(ToolMaterial.GOLD,    new TextureRegion(Assets.GUI_ITEMS, 496, 448, 16, 16));
		map.put(ToolMaterial.DIAMOND, new TextureRegion(Assets.GUI_ITEMS, 432, 448, 16, 16));
		TEXS.put(ToolType.AXE, map);
		
		map = new EnumMap<>(ToolMaterial.class);
		map.put(ToolMaterial.WOOD,    new TextureRegion(Assets.GUI_ITEMS, 272, 448, 16, 16));
		map.put(ToolMaterial.STONE,   new TextureRegion(Assets.GUI_ITEMS, 336, 448, 16, 16));
		map.put(ToolMaterial.IRON,    new TextureRegion(Assets.GUI_ITEMS, 320, 432, 16, 16));
		map.put(ToolMaterial.GOLD,    new TextureRegion(Assets.GUI_ITEMS, 0, 464, 16, 16));
		map.put(ToolMaterial.DIAMOND, new TextureRegion(Assets.GUI_ITEMS, 448, 448, 16, 16));
		TEXS.put(ToolType.HOE, map);
	}
	
	public short damage;
	public ToolType toolType;
	public ToolMaterial material;
	
	public ToolItem() {
		
	}
	
	public ToolItem(ToolType toolType, ToolMaterial material) {
		this.toolType = toolType;
		this.material = material;
		this.damage = (short) material.durability;
	}
	
	@Override
	public TextureRegion getTexture() {
		return TEXS.get(toolType).get(material);
	}
	
	@Override
	public String getName() {
		return material.name + ' ' + toolType.name;
	}
	
	@Override
	public int getDamage() {
		return damage;
	}
	
	@Override
	public int getMaxDamage() {
		return material.durability;
	}
	
	@Override
	public int getHitPoint() {
		return toolType.damage + material.level;
	}
	
	public void takeDamage() {
		damage--;
		if (damage < 0) {
			Sounds.BREAK.play();
		}
	}
	
	@Override
	public int getStackSize() {
		return 1;
	}
	
	@Override
	public boolean isEmpty() {
		return damage < 0 || super.isEmpty();
	}
	
	@Override
	public void onUse(Steve steve, RayContext context, boolean onTap) {
		if (toolType != ToolType.HOE || context.getHitType() != HitType.BLOCK || onTap) return;
		
		if (context.blockHit == Blocks.GRASS || context.blockHit == Blocks.DIRT) {
			world.setBlock(context.in, Blocks.FARMLAND, PlaceType.SET);
			Sounds.DIRT.play(1, 0.8f);
			takeDamage();
			if (context.blockHit == Blocks.GRASS && MathUtils.randomBoolean(0.1f)) {
				BlockPos pos = context.in;
				ItemEnitiy enitiy = new ItemEnitiy(new SeedItem());
				enitiy.setPos(pos.x+0.5f, pos.y+1.3f, pos.z+0.5f);
				enitiy.setVel();
				world.addEntity(enitiy);
			}
		}
	}
	
	@Override
	public ToolItem clone() {
		ToolItem item = new ToolItem();
		item.size = size;
		item.toolType = toolType;
		item.material = material;
		item.damage = damage;
		return item;
	}
	
	@Override
	public int hashCode() {
		return super.hashCode() * material.hashCode() * toolType.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj.getClass() == ToolItem.class) {
			final ToolItem item = (ToolItem)obj;
			return item.material == material && item.toolType == toolType;
		}
		return false;
	}
	
	// Furnace
	public FurnaceUses getFurnUses() {
		return material == ToolMaterial.WOOD ? FurnaceUses.FUEL : FurnaceUses.NONE;
	}
	
	public int getBurnTime() {
		return 10;
	}
	
	@Override
	public void save(Properties props) {
		props.put("damage", damage);
		props.put("toolType", toolType);
		props.put("material", material);
	}
	
	@Override
	public void load(Properties props) {
		damage = props.got("damage");
		toolType = props.got("toolType");
		material = props.got("material");
	}
}
