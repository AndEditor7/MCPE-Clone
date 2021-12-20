package com.andedit.arcubit.world.lights;

import static com.andedit.arcubit.world.World.world;

import com.andedit.arcubit.blocks.Blocks;
import com.andedit.arcubit.world.World;
import com.badlogic.gdx.math.GridPoint3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.ReflectionPool;

public final class LightHandle {
	
	private final Pool<GridPoint3> pool = new Pool<GridPoint3>() {
		protected GridPoint3 newObject() {
			return new GridPoint3();
		}
	};
	
	private final Array<GridPoint3> rayUpdate = new Array<>();
	
	private final Light light;
	
	public LightHandle(boolean useStaticPool) {
		final Pool<LightNewNode> poolNew = useStaticPool ? LightNewNode.POOL : new ReflectionPool<>(LightNewNode.class, 64);
		final Pool<LightDelNode> poolDel = useStaticPool ? LightDelNode.POOL : new ReflectionPool<>(LightDelNode.class, 64);
		light = new Light(poolNew, poolDel);
	}

	public void newSrclightAt(int x, int y, int z, int level) {
		world.setSrcLight(x, y, z, level);
		light.newlightAt(x, y, z, true);
	}
	
	public void newSunlightAt(int x, int y, int z, int level) {
		world.setSunLight(x, y, z, level);
		light.newlightAt(x, y, z, false);
	}
	
	public void delSrclightAt(int x, int y, int z) {
		light.dellightAt(x, y, z, true);
		world.setSrcLight(x, y, z, 0);
	}
	
	public void delSunlightAt(int x, int y, int z) {
		light.dellightAt(x, y, z, false);
		world.setSunLight(x, y, z, 0);
	}
	
	public void newRaySunlightAt(int x, int y, int z) {
		if (world.shadowMap[x][z] > y) return;
		rayUpdate.add(pool.obtain().set(x, y, z));
	}
	
	public void newSrclightShellAt(int x, int y, int z) {
		if (y+1 < World.HEIGHT) {
			light.newlightAt(x, y+1, z, true);
		}
		if (y-1 >= 0) {
			light.newlightAt(x, y-1, z, true);
		}
		if (z-1 >= 0) {
			light.newlightAt(x, y, z-1, true);
		}
		if (x-1 >= 0) {
			light.newlightAt(x-1, y, z, true);
		}
		if (z+1 < World.SIZE) {
			light.newlightAt(x, y, z+1, true);
		}
		if (x+1 < World.SIZE) {
			light.newlightAt(x+1, y, z, true);
		}
	}
	
	public void newSunlightShellAt(int x, int y, int z) {
		light.newlightAt(x, y, z, false);
		if (y+1 < World.HEIGHT) {
			light.newlightAt(x, y+1, z, false);
		}
		if (y-1 >= 0) {
			light.newlightAt(x, y-1, z, false);
		}
		if (z-1 >= 0) {
			light.newlightAt(x, y, z-1, false);
		}
		if (x-1 >= 0) {
			light.newlightAt(x-1, y, z, false);
		}
		if (z+1 < World.SIZE) {
			light.newlightAt(x, y, z+1, false);
		}
		if (x+1 < World.SIZE) {
			light.newlightAt(x+1, y, z, false);
		}
	}
	
	public void skyRay(int x, int z, int height) {
		final int start = world.shadowMap[x][z];
		for (short y = (short)height; y >= 0; y--)
		{
			if (Blocks.get(world.getData(x, y, z)).getMaterial().canBlockSunRay()) {
				world.shadowMap[x][z] = y;
				break;
			}
			world.setSunLight(x, y, z, 15);
		}
		
		final int end = world.shadowMap[x][z];
		if (start == end) return;
		
		if (start < end) {
			for(int i = start; i < end; i++) {
				delSunlightAt(x, i, z);
			}
		} else {
			for(int i = end+1; i < start; i++) {
				newSunlightAt(x, i, z, 15);
			}
		}
	}
	
	public void calculateLights() {
		for (GridPoint3 pos : rayUpdate) {
			skyRay(pos.x, pos.z, pos.y);
			pool.free(pos);
		}
		rayUpdate.size = 0;
		
		light.defill(true);
		light.fill(true);
		light.defill(false);
		light.fill(false);
	}
}
