package com.andedit.arcubit.ui.utils;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.viewport.Viewport;

public final class UIManager {
	final ObjectMap<Class<? extends UI>, UI> map;
	final Array<UI> list;
	UI currentUI;

	public UIManager() {
		map = new ObjectMap<>();
		list = new Array<>();
	}

	public void put(UI ui) {
		if (ui == null) throw new IllegalArgumentException("UI cannot be null.");
		map.put(ui.getClass(), ui);
		list.add(ui);
		ui.setVisible(false);
	}

	public void bind(Stage stage) {
		for (UI ui : list) ui.bind(stage);
	}

	public void resize(Viewport view) {
		for (UI ui : list) ui.resize(view);
	}

	@SuppressWarnings("unchecked")
	public <T extends UI> T setUI(Class<T> clazz) {
		final UI ui = map.get(clazz);
		if (ui == null) throw new IllegalArgumentException("Invailed class: " + clazz.getSimpleName());

		if (currentUI != null) currentUI.setVisible(false);
		ui.setVisible(true);
		currentUI = ui;
		return (T) ui;
	}

	@SuppressWarnings("unchecked")
	public <T extends UI> T getUI(Class<T> clazz) {
		return (T) map.get(clazz);
	}

	@SuppressWarnings("unchecked")
	public <T extends UI> T getCurrentUI() {
		return (T) currentUI;
	}
	
	public boolean IsUI(Class<? extends UI> clazz) {
		return currentUI != null ? currentUI.getClass() == clazz : false;
	}
	
	public void clear() {
		map.clear();
		list.clear();
		currentUI = null;
	}

	public void update() {
		if (currentUI != null) {
			currentUI.update();
		}
	}
}
