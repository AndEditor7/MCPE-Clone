package com.andedit.arcubit.blocks.models;

import static com.andedit.arcubit.world.World.world;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.blocks.Block;
import com.andedit.arcubit.blocks.Blocks;
import com.andedit.arcubit.graphics.quad.QuadBuilder;
import com.andedit.arcubit.graphics.quad.QuadNode;
import com.andedit.arcubit.utils.BlockPos;
import com.andedit.arcubit.utils.Facing;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;

public class FireModel extends BlockModel {
	
	private final TextureRegion tex = Assets.getBlockReg(14, 0);
	private final QuadNode quad1, quad2, quad3, quad4, quad5;

	public FireModel(Block block) {
		super(block);
		boxes.add(new BoundingBox(MIN.setZero(), MAX.set(1, 0.5f, 1)));
		
		final float a = 0.5f;
		quad1 = new QuadNode();
		quad1.p1.set(0, 0, a);
		quad1.p2.set(0, 1, a);
		quad1.p3.set(1, 1, a);
		quad1.p4.set(1, 0, a);
		quad1.face = Facing.NORTH;
		quad1.noLit();
		
		quad2 = new QuadNode();
		quad2.p1.set(a, 0, 0);
		quad2.p2.set(a, 1, 0);
		quad2.p3.set(a, 1, 1);
		quad2.p4.set(a, 0, 1);
		quad2.face = Facing.EAST;
		quad2.noLit();
		
		quad3 = new QuadNode();
		quad3.p1.set(1, 0, a);
		quad3.p2.set(1, 1, a);
		quad3.p3.set(0, 1, a);
		quad3.p4.set(0, 0, a);
		quad3.face = Facing.SOUTH;
		quad3.noLit();
		
		quad4 = new QuadNode();
		quad4.p1.set(a, 0, 1);
		quad4.p2.set(a, 1, 1);
		quad4.p3.set(a, 1, 0);
		quad4.p4.set(a, 0, 0);
		quad4.face = Facing.WEST;
		quad4.noLit();
		
		quad5 = new QuadNode();
		quad5.p1.set(1, 0.98f, 0);
		quad5.p2.set(1, 0.98f, 1);
		quad5.p3.set(0, 0.98f, 1);
		quad5.p4.set(0, 0.98f, 0);
		quad5.face = Facing.DOWN;
		quad5.noLit();
		
		quads.add(quad1, quad2, quad3, quad4);
	}

	@Override
	public void build(QuadBuilder build, BlockPos pos) {
		final int x = pos.x, y = pos.y, z = pos.z;
		final BlockPos offset = build.offset;
		build.region = tex;
		
		boolean attach = false;
		if (world.getBlock(offset.set(x, y, z+1)).isFlamable(offset)) {
			build.set(quad1);
			build.add(0, 0, 0.48f);
			build.flush(pos);
			attach = true;
		}
		
		if (world.getBlock(offset.set(x, y, z-1)).isFlamable(offset)) {
			build.set(quad3);
			build.add(0, 0, -0.48f);
			build.flush(pos);
			attach = true;
		}
		
		if (world.getBlock(offset.set(x+1, y, z)).isFlamable(offset)) {
			build.set(quad4);
			build.add(0.48f, 0, 0);
			build.flush(pos);
			attach = true;
		}
		
		if (world.getBlock(offset.set(x-1, y, z)).isFlamable(offset)) {
			build.set(quad2);
			build.add(-0.45f, 0, 0);
			build.flush(pos);
			attach = true;
		}
		
		if (world.getBlock(offset.set(x, y+1, z)).isFlamable(offset)) {
			build.set(quad5).flush(pos);;
		}
		if (attach ? world.getBlock(offset.set(x, y-1, z)).isFlamable(offset) : 
			world.getBlock(offset.set(x, y-1, z)).isFaceSolid(offset, Facing.UP)) {
			for (int i = 0; i < 4; i++) {
				build.set(quads.get(i)).flush(pos);
			}
		}
	}

	@Override
	public Array<BoundingBox> getBoundingBoxes(BlockPos pos) {
		boxes.first().max.y = world.getBlock(pos.x, pos.y-1, pos.z) != Blocks.AIR ? 0.5f : 1;
		return boxes;
	}

	@Override
	public TextureRegion getDefaultTex(int type) {
		return tex;
	}

}
