package com.andedit.arcubit.seriallizers;

import com.andedit.arcubit.blocks.Block;
import com.andedit.arcubit.blocks.Blocks;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.ImmutableSerializer;

public class BlockSeriallizer extends ImmutableSerializer<Block> {

	@Override
	public void write(Kryo kryo, Output output, Block block) {
		output.writeByte(block.id);
	}

	@Override
	public Block read(Kryo kryo, Input input, Class<? extends Block> type) {
		return Blocks.get(input.readByteUnsigned());
	}

}
