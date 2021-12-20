package com.andedit.arcubit.options;

import com.badlogic.gdx.Preferences;

public class IntPref extends Pref<Integer> {
	
	public final int min, max;
	
	IntPref(String name, int value, int min, int max) {
		super(name, value);
		this.min = min;
		this.max = max;
	}
	
	@Override
	void save(Preferences prefs) {
		prefs.putInteger(name, value);
	}
	
	@Override
	void load(Preferences prefs) {
		value = prefs.getInteger(name, defValue);
	}
}
