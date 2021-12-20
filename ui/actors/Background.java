package com.andedit.arcubit.ui.actors;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.ui.utils.Alignment;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;

public class Background extends Actor implements Alignment {
	
	private final TextureRegion region = new TextureRegion(Assets.BACKGROND);
	
	public Background() {
		setUserObject(Vector2.Zero);
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.setColor(Color.DARK_GRAY);
		region.setRegion(0, 0, MathUtils.roundPositive(getWidth())/2, MathUtils.roundPositive(getHeight())/2);
		batch.draw(region, getX(), getY(), getWidth(), getHeight());
		batch.setColor(Color.WHITE);
	}

	@Override
	public int getAlign() {
		return Align.bottomLeft;
	}

	@Override
	public void setAlign(int align) {
	}
}
