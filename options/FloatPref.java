package com.andedit.arcubit.options;

import com.badlogic.gdx.Preferences;

public class FloatPref extends Pref<Float> {
	
	public final float min, max;

	FloatPref(String name, float value, float min, float max) {
		super(name, value);
		this.min = min;
		this.max = max;
	}

	@Override
	void save(Preferences prefs) {
		prefs.putFloat(name, value);
	}

	@Override
	void load(Preferences prefs) {
		value = prefs.getFloat(name, defValue);
	}
}
