package com.andedit.arcubit.blocks.models;

import static com.andedit.arcubit.blocks.TorchBlock.comp;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.blocks.TorchBlock;
import com.andedit.arcubit.graphics.quad.QuadBuilder;
import com.andedit.arcubit.graphics.quad.QuadNode;
import com.andedit.arcubit.utils.BlockPos;
import com.andedit.arcubit.utils.Facing;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;

public class TorchModel extends BlockModel {
	
	private static final float H = 0.2f;
	
	private final TextureRegion texture = Assets.getBlockReg(0, 5);
	private final TextureRegion topTex = new TextureRegion(texture, 7, 6, 2, 2);
	
	private final BoundingBox box;
	
	private final QuadNode quad1, quad2, quad3, quad4, quad5;
	
	public TorchModel(TorchBlock torch) {
		super(torch);
		
		
		final float a = 7/16f;
		final float b = 10/16f;
		final float d = 1/16f;
		box = new BoundingBox(MIN.set(a-d, 0, a-d), MAX.set((1-a)+d, b+d, (1-a)+d));
		boxes.add(new BoundingBox());
		
		quad1 = new QuadNode();
		quad1.p1.set(a, b, a);
		quad1.p2.set(a, b, 1-a);
		quad1.p3.set(1-a, b, 1-a);
		quad1.p4.set(1-a, b, a);
		quad1.face = Facing.UP;
		quad1.noLit();
		
		quad2 = new QuadNode();
		quad2.p1.set(0, 0, a);
		quad2.p2.set(0, 1, a);
		quad2.p3.set(1, 1, a);
		quad2.p4.set(1, 0, a);
		quad2.face = Facing.NORTH;
		quad2.noLit();
		
		quad3 = new QuadNode();
		quad3.p1.set(1-a, 0, 0);
		quad3.p2.set(1-a, 1, 0);
		quad3.p3.set(1-a, 1, 1);
		quad3.p4.set(1-a, 0, 1);
		quad3.face = Facing.EAST;
		quad3.noLit();
		
		quad4 = new QuadNode();
		quad4.p1.set(1, 0, 1-a);
		quad4.p2.set(1, 1, 1-a);
		quad4.p3.set(0, 1, 1-a);
		quad4.p4.set(0, 0, 1-a);
		quad4.face = Facing.SOUTH;
		quad4.noLit();
		
		quad5 = new QuadNode();
		quad5.p1.set(a, 0, 1);
		quad5.p2.set(a, 1, 1);
		quad5.p3.set(a, 1, 0);
		quad5.p4.set(a, 0, 0);
		quad5.face = Facing.WEST;
		quad5.noLit();
	}
	
	@Override
	public void build(QuadBuilder build, BlockPos pos) {
		final Facing face = comp.getFace(pos);
		
		if (face == Facing.UP) {
			build.region = topTex;
			build.set(quad1).flush(pos);
			
			build.region = texture;
			build.set(quad2).flush(pos);
			build.set(quad3).flush(pos);
			build.set(quad4).flush(pos);
			build.set(quad5).flush(pos);
			return;
		}
		
		final float a = 0.2f;
		float x = 0, z = 0;
		switch (face) {
		case NORTH: z = -a; break;
		case SOUTH: z = a; break;
		case EAST:  x = a; break;
		case WEST:  x = -a; break;
		default: break;
		}
		
		final float xPos = -Math.signum(x) * 0.5f;
		final float zPos = -Math.signum(z) * 0.5f;
		
		build.region = topTex;
		build.set(quad1);
		build.add(x+xPos, H, z + zPos);
		build.flush(pos);
		
		x /= 10/16f;
		z /= 10/16f;
		
		build.region = texture;
		build.set(quad2);
		build.add(xPos, H, zPos);
		build.p3.x += x;
		build.p2.x += x;
		build.p3.z += z;
		build.p2.z += z;
		build.flush(pos);
		
		build.set(quad3);
		build.add(xPos, H, zPos);
		build.p3.x += x;
		build.p2.x += x;
		build.p3.z += z;
		build.p2.z += z;
		build.flush(pos);
		
		build.set(quad4);
		build.add(xPos, H, zPos);
		build.p3.x += x;
		build.p2.x += x;
		build.p3.z += z;
		build.p2.z += z;
		build.flush(pos);
		
		build.set(quad5);
		build.add(xPos, H, zPos);
		build.p3.x += x;
		build.p2.x += x;
		build.p3.z += z;
		build.p2.z += z;
		build.flush(pos);
	}

	@Override
	public TextureRegion getDefaultTex(int data) {
		return texture;
	}

	@Override
	public Array<BoundingBox> getBoundingBoxes(BlockPos pos) {
		final Facing face = comp.getFace(pos);
		final BoundingBox box = boxes.first().set(this.box);
		
		if (face == Facing.UP) {
			return boxes;
		}
		
		final float a = 6/16f;
		float x = 0, z = 0;
		switch (face) {
		case NORTH: z = a; break;
		case SOUTH: z = -a; break;
		case EAST:  x = -a; break;
		case WEST:  x = a; break;
		default: break;
		}
		
		box.min.add(x, H, z);
		box.max.add(x, H, z);
		
		return boxes;
	}
	
	@Override
	public boolean use3D() {
		return false;
	}

	
}
