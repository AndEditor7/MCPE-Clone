package com.andedit.arcubit.file;

import com.andedit.arcubit.blocks.Block;
import com.esotericsoftware.kryo.Registration;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.util.DefaultClassResolver;

@SuppressWarnings("rawtypes")
public class NewClassResolver extends DefaultClassResolver {
	@Override
	public Registration writeClass(Output output, Class type) {
		if (type != null && Block.class.isAssignableFrom(type)) {
			type = Block.class;
		}
		return super.writeClass(output, type);
	}
}
