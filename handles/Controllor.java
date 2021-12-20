package com.andedit.arcubit.handles;

import com.andedit.arcubit.utils.Util;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.scenes.scene2d.utils.Disableable;

public class Controllor extends InputAdapter implements Disableable {
	
	public boolean isForward, isRight, isLeft, isBack, isJump, hasOut, isMining, isPressDown, isDown;
	public int deltaX, deltaY, lastX, lastY;
	
	private boolean isDisabled, isPlaces;
	private int point, timer, click = -1;
	private GridPoint2 lastDown = new GridPoint2();
	
	{ 
		Gdx.input.setCursorPosition(0, 0);
	}
	
	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.W) isForward = true;
		if (keycode == Keys.A) isLeft = true;
		if (keycode == Keys.D) isRight = true;
		if (keycode == Keys.S) isBack = true;
		if (keycode == Keys.SPACE) isJump = true;
		if (keycode == Keys.SHIFT_LEFT) isDown = true;
		return false;
	}
	
	@Override
	public boolean keyUp(int keycode) {
		if (keycode == Keys.W) isForward = false;
		if (keycode == Keys.A) isLeft = false;
		if (keycode == Keys.D) isRight = false;
		if (keycode == Keys.S) isBack = false;
		if (keycode == Keys.SPACE) isJump = false;
		if (keycode == Keys.SHIFT_LEFT) isDown = false;
		return false;
	}
	
	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		deltaX = lastX - screenX;
		deltaY = screenY - lastY;
		lastX = screenX;
		lastY = screenY;
		return false;
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		point = pointer;
		lastX = screenX;
		lastY = screenY;
		lastDown.set(screenX, screenY);
		hasOut = false;
		isMining = false;
		timer = 40;
		isPressDown = true;
		click = Util.isDesktop() ? button : -1;
		return false;
	}
	
	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		isPressDown = false;
		click = -1;
		if (timer > 20 && !hasOut && !isMining) {
			isPlaces = true;
		}
		return false;
	}
	
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if (pointer == point) {
			deltaX = lastX - screenX;
			deltaY = screenY - lastY;
			lastX = screenX;
			lastY = screenY;
		}
		return false;
	}

	@Override
	public void setDisabled(boolean isDisabled) {
		this.isDisabled = isDisabled;
		isForward = false;
		isRight = false;
		isLeft = false;
		isBack = false;
		isJump = false;
	}

	@Override
	public boolean isDisabled() {
		return isDisabled;
	}
	
	public void update() {
		if (!isMining && lastDown.dst(lastX, lastY) > 40) {
			hasOut = true;
		}
		
		if (hasOut) return;

		timer--;
		if (timer <= 0) {
			isMining = true;
		}
		
		if (isMining && timer < 0) {
			timer = 25;
		}
	}
	
	public boolean isMoving() {
		return isForward || isBack || isLeft || isRight;
	}
	
	public boolean allowBreak() {
		if (Util.isDesktop()) {
			if (click == Buttons.LEFT) {
				click = -1;
				return true;
			}
			return false;
		}
		
		return isMining && timer == 0;
	}
	
	public boolean isOnHold() {
		return Util.isDesktop() ? click == Buttons.RIGHT : isMining && timer == 0;
	}
	
	public boolean allowPlace() {
		if (Util.isDesktop()) {
			if (click == Buttons.RIGHT) {
				click = -1;
				return true;
			}
			return false;
		}
		
		if (isPlaces) {
			isPlaces = false;
			return true;
		}
		return false;
	}
}
