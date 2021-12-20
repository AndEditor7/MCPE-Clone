package com.andedit.arcubit.utils;

public class IntegerArray {
	private final int[] array;
	
	public IntegerArray(int size) {
		array = new int[size];
	}
	
	public int get(int i) {
		return array[i];
	}
	
	public void set(int i, int newInt) {
		array[i] = newInt;
	}
}
