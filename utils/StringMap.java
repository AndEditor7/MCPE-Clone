package com.andedit.arcubit.utils;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectIntMap;

public class StringMap {
	private static final int SIZE = 128;
	
	private final ObjectIntMap<String> stringToIndex;
	private final Array<String> indexToString;
	
	public StringMap() {
		this(SIZE);
	}
	
	public StringMap(int size) {
		indexToString = new Array<>(size);
		stringToIndex = new ObjectIntMap<>((int)(size * 0.8f));
	}
	
	public void put(String string) {
		if (string == null || stringToIndex.containsKey(string)) {
			return;
		}
		
		stringToIndex.put(string, size());
		indexToString.add(string);
	}
	
	public String getString(int index) {
		return indexToString.get(index);
	}
	
	public int getIndex(String key) {
		return stringToIndex.get(key, -1);
	}

	public int size() {
		return indexToString.size;
	}
	
	public void clear() {
		stringToIndex.clear();
		indexToString.clear();
	}

	public void resize(int size) {
		clear();
		stringToIndex.ensureCapacity(size);
		indexToString.ensureCapacity(size);
	}
}
