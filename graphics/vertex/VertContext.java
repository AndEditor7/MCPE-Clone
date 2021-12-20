package com.andedit.arcubit.graphics.vertex;

import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public interface VertContext 
{
	ShaderProgram getShader();
	
	VertexAttributes getAttrs();
	
	default void setVertexAttributes() {
		final VertexAttributes attributes = getAttrs();
		final ShaderProgram shader = getShader();
		for (int i = 0; i < attributes.size(); i++) {
			final VertexAttribute attribute = attributes.get(i);
			final int location = shader.getAttributeLocation(attribute.alias);
			if (location < 0) continue;
			shader.enableVertexAttribute(location);

			shader.setVertexAttribute(location, attribute.numComponents, attribute.type, attribute.normalized,
					attributes.vertexSize, attribute.offset);
		}
	}
	
	default void unVertexAttributes() {
		final VertexAttributes attributes = getAttrs();
		final ShaderProgram shader = getShader();
		for (int i = 0; i < attributes.size(); i++) {
			shader.disableVertexAttribute(attributes.get(i).alias);
		}
	}
	
	static VertContext newVert(ShaderProgram shader, VertexAttribute... attributeArray) {
		return newVert(shader, new VertexAttributes(attributeArray));
	}
	
	static VertContext newVert(ShaderProgram shader, VertexAttributes attributes) {
		return new VertContext() {
			public ShaderProgram getShader() {
				return shader;
			}
			public VertexAttributes getAttrs() {
				return attributes;
			}
		};
	}
}
