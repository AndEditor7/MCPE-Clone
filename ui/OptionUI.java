package com.andedit.arcubit.ui;

import static com.andedit.arcubit.Main.main;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.Sounds;
import com.andedit.arcubit.ui.actors.Background;
import com.andedit.arcubit.ui.actors.Preference;
import com.andedit.arcubit.ui.utils.BaseUI;
import com.andedit.arcubit.ui.utils.UIManager;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

public class OptionUI extends BaseUI {
	
	private boolean isMenu;
	private final Background backgroundMenu = new Background();
	private final Image backgroundGame = new Image(Assets.BLANK);
	private final Button[] tabs = new Button[4];
	private final Table master;
	private final Table columns;
	
	public static final Array<Preference> gameTab = new Array<>();
	public static final Array<Preference> controlTab = new Array<>();
	public static final Array<Preference> graphicTab = new Array<>();
	public static final Array<Preference> soundTab = new Array<>();
	
	public OptionUI(UIManager manager) {
		add(backgroundMenu);
		add(backgroundGame);
		backgroundGame.setColor(0, 0, 0, 0.6f);
		
		master = new Table();
		master.setUserObject(new Vector2(0, 1));
		master.align(Align.topLeft);
		master.setSize(main.view.getWorldWidth(), main.view.getWorldHeight());
		add(master);
		
		TextButton textButt = new TextButton("Back", Assets.SKIN);
		textButt.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				if (isMenu) {
					manager.setUI(Menu.class);
				} else {
					manager.setUI(Setting.class);
				}
				Sounds.click();
			}
		});
		
		Table table = new Table();
		table.setBackground(new NinePatchDrawable(new NinePatch(new TextureRegion(Assets.SPRITES, 42, 1, 16, 16), 3, 3, 2, 3)));
		table.add(new Label("Options", Assets.SKIN));
		
		master.add(textButt).size(50, 25);
		master.add(table).height(25).growX().row();;
		
		table = new Table().background(new TextureRegionDrawable(new TextureRegion(Assets.TEXTURE, 7, 391, 1, 1)));
		ScrollPane scroll = new ScrollPane(table);
		scroll.setScrollingDisabled(true, false);
		scroll.setFlickScrollTapSquareSize(5);
		scroll.setSmoothScrolling(false);
		scroll.setFadeScrollBars(false);
		master.add(scroll).width(40).growY().padTop(1).align(Align.left);
		
		
		// Tabs start
		final ChangeListener change = new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				int id = (int) actor.getUserObject();
				if (!tabs[id].isDisabled()) {
					setTab(id);
					Sounds.click();
				}
			}
		};
		
		Button button;
		tabs[0] = button = new Button(Assets.SKIN.get("blank", ButtonStyle.class));
		button.add(new Image(new TextureRegion(Assets.TEXTURE, 128, 325, 30, 28)));
		table.add(button).size(35, 30).padBottom(2).row();
		button.addListener(change);
		button.setUserObject(0);
		
		tabs[1] = button = new Button(Assets.SKIN.get("blank", ButtonStyle.class));
		button.add(new Image(new TextureRegion(Assets.TEXTURE, 100, 324, 30, 28)));
		table.add(button).size(35, 30).padBottom(2).row();
		button.addListener(change);
		button.setUserObject(1);
		
		tabs[2] = button = new Button(Assets.SKIN.get("blank", ButtonStyle.class));
		button.add(new Image(new TextureRegion(Assets.TEXTURE, 128, 353, 30, 28)));
		table.add(button).size(35, 30).padBottom(2).row();
		button.addListener(change);
		button.setUserObject(2);
		
		tabs[3] = button = new Button(Assets.SKIN.get("blank", ButtonStyle.class));
		button.add(new Image(new TextureRegion(Assets.TEXTURE, 100, 352, 30, 28)));
		table.add(button).size(35, 30);
		button.addListener(change);
		button.setUserObject(3);
		
		scroll = new ScrollPane(columns = new Table());
		master.add(scroll).grow().padRight(12);
		scroll.setScrollingDisabled(true, false);
		scroll.setFlickScrollTapSquareSize(5);
		scroll.setSmoothScrolling(false);
		scroll.setFadeScrollBars(false);
	}

	private void setTab(int index) {
		for (Button button : tabs) {
			button.setDisabled(false);
		}
		tabs[index].setDisabled(true);
		
		columns.clearChildren();
		Array<Preference> array = getTab(index);
		for (Preference pref : array) {
			columns.add(pref).growX().size(0, 24).row();
		}
	}
	
	private Array<Preference> getTab(int id) {
		switch (id) {
		case 0: return gameTab;
		case 1: return controlTab;
		case 2: return graphicTab;
		case 3: return soundTab;
		default: return null;
		}
	}
	
	public void setMenu(boolean isMenu) {
		backgroundMenu.setVisible(isMenu);
		backgroundGame.setVisible(!isMenu);
		this.isMenu = isMenu;
	}
	
	@Override
	public void resize(Viewport view) {
		master.setSize(view.getWorldWidth(), view.getWorldHeight());
		backgroundGame.setSize(view.getWorldWidth(), view.getWorldHeight());
		backgroundMenu.setSize(view.getWorldWidth(), view.getWorldHeight());
	}
	
	@Override
	protected void show() {
		setTab(0);
	}
	
	@Override
	protected void hide() {
		main.stage.unfocus(master);
	}
}
