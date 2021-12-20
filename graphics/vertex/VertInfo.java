package com.andedit.arcubit.graphics.vertex;

public class VertInfo {
	/** Lighting. Must clamp it to 0 to 1 if necessary. Use the <code>MathUtils.clamp(value, 0f, 1f)</code> */
	public float ambLit = 1f, srcLit, sunLit = 1f;

	public float packData() {
		return Float.intBitsToFloat((((int) (255*sunLit)<<16) | ((int) (255*srcLit)<<8) | ((int) (255*ambLit))));
	}
}
