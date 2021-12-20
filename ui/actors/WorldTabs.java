package com.andedit.arcubit.ui.actors;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

public class WorldTabs extends Group {
	
	private final Array<WorldTab> tabs = new Array<>();
	int index;
	
	final Button delButt;
	
	public WorldTabs(Button delButt) {
		this.delButt = delButt;
	}
	
	public void setFolders(FileHandle[] folders) {
		clearChildren();
		tabs.clear();
		
		for (int i = 0; i < folders.length; i++) {
			WorldTab tab = new WorldTab();
			tab.setFolder(this, folders[i], i);
			tabs.add(tab);
			addActor(tab);
		}
		reposition();
	}
	
	private void reposition() {
		for (int i = 0; i < tabs.size; i++) {
			WorldTab tab = tabs.get(i);
			tab.setPosition(((tab.index-index)*125)+getOriginX(), getHeight() - 5f, Align.top);
		}
	}
	
	@Override
	protected void sizeChanged() {
		setOrigin(Align.center);
		reposition();
	}

	public void setIndex(int index) {
		this.index = index;
		reposition();
	}
}
