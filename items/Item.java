package com.andedit.arcubit.items;

import com.andedit.arcubit.Steve;
import com.andedit.arcubit.file.Properties;
import com.andedit.arcubit.file.Serial;
import com.andedit.arcubit.handles.RayContext;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class Item implements Serial {
	
	public short type;
	public byte size = 1;
	
	public abstract String getName();
	
	public abstract TextureRegion getTexture();
	
	public Item size(int size) {
		this.size = (byte) size;
		return this;
	}
	
	public Item type(int type) {
		this.type = (short) type;
		return this;
	}
	
	/** Get max stack size. (Default: 64) */
	public int getStackSize() {
		return 64;
	}
	
	/** Get hit damage point. (Default: 1) */
	public int getHitPoint() {
		return 1;
	}
	
	public boolean isEmpty() {
		return size <= 0;
	}
	
	public void remove() {
		size = 0;
	}
	
	protected final Item set(Item item) {
		size = item.size;
		type = item.type;
		return this;
	}
	
	public void onUse(Steve steve, RayContext context, boolean onTap) {
		
	}
	
	public String getShareType() {
		return null;
	}
	
	/** Use for crafting system. */
	public final boolean match(Item item) {
		if (equals(item)) return true;
		
		String share = getShareType();
		if (share == null) return false;
		
		if (share.equals(item.getShareType())) {
			return true;
		}
			
		return false;
	}
 	
	@Override
	public abstract Item clone();

	@Override
	public int hashCode() {
		return getClass().hashCode() * ((type+1) * 34684376);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		return getClass() == obj.getClass() && type == ((Item)obj).type;
	}
	
	@Override
	public void save(Properties props) {
		props.put("type", type);
		props.put("size", size);
	}
	
	@Override
	public void load(Properties props) {
		type = props.got("type");
		size = props.got("size");
	}
}
