package com.andedit.arcubit.blocks.models;

import static com.andedit.arcubit.world.World.world;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.blocks.Block;
import com.andedit.arcubit.blocks.Blocks;
import com.andedit.arcubit.blocks.SlabBlock;
import com.andedit.arcubit.blocks.data.SlabComp;
import com.andedit.arcubit.graphics.EntityBatch;
import com.andedit.arcubit.graphics.quad.QuadBuilder;
import com.andedit.arcubit.graphics.quad.QuadNode;
import com.andedit.arcubit.utils.ArrayDir;
import com.andedit.arcubit.utils.BlockPos;
import com.andedit.arcubit.utils.Facing;
import com.andedit.arcubit.utils.Facing.Axis;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;

public class SlabModel extends BlockModel {
	
	private final ArrayDir<TextureRegion> sideTexs, topTexs  = new ArrayDir<>(TextureRegion.class, 7);
	
	private final SlabComp comp;
	private final QuadNode quad1, quad2, quad3, quad4, quad5, quad6;

	public SlabModel(Block block) {
		super(block);
		boxes.add(new BoundingBox());
		comp = block.getData().getComponent(SlabComp.KEY);
		
		topTexs.put(SlabBlock.STONE, Assets.getBlockReg(6, 0));
		topTexs.put(SlabBlock.COBBLESONE, Assets.getBlockReg(0, 1));
		topTexs.put(SlabBlock.WOOD, Assets.getBlockReg(4, 0));
		topTexs.put(SlabBlock.BRICK, Assets.getBlockReg(7, 0));
		topTexs.put(SlabBlock.SANDSTONE, Assets.getBlockReg(0, 11));
		topTexs.put(SlabBlock.STONEBRICK, Assets.getBlockReg(6, 3));
		topTexs.put(SlabBlock.QUARTZ, Assets.getBlockReg(2, 14));
		
		sideTexs = topTexs.clone();
		sideTexs.put(SlabBlock.STONE, Assets.getBlockReg(5, 0));
		sideTexs.put(SlabBlock.SANDSTONE, Assets.getBlockReg(0, 12));
		
		final float a = 0.5f;
		quad1 = new QuadNode();
		quad1.p1.set(1, a, 1);
		quad1.p2.set(1, a, 0);
		quad1.p3.set(0, a, 0);
		quad1.p4.set(0, a, 1);
		quad1.face = Facing.UP;
		
		quad2 = new QuadNode();
		quad2.p1.set(0, 0, 0);
		quad2.p2.set(1, 0, 0);
		quad2.p3.set(1, 0, 1);
		quad2.p4.set(0, 0, 1);
		quad2.face = Facing.DOWN;
		
		quad3 = new QuadNode();
		quad3.p1.set(0, 0, 0);
		quad3.p2.set(0, a, 0);
		quad3.p3.set(1, a, 0);
		quad3.p4.set(1, 0, 0);
		quad3.face = Facing.NORTH;
		
		quad4 = new QuadNode();
		quad4.p1.set(1, 0, 0);
		quad4.p2.set(1, a, 0);
		quad4.p3.set(1, a, 1);
		quad4.p4.set(1, 0, 1);
		quad4.face = Facing.EAST;
		
		quad5 = new QuadNode();
		quad5.p1.set(1, 0, 1);
		quad5.p2.set(1, a, 1);
		quad5.p3.set(0, a, 1);
		quad5.p4.set(0, 0, 1);
		quad5.face = Facing.SOUTH;
		
		quad6 = new QuadNode();
		quad6.p1.set(0, 0, 1);
		quad6.p2.set(0, a, 1);
		quad6.p3.set(0, a, 0);
		quad6.p4.set(0, 0, 0);
		quad6.face = Facing.WEST;
		
		quads.add(quad1, quad2, quad3);
		quads.add(quad4, quad5, quad6);
	}

