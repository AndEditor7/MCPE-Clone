package com.andedit.arcubit.entity.ai;

import com.badlogic.gdx.utils.Array;

public class Brain {

	public int waitTimer;
	public Ai current;
	public final Array<Ai> list = new Array<>(8);
	public final Array<Ai> unlist = new Array<>(5);

	public void update() {
		
		if (unlist.notEmpty() && current != null && current.isRandomPick()) 
		for (Ai ai : unlist) {
			if (ai.check()) {
				setAi(ai);
			}
		}

		if (--waitTimer < 0)
		if (current == null || current.update()) {
			if (current == null) {
				current = list.random();
				current.start();
			} else {
				waitTimer = current.finish();
				current = null;
			}
		}
	}

	public void reset() {
		if (current != null) {
			current.finish();
			current = null;
		}
		waitTimer = 0;
	}

	public void add(Ai ai) {
		(ai.isRandomPick() ? list : unlist).add(ai);
	}

	public void add(Ai ai, int num) {
		for (int i = 0; i < num; i++)
			list.add(ai);
	}

	public void setAi(Class<? extends Ai> clazz) {
		for (Ai ai : unlist) {
			if (ai.getClass() == clazz) {
				setAi(ai);
				break;
			}
		}
		for (Ai ai : list) {
			if (ai.getClass() == clazz) {
				setAi(ai);
				break;
			}
		}
	}

	public void setAi(Ai ai) {
		reset();
		current = ai;
		current.start();
	}

	public boolean hasAi(Class<? extends Ai> clazz) {
		return current == null ? false : current.getClass() == clazz;
	}
}
