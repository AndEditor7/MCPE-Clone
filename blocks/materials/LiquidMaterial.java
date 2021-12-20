package com.andedit.arcubit.blocks.materials;

public class LiquidMaterial extends Material {
	
	private final boolean isWater;
	
	LiquidMaterial(boolean isWater) {
		this.isWater = isWater;
	}
	
	@Override
	public boolean isFullCube() {
		return false;
	}
	
	@Override
	public boolean isTransparent() {
		return isWater;
	}
	
	@Override
	public boolean isSolid() {
		return false;
	}

	@Override
	public boolean hasCollision() {
		return false;
	}

	@Override
	public boolean canBlockSunRay() {
		return true;
	}

	@Override
	public boolean canBlockLights() {
		return false;
	}
	
	@Override
	public boolean destoryByWater() {
		return false;
	}
}
