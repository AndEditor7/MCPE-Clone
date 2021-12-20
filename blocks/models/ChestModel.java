package com.andedit.arcubit.blocks.models;

import static com.andedit.arcubit.blocks.models.CubeModel.*;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.blocks.Block;
import com.andedit.arcubit.blocks.data.HoriComp;
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

public class ChestModel extends BlockModel {
	
	protected final CubeTex texs;
	protected final HoriComp component;
	
	private final TextureRegion frontChestTex = Assets.getBlockReg(11, 1);
	
	public ChestModel(Block block, CubeTex texs) {
		super(block);
		this.texs = texs;
		this.component = block.getData().getComponent(HoriComp.KEY);
		this.quads.addAll(QUADS);
	}

	@Override
	public void build(QuadBuilder build, BlockPos pos) {
		final int x = pos.x, y = pos.y, z = pos.z;
		final BlockPos offset = build.offset;
		Facing face = component.getFace(pos);
		if (face == null) face = Facing.NORTH;
		
		
		if (canAddFace(pos, offset.set(x, y+1, z), Facing.UP))    {
			build.region = texs.top;
			build.set(quad1).flush(pos);
		}
		if ((y != 0) && canAddFace(pos, offset.set(x, y-1, z), Facing.DOWN))  {
			build.region = texs.bottom;
			build.set(quad2).flush(pos);
		}
		
		final TextureRegion frontTex = getFrontTex(pos);
		if (canAddFace(pos, offset.set(x, y, z-1), Facing.NORTH)) {
			build.region = face == Facing.NORTH ? frontTex : texs.north;
			build.set(quad3).flush(pos);
		}
		if (canAddFace(pos, offset.set(x+1, y, z), Facing.EAST))  {
			build.region = face == Facing.EAST ? frontTex : texs.east;
			build.set(quad4).flush(pos);
		}
		if (canAddFace(pos, offset.set(x, y, z+1), Facing.SOUTH)) {
			build.region = face == Facing.SOUTH ? frontTex : texs.south;
			build.set(quad5).flush(pos);
		}
		if (canAddFace(pos, offset.set(x-1, y, z), Facing.WEST))  {
			build.region = face == Facing.WEST ? frontTex : texs.west;
			build.set(quad6).flush(pos);
		}
	}
	
	@Override
	public void draw(EntityBatch batch, int type) {
		final float a = 0.5f;

		batch.setTexture(texs.north.getTexture());
		for (QuadNode quad : quads) {
			final TextureRegion reg;
			switch (quad.face) {
				case UP: reg = texs.top; break;
				case DOWN: reg = texs.bottom; break;
				case NORTH: reg = getFrontTex(null); break;
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
	
	protected TextureRegion getFrontTex(BlockPos pos) {
		return frontChestTex;
	}

	@Override
	public Array<BoundingBox> getBoundingBoxes(BlockPos pos) {
		return CUBE_BOX;
	}

	@Override
	public TextureRegion getDefaultTex(int data) {
		return frontChestTex;
	}
	
	@Override
	public CubeTex getCubeTex(int type) {
		return texs;
	}
}
