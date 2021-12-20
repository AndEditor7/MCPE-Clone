package com.andedit.arcubit.handles;

import static com.andedit.arcubit.world.World.world;

import java.util.concurrent.atomic.AtomicReferenceArray;

import com.andedit.arcubit.blocks.Block;
import com.andedit.arcubit.blocks.Blocks;
import com.andedit.arcubit.graphics.ChunkMesh;
import com.andedit.arcubit.graphics.quad.QuadBuilder;
import com.andedit.arcubit.utils.AsyncThreaded;
import com.andedit.arcubit.utils.BlockPos;
import com.andedit.arcubit.utils.SimplePool;
import com.andedit.arcubit.utils.Util;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;

public class ChunkHandle extends AsyncThreaded {
	
	private static final Logger LOG = new Logger("ChunkHandle");

	private static final int SIZE = Util.isDesktop() ? 10 : 3;

	private final AtomicReferenceArray<Pointer> array = new AtomicReferenceArray<>(SIZE);
	private final QuadBuilder[] opaqeBuilders = new QuadBuilder[SIZE];
	private final QuadBuilder[] transBuilders = new QuadBuilder[SIZE];
	private boolean isRunning;
	private volatile int index;


	public ChunkHandle() {
		super("Chunk Builder");

		for (int i = 0; i < SIZE; i++) {
			opaqeBuilders[i] = new QuadBuilder();
			transBuilders[i] = new QuadBuilder();
		}
	}

	public void add(int x, int y, int z) {
		array.set(index++, new Pointer(x, y, z));
	}

	@Override
	public void start() {
		if (!isRunning && index != 0) {
			super.start();
			isRunning = true;
		}
	}

	private static final BlockPos pos = new BlockPos();

	@Override
	public void run() {

		synchronized (this) {
			final int size = index;
			for (int i = 0; i < size; i++) {
				final Pointer pointer = array.get(i);
				final QuadBuilder opaqeBuilder = opaqeBuilders[i];
				final QuadBuilder transBuilder = transBuilders[i];
				
				final int xPos, yPos, zPos;
				xPos = pointer.x * 16;
				yPos = pointer.y * 16;
				zPos = pointer.z * 16;

				opaqeBuilder.begin();
				transBuilder.begin();
				for (int x = 0; x < 16; x++)
				for (int y = 0; y < 16; y++)
				for (int z = 0; z < 16; z++) {
					final Block block = Blocks.get(world.getData(pos.set(x+xPos, y+yPos, z+zPos)));
					if (block.isAir()) continue;

					block.getBlockModel().build(block.getMaterial().isTransparent() ? transBuilder : opaqeBuilder, pos);
				}
			}
		}
	}

	public boolean isReady() {
		return index < SIZE && !isRunning;
	}

	@Override
	public void clear() {
		waitThread();
		for (int i = 0; i < SIZE; i++) {
			array.set(i, null);
		}
		isRunning = false;
		index = 0;
	}

	public void build(SimplePool<ChunkMesh> pool, Array<ChunkMesh> meshes) {
		if (isRunning && isDone()) {

			synchronized (this) {
				final int size = index;
				for (int i = 0; i < size; i++) {
					final Pointer pointer = array.get(i);
					final QuadBuilder opaqeBuilder = opaqeBuilders[i];
					final QuadBuilder transBuilder = transBuilders[i];
					array.set(i, null);
					
					ChunkMesh mesh = null;
					for (ChunkMesh m : meshes) {
						if (pointer.match(m)) {
							mesh = m;
							break;
						}
					}
					
					if (opaqeBuilder.isEmpty() && transBuilder.isEmpty()) {
						if (mesh != null) {
							pool.free(mesh);
							meshes.removeValue(mesh, true);
						}
						continue;
					}
					
					if (mesh == null) {
						mesh = pool.obtain();
						mesh.x = pointer.x;
						mesh.y = pointer.y;
						mesh.z = pointer.z;
						meshes.add(mesh);
					}
					
					mesh.setVertices(opaqeBuilder, transBuilder);
				}
				
				index = 0;
			}

			isRunning = false;
			future = null;
		}
		
		if (isRunning && future != null && !future.isDone()) {
			Gdx.app.log("Chunk", "ChunkMesh has'nt completed");
		}
	}
	
	private static class Pointer {
		final byte x, y, z;
		
		Pointer(int x, int y, int z) {
			this.x = (byte)x;
			this.y = (byte)y;
			this.z = (byte)z;
		}

		boolean match(ChunkMesh m) {
			return m.x == x && m.y == y && m.z == z;
		}
	}
}
