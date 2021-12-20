package com.andedit.arcubit.blocks;

import static com.andedit.arcubit.TheGame.game;
import static com.andedit.arcubit.world.World.world;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.Steve;
import com.andedit.arcubit.blocks.data.HoriComp;
import com.andedit.arcubit.blocks.materials.Material;
import com.andedit.arcubit.blocks.models.ChestModel;
import com.andedit.arcubit.blocks.utils.CubeTex;
import com.andedit.arcubit.blocks.utils.BlockType;
import com.andedit.arcubit.entity.ItemEnitiy;
import com.andedit.arcubit.file.Properties;
import com.andedit.arcubit.file.Serial;
import com.andedit.arcubit.handles.RayContext;
import com.andedit.arcubit.items.Item;
import com.andedit.arcubit.items.tools.ToolType;
import com.andedit.arcubit.ui.ChestUI;
import com.andedit.arcubit.utils.BlockPos;
import com.andedit.arcubit.utils.PlaceType;

public class ChestBlock extends Block {

	public final HoriComp component;

	ChestBlock() {
		this.manager.addCompoment(component = new HoriComp());
		this.model = new ChestModel(this, new CubeTex(Assets.getBlockReg(9, 1), Assets.getBlockReg(10, 1)));
		this.material = Material.BLOCK;
		this.setTexture(Assets.getItemReg(11, 4));
		this.setMining(ToolType.AXE, 0, 2.5f);
		this.blockType = BlockType.WOOD;
	}
	
	@Override
	public boolean onClick(BlockPos pos, Steve steve) {
		ChestContent content = world.chests.get(pos);
		if (content == null) {
			content = new ChestContent(pos);
			world.chests.put(content.pos, content);
		}
		game.manager.setUI(ChestUI.class).setContent(content);
		return true;
	}
	
	@Override
	public boolean onPlace(RayContext context, int type) {
		if (onPlace(context.out, type, PlaceType.PLACE)) {
			component.setFace(context.out, context.face2);
			return true;
		}
		return false;
	}
	
	@Override
	public void onDestory(BlockPos pos) {
		final ChestContent content = world.chests.remove(pos);
		if (content == null) return;
		
		for (Item item : content.items) {
			if (item != null && !item.isEmpty()) {
				ItemEnitiy enitiy = new ItemEnitiy(pos, item);
				enitiy.setVel();
				world.addEntity(enitiy);
			}
		}
	}

	public static class ChestContent implements Serial {
		public BlockPos pos;
		public final Item[] items = new Item[27];
		
		public ChestContent() {
			
		}
		
		public ChestContent(BlockPos pos) {
			this.pos = pos.clone();
		}
		
		public boolean addItem(final Item item) {
			if (item == null || item.isEmpty()) return false;
			
			final int maxSize = item.getStackSize();
			int failNum = 0;
			for (final Item slot : items) {
				if (item.equals(slot) && !slot.isEmpty() && slot.size < maxSize) {
					if (slot.size + item.size > maxSize) {
						int num = maxSize - slot.size;
						slot.size += num;
						item.size -= num;
						if (item.size <= 0) {
							return true;
						}
					} else {
						slot.size += item.size;
						return true;
					}
				} else {
					if (slot != null && slot.isEmpty()) {
						failNum++;
					}
				}
			}
			
			if (failNum == 27) {
				return false; // Inventory is full;
			}
			
			for (int i = 0; i < items.length; i++) {
				if (items[i] == null || items[i].isEmpty()) {
					items[i] = item.clone();
					return true;
				}
			}
			
			return false;
		}
		
		@Override
		public Object getKey() {
			return pos;
		}

		@Override
		public void save(Properties props) {
			props.put("pos", pos);
			
			for (byte i = 0; i < items.length; i++) {
				final Item item = items[i];
				if (item == null || item.isEmpty()) continue; 
				Properties itemProps = props.newProps();
				itemProps.putClass(item);
				itemProps.put("index", Byte.valueOf(i));
				item.save(itemProps);
			}
		}

		@Override
		public void load(Properties props) {
			pos = props.got("pos");
			
			for (Object obj : props.array()) {
				Properties itemProps = (Properties)obj;
				Item item = itemProps.newObject();
				item.load(itemProps);
				items[(byte)itemProps.get("index")] = item;
			}
		}
	}

	@Override
	public String getName(int type) {
		return "Chest";
	}
}
