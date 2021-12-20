package com.andedit.arcubit.world;

import static com.andedit.arcubit.TheGame.game;
import static com.andedit.arcubit.blocks.Blocks.FURNACE;
import static com.badlogic.gdx.math.MathUtils.random;

import com.andedit.arcubit.blocks.Block;
import com.andedit.arcubit.blocks.Blocks;
import com.andedit.arcubit.blocks.ChestBlock.ChestContent;
import com.andedit.arcubit.blocks.FireBlock;
import com.andedit.arcubit.blocks.furnace.FurnaceContent;
import com.andedit.arcubit.blocks.utils.BlockUtils;
import com.andedit.arcubit.blocks.utils.UpdateState;
import com.andedit.arcubit.entity.Entity;
import com.andedit.arcubit.entity.ItemEnitiy;
import com.andedit.arcubit.entity.Sheep;
import com.andedit.arcubit.entity.Zombie;
import com.andedit.arcubit.file.Properties;
import com.andedit.arcubit.file.Serial;
import com.andedit.arcubit.graphics.Renderer;
import com.andedit.arcubit.handles.Inputs;
import com.andedit.arcubit.handles.RayContext;
import com.andedit.arcubit.handles.VoxelRay;
import com.andedit.arcubit.items.BlockItem;
import com.andedit.arcubit.items.Item;
import com.andedit.arcubit.particle.BlockBreak;
import com.andedit.arcubit.particle.Particle;
import com.andedit.arcubit.particle.ParticleSystem;
import com.andedit.arcubit.utils.ArraySerial;
import com.andedit.arcubit.utils.BlockPos;
import com.andedit.arcubit.utils.Camera;
import com.andedit.arcubit.utils.IntegerArray;
import com.andedit.arcubit.utils.PlaceType;
import com.andedit.arcubit.world.lights.LightHandle;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint3;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.OrderedMap;

public final class World implements Disposable, Serial {
	public static final World world = new World();
	private World() {}
	
	private static final BlockPos blockPos = new BlockPos();
	private static final BlockPos blockPos2 = new BlockPos();
	
	public static final int AMOUNT = 4;
	public static final int SIZE = 512, HEIGHT = 128;
	public static final int SEA_LEVEL = 64;
	//private final int[][][] data = new int[SIZE][HEIGHT][SIZE];
	public static final IntegerArray data = new IntegerArray(SIZE * HEIGHT * SIZE);
	public final short[][] shadowMap = new short[SIZE][SIZE];
	
	private double cycle = 0.2; // 0.25
	public int level;
	
	private Renderer render;
	public final LightHandle light = new LightHandle(true);
	
	public final Array<Entity> entities = new Array<>(false, 64);
	public final Array<ItemEnitiy> items = new Array<>(false, 64);
	
	public final MobManager manager = new MobManager();
	public final OrderedMap<BlockPos, ChestContent> chests = new OrderedMap<>();
	
	public final Array<FurnaceContent> activeFurns = new Array<>();
	public final OrderedMap<BlockPos, FurnaceContent> furnaces = new OrderedMap<>();
	
	public final ArraySerial<BlockPos> fireTick = new ArraySerial<>(BlockPos.class, 64);
	
	private final ArraySerial<BlockPos> waterQue = new ArraySerial<>(BlockPos.class, 64);
	private final Array<BlockPos> waterExe = new Array<>(64);
	
	private final ArraySerial<BlockPos> lavaQue = new ArraySerial<>(BlockPos.class, 64);
	private final Array<BlockPos> lavaExe = new Array<>(64);
	
	private final ArraySerial<BlockPos> updateQue = new ArraySerial<>(BlockPos.class, 64);
	private final Array<BlockPos> updateExe = new Array<>(64);
	
	public void ints() {
		manager.ints();
		cycle = 0.2;
	}
	
	public void newRender() {
		render = new Renderer();
	}

	public Renderer getRender() {
		return render;
	}
	
	public void buildShadowMap() {
		for (int x = 0; x < World.SIZE; x++)
		for (int z = 0; z < World.SIZE; z++) {
			world.shadowMap[x][z] = 0;
			for (int y = World.HEIGHT-1; y >= 0; y--) {
				if (Blocks.get(world.getData(x, y, z)).getMaterial().canBlockSunRay()) {
					world.shadowMap[x][z] = (short)y;
					break;
				}
			}
		}
	}
	
	private int waterTimer, lavaTimer, fireTimer;
	private final GridPoint3 chunkPos = new GridPoint3();
	
