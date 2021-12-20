package com.andedit.arcubit.world.loader;

import com.andedit.arcubit.TheGame;
import com.andedit.arcubit.world.gen.DefaultGen;

public class WorldGenerator extends WorldHandleBase {

	public WorldGenerator() {
		super(HandleType.GENERATOR);
	}
	
	@Override
	public void run() {
		new DefaultGen().generate();
	}

	@Override
	public String getStatus() {
		return "Generating World..";
	}

	@Override
	public TheGame getGame() {
		return null;
	}
}
