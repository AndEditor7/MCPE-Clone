package com.andedit.arcubit.file;

import static com.andedit.arcubit.world.World.data;

import java.io.InputStream;

import com.andedit.arcubit.world.World;

public class WorldInputStream extends InputStream {

	private short x, y, z;
	private int pos;
	private int idx;

	@Override
	public int read() {
		if (pos >= WorldIO.SIZE) return -1;
		
		final int byteIndex = pos++ & 3;
		final int intIndex  = idx;
		if (byteIndex == 3) next();
		
		return (data.get(intIndex) >>> WorldIO.OFFSET[byteIndex]) & 0xFF;
	}
	
	@Override
	public int read(byte[] b, int off, int len) {
		if (pos >= WorldIO.SIZE) return -1;
		
        int i = 0;
        for (; i < len; i += 4) {
    		int val = data.get(idx);
    		next();
    		
    		b[i]   = (byte) ((val >>> 24) & 0xFF);
    		b[i+1] = (byte) ((val >>> 16) & 0xFF);
    		b[i+2] = (byte) ((val >>> 8) & 0xFF);
    		b[i+3] = (byte) (val & 0xFF);
		}
        pos += i;
        return i;
	}
	
	private void next() {
		if (++x == World.SIZE) {
			x = 0;
			if (++z == World.SIZE) {
				z = 0;
				if(++y == World.HEIGHT) {
					y = 0;
				}
			}
		}
		idx = World.getIndex(x, y, z);
	}
}
