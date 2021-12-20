package com.andedit.arcubit.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.utils.Disposable;

public abstract class AsyncThreaded implements Runnable, Disposable {
	
	/** The asynchronous executer. */
	protected final ExecutorService executor;
	
	protected Future<?> future;
	
	public AsyncThreaded(String name) {
		this(Executors.newSingleThreadExecutor(new DaemonThreadFactory(name)));
	}

	public AsyncThreaded(ExecutorService executor) {
		this.executor = executor;
	}
	
	/** start the thread to execute. */
	protected void start() {
		future = executor.submit(this);
	}
	
	/** Wait for the thread to finish executing. */
	protected void waitThread() {
		if (future != null) {
			try {
				future.get();
			} catch (Exception e) {
			}
		}
	}
	
	/** Check the thread is finished executing. */
	public boolean isDone() {
		return future == null ? true : future.isDone();
	}

	@Override
	/** The method where the thread can execute. */
	public void run() {

	}
	
	/** Wait for the thread to finish, and than Clear anything. */
	public void clear() {
	
	}
	
	@Override
	/** Clear anything and than dispose the thread. */
	public void dispose() {
		clear();
		
		executor.shutdown();
		try {
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			throw new RuntimeException("Couldn't shutdown loading thread", e);
		}
	}
	
	public static class DaemonThreadFactory implements ThreadFactory  {
		private final String name;
		
		public DaemonThreadFactory(String name) {
			this.name = name;
		}
		
		@Override
		public Thread newThread(Runnable r) {
			Thread thread = new Thread(r, name);
			thread.setDaemon(true);
			return thread;
		}
	}
}
