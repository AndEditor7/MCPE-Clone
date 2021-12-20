package com.andedit.arcubit.options;

import static com.andedit.arcubit.ui.OptionUI.*;

import java.lang.reflect.Field;

import com.andedit.arcubit.ui.actors.Preference;
import com.andedit.arcubit.utils.Util;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Array;

public class Options {
	public static final String PATH = "options.txt";
	
	private static final Array<Pref<?>> ARRAY = new Array<>();
	
	// Game
	public static final BoolPref peaceful = new BoolPref("peaceful", false);
	
	// Controls
	public static final FloatPref sens = new FloatPref("sens", 1, 0.2f, 1.8f);
	public static final BoolPref yInvert = new BoolPref("yInvert", false);
	public static final BoolPref split = new BoolPref("split", false);
	public static final BoolPref autoJump = new BoolPref("autoJump", !Util.isDesktop());
	public static final BoolPref lefty = new BoolPref("lefty", false);
	
	// Graphics
	public static final IntPref  dist = new IntPref("dist", 5, 4, 8);
	public static final FloatPref bright = new FloatPref("bright", 0.4f, 0.2f, 0.6f);
	public static final BoolPref niceSky = new BoolPref("niceSky", true);
	public static final BoolPref clouds = new BoolPref("clouds", true);
	public static final BoolPref viewBob = new BoolPref("viewBob", true);
	public static final BoolPref smoothLight = new BoolPref("smoothLight", true);
	
	// Sounds
	public static final BoolPref noSounds = new BoolPref("noSounds", false);
	
	
	static {
		Field[] feilds = Options.class.getFields();
		for (Field field : feilds) {
			if (Pref.class.isAssignableFrom(field.getType())) {
				try {
					ARRAY.add((Pref<?>)field.get(null));
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}
	
	private static void loadPref() {
		gameTab.add(new Preference("Peaceful Mode", peaceful));
		
		controlTab.add(new Preference("Invert Y-Axis", yInvert));
		controlTab.add(new Preference("Split Controls", split));
		controlTab.add(new Preference("Auto Jump", autoJump));
		controlTab.add(new Preference("Lefty", lefty));
		
		graphicTab.add(new Preference("Render Distance", dist));
		graphicTab.add(new Preference("Brightness", bright));
		graphicTab.add(new Preference("Beautiful Skies", niceSky));
		graphicTab.add(new Preference("Render Clouds", clouds));
		graphicTab.add(new Preference("View Bobbing", viewBob));
		graphicTab.add(new Preference("Smooth Lighting", smoothLight));
		
		soundTab.add(new Preference("No Sounds", noSounds));
	}
	
	public static void save() {
		Preferences prefs = getPrefs();
		for (Pref<?> pref : ARRAY) {
			pref.save(prefs);
		}
		prefs.flush();
		prefs.clear();
	}
	
	public static void load() {
		Preferences prefs = getPrefs();
		for (Pref<?> pref : ARRAY) {
			pref.load(prefs);
		}
		prefs.clear();
		
		loadPref();
	}
	
	public static void clear() {
		for (Pref<?> pref : ARRAY) {
			pref.clear();
		}
	}
	
	public static Preferences getPrefs() {
		return Gdx.app.getPreferences(PATH);
	}
}
