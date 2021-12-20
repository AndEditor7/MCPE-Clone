package com.andedit.arcubit.options;
import java.util.function.Consumer;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Array;

public abstract class Pref<T> {
	private final Array<Consumer<T>> consumers = new Array<>(8);
	
	String name;
	public final T defValue;
	public T value;
	
	Pref(String name, T value) {
		this.name = name;
		this.value = value;
		this.defValue = value;
	}
	
	public void set(T value) {
		if (value.equals(this.value)) return;
		this.value = value;
		for (Consumer<T> consumer : consumers) {
			consumer.accept(value);
		}
	}
	
	public void addConsumer(Consumer<T> consumer) {
		consumers.add(consumer);
	}
	
	void clear() {
		consumers.clear();
	}
	
	abstract void save(Preferences prefs);
	
	abstract void load(Preferences prefs);
}
