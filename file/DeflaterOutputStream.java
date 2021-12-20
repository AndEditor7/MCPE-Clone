package com.andedit.arcubit.file;

import java.io.IOException;
import java.io.OutputStream;

import com.andedit.arcubit.utils.Util;

class DeflaterOutputStream extends java.util.zip.DeflaterOutputStream {
	
	private final byte[] bufTmp;

	public DeflaterOutputStream(OutputStream out) {
		super(out);
		bufTmp = buf;
		buf = WorldIO.BUFFER;
		def.setLevel(Util.isDesktop()?3:2);
	}
	
	@Override
	public void write(int b) throws IOException {
        bufTmp[0] = (byte)(b & 0xff);
        write(bufTmp, 0, 1);
    }
}
