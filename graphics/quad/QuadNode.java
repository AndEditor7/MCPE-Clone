package com.andedit.arcubit.graphics.quad;

import com.andedit.arcubit.utils.Facing;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class QuadNode {
	
	public static final float 
	lightHigh = 1.0f,
	lightMed = 0.8f,
	lightLow = 0.65f,
	lightDim = 0.52f;
	
	public Facing face;
	
	public boolean ambLight = true;
	public boolean simpleLight;
	
	boolean useWhat;
	
	/** Positions */
	public final Vector3
	p1 = new Vector3(),
	p2 = new Vector3(),
	p3 = new Vector3(),
	p4 = new Vector3();
	
	public QuadNode noLit() {
		ambLight = false;
		simpleLight = true;
		return this;
	}
	
	public QuadNode setPos(QuadNode node) {
		p1.set(node.p1);
		p2.set(node.p2);
		p3.set(node.p3);
		p4.set(node.p4);
		return this;
	}
	
	public QuadNode set(QuadNode node) {
		face = node.face;
		ambLight = node.ambLight;
		simpleLight = node.simpleLight;
		useWhat = false;
		return setPos(node);
	}
	
	public QuadNode scl(float x, float y, float z) {
		p1.scl(x,y,z);
		p2.scl(x,y,z);
		p3.scl(x,y,z);
		p4.scl(x,y,z);
		return this;
	}

	public QuadNode add(float x, float y, float z) {
		p1.add(x,y,z);
		p2.add(x,y,z);
		p3.add(x,y,z);
		p4.add(x,y,z);
		return this;
	}
	
	public float getAmbLit() {
		switch (face) {
		case NORTH: case SOUTH: return lightMed;
		case EAST: case WEST: return lightLow;
		case DOWN: return lightDim;
		default: return lightHigh;
		}
	}
	
	public QuadNode mul(Matrix4 matrix) {
		add(-0.5f, -0.5f, -0.5f);
		p1.mul(matrix);
		p2.mul(matrix);
		p3.mul(matrix);
		p4.mul(matrix);
		return add(0.5f, 0.5f, 0.5f);
	}
	
	public QuadNode clone() {
		return new QuadNode().set(this);
	}
}
