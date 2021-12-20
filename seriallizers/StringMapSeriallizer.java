package com.andedit.arcubit.seriallizers;

import com.andedit.arcubit.utils.StringMap;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.ImmutableSerializer;

public class StringMapSeriallizer extends ImmutableSerializer<StringMap> {
	
	private final StringMap map;
	
	public StringMapSeriallizer(StringMap map) {
		this.map = map;
	}

	@Override
	public void write(Kryo kryo, Output output, StringMap map) {
		final int size = map.size();
		output.writeVarInt(size, true);
		
		for (int i = 0; i < size; i++) {
			output.writeAscii(map.getString(i));
		}
	}

	@Override
	public StringMap read(Kryo kryo, Input input, Class<? extends StringMap> type) {
		final int size = input.readVarInt(true);
		map.resize(size);
		
		for (int i = 0; i < size; i++) {
			map.put(input.readString());
		}
		
		return map;
	}

}
