package com.andedit.arcubit.ui.actors;

import static com.andedit.arcubit.Main.main;
import static com.andedit.arcubit.TheGame.game;
import static com.andedit.arcubit.options.Options.split;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.graphics.Breaking;
import com.andedit.arcubit.handles.Controllor;
import com.andedit.arcubit.utils.Util;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class BlobSelect extends Actor {

	private static final TextureRegion
	OUTLINE = new TextureRegion(Assets.TEXTURE, 432, 448, 64, 64),
	CIRCLE  = new TextureRegion(Assets.TEXTURE, 432, 383, 64, 64);

	private final Controllor control;

	public BlobSelect(Controllor control) {
		this.control = control;
		setSize(40, 40);
		setOrigin(Align.center);
		setTouchable(Touchable.disabled);
		setVisible(false);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		if (!control.isPressDown || (!Util.isDesktop() && split.value)) return;

		batch.setColor(Color.WHITE);
		final ScreenViewport view = main.view;
		setPosition(control.lastX * view.getUnitsPerPixel(), view.getWorldHeight() - (control.lastY * view.getUnitsPerPixel()), Align.center);

		batch.draw(OUTLINE, getX(), getY(), getWidth(), getHeight());

		final Breaking breaking = game.steve.breaking;
		final float pro = breaking.process;
		if (pro != 0) {
			final float inv = 1.0f - pro;
			batch.draw(CIRCLE, getX() + (getOriginX() * inv), getY() + (getOriginY() * inv), getWidth() * pro, getHeight() * pro);
		}
	}

	@Override
	public Actor hit(float x, float y, boolean touchable) {
		return null;
	}
}
