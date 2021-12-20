package com.andedit.arcubit.blocks;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.blocks.models.CubeModel;
import com.andedit.arcubit.blocks.utils.BlockType;
import com.andedit.arcubit.blocks.utils.CubeTex;
import com.andedit.arcubit.items.tools.ToolType;
import com.andedit.arcubit.utils.ArrayDir;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class WoolBlock extends Block {
	
	private static int a;
	public static final int
	WHITE = a++,
	ORANGE = a++,
	MAGENTA = a++,
	LIGHT_BLUE = a++,
	YELLOW = a++,
	LIME = a++,
	PINK = a++,
	GRAY = a++,
	LIGHT_GRAY = a++,
	CYAN = a++,
	PURPLE = a++,
	BLUE = a++,
	BROWN = a++,
	GREEN = a++,
	RED = a++,
	BLACK = a;
	
	WoolBlock() {
		int a = 0;
		names = new ArrayDir<>(String.class, 16);
		names.put(a++, "Wool");
		names.put(a++, "Orange Wool");
		names.put(a++, "Magenta Wool");
		names.put(a++, "Light blue Wool");
		names.put(a++, "Yellow Wool");
		names.put(a++, "Lime Wool");
		names.put(a++, "Pink Wool");
		names.put(a++, "Gray Wool");
		names.put(a++, "Light gray Wool");
		names.put(a++, "Cyan Wool");
		names.put(a++, "Purple Wool");
		names.put(a++, "Blue Wool");
		names.put(a++, "Brown Wool");
		names.put(a++, "Green Wool");
		names.put(a++, "Red Wool");
		names.put(a, "Black Wool");
		
		a = 0;
		textures = new ArrayDir<>(TextureRegion.class, 16);
		textures.put(a++, Assets.getItemReg(5, 3));
		textures.put(a++, Assets.getItemReg(12, 3));
		textures.put(a++, Assets.getItemReg(11, 3));
		textures.put(a++, Assets.getItemReg(10, 3));
		textures.put(a++, Assets.getItemReg(9, 3));
		textures.put(a++, Assets.getItemReg(8, 3));
		textures.put(a++, Assets.getItemReg(7, 3));
		textures.put(a++, Assets.getItemReg(6, 3));
		textures.put(a++, Assets.getItemReg(4, 4));
		textures.put(a++, Assets.getItemReg(3, 4));
		textures.put(a++, Assets.getItemReg(2, 4));
		textures.put(a++, Assets.getItemReg(1, 4));
		textures.put(a++, Assets.getItemReg(0, 4));
		textures.put(a++, Assets.getItemReg(15, 3));
		textures.put(a++, Assets.getItemReg(14, 3));
		textures.put(a,   Assets.getItemReg(13, 3));
		
		a = 0;
		ArrayDir<CubeTex> cubeTexs = new ArrayDir<>(CubeTex.class, 16);
		cubeTexs.put(a++, new CubeTex(Assets.getBlockReg(0, 4)));
		cubeTexs.put(a++, new CubeTex(Assets.getBlockReg(2, 13)));
		cubeTexs.put(a++, new CubeTex(Assets.getBlockReg(2, 12)));
		cubeTexs.put(a++, new CubeTex(Assets.getBlockReg(2, 11)));
		cubeTexs.put(a++, new CubeTex(Assets.getBlockReg(2, 10)));
		cubeTexs.put(a++, new CubeTex(Assets.getBlockReg(2, 9)));
		cubeTexs.put(a++, new CubeTex(Assets.getBlockReg(2, 8)));
		cubeTexs.put(a++, new CubeTex(Assets.getBlockReg(2, 7)));
		cubeTexs.put(a++, new CubeTex(Assets.getBlockReg(1, 14)));
		cubeTexs.put(a++, new CubeTex(Assets.getBlockReg(1, 13)));
		cubeTexs.put(a++, new CubeTex(Assets.getBlockReg(1, 12)));
		cubeTexs.put(a++, new CubeTex(Assets.getBlockReg(1, 11)));
		cubeTexs.put(a++, new CubeTex(Assets.getBlockReg(1, 10)));
		cubeTexs.put(a++, new CubeTex(Assets.getBlockReg(1, 9)));
		cubeTexs.put(a++, new CubeTex(Assets.getBlockReg(1, 8)));
		cubeTexs.put(a,   new CubeTex(Assets.getBlockReg(1, 7)));
		
		model = new CubeModel(this, cubeTexs);
		
		blockType = BlockType.WOOL;
		setMining(ToolType.SHEARS, 0, 0.8f);
		newTypeComponent(15);
	}
}
