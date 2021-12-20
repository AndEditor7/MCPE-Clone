package com.andedit.arcubit.blocks.data;

import java.util.Objects;

import com.andedit.arcubit.blocks.utils.BlockUtils;
import com.badlogic.gdx.utils.ObjectMap;

public final class DataManager {
	
	private final ObjectMap<String, DataComp> components = new ObjectMap<>();
	
	/** Bit size allocated. */
	private int bitSize;
	
	public void addCompoment(DataComp component) {
		addCompoment(component.getKey(), component);
	}
	
	public void addCompoment(String key, DataComp component) {
		if (bitSize+component.size >= 16) throw new IllegalStateException("Block data has reached the bit limit! Max bits: 16");
		if (components.containsKey(key)) throw new IllegalStateException("Duplicated key - use the different key. Key: " + key);
		component.genData(BlockUtils.DATA_SHIFT+bitSize);
		bitSize += component.size;
		components.put(key, component);
	}

	public boolean isEmpty() {
		return components.isEmpty();
	}
	
	public int getSize() {
		return components.size;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends DataComp> T getComponent(String key) {
		return (T) components.get(key);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends DataComp> T getComponent(Class<T> clazz, String key) {
		return (T) components.get(key);
	}
}
