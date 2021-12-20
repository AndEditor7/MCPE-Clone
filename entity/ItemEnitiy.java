package com.andedit.arcubit.entity;

import static com.andedit.arcubit.TheGame.game;
import static com.andedit.arcubit.world.World.world;
import static com.badlogic.gdx.math.MathUtils.floor;
import static com.badlogic.gdx.math.MathUtils.random;

import com.andedit.arcubit.Sounds;
import com.andedit.arcubit.blocks.Block;
import com.andedit.arcubit.blocks.models.BlockModel;
import com.andedit.arcubit.file.Properties;
import com.andedit.arcubit.graphics.EntityBatch;
import com.andedit.arcubit.items.BlockItem;
import com.andedit.arcubit.items.Item;
import com.andedit.arcubit.utils.BlockPos;
import com.andedit.arcubit.utils.Util;
import com.andedit.arcubit.utils.maths.CollisionBox;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public final class ItemEnitiy extends Entity {
	
	public static final CollisionBox BOX = new CollisionBox();
	
	public Item item;
	public int timer = 60*480;
	public boolean isDead;
	
	private boolean pickUp;

	public int cooldown = 30;
	
	public ItemEnitiy() {
		
	}
	
	public ItemEnitiy(Item item) {
		this.item = item;
	}

	public ItemEnitiy(BlockPos pos, Item item) {
		this.item = item;
		setPos(pos.x+0.5f, pos.y+0.5f, pos.z+0.5f);
	}
	
	public ItemEnitiy(Vector3 pos, Item item) {
		this.item = item;
		setPos(pos.x, pos.y, pos.z);
	}
	
	private float lerp;
	private Vector3 tmp = new Vector3();
	
	@Override
	public Vector3 getPos() {
		if (pickUp) {
			return tmp.set(pos).lerp(game.steve.player.getPos().add(0,0.9f,0), lerp);
		}
		return super.getPos();
	}
	
	@Override
	public ItemEnitiy setPos(float x, float y, float z) {
		final float s = 0.125f;
		box.set(x - s, y - s, z - s, x + s, y + s, z + s);
		return this;
	}

	@Override
	public void update() {
		
		if (pickUp) {
			lerp += 0.15f;
			isDead |= lerp > 1.0f;
			return;
		}
		
		timer--;
		isDead |= timer < 0;
		
		vel.y -= 0.004f;
		
		if (intersectBlocks()) {
			blockPos.set(getPos().add(0, 0.125f, 0));
			
			final float speed = 0.04f;
			if (!world.getBlock(blockPos.offset(0, -1, 0)).getMaterial().hasCollision()) {
				vel.y = -speed;
			}
			if (!world.getBlock(blockPos.offset(0, 1, 0)).getMaterial().hasCollision()) {
				vel.y = speed;
			}
			if (!world.getBlock(blockPos.offset(-1, 0, 0)).getMaterial().hasCollision()) {
				vel.x = -speed;
			}
			if (!world.getBlock(blockPos.offset(0, 0, -1)).getMaterial().hasCollision()) {
				vel.z = -speed;
			}
			if (!world.getBlock(blockPos.offset(1, 0, 0)).getMaterial().hasCollision()) {
				vel.x = speed;
			}
			if (!world.getBlock(blockPos.offset(0, 0, 1)).getMaterial().hasCollision()) {
				vel.z = speed;
			}
		}
		
		waterFlow();
		final float a;
		if (onGround) {
			a = 0.15f;
		} else {
			a = 0.03f;
		}
		
		vel.x = MathUtils.lerp(vel.x, 0, a);
		vel.y = MathUtils.lerp(vel.y, 0, 0.02f);
		vel.z = MathUtils.lerp(vel.z, 0, a);
		
		spin += 0.02f;
		if (spin > MathUtils.PI2) {
			spin -= MathUtils.PI2;
		}

		if (cooldown > 0) cooldown--;

		// Is player near.
		if (!pickUp && cooldown <= 0 && box.intersects(BOX)) {
			pickUp = game.steve.inventory.addItem(item);
			if (pickUp) Sounds.POP.play(0.2f, 2);
		}
		
		move();
	}
		

	private float spin = random(MathUtils.PI2);
	
	private static final byte[] ARRAY = {2, 17, 33, 49};
	
	@Override
	public void render(EntityBatch batch) {
		final Matrix4 mat = batch.pushMatrix();
		final Matrix4 off = batch.pushMatrix();
		
		final Vector3 pos = getPos();
		
		// set lighting
		batch.setLight(world.getData(floor(pos.x), floor(pos.y+0.125f), floor(pos.z)));
		
		// translate
		mat.trn(pos);
		
		// scale
		final float scale = 0.25f;
		mat.val[Matrix4.M11] *= scale;
		
		if (item instanceof BlockItem && ((BlockItem)item).block.getBlockModel().use3D()) {
			mat.val[Matrix4.M13] += (MathUtils.sin(spin*2f) * 0.08f) + 0.25f;
			
			// rotate
			Util.rotateY(mat, spin, scale);
			
			final BlockModel model = ((BlockItem)item).block.getBlockModel();
			// render the item
			
			model.draw(batch, item.type);
			
			if (item.size >= ARRAY[0]) {
				off.setTranslation(0.5f, 0.5f, 0.5f);
				model.draw(batch, item.type);
			}
			if (item.size >= ARRAY[1]) {
				off.setTranslation(-0.3f, 0.2f, 0.4f);
				model.draw(batch, item.type);
			}
			if (item.size >= ARRAY[2]) {
				off.setTranslation(0.2f, -0.3f, -0.3f);
				model.draw(batch, item.type);
			}
			if (item.size >= ARRAY[3]) {
				off.setTranslation(0.4f, 0.4f, -0.7f);
				model.draw(batch, item.type);
			}
			
		} else {
			///*
			final float yaw = game.getCamera().yaw;
			
			mat.val[Matrix4.M13] += (MathUtils.sin(spin*2f) * 0.08f) + 0.3f;
			
			// rotate
			Util.rotateY(mat, yaw * MathUtils.degreesToRadians, scale);
			//*/
			
			final TextureRegion reg = item.getTexture();
			batch.setTexture(reg.getTexture());
			
			draw(batch, reg);
			
			if (item.size >= ARRAY[0]) {
				off.setTranslation(-0.4f, 0.4f, 0.1f);
				draw(batch, reg);
			}
			
			if (item.size >= ARRAY[1]) {
				off.setTranslation(0.4f, 0.6f, 0.05f);
				draw(batch, reg);
			}
			
			if (item.size >= ARRAY[2]) {
				off.setTranslation(0.2f, -0.3f, -0.1f);
				draw(batch, reg);
			}
			
			if (item.size >= ARRAY[3]) {
				off.setTranslation(-0.4f, -0.2f, -0.15f);
				draw(batch, reg);
			}
		}
		
		// remove the current matrix.
		batch.popMatrix();
		batch.popMatrix();
	}
	
	private static void draw(EntityBatch batch, TextureRegion reg) {
		final float a = 1f;
		batch.pos(-a, -a, 0);
		batch.light();
		batch.tex(reg.getU2(), reg.getV2());
		
		batch.pos(-a, a, 0);
		batch.light();
		batch.tex(reg.getU2(), reg.getV());
		
		batch.pos(a, a, 0);
		batch.light();
		batch.tex(reg.getU(), reg.getV());
		
		batch.pos(a, -a, 0);
		batch.light();
		batch.tex(reg.getU(), reg.getV2());
	}
	
	public void setVel() {
		vel.set(random(-0.05f, 0.05f), random(0.04f, 0.06f), random(-0.05f, 0.05f));
	}
	
	private boolean intersectBlocks() {
		final int xMin = MathUtils.floor(box.xMin);
		final int yMin = MathUtils.floor(box.yMin);
		final int zMin = MathUtils.floor(box.zMin);
		final int xMax = MathUtils.ceil(box.xMax);
		final int yMax = MathUtils.ceil(box.yMax);
		final int zMax = MathUtils.ceil(box.zMax);
		
		for (int x = xMin; x < xMax; x++)
		for (int y = yMin; y < yMax; y++)
		for (int z = zMin; z < zMax; z++) {
			Block block = blockPos.set(x, y, z).getBlock();
			if (block.intersects(blockPos, box) && block.getMaterial().hasCollision()) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public boolean isDead() {
		return isDead;
	}
	
	@Override
	public void save(Properties props) {
		super.save(props);
		
		Properties itemProps = props.newProps("item");
		itemProps.putClass(item);
		item.save(itemProps);
		
		props.put("timer", timer);
	}
	
	@Override
	public void load(Properties props) {
		super.load(props);
		
		Properties itemProps = props.getProps("item");
		item = itemProps.newObject();
		item.load(itemProps);
		
		timer = props.got("timer");
	}
	
	public static ItemEnitiy newEntity(BlockPos pos, Item item) {
		ItemEnitiy enitiy = new ItemEnitiy(item);
		enitiy.setPos(0.5f+pos.x, 0.5f+pos.y, 0.5f+pos.z);
		enitiy.setVel();
		return enitiy;
	}
}
