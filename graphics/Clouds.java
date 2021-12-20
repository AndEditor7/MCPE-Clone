package com.andedit.arcubit.graphics;

import static com.andedit.arcubit.world.World.world;
import static com.badlogic.gdx.Gdx.files;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.graphics.quad.QuadIndexBuffer;
import com.andedit.arcubit.graphics.vertex.VertContext;
import com.andedit.arcubit.graphics.vertex.Vertex;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Disposable;

public class Clouds implements Disposable {
	
	private final Vertex vertex;
	private final ShaderProgram shader;
	private final VertexAttribute attribute;
			
	public Clouds() {
		shader = new ShaderProgram(files.internal("shaders/clouds.vert"), files.internal("shaders/clouds.frag"));
		attribute = new VertexAttribute(Usage.Position, 2, ShaderProgram.POSITION_ATTRIBUTE);
		vertex = Vertex.newVa(VertContext.newVert(shader, attribute));
		
		final float[] array = new float[8];
		final float size = 2;
		
		array[0] = -size;
		array[1] = size;
		
		array[2] = -size;
		array[3] = -size;
		
		array[4] = size;
		array[5] = -size;
		
		array[6] = size;
		array[7] = size;
		
		vertex.setVertices(array, array.length, 0);
	}
	
	private float time = -2000f;
	public void render(PerspectiveCamera camera) {
		Assets.CLOUDS.bind();
		
		shader.bind();
		shader.setUniformMatrix("projTrans", camera.combined);
		shader.setUniformf("camPos", camera.position);
		shader.setUniformf("light", MathUtils.lerp(world.getSunLight(), 1, 0.08f));
		
		time += 0.02f;
		if (time > 2000f) {
			time = -2000f;
		}
		shader.setUniformf("offset", time);
		
		
		QuadIndexBuffer.preBind();
		vertex.bind();
		Gdx.gl.glDrawElements(GL20.GL_TRIANGLES, 6, GL20.GL_UNSIGNED_SHORT, 0);
		vertex.unbind();
	}

	@Override
	public void dispose() {
		shader.dispose();
		vertex.dispose();
	}
}
