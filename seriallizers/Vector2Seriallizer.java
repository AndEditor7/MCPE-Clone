package com.andedit.arcubit.seriallizers;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class Vector2Seriallizer extends Serializer<Vector2> {

	@Override
	public void write(Kryo kryo, Output output, Vector2 vec) {
		output.writeFloat(vec.x);
		output.writeFloat(vec.y);
	}

	@Override
	public Vector2 read(Kryo kryo, Input input, Class<? extends Vector2> type) {
		return new Vector2(input.readFloat(), input.readFloat());
	}
}
