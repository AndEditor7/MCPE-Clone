package com.andedit.arcubit.ui;

import static com.andedit.arcubit.options.Options.split;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.Sounds;
import com.andedit.arcubit.Steve;
import com.andedit.arcubit.TheGame;
import com.andedit.arcubit.ui.actors.BlobSelect;
import com.andedit.arcubit.ui.actors.Hearts;
import com.andedit.arcubit.ui.actors.MovmentButtion;
import com.andedit.arcubit.ui.utils.BaseUI;
import com.andedit.arcubit.utils.Util;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

public class InGame extends BaseUI {

	public InGame(TheGame game) {
		final Steve steve = game.steve;

		Image image = new Image(Assets.CROSS) {
			public void draw(Batch batch, float parentAlpha) {
				if (Util.isDesktop() || split.value) {
					super.draw(batch, parentAlpha);
				}
			}
		};

		image.setUserObject(new Vector2(0.5f, 0.5f));
		image.setAlign(Align.center);
		image.setColor(1, 1, 1, 0.8f);
		add(image);
		
		if (!Util.isDesktop()) {
			add(new BlobSelect(steve.controllor));
			add(new MovmentButtion(steve.controllor));
		}
		
		Button button = new Button(new TextureRegionDrawable(new TextureRegion(Assets.TEXTURE, 448, 64, 18, 18)));
		button.align(Align.topRight);
		button.setUserObject(new Vector2(1, 1));
		button.setColor(1, 1, 1, 0.5f);
		add(button);
		button.addListener(new ChangeListener() {
			public boolean handle(Event e) {
				if (Util.isCatched()) return false;
				return super.handle(e);
			}
			public void changed(ChangeEvent event, Actor actor) {
				game.manager.setUI(Setting.class);
				Sounds.click();
			}
		});
		
		add(steve.hotbar);
		add(new Hearts(game.player));
	}
}
