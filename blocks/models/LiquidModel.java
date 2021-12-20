package com.andedit.arcubit.blocks.models;


import static com.andedit.arcubit.world.World.world;

import com.andedit.arcubit.blocks.Block;
import com.andedit.arcubit.blocks.LiquidBlock;
import com.andedit.arcubit.blocks.WaterBlock;
import com.andedit.arcubit.blocks.data.LiquidComp;
import com.andedit.arcubit.blocks.models.CubeModel;
import com.andedit.arcubit.graphics.quad.QuadBuilder;
import com.andedit.arcubit.graphics.quad.QuadNode;
import com.andedit.arcubit.utils.BlockPos;
import com.andedit.arcubit.utils.Facing;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;

public class LiquidModel extends BlockModel {
	
	private final QuadNode quad1, quad2, quad3, quad4, quad5, quad6;
	private final TextureRegion texture;
	private final LiquidBlock block;
	private final LiquidComp comp;

	public LiquidModel(LiquidBlock block, TextureRegion texture) {
		super(block);
		this.texture = texture;
		this.block = block;
		this.comp = block.getComp();
		final boolean isWater = block instanceof WaterBlock;
		
		boxes.add(new BoundingBox());
		
		quad1 = CubeModel.quad1.clone();
		quad1.simpleLight = true;
		
		quad2 = CubeModel.quad2.clone();
		quad2.simpleLight = true;
		
		quad3 = CubeModel.quad3.clone();
		quad3.simpleLight = true;
		
		quad4 = CubeModel.quad4.clone();
		quad4.simpleLight = true;
		
		quad5 = CubeModel.quad5.clone();
		quad5.simpleLight = true;
		
		quad6 = CubeModel.quad6.clone();
		quad6.simpleLight = true;
		
		quads.add(quad1, quad2, quad3);
		quads.add(quad4, quad5, quad6);
	}

	private static final float[][] MAP = new float[3][3];
	@Override
	public void build(QuadBuilder build, BlockPos pos) {
		final int x = pos.x, y = pos.y, z = pos.z;
		final BlockPos offset = build.offset;
		final int level = comp.getLevel(pos);
		
		build.region();
		build.region.setRegion(texture);
		build.set(quad1);
		
		float f1=0, f2=0, f3=0, f4=0;
		final boolean isAbove = world.getBlock(offset.set(x, y+1, z)) == block;
		final boolean isBelow = world.getBlock(offset.set(x, y-1, z)) == block && !comp.isSource(pos);
			
		if (!isAbove) {
			for (int xx = -1; xx < 2; xx++)
			for (int zz = -1; zz < 2; zz++) {
				final Block blk = world.getBlock(offset.set(x+xx, y, z+zz));
				if (blk == block) {
					MAP[xx+1][zz+1] = toWaterLevel(comp.getLevel(offset));
				} else {
					MAP[xx+1][zz+1] = Float.NaN;
				}
				
				offset.y++;
				if (world.getBlock(offset) == block) {
					MAP[xx+1][zz+1] = 100;
				}
			}
			
			int i = 0;
			for (int xx = 0; xx < 2; xx++)
			for (int zz = 0; zz < 2; zz++) {
				if (Float.isNaN(MAP[xx][zz])) continue;
				f3 += MAP[xx][zz]; i++;
			}
			if (i == 1 && isBelow) {
				f3 = toWaterLevel(0);
			} else {
				f3 /= (float)i;
			}
			
			
			i = 0;
			for (int xx = 0; xx < 2; xx++)
			for (int zz = 0; zz < 2; zz++) {
				if (Float.isNaN(MAP[xx+1][zz+1])) continue;
				f1 += MAP[xx+1][zz+1]; i++;
			}
			if (i == 1 && isBelow) {
				f1 = toWaterLevel(0);
			} else {
				f1 /= (float)i;
			}
			
			i = 0;
			for (int xx = 0; xx < 2; xx++)
			for (int zz = 0; zz < 2; zz++) {
				if (Float.isNaN(MAP[xx+1][zz])) continue;
				f2 += MAP[xx+1][zz]; i++;
			}
			if (i == 1 && isBelow) {
				f2 = toWaterLevel(0);
			} else {
				f2 /= (float)i;
			}
			
			i = 0;
			for (int xx = 0; xx < 2; xx++)
			for (int zz = 0; zz < 2; zz++) {
				if (Float.isNaN(MAP[xx][zz+1])) continue;
				f4 += MAP[xx][zz+1]; i++;
			}
			if (i == 1 && isBelow) {
				f4 = toWaterLevel(0);
			} else {
				f4 /= (float)i;
			}
			
			if (comp.isFalling(pos)) {
				
			}
			
			f1 = Math.min(f1, 1f);
			f2 = Math.min(f2, 1f);
			f3 = Math.min(f3, 1f);
			f4 = Math.min(f4, 1f);
			build.p1.y = f1;
			build.p2.y = f2; 
			build.p3.y = f3; 
			build.p4.y = f4;
			build.flush(pos);
		}
		
		if (canAddFace(pos, offset.set(x, y-1, z), Facing.DOWN))  {
			build.set(quad2).flush(pos);
		}
		
		if (isAbove) {
			f1 = 1f;
			f2 = 1f;
			f3 = 1f;
			f4 = 1f;
		}
		
		build.region.setV2(MathUtils.lerp(build.region.getV2(), texture.getV(), (block.getMaxFlow()-level)/(float)(block.getLightLevel()+1)));
		if (canAddFace(pos, offset.set(x, y, z-1), Facing.NORTH)) {
			build.set(quad3);
			build.p2.y = f3; 
			build.p3.y = f2; 
			build.calcRegion(texture);
			build.flush(pos);
		}
		if (canAddFace(pos, offset.set(x+1, y, z), Facing.EAST)) {
			build.set(quad4);
			build.p2.y = f2; 
			build.p3.y = f1; 
			build.calcRegion(texture);
			build.flush(pos);
		}
		
		if (canAddFace(pos, offset.set(x, y, z+1), Facing.SOUTH)) {
			build.set(quad5);
			build.p2.y = f1; 
			build.p3.y = f4;
			build.calcRegion(texture);
			build.flush(pos);
		}
		
		if (canAddFace(pos, offset.set(x-1, y, z), Facing.WEST)) {
			build.set(quad6);
			build.p2.y = f4; 
			build.p3.y = f3;
			build.calcRegion(texture);
			build.flush(pos);
		}
	}
	
	private float toWaterLevel(int level) {
		return (level + 1) / (float)(block.getMaxFlow()+2);
	}

	@Override
	public TextureRegion getDefaultTex(int data) {
		return texture;
	}
	
	public Array<BoundingBox> getBoundingBoxes(BlockPos pos) {
		boxes.get(0).set(MIN.setZero(), MAX.set(1, toWaterLevel(comp.getLevel(pos)), 1));
		return boxes;
	}
}
