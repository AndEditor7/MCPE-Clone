package com.andedit.arcubit.ui.actors;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.entity.Player;
import com.andedit.arcubit.ui.utils.PosOffset;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Hearts extends Actor {
	
	private static final TextureRegion BASE  = new TextureRegion(Assets.ICONS, 16, 0, 9, 9);
	private static final TextureRegion HEART = new TextureRegion(Assets.ICONS, 52, 0, 9, 9);
	private static final TextureRegion HEART_HAFT = new TextureRegion(Assets.ICONS, 61, 0, 9, 9);
	private final Player player;
	
	public Hearts(Player player) {
		this.player = player;
		setSize(90, 19);
		setUserObject(new PosOffset(0.0f, 1.0f, 2, -3));
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.setColor(1, 1, 1, 0.8f);
		final int health = player.health;
		for (int i = 0; i < 10; i++) {
			batch.draw(BASE, getX()+(i*8), getY()-9);
			int len = i * 2;
			if (len < health) {
				batch.draw(len == health-1 && (len & 1) == 0 ? HEART_HAFT : HEART, getX()+(i*8), getY()-9);
			}
		}
		batch.setColor(Color.WHITE);
	}
}
