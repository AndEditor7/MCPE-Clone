package com.andedit.arcubit.handles;

import static com.andedit.arcubit.world.World.world;

import com.andedit.arcubit.blocks.Block;
import com.andedit.arcubit.blocks.LiquidBlock;
import com.andedit.arcubit.utils.BlockPos;
import com.andedit.arcubit.utils.Facing;
import com.andedit.arcubit.utils.Facing.Axis;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.utils.Array;

public final class VoxelRay {
	
	private static final Ray RAY = new Ray();
	private static final Vector3 INTERSECT = new Vector3();
	private static final BoundingBox BOX = new BoundingBox();
	private static final BlockPos INBLOCK = new BlockPos();
	private static final Vector3 VEC = new Vector3();
	
	private static final Ray TEMP = new Ray();
	public static boolean shot(Vector3 start, Vector3 end, RayContext context) {
		return shot(TEMP.set(start, VEC.set(end).sub(start)), start.dst(end), context);
	}

	public static boolean shot(final Ray ray, float radius, RayContext context) {
		context.blockHit = null;
		
		float dx, dy, dz;
		dx = ray.direction.x;
		dy = ray.direction.y;
		dz = ray.direction.z;
		
		if (Float.isNaN(dx) || Float.isNaN(dy) || Float.isNaN(dz)) {
			return false;
		}
		
		final Vector3 pos = ray.origin;

		int x, y, z;
		x = MathUtils.floor(pos.x);
		y = MathUtils.floor(pos.y);
		z = MathUtils.floor(pos.z);
		
		final int xPos, yPos, zPos;
		xPos = x;
		yPos = y;
		zPos = z;

		final int
		xStep = sign(dx),
		yStep = sign(dy),
		zStep = sign(dz);
		
		float tDeltaX = xStep == 0 ? Float.MAX_VALUE : (float) xStep / dx;
		float tDeltaY = yStep == 0 ? Float.MAX_VALUE : (float) yStep / dy;
		float tDeltaZ = zStep == 0 ? Float.MAX_VALUE : (float) zStep / dz;
		
		VEC.set(fract(pos.x), fract(pos.y), fract(pos.z));
		float tMaxX = tDeltaX * (xStep > 0 ? 1.0f - VEC.x : VEC.x);
		float tMaxY = tDeltaY * (yStep > 0 ? 1.0f - VEC.y : VEC.y);
		float tMaxZ = tDeltaZ * (zStep > 0 ? 1.0f - VEC.z : VEC.z);

		radius /= Math.sqrt(dx * dx + dy * dy + dz * dz);
		
		RAY.origin.set(VEC);
		RAY.direction.set(ray.direction);

		while (true) {

			final Block block = world.getBlock(INBLOCK.set(x, y, z));
			boolean isUpper = false;

			if (!block.isAir()) {
				final Array<BoundingBox> boxes = block.getBoundingBoxes(INBLOCK);
				Facing face = null;
				for (BoundingBox staticBox : boxes) {
					float closest = 10f;
					BOX.set(staticBox);
					BOX.max.add(x - xPos, y - yPos, z - zPos);
					BOX.min.add(x - xPos, y - yPos, z - zPos);
					if (Intersector.intersectRayBounds(RAY, BOX, INTERSECT)) {
						
						if (BOX.max.y - INTERSECT.y < closest) {
							closest = BOX.max.y - INTERSECT.y;
							face = Facing.UP;
						}
						
						if (-(BOX.min.y - INTERSECT.y) < closest) {
							closest = -(BOX.min.y - INTERSECT.y);
							face = Facing.DOWN;
						}
						
						if (BOX.max.x - INTERSECT.x < closest) {
							closest = BOX.max.x - INTERSECT.x;
							face = Facing.EAST;
						}
						
						if (-(BOX.min.x - INTERSECT.x) < closest) {
							closest = -(BOX.min.x - INTERSECT.x);
							face = Facing.WEST;
						}
						
						if (-(BOX.min.z - INTERSECT.z) < closest) {
							closest = -(BOX.min.z - INTERSECT.z);
							face = Facing.NORTH;
						}
						
						if (BOX.max.z - INTERSECT.z < closest) {
							closest = BOX.max.z - INTERSECT.z;
							face = Facing.SOUTH;
						}
						
						isUpper = (INTERSECT.y - MathUtils.floor(BOX.min.y)) > 0.49f;
					}
				}
				
				if (face == null) {
					if (block.getMaterial().isFullCube()) {
						return false;
					}
				} else {
					if (block instanceof LiquidBlock) {
						if (context.liquidHit == null) {
							LiquidBlock liquid = (LiquidBlock)block;
							if (liquid.isFull(INBLOCK)) {
								context.inl.set(INBLOCK);
								context.liquidHit = liquid;
							}
						}
					} else {
						Facing face2 = Facing.UP;
						if (Math.abs(dx) > Math.abs(dz)) {
							if (xStep == 1) {
								face2 = Facing.WEST;
							} else {
								face2 = Facing.EAST;
							}
						} else {
							if (zStep == 1) {
								face2 = Facing.NORTH;
							} else {
								face2 = Facing.SOUTH;
							}
						}
						
						if (face.axis == Axis.Y) isUpper = !isUpper;
						context.isUpper = isUpper;
						context.face2 = face2;
						context.face1 = face;
						context.in.set(INBLOCK);
						context.out.set(INBLOCK).add(face.offset);
						context.blockHit = block;
						context.blockDst = VEC.dst(0.5f + x - xPos, 0.5f + y - yPos, 0.5f + z - zPos);
						return true;
					}
				}
			}

			// Traversal stepping.
			if (tMaxX < tMaxY) {
				if (tMaxX < tMaxZ) {
					if (tMaxX > radius) break;
					x += xStep;
					tMaxX += tDeltaX;
				} else {
					if (tMaxZ > radius) break;
					z += zStep;
					tMaxZ += tDeltaZ;
				}
			} else {
				if (tMaxY < tMaxZ) {
					if (tMaxY > radius) break;
					y += yStep;
					tMaxY += tDeltaY;
				} else {
					if (tMaxZ > radius) break;
					z += zStep;
					tMaxZ += tDeltaZ;
				}
			}
		}

		return false;
	}

	private static int sign(float a) {
		if (a == 0.0f) {
			return 0;
		} else {
			return a > 0.0f ? 1 : -1;
		}
	}

	private static float fract(float a) {
		return a - (float) MathUtils.floor(a);
	}
}
