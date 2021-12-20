package com.andedit.arcubit.graphics;

import com.andedit.arcubit.graphics.quad.QuadIndexBuffer;
import com.andedit.arcubit.graphics.vertex.VertContext;
import com.andedit.arcubit.graphics.vertex.Vertex;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Disposable;

public class SimpleBatch implements Disposable{
	public static final int VERTEX_SIZE = 2 + 1;
	public static final int QUAD_SIZE = 4 * VERTEX_SIZE;
	
	private final Vertex vertex;
	private final ShaderProgram shader;
	private final float[] array;
	private int idx;
	private float color;
	
	public SimpleBatch(int quads) {
		final VertexAttributes attributes = new VertexAttributes(
			new VertexAttribute(Usage.Position, 2, "pos"),
			new VertexAttribute(Usage.ColorPacked, 4, "color")
		);
		
		shader = loadShader();
		vertex = Vertex.newVa(VertContext.newVert(shader, attributes));
		array = new float[quads * QUAD_SIZE];
	}
	
	public void begin(Matrix4 combine) {
		shader.bind();
		shader.setUniformMatrix("projTrans", combine);
		vertex.bind();
		QuadIndexBuffer.preBind();
	}
	
	public void setColor(Color color) {
		this.color = color.toFloatBits();
	}
	
	public void setColor(float r, float g, float b) {
		this.color = Color.toFloatBits(r, g, b, 1);
	}
	
	public void draw(float x, float y, float w, float h) {
		final int i = idx;
		array[i+0]  = x+w;
		array[i+1]  = y;
		array[i+2]  = color;
		
		array[i+3]  = x+w;
		array[i+4]  = y+h;
		array[i+5]  = color;
		
		array[i+6]  = x;
		array[i+7]  = y+h;
		array[i+8]  = color;
		
		array[i+9]  = x;
		array[i+10] = y;
		array[i+11] = color;
		idx += QUAD_SIZE;
	}
	
	public void end() {
		if (idx == 0) return;
		vertex.setVertices(array, idx, 0);
		Gdx.gl.glDrawElements(GL20.GL_TRIANGLES, (idx*4), GL20.GL_UNSIGNED_SHORT, 0);
		
		vertex.unbind();
		Gdx.gl.glUseProgram(0);
		idx = 0;
	}
	
	@Override
	public void dispose() {
		vertex.dispose();
		shader.dispose();
	}
	
	private static ShaderProgram loadShader() {
		String vert = "#version 100\nattribute vec4 pos;attribute vec4 color;varying vec4 colour;uniform mat4 projTrans;void main(){colour = color;colour.a = 1.0;gl_Position = projTrans * pos;}";
		String frag = "#version 100\n#ifdef GL_ES\nprecision mediump float;\n#endif\nvarying vec4 colour;void main() {gl_FragColor = colour;}";
		return new ShaderProgram(vert, frag);
	}
}
