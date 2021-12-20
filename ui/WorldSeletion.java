package com.andedit.arcubit.ui;

import java.io.File;
import java.io.FileFilter;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.Sounds;
import com.andedit.arcubit.TheMenu;
import com.andedit.arcubit.file.FileIO;
import com.andedit.arcubit.ui.actors.Background;
import com.andedit.arcubit.ui.actors.WorldTabs;
import com.andedit.arcubit.ui.utils.BaseUI;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;

public class WorldSeletion extends BaseUI {
	
	public final FileHandle saves;
	public FileHandle[] folders;
	
	private final Button dulBut;
	
	private final Table master = new Table();
	private final Background background = new Background();
	private final WorldTabs tabs;
	
	public WorldSeletion(final TheMenu menu) {
		saves = FileIO.external("saves");
		saves.mkdirs();
		
		add(background);
		add(master);
		master.setUserObject(new Vector2(0, 1));
		master.align(Align.topLeft);
		
		TextButton textButt = new TextButton("Back", Assets.SKIN);
		textButt.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				menu.manager.setUI(Menu.class);
				Sounds.click();
			}
		});
		
		Table table = new Table();
		table.setBackground(new NinePatchDrawable(new NinePatch(new TextureRegion(Assets.SPRITES, 42, 1, 16, 16), 3, 3, 2, 3)));
		table.add(new Label("Select world", Assets.SKIN));
		
		master.add(textButt).size(70, 25);
		master.add(table).height(25).growX();
		
		textButt = new TextButton("Create new", Assets.SKIN);
		textButt.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				Sounds.click();
				menu.manager.setUI(WorldOption.class);
			}
		});
		
		Drawable drawable = new TextureRegionDrawable(new TextureRegion(Assets.TEXTURE, 184, 59, 34, 26));
		dulBut = new Button(
		new TextureRegionDrawable(new TextureRegion(Assets.TEXTURE, 150, 59, 34, 26)), drawable, drawable);
		dulBut.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				Sounds.click();
			}
		});
		
		master.add(textButt).size(70, 25).row();
		master.add(tabs = new WorldTabs(dulBut)).grow().size(0).colspan(3).row();
		master.add(dulBut).expandX().padBottom(5).colspan(3);
	}
	
	@Override
	protected void show() {
		folders = saves.list(FILTER);
		tabs.setFolders(folders);
		dulBut.setChecked(false);
	}
	
	@Override
	public void resize(Viewport view) {
		background.setSize(view.getWorldWidth(), view.getWorldHeight());
		master.setSize(view.getWorldWidth(), view.getWorldHeight());
	}
	
	private static final FileFilter FILTER = new FileFilter() {
		public boolean accept(File pathname) {
			return pathname.isDirectory();
		}
	};
	
	void resetIndex() {
		tabs.setIndex(0);
	}
}
