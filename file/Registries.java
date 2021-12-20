package com.andedit.arcubit.file;

import java.lang.reflect.Constructor;

import com.andedit.arcubit.blocks.Block;
import com.andedit.arcubit.blocks.ChestBlock.ChestContent;
import com.andedit.arcubit.blocks.furnace.FurnaceContent;
import com.andedit.arcubit.entity.*;
import com.andedit.arcubit.items.*;
import com.andedit.arcubit.items.tools.*;
import com.andedit.arcubit.seriallizers.*;
import com.andedit.arcubit.ui.actors.Hotbar.HotIndex;
import com.andedit.arcubit.utils.ArraySerial;
import com.andedit.arcubit.utils.BlockPos;
import com.andedit.arcubit.utils.StringMap;
import com.andedit.arcubit.utils.maths.CollisionBox;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.esotericsoftware.kryo.Kryo;

final class Registries {
	private static short a = 0;
	static final ObjectMap<Class<?>, Short> MAPID = new ObjectMap<>();
	static final Array<Constructor<?>> STRUCTS = new Array<>(48);
	
	static final StringMap MAP = new StringMap();
	
	static void register(Kryo kryo) {
		kryo.register(Properties.class, new PropertiesSeriallizer(MAP));
		kryo.register(CollisionBox.class, new CollisionBoxSeriallizer());
		kryo.register(Vector2.class, new Vector2Seriallizer());
		kryo.register(Vector3.class, new Vector3Seriallizer());
		kryo.register(ToolType.class);
		kryo.register(ToolMaterial.class);
		kryo.register(BlockPos.class, new BlockPosSeriallizer());
		kryo.register(HotIndex.class);
		kryo.register(StringMap.class, new StringMapSeriallizer(MAP));
		kryo.register(Block.class, new BlockSeriallizer());
		kryo.register(ArraySerial.class, new ArraySeriallizer());
		
		register(BasicItem.class);
		register(BlockItem.class);
		register(FoodItem.class);
		register(LootBag.class);
		register(SeedItem.class);
		register(ToolItem.class);
		register(ChestContent.class);
		register(FurnaceContent.class);
		register(Pig.class);
		register(Zombie.class);
		register(FallingBlock.class);
		register(ItemEnitiy.class);
		register(BusketItem.class);
		register(FlintSteel.class);
		register(Sheep.class);
		register(ShearsItem.class);
		register(DyesItem.class);
	}
	
	@SuppressWarnings("unchecked")
	static <V> V newObject(Class<V> clazz) {
		try {
			return (V) STRUCTS.get(MAPID.get(clazz)).newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private static void register(Class<?> clazz) {
		MAPID.put(clazz, a++);
		try {
			STRUCTS.add(clazz.getConstructor((Class[])null));
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
	}
}
