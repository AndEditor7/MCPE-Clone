package com.andedit.arcubit;

import static com.andedit.arcubit.Main.main;
import static com.andedit.arcubit.graphics.SkyBox.FOG;
import static com.andedit.arcubit.world.World.world;

import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

import com.andedit.arcubit.blocks.Blocks;
import com.andedit.arcubit.entity.Player;
import com.andedit.arcubit.file.Properties;
import com.andedit.arcubit.file.Serial;
import com.andedit.arcubit.handles.Inputs;
import com.andedit.arcubit.options.Options;
import com.andedit.arcubit.particle.ParticleSystem;
import com.andedit.arcubit.ui.ChestUI;
import com.andedit.arcubit.ui.Crafting;
import com.andedit.arcubit.ui.DeathScreen;
import com.andedit.arcubit.ui.FurnaceUI;
import com.andedit.arcubit.ui.InGame;
import com.andedit.arcubit.ui.Inventory;
import com.andedit.arcubit.ui.OptionUI;
import com.andedit.arcubit.ui.Setting;
import com.andedit.arcubit.ui.utils.UIManager;
import com.andedit.arcubit.utils.Camera;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

public class TheGame extends ScreenAdapter implements Serial {
	public static TheGame game;
	
	private static final ReentrantLock ENTRANT = new ReentrantLock();
	
	public final Steve steve;
	public final Player player;
	
	public final InGame inGame;	
	public final UIManager manager = new UIManager();
	
	private final Stage stage = main.stage;
	private final Vector3 camPos;
	
	public boolean isSurvival = true;
	public final FileHandle folder;
	
	public TheGame(FileHandle folder, boolean isLoad) {
		game = this;
		this.folder = folder;
		steve = new Steve(this, isLoad);
		player = steve.player;
		camPos = getCamera().position;
		
		manager.put(steve.inventory);
		manager.put(inGame = new InGame(this));
		manager.put(Crafting.crafting.setInvstory(steve.inventory));
		manager.put(new DeathScreen(steve));
		manager.put(new ChestUI(steve.inventory));
		manager.put(new FurnaceUI(steve.inventory));
		manager.put(new Setting(this));
		manager.put(new OptionUI(manager));
	}
	
	@Override
	public void show() {
		world.newRender();
		Gdx.input.setCursorCatched(true);
		main.inputs.addProcessor(steve.controllor);
		
		manager.bind(stage);
		manager.setUI(InGame.class);
		
		time = 0;
	}
	
	@Override
	public void hide() {
		game.steve.useMouse(true);
	}
	
	private static final float STEP = 0.017f;
	private float time;

	private void update() {

		boolean isLock = false;
		if (manager.IsUI(Setting.class)) {
			ENTRANT.lock();
			isLock = true;
		}
		
		try {
			steve.update();
			manager.update();
			world.update();
		} finally {
			if (isLock) ENTRANT.unlock();
		}
		
		ParticleSystem.update();
	}
	
	int timer = 50;
	@Override
	public void render(final float delta) {

		//Util.startMillis();
		if (!isPlaying()) {
			steve.controllor.allowPlace();
		}
		
		if (Inputs.isKeyJustPressed(Keys.E) && !player.isDying()) {
			if (isPlaying()) {
				manager.setUI(Inventory.class);
				steve.useMouse(true);
			} else {
				manager.setUI(InGame.class);
				steve.useMouse(false);
			}
		}
		
		if (Inputs.isKeyJustPressed(Keys.ESCAPE) && isPlaying()) {
			manager.setUI(Setting.class);
		}

		steve.tick(delta);
		time += delta;
		do {
			update();
			time -= STEP;
		} while (time >= STEP);
		time %= STEP;

		world.getRender().chunkUpdate(getCamera());
		
		Gdx.gl.glClearColor(FOG.r, FOG.g, FOG.b, 1f);
		Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT | GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glEnable(GL20.GL_CULL_FACE);
		world.render(getCamera());
		Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);
		steve.render(delta);
		Gdx.gl.glDisable(GL20.GL_CULL_FACE);
		
		// Water filter
		if (world.getBlock(camPos) == Blocks.WATER) {
			final Viewport view = stage.getViewport();
			final Batch batch = stage.getBatch();
			final float lvl = 0.8f;
			batch.begin();
			batch.setColor(lvl, lvl, lvl, 0.6f);
			batch.draw(Assets.WATER, 0, 0, view.getWorldWidth(), view.getWorldHeight());
			batch.end();
		}
		
		stage.act(delta);
		stage.draw();
	}
	
	public boolean isPlaying() {
		return manager.IsUI(InGame.class);
	}
	
	public Camera getCamera() {
		return steve.camera;
	}
	
	public float getCamDst(Vector3 pos) {
		return camPos.dst(pos);
	}
	
	@Override
	public void resize(int width, int height) {
		steve.camera.viewportWidth = width;
		steve.camera.viewportHeight = height;
		manager.resize(main.view);
		steve.resize(width, height);
	}
	
	@Override
	public void dispose() {
		ENTRANT.lock();
		try {
			world.dispose();
		} finally {
			ENTRANT.unlock();
		}
		
		Options.clear();
		game = null;
		Gdx.input.setCursorCatched(false);
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		
		MathUtils.random.setSeed(new Random().nextLong());
		System.out.println("Game disposed!");
	}

	@Override
	public void save(Properties props) {
		ENTRANT.lock();
		try {
			steve.save(props.newProps("steve"));
			world.save(props.newProps("world"));
		} finally {
			ENTRANT.unlock();
		}
		
	}

	@Override
	public void load(Properties props) {
		ENTRANT.lock();
		try {
			steve.load(props.getProps("steve"));
			world.load(props.getProps("world"));
		} finally {
			ENTRANT.unlock();
		}
	}
	
	public void ints() {
		ENTRANT.lock();
		try {
			world.ints();
			steve.respawn();
		} finally {
			ENTRANT.unlock();
		}
	}
}
