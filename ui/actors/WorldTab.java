package com.andedit.arcubit.ui.actors;

import static com.andedit.arcubit.Main.main;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.Sounds;
import com.andedit.arcubit.WorldScreen;
import com.andedit.arcubit.ui.DeleteMessage;
import com.andedit.arcubit.world.loader.WorldLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

public class WorldTab extends Actor {
	
	private static final TextureRegion IMAGE = new TextureRegion(Assets.TEXTURE, 162, 317, 128, 96);
	
	int index;
	
	private FileHandle folder;
	private WorldTabs tabs;
	private final BitmapFontCache title = new BitmapFontCache(Assets.FONT);
	
	public WorldTab() {
		setSize(120, 100);
		setOrigin(Align.center);
		addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				Sounds.click();
				if (tabs.index == index) {
					if (tabs.delButt.isChecked()) {
						main.menu.manager.setUI(DeleteMessage.class).setPromp(folder.name(), folder);
					} else {
						main.setScreen(new WorldScreen(new WorldLoader(), folder));
					}
				} else {
					tabs.setIndex(index);
				}
			}
		});
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		if (tabs.index == index) {
			Assets.FRAME.draw(batch, getX(), getY(), getWidth(), getHeight());
			title.setColors(Color.WHITE);
		} else {
			batch.setColor(1, 1, 1, 0.5f);
			title.setColors(1, 1, 1, 0.5f);
		}
		
		float w = IMAGE.getRegionWidth()/2;
		float h = IMAGE.getRegionHeight()/2;
		batch.draw(IMAGE, getX()+getOriginX()-(w/2f), getY()+getHeight()-53f, w, h);
		
		title.setPosition(getX()+7f, getY()+40f);
		title.draw(batch);
		
		batch.setColor(Color.WHITE);
	}

	public void setFolder(WorldTabs tabs, FileHandle folder, int i) {
		this.tabs = tabs;
		this.folder = folder;
		title.setText(folder.name(), 0, 0);
		index = i;
	}

}
