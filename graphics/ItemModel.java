package com.andedit.arcubit.graphics;

import com.andedit.arcubit.items.Item;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectMap.Entry;

public final class ItemModel {
	
	private static final ObjectMap<Item, FloatArray> CACHE = new ObjectMap<>();
	private static final ArrayMap<TextureData, Pixmap> PIXMAPS = new ArrayMap<>();
	private static final TextureRegion REGTEMP = new TextureRegion();
	
	private static Pixmap PIXMAP;
	
	public static void draw(Item item, EntityBatch batch) {
		
		FloatArray array = CACHE.get(item);
		if (array != null) {
			batch.add(array);
			return;
		}
		CACHE.put(item, array = new FloatArray(128));
		
		final TextureRegion region = item.getTexture();
		REGTEMP.setRegion(region);
		
		final TextureData data = region.getTexture().getTextureData();
		PIXMAP = PIXMAPS.get(data);
		if (PIXMAP == null) {
			if (!data.isPrepared()) data.prepare();
			PIXMAP = data.consumePixmap();
			PIXMAPS.put(data, PIXMAP);
		}
		
		final int xPos, yPos;
		xPos = region.getRegionX();
		yPos = region.getRegionY();
		
		final float a = 1/16f;

		REGTEMP.flip(false, true);
		array.add(1, 0, a);
		array.add(REGTEMP.getU2(), REGTEMP.getV2());
		array.add(1, 1, a);
		array.add(REGTEMP.getU2(), REGTEMP.getV());
		array.add(0, 1, a);
		array.add(REGTEMP.getU(), REGTEMP.getV());
		array.add(0, 0, a);
		array.add(REGTEMP.getU(), REGTEMP.getV2());

		REGTEMP.flip(true, false);
		array.add(0, 0, 0);
		array.add(REGTEMP.getU2(), REGTEMP.getV2());
		array.add(0, 1, 0);
		array.add(REGTEMP.getU2(), REGTEMP.getV());
		array.add(1, 1, 0);
		array.add(REGTEMP.getU(), REGTEMP.getV());
		array.add(1, 0, 0);
		array.add(REGTEMP.getU(), REGTEMP.getV2());
		
		for (int x = 0; x < region.getRegionWidth();  x++)
		for (int y = 0; y < region.getRegionHeight(); y++) {
			if (isAlpha(x+xPos, y+yPos)) continue;
			
			final float xx = x/16f;
			final float yy = y/16f;
			
			if (y+1 > 15 || isAlpha(x+xPos, y+yPos+1)) {
				REGTEMP.setRegion(x+xPos, y+yPos, 1, 1);
				
				array.add(xx+a, yy+a, a);
				array.add(REGTEMP.getU2(), REGTEMP.getV2());
				
				array.add(xx+a, yy+a, 0);
				array.add(REGTEMP.getU2(), REGTEMP.getV());
				
				array.add(xx, yy+a, 0);
				array.add(REGTEMP.getU(), REGTEMP.getV());
				
				array.add(xx, yy+a, a);
				array.add(REGTEMP.getU(), REGTEMP.getV2());
			}
			
			if (y-1 < 0 || isAlpha(x+xPos, y+yPos-1)) {
				REGTEMP.setRegion(x+xPos, y+yPos, 1, 1);
				
				array.add(xx, yy, a);
				array.add(REGTEMP.getU2(), REGTEMP.getV2());
				
				array.add(xx, yy, 0);
				array.add(REGTEMP.getU2(), REGTEMP.getV());
				
				array.add(xx+a, yy, 0);
				array.add(REGTEMP.getU(), REGTEMP.getV());
				
				array.add(xx+a, yy, a);
				array.add(REGTEMP.getU(), REGTEMP.getV2());
			}
			
			if (x+1 > 15 || isAlpha(x+xPos+1, y+yPos)) {
				REGTEMP.setRegion(x+xPos, y+yPos, 1, 1);
				
				array.add(xx+a, yy, 0);
				array.add(REGTEMP.getU2(), REGTEMP.getV2());
				
				array.add(xx+a, yy+a, 0);
				array.add(REGTEMP.getU2(), REGTEMP.getV());
				
				array.add(xx+a, yy+a, a);
				array.add(REGTEMP.getU(), REGTEMP.getV());
				
				array.add(xx+a, yy, a);
				array.add(REGTEMP.getU(), REGTEMP.getV2());
			}
			
			if (x-1 < 0 || isAlpha(x+xPos-1, y+yPos)) {
				REGTEMP.setRegion(x+xPos, y+yPos, 1, 1);
				
				array.add(xx, yy, a);
				array.add(REGTEMP.getU2(), REGTEMP.getV2());
				
				array.add(xx, yy+a, a);
				array.add(REGTEMP.getU2(), REGTEMP.getV());
				
				array.add(xx, yy+a, 0);
				array.add(REGTEMP.getU(), REGTEMP.getV());
				
				array.add(xx, yy, 0);
				array.add(REGTEMP.getU(), REGTEMP.getV2());
			}
		}
		
		batch.add(array);
		return;
	}
	
	private static boolean isAlpha(int x, int y) {
		if (x < 0 || y < 0 || x >= PIXMAP.getWidth() || y >= PIXMAP.getHeight()) {
			return true;
		}
		
		return (PIXMAP.getPixel(x, y) & 0xFF) != 255;
	}
	
	public static void dispose() {
		for (Entry<TextureData, Pixmap> entry : PIXMAPS) {
			if (entry.key.disposePixmap()) entry.value.dispose();
		}
	}
}
