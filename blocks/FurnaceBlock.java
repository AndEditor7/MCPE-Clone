package com.andedit.arcubit.blocks;

import static com.andedit.arcubit.TheGame.game;
import static com.andedit.arcubit.world.World.world;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.Steve;
import com.andedit.arcubit.blocks.data.FurnComp;
import com.andedit.arcubit.blocks.data.HoriComp;
import com.andedit.arcubit.blocks.furnace.FurnaceContent;
import com.andedit.arcubit.blocks.models.FurnaceModel;
import com.andedit.arcubit.entity.ItemEnitiy;
import com.andedit.arcubit.handles.RayContext;
import com.andedit.arcubit.items.tools.ToolType;
import com.andedit.arcubit.ui.FurnaceUI;
import com.andedit.arcubit.utils.BlockPos;
import com.andedit.arcubit.utils.PlaceType;

public class FurnaceBlock extends Block {
	
	public static final HoriComp horiComp = new HoriComp();
	public static final FurnComp furnComp = new FurnComp();

	FurnaceBlock() {
		this.manager.addCompoment(horiComp);
		this.manager.addCompoment(furnComp);
		this.setMining(ToolType.PICKAXE, 1, 3.5f);
		this.setTexture(Assets.getItemReg(12, 4));
		this.model = new FurnaceModel(this);
	}
	
	@Override
	public String getName(int type) {
		return "Furnace";
	}
	
	@Override
	public boolean onClick(BlockPos pos, Steve steve) {
		FurnaceContent content = world.furnaces.get(pos);
		if (content == null) {
			content = new FurnaceContent(pos);
			world.furnaces.put(content.pos, content);
		}
		game.manager.setUI(FurnaceUI.class).setContent(content);
		return true;
	}
	
	@Override
	public boolean onPlace(RayContext context, int type) {
		if (onPlace(context.out, type, PlaceType.PLACE)) {
			horiComp.setFace(context.out, context.face2);
			return true;
		}
		return false;
	}
	
	public void isActive(BlockPos pos, boolean isActive) {
		furnComp.setActive(pos, isActive);
	}
	
	public void setActive(BlockPos pos, boolean isActive) {
		furnComp.setActive(pos, isActive);
	}
	
	public void onDestory(BlockPos pos) {
		FurnaceContent content = world.furnaces.remove(pos);
		if (content == null) return;
		
		if (content.fuel != null && !content.fuel.isEmpty()) {
			ItemEnitiy enitiy = new ItemEnitiy(pos, content.fuel);
			enitiy.setVel();
			world.addEntity(enitiy);
		}
		
		if (content.smelt != null && !content.smelt.isEmpty()) {
			ItemEnitiy enitiy = new ItemEnitiy(pos, content.smelt);
			enitiy.setVel();
			world.addEntity(enitiy);
		}
		
		if (content.result != null && !content.result.isEmpty()) {
			ItemEnitiy enitiy = new ItemEnitiy(pos, content.result);
			enitiy.setVel();
			world.addEntity(enitiy);
		}
		
		content.reset();
	}
}
