package com.javagameengine.math.geometry;

import com.javagameengine.math.Vector3f;

//Unused at the moment! Replaced by Bounds

public class Box extends Primitive<Box>
{
	public float dimX, dimY, dimZ; // 1/2 length in derction
	public float maxX, maxY, maxZ;
	public float minX, minY, minZ;

	public Box()
	{
		this(0.0f, 0.0f, 0.0f, Vector3f.zero);
	}

	public Box(Box b)
	{
		super(b.p);
		set(b);
	}

	public Box(float x, float y, float z, Vector3f v)
	{
		super(v);
		set(x, y, z);
	}
	
	public Box(float x, float y, float z)
	{
		super(new Vector3f());
		set(x, y, z);
	}

	public Box(Vector3f v)
	{
		this(0.0f, 0.0f, 0.0f, v);
	}

	public Box boundingBox()
	{
		return this;
	}

	public Sphere boundingSphere()
	{
		return new Sphere(0.5f * (new Vector3f(dimX, dimY, dimZ).magnitude()),
				p);
	}

	public boolean contains(Box b)
	{
		return ((minX <= b.maxX && minX >= b.minX) && (maxX >= b.minX && maxX <= b.maxX))
				&& ((minY <= b.maxY && minY >= b.minY) && (maxY >= b.minY && maxY <= b.maxY))
				&& ((minZ <= b.maxZ && minZ >= b.minZ) && (maxZ >= b.minZ && maxZ <= b.maxZ));
	}

	public void extend(Box b)
	{
		if (b.minX < minX)
			minX = b.minX;
		if (b.minY < minY)
			minY = b.minY;
		if (b.minZ < minZ)
			minZ = b.minZ;
		if (b.maxX < maxX)
			maxX = b.maxX;
		if (b.maxY < maxY)
			maxY = b.maxY;
		if (b.maxZ < maxZ)
			maxZ = b.maxZ;
		p.set((maxX + minX) / 2.0f, (maxY + minY) / 2.0f, (maxZ + minZ) / 2.0f);
		dimX = maxX - p.x;
		dimY = maxY - p.y;
		dimZ = maxZ - p.z;
	}

	public float getLengthX()
	{
		return dimX * 2.0f;
	}

	public float getLengthY()
	{
		return dimY * 2.0f;
	}

	public float getLengthZ()
	{
		return dimZ * 2.0f;
	}

	public boolean intersects(Box b)
	{
		return ((minX <= b.maxX && minX >= b.minX) || (maxX >= b.minX && maxX <= b.maxX))
				&& ((minY <= b.maxY && minY >= b.minY) || (maxY >= b.minY && maxY <= b.maxY))
				&& ((minZ <= b.maxZ && minZ >= b.minZ) || (maxZ >= b.minZ && maxZ <= b.maxZ));
	}

	public void scale(float s)
	{
		set(dimX *= s, dimY *= s, dimZ *= s);
	}

	public void scale(float x, float y, float z)
	{
		set(dimX *= x, dimY *= y, dimZ *= z);

	}
	
	public void translate(float x, float y, float z)
	{
		minX += x;
		maxX += x;
		minY += y;
		maxY += y;
		minZ += z;
		maxZ += z;
	}

	public void set(Box b)
	{
		dimX = b.dimX;
		dimY = b.dimY;
		dimZ = b.dimZ;
		maxX = b.maxX;
		maxY = b.maxY;
		maxZ = b.maxZ;
		minX = b.minX;
		minY = b.minY;
		minZ = b.minZ;
	}

	public void set(float x, float y, float z)
	{
		dimX = x;
		dimY = y;
		dimZ = z;
		maxX = p.x + dimX;
		maxY = p.y + dimY;
		maxZ = p.z + dimZ;
		minX = p.x - dimX;
		minY = p.y - dimY;
		minZ = p.z - dimZ;
	}
}
