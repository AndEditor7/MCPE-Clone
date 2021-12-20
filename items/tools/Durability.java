package com.andedit.arcubit.items.tools;

public interface Durability {
	
	int getDamage();
	
	int getMaxDamage();
	
	void takeDamage();
	
	default boolean isFull() {
		return getDamage() >= getMaxDamage();
	}
	
	default float getPercent() {
		return getDamage() / (float)getMaxDamage();
	}
}
