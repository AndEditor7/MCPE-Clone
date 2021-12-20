package com.andedit.arcubit.blocks.materials;

public class BarrierMaterial extends Material {
	
	@Override
	public boolean isFullCube() {
		return true;
	}
	
	@Override
	public boolean isTransparent() {
		return false;
	}

	@Override
	public boolean isSolid() {
		return true;
	}
	
	@Override
	public boolean hasCollision() {
		return true;
	}

	@Override
	public boolean canBlockSunRay() {
		return true;
	}

	@Override
	public boolean canBlockLights() {
		return true;
	}
}
