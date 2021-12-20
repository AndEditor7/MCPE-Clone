package com.andedit.arcubit.world.loader;

import com.andedit.arcubit.TheGame;
import com.andedit.arcubit.file.WorldIO;
import com.badlogic.gdx.Gdx;

public class WorldLoader extends WorldHandleBase {
	
	public volatile TheGame game;

	public WorldLoader() {
		super(HandleType.LOADER);
	}
	
	@Override
	public void run() {
		try {
			WorldIO.load(game);
		} catch (Exception e) {
			Gdx.app.error("WorldIO", "Failed to load the world.");
			e.printStackTrace();
		}
	}

	@Override
	public String getStatus() {
		return "Loading World..";
	}

	@Override
	public TheGame getGame() {
		return game;
	}
}
