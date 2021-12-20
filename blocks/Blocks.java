package com.andedit.arcubit.blocks;

import static com.andedit.arcubit.Assets.getBlockReg;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.blocks.materials.Material;
import com.andedit.arcubit.blocks.models.BarrierModel;
import com.andedit.arcubit.blocks.models.CubeModel;
import com.andedit.arcubit.blocks.utils.BlockType;
import com.andedit.arcubit.blocks.utils.BlockUtils;
import com.andedit.arcubit.blocks.utils.CubeTex;
import com.andedit.arcubit.items.tools.ToolType;

public final class Blocks {	
	static int ID = 0;
	
	static final Block[] blocks = new Block[256];
	
	public static final Block AIR = new AirBlock();
	public static final Block STONE = new StoneBlock();
	public static final Block GRASS = new GrassBlock();
	public static final Block DIRT;
	public static final Block COBBLESTONE;
	public static final Block PLANK;
	public static final Block BEDROCK;
	public static final WaterBlock WATER = new WaterBlock();
	public static final Block SANDSTONE = new SandstoneBlock();
	public static final Block SAND = new SandBlock();
	public static final Block GRAVEL = new GravelBlock();
	public static final Block GLASS;
	public static final Block ORE = new OreBlock();
	public static final Block BOOKSHELF;
	public static final Block CRAFTING = new CraftingTableBlock();
	public static final Block CHEST = new ChestBlock();
	public static final Block TORCH = new TorchBlock();
	public static final FurnaceBlock FURNACE = new FurnaceBlock();
	public static final SablingBlock SABLING = new SablingBlock();
	public static final Block LOG = new LogBlock();
	public static final Block LEAVES = new LeavesBlock();
	public static final DoorBlock DOOR = new DoorBlock();
	public static final Block METAL = new MetalBlock();
	public static final Block FLOWER = new FlowerBlock();
	public static final Block BRICK;
	public static final Block CLAY = new ClayBlock();
	public static final Block STONEBRICK;
	public static final Block GLOWSTONE;
	public static final Block BARRIER;
	public static final Block FARMLAND = new FarmlandBlock();
	public static final Block CROP = new CropBlock();
	public static final Block SLAB = new SlabBlock();
	public static final Block STAIR = new StairBlock();
	public static final Block SNOW_LAYER = new SnowLayerBlock();
	public static final Block ICE;
	public static final Block LADDER = new LadderBlock();
	public static final Block TALLGRASS = new TallGrassBlock();
	public static final Block CACTUS = new CactusBlock();
	public static final LavaBlock LAVA = new LavaBlock();
	public static final Block OBSIDIAN;
	public static final Block FIRE = new FireBlock();
	public static final Block WOOL = new WoolBlock();
	
	static {
		BlockSetting setting = new BlockSetting();
		setting.setName("Dirt");
		setting.model = new CubeModel(Assets.getBlockReg(2, 0));
		setting.setTexture(Assets.getItemReg(8, 0));
		setting.setBlockType(BlockType.DIRT);
		setting.hardness = 0.5f;
		DIRT = new Block(setting);
		
		setting.reset();
		setting.setName("Cobblestone");
		setting.model = new CubeModel(Assets.getBlockReg(0, 1));
		setting.setTexture(Assets.getItemReg(0, 0));
		setting.setMining(ToolType.PICKAXE, 1, 2);
		COBBLESTONE = new Block(setting);
		
		setting.reset();
		setting.setName("Plank");
		setting.model = new CubeModel(Assets.getBlockReg(4, 0));
		setting.setTexture(Assets.getItemReg(5, 0));
		setting.setMining(0, 2f);
		setting.setBlockType(BlockType.WOOD);
		PLANK = new Block(setting);
		
		setting.reset();
		setting.setName("Bedrock");
		setting.model = new CubeModel(Assets.getBlockReg(1, 1));
		setting.miningLevel = -1;
		BEDROCK = new Block(setting);
		
		setting.reset();
		setting.setName("Glass");
		setting.model = new CubeModel(Assets.getBlockReg(1, 3));
		setting.material = Material.GLASS;
		setting.setTexture(Assets.getItemReg(2, 3));
		setting.setBlockType(BlockType.GLASS);;
		setting.hardness = 0.3f;
		GLASS = new Block(setting);
		
		setting.reset();
		setting.setName("Bookshelf");
		setting.model = new CubeModel(new CubeTex(getBlockReg(4, 0), getBlockReg(3, 2)));
		setting.setTexture(Assets.getItemReg(8, 4));
		setting.setBlockType(BlockType.WOOD);
		setting.hardness = 1.5f;
		BOOKSHELF = new Block(setting);
		
		setting.reset();
		setting.setName("Brick Block");
		setting.model = new CubeModel(Assets.getBlockReg(7, 0));
		setting.setTexture(Assets.getItemReg(6, 0));
		setting.setMining(ToolType.PICKAXE, 1, 2f);
		BRICK = new Block(setting);
		
		setting.reset();
		setting.setName("Stone Brick");
		setting.model = new CubeModel(Assets.getBlockReg(6, 3));
		setting.setTexture(Assets.getItemReg(1, 0));
		setting.setMining(ToolType.PICKAXE, 1, 1.5f);
		STONEBRICK = new Block(setting);
		
		setting.reset();
		setting.setName("Glowstone");
		setting.model = new CubeModel(Assets.getBlockReg(9, 6));
		setting.lightLevel = 15;
		setting.setTexture(Assets.getItemReg(3, 3));
		setting.setBlockType(BlockType.GLASS);
		setting.hardness = 0.3f;
		GLOWSTONE = new Block(setting);
		
		setting.reset();
		setting.setName("Barrier Block");
		setting.material = Material.BARRIER;
		setting.model = new BarrierModel();
		setting.miningLevel = -1;
		BARRIER = new Block(setting);
		
		setting.reset();
		setting.setName("Ice Block");
		setting.setBlockType(BlockType.ICE);
		setting.model = new CubeModel(Assets.getBlockReg(3, 4));
		setting.hardness = 0.5f;
		ICE = new Block(setting);
		
		setting.reset();
		setting.setName("Obsidian");
		setting.model = new CubeModel(Assets.getBlockReg(5, 2));
		setting.setTexture(Assets.getItemReg(0, 3));
		setting.setMining(ToolType.PICKAXE, 4, 20f);
		OBSIDIAN = new Block(setting);
	}
	
	public static final int SIZE = ID;
	
	static void addBlock(Block block) {
		blocks[block.id] = block;
	}
	
	public static Block get(int data) {
		return blocks[BlockUtils.toBlockID(data)];
	}
}
