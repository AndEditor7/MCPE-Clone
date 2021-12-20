package com.andedit.arcubit.entity.ai;

public class Nothing implements Ai {

	private int count;
	public final int timer;
	
	public Nothing(int timer) {
		this.timer = timer;
	}
	
	@Override
	public void start() {
		count = timer;
	}

	@Override
	public boolean update() {
		if (--count < 0) {
			return true;
		}
		
		return false;
	}

	@Override
	public int finish() {
		return 0;
	}
}
