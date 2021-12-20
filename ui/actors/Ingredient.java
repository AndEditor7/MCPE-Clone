package com.andedit.arcubit.ui.actors;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.items.Item;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.StringBuilder;

public class Ingredient extends Actor {
	
	public Item item;
	public final StringBuilder text = new StringBuilder();
	public boolean isWhite;
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(item.getTexture(), getX(), getY(), 16, 16);
		Assets.FONT.setColor(isWhite ? Color.WHITE : Color.DARK_GRAY);
		Assets.FONT.draw(batch, text, getX()-1, getY());
		Assets.FONT.setColor(Color.WHITE);
	}
}
