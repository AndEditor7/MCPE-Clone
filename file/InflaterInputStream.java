package com.andedit.arcubit.file;

import java.io.InputStream;

class InflaterInputStream extends java.util.zip.InflaterInputStream {

	public InflaterInputStream(InputStream in) {
		super(in);
		buf = WorldIO.BUFFER;
	}
}
