package com.javagameengine.scene;

import com.javagameengine.math.Vector3f;

/**
 * @author ClairaLyrae
 * Bounds is an axis-aligned three dimensional box specified with a position vector and dimensions in all three axis.
 * Any dimension may be zero to specify an axis-aligned plane, line, or point.
 */
public class Bounds
{
	public float dimX, dimY, dimZ; // Dimensions given between center and max (1/2 length)
	public float maxX, maxY, maxZ;
	public float minX, minY, minZ;

	private Vector3f p;
	
	public Bounds()
	{
		this(0.0f, 0.0f, 0.0f, Vector3f.zero);
	}

	public Bounds(Bounds b)
	{
		this.p = b.p;
		set(b);
	}

	public Bounds(float x, float y, float z, Vector3f p)
	{
		this.p = p;
		set(x, y, z);
	}
	
	public Bounds(float x, float y, float z)
	{
		this(x, y, z, new Vector3f());
	}

	public Bounds(Vector3f v)
	{
		this(0.0f, 0.0f, 0.0f, v);
	}

	public Bounds boundingBox()
	{
		return this;
	}

	public boolean contains(Bounds b)
	{
		return ((minX <= b.maxX && minX >= b.minX) && (maxX >= b.minX && maxX <= b.maxX))
				&& ((minY <= b.maxY && minY >= b.minY) && (maxY >= b.minY && maxY <= b.maxY))
				&& ((minZ <= b.maxZ && minZ >= b.minZ) && (maxZ >= b.minZ && maxZ <= b.maxZ));
	}

	public void extend(Bounds b)
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

	public boolean intersects(Bounds b)
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

	public void set(Bounds b)
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