	public void update() {
		cycle = Math.PI;
		
		if (Inputs.isKeyJustPressed(Keys.G)) {
			final Vector3 pos = game.steve.camera.position;
			addEntity(new Zombie().setPos(pos.x, pos.y, pos.z));
		}
		if (Inputs.isKeyJustPressed(Keys.H)) {
			final Vector3 pos = game.steve.camera.position;
			addEntity(new Sheep().setPos(pos.x, pos.y, pos.z));
		}
		
		// Cycle update.
		cycle += cycle > Math.PI ? 0.000145d : 0.000105d;
		//cycle += 0.001d;
		if (cycle > Math.PI * 2d) {
			cycle -= Math.PI * 2d;
		}
		level = MathUtils.round(MathUtils.lerp(-12, 0, getSunLight()));
		
		// Mob update.
		manager.update(game.player);
		for (int i = 0; i < entities.size; i++) {
			final Entity entity = entities.get(i);
			entity.update();
			if (entity.isDead()) {
				entities.removeIndex(i--);
			}
		}
		for (int i = 0; i < items.size; i++) {
			final Entity entity = items.get(i);
			entity.update();
			if (entity.isDead()) {
				items.removeIndex(i--);
			}
		}
		
		// Furnace update.
		for (int i = 0; i < activeFurns.size; i++) {
			final FurnaceContent content = activeFurns.get(i);
			if (getBlock(content.pos) == FURNACE) {
				content.update();
				if (!content.isSmelting()) {
					FURNACE.setActive(content.pos, false);
					activeFurns.removeIndex(i--);
					dirty(content.pos.x, content.pos.y, content.pos.z);
				}
			} else {
				activeFurns.removeIndex(i--);
			}
		}
		
		// Random block update.
		final Vector3 camPos = game.getCamera().position;
		chunkPos.set(MathUtils.floor(camPos.x)>>4, MathUtils.floor(camPos.y)>>4, MathUtils.floor(camPos.z)>>4);
		for (int x = chunkPos.x-4; x <= chunkPos.x+4; x++)
		for (int y = 0; y < 8; y++)
		for (int z = chunkPos.z-4; z <= chunkPos.z+4; z++) {
			if (Renderer.isOutBound(x, y, z)) continue;
			
			final int bits = random.nextInt();
			final int xPos, yPos, zPos;
			xPos = (bits & 15) + (x << 4);
			yPos = ((bits >>> 4) & 15) + (y << 4);
			zPos = ((bits >>> 8) & 15) + (z << 4);
			
			getBlock(blockPos.set(xPos, yPos, zPos)).onRandonTickUpdate(blockPos);
		}
		
		// Water update.
		if (--waterTimer < 0) {
			waterTimer = 15;
			waterExe.addAll(waterQue);
			waterQue.size = 0;
			
			LiquidFall.update(waterExe, waterQue, Blocks.WATER);
		}
		
		// Lava update.
		if (--lavaTimer < 0) {
			lavaTimer = 60;
			lavaExe.addAll(lavaQue);
			lavaQue.size = 0;
			
			LiquidFall.update(lavaExe, lavaQue, Blocks.LAVA);
		}
		
		// Fire update.
		if (--fireTimer < 0) {
			fireTimer = 24;
			for (int i = 0; i < fireTick.size; i++) {
				BlockPos pos = fireTick.get(i);
				if (pos.getBlock() != Blocks.FIRE) {
					fireTick.removeIndex(i--);
					continue;
				}
				
				if (MathUtils.randomBoolean(0.1f)) {
					if (FireBlock.update(pos)) {
						fireTick.removeIndex(i--);
					}
				}
			}
		}
		
		updateExe.addAll(updateQue);
		updateQue.size = 0;
		for (BlockPos pos : updateExe) {
			getBlock(pos).onUpdateTick(pos);
		}
		updateExe.size = 0;
	}
	
	public float getSunLight() {
		return (float)MathUtils.clamp((Math.sin(cycle) * 2.3d) + 0.3, 0d, 1d);
	}
	
	public float getCycle() {
		return (float)cycle;
	}
	
	public void render(Camera camera) {
		render.render(camera);
	}
	
	private final Array<Entity> entityHits = new Array<Entity>();
	public RayContext shoot(Ray ray) {
		final RayContext context = RayContext.CONTEXT.clear();
		
		VoxelRay.shot(ray, game.isSurvival ? 5 : 8, context);
		
		for (Entity entity : entities) {
			if (ray.origin.dst(entity.getCenter()) < 4.5f && entity.box.intersect(ray)) {
				entityHits.add(entity);
			}
		}
		
		if (entityHits.size == 1) {
			context.entityHit = entityHits.first();
			context.entityDst = entityHits.first().getCenter().dst(ray.origin);
		} else if (entityHits.notEmpty()) {
			Entity target = null;
			float dst = Float.MAX_VALUE;
			for (Entity entity : entityHits) {
				float tmp = entity.getCenter().dst2(ray.origin);
				if (tmp < dst) {
					dst = tmp;
					target = entity;
				}
			}
			context.entityDst = (float)Math.sqrt(dst);
			context.entityHit = target;
		}	
		
		entityHits.clear();
		return context;
	}
	
