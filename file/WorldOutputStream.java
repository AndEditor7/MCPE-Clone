package com.andedit.arcubit.file;

import static com.andedit.arcubit.world.World.data;

import java.io.OutputStream;

import com.andedit.arcubit.world.World;

public class WorldOutputStream extends OutputStream {

	private short x, y, z;
	private int pos, idx;

	@Override
	public void write(final int b) {
		if (pos >= WorldIO.SIZE) return;
		
		final int byteIndex = pos++ & 3;
		final int intIndex  = idx;
		if (byteIndex == 3) next();

		data.set(intIndex, (data.get(intIndex) & WorldIO.AND[byteIndex]) | (b << WorldIO.OFFSET[byteIndex]));
	}
	
	@Override
	public void write(final byte b[], final int off, final int len) {
		if (pos >= WorldIO.SIZE) return;
		
		int i = 0;
        for (;i < len; i += 4) {
    		data.set(idx, ((b[i]&0xFF) << 24) | ((b[i+1]&0xFF) << 16) | ((b[i+2]&0xFF) << 8) | (b[i+3]&0xFF));
    		next();
        }
        pos += i;
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
