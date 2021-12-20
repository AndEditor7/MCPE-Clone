package com.andedit.arcubit.world.lights;

import static com.andedit.arcubit.world.World.world;

import com.andedit.arcubit.blocks.Blocks;
import com.andedit.arcubit.blocks.utils.BlockUtils;
import com.andedit.arcubit.world.World;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Queue;

class Light {
	private final Handle src = new HandleSrc();
	private final Handle sun = new HandleSun();
	
	private final Pool<LightNewNode> poolNew;
	private final Pool<LightDelNode> poolDel;
	
	Light(Pool<LightNewNode> poolNew, Pool<LightDelNode> poolDel) {
		this.poolNew = poolNew;
		this.poolDel = poolDel;
	}
	
	public void newlightAt(int x, int y, int z, boolean useSrc) {
		use(useSrc).lightNew.addLast(poolNew.obtain().set(x, y, z));
	}
	
	public void dellightAt(int x, int y, int z, boolean useSrc) {
		use(useSrc).lightDel.addLast(poolDel.obtain().set(x, y, z, (byte)use(useSrc).toLight(world.getData(x, y, z))));
	}
	
	Handle use(boolean useSrc) {
		return useSrc ? src : sun;
	}
	
	void fill(boolean useSrc) {
		final Handle handle = use(useSrc);
		final Queue<LightNewNode> lightNew = handle.lightNew;
		
		while(lightNew.notEmpty()) {
			// get the first node from the queue.
			final LightNewNode node = lightNew.removeFirst();
			
			// Cashes position for quick access.
			final int x = node.x;
			final int y = node.y;
			final int z = node.z;
			
			// Set the chunk dirty.
			world.dirty(x, y, z);
			
			// Get the light value from lightMap at current position
			final int lightLevel = handle.toLight(world.getData(x, y, z));
			
			if (y+1 < World.HEIGHT) {
				int data = world.getData(x, y+1, z);
				if (!Blocks.get(data).getMaterial().canBlockLights() && handle.toLight(data)+2 <= lightLevel) {
					handle.setLight(x, y+1, z, lightLevel-1);
					lightNew.addLast(poolNew.obtain().set(x, y+1, z));
				}
			}
				
			if (y-1 >= 0) {
				int data = world.getData(x, y-1, z);
				if (!Blocks.get(data).getMaterial().canBlockLights() && handle.toLight(data)+2 <= lightLevel) {
					handle.setLight(x, y-1, z, lightLevel-1);
					lightNew.addLast(poolNew.obtain().set(x, y-1, z));
				}
			}
			
			if (z-1 >= 0) {
				int data = world.getData(x, y, z-1);
				if (!Blocks.get(data).getMaterial().canBlockLights() && handle.toLight(data)+2 <= lightLevel) {
					handle.setLight(x, y, z-1, lightLevel-1);
					lightNew.addLast(poolNew.obtain().set(x, y, z-1));
				}
			}
			
			if (x-1 >= 0) {
				int data = world.getData(x-1, y, z);
				if (!Blocks.get(data).getMaterial().canBlockLights() && handle.toLight(data)+2 <= lightLevel) {
					handle.setLight(x-1, y, z, lightLevel-1);
					lightNew.addLast(poolNew.obtain().set(x-1, y, z));
				}
			}
			
			if (z+1 < World.SIZE) {
				int data = world.getData(x, y, z+1);
				if (!Blocks.get(data).getMaterial().canBlockLights() && handle.toLight(data)+2 <= lightLevel) {
					handle.setLight(x, y, z+1, lightLevel-1);
					lightNew.addLast(poolNew.obtain().set(x, y, z+1));
				}
			}
			
			if (x+1 < World.SIZE) {
				int data = world.getData(x+1, y, z);
				if (!Blocks.get(data).getMaterial().canBlockLights() && handle.toLight(data)+2 <= lightLevel) {
					handle.setLight(x+1, y, z, lightLevel-1);
					lightNew.addLast(poolNew.obtain().set(x+1, y, z));
				}
			}
			
			poolNew.free(node);
		}
	}
	
