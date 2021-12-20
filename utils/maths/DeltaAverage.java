package com.andedit.arcubit.utils.maths;

import java.util.Arrays;

public class DeltaAverage {
	private final float[] table;
	private int index;

	public DeltaAverage(int size) {
		table = new float[size];
		Arrays.fill(table, 0.01666f);
	}

	public float getDelta() {
		float time = 0;
		for (float delta : table) {
			time += delta;
		}
		return time / table.length;
	}

	public void putDelta(float delta) {
		table[index] = delta;
		index = (index + 1) % table.length;
	}
}
