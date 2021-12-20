package com.andedit.arcubit.graphics.vertex;

import java.nio.ByteBuffer;

import com.andedit.arcubit.utils.Util;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.Disposable;

public interface Vertex extends Disposable {
	ByteBuffer getBuffer();
	void updateVert();
	void setVertices(float[] array, int size, int offset);
	void bind();
	void unbind();

	static Vertex newVbo(VertContext context, int draw) {
		return Util.isGL30() ? new Vbo30(context, draw, true) : new Vbo20(context, draw);
	}
	
	static Vertex newVa(VertContext context) {
		return newVa(context, Util.BUFFER);
	}
	
	static Vertex newVa(VertContext context, ByteBuffer buffer) {
		return Util.isGL30() ? new Vbo30(context, GL20.GL_DYNAMIC_DRAW, true) : new Va20(context, buffer);
	}
}
