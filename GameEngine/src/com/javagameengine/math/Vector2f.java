package com.javagameengine.math;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

/**
 * 2 dimensional vector class implemented using floats.
 * @author ClairaLyrae
 */
public class Vector2f extends Vector<Vector2f>
{
	public static final Vector2f zero = new Vector2f(0, 0);
	public static final Vector2f one = new Vector2f(1, 1);
	public static final Vector2f unit_x = new Vector2f(1, 0);
	public static final Vector2f unit_y = new Vector2f(0, 1);
	public float x, y;

	public Vector2f()
	{
		this(0, 0);
	}

	public Vector2f(float f)
	{
		set(f, f);
	}

	public Vector2f(float x, float y)
	{
		this.x = x;
		this.y = y;
	}

	public Vector2f(float[] a) throws IndexOutOfBoundsException
	{
		if (a.length < 2)
			throw new IndexOutOfBoundsException();
		else
			set(a[0], a[1]);
	}

	public Vector2f add(float ax, float ay)
	{
		x += ax;
		y += ay;
		return this;
	}

	public Vector2f add(Vector2f v)
	{
		return addInto(v, this);
	}

	public Vector2f addInto(Vector2f v, Vector2f r)
	{
		if (r == null)
			r = new Vector2f();
		r.x = x + v.x;
		r.y = y + v.y;
		return r;
	}

	public Vector2f conjugate()
	{
		return conjugateInto(this);
	}

	public Vector2f conjugateInto(Vector2f r)
	{
		if (r == null)
			r = new Vector2f();
		r.y = -y;
		return r;
	}

	public float dot(Vector2f v)
	{
		return this.x * v.x + this.y * v.y;
	}

	public Vector2f lerp(Vector2f q, float lerp)
	{
		return lerpInto(q, lerp, this);
	}

	public Vector2f lerpInto(Vector2f q, float lerp, Vector2f r)
	{
		if (r == null)
			r = new Vector2f();
		r.x = ((this.x - q.x) * lerp) + x;
		r.y = ((this.y - q.y) * lerp) + y;
		return r;
	}

	public float magnitude()
	{
		return (float) FastMath.sqrt(magnitudeSquared());
	}

	public float magnitudeSquared()
	{
		return (x * x + y * y);
	}

	public Vector2f multiply(Matrix2f m)
	{
		return multiplyInto(m, this);
	}

	public Vector2f multiplyInto(Matrix2f m, Vector2f r)
	{
		if (r == null)
			r = new Vector2f();
		float xt = x;
		r.x = x * m.f00 + y * m.f10;
		r.y = xt * m.f01 + y * m.f11;
		return r;
	}

	public Vector2f normalize()
	{
		return normalizeInto(this);
	}

	public Vector2f normalizeInto(Vector2f r)
	{
		if (r == null)
			r = new Vector2f();
		float mag = 1.0f / magnitude();
		r.x = x * mag;
		r.y = y * mag;
		return r;
	}

	public Vector2f scale(float s)
	{
		return scaleInto(s, this);
	}

	public Vector2f scale(float ax, float ay)
	{
		x *= ax;
		y *= ay;
		return this;
	}

	public Vector2f scale(Vector2f v)
	{
		return scaleInto(v, this);
	}

	public Vector2f scaleInto(float s, Vector2f r)
	{
		if (r == null)
			r = new Vector2f();
		r.x = x * s;
		r.y = y * s;
		return r;
	}

	public Vector2f scaleInto(Vector2f v, Vector2f r)
	{
		if (r == null)
			r = new Vector2f();
		r.x = x * v.x;
		r.y = y * v.y;
		return r;
	}

	public Vector2f set(float f)
	{
		return set(f, f);
	}

	public Vector2f set(float vx, float vy)
	{
		x = vx;
		y = vy;
		return this;
	}

	@Override
	public Vector2f set(Vector2f m)
	{
		return set(m.x, m.y);
	}

	public Vector2f subtract(float ax, float ay)
	{
		x -= ax;
		y -= ay;
		return this;
	}

	public Vector2f subtract(Vector2f v)
	{
		return subtractInto(v, this);
	}

	public Vector2f subtractInto(Vector2f v, Vector2f r)
	{
		if (r == null)
			r = new Vector2f();
		r.x = x - v.x;
		r.y = y - v.y;
		return r;
	}

	@Override
	public float[] toArray(float[] a)
	{
		if (a == null || a.length != 2)
			a = new float[2];
		a[0] = x;
		a[1] = y;
		return a;
	}
	
	@Override
	public Vector2f loadFromBuffer(FloatBuffer fb)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector2f loadFromArray(float[] f)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FloatBuffer toBuffer(FloatBuffer fb)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public String toString()
	{
		return String.format("[%f,%f]", x, y);
	}

	public static FloatBuffer getBufferFromArray(Vector2f[] array)
	{
		FloatBuffer fb = BufferUtils.createFloatBuffer(array.length * 2);
		for(int i = 0; i < array.length; i++)
			fb.put(array[i].x).put(array[i].y);
		fb.flip();
		return fb;
	}
	
	public static Vector2f[] getArrayFromBuffer(FloatBuffer fb)
	{
		Vector2f[] array = new Vector2f[(int)(fb.limit()/2)];
		for(int i = 0; i < array.length; i++)
			array[i] = new Vector2f(fb.get(), fb.get());
		fb.rewind();
		return array;
	}
}
