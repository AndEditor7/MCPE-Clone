package com.andedit.arcubit;

import java.util.function.Consumer;

import com.andedit.arcubit.handles.Audio;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SoundLoader;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;

public final class Sounds {

	private static AssetManager asset;
	private static Array<Handle> list = new Array<>(50);
	
	public static Audio GRASS;
	public static Audio DIRT;
	public static Audio GRAVEL;
	public static Audio STONE;
	public static Audio SAND;
	public static Audio WOOD;
	public static Audio WOOL;
	public static Audio GLASS;
	
	public static Audio POP, CLICK;
	
	public static Audio OPEN, CLOSE;
	
	public static Audio PIG;
	public static Audio PIG_DEATH;
	
	public static Audio SHEEP;
	
	public static Audio ZOMBIE;
	public static Audio ZOMBIE_HURT;
	public static Audio ZOMBIE_DEATH;
	
	public static Audio SPLASH;
	
	public static Audio HURT;
	
	public static Audio EAT;
	
	public static Audio BREAK;
	public static Audio IGNITE;
	public static Audio FIZZ;
	
	public static void load(AssetManager asset) {
		asset.setLoader(Sound.class, new SoundLoader(asset.getFileHandleResolver()));
		Sounds.asset = asset;
		
		loadSound("step/grass",             t->GRASS=t,       4);
		loadSound("step/dirt",              t->DIRT=t,        4);
		loadSound("step/gravel",            t->GRAVEL=t,      4);
		loadSound("step/stone",             t->STONE=t,       6);
		loadSound("step/sand",              t->SAND=t,        5);
		loadSound("step/wood",              t->WOOD=t,        6);
		loadSound("step/cloth",             t->WOOL=t,        4);
		loadSound("random/glass",           t->GLASS=t,       3);
		loadSound("random/pop",             t->POP=t           );
		loadSound("random/click",           t->CLICK=t         );
		loadSound("random/door_open",       t->OPEN=t          );
		loadSound("random/door_close",      t->CLOSE=t         );
		loadSound("random/splash",          t->SPLASH=t        );
		loadSound("random/hurt",            t->HURT=t          );
		loadSound("random/eat",             t->EAT=t,         3);
		loadSound("random/ignite",          t->IGNITE=t        );
		loadSound("random/fizz",            t->FIZZ=t          );
		loadSound("mob/pig/pig",            t->PIG=t,         3);
		loadSound("mob/sheep/sheep",        t->SHEEP=t,       3);
		loadSound("mob/pig/pigdeath",       t->PIG_DEATH=t     );
		loadSound("mob/zombie/zombie",      t->ZOMBIE=t,      3);
		loadSound("mob/zombie/zombiehurt",  t->ZOMBIE_HURT=t, 2);
		loadSound("mob/zombie/zombiedeath", t->ZOMBIE_DEATH=t  );
		loadSound("random/break",           t->BREAK=t         );
	}
	
	public static void get(AssetManager asset) {
		for (Handle handle : list) handle.load();
		Sounds.asset = null;
		list = null;
	}
	
	public static void click() {
		CLICK.play(0.6f);
	}
	
	private static final StringBuilder build = new StringBuilder("sounds/");
	
	private static void loadSound(String path, Consumer<Audio> consumer) {
		asset.load("sounds/" + path + ".ogg", Sound.class);
		list.add(new Handle(path, consumer, 0));
	}
	
	private static void loadSound(String path, Consumer<Audio> consumer, int num) {
		for (int i = 0; i < num; i++) {
			build.setLength(7);
			build.append(path).append(i+1);
			build.append(".ogg");
			asset.load(build.toString(), Sound.class);
		}
		list.add(new Handle(path, consumer, num));
	}
	
	private static Audio getSound(String path, int num) {
		if (num == 0) {
			return new Audio(asset.get("sounds/" + path + ".ogg", Sound.class));
		}
		
		final Sound[] sounds = new Sound[num];
		for (int i = 0; i < num; i++) {
			build.setLength(7);
			build.append(path).append(i+1);
			build.append(".ogg");
			sounds[i] = asset.get(build.toString(), Sound.class);
		}
		return new Audio(sounds);
	}
	
	private static class Handle {
		final String path;
		final Consumer<Audio> consumer;
		final int num;
		
		Handle(String path, Consumer<Audio> consumer, int num) {
			this.path = path;
			this.consumer = consumer;
			this.num = num;
		}
		
		void load() {
			consumer.accept(getSound(path, num));
		}
	}
}
