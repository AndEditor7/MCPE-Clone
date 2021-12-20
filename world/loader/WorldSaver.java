package com.andedit.arcubit.world.loader;

import com.andedit.arcubit.TheGame;
import com.andedit.arcubit.file.WorldIO;
import com.badlogic.gdx.Gdx;

public class WorldSaver extends WorldHandleBase {
	
	private final TheGame game;

	public WorldSaver(TheGame game) {
		super(HandleType.SAVER);
		this.game = game;
	}
	
	@Override
	public void run() {
		try {
			WorldIO.save(game);
		} catch (Exception e) {
			Gdx.app.error("WorldIO", "Failed to save the world.");
			e.printStackTrace();
		}
	}

	@Override
	public String getStatus() {
		return "Saving World..";
	}

	@Override
	public TheGame getGame() {
		return game;
	}
}
