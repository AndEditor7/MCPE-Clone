package com.andedit.arcubit.ui;

import static com.andedit.arcubit.TheGame.game;

import com.andedit.arcubit.Main;
import com.andedit.arcubit.Assets;
import com.andedit.arcubit.Sounds;
import com.andedit.arcubit.crafting.CraftManager;
import com.andedit.arcubit.crafting.Recipe;
import com.andedit.arcubit.ui.actors.Column;
import com.andedit.arcubit.ui.actors.Craft;
import com.andedit.arcubit.ui.actors.Slot;
import com.andedit.arcubit.ui.utils.BaseUI;
import com.andedit.arcubit.ui.utils.PosOffset;
import com.andedit.arcubit.ui.utils.UI;
import com.andedit.arcubit.utils.Util;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Crafting extends BaseUI {
	
	public static final Crafting crafting = new Crafting();
	
	private static final int BUILD = 0;
	private static final int TOOLS = 1;
	private static final int ARRMOR = 2;
	private static final int MISC = 3;
	
	private final Array<Column> buildTab = new Array<>();
	private final Array<Column> toolsTab = new Array<>();
	private final Array<Column> armorTab = new Array<>();
	private final Array<Column> miscTab = new Array<>();
	private final Craft craft = new Craft(this);
	
	private void ints() {
		for (Recipe recipe : CraftManager.BUILD) {
			buildTab.add(new Column(craft, recipe));
		}
		for (Recipe recipe : CraftManager.TOOLS) {
			toolsTab.add(new Column(craft, recipe));
		}
		for (Recipe recipe : CraftManager.ARMOR) {
			armorTab.add(new Column(craft, recipe));
		}
		for (Recipe recipe : CraftManager.MISC) {
			miscTab.add(new Column(craft, recipe));
		}
	}
	
	private boolean is3x3;
	public Inventory inventory;
	
	// UIs
	private final Table content = new Table();
	
	private final Table columns;
	private final Button[] tabs = new Button[4];
	
	private Crafting() {
		ints();
		
		content.setUserObject(Vector2.Zero);
		content.align(Align.bottomLeft);
		//content.setDebug(true);
		content.setBackground(new NinePatchDrawable(new NinePatch(new TextureRegion(Assets.SPRITES, 0, 0, 16, 16), 4, 4, 4, 4)));
		add(content);
		
		Table table = new Table();
		content.add(table).growY().width(45);
		
		// Tabs start
		final ChangeListener change = new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				final int id = (int) actor.getUserObject();
				if (!tabs[id].isDisabled()) {
					setTab(id);
					Sounds.click();
				}
			}
		};
		
		Button button = new Button(Assets.SKIN.get("blank", ButtonStyle.class));
		button.add(new Image(new TextureRegion(Assets.SPRITES, 4, 68, 24, 24)));
		table.add(button).grow().pad(1).row();
		button.addListener(change);
		button.setUserObject(BUILD);
		tabs[BUILD] = button;
		button.setTransform(false);
		
		button = new Button(Assets.SKIN.get("blank", ButtonStyle.class));
		button.add(new Image(new TextureRegion(Assets.SPRITES, 4, 100, 24, 24)));
		table.add(button).grow().pad(1).row();
		button.setUserObject(TOOLS);
		button.addListener(change);
		tabs[TOOLS] = button;
		button.setTransform(false);
		
		button = new Button(Assets.SKIN.get("blank", ButtonStyle.class));
		button.add(new Image(new TextureRegion(Assets.SPRITES, 36, 68, 24, 24)));
		table.add(button).grow().pad(1).row();
		button.setUserObject(ARRMOR);
		button.addListener(change);
		tabs[ARRMOR] = button;
		button.setTransform(false);
		
		button = new Button(Assets.SKIN.get("blank", ButtonStyle.class));
		button.add(new Image(new TextureRegion(Assets.SPRITES, 36, 100, 24, 24)));
		table.add(button).grow().pad(1).row();
		button.setUserObject(MISC);
		button.addListener(change);
		tabs[MISC] = button;
		button.setTransform(false);
		// Tabs end
		
		// Scroll start
		Table container = new Table();
		container.setBackground(new NinePatchDrawable(new NinePatch(new TextureRegion(Assets.SPRITES, 0, 44, 8, 8), 1, 1, 1, 2)));
		content.add(container).grow().padLeft(4).padRight(5).padTop(1).padBottom(1);
		container.setTransform(false);
		content.setTransform(false);
		
		ScrollPane scroll = new ScrollPane(columns = new Table());
		container.add(scroll).grow();
		scroll.setScrollingDisabled(true, false);
		scroll.setFlickScrollTapSquareSize(5);
		scroll.setSmoothScrolling(false);
		// Scroll end
		
		content.add(craft).padRight(5).padTop(18).size(80, 64).align(Align.topRight);
		
		// back button
		button = new Button(Assets.SKIN.get("back", ButtonStyle.class));
		button.setUserObject(new PosOffset(1.0f, 1.0f, -1, -1));
		button.setSize(18, 18);
		button.align(Align.topRight);
		add(button);
		button.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				game.manager.setUI((Class<? extends UI>)(is3x3 ? InGame.class : Inventory.class));
				if (is3x3) game.steve.useMouse(false);
				Sounds.click();
			}
		});
	}
	
	public Crafting setInvstory(Inventory inventory) {
		this.inventory = inventory;
		return this;
	}
	
	private int index;
	private void setTab(int index) {
		this.index = index;
		for (Button button : tabs) {
			button.setDisabled(false);
		}
		tabs[index].setDisabled(true);
		
		columns.clearChildren();
		final Array<Column> array = getTab(index);
		
		for (Column column : array) {
			if (is3x3 || !column.is3x3()) {
				columns.add(column).growX().width(0).height(22).row();
			}
		}
		
		refresh();
		craft.clearStuff();
	}
	
	public void refresh() {
		final Array<Column> array = getTab(index);
		
		for (Column column : array) {
			column.begin();
		}
		for (Slot slot : inventory.slots) {
			for (Column column : array) {
				column.checkItem(slot);
			}
		}
		for (Column column : array) {
			column.end();
		}
	}
	
	private Array<Column> getTab(int id) {
		switch (id) {
		case BUILD: return buildTab;
		case TOOLS: return toolsTab;
		case ARRMOR: return armorTab;
		case MISC: return miscTab;
		default: return null;
		}
	}
	
	public void is3x3Grid(boolean bool) {
		is3x3 = bool;
	}
	
	@Override
	protected void show() {
		setTab(0);
		if (Util.isCatched()) game.steve.useMouse(true);
	}
	
	@Override
	protected void hide() {
		Main.main.stage.unfocus(content);
		craft.clearStuff();
	}
	
	@Override
	public void resize(Viewport view) {
		content.setSize(view.getWorldWidth(), view.getWorldHeight());
	}
}
