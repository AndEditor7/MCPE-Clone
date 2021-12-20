package com.andedit.arcubit.entity;

import static com.andedit.arcubit.world.World.world;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.blocks.Block;
import com.andedit.arcubit.blocks.Blocks;
import com.andedit.arcubit.blocks.models.BlockModel;
import com.andedit.arcubit.file.Properties;
import com.andedit.arcubit.graphics.EntityBatch;
import com.andedit.arcubit.graphics.quad.QuadNode;
import com.andedit.arcubit.items.BlockItem;
import com.andedit.arcubit.utils.BlockPos;
import com.andedit.arcubit.utils.PlaceType;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;

public class FallingBlock extends Entity {
	
	private Block block;
	private int type;
	private boolean isDead;

	@Override
	public Entity setPos(float x, float y, float z) {
		final float s = 0.49f;
		box.set(x-s, y-s, z-s, x+s, y+s, z+s);
		return this;
	}
	
	@Override
	public Vector3 getPos() {
		return pos.set((box.xMin+box.xMax)*0.5f, (box.yMin+box.yMax)*0.5f, (box.zMin+box.zMax)*0.5f);
	}

	@Override
	public void update() {
		vel.y = Math.min(vel.y-0.005f, 2);
		move();
		
		if (onGround) {
			isDead = true;
			getPos();
			pos.y = box.yMin + 0.1f;
			if (isFallable(blockPos.set(pos))) {
				world.setBlock(blockPos, block, type, PlaceType.SET);
			} else {
				world.addEntity(ItemEnitiy.newEntity(blockPos, new BlockItem(block, type)));
			}
		}
	}

	@Override
	public void render(EntityBatch batch) {
		final BlockModel model = block.getBlockModel();
		final int data = world.getData(getPos());
		batch.setTexture(Assets.TERRAIN);
		batch.pushMatrix().setTranslation(pos);
		
		final float a = 0.5f;
		final TextureRegion reg = model.getDefaultTex(type);
		for (QuadNode quad : model.quads) {
			final Vector3 p1 = quad.p1, p2 = quad.p2, p3 = quad.p3, p4 = quad.p4;
			batch.setLight(data, quad.getAmbLit());
			
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
		
		batch.popMatrix();
	}

	@Override
	public boolean isDead() {
		return isDead;
	}

	
	public static FallingBlock newEntity(BlockPos pos) {
		final FallingBlock entity = new FallingBlock();
		entity.block = world.getBlock(pos);
		entity.type = entity.block.getType(pos);
		entity.setPos(0.5f+pos.x, 0.5f+pos.y, 0.5f+pos.z);
		return entity;
	}
	
	public static boolean isFallable(BlockPos pos) {
		final Block block = world.getBlock(pos);
		return block.isAir() || block == Blocks.WATER;
	}
	
	@Override
	public void save(Properties props) {
		super.save(props);
		props.put("block", block);
		props.put("type", type);
	}
	
	@Override
	public void load(Properties props) {
		super.load(props);
		block = props.got("block");
		type = props.got("type");
	}
}
