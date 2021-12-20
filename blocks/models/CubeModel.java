package com.andedit.arcubit.blocks.models;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.blocks.Block;
import com.andedit.arcubit.blocks.utils.CubeTex;
import com.andedit.arcubit.graphics.EntityBatch;
import com.andedit.arcubit.graphics.quad.QuadBuilder;
import com.andedit.arcubit.graphics.quad.QuadNode;
import com.andedit.arcubit.utils.ArrayDir;
import com.andedit.arcubit.utils.BlockPos;
import com.andedit.arcubit.utils.Facing;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;

public class CubeModel extends BlockModel {
	
	static final CubeTex MISSING = new CubeTex(Assets.MISSING);
	
	static final Array<BoundingBox> CUBE_BOX;
	
	final static QuadNode quad1, quad2, quad3, quad4, quad5, quad6;
	final static Array<QuadNode> QUADS = new Array<>(6);

	static {
		CUBE_BOX = new Array<BoundingBox>(1);
		CUBE_BOX.add(new BoundingBox(MIN.set(0, 0, 0), MAX.set(1, 1, 1)));
		
		quad1 = new QuadNode();
		quad1.p1.set(1, 1, 1);
		quad1.p2.set(1, 1, 0);
		quad1.p3.set(0, 1, 0);
		quad1.p4.set(0, 1, 1);
		quad1.face = Facing.UP;
		
		quad2 = new QuadNode();
		quad2.p1.set(1, 0, 0);
		quad2.p2.set(1, 0, 1);
		quad2.p3.set(0, 0, 1);
		quad2.p4.set(0, 0, 0);
		quad2.face = Facing.DOWN;
		
		quad3 = new QuadNode();
		quad3.p1.set(0, 0, 0);
		quad3.p2.set(0, 1, 0);
		quad3.p3.set(1, 1, 0);
		quad3.p4.set(1, 0, 0);
		quad3.face = Facing.NORTH;
		
		quad4 = new QuadNode();
		quad4.p1.set(1, 0, 0);
		quad4.p2.set(1, 1, 0);
		quad4.p3.set(1, 1, 1);
		quad4.p4.set(1, 0, 1);
		quad4.face = Facing.EAST;
		
		quad5 = new QuadNode();
		quad5.p1.set(1, 0, 1);
		quad5.p2.set(1, 1, 1);
		quad5.p3.set(0, 1, 1);
		quad5.p4.set(0, 0, 1);
		quad5.face = Facing.SOUTH;
		
		quad6 = new QuadNode();
		quad6.p1.set(0, 0, 1);
		quad6.p2.set(0, 1, 1);
		quad6.p3.set(0, 1, 0);
		quad6.p4.set(0, 0, 0);
		quad6.face = Facing.WEST;
		
		QUADS.add(quad3, quad2, quad1);
		QUADS.add(quad4, quad5, quad6);
	}
	
	private final ArrayDir<CubeTex> textures;
	
	public CubeModel(TextureRegion texture) {
		this(new ArrayDir<>(CubeTex.class, 1).put(0, new CubeTex(texture)));
	}
	
	public CubeModel(CubeTex textures) {
		this(new ArrayDir<>(CubeTex.class, 1).put(0, textures));
	}
	
	public CubeModel(ArrayDir<CubeTex> textures) {
		this.textures = textures;
		
		quads.addAll(QUADS);
		boxes.addAll(CUBE_BOX);
	}
	
	public CubeModel(Block block, TextureRegion texture) {
		this(block, new ArrayDir<>(CubeTex.class, 1).put(0, new CubeTex(texture)));
	}
	
	public CubeModel(Block block, CubeTex textures) {
		this(block, new ArrayDir<>(CubeTex.class, 1).put(0, textures));
	}
	
	public CubeModel(Block block, ArrayDir<CubeTex> textures) {
		super(block);
		this.textures = textures;
		
		quads.addAll(QUADS);
		boxes.addAll(CUBE_BOX);
	}

	@Override
	public void build(QuadBuilder build, BlockPos pos) {
		final int x = pos.x, y = pos.y, z = pos.z;
		final BlockPos offset = build.offset;
		
		final CubeTex texs = getTex(pos);
		
		if (canAddFace(pos, offset.set(x, y+1, z), Facing.UP))    {
			build.region = texs.top;
			build.set(quad1).flush(pos);
		}
		if (canAddFace(pos, offset.set(x, y-1, z), Facing.DOWN))  {
			build.region = texs.bottom;
			build.set(quad2).flush(pos);
		}
		if (canAddFace(pos, offset.set(x, y, z-1), Facing.NORTH)) {
			build.region = texs.north;
			build.set(quad3).flush(pos);
		}
		if (canAddFace(pos, offset.set(x+1, y, z), Facing.EAST))  {
			build.region = texs.east;
			build.set(quad4).flush(pos);
		}
		if (canAddFace(pos, offset.set(x, y, z+1), Facing.SOUTH)) {
			build.region = texs.south;
			build.set(quad5).flush(pos);
		}
		if (canAddFace(pos, offset.set(x-1, y, z), Facing.WEST))  {
			build.region = texs.west;
			build.set(quad6).flush(pos);
		}
	}
	
	protected CubeTex getTex(BlockPos pos) {
		final int type = block.getType(pos);
		return type > block.getNumsType() ? MISSING : textures.get(type);
	}

	@Override
	public void draw(EntityBatch batch, int type) {
		final float a = 0.5f;

		final CubeTex texs = textures.get(type);
		batch.setTexture(texs.north.getTexture());
		for (QuadNode quad : quads) {
			final TextureRegion reg;
			switch (quad.face) {
				case UP: reg = texs.top; break;
				case DOWN: reg = texs.bottom; break;
				case NORTH: reg = texs.north; break;
				case EAST: reg = texs.east; break;
				case SOUTH: reg = texs.south; break;
				case WEST: reg = texs.west; break;
				default: reg = null;
			}

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

	@Override
	public TextureRegion getDefaultTex(int data) {
		return getCubeTex(data).north;
	}
	
	@Override
	public CubeTex getCubeTex(int type) {
		return textures.get(type);
	}

	@Override
	public Array<BoundingBox> getBoundingBoxes(BlockPos pos) {
		return CUBE_BOX;
	}
}
