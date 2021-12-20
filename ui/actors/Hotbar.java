package com.andedit.arcubit.ui.actors;

import com.andedit.arcubit.Main;
import com.andedit.arcubit.Assets;
import com.andedit.arcubit.Steve;
import com.andedit.arcubit.file.Properties;
import com.andedit.arcubit.file.Serial;
import com.andedit.arcubit.graphics.Hand;
import com.andedit.arcubit.handles.Inputs;
import com.andedit.arcubit.items.Item;
import com.andedit.arcubit.ui.Inventory;
import com.andedit.arcubit.ui.utils.Alignment;
import com.andedit.arcubit.utils.Util;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

public class Hotbar extends Group implements Alignment, Serial {
	
	public final Steve steve;
	public final Hand hand;
	public Inventory inventory;
	public int slots = Util.isDesktop() ? 8 : 6;
	public final Array<Hot> hots = new Array<>();
	public int index = 2;
	
	private int align = Align.bottom;
	private Hot dots;
	
	private float fade;
	private final BitmapFontCache title = new BitmapFontCache(Assets.FONT);

	public Hotbar(final Steve steve, Hand hand, boolean isLoad) {
		this.steve = steve;
		this.hand = hand;
		setUserObject(new Vector2(0.5f, 0.0f));
		setHeight(22);
		resize(slots);
		hand.hotbar = this;
		setTransform(false);
	}
	
	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
		hots.get(0).item = inventory.slots.get(0).item;
		hots.get(1).item = inventory.slots.get(1).item;
		hots.get(2).item = inventory.slots.get(2).item;
		hots.get(3).item = inventory.slots.get(3).item;
		hots.get(4).item = inventory.slots.get(4).item;
		hots.get(5).item = inventory.slots.get(5).item;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		if (Inputs.scroll != 0) {
			setIndex(Util.mod(index + Inputs.scroll, hots.size));
		}

		reset();
		super.draw(batch, 1);
		
		batch.setColor(1, 1, 1, 0.7f);
		batch.draw(Assets.HOT_SELECT, (getX()-1)+(index*20), getY()-1);
		batch.setColor(Color.WHITE);
		
		fade = Math.max(fade-(Util.delta()*1.5f), 0f) ;
		if (fade != 0f) {
			title.draw(batch, Math.min(fade, 1f));
		}
	}
	
	public void resize(int slots) {
		this.slots = slots;
		hots.clear();
		clearChildren();
		
		int total = 0;
		for (int i = 0; i < slots; i++) {
			int width = i==0 ? 21 : 20;
			
			Hot hot = new Hot(this, i);
			
			hot.setSize(width, 22);
			hot.setX(total);
			hots.add(hot);
			addActor(hot);
			if (i == 0) {
				hot.texture = Assets.HOT_START;
			} else {
				hot.texture = new TextureRegion(Assets.GUI, total+20, 0, 20, 22);
			}
			
			total += width;
		}
		dots = new Hot(this, -1);
		dots.setSize(21, 22);
		dots.setX(total);
		dots.texture = Assets.HOT_END;
		addActor(dots);
		total += 21;
		
		setWidth(total);
		setOrigin(Align.center);
	}
	
	void reset() {
		int total = 0;
		for (int i = 0; i < hots.size; i++) {
			final Hot hot = hots.get(i);
			hot.setX(total);
			addActor(hot);
			total += i==0 ? 21 : 20;
		}
		
		dots.setX(total);
		
		Main.main.resize();
	}
	
	public void setIndex(int index) {
		this.index = index;
		Item item = getItem();
		if (item != null && !item.isEmpty()) {
			GlyphLayout layout = title.setText(item.getName(), 0, 0);
			title.setPosition(getX()+getOriginX()-(layout.width/2f), getY()+35f);
			fade = 3;
		} else if (fade != 0f) {
			fade = 1;
		}
	}
	
	public Item getItem() {
		return hots.get(index).item;
	}
	
	public void setItem(Item item, short idx) {
		hots.get(index).item = item;
		hots.get(index).index = idx;
	}
	
	public void findItem(Item item, short idx) {
		if (item != null) 
		for (int i = 0; i < hots.size; i++) {
			if (hots.get(i).item == item) {
				index = i;
				return;
			}
		}
		setItem(item, idx);
	}
	
	public boolean addItem(Item item, short idx) {
		int index = -1;
		for (int i = 0; i < hots.size; i++) {
			if (index == -1 && hots.get(i).isEmpty()) {
				index = i;
			} else if (hots.get(i).item == item) {
				return false;
			}
		}
		
		if (index != -1) {
			hots.get(index).item = item;
			hots.get(index).index = idx;
			return true;
		}
		
		return false;
	}

	@Override
	public int getAlign() {
		return align;
	}

	@Override
	public void setAlign(int align) {
		this.align = align;
	}

	@Override
	public void save(Properties props) {
		for (int i = 0; i < slots; i++) {
			final Item item = hots.get(i).item;
			if (item == null || item.isEmpty()) continue;
			props.put(new HotIndex(hots.get(i).index, i));
		}
	}

	@Override
	public void load(Properties props) {
		for (Object obj : props.array()) {
			HotIndex hot = (HotIndex)obj;
			short idx = hot.itemIdx;
			if (idx == -1) continue; 
			hots.get(hot.slotIdx).index = idx;
			hots.get(hot.slotIdx).item = inventory.slots.get(idx).item;
		}
	}
	
	public static final class HotIndex {
		public HotIndex() {
		}
		
		public HotIndex(int itemIdx, int slotIdx) {
			this.itemIdx = (short)itemIdx;
			this.slotIdx = (byte)slotIdx;
		}
		
		public short itemIdx;
		public byte slotIdx;
	}
}
