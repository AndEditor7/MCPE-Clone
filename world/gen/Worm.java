package com.andedit.arcubit.world.gen;

import static com.andedit.arcubit.world.World.world;

import java.util.Random;

import com.andedit.arcubit.blocks.Block;
import com.andedit.arcubit.blocks.Blocks;
import com.andedit.arcubit.utils.maths.FastNoiseOctaves;
import com.andedit.arcubit.world.World;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

class Worm {
	private static final Quaternion QUAT = new Quaternion();
	
	private final FastNoiseOctaves noisePitch, noiseYaw, rough;
	private final Vector3 velocity = new Vector3();
	public final Vector3 posision;
	
	private final int size;
	private final float length, steps, offset;
	private float move;
	
	public Worm(Random random, float x, float y, float z) {
		this.noisePitch = new FastNoiseOctaves(3, random).setGain(0.7f);
		this.noiseYaw = new FastNoiseOctaves(3, random).setGain(0.7f);
		this.rough = new FastNoiseOctaves(2);
		this.posision = new Vector3(x, y, z);
		
		this.steps = 2f;
		this.length = 100.0f;
		this.size = 5;
		this.offset = (random.nextFloat() * (length * 0.5f)) - length;
		
		float theta = MathUtils.PI2 * random.nextFloat();
		float phi = MathUtils.acos(2f * random.nextFloat() - 1f);
		velocity.setFromSpherical(theta, phi);
	}
	
	public boolean update() {
		final float scl = 80f;
		QUAT.setEulerAngles(
		noiseYaw.getPerlin((move/scl) + offset, 0) * 35f, 
		noisePitch.getPerlin((move/scl) + offset, 0) * 35f, 0f);
		
		QUAT.transform(velocity);
		posision.add(velocity.x * steps, velocity.y * steps * 0.5f, velocity.z * steps);
		
		final int x, y, z;
		x = MathUtils.floor(posision.x);
		y = MathUtils.floor(posision.y);
		z = MathUtils.floor(posision.z);
		
		final int haft = (size / 2) + 2;
		final float haftf = size / 2.0f;
		for (int xx = -haft; xx < haft+1; xx++)
		for (int yy = -haft; yy < haft+1; yy++)
		for (int zz = -haft; zz < haft+1; zz++) {
			final double xd, yd, zd;
			xd = xx * xx;
			yd = (yy * yy) * 1.2;
			zd = zz * zz;
			if ((float)Math.sqrt(xd + yd + zd) < haftf+rough.getPerlin((xx+x)/8f, (y+yy)/10f, (zz+z)/8f)*1.1f) {
				Block block = world.getBlock(xx+x, yy+y, zz+z);
				if (block != Blocks.WATER && block != Blocks.BEDROCK) {
					if (yy+y < World.SEA_LEVEL) {
						if (block == Blocks.SAND || block == Blocks.SANDSTONE || block == Blocks.CLAY) {
							continue;
						}
					}
					
					world.setBlock(xx+x, yy+y, zz+z, Blocks.AIR);
				}
				
			}
		}
		
		return (move += steps) < length;
	}
	
	public static void updateAll(Array<Worm> worms) {
		for (int i = 0, s = worms.size; i < s; i++) {
			while (worms.get(i).update());
		}
	}
}
