package com.andedit.arcubit.blocks.models;

import static com.andedit.arcubit.world.World.world;

import com.andedit.arcubit.blocks.Block;
import com.andedit.arcubit.blocks.utils.CubeTex;
import com.andedit.arcubit.graphics.EntityBatch;
import com.andedit.arcubit.graphics.quad.QuadBuilder;
import com.andedit.arcubit.graphics.quad.QuadNode;
import com.andedit.arcubit.utils.BlockPos;
import com.andedit.arcubit.utils.Facing;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;

public abstract class BlockModel {
	protected final Array<BoundingBox> boxes = new Array<>(4);
	
	/** Optional static Vectors for creating bounding boxes. */
	protected static final Vector3 MIN = new Vector3(), MAX = new Vector3(), VEC = new Vector3();
	
	protected Block block;
	
	public final Array<QuadNode> quads = new Array<>();
	
	private final IntMap<CubeTex> cubeMap = new IntMap<CubeTex>();
	
	public BlockModel() {
		
	}
	
	public BlockModel(Block block) {
		this.block = block;
	}
	
	public void setBlock(Block block) {
		this.block = block;
	}
	
	/* Draw item entity */
	public void draw(EntityBatch batch, int type) {
		final float a = 0.5f;
		final TextureRegion reg = getDefaultTex(type);
		batch.setTexture(reg.getTexture());
		for (QuadNode quad : quads) {
			final Vector3 p1 = quad.p1, p2 = quad.p2, p3 = quad.p3, p4 = quad.p4;
			
			batch.pos(p1.x-a, p1.y-a, p1.z-a);
			batch.light();
			batch.tex(reg.getU2(), reg.getV2());
			
			batch.pos(p2.x-a, p2.y-a, p2.z-a);
			batch.light();
			batch.tex(reg.getU2(), reg.getV());
			
			batch.pos(p3.x-a, p3.y-a, p3.z-a);
			batch.light();
			batch.tex(reg.getU(), reg.getV());
			
			batch.pos(p4.x-a, p4.y-a, p4.z-a);
			batch.light();
			batch.tex(reg.getU(), reg.getV2());
		}
	}
	
	public abstract void build(QuadBuilder build, BlockPos pos);
	
	/** Get bounding box. */
	public abstract Array<BoundingBox> getBoundingBoxes(BlockPos pos);
	
	/** Get default texture. */
	public abstract TextureRegion getDefaultTex(int type);
	
	/** Get default texture. */
	public TextureRegion getDefaultTex(BlockPos pos) {
		return getDefaultTex(block.getType(pos));
	};
	
	public CubeTex getCubeTex(int type) {
		CubeTex tex = cubeMap.get(type);
		if (tex == null) {
			tex = new CubeTex(getDefaultTex(type));
			cubeMap.put(type, tex);
		}
		return tex;
	}
	
	public CubeTex getCubeTex(BlockPos pos) {
		return getCubeTex(block.getType(pos));
	}
	
	/** Can this block add from secondary block. */
	public boolean canAddFace(BlockPos primaray, BlockPos secondary, Facing face) {
		final Block block2 = world.getBlock(secondary);
		if (block2.isAir()) return true;
		
		final boolean first  = this.isFaceSolid(primaray,  face);
		final boolean second = block2.isFaceSolid(secondary, face.invert());
		
		if (first && second)
			return false;
		if (first && !second)
			return true; // primary is solid and secondary is trans.
		if (!first && second)
			return false;// primary is trans and secondary is solid.
		
		return block != block2;
	}
	
	/** Is this block has solid face. */
	public boolean isFaceSolid(BlockPos pos, Facing face) {
		return block.getMaterial().isSolid();
	}

	public boolean use3D() {
		return true;
	}
	
	public boolean useFullCube() {
		return false;
	}

	public Array<QuadNode> getQuads() {
		return quads;
	}
}
