package com.andedit.arcubit.ui.actors;

import static com.andedit.arcubit.Assets.FONT;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.items.Item;
import com.andedit.arcubit.items.tools.Durability;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class SlotBase extends Actor {
	private static final Vector2 TEMP  = new Vector2();
	private static final Vector2 RED   = new Vector2(240/255f, -0.1f);
	private static final Vector2 GREEN = new Vector2(5/255f, 250/255f);

	
	private static final String[] STRINGS = new String[70];
	static {
		for (int i = 0; i < STRINGS.length; i++) {
			STRINGS[i] = Integer.toString(i);
		}
	}
	
	protected void draw(Batch batch, Item item, float xOffset, float yOffset) {
		if (item == null || item.isEmpty()) {
			return;
		}
		
		final float x = getX()+2+xOffset;
		final float y = getY()+3+yOffset;
		
		batch.draw(item.getTexture(), x, y, 16, 16);
		
		if (item instanceof Durability) {
			final Durability tool = (Durability)item;
			if (!tool.isFull()) {
				batch.setColor(40/255f, 40/255f, 40/255f,1);
				batch.draw(Assets.BLANK,  x+1, y+2, 14f, 2);

				TEMP.set(RED).lerp(GREEN, tool.getPercent());
				
				batch.setColor((float)Math.pow(TEMP.x, 1.0/2.2), (float)Math.pow(TEMP.y, 1.0/2.2), 0, 1);
				batch.draw(Assets.BLANK, x+1, y+2, 14f * tool.getPercent(), 2);
				batch.setColor(Color.WHITE);
			}
		}
		
		if (item.size != 1) {
			FONT.draw(batch, STRINGS[item.size], x, y+7);
		}
	}
}
