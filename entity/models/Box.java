package com.andedit.arcubit.entity.models;

import com.andedit.arcubit.blocks.utils.CubeTex;
import com.andedit.arcubit.graphics.EntityBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;

public class Box {
	private static final Vector3 out = new Vector3();
	
	public final CubeTex texs;
	public final BoundingBox box;
	
	protected final Vector2[] uv = new Vector2[4];
	
	public Box(CubeTex texs, BoundingBox box) {
		this.texs = texs;
		this.box = box;
		
		uv[0] = new Vector2();
		uv[1] = new Vector2();
		uv[2] = new Vector2();
		uv[3] = new Vector2();
	}
	
	public void render(EntityBatch batch) {
		// up
		TextureRegion tex = texs.top;
		if (tex != null) {
			up(tex);
			batch.pos(box.getCorner111(out));
			batch.light();
			batch.tex(uv[0].x, uv[0].y);
			
			batch.pos(box.getCorner110(out));
			batch.light();
			batch.tex(uv[1].x, uv[1].y);
			
			batch.pos(box.getCorner010(out));
			batch.light();
			batch.tex(uv[2].x, uv[2].y);
			
			batch.pos(box.getCorner011(out));
			batch.light();
			batch.tex(uv[3].x, uv[3].y);
		}
		
		//down
		tex = texs.bottom;
		if (tex != null) {
			down(tex);
			batch.pos(box.getCorner001(out));
			batch.light();
			batch.tex(uv[0].x, uv[0].y);
			
			batch.pos(box.getCorner000(out));
			batch.light();
			batch.tex(uv[1].x, uv[1].y);
			
			batch.pos(box.getCorner100(out));
			batch.light();
			batch.tex(uv[2].x, uv[2].y);
			
			batch.pos(box.getCorner101(out));
			batch.light();
			batch.tex(uv[3].x, uv[3].y);
		}
		
		// north
		tex = texs.north;
		if (tex != null) {
			north(tex);
			batch.pos(box.getCorner000(out));
			batch.light();
			batch.tex(uv[0].x, uv[0].y);
			
			batch.pos(box.getCorner010(out));
			batch.light();
			batch.tex(uv[1].x, uv[1].y);
			
			batch.pos(box.getCorner110(out));
			batch.light();
			batch.tex(uv[2].x, uv[2].y);
			
			batch.pos(box.getCorner100(out));
			batch.light();
			batch.tex(uv[3].x, uv[3].y);
		}
		
		// east
		tex = texs.east;
		if (tex != null) {
			east(tex);
			batch.pos(box.getCorner100(out));
			batch.light();
			batch.tex(uv[0].x, uv[0].y);
			
			batch.pos(box.getCorner110(out));
			batch.light();
			batch.tex(uv[1].x, uv[1].y);
			
			batch.pos(box.getCorner111(out));
			batch.light();
			batch.tex(uv[2].x, uv[2].y);
			
			batch.pos(box.getCorner101(out));
			batch.light();
			batch.tex(uv[3].x, uv[3].y);
		}
		
		
		// south
		tex = texs.south;
		if (tex != null) {
			south(tex);
			batch.pos(box.getCorner101(out));
			batch.light();
			batch.tex(uv[0].x, uv[0].y);
			
			batch.pos(box.getCorner111(out));
			batch.light();
			batch.tex(uv[1].x, uv[1].y);
			
			batch.pos(box.getCorner011(out));
			batch.light();
			batch.tex(uv[2].x, uv[2].y);
			
			batch.pos(box.getCorner001(out));
			batch.light();
			batch.tex(uv[3].x, uv[3].y);
		}
		
		
		// west
		tex = texs.west;
		if (tex != null) {
			west(tex);
			batch.pos(box.getCorner001(out));
			batch.light();
			batch.tex(uv[0].x, uv[0].y);
			
			batch.pos(box.getCorner011(out));
			batch.light();
			batch.tex(uv[1].x, uv[1].y);
			
			batch.pos(box.getCorner010(out));
			batch.light();
			batch.tex(uv[2].x, uv[2].y);
			
			batch.pos(box.getCorner000(out));
			batch.light();
			batch.tex(uv[3].x, uv[3].y);
		}
	}
	
	protected void leftUV(TextureRegion tex) {
		uv[0].set(tex.getU(), tex.getV2()); // 2  2
		uv[1].set(tex.getU2(), tex.getV2());  // 2  1
		uv[2].set(tex.getU2(), tex.getV());   // 1  1
		uv[3].set(tex.getU(), tex.getV());  // 1  2
	}
	
	protected void defaultUV(TextureRegion tex) {
		uv[0].set(tex.getU2(), tex.getV2()); // 2  2
		uv[1].set(tex.getU2(), tex.getV());  // 2  1
		uv[2].set(tex.getU(), tex.getV());   // 1  1
		uv[3].set(tex.getU(), tex.getV2());  // 1  2
	}
	
	protected void rightUV(TextureRegion tex) {
		uv[0].set(tex.getU2(), tex.getV()); // 2  2
		uv[1].set(tex.getU(), tex.getV());  // 2  1
		uv[2].set(tex.getU(), tex.getV2());   // 1  1
		uv[3].set(tex.getU2(), tex.getV2());  // 1  2
	}
	
	protected void up(TextureRegion tex) {
		defaultUV(tex);
	}
	
	protected void down(TextureRegion tex) {
		defaultUV(tex);
	}
	
	protected void north(TextureRegion tex) {
		defaultUV(tex);
	}
	
	protected void east(TextureRegion tex) {
		defaultUV(tex);
	}
	
	protected void south(TextureRegion tex) {
		defaultUV(tex);
	}
	
	protected void west(TextureRegion tex) {
		defaultUV(tex);
	}
}
