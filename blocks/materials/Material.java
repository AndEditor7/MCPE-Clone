package com.andedit.arcubit.blocks.materials;

public class Material {

	/* Static materials */
	public static final Material AIR = new AirMaterial();
	public static final Material BLOCK = new Material();
	public static final Material LEAVES = new LeavesMaterial();
	public static final Material WATER = new LiquidMaterial(true);
	public static final Material LAVA = new LiquidMaterial(false);
	public static final Material SLAB = new SlabMaterial();
	public static final Material CACTUS = new CactusMaterial();
	public static final Material DIAGONAL = new DiagonalMaterial();
	public static final Material TORCH = new TorchMaterial();
	public static final Material DOOR = new DoorMaterial();
	public static final Material BARRIER = new BarrierMaterial();
	public static final Material GLASS = new GlassMaterial();
	public static final Material SNOW = new SnowMaterial();
	public static final Material LADDER = new LadderMaterial();

	/* Getters */

	/** Is full cube. The default is true. */
	public boolean isFullCube() {
		return true;
	}
	
	/** Is block transparent, then put block model in transparent mesh. */
	public boolean isTransparent() {
		return false;
	}

	/** Is block solid. The default is <code>isSolid = isFullCube()</code>. If not true, then it's transparent block. 
	 * @return true if this block has all side solid. Else return false for transparently or custom solid sides. */
	public boolean isSolid() {
		return isFullCube();
	}
	
	/** Is block has Collision. The default is <code>isFullCube()</code>. */
	public boolean hasCollision() {
		return isFullCube();
	}

	/** Can it blocks sun's ray. The default is <code>canBlockLights()</code>. */
	public boolean canBlockSunRay() {
		return canBlockLights();
	}

	/** Can it blocks flood lights. The default is <code>isSolid()</code>. */
	public boolean canBlockLights() {
		return isSolid();
	}
	
	public boolean destoryByWater() {
		return false;
	}
	
	public boolean spawnable() {
		return isFullCube() && hasCollision();
	}

	/* Utilities */

	private static final StringBuilder build = new StringBuilder();

	@Override
	public String toString() {
		build.setLength(0);
		build.append("isFullCube: ").append(isFullCube()).append('\n');
		build.append("isSolid: ").append(isSolid()).append('\n');
		build.append("hasCollision: ").append(hasCollision()).append('\n');
		build.append("canBlockSunRay: ").append(canBlockSunRay()).append('\n');
		build.append("canBlockLights: ").append(canBlockLights()).append('\n');
		return build.toString();
	}
}
