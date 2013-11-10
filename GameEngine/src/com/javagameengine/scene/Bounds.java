package com.javagameengine.scene;

import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_LINE;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex3f;

import com.javagameengine.Graphics;
import com.javagameengine.math.Vector3f;

/**
 * @author ClairaLyrae
 * Bounds is an axis-aligned three dimensional box specified with a position vector and dimensions in all three axis.
 * Any dimension may be zero to specify an axis-aligned plane, line, or point. In addition, a special case of a void
 * bounds can be obtained from the static method getVoid. Void bounds represent Bounds objects that do not actually
 * count as bounds, and will not effect other bounds in most Bounds methods. For example, the intersect or contains 
 * methods will never return true if a void bounds is used in the calculation on either side. Whether or not a Bounds
 * object is currently void can be found by using the isVoid method.
 */
public class Bounds implements Graphics
{	
	public float dimX, dimY, dimZ; // Dimensions given between center and max (1/2 length)
	public float maxX, maxY, maxZ;
	public float minX, minY, minZ;
	private boolean isVoid = false;

	private Vector3f p;
	
	private Bounds()
	{
		this(0.0f, 0.0f, 0.0f, new Vector3f());
		isVoid = true;
	}

	public Bounds(Bounds b)
	{
		set(b);
	}

	public Bounds(float x, float y, float z, Vector3f p)
	{
		this.p = p;
		setDimensions(x, y, z);
	}
	
	public Bounds(float x, float y, float z)
	{
		this(x, y, z, new Vector3f());
	}

	public Bounds(Vector3f v)
	{
		this(0.0f, 0.0f, 0.0f, v);
	}

	public static Bounds getVoid()
	{
		return new Bounds();
	}

	/**
	 * Determines if the given Bounds object is contained within the volume of this Bounds object. If 
	 * either Bounds object is void, this will always return false.
	 * @param b Bounds object to check
	 * @return True if this Bounds object contains b
	 */
	public boolean contains(Bounds b)
	{
		if(b.isVoid() || isVoid)
			return false;
		return ((minX <= b.maxX && minX >= b.minX) && (maxX >= b.minX && maxX <= b.maxX))
				&& ((minY <= b.maxY && minY >= b.minY) && (maxY >= b.minY && maxY <= b.maxY))
				&& ((minZ <= b.maxZ && minZ >= b.minZ) && (maxZ >= b.minZ && maxZ <= b.maxZ));
	}

	public boolean isVoid()
	{
		return isVoid;
	}
	
	public void setVoid()
	{
		isVoid = true;
	}