	void defill(boolean useSrc) {
		final Handle handle = use(useSrc);
		final Queue<LightNewNode> lightNew = handle.lightNew;
		final Queue<LightDelNode> lightDel = handle.lightDel;
		byte neighborLevel;
		
		while(lightDel.notEmpty()) {
			LightDelNode node = lightDel.removeFirst();
			
			// Cashes position for quick access.
			final int x = node.x;
			final int y = node.y;
			final int z = node.z;
			final byte lightLevel = node.val;
			
			// Set the chunk dirty.
			world.dirty(x, y, z);
			
			if (y+1 < World.HEIGHT) {
				neighborLevel = (byte)handle.toLight(world.getData(x, y+1, z));
				if (neighborLevel != 0 && neighborLevel < lightLevel) {
					handle.setLight(x, y+1, z, 0);
					lightDel.addLast(poolDel.obtain().set(x, y+1, z, neighborLevel));
		        } else if (neighborLevel >= lightLevel) {
		        	lightNew.addLast(poolNew.obtain().set(x, y+1, z));
		        }	
			}
			if (y-1 >= 0) {
				neighborLevel = (byte)handle.toLight(world.getData(x, y-1, z));
				if (neighborLevel != 0 && neighborLevel < lightLevel) {
					handle.setLight(x, y-1, z, 0);
					lightDel.addLast(poolDel.obtain().set(x, y-1, z, neighborLevel));
		        } else if (neighborLevel >= lightLevel) {
		        	lightNew.addLast(poolNew.obtain().set(x, y-1, z));
		        }	
			}
			if (z-1 >= 0) {
				neighborLevel = (byte)handle.toLight(world.getData(x, y, z-1));
				if (neighborLevel != 0 && neighborLevel < lightLevel) {
					handle.setLight(x, y, z-1, 0);
					lightDel.addLast(poolDel.obtain().set(x, y, z-1, neighborLevel));
		        } else if (neighborLevel >= lightLevel) {
		        	lightNew.addLast(poolNew.obtain().set(x, y, z-1));
		        }	
			}
			if (x-1 >= 0) {
				neighborLevel = (byte)handle.toLight(world.getData(x-1, y, z));
				if (neighborLevel != 0 && neighborLevel < lightLevel) {
					handle.setLight(x-1, y, z, 0);
					lightDel.addLast(poolDel.obtain().set(x-1, y, z, neighborLevel));
		        } else if (neighborLevel >= lightLevel) {
		        	lightNew.addLast(poolNew.obtain().set(x-1, y, z));
		        }	
			}
			if (z+1 < World.SIZE) {
				neighborLevel = (byte)handle.toLight(world.getData(x, y, z+1));
				if (neighborLevel != 0 && neighborLevel < lightLevel) {
					handle.setLight(x, y, z+1, 0);
					lightDel.addLast(poolDel.obtain().set(x, y, z+1, neighborLevel));
		        } else if (neighborLevel >= lightLevel) {
		        	lightNew.addLast(poolNew.obtain().set(x, y, z+1));
		        }	
			}
			if (x+1 < World.SIZE) {
				neighborLevel = (byte)handle.toLight(world.getData(x+1, y, z));
				if (neighborLevel != 0 && neighborLevel < lightLevel) {
					handle.setLight(x+1, y, z, 0);
					lightDel.addLast(poolDel.obtain().set(x+1, y, z, neighborLevel));
		        } else if (neighborLevel >= lightLevel) {
		        	lightNew.addLast(poolNew.obtain().set(x+1, y, z));
		        }	
			}
			
			poolDel.free(node);
		}
	}
	
	private static abstract class Handle {
		final Queue<LightNewNode> lightNew = new Queue<>(100);
		final Queue<LightDelNode> lightDel = new Queue<>(100);
		
		abstract int toLight(int data);
		abstract void setLight(int x, int y, int z, int level);
	}
	
	private static class HandleSrc extends Handle {
		int toLight(int light) {
			return BlockUtils.toSrcLight(light);
		}
		void setLight(int x, int y, int z, int level) {
			world.setSrcLight(x, y, z, level);
		}
	}
	
	private static class HandleSun extends Handle {
		int toLight(int light) {
			return BlockUtils.toSunLight(light);
		}
		void setLight(int x, int y, int z, int level) {
			world.setSunLight(x, y, z, level);
		}
	}
}
