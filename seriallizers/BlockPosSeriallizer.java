package com.andedit.arcubit.seriallizers;

import com.andedit.arcubit.utils.BlockPos;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class BlockPosSeriallizer extends Serializer<BlockPos> {

	@Override
	public void write(Kryo kryo, Output output, BlockPos object) {
		output.writeShort(object.x);
		output.writeShort(object.y);
		output.writeShort(object.z);
	}

	@Override
	public BlockPos read(Kryo kryo, Input in, Class<? extends BlockPos> type) {
		return new BlockPos(in.readShort(), in.readShort(), in.readShort());
	}
}
