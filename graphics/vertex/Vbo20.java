package com.andedit.arcubit.graphics.vertex;

import static com.andedit.arcubit.utils.Util.BUFFER;
import static com.badlogic.gdx.Gdx.gl;

import java.nio.ByteBuffer;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.BufferUtils;

public class Vbo20 implements Vertex {
	
	public int glDraw;

	private int handle;
	private boolean isBound;
	private final VertContext context;

	public Vbo20(VertContext context, int glDraw) {
		this.glDraw = glDraw;
		this.context = context;

		handle = gl.glGenBuffer();
	}
	
	@Override
	public ByteBuffer getBuffer() {
		return BUFFER;
	}

	@Override
	public void updateVert() {
		if (isBound) {
			gl.glBufferData(GL20.GL_ARRAY_BUFFER, BUFFER.remaining(), BUFFER, glDraw);
		} else {
			gl.glBindBuffer(GL20.GL_ARRAY_BUFFER, handle);
			gl.glBufferData(GL20.GL_ARRAY_BUFFER, BUFFER.remaining(), BUFFER, glDraw);
			gl.glBindBuffer(GL20.GL_ARRAY_BUFFER, 0);
		}
	}

	@Override
	public void setVertices(float[] array, int size, int offset) {
		BufferUtils.copy(array, BUFFER, size, offset);
		updateVert();
	}

	@Override
	public void bind() {
		gl.glBindBuffer(GL20.GL_ARRAY_BUFFER, handle);
		context.setVertexAttributes();
		isBound = true;
	}

	@Override
	public void unbind() {
		context.unVertexAttributes();
		gl.glBindBuffer(GL20.GL_ARRAY_BUFFER, 0);
		isBound = false;
	}
	
	@Override
	public void dispose() {
		gl.glBindBuffer(GL20.GL_ARRAY_BUFFER, 0);
		gl.glDeleteBuffer(handle);
	}
}
