package com.andedit.arcubit.handles;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.andedit.arcubit.graphics.EntityBatch;
import com.andedit.arcubit.graphics.SkyBox;
import com.andedit.arcubit.utils.Util;

public final class Static {
	private Static() {}
	
	public static EntityBatch BATCH1, BATCH2;
	public static ChunkHandle HANDLE;
	public static ExecutorService EXECUTOR;
	public static SkyBox SKYBOX;
	
	public static void ints() {
		BATCH1 = new EntityBatch(200);
		BATCH2 = new EntityBatch(1500);
		HANDLE = new ChunkHandle();
		EXECUTOR = Executors.newSingleThreadExecutor();
		SKYBOX = new SkyBox();
	}
	
	public static void dispose() {
		EXECUTOR.shutdown();
		Util.disposes(BATCH1, BATCH2, HANDLE, SKYBOX);
	}
}
