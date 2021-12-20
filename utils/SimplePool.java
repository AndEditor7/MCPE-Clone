package com.andedit.arcubit.utils;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool.Poolable;

public abstract class SimplePool<T> {
	public final Array<T> objects;
	
	public SimplePool() {
		objects = new Array<>();
	}
	
	public SimplePool(int size) {
		objects = new Array<>(size);
	}
	
	public T obtain() {
		return objects.isEmpty() ? newObject() : objects.pop();
	}
	
	public void free(T object) {
		objects.add(object);
		if (object instanceof Poolable) {
			((Poolable) object).reset();
		}
	}
	
	public void freeAll(Array<? extends T> array) {
		for (T object : array) {
			free(object);
		}
	}
	
	protected abstract T newObject();
}