	public void setBlock(int x, int y, int z, Block block) {
		setBlock(x, y, z, block, 0);
	}
	
	public void setBlock(int x, int y, int z, Block block, int type) {
		world.setData(x, y, z, (world.getData(x, y, z) & BlockUtils.NODATA) | block.id);
		if (type != 0) {
			block.setType(blockPos2.set(x, y, z), type);
		}
	}
	
	public boolean setBlock(RayContext context, BlockItem item) {
		if (isOutBound(context.out.x, context.out.y, context.out.z)) return false;
		if (!item.block.onPlace(context, item.type)) return false;
		return setBlockBase(context.out, item.block, PlaceType.PLACE);
	}
	
	public boolean setBlock(BlockPos pos, BlockItem item, PlaceType type) {
		return setBlock(pos, item.block, item.type, type);
	}
	
	public boolean setBlock(BlockPos pos, Block block, PlaceType placeType) {
		return setBlock(pos, block, 0, placeType);
	}
	
	public boolean setBlock(BlockPos pos, Block block, int type, PlaceType placeType) {
		if (isOutBound(pos.x, pos.y, pos.z)) return false;
		if (!block.onPlace(pos, type, placeType)) return false;
		return setBlockBase(pos, block, placeType);
	}
	
	private boolean setBlockBase(BlockPos pos, Block block, PlaceType type) {
		if (type != PlaceType.NONE) {
			final UpdateState state;
			state = type == PlaceType.PLACE ? UpdateState.ON_PLACE : UpdateState.ON_CHANGE;
			block.updateNearByBlocks(pos, state);
			render.dirty(pos.x, pos.y, pos.z);
			
			if (block.isSrclight()) { // if place srclight block.
				light.newSrclightAt(pos.x, pos.y, pos.z, block.getLightLevel());
			} else { // if place non-srclight block.
				light.delSrclightAt(pos.x, pos.y, pos.z);
			}
			
			light.newRaySunlightAt(pos.x, pos.y, pos.z);
			light.delSunlightAt(pos.x, pos.y, pos.z);
		}
		
		return true;
	}
	
	public boolean isAir(BlockPos pos) {
		return BlockUtils.toBlockID(getData(pos.x, pos.y, pos.z)) == 0;
	}
	
	public void setAir(BlockPos pos, PlaceType type, boolean dropItem) {
		if (isOutBound(pos.x, pos.y, pos.z)) return;
		
		final Block block = world.getBlock(pos);
		
		if (type == PlaceType.PLACE) {
			final TextureRegion texture = block.getBlockModel().getDefaultTex(pos);
			for (int i = 0; i < 20; i++) {
				Particle part = ParticleSystem.newPart(BlockBreak.comp);
				part.setPos(pos.x + MathUtils.random(0.1f, 0.9f), pos.y + MathUtils.random(0.1f, 0.9f), pos.z + MathUtils.random(0.1f, 0.9f));
				part.tex = BlockBreak.getTex(texture);
			}
		}
		
		if (type != PlaceType.NONE) {
			render.dirty(pos.x, pos.y, pos.z);
			
			if (block.isSrclight()) {
				light.delSrclightAt(pos.x, pos.y, pos.z);
			} else {
				light.newSrclightShellAt(pos.x, pos.y, pos.z);
			}

			light.newRaySunlightAt(pos.x, pos.y, pos.z);
			light.newSunlightShellAt(pos.x, pos.y, pos.z);
		}
		
		if (game.isSurvival && dropItem) { 	
			for (Item item : block.getDropItem(pos)) {
				if (item == null) continue;
				for (int i = 0; i < item.size; i++) {
					final ItemEnitiy enitiy = 
					new ItemEnitiy(pos, item.size == 1 ? item : item.clone().size(1));
					enitiy.setVel();
					addEntity(enitiy);
				}
			}
		}

		block.onDestory(pos);
		final int index = getIndex(pos.x, pos.y, pos.z);
		data.set(index, data.get(index) & BlockUtils.NODATA);
		//data[pos.x][pos.y][pos.z] &= BlockUtils.NODATA;
		if (type != PlaceType.NONE) block.updateNearByBlocks(pos, UpdateState.ON_BREAK); 
	}
	
	public Block getBlock(int x, int y, int z) {
		return Blocks.get(getData(x, y, z));
	}
	
	public Block getBlock(BlockPos pos) {
		return Blocks.get(getData(pos));
	}
	
	public Block getBlock(Vector3 pos) {
		return Blocks.get(getData(pos));
	}
	
