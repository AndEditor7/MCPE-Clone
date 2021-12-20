package com.andedit.arcubit.options;

import com.badlogic.gdx.Preferences;

public class BoolPref extends Pref<Boolean> {

	BoolPref(String name, boolean value) {
		super(name, value);
	}

	@Override
	void save(Preferences prefs) {
		prefs.putBoolean(name, value);
	}

	@Override
	void load(Preferences prefs) {
		value = prefs.getBoolean(name, defValue);
	}
}
