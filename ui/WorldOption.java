package com.andedit.arcubit.ui;

import static com.andedit.arcubit.Main.main;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.Sounds;
import com.andedit.arcubit.TheMenu;
import com.andedit.arcubit.WorldScreen;
import com.andedit.arcubit.ui.actors.Background;
import com.andedit.arcubit.ui.utils.BaseUI;
import com.andedit.arcubit.ui.utils.PosOffset;
import com.andedit.arcubit.ui.utils.UIManager;
import com.andedit.arcubit.world.loader.WorldGenerator;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldFilter;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;

public class WorldOption extends BaseUI {
	
	private final TextField field = new TextField("World", Assets.SKIN);
	private final Label label = new Label(null, Assets.SKIN);
	private final Background background = new Background();

	public WorldOption(final TheMenu menu) {
		final UIManager manager = menu.manager;
		add(background);
		
		field.setAlignment(Align.center);
		field.setUserObject(new Vector2(0.5f, 0.8f));
		field.setSize(100, 20);
		field.setTextFieldFilter(FILTER);
		add(field);
		
		label.setAlignment(Align.top);
		label.setUserObject(new PosOffset(0.5f, 0.8f, 0, -20));
		add(label);
		
		Button button = new Button(Assets.SKIN.get("back", ButtonStyle.class));
		button.setUserObject(new Vector2(1.0f, 1.0f));
		button.setSize(18, 18);
		button.align(Align.topRight);
		add(button);
		button.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				menu.manager.setUI(WorldSeletion.class);
				Sounds.click();
			}
		});
		
		TextButton TextButton = new TextButton("Generate", Assets.SKIN);
		TextButton.setUserObject(new Vector2(0.5f, 0.3f));
		TextButton.setSize(80, 30);
		TextButton.align(Align.center);
		add(TextButton);
		TextButton.addListener(new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor) {
				WorldSeletion seletion = manager.getUI(WorldSeletion.class);
				FileHandle[] folders = seletion.folders;
				
				for (FileHandle folder : folders) {
					if (folder.name().equalsIgnoreCase(field.getText())) {
						label.setText("World is already existed.");
						main.resize();
						return;
					}
				}
				
				FileHandle folder = seletion.saves.child(field.getText());
				main.setScreen(new WorldScreen(new WorldGenerator(), folder));
			}
		});
	}
	
	@Override
	protected void show() {
		label.setText(null);
		field.setText("World");
	}
	
	@Override
	protected void hide() {
		main.stage.unfocus(field);
	}
	
	@Override
	public void resize(Viewport view) {
		background.setSize(view.getWorldWidth(), view.getWorldHeight());
	}
	
	private static final TextFieldFilter FILTER = new TextFieldFilter() {
		public boolean acceptChar(TextField textField, char c) {
			return Character.isLetterOrDigit(c) || c == ' ';
		}
	};
}
