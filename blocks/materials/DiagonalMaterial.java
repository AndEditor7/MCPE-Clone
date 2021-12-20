package com.andedit.arcubit.blocks.materials;

public class DiagonalMaterial extends Material {
	@Override
	public boolean isFullCube() {
		return false;
	}
	
	@Override
	public boolean isTransparent() {
		return false;
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
		return false;
	}

	@Override
	public boolean canBlockLights() {
		return false;
	}
	
	@Override
	public boolean destoryByWater() {
		return true;
	}
}
