package com.javagameengine.math;

import java.nio.FloatBuffer;

/**
 * 4 dimensional vector class implemented using floats.
 * @author ClairaLyrae
 */
public class Vector4f extends Vector<Vector4f>
{
	public static final Vector4f zero = new Vector4f(0, 0, 0, 0);
	public static final Vector4f one = new Vector4f(1, 1, 1, 1);
	public static final Vector4f unit_w = new Vector4f(1, 0, 0, 0);
	public static final Vector4f unit_x = new Vector4f(0, 1, 0, 0);
	public static final Vector4f unit_y = new Vector4f(0, 0, 1, 0);
	public static final Vector4f unit_z = new Vector4f(0, 0, 0, 1);

	public float w, x, y, z;

	public Vector4f()
	{
		set(0f, 0f, 0f, 0f);
	}

	public Vector4f(float f)
	{
		set(f, f, f, f);
	}

	public Vector4f(float w, float x, float y, float z)
	{
		this.w = w;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector4f(float[] a) throws IndexOutOfBoundsException
	{
		if (a.length != 4)
			throw new IndexOutOfBoundsException();
		else
			set(a[0], a[1], a[2], a[3]);
	}

	public Vector4f(Vector4f v)
	{
		this(v.w, v.x, v.y, v.z);
	}

	public Vector4f add(float aw, float ax, float ay, float az)
	{
		w += aw;
		x += ax;
		y += ay;
		z += az;
		return this;
	}

	public Vector4f add(Vector4f v)
	{
		return addInto(v, this);
	}

	public Vector4f addInto(Vector4f v, Vector4f r)
	{
		if (r == null)
			r = new Vector4f();
		r.w = w + v.w;
		r.x = x + v.x;
		r.y = y + v.y;
		r.z = z + v.z;
		return r;
	}

	public Vector4f conjugate()
	{
		return conjugateInto(this);
	}

	public Vector4f conjugateInto(Vector4f r)
	{
		if (r == null)
			r = new Vector4f();
		r.x = -x;
		r.y = -y;
		r.z = -z;
		return r;
	}

	public float dot(Vector4f v)
	{
		return w * v.w + x * v.x + y * v.y + z * v.z;
	}

	public Vector4f lerp(Vector4f v, float s)
	{
		return lerpInto(v, s, this);
	}

	public Vector4f lerpInto(Vector4f v, float lerp, Vector4f r)
	{
		if (r == null)
			r = new Vector4f();
		r.w = ((w - v.w) * lerp) + w;
		r.x = ((x - v.x) * lerp) + x;
		r.y = ((y - v.y) * lerp) + y;
		r.z = ((z - v.z) * lerp) + z;
		return r;
	}

	public float magnitude()
	{
		return (float) MathUtil.fastSqrt(w * w + x * x + y * y + z * z);
	}

	public float magnitudeSquared()
	{
		return (w * w + x * x + y * y + z * z);
	}

	public Vector4f multiply(Matrix4f m)
	{
		return multiplyInto(m, this);
	}

	public Vector4f multiplyInto(Matrix4f m, Vector4f r)
	{
		if (r == null)
			r = new Vector4f();
		float wt = w;
		float xt = x;
		float yt = y;
		float zt = z;
		r.w = xt * m.f00 + yt * m.f10 + zt * m.f20 + wt * m.f30;
		r.x = xt * m.f01 + yt * m.f11 + zt * m.f21 + wt * m.f31;
		r.y = xt * m.f02 + yt * m.f12 + zt * m.f22 + wt * m.f32;
		r.z = xt * m.f03 + yt * m.f13 + zt * m.f23 + wt * m.f33;
		return r;
	}

	public Vector4f normalize()
	{
		return normalizeInto(this);
	}

	public Vector4f normalizeInto(Vector4f r)
	{
		if (r == null)
			r = new Vector4f();
		float mag = 1.0f / magnitude();
		r.w = mag * w;
		r.x = mag * x;
		r.y = mag * y;
		r.z = mag * z;
		return r;
	}

	public Vector4f scale(float s)
	{
		return scaleInto(s, this);
	}

	public Vector4f scale(float aw, float ax, float ay, float az)
	{
		w *= aw;
		x *= ax;
		y *= ay;
		z *= az;
		return this;
	}

	public Vector4f scale(Vector4f v)
	{
		return scaleInto(v, this);
	}

	public Vector4f scaleInto(float s, Vector4f r)
	{
		if (r == null)
			r = new Vector4f();
		r.w = w * s;
		r.x = x * s;
		r.y = y * s;
		r.z = z * s;
		return r;
	}

	public Vector4f scaleInto(Vector4f v, Vector4f r)
	{
		if (r == null)
			r = new Vector4f();
		r.w = w * v.w;
		r.x = x * v.x;
		r.y = y * v.y;
		r.z = z * v.z;
		return r;
	}

	public Vector4f set(float f)
	{
		return set(f, f, f, f);
	}

	public Vector4f set(float vw, float vx, float vy, float vz)
	{
		w = vw;
		x = vx;
		y = vy;
		z = vz;
		return this;
	}

	public Vector4f set(Vector4f v)
	{
		return set(v.w, v.x, v.y, v.z);
	}

	public Vector4f subtract(float aw, float ax, float ay, float az)
	{
		w -= aw;
		x -= ax;
		y -= ay;
		z -= az;
		return this;
	}

	public Vector4f subtract(Vector4f v)
	{
		return subtractInto(v, this);
	}

	public Vector4f subtractInto(Vector4f v, Vector4f r)
	{
		if (r == null)
			r = new Vector4f();
		r.w = w - v.w;
		r.x = x - v.x;
		r.y = y - v.y;
		r.z = z - v.z;
		return r;
	}

	@Override
	public float[] toArray(float[] a)
	{
		if (a == null || a.length != 4)
			a = new float[4];
		a[0] = w;
		a[1] = x;
		a[2] = y;
		a[3] = z;
		return a;
	}

	@Override
	public Vector4f loadFromBuffer(FloatBuffer fb)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector4f loadFromArray(float[] f)
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
		return String.format("[%f,%f,%f,%f]", w, x, y, z);
	}
}
