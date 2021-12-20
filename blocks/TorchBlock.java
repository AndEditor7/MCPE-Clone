package com.andedit.arcubit.blocks;

import static com.andedit.arcubit.world.World.world;

import com.andedit.arcubit.blocks.data.FaceComp;
import com.andedit.arcubit.blocks.materials.Material;
import com.andedit.arcubit.blocks.models.TorchModel;
import com.andedit.arcubit.blocks.utils.BlockType;
import com.andedit.arcubit.blocks.utils.UpdateState;
import com.andedit.arcubit.handles.RayContext;
import com.andedit.arcubit.items.tools.ToolType;
import com.andedit.arcubit.utils.BlockPos;
import com.andedit.arcubit.utils.Facing;
import com.andedit.arcubit.utils.PlaceType;

public class TorchBlock extends Block {
	
	public static final FaceComp comp = new FaceComp();

	TorchBlock() {
		this.material = Material.TORCH;
		this.model = new TorchModel(this);
		this.manager.addCompoment(comp);
		this.lightLevel = 14;
		this.setMining(ToolType.NONE, 0, 0);
		this.blockType = BlockType.WOOD;
	}
	
	@Override
	public String getName(int type) {
		return "Torch";
	}
	
	@Override
	public boolean onPlace(RayContext context, int type) {
		if (context.face1 == Facing.DOWN) return false; 
		
		if (context.blockHit.isFaceSolid(context.in, context.face1)) {
			if (onPlace(context.out, 0, PlaceType.PLACE)) {
				comp.setFace(context.out, context.face1);
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public void onNeighbourUpdate(BlockPos primaray, BlockPos secondary, Facing face, UpdateState state) {
		face = comp.getFace(primaray);
		secondary = primaray.offset(face.invert());
		if (!world.getBlock(secondary).isFaceSolid(secondary, face)) {
			world.setAir(primaray, PlaceType.PLACE, true);
		}
	}
}
