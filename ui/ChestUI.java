package com.andedit.arcubit.ui;

import static com.andedit.arcubit.TheGame.game;
import static com.andedit.arcubit.Main.main;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.Sounds;
import com.andedit.arcubit.blocks.ChestBlock.ChestContent;
import com.andedit.arcubit.ui.actors.ChestSlot;
import com.andedit.arcubit.ui.actors.Slot;
import com.andedit.arcubit.ui.utils.BaseUI;
import com.andedit.arcubit.utils.Util;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;

public class ChestUI extends BaseUI {
	
	private final Inventory inventory;
	private ChestContent content;
	
	private final Table table = new Table();
	private final Image background = new Image(new NinePatch(new TextureRegion(Assets.SPRITES, 0, 0, 16, 16), 4, 4, 4, 4));
	
	private final Table invenContent = new Table();
	private final Table chestContent = new Table();
	
	private final ScrollPane invenScroll;
	private final ScrollPane chestScroll;
	
	private final Cell<ScrollPane> invenCell;
	private final Cell<ScrollPane> chestCell;
	
	private final ChestSlot[] slots = new ChestSlot[27];

	public ChestUI(final Inventory inventory) {
		this.inventory = inventory;
		background.setUserObject(Vector2.Zero);
		background.setAlign(Align.bottomLeft);
		add(background);
		add(table);
		
		table.align(Align.topLeft);
		table.setUserObject(new Vector2(0.0f, 1.0f));
		
		for (int i = 0; i < slots.length; i++) {
			final ChestSlot slot = new ChestSlot();
			slots[i] = slot;
			slot.addListener(new ClickListener() {
				public void clicked(InputEvent event, float x, float y) {
					if (inventory.addItem(slot.getItem())) {
						slot.getItem().size = 0;
					}
				}
			});
		}
		for (final Slot slot : inventory.slots) {
			slot.addListener(new ClickListener() {
				public void clicked(InputEvent event, float x, float y) {
					if (game.manager.IsUI(ChestUI.class) && content != null) {
						if (content.addItem(slot.item)) {
							slot.item.size = 0;
						}
					}
				}
			});
		}
		
		NinePatch ninePatch = new NinePatch(new TextureRegion(Assets.SPRITES, 42, 1, 16, 16), 3, 3, 2, 3);
		Label inv = new Label("Inventory", Assets.SKIN);
		inv.setAlignment(Align.center);
		Label che = new Label("Chest", Assets.SKIN);
		che.setAlignment(Align.center);
		table.add(new Table().background(new NinePatchDrawable(ninePatch)).add(inv).align(Align.center).width(5f).getTable()).height(25).growX();
		table.add(new Table().background(new NinePatchDrawable(ninePatch)).add(che).align(Align.center).width(5f).getTable()).height(25).growX().row();;
		table.row();
		
		invenScroll = new ScrollPane(invenContent);
		invenCell = table.add(invenScroll).growX();
		invenScroll.setScrollingDisabled(true, false);
		invenScroll.setFlickScrollTapSquareSize(5);
		invenScroll.setSmoothScrolling(false);
		
		chestScroll = new ScrollPane(chestContent) {
			public void draw(Batch batch, float parentAlpha) {
				batch.setColor(Color.WHITE);
				super.draw(batch, 1);
			}
		};
		chestCell = table.add(chestScroll).growX();
		chestScroll.setScrollingDisabled(true, false);
		chestScroll.setFlickScrollTapSquareSize(5);
		chestScroll.setSmoothScrolling(false);
		
		Button button = new Button(Assets.SKIN.get("back", ButtonStyle.class));
		button.setUserObject(new Vector2(1.0f, 1.0f));
		button.setSize(18, 18);
		button.align(Align.topRight);
		add(button);
		button.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				game.manager.setUI(InGame.class);
				Sounds.click();
			}
		});
		add(button);
	}
	
	public void setContent(ChestContent content) {
		this.content = content;
		
		for (int i = 0; i < content.items.length; i++) {
			slots[i].setItems(content.items, i);
		}
		
		resize(main.stage.getViewport());
	}
	
	@Override
	protected void show() {
		if (Util.isCatched()) game.steve.useMouse(true);
		
		invenScroll.setScrollY(0);
		chestScroll.setScrollY(0);
	}
	
	@Override
	protected void hide() {
		main.stage.unfocus(table);
	}
	
	@Override
	public void resize(Viewport view) {
		table.setSize(view.getWorldWidth(), view.getWorldHeight());
		background.setSize(view.getWorldWidth(), view.getWorldHeight());
		invenCell.height(view.getWorldHeight() - 28);
		chestCell.height(view.getWorldHeight() - 28);
		
		final float haft = view.getWorldWidth() * 0.5f;
		
		chestContent.clearChildren();
		float size = slots[0].getWidth();
		float total = size+16;
		for (ChestSlot slot : slots) {
			chestContent.add(slot);
			total += size;
			if (total > haft) {
				chestContent.row();
				total = size+16;
			}
		}
		
		invenContent.clearChildren();
		size = slots[0].getWidth();
		total = size+16;
		for (Slot slot : inventory.slots) {
			invenContent.add(slot);
			total += size;
			if (total > haft) {
				invenContent.row();
				total = size+16;
			}
		}
	}
}
