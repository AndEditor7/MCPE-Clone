package com.andedit.arcubit.blocks.materials;

public class GlassMaterial extends Material {
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
		return false;
	}
	
	@Override
	public boolean hasCollision() {
		return true;
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
		return false;
	}
}
