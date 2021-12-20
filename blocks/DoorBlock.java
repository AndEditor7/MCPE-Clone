package com.andedit.arcubit.blocks;

import static com.andedit.arcubit.world.World.world;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.Sounds;
import com.andedit.arcubit.Steve;
import com.andedit.arcubit.blocks.data.DoorComp;
import com.andedit.arcubit.blocks.data.HoriComp;
import com.andedit.arcubit.blocks.materials.Material;
import com.andedit.arcubit.blocks.models.DoorModel;
import com.andedit.arcubit.blocks.utils.BlockType;
import com.andedit.arcubit.blocks.utils.UpdateState;
import com.andedit.arcubit.handles.RayContext;
import com.andedit.arcubit.items.tools.ToolType;
import com.andedit.arcubit.utils.BlockPos;
import com.andedit.arcubit.utils.Facing;
import com.andedit.arcubit.utils.PlaceType;
import com.andedit.arcubit.world.World;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class DoorBlock extends Block {
	
	public final DoorComp doorComponent = new DoorComp();
	public final HoriComp horizontalComponent = new HoriComp();

	DoorBlock() {
		this.material = Material.DOOR;
		this.model = new DoorModel(this);
		this.manager.addCompoment(doorComponent);
		this.manager.addCompoment(horizontalComponent);
		
		this.setMining(ToolType.AXE, 0, 2f);
		this.blockType = BlockType.WOOD;
		this.setTexture(new TextureRegion(Assets.GUI_ITEMS, 96, 433, 16, 16));
	}
	
	@Override
	public boolean onClick(BlockPos pos, Steve steve) {
		final BlockPos offset = pos.offset(isUpper(pos) ? Facing.DOWN : Facing.UP);
		if (world.getBlock(offset) == this) {
			doorComponent.setIsOpen(pos, !isOpen(pos));
			doorComponent.setIsOpen(offset, !isOpen(offset));
			world.dirty(pos.x, pos.y, pos.z);
			(isOpen(pos)?Sounds.OPEN:Sounds.CLOSE).play();
			return true;
		}
		
		return false;
	}
	
	@Override
	public boolean onPlace(RayContext context, int type) {
		final BlockPos out = context.out;
		if (!world.isAir(out)) return false;
		final BlockPos up = out.offset(Facing.UP);
		if (!world.isAir(up) || World.HEIGHT <= up.y) return false;
		
		onPlace(out, 0, PlaceType.PLACE);
		onPlace(up, 0, PlaceType.PLACE);
		
		doorComponent.setIsUpper(up, true);
		
		Facing face = context.face2;
		switch (face) {
		case EAST:
		case SOUTH:
		case WEST: break;
		default: face = Facing.NORTH; break;
		}
		
		horizontalComponent.setFace(out, face);
		horizontalComponent.setFace(up, face);
		
		return true;
	}
	
	@Override
	public void onNeighbourUpdate(BlockPos primaray, BlockPos secondary, Facing face, UpdateState state) {
		
		if (isUpper(primaray)) {
			BlockPos offset = primaray.offset(Facing.DOWN);
			if (world.getBlock(offset) != this) {
				world.setAir(primaray, PlaceType.PLACE, false);
			}
		} else {
			BlockPos offset = primaray.offset(Facing.UP);
			if (world.getBlock(offset) != this) {
				world.setAir(primaray, PlaceType.PLACE, false);
			}
		}
		
	}
	
	public boolean isUpper(BlockPos pos) {
		return doorComponent.isUpper(pos);
	}
	
	public boolean isOpen(BlockPos pos) {
		return doorComponent.isOpen(pos);
	}

	@Override
	public String getName(int type) {
		return "Door";
	}
}
