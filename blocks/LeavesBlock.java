package com.andedit.arcubit.blocks;

import static com.andedit.arcubit.blocks.Blocks.LOG;
import static com.andedit.arcubit.world.World.world;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.blocks.materials.Material;
import com.andedit.arcubit.blocks.models.CubeModel;
import com.andedit.arcubit.blocks.utils.CubeTex;
import com.andedit.arcubit.blocks.utils.BlockType;
import com.andedit.arcubit.items.BlockItem;
import com.andedit.arcubit.items.Item;
import com.andedit.arcubit.items.tools.ToolType;
import com.andedit.arcubit.utils.ArrayDir;
import com.andedit.arcubit.utils.BlockPos;
import com.andedit.arcubit.utils.Facing;
import com.andedit.arcubit.utils.PlaceType;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;

public class LeavesBlock extends Block {
	
	private static final int SIZE = 7;
	private static final int MASK = SIZE / 2;
	private static final boolean[][][] TABLE = new boolean[SIZE][SIZE][SIZE];
	private static final Queue<BlockPos> QUEUE = new Queue<>(32);

	private static void reset() {
		for (int x = 0; x < SIZE; x++)
		for (int y = 0; y < SIZE; y++)
		for (int z = 0; z < SIZE; z++) {
			TABLE[x][y][z] = true;
		}
		QUEUE.clear();
	}
	
	private static boolean getBool(int x, int y, int z) {
		x += MASK; y += MASK; z += MASK;
		if (x < 0 || y < 0 || z < 0 || x >= SIZE || y >= SIZE || z >= SIZE) {
			return false;
		}
		
		return TABLE[x][y][z];
	}
	
	private static void setBool(int x, int y, int z, boolean bool) {
		x += MASK; y += MASK; z += MASK;
		if (x < 0 || y < 0 || z < 0 || x >= SIZE || y >= SIZE || z >= SIZE) {
			return;
		}
		
		TABLE[x][y][z] = bool;
	}
	
	public static final int 
	OAK = 0,
	SPRUCE = 1,
	BIRCH = 2;

	LeavesBlock() {
		ArrayDir<CubeTex> texs = new ArrayDir<>(CubeTex.class, 3);
		texs.put(OAK, new CubeTex(Assets.getBlockReg(12, 14)));
		texs.put(SPRUCE, new CubeTex(Assets.getBlockReg(12, 13)));
		texs.put(BIRCH, new CubeTex(Assets.getBlockReg(12, 15)));
		
		this.newTypeComponent(2);
		this.model = new CubeModel(this, texs);
		this.material = Material.LEAVES;
		this.setMining(ToolType.NONE, 0, 0.2f);
		this.blockType = BlockType.LEAVES;
		
		textures = new ArrayDir<>(TextureRegion.class, 3);
		textures.put(OAK, Assets.getItemReg(0, 5));
		textures.put(SPRUCE, model.getDefaultTex(SPRUCE));
		textures.put(BIRCH, model.getDefaultTex(BIRCH));
	}
	
	@Override
	public String getName(int type) {
		return "Leaves";
	}
	
	@Override
	public Array<Item> getDropItem(BlockPos pos) {
		return dropList(MathUtils.randomBoolean(0.1f) ? new BlockItem(Blocks.SABLING) : null);
	}
	
	@Override
	public void onRandonTickUpdate(final BlockPos pos) {
		reset();
		
		boolean logFound = false;
		QUEUE.addFirst(pos);
		setBool(0, 0, 0, false);
		loop : while (QUEUE.notEmpty()) {
			final BlockPos p = QUEUE.removeFirst();
			
			for (int i = 0; i < 6; i++) {
				BlockPos o = p.offset(Facing.get(i));
				Block block = world.getBlock(o);
				if (block == this) {
					if (getBool(o.x-pos.x, o.y-pos.y, o.z-pos.z)) {
						setBool(o.x-pos.x, o.y-pos.y, o.z-pos.z, false);
						QUEUE.addLast(o);
					}
				} else if (block == LOG) {
					logFound = true;
					break loop;
				}
			}
		}
		
		if (!logFound) {
			world.setAir(pos, PlaceType.SET, true);
		}
	}
}