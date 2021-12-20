package com.andedit.arcubit.ui.actors;

import static com.andedit.arcubit.Assets.KNOB;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.options.FloatPref;
import com.andedit.arcubit.options.IntPref;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Align;

public class Slider extends Actor {
	
	private final IntPref intPref;
	private final FloatPref floatPref;
	private int dragPointer = -1;
	private float xPos;
	
	public Slider(IntPref pref) {
		intPref = pref;
		floatPref = null;
		ints();
		xPos = ((pref.value-pref.min) * getWidth()) / (float)(pref.max-pref.min);
	}
	
	public Slider(FloatPref pref) {
		intPref = null;
		floatPref = pref;
		ints();
		xPos = ((pref.value-pref.min) * getWidth()) / (pref.max-pref.min);
	}
	
	private void ints() {
		setSize(100, 17);
		addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				if (dragPointer != -1) return false;
				dragPointer = pointer;
				calcPosAndVal(x);
				event.setBubbles(false);
				return true;
			}

			public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
				if (pointer != dragPointer) return;
				dragPointer = -1;
			}

			public void touchDragged (InputEvent event, float x, float y, int pointer) {
				calcPosAndVal(x);
			}
		});
	}
	
	@Override
	public void draw(Batch batch, float height) {
		batch.setColor(Color.GRAY);
		batch.draw(Assets.BLANK, getX(), getY()+7f, getWidth(), getHeight()-14f);
		
		if (isInt()) {
			int steps = intPref.max - intPref.min;
			for (int i = 0; i < steps+1; i++) {
				batch.draw(Assets.BLANK, getX()+((i*getWidth())/(float)steps)-1.5f, getY()+4f, 3, getHeight()-8f);
			}
		} else {
			batch.draw(Assets.BLANK, getX()-1.5f, getY()+4f, 3, getHeight()-8f);
			batch.draw(Assets.BLANK, getX()+getWidth()-1.5f, getY()+4f, 3, getHeight()-8f);
		}
		
		batch.setColor(Color.WHITE);
		float x = xPos+getX()-(KNOB.getRegionWidth()/2f);
		float y = getY()-(KNOB.getRegionHeight()/2f) + getOriginY();
		batch.draw(KNOB, x, y);
	}
	
	private boolean isInt() {
		return floatPref == null;
	}
	
	@Override
	protected void sizeChanged() {
		setOrigin(Align.center);
	}
	
	private void calcPosAndVal(float x) {
		// Clamp the knob.
		x = MathUtils.clamp(x, 0f, getWidth());
		
		// Get steps amount.
		float steps;
		if (isInt()) {
			steps = intPref.max - intPref.min;
		} else {
			steps = floatPref.max - floatPref.min;
		}
		x = (x / getWidth()) * steps;
		
		// Round it and set the result.
		float a;
		if (isInt()) {
			int result = Math.round(x);
			intPref.set(intPref.min + result);
			a = result;
		} else {
			floatPref.set(floatPref.min + x);
			a = x;
		}
		
		// Update the knob position.
		xPos = (a * getWidth()) / steps;
	}
}
