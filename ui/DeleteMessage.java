package com.andedit.arcubit.ui;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.Sounds;
import com.andedit.arcubit.TheMenu;
import com.andedit.arcubit.ui.actors.Background;
import com.andedit.arcubit.ui.utils.BaseUI;
import com.andedit.arcubit.ui.utils.UIManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;

public class DeleteMessage extends BaseUI {
	private final Group group;
	
	private final Label label;
	private final Background background = new Background();
	
	private FileHandle folder;
	
	public DeleteMessage(final TheMenu menu) {
		final UIManager manager = menu.manager;
		group = new Group();
		group.setUserObject(new Vector2(0.5f, 0.5f));
		add(background);
		add(group);
		
		label = new Label(null, Assets.SKIN);
		label.setPosition(0, 30, Align.center);
		group.addActor(label);
		
		TextButton butt = new TextButton("Cancel", Assets.SKIN);
		butt.setSize(70, 25);
		butt.setPosition(-50, -25, Align.center);
		butt.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				Sounds.click();
				manager.setUI(WorldSeletion.class);
			}
		});
		group.addActor(butt);
		
		butt = new TextButton("Delete", Assets.SKIN);
		butt.setSize(70, 25);
		butt.setPosition(50, -25, Align.center);
		butt.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				Sounds.click();
				folder.deleteDirectory();
				manager.setUI(WorldSeletion.class).resetIndex();
			}
		});
		group.addActor(butt);
	}
	
	public void setPromp(String name, FileHandle folder) {
		this.folder = folder;
		label.setText("Are you sure you want to delete the " + name +'?');
		label.pack();
		label.setPosition(0, 30, Align.center);
	}
	
	@Override
	public void resize(Viewport view) {
		background.setSize(view.getWorldWidth(), view.getWorldHeight());
	}
}
