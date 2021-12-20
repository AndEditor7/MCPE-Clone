package com.andedit.arcubit.world.lights;

import com.badlogic.gdx.utils.Pool;

final class LightNewNode {
	static final Pool<LightNewNode> POOL = new Pool<LightNewNode>(64) {
		protected LightNewNode newObject() {
			return new LightNewNode();
		}
	};
	
	short x, y, z;
	
	LightNewNode() {
	}
	
	LightNewNode set(int x, int y, int z) {
		this.x = (short)x;
		this.y = (short)y;
		this.z = (short)z;
		return this;
	}
	
	@Override
	public boolean equals(Object obj) {
		final LightNewNode node = (LightNewNode)obj;
		return node.x == x && node.y == y && node.z == z;
	}
}
