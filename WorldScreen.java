package com.andedit.arcubit;

import static com.andedit.arcubit.Main.main;

import com.andedit.arcubit.ui.actors.Background;
import com.andedit.arcubit.utils.Util;
import com.andedit.arcubit.world.loader.WorldHandleBase;
import com.andedit.arcubit.world.loader.WorldHandleBase.HandleType;
import com.andedit.arcubit.world.loader.WorldLoader;
import com.andedit.arcubit.world.loader.WorldSaver;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;

public class WorldScreen extends ScreenAdapter {
	private final WorldHandleBase handle;
	private final TheGame game;
	private final Stage stage = main.stage;
	
	private final Background background = new Background();
	private final Label label = new Label(null, Assets.SKIN);
	
	// Load and Gen
	public WorldScreen(WorldHandleBase handle, FileHandle folder) {
		this.handle = handle;
		this.game = new TheGame(folder, handle.type == HandleType.LOADER);
		
		if (handle instanceof WorldLoader) {
			((WorldLoader)handle).game = game;
		}

	}
	
	// Save
	public WorldScreen(TheGame game) {
		this.handle = new WorldSaver(game);
		this.game = game;
	}
	
	@Override
	public void render(float delta) {
		
		if (handle.isDone()) {
			switch (handle.type) {
			case GENERATOR:
				game.ints();
			case LOADER:
				main.setScreen(game);	
				break;
			case SAVER:
				main.setScreen(main.menu);
				break;
			}
		}
		
		stage.act(delta);
		
		Util.glClear();
		stage.draw();
	}
	
	public void show() {
		handle.start();
		label.setText(handle.getStatus());
		
		stage.addActor(background);
		stage.addActor(label);
		
		label.setUserObject(new Vector2(0.5f, 0.5f));
		label.setAlignment(Align.center);
		main.resize();
	}
	
	@Override
	public void hide() {
		handle.clear();
		if (handle.type == HandleType.SAVER) {
			game.dispose();
		}
	}
	
	@Override
	public void resize(int width, int height) {
		final Viewport view = stage.getViewport();
		background.setSize(view.getWorldWidth(), view.getWorldHeight());
	}
}
