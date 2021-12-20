package com.andedit.arcubit.ui;

import static com.andedit.arcubit.Main.main;
import static com.andedit.arcubit.TheGame.game;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.Sounds;
import com.andedit.arcubit.blocks.Blocks;
import com.andedit.arcubit.file.Properties;
import com.andedit.arcubit.file.Serial;
import com.andedit.arcubit.items.BlockItem;
import com.andedit.arcubit.items.BusketItem;
import com.andedit.arcubit.items.Item;
import com.andedit.arcubit.items.tools.FlintSteel;
import com.andedit.arcubit.items.tools.ShearsItem;
import com.andedit.arcubit.ui.actors.Hotbar;
import com.andedit.arcubit.ui.actors.Slot;
import com.andedit.arcubit.ui.utils.BaseUI;
import com.andedit.arcubit.ui.utils.PosOffset;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Inventory extends BaseUI implements Serial {
	public static final int SIZE = 36;
	
	private final Hotbar hotbar;
	private final Table contain = new Table();
	private final Table table = new Table();
	private final ScrollPane scroll = new ScrollPane(table);
	private final Image background;
	
	public final Array<Slot> slots = new Array<>(SIZE);
	
	public Inventory(final Hotbar hotbar, boolean isLoad) {
		add(this.hotbar = hotbar);
		
		contain.setTransform(false);
		table.setTransform(false);
		
		contain.setUserObject(new PosOffset(0.5f, 0.0f, 0, 24));
		contain.align(Align.top);
		add(contain);
		contain.setBackground(new NinePatchDrawable(Assets.ITEM_FRAME));
		contain.align(Align.bottom);
		
		contain.add(scroll).grow();
		scroll.setScrollingDisabled(true, false);
		scroll.setFlickScrollTapSquareSize(5);
		scroll.setSmoothScrolling(false);
		table.center();
		
		if (game.isSurvival) {
			for (int i = 0; i < SIZE; i++) {
				slots.add(new Slot(hotbar, i));
			}
		}
		
		if (!isLoad) {
			addItem(new ShearsItem());
			addItem(new ShearsItem());
		}
		
		
		background = new Image(new NinePatch(new TextureRegion(Assets.SPRITES, 42, 1, 16, 16), 3, 3, 2, 3));
		background.setUserObject(new Vector2(0.0f, 1.0f));
		background.setAlign(Align.topLeft);
		background.setHeight(22);
		add(background);
		
		TextButton textButton;
		Button button;
		if (game.isSurvival) {
			textButton = new TextButton("Craft", Assets.SKIN);
			textButton.setUserObject(new Vector2(0.0f, 1.0f));
			textButton.setSize(60, 22);
			textButton.align(Align.topLeft);
			add(textButton);
			textButton.addListener(new ClickListener() {
				public void clicked(InputEvent event, float x, float y) {
					Crafting.crafting.is3x3Grid(false);
					game.manager.setUI(Crafting.class);
					Sounds.click();
				}
			});
			
			button = new Button(Assets.SKIN.get("back", ButtonStyle.class));
			button.setUserObject(new Vector2(1.0f, 1.0f));
			button.setSize(18, 18);
			button.align(Align.topRight);
			add(button);
			button.addListener(new ClickListener() {
				public void clicked(InputEvent event, float x, float y) {
					game.manager.setUI(InGame.class);
					game.steve.useMouse(false);
					Sounds.click();
				}
			});
		}
		
		hotbar.setInventory(this);
	}
	
	public boolean addItem(Item item) {
		if (item == null || item.isEmpty()) return false;
		
		final int maxSize = item.getStackSize();
		int failNum = 0;
		for (Slot slot : slots) {
			Item slotItem =  slot.item;
			if (item.equals(slotItem) && !slotItem.isEmpty() && slotItem.size < maxSize) {
				if (slotItem.size + item.size > maxSize) {
					int num = maxSize - slotItem.size;
					slotItem.size += num;
					item.size -= num;
					if (item.size <= 0) {
						return true;
					}
				} else {
					slotItem.size += item.size;
					hotbar.addItem(slotItem, slot.index);
					return true;
				}
			} else {
				if (!slot.isEmpty()) {
					failNum++;
				}
			}
		}
		
		if (failNum == SIZE) {
			return false; // Inventory is full;
		}
		
		for (Slot slot : slots) {
			if (slot.isEmpty()) {
				slot.item = item.clone();
				hotbar.addItem(slot.item, slot.index);
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	protected void show() {
		for (Slot slot : slots) {
			slot.update();
		}
		reorder(); // Fix weird slot bug on android.
	}
	
	@Override
	public void resize(Viewport view) {
		background.setWidth(view.getWorldWidth());
		contain.setSize(view.getWorldWidth(), view.getWorldHeight()-45f); //  -40f
		reorder();
	}

	private void reorder() {
		table.clearChildren();
		final float size = slots.get(0).getWidth();
		float total = size+16;
		for (Slot slot : slots) {
			table.add(slot);
			total += size;
			if (total > contain.getWidth()) {
				table.row();
				total = size+16;
			}
		}
	}
	
	@Override
	protected void hide() {
		main.stage.unfocus(contain);
	}

	@Override
	public void save(Properties props) {
		Properties newProps = props.newProps("items");
		for (int i = 0; i < slots.size; i++) {
			final Item item = slots.get(i).item;
			if (item == null || item.isEmpty()) continue;
			Properties itemProps = newProps.newProps();
			itemProps.putClass(item);
			itemProps.put("index", (short)i);
			item.save(itemProps);
		}
	}

	@Override
	public void load(Properties props) {
		Properties newProps = props.getProps("items");
		for (Object obj : newProps.array()) {
			Properties itemProps = (Properties)obj;
			Item item = itemProps.newObject();
			item.load(itemProps);
			slots.get((short)itemProps.get("index")).item = item;
		}
	}
}
