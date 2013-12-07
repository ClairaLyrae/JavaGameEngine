package com.javagameengine.math;

public class Color4f
{
	public static final Color4f white = new Color4f(1f, 1f, 1f, 1f);
	public static final Color4f black = new Color4f(0f, 0f, 0f, 1f);
	public static final Color4f gray = new Color4f(0.5f, 0.5f, 0.5f, 1f);
	public static final Color4f light_gray = new Color4f(0.75f, 0.75f, 0.75f, 1f);
	public static final Color4f dark_gray = new Color4f(0.25f, 0.25f, 0.25f, 1f);
	public static final Color4f green = new Color4f(0f, 1f, 0f, 1f);
	public static final Color4f red = new Color4f(1f, 0f, 0f, 1f);
	public static final Color4f blue = new Color4f(0f, 0f, 1f, 1f);
	public static final Color4f purple = new Color4f(1f, 0f, 1f, 1f);
	public static final Color4f cyan = new Color4f(0f, 1f, 1f, 1f);
	public static final Color4f yellow = new Color4f(1f, 1f, 0f, 1f);
	public static final Color4f transRed = new Color4f(1f, 0f, 0f, 0.5f);
	public static final Color4f transBlack = new Color4f(0f, 0f, 0f, 0.5f);
	
	public final float r, g, b, a;
	
	public Color4f(float r, float b, float g, float a)
	{
		this.r = r;
		this.b = b;
		this.g = g;
		this.a = a;
	}

	
	
	public static Vector4f toVector(Color4f c)
	{
		return new Vector4f(c.r, c.g, c.b, c.a);
	}
	
	public static Color4f fromVector(Vector4f v)
	{
		return new Color4f(v.w, v.x, v.y, v.z);
	}
	
	// return color at half transparency
	public Color4f setTrans()
	{
		Color4f transColor = new Color4f(this.r, this.g, this.b, 0.5f);;
		
		return transColor;
	}
	
	// return color at specified transparency
	public Color4f setTrans(float trans)
	{
		Color4f transColor = new Color4f(this.r, this.g, this.b, trans);;
		
		return transColor;
	}
}
