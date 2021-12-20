package com.andedit.arcubit.blocks;

import static com.andedit.arcubit.world.World.world;

import com.andedit.arcubit.blocks.data.HoriComp;
import com.andedit.arcubit.blocks.materials.Material;
import com.andedit.arcubit.blocks.models.LadderModel;
import com.andedit.arcubit.blocks.utils.BlockType;
import com.andedit.arcubit.blocks.utils.UpdateState;
import com.andedit.arcubit.handles.RayContext;
import com.andedit.arcubit.items.tools.ToolType;
import com.andedit.arcubit.utils.Facing.Axis;
import com.andedit.arcubit.utils.BlockPos;
import com.andedit.arcubit.utils.Facing;
import com.andedit.arcubit.utils.PlaceType;

public class LadderBlock extends Block {
	
	public static final HoriComp comp = new HoriComp();
	
	public LadderBlock() {
		setName("Ladder");
		manager.addCompoment(comp);
		blockType = BlockType.WOOD;
		model = new LadderModel(this);
		setMining(ToolType.AXE, 0, 0.4f);
		material = Material.LADDER;
	}
	
	@Override
	public boolean onPlace(RayContext context, int type) {
		if (context.face1.axis == Axis.Y) return false;
		if (context.blockHit.isFaceSolid(context.in, context.face1)) {
			if (onPlace(context.out, type, PlaceType.PLACE)) {
				comp.setFace(context.out, context.face1);
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public void onNeighbourUpdate(BlockPos primaray, BlockPos secondary, Facing face, UpdateState state) {
		face = comp.getFace(primaray);
		if (!primaray.offset(face.invert()).getBlock().isFaceSolid(secondary, face)) {
			world.setAir(primaray, PlaceType.SET, true);
		}
	}
}
