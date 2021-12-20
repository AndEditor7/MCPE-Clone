package com.andedit.arcubit;

import static com.andedit.arcubit.Main.main;

import com.andedit.arcubit.options.Options;
import com.andedit.arcubit.ui.DeleteMessage;
import com.andedit.arcubit.ui.Menu;
import com.andedit.arcubit.ui.OptionUI;
import com.andedit.arcubit.ui.WorldOption;
import com.andedit.arcubit.ui.WorldSeletion;
import com.andedit.arcubit.ui.utils.UIManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class TheMenu extends ScreenAdapter {
	
	public final UIManager manager = new UIManager();
	
	private final Stage stage = main.stage;
	private final OptionUI optionUI = new OptionUI(manager);
	
	public TheMenu() {
		manager.put(new Menu(this));
		manager.put(new WorldSeletion(this));
		manager.put(new WorldOption(this));
		manager.put(new DeleteMessage(this));
		manager.put(optionUI);
	}
	
	@Override
	public void show() {
		manager.bind(stage);
		manager.setUI(Menu.class);
	}
	
	@Override
	public void render(float delta) {
		stage.act(delta);
		
		Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT | GL20.GL_COLOR_BUFFER_BIT);
		stage.draw();
	}
	
	@Override
	public void hide() {
		super.hide();
	}
	
	@Override
	public void resize(int width, int height) {
		manager.resize(stage.getViewport());
	}
}
