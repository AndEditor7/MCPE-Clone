package com.andedit.arcubit.graphics;

import com.andedit.arcubit.blocks.utils.CubeTex;
import com.badlogic.gdx.math.collision.BoundingBox;

public class MeshBox {
	public final BoundingBox box;
	public final CubeTex tex = null;
	
	public MeshBox() {
		box = new BoundingBox();
	}
}
