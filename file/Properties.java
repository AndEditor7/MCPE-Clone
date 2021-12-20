package com.andedit.arcubit.file;

import static com.andedit.arcubit.file.Registries.STRUCTS;
import static com.andedit.arcubit.file.Registries.MAPID;

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Supplier;

import com.andedit.arcubit.blocks.ChestBlock.ChestContent;
import com.andedit.arcubit.entity.Entity;
import com.andedit.arcubit.entity.ItemEnitiy;
import com.andedit.arcubit.utils.BlockPos;
import com.andedit.arcubit.utils.StringMap;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.Null;
import com.badlogic.gdx.utils.OrderedMap;

public class Properties extends ArrayMap<String, Object> {
	private static final int SIZE = 20;
	
	private static final Properties DUMMY = new DummyProps(); 
	
	public final boolean isDummy;
	
	public Properties() {
		super(SIZE);
		isDummy = false;
	}
	
	public Properties(int size) {
		super(size);
		isDummy = false;
	}

	private Properties(boolean bool) {
		super(1);
		isDummy = true;
	}
	
	@SuppressWarnings("unchecked")
	public <V> V got(String key) {
		return (V) get(key);
	}
	
	@SuppressWarnings("unchecked")
	public <V> V got(String key, Class<V> clazz) {
		return (V) get(key);
	}
	
	@SuppressWarnings("unchecked")
	public <V> V got(String key, V defaultValue) {
		return (V) get(key, defaultValue);
	}
	
	@SuppressWarnings("unchecked")
	public <V> V got(String key, Supplier<V> supplier) {
		final Object obj = get(key);
		return obj == null ? supplier.get() : (V)obj;
	}
	
	@Override
	public Object get(String key, Object defaultValue) {
		for (int i = size-1; i >= 0; i--) {
			if (key.equals(getKeyAt(i))) return getValueAt(i);
		}
		return defaultValue;
	}
	
	public @Null Object removeKey(String key) {
		for (int i = 0, n = size; i < n; i++) {
			if (key.equals(getKeyAt(i))) {
				Object value = getValueAt(i);
				removeIndex(i);
				return value;
			}
		}
		return null;
	}
	
	public <V> V newObject() {
		return newObject("class");
	}
	
