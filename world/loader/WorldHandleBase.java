package com.andedit.arcubit.world.loader;

import com.andedit.arcubit.TheGame;
import com.andedit.arcubit.handles.Static;
import com.andedit.arcubit.utils.AsyncThreaded;

public abstract class WorldHandleBase extends AsyncThreaded {
	
	public final HandleType type;

	public WorldHandleBase(HandleType handle) {
		super(Static.EXECUTOR);
		this.type = handle;
	}
	
	public abstract TheGame getGame();
	
	@Override
	public void start() {
		super.start();
	}
	
	public abstract String getStatus();
	
	public static enum HandleType {
		LOADER, SAVER, GENERATOR
	}
}
