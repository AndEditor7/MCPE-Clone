package com.andedit.arcubit.ui;

import static com.andedit.arcubit.Main.main;
import static com.andedit.arcubit.TheGame.game;
import static com.andedit.arcubit.world.World.world;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.Sounds;
import com.andedit.arcubit.blocks.furnace.FurnaceContent;
import com.andedit.arcubit.ui.actors.FurnaceGroup;
import com.andedit.arcubit.ui.actors.Slot;
import com.andedit.arcubit.ui.utils.BaseUI;
import com.andedit.arcubit.ui.utils.PosOffset;
import com.andedit.arcubit.utils.Util;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;

public class FurnaceUI extends BaseUI {
	
	private final Table master = new Table();
	
	private final Table title;
	private final Table invenContent = new Table();
	private final ScrollPane invenScroll;
	
	private final FurnaceGroup group;
	
	public final Inventory inventory;
	public FurnaceContent content;
	
	public FurnaceUI(final Inventory inventory) {
		this.inventory = inventory;
		
		master.setBackground(new NinePatchDrawable(new NinePatch(new TextureRegion(Assets.SPRITES, 0, 0, 16, 16), 4, 4, 4, 4)));
		master.setUserObject(new Vector2(0.0f, 1.0f));
		master.align(Align.topLeft);
		add(master);
		
		NinePatch ninePatch = new NinePatch(new TextureRegion(Assets.SPRITES, 42, 1, 16, 16), 3, 3, 2, 3);
		title = new Table().background(new NinePatchDrawable(ninePatch));
		title.setHeight(25);
		title.setUserObject(new PosOffset(0.5f, 1.0f, 0, -12.5f));
		title.add(new Label("Furnace", Assets.SKIN));
		title.align(Align.center);
		add(title);
		
		for (final Slot slot : inventory.slots) {
			slot.addListener(new ClickListener() {
				public void clicked(InputEvent event, float x, float y) {
					if (game.manager.IsUI(FurnaceUI.class) && !slot.isEmpty()) {
						if (content.putItem(slot.item) && content.canBeSmelt()) {
							world.addActiveFurn(content);
						}
					}
				}
			});
		}
		
		Table table = new Table() {
			public void draw(Batch batch, float parentAlpha) {
				if (hasResize) resize(getWidth());
				super.draw(batch, parentAlpha);
			}
		};
		table.background(new NinePatchDrawable(new NinePatch(new TextureRegion(Assets.TEXTURE, 135, 390, 20, 116), 1, 1, 2, 1)));
		invenScroll = new ScrollPane(invenContent);
		table.add(invenScroll);
		master.add(table).grow().padBottom(20).padLeft(5).padTop(3+25);
		invenScroll.setScrollingDisabled(true, false);
		invenScroll.setFlickScrollTapSquareSize(5);
		invenScroll.setSmoothScrolling(false);
		
		group = new FurnaceGroup(this);
		master.add(group).size(group.getWidth(), group.getHeight()).align(Align.topRight).padRight(15).padTop(10+25).padLeft(10);
		
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
	
	public void setContent(FurnaceContent content) {
		this.content = content;
	}
	
	@Override
	protected void show() {
		if (Util.isCatched()) game.steve.useMouse(true);
		hasResize = true;
		invenScroll.setScrollY(0);
	}
	
	@Override
	protected void hide() {
		main.stage.unfocus(master);
	}
	
	private boolean hasResize = true;
	
	@Override
	public void resize(Viewport view) {
		master.setSize(view.getWorldWidth(), view.getWorldHeight());
		title.setWidth(view.getWorldWidth());
		hasResize = true;
	}
	
	private void resize(float width) {
		invenContent.clearChildren();
		float size = 24;
		float total = size;
		for (Slot slot : inventory.slots) {
			invenContent.add(slot);
			total += size;
			if (total > width-2) {
				invenContent.row();
				total = size;
			}
		}
		hasResize = false;
	}
}
