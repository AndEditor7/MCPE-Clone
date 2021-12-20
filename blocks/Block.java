package com.andedit.arcubit.blocks;

import static com.andedit.arcubit.world.World.world;

import com.andedit.arcubit.Steve;
import com.andedit.arcubit.TheGame;
import com.andedit.arcubit.blocks.data.DataManager;
import com.andedit.arcubit.blocks.data.NoComp;
import com.andedit.arcubit.blocks.data.TypeComp;
import com.andedit.arcubit.blocks.materials.Material;
import com.andedit.arcubit.blocks.models.BlockModel;
import com.andedit.arcubit.blocks.utils.BlockType;
import com.andedit.arcubit.blocks.utils.BlockUtils;
import com.andedit.arcubit.blocks.utils.UpdateState;
import com.andedit.arcubit.entity.Entity;
import com.andedit.arcubit.handles.RayContext;
import com.andedit.arcubit.items.BlockItem;
import com.andedit.arcubit.items.Item;
import com.andedit.arcubit.items.tools.ToolType;
import com.andedit.arcubit.utils.ArrayDir;
import com.andedit.arcubit.utils.BlockPos;
import com.andedit.arcubit.utils.Facing;
import com.andedit.arcubit.utils.PlaceType;
import com.andedit.arcubit.utils.maths.CollisionBox;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class Block {
	static final Vector3 MIN = new Vector3(), MAX = new Vector3();
	private static final Array<BoundingBox> EMPTY = new Array<>(0);

	/** Must be in between 0-15 (optional) */
	int lightLevel;

	/** Material of this block. */
	Material material = Material.BLOCK;

	/** Type of tool to use in mining. */
	ToolType toolType = ToolType.NONE;

	BlockType blockType = BlockType.STONE;

	/** Mining level. -1 means it can't be mined. */
	int miningLevel;

	float hardness = 1.0f;

	/** Model of this block. (required) */
	BlockModel model;

	/** The textures for this block item. */
	ArrayDir<TextureRegion> textures;
	
	/** The names for this block item. */
	ArrayDir<String> names;

	/** Block data manager. (optional) */
	final DataManager manager = new DataManager();

	/** ID of this block. */
	public final int id = Blocks.ID++;

	/** Block type component. */
	private TypeComp typeComp = new NoComp();

	private int numsType = Short.MAX_VALUE;
	
	Block() {
		Blocks.addBlock(this);
	}
	
	Block(BlockSetting setting) {
		setSetting(setting);
		Blocks.addBlock(this);
	}
	
	void setSetting(BlockSetting setting) {
		lightLevel = setting.lightLevel;
		material = setting.material;
		toolType = setting.toolType;
		blockType = setting.blockType;
		miningLevel = setting.miningLevel;
		hardness = setting.hardness;
		model = setting.model;
		textures = setting.textures;
		names = setting.names;
		newTypeComponent(setting.typeSize);
		model.setBlock(this);
	}

	/** Set block type component. */
	final void newTypeComponent(int size) {
		if (size < 1) return; 
		typeComp = new TypeComp(size);
		manager.addCompoment(typeComp);
		numsType = size;
	}

	final void setTexture(TextureRegion texture) {
		textures = new ArrayDir<>(TextureRegion.class, 1);
		textures.put(0, texture);
	}
	
	final void setName(String name) {
		names = new ArrayDir<>(String.class, 1);
		names.put(0, name);
	}

	final void setMining(ToolType toolType, int miningLevel, float hardness) {
		this.toolType = toolType;
		this.miningLevel = miningLevel;
		this.hardness = hardness;
	}

	/** Set block type. */
	public final void setType(BlockPos pos, int type) {
		typeComp.setType(pos, type);
	}

	public final int getType(BlockPos pos) {
		return typeComp.getType(pos);
	}

	/** Get the name of this block. */
	public String getName(BlockPos pos) {
		return getName(getType(pos));
	}

	/** Get the name of this block. */
	public String getName(int type) {
		return names == null ? "Unnamed Block" : names.get(type);
	}

	public int getMiningLevel(BlockPos pos) {
		return getMiningLevel(getType(pos));
	}

	public int getMiningLevel(int type) {
		return miningLevel;
	}

	public float getHardness(BlockPos pos) {
		return getHardness(getType(pos));
	}

	public float getHardness(int type) {
		return hardness;
	}
	
	public ToolType getToolType(BlockPos pos) {
		return getToolType(getType(pos));
	}

	public ToolType getToolType(int type) {
		return toolType;
	}
	
	public BlockType getBlockType(BlockPos pos) {
		return getBlockType(getType(pos));
	}

	public BlockType getBlockType(int type) {
		return blockType;
	}
	
	public boolean isFlamable(BlockPos pos) {
		return getBlockType(pos).isFlamable();
	}
	
	public BlockType getStep(BlockPos pos) {
		if (world.getBlock(pos.offset(Facing.UP)) == Blocks.SNOW_LAYER) {
			return BlockType.SNOW;
		}
		
		return getBlockType(pos);
	}

	/** Get material of this block. */
	public final Material getMaterial() {
		return material;
	}

	/** Get model of this block. */
	public final BlockModel getBlockModel() {
		return model;
	}

	public TextureRegion getItemTexture(int type) {
		if (textures == null) {
			return model.getDefaultTex(type);
		}
		return textures.get(type);
	}

	/** Is this block has solid face. */
	public boolean isFaceSolid(BlockPos pos, Facing face) {
		return model == null ? material.isSolid() : model.isFaceSolid(pos, face);
	}

	/** Get bounding boxes of this block. */
	public Array<BoundingBox> getBoundingBoxes(BlockPos pos) {
		return model == null ? EMPTY : model.getBoundingBoxes(pos);
	}

	/** For collision detection. */
	public void addCollisions(BlockPos pos, Array<CollisionBox> boxes, Pool<CollisionBox> pool) {
		if (!material.hasCollision())
			return;

		for (BoundingBox box : getBoundingBoxes(pos)) {
			boxes.add(pool.obtain().set(box).move(pos.x, pos.y, pos.z));
		}
	}
	
	private static final CollisionBox BOX = new CollisionBox();
	public boolean intersects(BlockPos pos, CollisionBox box) {
		for (BoundingBox bound : getBoundingBoxes(pos)) {
			if (box.intersects(BOX.set(bound).move(pos.x, pos.y, pos.z))) {
				return true;
			}
		}
		
		return false;
	}
	
	/** Used for water flows. */ 
	public void inBlockHandle(BlockPos pos, Entity entity) {
		
	}

	/**
	 * Call when player click on the block.
	 * 
	 * @return true to stop placing the block (onPlace).
	 */
	public boolean onClick(BlockPos pos, Steve steve) {
		return false;
	}

	/** return true if success. */
	public boolean onPlace(BlockPos pos, int type, PlaceType place) {
		if (place == PlaceType.PLACE) {
			final Block block = world.getBlock(pos);
			if (block != Blocks.AIR && !(block instanceof LiquidBlock))
				return false;
		}

		world.setData(pos, (world.getData(pos) & BlockUtils.NODATA) | id);
		setType(pos, type);
		return true;
	}

	/** return true if success. */
	public boolean onPlace(RayContext context, int type) {
		return onPlace(context.out, type, PlaceType.PLACE);
	}

	public void onRandonTickUpdate(BlockPos pos) {
	}
	
	public void onUpdateTick(BlockPos pos) {
	}

	/** Add doc plz. */
	public void updateNearByBlocks(BlockPos pos, UpdateState state) {
		BlockPos offset;

		offset = pos.offset(Facing.UP);
		world.getBlock(offset).onNeighbourUpdate(offset, pos, Facing.UP, state);

		offset = pos.offset(Facing.DOWN);
		world.getBlock(offset).onNeighbourUpdate(offset, pos, Facing.DOWN, state);

		offset = pos.offset(Facing.NORTH);
		world.getBlock(offset).onNeighbourUpdate(offset, pos, Facing.NORTH, state);

		offset = pos.offset(Facing.EAST);
		world.getBlock(offset).onNeighbourUpdate(offset, pos, Facing.EAST, state);

		offset = pos.offset(Facing.SOUTH);
		world.getBlock(offset).onNeighbourUpdate(offset, pos, Facing.SOUTH, state);

		offset = pos.offset(Facing.WEST);
		world.getBlock(offset).onNeighbourUpdate(offset, pos, Facing.WEST, state);
	}

	/**
	 * Add doc plz.
	 * 
	 * @param face where the caller facing toward.
	 */
	public void onNeighbourUpdate(BlockPos primaray, BlockPos secondary, Facing face, UpdateState state) {
	}

	/** Is this block contains data. */
	public final boolean hasData() {
		return !manager.isEmpty();
	}

	/** Get block's data component manager. */
	public final DataManager getData() {
		return manager;
	}

	/** Get source light level of this block. */
	public final int getLightLevel() {
		return lightLevel;
	}

	static final Array<Item> LIST = new Array<Item>(8);
	static final Array<Item> dropList(Item item) {
		LIST.size = 0;
		LIST.add(item);
		return LIST;
	}
	static final Array<Item> noDrop() {
		LIST.size = 0;
		return LIST;
	}
	public Array<Item> getDropItem(BlockPos pos) {
		return dropList(new BlockItem(pos));
	}

	public void onDestory(BlockPos pos) {

	}

	/** Is this a source light block. */
	public final boolean isSrclight() {
		return lightLevel != 0;
	}

	/** Is this block air. */
	public boolean isAir() {
		return false;
	}
	
	public int getNumsType() {
		return numsType;
	}

	public static void destroy(RayContext context, boolean dropItem) {
		if (context == null)
			return;
		world.setAir(context.in, PlaceType.PLACE, dropItem);
	}

	public static boolean place(RayContext context, BlockItem item) {
		if (context == null || item == null || item.block == null)
			return false;
		if (world.setBlock(context, item)) {
			if (TheGame.game.isSurvival) item.size--;
			return true;
		}
		return false;
	}
}
