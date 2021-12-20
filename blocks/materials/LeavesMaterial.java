package com.andedit.arcubit.blocks.materials;

public class LeavesMaterial extends Material {
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
		return false;
	}
	
	@Override
	public boolean spawnable() {
		return false;
	}
}
