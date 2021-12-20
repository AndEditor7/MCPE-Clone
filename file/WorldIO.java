package com.andedit.arcubit.file;

import static com.andedit.arcubit.world.World.world;
import static com.andedit.arcubit.file.Registries.MAP;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.andedit.arcubit.TheGame;
import com.andedit.arcubit.utils.StringMap;
import com.andedit.arcubit.utils.Util;
import com.andedit.arcubit.world.World;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.StreamUtils;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.unsafe.UnsafeInput;
import com.esotericsoftware.kryo.unsafe.UnsafeOutput;

public final class WorldIO {
	private static final Kryo KRYO = new Kryo(new NewClassResolver(), null);
	
	static final int SIZE = World.SIZE * World.HEIGHT * World.SIZE * Integer.BYTES;
	static final int[] OFFSET = {24, 16, 8, 0};
	static final int[] AND = {~(255<<24), ~(255<<16), ~(255<<8), ~255};
	
	public static final String WORLD_DAT = "world.dat";
	public static final String PROPS_BIN = "props.bin";
	
	static final int BUFFER_SIZE = 1<<13;
	private static final byte[] STREAM_BUFFER = new byte[BUFFER_SIZE];
	static final byte[] BUFFER = new byte[BUFFER_SIZE];
	
	static {
		Registries.register(KRYO);
	}
	
	public static void save(TheGame game) throws Exception {
		Output output = null;
		OutputStream outputStream = null;
		FileHandle folder = game.folder;
		
		try {
			outputStream = new DeflaterOutputStream(folder.child(WORLD_DAT).write(false));
			StreamUtils.copyStream(new WorldInputStream(), outputStream, STREAM_BUFFER);
			
			output = getOutput(folder.child(PROPS_BIN).write(false));
			Properties props = new Properties();
			game.save(props);
			props.getAllKeys(MAP);
			
			KRYO.writeObject(output, MAP);
			KRYO.writeObject(output, props);
		} finally {
			StreamUtils.closeQuietly(outputStream);
			StreamUtils.closeQuietly(output);
			MAP.clear();
		}
	}
	
	public static void load(TheGame game) throws Exception {
		Input input = null;
		InputStream inputStream = null;
		FileHandle folder = game.folder;
		
		try {
			input = getInput(folder.child(PROPS_BIN).read());
			KRYO.readObject(input, StringMap.class);
			game.load(KRYO.readObject(input, Properties.class));
			
			inputStream = new InflaterInputStream(folder.child(WORLD_DAT).read());
			copyStream(inputStream, new WorldOutputStream());
			world.buildShadowMap();
		} finally {
			StreamUtils.closeQuietly(input);
			StreamUtils.closeQuietly(inputStream);
			MAP.clear();
		}
	}
	
	private static Input getInput(InputStream stream) {
		return Util.hasUnsafe() ? new UnsafeInput(stream) : new Input(stream);
	}
	
	private static Output getOutput(OutputStream stream) {
		return Util.hasUnsafe() ? new UnsafeOutput(stream) : new Output(stream);
	}
	
	private static void copyStream(InputStream input, OutputStream output) throws IOException {
		int bytesRead, size = 0;
		while ((bytesRead = input.read(STREAM_BUFFER, size, BUFFER_SIZE - size)) != -1) {
			size += bytesRead;
			if (size == BUFFER_SIZE) {
				output.write(STREAM_BUFFER, 0, size);
				size = 0;
			}
		}
	}
}
