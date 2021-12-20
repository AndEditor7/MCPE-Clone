package com.andedit.arcubit.graphics.vertex;

import static com.andedit.arcubit.utils.Util.BUFFER;
import static com.andedit.arcubit.utils.Util.intBuf;
import static com.badlogic.gdx.Gdx.gl;
import static com.badlogic.gdx.Gdx.gl30;
import static com.badlogic.gdx.graphics.GL20.GL_ARRAY_BUFFER;

import java.nio.ByteBuffer;

import com.andedit.arcubit.graphics.quad.QuadIndexBuffer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.BufferUtils;


public class Vbo30 implements Vertex {

	public int glDraw;

	private final int handle, vao;
	private boolean isBound;

	public Vbo30(VertContext context, int glDraw, boolean useQuad) {
		this.glDraw = glDraw;

		intBuf.clear();
		gl30.glGenVertexArrays(1, intBuf);
		vao = intBuf.get();

		handle = gl.glGenBuffer();

		gl30.glBindVertexArray(vao);
		gl.glBindBuffer(GL20.GL_ARRAY_BUFFER, handle);

		context.setVertexAttributes();

		if (useQuad) {
			QuadIndexBuffer.bind();
		}

		gl30.glBindVertexArray(0);
	}
	
	@Override
	public ByteBuffer getBuffer() {
		return BUFFER;
	}

	@Override
	public void updateVert() {
		if (!isBound) gl30.glBindVertexArray(vao);
		gl30.glBindBuffer(GL_ARRAY_BUFFER, handle);
		gl30.glBufferData(GL_ARRAY_BUFFER, BUFFER.remaining(), BUFFER, glDraw);
		if (!isBound) gl30.glBindVertexArray(0);
	}

	@Override
	public void setVertices(float[] array, int size, int offset) {
		BufferUtils.copy(array, BUFFER, size, offset);
		updateVert();
	}

	@Override
	public void bind() {
		gl30.glBindVertexArray(vao);
		isBound = true;
	}

	@Override
	public void unbind() {
		gl30.glBindVertexArray(0);
		isBound = false;
	}

	@Override
	public void dispose() {
		gl30.glBindVertexArray(0);

		intBuf.clear();
		intBuf.put(vao);
		intBuf.flip();
		gl30.glDeleteVertexArrays(1, intBuf);
		gl.glDeleteBuffer(handle);
	}
}
