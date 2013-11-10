package com.javagameengine.math.geometry;

import com.javagameengine.math.Vector3f;

// Unused at the moment! Replaced by Bounds

public class Sphere extends Primitive<Sphere>
{
	protected float radius;

	public Sphere()
	{
		this(1.0f);
	}

	public Sphere(float r)
	{
		this(r, Vector3f.zero);
	}

	public Sphere(float r, Vector3f v)
	{
		super(v);
		setRadius(r);
	}

	public Box boundingBox()
	{
		float size = radius * 2;
		return new Box(size, size, size, p);
	}

	public Sphere boundingSphere()
	{
		return this;
	}

	public boolean contains(Sphere s)
	{
		float dist = p.subtract(s.p).magnitude();
		float reach = (dist + s.radius);
		return reach <= radius;
	}

	public void extend(Sphere s)
	{
		float dist = p.subtract(s.p).magnitude() + s.radius;
		if (dist > radius)
			radius = dist;
	}

	public float getRadius()
	{
		return radius;
	}

	public boolean intersects(Sphere s)
	{
		float distsq = p.subtract(s.p).magnitudeSquared();
		float reach = (radius + s.radius);
		return (reach * reach) >= distsq;
	}

	public void scale(float s)
	{
		radius *= s;
	}

	public void setRadius(float radius)
	{
		this.radius = radius;
	}
}
