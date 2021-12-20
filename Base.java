package com.andedit.arcubit;

import com.andedit.arcubit.handles.Inputs;
import com.andedit.arcubit.ui.utils.StageUtils;
import com.andedit.arcubit.utils.BlockPos;
import com.andedit.arcubit.utils.Util;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public abstract class Base extends Game {

	public Stage stage;
	public final InputMultiplexer inputs = new InputMultiplexer();
	protected Screen newScreen;

	@Override
	public void render() {
		nextScreen();
		super.render();
		Gdx.gl.glUseProgram(0);
		
		Inputs.reset();
		BlockPos.reset();
	}

	protected void nextScreen() {
		if (newScreen == null)
			return;

		if (screen != null)
			screen.hide();
		
		screen = newScreen;
		newScreen = null;

		stage.clear(); // Always clear UI when switching screen.
		inputs.clear(); // Always clear the input processors.

		screen.show();
		screen.resize(stage.getViewport().getScreenWidth(), stage.getViewport().getScreenHeight());
		resize();
	}
	
	/** Scale the UI. */
	public void scale(int scale) {
		final ScreenViewport view = (ScreenViewport)stage.getViewport();
		view.setUnitsPerPixel(1 / (float) scale);
		view.update(view.getScreenWidth(), view.getScreenHeight(), true);
		super.resize(view.getScreenWidth(), view.getScreenHeight());
		resize();
	}

	@Override
	public void resize(int width, int height) {
		final ScreenViewport view = (ScreenViewport)stage.getViewport();
		view.setUnitsPerPixel(1 / (float)Math.max(1, MathUtils.round(height*(Util.isDesktop()?0.0045f:0.0063f)))); // 0.0065f
		view.update(width, height, true);
		super.resize(width, height);
		resize();
		Inputs.clear();
	}
	
	/** Reqest for UI re-position. */
	public void resize() {
		StageUtils.resize(stage);
	}

	@Override
	public void setScreen(Screen screen) {
		newScreen = screen;
	}

	@Override
	public void dispose() {
		if (screen != null) {
			screen.dispose();
			screen = null;
		}
			
		if (newScreen != null) {
			newScreen.dispose();
			newScreen = null;
		}
	}
}
