package com.andedit.arcubit.utils;

import java.lang.reflect.Array;

/** Array direct. For simpler IntMap and faster. */
public class ArrayDir<T> {
	public final T[] objs;
	
	private ArrayDir(ArrayDir<T> array) {
		objs = array.objs.clone();
	}
	
	@SuppressWarnings("unchecked")
	public ArrayDir(Class<T> clazz, int size) {
		objs = (T[]) Array.newInstance(clazz, size);
	}
	
	public T get(int index) {
		return objs[index];
	}
	
	public ArrayDir<T> put(int index, T obj) {
		objs[index] = obj;
		return this;
	}
	
	public int size() {
		return objs.length;
	}
	
	@Override
	public ArrayDir<T> clone() {
		return new ArrayDir<T>(this);
	}
}
