package com.andedit.arcubit.ui.actors;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.handles.Controllor;
import com.andedit.arcubit.ui.utils.Alignment;
import com.andedit.arcubit.ui.utils.PosOffset;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;

public class MovmentButtion extends Group implements Alignment {
	
	private int align = Align.bottomLeft;

	private final Image up, down, left, right, center;
	
	public MovmentButtion(final Controllor controllor) {
		setUserObject(new PosOffset(0, 0, 15, 10));
		setSize(74, 74);
		
		final float scale = 1.0f;
		setTransform(false);

		Actor actor = new Actor();
		actor.setSize(50*scale, 50*scale);
		actor.setPosition(37*scale, 37*scale, Align.center);
		actor.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
		});
		addActor(actor);

		up = new Image(new TextureRegionDrawable(Assets.UP_ARROW));
		up.setSize(up.getWidth()*scale, up.getHeight()*scale);
		up.setPosition(37*scale, 74*scale, Align.top);
		up.setColor(1,1,1, 0.7f);
		addActor(up);
		up.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				controllor.isForward = true;
				up.setColor(1,1,1, 0.5f);
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				controllor.isForward = false;
				up.setColor(1,1,1, 0.7f);
			}
		});

		down = new Image(new TextureRegionDrawable(Assets.DOWN_ARROW));
		down.setSize(down.getWidth()*scale, down.getHeight()*scale);
		down.setPosition(37*scale, 0*scale, Align.bottom);
		down.setColor(1,1,1, 0.7f);
		addActor(down);
		down.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				controllor.isBack = true;
				down.setColor(1,1,1, 0.5f);
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				controllor.isBack = false;
				down.setColor(1,1,1, 0.7f);
			}
		});

		left = new Image(new TextureRegionDrawable(Assets.LEFT_ARROW));
		left.setSize(left.getWidth()*scale, left.getHeight()*scale);
		left.setPosition(0*scale, 37*scale, Align.left);
		left.setColor(1,1,1, 0.7f);
		addActor(left);
		left.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				controllor.isLeft = true;
				left.setColor(1,1,1, 0.5f);
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				controllor.isLeft = false;
				left.setColor(1,1,1, 0.7f);
			}
		});

		right = new Image(new TextureRegionDrawable(Assets.RIGHT_ARROW));
		right.setSize(right.getWidth()*scale, right.getHeight()*scale);
		right.setPosition(74*scale, 37*scale, Align.right);
		right.setColor(1,1,1, 0.7f);
		addActor(right);
		right.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				controllor.isRight = true;
				right.setColor(1,1,1, 0.5f);
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				controllor.isRight = false;
				right.setColor(1,1,1, 0.7f);
			}
		});

		center = new Image(new TextureRegionDrawable(Assets.CENTER_ARROW));
		center.setSize(center.getWidth()*scale, center.getHeight()*scale);
		center.setPosition(37*scale, 37*scale, Align.center);
		center.setColor(1,1,1, 0.7f);
		addActor(center);
		center.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				controllor.isJump = true;
				center.setColor(1,1,1, 0.5f);
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				controllor.isJump = false;
				center.setColor(1,1,1, 0.7f);
			}
		});
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		final Color c = batch.getColor();
		final float a = c.a;
		super.draw(batch, a);
		batch.setColor(c.r, c.g, c.b, a);
	}

	@Override
	public int getAlign() {
		return align;
	}

	@Override
	public void setAlign(int align) {
		this.align = align;
	}
}
