package com.andedit.arcubit.seriallizers;

import com.andedit.arcubit.utils.maths.CollisionBox;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class CollisionBoxSeriallizer extends Serializer<CollisionBox> {

	@Override
	public void write(Kryo kryo, Output output, CollisionBox box) {
		output.writeFloat(box.xMin);
		output.writeFloat(box.yMin);
		output.writeFloat(box.zMin);
		output.writeFloat(box.xMax);
		output.writeFloat(box.yMax);
		output.writeFloat(box.zMax);
	}

	@Override
	public CollisionBox read(Kryo kryo, Input input, Class<? extends CollisionBox> type) {
		return new CollisionBox(input.readFloats(6));
	}
}