	public int getData(Vector3 pos) {
		return getData(MathUtils.floor(pos.x), MathUtils.floor(pos.y), MathUtils.floor(pos.z));
	}
	
	public int getData(BlockPos pos) {
		return getData(pos.x, pos.y, pos.z);
	}

	public int getData(int x, int y, int z) {
		if (y >= HEIGHT) {
			return 0xF0000000;
		}
		if (y < 0 || x < 0 || z < 0 || x >= SIZE || z >= SIZE) {
			return Blocks.BARRIER.id | 0xF0000000;
		}
		return data.get(getIndex(x, y, z));
		//return data[x][y][z];
	}
	
	public void setData(int x, int y, int z, int data) {
		if (!isOutBound(x, y, z)) {
			World.data.set(getIndex(x, y, z), data);
			//this.data[x][y][z] = data;
		}
	}
	
	public void addWaterQue(BlockPos pos) {
		waterQue.add(pos);
	}
	
	public void addLavaQue(BlockPos pos) {
		lavaQue.add(pos);
	}
	
	public void addUpdateQue(BlockPos pos) {
		updateQue.add(pos);
	}

	@Override
	public void dispose() {
		ParticleSystem.clear();
		if (render != null) {
			render.dispose();
			render = null;
		}
		
		waterQue.clear();
		waterExe.clear();
		lavaQue.clear();
		lavaExe.clear();
		updateQue.clear();
		updateExe.clear();
		entities.clear();
		items.clear();
		chests.clear();
		activeFurns.clear();
		furnaces.clear();
		fireTick.clear();
	}
	
	private static boolean isOutBound(int x, int y, int z) {
		return x < 0 || y < 0 || z < 0 || x >= SIZE || y >= HEIGHT || z >= SIZE;
	}

	public void setSunLight(int x, int y, int z, int l) {
		final int index = getIndex(x, y, z);
		data.set(index, (data.get(index) & BlockUtils.SUN_INV) | (l << BlockUtils.SUN_SHIFT));
		//data[x][y][z] = (data[x][y][z] & BlockUtils.SUN_INV) | (l << BlockUtils.SUN_SHIFT);
	}
	
	public void setSrcLight(int x, int y, int z, int l) {
		final int index = getIndex(x, y, z);
		data.set(index, (data.get(index) & BlockUtils.SRC_INV) | (l << BlockUtils.SRC_SHIFT));
		//data[x][y][z] = (data[x][y][z] & BlockUtils.SRC_INV) | (l << BlockUtils.SRC_SHIFT);
	}
	
	public static int getIndex(int x, int y, int z) {
		return x + (y * SIZE) + (z * SIZE  * HEIGHT);
	}
	
	public int getShadow(int x, int z) {
		if (x < 0 || z < 0 || x >= SIZE || z >= SIZE)
			return HEIGHT;

		return shadowMap[x][z];
	}
	
	public void dirty(int x, int y, int z) {
		if (render != null) render.dirty(x, y, z);
	}

	public void addActiveFurn(FurnaceContent content) {
		if (getBlock(content.pos) == FURNACE && !activeFurns.contains(content, true)) {
			activeFurns.add(content);
			FURNACE.setActive(content.pos, true);
			dirty(content.pos.x, content.pos.y, content.pos.z);
		}
	}
	
	public void addEntity(Entity entity) {
		if (entity.getClass() == ItemEnitiy.class) {
			items.add((ItemEnitiy)entity);
		} else {
			entities.add(entity);
		}
	}

	public void setData(BlockPos pos, int i) {
		setData(pos.x, pos.y, pos.z, i);
	}

	@Override
	public void save(Properties props) {
		props.put("cycle", cycle);
		props.newProps("entities", entities.size).save(entities, true);
		props.newProps("items", items.size).save(items, false);
		props.newProps("chests", chests.size).save(chests);
		props.newProps("furnaces", furnaces.size).save(furnaces);
		props.put("waterQue", waterQue);
		props.put("lavaQue", lavaQue);
		props.put("updateQue", updateQue);
		props.put("fireTick", fireTick);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void load(Properties props) {
		cycle = props.got("cycle");
		props.getProps("entities").load(entities);
		props.getProps("items").load(items, ItemEnitiy.class);
		props.getProps("chests").load(chests, ChestContent.class);
		props.getProps("furnaces").load(furnaces, FurnaceContent.class, context -> {
			if (context.isSmelting()) activeFurns.add(context);
		});
		
		props.got("waterQue", ArraySerial.class).load(waterQue);
		props.got("lavaQue", ArraySerial.class).load(lavaQue);
		props.got("updateQue", ArraySerial.class).load(updateQue);
		props.got("fireTick", ArraySerial.class).load(fireTick);
	}
}