	/**
	 * Extends this Bounds object to tightly encompass the given Bounds in addition to its original 
	 * bounding volume. If either of the two Bounds are void, this method will result in the non-void 
	 * Bounds. If both Bounds are void, this method will also return a void Bounds.
	 * @param b Bounds object to encompass
	 */
	public void encompass(Bounds b)
	{
		if(isVoid)
		{
			set(b);
			return;
		}
		if(b.isVoid)
			return;
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

	public float getDimX()
	{
		return dimX * 2.0f;
	}

	public float getDimY()
	{
		return dimY * 2.0f;
	}

	public float getDimZ()
	{
		return dimZ * 2.0f;
	}

	/**
	 * Determines if the given Bounds object is intersecting the volume of this Bounds object. If 
	 * either Bounds object is void, this will always return false.
	 * @param b Bounds object to check
	 * @return True if this Bounds object intersects b
	 */
	public boolean intersects(Bounds b)
	{
		if(b.isVoid() || isVoid)
			return false;
		return ((minX <= b.maxX && minX >= b.minX) || (maxX >= b.minX && maxX <= b.maxX))
				&& ((minY <= b.maxY && minY >= b.minY) || (maxY >= b.minY && maxY <= b.maxY))
				&& ((minZ <= b.maxZ && minZ >= b.minZ) || (maxZ >= b.minZ && maxZ <= b.maxZ));
	}

	public void scale(float s)
	{
		setDimensions(dimX *= s, dimY *= s, dimZ *= s);
	}

	public void scale(float x, float y, float z)
	{
		setDimensions(dimX *= x, dimY *= y, dimZ *= z);
	}
	
	public void translate(float x, float y, float z)
	{
		minX += x;
		maxX += x;
		minY += y;
		maxY += y;
		minZ += z;
		maxZ += z;
		p.add(x, y, z);
	}

	public void set(Bounds b)
	{
		if(b.isVoid())
		{
			isVoid = true;
			return;
		}
		isVoid = false;
		dimX = b.dimX;
		dimY = b.dimY;
		dimZ = b.dimZ;
		maxX = b.maxX;
		maxY = b.maxY;
		maxZ = b.maxZ;
		minX = b.minX;
		minY = b.minY;
		minZ = b.minZ;
		p = new Vector3f(b.p);
	}

	public void setPoint(float x, float y, float z)
	{
		isVoid = false;
		setDimensions(0,0,0);
		p.set(x, y, z);
		recalculate();
	}
	
	public void setPoint(Vector3f v)
	{
		isVoid = false;
		setDimensions(0,0,0);
		p.set(v);
		recalculate();
	}
	
	public boolean isPoint()
	{
		return (dimX == 0 && dimY == 0 && dimZ == 0);
	}
	
	public boolean isLine()
	{
		return (dimX == 0 && dimY == 0 && dimZ != 0) || 
			(dimX == 0 && dimY != 0 && dimZ == 0) || 
			(dimX != 0 && dimY == 0 && dimZ == 0) ;
	}
	
	public boolean isPlane()
	{
		return (dimX != 0 && dimY == 0 && dimZ != 0) || 
			(dimX == 0 && dimY != 0 && dimZ != 0) || 
			(dimX != 0 && dimY != 0 && dimZ == 0) ;
	}
	
	public void setDimensions(float x, float y, float z)
	{
		dimX = x;
		dimY = y;
		dimZ = z;
		recalculate();
	}
	
	private void recalculate()
	{
		maxX = p.x + dimX;
		maxY = p.y + dimY;
		maxZ = p.z + dimZ;
		minX = p.x - dimX;
		minY = p.y - dimY;
		minZ = p.z - dimZ;
	}

	@Override
	public void graphics()
	{
		glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		glTranslatef(0.0f, 0.0f, -5.0f); // Move Into The Screen
		glBegin(GL_QUADS); // Start Drawing 
		glVertex3f(minX, minY, minZ); 
		glVertex3f(minX, maxY, minZ); 
		glVertex3f(maxX, maxY, minZ); 
		glVertex3f(maxX, minY, minZ); 
		
		glVertex3f(minX, minY, minZ); 
		glVertex3f(minX, minY, maxZ); 
		glVertex3f(maxX, minY, maxZ); 
		glVertex3f(minX, minY, maxZ); 
		
		glVertex3f(minX, minY, minZ); 
		glVertex3f(minX, minY, maxZ); 
		glVertex3f(minX, maxY, maxZ); 
		glVertex3f(minX, maxY, minZ);
		
		glVertex3f(maxX, maxY, maxZ); 
		glVertex3f(maxX, minY, maxZ); 
		glVertex3f(minX, minY, maxZ); 
		glVertex3f(minX, maxY, maxZ); 
		
		glVertex3f(maxX, maxY, maxZ); 
		glVertex3f(maxX, maxY, minZ); 
		glVertex3f(minX, maxY, minZ); 
		glVertex3f(maxX, maxY, minZ);
		
		glVertex3f(maxX, maxY, maxZ); 
		glVertex3f(maxX, maxY, minZ); 
		glVertex3f(maxX, minY, minZ); 
		glVertex3f(maxX, minY, maxZ); 
		glEnd();
	}
}
