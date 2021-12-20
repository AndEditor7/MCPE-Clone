package com.andedit.arcubit.seriallizers;

import com.badlogic.gdx.math.Vector3;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class Vector3Seriallizer extends Serializer<Vector3> {

	@Override
	public void write(Kryo kryo, Output output, Vector3 vec) {
		output.writeFloat(vec.x);
		output.writeFloat(vec.y);
		output.writeFloat(vec.z);
	}

	@Override
	public Vector3 read(Kryo kryo, Input input, Class<? extends Vector3> type) {
		return new Vector3(input.readFloat(), input.readFloat(), input.readFloat());
	}
}