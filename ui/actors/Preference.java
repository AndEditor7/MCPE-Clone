package com.andedit.arcubit.ui.actors;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.Sounds;
import com.andedit.arcubit.options.BoolPref;
import com.andedit.arcubit.options.FloatPref;
import com.andedit.arcubit.options.IntPref;
import com.andedit.arcubit.options.Pref;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

public class Preference extends Table {
	
	private final Label label;
	private Actor actor;
	
	public Preference(String title, Pref<?> pref) {
		setTransform(false);
		label = new Label(title, Assets.SKIN);
		label.setColor(Color.LIGHT_GRAY);
		label.setAlignment(Align.center);
		add(label).align(Align.center).growX();
		//debug();
		
		if (pref instanceof BoolPref) {
			BoolPref bools = (BoolPref)pref;
			SimpleButt button = new SimpleButt(Assets.SKIN, "switch");
			button.isChecked = bools.value;
			actor = button;
			button.addListener(new ChangeListener() {
				public void changed(ChangeEvent event, Actor actor) {
					bools.set(button.isChecked);
					Sounds.click();
				}
			});
		} else if (pref instanceof IntPref) {
			IntPref ints = (IntPref)pref;
			actor = new Slider(ints);
		} else if (pref instanceof FloatPref) {
			FloatPref floats = (FloatPref)pref;
			actor = new Slider(floats);
		}
		
		Cell<Actor> cell = add(actor).align(Align.right);
		if (actor instanceof Slider) {
			cell.padRight(6);
		}
	}
}
