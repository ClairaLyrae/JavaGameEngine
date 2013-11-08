package com.simple3d.math.geometry;

import com.simple3d.math.Vector3f;

public abstract class Primitive<T extends Primitive<T>>
{
	protected Vector3f p;

	public Primitive(Vector3f v)
	{
		this.p = v;
	}

	public abstract Box boundingBox();

	public abstract Sphere boundingSphere();

	public abstract boolean contains(T g);

	public abstract void extend(T g);

	public Vector3f getPosition()
	{
		return p;
	}

	public abstract boolean intersects(T g);

	public abstract void scale(float s);

	public void setPosition(Vector3f v)
	{
		this.p = v;
	}
}
