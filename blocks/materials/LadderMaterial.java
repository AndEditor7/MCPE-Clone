package com.andedit.arcubit.blocks.materials;

public class LadderMaterial extends Material {
	public boolean isFullCube() {
		return false;
	}
	
	public boolean isTransparent() {
		return false;
	}

	public boolean isSolid() {
		return false;
	}
	
	public boolean hasCollision() {
		return false;
	}

	public boolean canBlockSunRay() {
		return false;
	}

	public boolean canBlockLights() {
		return false;
	}
	
	public boolean destoryByWater() {
		return false;
	}
	
	public boolean spawnable() {
		return false;
	}
}
