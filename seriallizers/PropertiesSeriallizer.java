package com.andedit.arcubit.seriallizers;

import com.andedit.arcubit.file.Properties;
import com.andedit.arcubit.utils.StringMap;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;


public class PropertiesSeriallizer extends Serializer<Properties> {
	
	private final StringMap map;

	public PropertiesSeriallizer(StringMap map) {
		this.map = map;
	}

	@Override
	public void write (Kryo kryo, Output output, Properties props) {
		final int size = props.size;
		output.writeVarInt(size, true);

		for (int i = 0; i < size; i++) {
			String key = props.getKeyAt(i);
			Object val = props.getValueAt(i);
			
			output.writeVarInt(key == null ? 0 : map.getIndex(key)+1, true);
			kryo.writeClassAndObject(output, val);
		}
	}

	@Override
	public Properties read (Kryo kryo, Input input, Class<? extends Properties> type) {
		final int size = input.readVarInt(true);
		Properties props = new Properties(size);
		
		for (int i = 0; i < size; i++) {
			int index = input.readVarInt(true)-1;
			String key = index == -1 ? null : map.getString(index);
			Object val = kryo.readClassAndObject(input);
			
			props.put(key, val);
		}
		
		return props;
	}
}
