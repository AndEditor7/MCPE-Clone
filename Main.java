package com.andedit.arcubit;

import com.andedit.arcubit.graphics.FastBatch;
import com.andedit.arcubit.graphics.ItemModel;
import com.andedit.arcubit.graphics.SimpleBatch;
import com.andedit.arcubit.graphics.quad.QuadIndexBuffer;
import com.andedit.arcubit.graphics.vertex.VoxelTerrain;
import com.andedit.arcubit.handles.Inputs;
import com.andedit.arcubit.handles.Static;
import com.andedit.arcubit.options.Options;
import com.andedit.arcubit.particle.ParticleSystem;
import com.andedit.arcubit.utils.Android;
import com.andedit.arcubit.utils.Util;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public final class Main extends Base {
	public static final Main main = new Main();
	private Main () {}

	public Android android;
	public ScreenViewport view;
	public AssetManager asset;
	public FastBatch batch;
	
	public TheMenu menu;
	
	private SimpleBatch simple;

	@Override
	public void create() {
		QuadIndexBuffer.ints();
		stage = new Stage(view = new ScreenViewport(), batch = new FastBatch());
		Gdx.input.setInputProcessor(new InputMultiplexer(stage, inputs, Inputs.input));
		
		view.setUnitsPerPixel(1/7f);
		Assets.loadAssets(asset = new AssetManager(new InternalFileHandleResolver(), false));
		Sounds.load(asset);
		VoxelTerrain.ints();
		ParticleSystem.ints();
		simple = new SimpleBatch(2);

		Gdx.gl.glCullFace(GL20.GL_BACK);
		Gdx.gl.glLineWidth(3);
		Gdx.gl.glClearColor(0.4f, 0.4f, 0.4f, 1);
	}

	private boolean exit;
	@Override
	public void render() {
		if (exit) {
			super.render();
			return;
		}

		if (asset.update(10)) {
			exit = true;
			simple.dispose();
			simple = null;
			Assets.getAssets(asset);
			Sounds.get(asset);
			Inputs.clear();
			Static.ints();
			Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
			Options.load();
			setScreen(menu = new TheMenu());
			return;
		}
		
		// loading screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		simple.begin(view.getCamera().combined);
		float x = view.getWorldWidth()/2f;
		float y = view.getWorldHeight()/2.5f;
		
		simple.setColor(0.1f, 0.1f, 0.1f);
		simple.draw(x-90f, y-8f, 180, 16);
		
		simple.setColor(0.8f, 0.2f, 0.2f);
		simple.draw(x-88f, y-6f, 176f * asset.getProgress(), 12);
		
		simple.end();
	}

	@Override
	public void pause() {
		if (!Util.isDesktop()) Options.save();
	}

	@Override
	public void dispose() {
		if (Util.isDesktop()) Options.save();
		super.dispose();
		if (screen != menu) menu.dispose();
		if (simple != null) simple.dispose();
		asset.dispose();
		stage.dispose();
		batch.dispose();
		VoxelTerrain.dispose();
		ParticleSystem.dispose();
		QuadIndexBuffer.dispose();
		ItemModel.dispose();
		Static.dispose();
	}
}