	@SuppressWarnings("unchecked")
	public <V> V newObject(String key) {
		try {
			return (V) STRUCTS.get((short)get(key)).newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void putClass(Object obj) {
		putClass("class", obj.getClass());
	}
	
	public void putClass(String key, Object obj) {
		putClass(key, obj.getClass());
	}
	
	public void putClass(String key, Class<?> clazz) {
		put(key, MAPID.get(clazz));
	}
	
	public int put(Object obj) {
		return super.put(null, obj);
	}

	@Override
	public int put(String key, Object obj) {
		if (key != null && containsKey(key)) {
			throw new IllegalStateException("Duplicated key: " + key);
		}
		return super.put(key, obj);
	}
	
	@Override
	public int indexOfKey(String key) {
		if (key == null) {
			return -1;
		}
		
		for (int i = 0, n = size; i < n; i++) {
			if (key.equals(getKeyAt(i))) return i;
		}
		
		return -1;
	}
	
	public void getAllKeys(StringMap map) {
		for (int i = 0; i < size; i++) {
			map.put(getKeyAt(i));
			if (getValueAt(i) instanceof Properties) {
				((Properties)getValueAt(i)).getAllKeys(map);
			}
		}
	}
	
	public Properties newProps() {
		return newProps(null);
	}
	
	public Properties newProps(int size) {
		return newProps(null, size);
	}

	/** @param key null for array mode. */
	public Properties newProps(String key) {
		return newProps(key, SIZE);
	}
	
	/** @param key null for array mode. */
	public Properties newProps(String key, int size) {
		Properties props = new Properties(size);
		put(key, props);
		return props;
	}
	
	public Properties save(Serial serial) {
		if (serial == null) return this; 
		putClass(serial);
		serial.save(this);
		return this;
	}
	
	public Properties save(Array<? extends Serial> array, boolean saveClass) {
		for (int i = 0; i < array.size; i++) {
			Properties props = newProps();
			Serial serial = array.get(i);
			if (saveClass) props.putClass(serial);
			serial.save(props);
		}
		return this;
	}
	
	public <V> Properties save(OrderedMap<V, ? extends Serial> map) {
		Array<V> array = map.orderedKeys();
		for (int i = 0; i < array.size; i++) {
			map.get(array.get(i)).save(newProps());
		}
		return this;
	}
	
	public <V extends Serial> Properties load(Array<V> array) {
		for (Object obj : array()) {
			if (obj instanceof Properties) {
				Properties props = (Properties)obj;
				V serial = props.newObject();
				serial.load(props);
				array.add(serial);
			}
		}
		return this;
	}
	
	public <V extends Serial> Properties load(Array<V> array, Class<V> clazz) {
		for (Object obj : array()) {
			if (obj instanceof Properties) {
				V serial = Registries.newObject(clazz);
				serial.load((Properties)obj);
				array.add(serial);
			}
		}
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public <K, V extends Serial> Properties load(OrderedMap<K, V> map, Class<V> clazz) {
		for (Object obj : array()) {
			if (obj instanceof Properties) {
				V serial = Registries.newObject(clazz);
				serial.load((Properties)obj);
				map.put((K)serial.getKey(), serial);
			}
		}
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public <K, V extends Serial> Properties load(OrderedMap<K, V> map, Class<V> clazz, Consumer<V> consumer) {
		for (Object obj : array()) {
			if (obj instanceof Properties) {
				V serial = Registries.newObject(clazz);
				serial.load((Properties)obj);
				K key = (K)serial.getKey();
				map.put(key, serial);
				consumer.accept(serial);
			}
		}
		return this;
	}
	
	public Properties getProps(String key) {
		return got(key, DUMMY);
	}
	
	/** Use null string to iterator objects. */
	public Iterable<Object> array() {
		return new NullIterator(this);
	}
	
	private static class NullIterator implements Iterator<Object>, Iterable<Object>  {
		
		private final Properties props;
		private int index;
		
		private NullIterator(Properties props) {
			this.props = props;
		}

		@Override
		public boolean hasNext() {
			for (; index < props.size; index++) {
				if (props.getKeyAt(index) == null) {
					return true;
				}
			}
			return false;
		}

		@Override
		public Object next() {
			return props.getValueAt(index++);
		}

		@Override
		public Iterator<Object> iterator() {
			return this;
		}
	}
	
	private static class DummyProps extends Properties {
		private static final String MESSAGE = "This is a dummy properties.";
		
		private DummyProps() {
			super(true);
		}
		
		@Override
		public int put(Object obj) {
			throw new UnsupportedOperationException(MESSAGE);
		}
		
		@Override
		public int put(String key, Object obj) {
			throw new UnsupportedOperationException(MESSAGE);
		}
		
		@Override
		public Properties newProps() {
			throw new UnsupportedOperationException(MESSAGE);
		}
		
		@Override
		public Properties newProps(String key) {
			throw new UnsupportedOperationException(MESSAGE);
		}
		
		@Override
		public Properties newProps(int size) {
			throw new UnsupportedOperationException(MESSAGE);
		}
		
		@Override
		public Properties newProps(String key, int size) {
			throw new UnsupportedOperationException(MESSAGE);
		}
		
		@Override
		public void putClass(String key, Object obj) {
			throw new UnsupportedOperationException(MESSAGE);
		}
		
		@Override
		public void putClass(String key, Class<?> clazz) {
			throw new UnsupportedOperationException(MESSAGE);
		}
		
		@Override
		public Object get(String key) {
			return null;
		}
		
		@Override
		public Object get(String key, Object defaultValue) {
			return defaultValue;
		}
		
		@Override
		public <V> V got(String key) {
			return null;
		}
		
		@Override
		public <V> V got(String key, V defaultValue) {
			return defaultValue;
		}
		
		@Override
		public Properties getProps(String key) {
			return this;
		}
		
		@Override
		public boolean containsKey(String key) {
			return false;
		}
		
		@Override
		public boolean containsValue(Object obj, boolean identity) {
			return false;
		}
		
		@Override
		public boolean isEmpty() {
			return true;
		}
		
		@Override
		public boolean notEmpty() {
			return false;
		}
	}
}
