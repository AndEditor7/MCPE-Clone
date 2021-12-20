package com.andedit.arcubit.graphics;

import static com.andedit.arcubit.options.Options.niceSky;
import static com.andedit.arcubit.world.World.world;
import static com.badlogic.gdx.Gdx.files;
import static com.badlogic.gdx.Gdx.gl;
import static com.badlogic.gdx.graphics.Color.BLACK;
import static com.badlogic.gdx.math.Interpolation.fastSlow;

import com.andedit.arcubit.blocks.Blocks;
import com.andedit.arcubit.utils.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.shapebuilders.SphereShapeBuilder;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Disposable;

public final class SkyBox implements Disposable {
	// 106 180 225
	public static final Color FOG_COLOR = new Color(82/265f, 175/265f, 210/265f, 1.0f).mul(1.1f);
	public static final Color SKY_COLOR = new Color(40/255f, 101/255f, 208/255f, 1.0f);
	public static final Color DAWN_COLOR = new Color(242/255f, 118/255f, 24/255f, 1.0f);

	public static final Color FOG = new Color();
	public static final Color HORIZON = new Color();
	
	private static final Color TEMP = new Color();
	
	private final Mesh mesh;
	private final ShaderProgram shader;
	private final PerspectiveCamera skyCam = new PerspectiveCamera();
	private final Clouds clouds = new Clouds();
	private final SunMoon sunMoon = new SunMoon();
	
	public SkyBox() {
		final MeshBuilder builder = new MeshBuilder();
		builder.begin(Usage.Position, GL20.GL_TRIANGLES);
		SphereShapeBuilder.build(builder, 1, 1, 1, 16, 20);
		mesh = builder.end();
		
		shader = new ShaderProgram(files.internal("shaders/skybox.vert"), files.internal("shaders/skybox.frag"));
		skyCam.near = 0.1f;
		skyCam.far  = 8.0f;
	}
	
	public void renderSky(Camera camera) {
		
		final float sunLight = world.getSunLight();
		FOG.set(BLACK).lerp(FOG_COLOR, sunLight);
		HORIZON.set(FOG);
		
		if (niceSky.value) {
			float LowLevel = Math.min(sunLight*2f, 1f);
			float highLevel = MathUtils.clamp((sunLight-0.5f)*2f, 0f, 1f);
			float angle = MathUtils.cos(world.getCycle()) > 0f ? camera.yaw-90f: camera.yaw-270f;
			angle = MathUtils.clamp(angle/90f, -1f, 1f);
			
			float result = fastSlow.apply(1f-Math.abs(angle));
			TEMP.set(FOG).lerp(DAWN_COLOR, result);
			if (sunLight > 0.5f) {
				HORIZON.set(TEMP).lerp(FOG, highLevel);
			} else {
				HORIZON.set(BLACK).lerp(TEMP, LowLevel);
			}
			FOG.set(HORIZON);
		}
		
		if (world.getBlock(camera.position) == Blocks.WATER) {
			FOG.set(60/255f, 111/255f, 208/255f, 1.0f);
		}
		
		if (!niceSky.value) return;
		intsSkyCam(camera);
		
		shader.bind();
		shader.setUniformf("fogColor", HORIZON);
		shader.setUniformf("skyColor", TEMP.set(Color.BLACK).lerp(SKY_COLOR, sunLight));
		shader.setUniformMatrix("projTrans", skyCam.combined);
		
		gl.glDisable(GL20.GL_CULL_FACE);
		gl.glDepthMask(false);
		mesh.render(shader, GL20.GL_TRIANGLES);
		sunMoon.render(skyCam);
		gl.glDepthMask(true);
		gl.glEnable(GL20.GL_CULL_FACE);
	}
	
	public void renderClouds(PerspectiveCamera camera) {
		gl.glDisable(GL20.GL_CULL_FACE);
		clouds.render(camera);
	}
	
	private void intsSkyCam(PerspectiveCamera camera) {
		skyCam.direction.set(camera.direction);
		skyCam.up.set(camera.up);
		skyCam.fieldOfView = camera.fieldOfView;
		skyCam.viewportWidth = camera.viewportWidth;
		skyCam.viewportHeight = camera.viewportHeight;
		skyCam.update(false);
	}

	@Override
	public void dispose() {
		mesh.dispose();
		shader.dispose();
		clouds.dispose();
		sunMoon.dispose();
	}
}
