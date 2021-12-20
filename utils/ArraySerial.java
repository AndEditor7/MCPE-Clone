package com.andedit.arcubit.utils;

import com.badlogic.gdx.utils.Array;

public class ArraySerial<T> extends Array<T> {
	
	public final Class<T> clazz;
	
	public ArraySerial (Class<T> clazz) {
		super(false, 16, clazz);
		this.clazz = clazz;
	}

	public ArraySerial (Class<T> clazz, int capacity) {
		super(false, capacity, clazz);
		this.clazz = clazz;
	}
	
	public void load(ArraySerial<T> a) {
		a.size = size;
		if (items.length >= a.items.length) {
			a.items = items;
		} else {
			System.arraycopy(items, 0, a.items, 0, size);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void put(Object obj) {
		add((T)obj);
	}
}
