package com.andedit.arcubit.ui.utils;

import com.badlogic.gdx.math.Vector2;

/** An PosOffset for StageUtils */
public final class PosOffset {
	public final Vector2 pos, offset;
	
	private PosOffset (PosOffset po) {
		pos = po.pos.cpy();
		offset = po.offset.cpy();
	}
	
	public PosOffset(float xPos, float yPos, float xOffset, float yOffset) {
		pos = new Vector2(xPos, yPos);
		offset = new Vector2(xOffset, yOffset);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + offset.hashCode();
		result = prime * result + pos.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (obj.getClass() == this.getClass()) {
			PosOffset po = (PosOffset)obj;
			return pos.equals(po.pos) && offset.equals(po.offset);
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "pos: " + pos + "\n offset: " + offset;
	}
	
	@Override
	public PosOffset clone() {
		return new PosOffset(this);
	}
}
