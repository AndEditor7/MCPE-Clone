package com.andedit.arcubit.handles;

import static com.badlogic.gdx.math.Interpolation.fastSlow;
import static com.andedit.arcubit.options.Options.noSounds;

import java.util.Random;

import com.badlogic.gdx.audio.Sound;

public final class Audio {	
	private static final Random rand = new Random();
	
	public final Sound[] sounds;
	
	public Audio(Sound... sounds) {
		this.sounds = sounds;
	}
	
	public Sound getSound() {
		return sounds[size() == 1 ? 0 : rand.nextInt(size())]; 
	}
	
	public int size() {
		return sounds.length;
	}
	
	public void play() {
		if (!noSounds.value) getSound().play();
	}
	
	public void play(float volume) {
		if (!noSounds.value) getSound().play(volume);
	}
	
	public void play(float volume, float pitch) {
		if (!noSounds.value) getSound().play(volume, pitch, 0);
	}
	
	public void play3d(float dist, float range) {
		play3d(dist, range, 1);
	}
	
	public void play3d(float dist, float range, float volume) {
		dist = range - dist;
		if (dist > 0f && !noSounds.value) {
			getSound().play(fastSlow.apply(dist / range) * volume);
		}
	}
}
