package com.andedit.arcubit.entity.ai;

public interface Ai {
	/** Beginning of the AI. */
	void start();
	/** Update the AI. Return true if finish. */
	boolean update();
	/** Ending of the AI. Return int to wait amount. */
	int finish();
	
	default boolean isRandomPick() {
		return true;
	}
	
	default boolean check() {
		return false;
	}
}
