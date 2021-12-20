package com.andedit.arcubit.ui.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Array;

/** AndEditor7's Stage utilities. */
public class StageUtils 
{	
	/** Re-position all actors that contains Vector2 or PosOffset in userObject. */
	public static void resize(Stage stage) {
		final Array<Actor> acts = stage.getActors();
		final int size = acts.size;
		final float w = stage.getViewport().getWorldWidth();
		final float h = stage.getViewport().getWorldHeight();
		
		for (int i = 0; i < size; ++i) {
			setPos(acts.get(i), w, h);
		}
	}
	
	private static void setPos(Actor actor, float w, float h) 
	{
		final Object obj = actor.getUserObject();
		if (obj == null) return;
		
		final Vector2 pos, offset;
		final Class<?> clazz = obj.getClass();
		if (clazz == Vector2.class) {
			pos = (Vector2)obj;
			offset = Vector2.Zero;
		} else if (clazz == PosOffset.class) {
			PosOffset po = (PosOffset)obj;
			pos = po.pos;
			offset = po.offset;
		} else return; // Make sure it returns because there's no Vector2 or PosOffset class.
		
		int align = -1;
		if (actor instanceof Table) {
			align = ((Table)actor).getAlign();
		} else if (actor instanceof TextField) {
			align = ((TextField)actor).getAlignment();
		} else if (actor instanceof Label) {
			align = ((Label)actor).getLabelAlign();
		} else if (actor instanceof Image) {
			align = ((Image)actor).getAlign();
		} else if (actor instanceof Alignment) {
			align = ((Alignment)actor).getAlign();
		}
		
		final float x = (pos.x * w) + offset.x;
		final float y = (pos.y * h) + offset.y;
		if (align != -1) {
			actor.setPosition(x, y, align);
		} else {
			actor.setPosition(x, y);
		}
	}
}
