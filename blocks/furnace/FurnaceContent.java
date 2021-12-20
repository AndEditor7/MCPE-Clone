package com.andedit.arcubit.blocks.furnace;

import com.andedit.arcubit.file.Properties;
import com.andedit.arcubit.file.Serial;
import com.andedit.arcubit.items.Item;
import com.andedit.arcubit.utils.BlockPos;
import com.badlogic.gdx.utils.Pool.Poolable;

public class FurnaceContent implements Poolable, Serial {
	public static final int MAX_PROCESS = 500;
	
	public BlockPos pos;
	public Item fuel, smelt, result;
	public int burnTimer, maxBurnTimer, process;
	
	public FurnaceContent() {
		
	}
	
	public FurnaceContent(BlockPos pos) {
		this.pos = pos.clone();
	}
	
	public boolean putItem(final Item item) {
		if (!(item instanceof FurnaceRecipe)) return false;
		
		final FurnaceRecipe recipe = (FurnaceRecipe)item;
		final FurnaceUses uses = recipe.getFurnUses();
		final int maxSize = item.getStackSize();
		
		if (uses.isSmeltable()) {
			if (smelt == null || smelt.isEmpty()) {
				smelt = item.clone();
				item.remove();
				return true;
			} else if (item.equals(smelt) && smelt.size < maxSize) {
				if (smelt.size+item.size > maxSize) {
					int num = maxSize - smelt.size;
					smelt.size += num;
					item.size -= num;
				} else {
					smelt.size += item.size;
					item.remove();
				}
				return true;
			}
		}
			
		if (uses.isFuel()) {
			if (fuel == null || fuel.isEmpty()) {
				fuel = item.clone();
				item.remove();
				return true;
			} else if (item.equals(fuel) && fuel.size < maxSize) {
				if (smelt.size+item.size > maxSize) {
					int num = maxSize - fuel.size;
					fuel.size += num;
					item.size -= num;
				} else {
					smelt.size += item.size;
					item.remove();
				}
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public Object getKey() {
		return pos;
	}
	
	public boolean isSmelting() {
		return burnTimer > 0;
	}
	
	public void update() {
		if (burnTimer > 0) burnTimer--;
		if (burnTimer <= 0 && isSmeltable()) {
			burnTimer = getBurnTime();
			maxBurnTimer = burnTimer;
			if (burnTimer != 0) {
				fuel.size--;
			}
		}
		
		if (isSmelting() && isSmeltable() && smelt != null && !smelt.isEmpty()) {
			process++;
			if (process >= MAX_PROCESS) {
				process = 0;
				if (result == null || result.isEmpty()) {
					result = getResult().clone().size(0);
				}
				result.size++;
				smelt.size--;
			}
		} else {
			process = 0;
		}
	}
	
	private int getBurnTime() {
		if (fuel == null || fuel.isEmpty()) {
			return 0;
		}
		return ((FurnaceRecipe)fuel).getBurnTime() * 50;
	}
	
	private Item getResult() {
		if (smelt == null || smelt.isEmpty()) {
			return null;
		}
		return ((FurnaceRecipe)smelt).getResult();
	}
	
	private boolean isSmeltable() {
		final Item item = getResult();
		
		if (item != null) {
			if (result == null || result.isEmpty() || (item.equals(result) && result.size < item.getStackSize())) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean canBeSmelt() {
		return smelt != null && !smelt.isEmpty() && isSmeltable();
	}
	
	@Override
	public void reset() {
		pos = null;
		fuel = null;
		result = null;
		smelt = null;
		burnTimer = 0;
		maxBurnTimer = 0;
		process = 0;
	}

	@Override
	public void save(Properties props) {
		props.put("pos", pos);
		
		if (fuel != null && !fuel.isEmpty()) {
			props.newProps("fuel").save(fuel);
		}
		
		if (smelt != null && !smelt.isEmpty()) {
			props.newProps("smelt").save(smelt);
		}
		
		if (result != null && !result.isEmpty()) {
			props.newProps("result").save(result);
		}
		
		props.put("burnTimer", burnTimer);
		props.put("maxBurnTimer", maxBurnTimer);
		props.put("process", process);
	}

	@Override
	public void load(Properties props) {
		pos = props.got("pos");
		Properties itemProps;
		
		itemProps = props.getProps("fuel");
		if (itemProps.notEmpty()) {
			fuel = itemProps.newObject();
			fuel.load(itemProps);
		}
		
		itemProps = props.getProps("smelt");
		if (itemProps.notEmpty()) {
			smelt = itemProps.newObject();
			smelt.load(itemProps);
		}
		
		itemProps = props.getProps("result");
		if (itemProps.notEmpty()) {
			result = itemProps.newObject();
			result.load(itemProps);
		}
		
		burnTimer = props.got("burnTimer");
		maxBurnTimer = props.got("maxBurnTimer");
		process = props.got("process");
	}
}
