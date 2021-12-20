package com.andedit.arcubit.ui.utils;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

public class BaseUI extends InputAdapter implements UI {

	protected final Array<Actor> actors = new Array<>();

	@Override
	public void bind(Stage stage) {
		for (Actor actor : actors)
			stage.addActor(actor);
	}

	@Override
	public void setVisible(boolean visible) {
		for (Actor actor : actors) actor.setVisible(visible);
		if (visible) {
			show();
		} else {
			hide();
		}
	}

	protected final void add(Actor actor) {
		actors.add(actor);
	}
	
	@Override
	public void resize(Viewport view) {

	}
	
	@Override
	public void update() {
		
	}

	protected void show() {

	}

	protected void hide() {

	}
}
