package com.andedit.arcubit.ui;

import static com.andedit.arcubit.TheGame.game;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.Steve;
import com.andedit.arcubit.ui.utils.BaseUI;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;

public class DeathScreen extends BaseUI {
	
	private final Image background;
	
	public DeathScreen(final Steve steve) {
		background = new Image(Assets.BLANK);
		background.setUserObject(Vector2.Zero);
		background.setAlign(Align.bottomLeft);
		background.setColor(0.6f, 0.2f, 0.2f, 0.4f);
		add(background);
		
		Label label = new Label("You died!", Assets.SKIN);
		label.setUserObject(new Vector2(0.5f, 0.7f));
		label.setAlignment(Align.center);
		add(label);
		
		TextButton button = new TextButton("Respawn", Assets.SKIN);
		button.setUserObject(new Vector2(0.5f, 0.4f));
		button.setSize(80, 30);
		button.align(Align.center);
		button.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				steve.respawn();
			}
		});
		add(button);
		button.setTransform(false);
	}
	
	@Override
	protected void show() {
		game.steve.useMouse(true);
	}
	
	@Override
	protected void hide() {
		game.steve.useMouse(false);
	}
	
	@Override
	public void resize(Viewport view) {
		background.setSize(view.getWorldWidth(), view.getWorldHeight());
	}
}
