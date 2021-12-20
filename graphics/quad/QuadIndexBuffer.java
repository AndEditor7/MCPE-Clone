package com.andedit.arcubit.graphics.quad;

import static com.badlogic.gdx.Gdx.gl;
import static com.andedit.arcubit.utils.Util.BUFFER;

import java.nio.ShortBuffer;

import com.andedit.arcubit.utils.Util;
import com.badlogic.gdx.graphics.GL20;

/** This QuadIndexBuffer intention to render meshes with "quads" instead of triangles. */
public final class QuadIndexBuffer 
{
	public static final int maxIndex  = 98304;
	public static final int maxVertex = (maxIndex/6)*4;
	
	static int bufferHandle;

	/** Installation of the index buffer and upload it to the GPU. */
	public static void ints() {
		// Temporally Short buffer.
		final ShortBuffer buffer = BUFFER.asShortBuffer();
		
		// Indexing so it can reuse the same vertex to make up a quad.
		for (int i = 0, v = 0; i < maxIndex; i += 6, v += 4) { 
			buffer.put((short)v);
			buffer.put((short)(v+1));
			buffer.put((short)(v+2));
			buffer.put((short)(v+2));
			buffer.put((short)(v+3));
			buffer.put((short)v);
		}
		
		buffer.flip(); // Make it readable for GPU.
		
		// Upload the buffer to GPU.
		bufferHandle = gl.glGenBuffer();
		gl.glBindBuffer(GL20.GL_ELEMENT_ARRAY_BUFFER, bufferHandle);
		gl.glBufferData(GL20.GL_ELEMENT_ARRAY_BUFFER, buffer.remaining() * Short.BYTES, buffer, GL20.GL_STATIC_DRAW);
		gl.glBindBuffer(GL20.GL_ELEMENT_ARRAY_BUFFER, 0);
	}
	
	/** Bind the index. */
	public static void bind() {
		gl.glBindBuffer(GL20.GL_ELEMENT_ARRAY_BUFFER, bufferHandle);
	}
	
	public static void preBind() {
		if (!Util.isGL30()) bind();
	}
	
	/** Delete the index buffer from GPU. */
	public static void dispose() {
		gl.glBindBuffer(GL20.GL_ELEMENT_ARRAY_BUFFER, 0);
		gl.glDeleteBuffer(bufferHandle);
	}
}