	@Override
	public void build(QuadBuilder build, BlockPos pos) {
		final int x = pos.x, y = pos.y, z = pos.z;
		final BlockPos offset = build.offset;
		final int type = MathUtils.clamp(block.getType(pos), 0, 5)  ;
		final int loops = comp.isFullBlock(pos)?2:1;
	
		for (int i = 0; i < loops; i++) {
			float upper = comp.isUpper(pos)?0.5f:0;
			if (loops == 2) {
				upper = (i==0)?0:0.5f;
			}
			
			build.region = topTexs.get(type);
			if (canAddFace(pos, offset.set(x, y+1, z), Facing.UP))    {
				build.set(quad1);
				build.add(0, upper, 0);
				build.flush(pos);
			}
			if (canAddFace(pos, offset.set(x, y-1, z), Facing.DOWN))  {
				build.set(quad2);
				build.add(0, upper, 0);
				build.flush(pos);
			}
			
			build.region().setRegion(sideTexs.get(type));
			if (upper < 0.4f) {
				build.region.setRegionY(build.region.getRegionY()+8);
			}
			build.region.setRegionHeight(8);
			
			if (canAddFace(pos, offset.set(x, y, z-1), Facing.NORTH)) {
				build.set(quad3);
				build.add(0, upper, 0);
				build.flush(pos);
			}
			if (canAddFace(pos, offset.set(x+1, y, z), Facing.EAST))  {
				build.set(quad4);
				build.add(0, upper, 0);
				build.flush(pos);
			}
			if (canAddFace(pos, offset.set(x, y, z+1), Facing.SOUTH)) {
				build.set(quad5);
				build.add(0, upper, 0);
				build.flush(pos);
			}
			if (canAddFace(pos, offset.set(x-1, y, z), Facing.WEST))  {
				build.set(quad6);
				build.add(0, upper, 0);
				build.flush(pos);
			}
		}
	}

	@Override
	public Array<BoundingBox> getBoundingBoxes(BlockPos pos) {
		if (comp.isFullBlock(pos)) {
			return CubeModel.CUBE_BOX;
		}
		
		final float upper = comp.isUpper(pos)?0.5f:0;
		boxes.first().set(MIN.set(0, upper, 0), MAX.set(1, 0.5f+upper, 1));
		return boxes;
	}

	@Override
	public TextureRegion getDefaultTex(int data) {
		return topTexs.get(5);
	}
	
	private static final TextureRegion REG = new TextureRegion();
	@Override
	public void draw(EntityBatch batch, int type) {
		final float a = 0.5f;
		final TextureRegion top = topTexs.get(type);
		final TextureRegion side = sideTexs.get(type);
		batch.setTexture(top.getTexture());
		for (QuadNode quad : quads) {
			final Vector3 p1 = quad.p1, p2 = quad.p2, p3 = quad.p3, p4 = quad.p4;
			final TextureRegion reg;
			if (quad == quad1 || quad == quad2) {
				reg = top;
			} else {
				reg = REG;
				reg.setRegion(side);
				reg.setRegionHeight(8);
			}
			
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
	public boolean canAddFace(BlockPos primaray, BlockPos secondary, Facing face) {
		if (face.axis == Axis.Y && !comp.isFullBlock(primaray)) {
			boolean isUpper = comp.isUpper(primaray);
			if ((!isUpper && face == Facing.UP) || (isUpper && face == Facing.DOWN)) {
				return true;
			}
		}
		
		boolean bool = super.canAddFace(primaray, secondary, face);
		if (world.getBlock(secondary) != Blocks.SLAB || comp.isFullBlock(primaray)) {
			return bool;
		}
		
		if (face.isSide()) {
			return comp.isUpper(primaray) != comp.isUpper(secondary);
		}
		
		return bool;
	}
	
	@Override
	public boolean isFaceSolid(BlockPos pos, Facing face) {
		if (comp.isFullBlock(pos)) {
			return true;
		}
		
		return comp.isUpper(pos) ? face == Facing.UP : face == Facing.DOWN;
	}
	
	@Override
	public boolean useFullCube() {
		return true;
	}

}
