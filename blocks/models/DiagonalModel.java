package com.andedit.arcubit.blocks.models;

import com.andedit.arcubit.Assets;
import com.andedit.arcubit.blocks.Block;
import com.andedit.arcubit.graphics.quad.QuadBuilder;
import com.andedit.arcubit.graphics.quad.QuadNode;
import com.andedit.arcubit.utils.ArrayDir;
import com.andedit.arcubit.utils.BlockPos;
import com.andedit.arcubit.utils.Facing;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;

public class DiagonalModel extends BlockModel {

	private final ArrayDir<TextureRegion> textures;
	private static final QuadNode quad1, quad2, quad3, quad4;
	
	static {
		quad1 = new QuadNode();
		quad1.p1.set(0, 0, 0);
		quad1.p2.set(0, 1, 0);
		quad1.p3.set(1, 1, 1);
		quad1.p4.set(1, 0, 1);
		quad1.face = Facing.UP;
		quad1.simpleLight = true;
		quad1.ambLight = false;
		
		quad2 = new QuadNode();
		quad2.p1.set(1, 0, 0);
		quad2.p2.set(1, 1, 0);
		quad2.p3.set(0, 1, 1);
		quad2.p4.set(0, 0, 1);
		quad2.face = Facing.UP;
		quad2.simpleLight = true;
		quad2.ambLight = false;

		quad3 = new QuadNode();
		quad3.p1.set(0, 0, 1);
		quad3.p2.set(0, 1, 1);
		quad3.p3.set(1, 1, 0);
		quad3.p4.set(1, 0, 0);
		quad3.face = Facing.UP;
		quad3.simpleLight = true;
		quad3.ambLight = false;

		quad4 = new QuadNode();
		quad4.p1.set(1, 0, 1);
		quad4.p2.set(1, 1, 1);
		quad4.p3.set(0, 1, 0);
		quad4.p4.set(0, 0, 0);
		quad4.face = Facing.UP;
		quad4.simpleLight = true;
		quad4.ambLight = false;
	}
	
	public DiagonalModel(TextureRegion texture) {
		this(new ArrayDir<TextureRegion>(TextureRegion.class, 1).put(0, texture));
	}

	public DiagonalModel(ArrayDir<TextureRegion> textures) {
		this.textures = textures;
		quads.add(quad1, quad2, quad3, quad4);
	}
	
	public DiagonalModel(Block block, TextureRegion texture) {
		this(block, new ArrayDir<TextureRegion>(TextureRegion.class, 1).put(0, texture));
	}

	public DiagonalModel(Block block, ArrayDir<TextureRegion> textures) {
		super(block);
		this.textures = textures;
		quads.add(quad1, quad2, quad3, quad4);
	}
	
	@Override
	public boolean use3D() {
		return false;
	}
	
	@Override
	public void build(QuadBuilder builder, BlockPos pos) {
		builder.region = getDefaultTex(pos);
		builder.set(quad1).flush(pos);
		builder.set(quad2).flush(pos);
		builder.set(quad3).flush(pos);
		builder.set(quad4).flush(pos);
	}

	@Override
	public TextureRegion getDefaultTex(int data) {
		return data >= block.getNumsType() ? Assets.MISSING : textures.get(data);
	}
	
	@Override
	public Array<BoundingBox> getBoundingBoxes(BlockPos pos) {
		return CubeModel.CUBE_BOX;
	}
}
