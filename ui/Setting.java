package com.andedit.arcubit.ui;

import static com.andedit.arcubit.TheGame.game;
import static com.andedit.arcubit.Main.main;
import static com.andedit.arcubit.options.Options.sens;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.Sounds;
import com.andedit.arcubit.TheGame;
import com.andedit.arcubit.WorldScreen;
import com.andedit.arcubit.ui.actors.Background;
import com.andedit.arcubit.ui.utils.BaseUI;
import com.andedit.arcubit.ui.utils.PosOffset;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Setting extends BaseUI {
	private final Background background;
	
	public Setting(TheGame game) {
		add(background = new Background());
		
		SliderStyle style = new SliderStyle(
				new TextureRegionDrawable(new TextureRegion(Assets.TEXTURE, 248, 46, 200, 20)), 
				new TextureRegionDrawable(new TextureRegion(Assets.TEXTURE, 225, 184, 11, 17)));
		Slider slider = new Slider(0.2f, 1.8f, 0.05f, false, style);
		slider.setUserObject(new PosOffset(0.5f, 0.5f, -75, 20));
		slider.setValue(sens.value);
		slider.setWidth(160);
		add(slider);
		
		final Label text = new Label("Value: " + sens.value, Assets.SKIN);
		text.setAlignment(Align.center);
		text.setUserObject(new PosOffset(0.5f, 0.5f, 0, 30));
		text.setTouchable(Touchable.disabled);
		add(text);
		
		slider.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				sens.set(slider.getValue());
				text.setText("Value: " + sens.value);
			}
		});
		
		Label label = new Label("mouse sensitivity", Assets.SKIN);
		label.setAlignment(Align.center);
		label.setUserObject(new PosOffset(0.5f, 0.5f, 0, 50));
		add(label);
		
		TextButton textButt = new TextButton("Exit and Save",Assets.SKIN);
		textButt.setUserObject(new PosOffset(0.5f, 0.5f, 0, -50));
		textButt.setSize(120, 30);
		textButt.align(Align.center);
		add(textButt);
		textButt.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				Sounds.click();
				main.setScreen(new WorldScreen(game));
			}
		});
		
		textButt = new TextButton("Options",Assets.SKIN);
		textButt.setUserObject(new PosOffset(0.5f, 0.5f, 0, -10));
		textButt.setSize(120, 30);
		textButt.align(Align.center);
		add(textButt);
		textButt.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				Sounds.click();
				game.manager.setUI(OptionUI.class).setMenu(false);
			}
		});
		
		Button button = new Button(Assets.SKIN.get("back", ButtonStyle.class));
		button.setUserObject(new Vector2(1.0f, 1.0f));
		button.setSize(18, 18);
		button.align(Align.topRight);
		add(button);
		button.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				game.manager.setUI(InGame.class);
				game.steve.useMouse(false);
				Sounds.click();
			}
		});
	}
	
	@Override
	protected void show() {
		game.steve.useMouse(true);
	}
	
	@Override
	public void resize(Viewport view) {
		background.setSize(view.getWorldWidth(), view.getWorldHeight());
	}
}
