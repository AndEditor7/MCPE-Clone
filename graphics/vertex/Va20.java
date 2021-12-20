package com.andedit.arcubit.graphics.vertex;

import java.nio.ByteBuffer;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.BufferUtils;

public class Va20 implements Vertex {
	final VertContext context;
	final ByteBuffer buffer;
	
	public Va20(VertContext context, ByteBuffer buffer) {
		this.context = context;
		this.buffer = buffer;
	}
	
	@Override
	public void setVertices(float[] array, int size, int offset) {
		BufferUtils.copy(array, buffer, size, offset);
	}
	
	@Override
	public ByteBuffer getBuffer() {
		return buffer;
	}

	@Override
	public void updateVert() {
	}

	@Override
	public void bind() {
		final int pos = buffer.position();
		final int limit = buffer.limit();
		buffer.limit(buffer.capacity());
		
		final ShaderProgram shader = context.getShader();
		final VertexAttributes attributes = context.getAttrs();
		final int numAttributes = attributes.size();
		for (int i = 0; i < numAttributes; i++) {
			final VertexAttribute attribute = attributes.get(i);
			final int location = shader.getAttributeLocation(attribute.alias);
			if (location < 0) continue;
			shader.enableVertexAttribute(location);

			buffer.position(attribute.offset);
			shader.setVertexAttribute(location, attribute.numComponents, attribute.type, attribute.normalized,
				attributes.vertexSize, buffer);
		}
		
		buffer.position(pos);
		buffer.limit(limit);
	}

	@Override
	public void unbind() {
		context.unVertexAttributes();
	}

	@Override
	public void dispose() {
	}
}
