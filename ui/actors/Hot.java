package com.andedit.arcubit.ui.actors;

import static com.andedit.arcubit.TheGame.game;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.Sounds;
import com.andedit.arcubit.items.Item;
import com.andedit.arcubit.ui.InGame;
import com.andedit.arcubit.ui.Inventory;
import com.andedit.arcubit.ui.utils.UIManager;
import com.andedit.arcubit.utils.Util;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class Hot extends SlotBase {
	
	private static final float ZERO = -0.3f;
	
	public final Hotbar hotbar;
	public final int slot;
	public Item item;
	public short index;
	public TextureRegion texture;
	private boolean isPressed;
	private float value = ZERO;
	
	public Hot(final Hotbar hotbar, int slot) {
		this.slot = slot;
		this.hotbar = hotbar;
		if (slot == -1) {
			final UIManager manager = game.manager;
			addListener(new ClickListener() {
				public boolean handle(Event e) {
					if (Util.isCatched()) return false;
					return super.handle(e);
				}
				public void clicked(InputEvent event, float x, float y) {
					if (manager.getCurrentUI().getClass() == Inventory.class) {
						manager.setUI(InGame.class);
					} else {
						manager.setUI(Inventory.class);
					}
					Sounds.click();
				}
			});
			
		} else {
			addListener(new InputListener() {
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
					if (Util.isCatched()) return false;
					isPressed = true;
					return true;
				}
				@Override
				public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
					isPressed = false;
					value = ZERO;
				}
			});
			
			addListener(new ClickListener() {
				public boolean handle(Event e) {
					if (Util.isCatched()) return false;
					return super.handle(e);
				}
				public void clicked(InputEvent event, float x, float y) {
					hotbar.setIndex(slot);
					//world.getRender().clearMeshes();
				}
			});
		}
		
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.setColor(1, 1, 1, 0.8f);
		batch.draw(texture, getX(), getY());
		batch.setColor(Color.WHITE);
		if (slot == -1) {
			batch.draw(Assets.DOTS, getX()+3.5f, getY()+9, 14, 4);
		} else {
			if (isEmpty()) {
				item = null;
				value = ZERO;
				return;
			}
			
			if (isPressed) {
				value += Util.delta() * 0.7f;
			}
			
			if (value > 1.0f) {
				game.steve.dropItem(item);
			}
			
			final float x = getX()+2f+(slot==0?1f:0f);
			final float y = getY()+3f;
			
			if (value > 0.0f) 
			batch.draw(Assets.GREEN, x, y, 16f, 16f * value);
			
			draw(batch, item, (slot==0?1f:0f), 0);
		}
	}

	public boolean isEmpty() {
		return item == null || item.isEmpty();
	}
}
