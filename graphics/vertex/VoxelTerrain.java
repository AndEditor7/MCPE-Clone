package com.andedit.arcubit.graphics.vertex;

import static com.andedit.arcubit.options.Options.dist;
import static com.andedit.arcubit.options.Options.bright;
import static com.andedit.arcubit.world.World.world;
import static com.badlogic.gdx.Gdx.files;

import com.andedit.arcubit.blocks.Blocks;
import com.andedit.arcubit.graphics.SkyBox;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

// Needs update comments after attribute change.
/** The static class contains vertex attributes and shader */
public final class VoxelTerrain {
	// Data[sideLight&Ambiant, source-light, sunlight, unused]
	/** 3 Position, 4 Data (Packed into 1 float) and 2 TextureCoordinates [x,y,z,d,u,v] */
	public static final VertexAttributes attributes = new VertexAttributes(
			 	new VertexAttribute(Usage.Position, 3, "position"),
				new VertexAttribute(Usage.ColorPacked, 4, "data"),
				new VertexAttribute(Usage.TextureCoordinates, 2, "texCoord")
			);
	
	/** 24 bytes in a single vertex with 6 float components. */ 
	public static final int byteSize = attributes.vertexSize;
	
	/** 6 floats in a single vertex. */ 
	public static final int floatSize = byteSize/Float.BYTES;
	
	public static ShaderProgram shader;
	
	public static void ints() {
		shader = new ShaderProgram(files.internal("shaders/default.vert"), files.internal("shaders/default.frag"));
	}
	
	/** Begins the shader. */
	public static void begin(Camera cam) {
		shader.bind();
		shader.setUniformMatrix("projTrans", cam.combined);
		shader.setUniformf("fogColor", SkyBox.FOG);
		shader.setUniformf("sunLightIntensity", Math.max(world.getSunLight(), 0.15f));
		shader.setUniformf("brightness", bright.value); // 0.35f
		
		float dst = (dist.value * 16) + 8;
		shader.setUniformf("start", dst);
		shader.setUniformf("end", dst * 0.7f);
	}
	
	/** End the shader. */
	public static void end() {
		Gdx.gl.glUseProgram(0);
	}
	
	public static void dispose() {
		shader.dispose();
	}	
	
	public final static VertContext context = new VertContext() {
		public VertexAttributes getAttrs() {
			return attributes;
		}
		public ShaderProgram getShader() {
			return shader;
		}
	};
}
