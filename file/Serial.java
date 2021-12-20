package com.andedit.arcubit.file;

public interface Serial {
	void save(Properties props);
	void load(Properties props);
	
	/** To support for hash map. */
	default Object getKey() {
		return null;
	}
}
