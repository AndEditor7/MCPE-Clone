package com.andedit.arcubit.ui;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.Sounds;
import com.andedit.arcubit.TheMenu;
import com.andedit.arcubit.ui.actors.Background;
import com.andedit.arcubit.ui.utils.BaseUI;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Menu extends BaseUI {
	private final TheMenu menu;
	private final Background background;
	
	public Menu(final TheMenu menu) {
		this.menu = menu;
		
		add(background = new Background());
		
		Group group = new Group();
		group.setUserObject(new Vector2(0.5f, 0.5f));
		add(group);
		
		ButtonStyle buttStyle = new ButtonStyle(
			new TextureRegionDrawable(new TextureRegion(Assets.TEXTURE, 0, 160, 75, 75)), 
			new TextureRegionDrawable(new TextureRegion(Assets.TEXTURE, 75, 160, 75, 75)), null);
		
		Button button = new Button(buttStyle);
		button.setPosition(-45, 0, Align.center);
		group.addActor(button);
		button.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				menu.manager.setUI(WorldSeletion.class);
				Sounds.click();
			}
		});
		
		buttStyle = new ButtonStyle(
			new TextureRegionDrawable(new TextureRegion(Assets.TEXTURE, 0, 85, 75, 75)), 
			new TextureRegionDrawable(new TextureRegion(Assets.TEXTURE, 75, 85, 75, 75)), null);
		
		button = new Button(buttStyle);
		button.setPosition(45, 0, Align.center);
		group.addActor(button);
		button.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				menu.manager.setUI(OptionUI.class).setMenu(true);
				Sounds.click();
			}
		});
	}
	
	@Override
	public void resize(Viewport view) {
		background.setSize(view.getWorldWidth(), view.getWorldHeight());
	}
}
