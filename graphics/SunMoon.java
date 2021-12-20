package com.andedit.arcubit.graphics;

import static com.andedit.arcubit.world.World.world;
import static com.badlogic.gdx.Gdx.files;
import static com.badlogic.gdx.Gdx.gl;

import java.util.Random;

import org.objenesis.instantiator.perc.PercInstantiator;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.graphics.quad.QuadIndexBuffer;
import com.andedit.arcubit.graphics.vertex.VertContext;
import com.andedit.arcubit.graphics.vertex.Vertex;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.FloatArray;

class SunMoon implements Disposable {
	public static final int VERTEX_SIZE = 3 + 2;
	public static final int QUAD_SIZE = 4 * VERTEX_SIZE;
	
	private static final VertexAttributes ATTRIBUTES = new VertexAttributes(
		new VertexAttribute(Usage.Position, 3, "position"),
		new VertexAttribute(Usage.TextureCoordinates, 2, "texCoord")
	);
	
	private final ShaderProgram shader;
	private final Vertex sunMoon, stars;
	private final Matrix4 matrix = new Matrix4();
	private final int starSize;
	
	public SunMoon() {
		shader = new ShaderProgram(files.internal("shaders/sunmoon.vert"), files.internal("shaders/sunmoon.frag"));
		final VertContext context = VertContext.newVert(shader, ATTRIBUTES);
		sunMoon = Vertex.newVbo(context, GL20.GL_STATIC_DRAW);
		stars = Vertex.newVbo(context, GL20.GL_STATIC_DRAW);
		
		float size = 1.5f;
		final float dst = 5.0f;
		final FloatArray array = new FloatArray(QUAD_SIZE*2);
		final TextureRegion sun  = new TextureRegion(Assets.TEXTURE, 240, 252, 32, 32);
		final TextureRegion moon = new TextureRegion(Assets.TEXTURE, 240, 284, 32, 32);
		
		array.add(dst, -size, size);
		array.add(sun.getU2(), sun.getV2());
		array.add(dst, size, size);
		array.add(sun.getU2(), sun.getV());
		array.add(dst, size, -size);
		array.add(sun.getU(), sun.getV());
		array.add(dst, -size, -size);
		array.add(sun.getU(), sun.getV2());
		
		array.add(-dst, -size, -size);
		array.add(moon.getU(), moon.getV());
		array.add(-dst, size, -size);
		array.add(moon.getU(), moon.getV2());
		array.add(-dst, size, size);
		array.add(moon.getU2(), moon.getV2());
		array.add(-dst, -size, size);
		array.add(moon.getU2(), moon.getV());
		
		sunMoon.setVertices(array.items, array.size, 0);
		
		size = 0.013f;
		final int amount = 350; // 300
		final TextureRegion star = new TextureRegion(Assets.TEXTURE, 274, 314, 1, 1);
		array.items = new float[QUAD_SIZE * amount];
		Random rand = new Random(247139874109126462L);
		Vector3 vec = new Vector3();
		Vector3 vec2 = new Vector3();
		Quaternion quat = new Quaternion();
		for (int i = 0; i < amount; i++) {
			
			float yaw = MathUtils.PI2 * rand.nextFloat();
			float pitch = MathUtils.acos(2f * rand.nextFloat() - 1f) - MathUtils.HALF_PI;
			
			quat.setEulerAnglesRad(yaw, pitch, rand.nextFloat() * MathUtils.HALF_PI);
			vec2.set(Vector3.Z).mul(quat).scl(5);
			
			vec.set(-size, -size, 0).mul(quat).add(vec2);
			array.add(vec.x, vec.y, vec.z);
			array.add(star.getU2(), star.getV2());
			
			vec.set(-size, size, 0).mul(quat).add(vec2);
			array.add(vec.x, vec.y, vec.z);
			array.add(star.getU2(), star.getV());
			
			vec.set(size, size, 0).mul(quat).add(vec2);
			array.add(vec.x, vec.y, vec.z);
			array.add(star.getU(), star.getV());
			
			vec.set(size, -size, 0).mul(quat).add(vec2);
			array.add(vec.x, vec.y, vec.z);
			array.add(star.getU(), star.getV2());
		}
		
		starSize = ((QUAD_SIZE*amount)/QUAD_SIZE)*6;
		stars.setVertices(array.items, array.size, 0);
	}
	
	public void render(Camera camera) {
		
		matrix.set(camera.combined).rotateRad(Vector3.Z, world.getCycle());
		gl.glEnable(GL20.GL_BLEND);
		gl.glBlendFunc(GL20.GL_ONE, GL20.GL_ONE);
		shader.bind();
		shader.setUniformMatrix("projTrans", matrix);
		shader.setUniformf("alpha", 1);
		QuadIndexBuffer.preBind();
		
		sunMoon.bind();
		gl.glDrawElements(GL20.GL_TRIANGLES, ((QUAD_SIZE*2)/QUAD_SIZE)*6, GL20.GL_UNSIGNED_SHORT, 0);
		sunMoon.unbind();
		
		// Stars
		//gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		
		shader.setUniformf("alpha", 1f - world.getSunLight());
		stars.bind();
		gl.glDrawElements(GL20.GL_TRIANGLES, starSize, GL20.GL_UNSIGNED_SHORT, 0);
		stars.unbind();
		
		gl.glUseProgram(0);
		gl.glDisable(GL20.GL_BLEND);
		gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
	}

	@Override
	public void dispose() {
		sunMoon.dispose();
		stars.dispose();
		shader.dispose();
	}

}
