package com.javagameengine.math;

import java.nio.FloatBuffer;

/**
 * 3 dimensional vector class implemented using floats.
 * @author ClairaLyrae
 */
public class Vector3f extends Vector<Vector3f>
{
	public static final Vector3f zero = new Vector3f(0, 0, 0);
	public static final Vector3f one = new Vector3f(1, 1, 1);
	public static final Vector3f unit_x = new Vector3f(1, 0, 0);
	public static final Vector3f unit_y = new Vector3f(0, 1, 0);
	public static final Vector3f unit_z = new Vector3f(0, 0, 1);

	public float x, y, z;

	public Vector3f()
	{
		set(0f, 0f, 0f);
	}

	public Vector3f(float f)
	{
		set(f, f, f);
	}

	public Vector3f(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3f(float[] a) throws IndexOutOfBoundsException
	{
		if (a.length < 3)
			throw new IndexOutOfBoundsException();
		else
			set(a[0], a[1], a[2]);
	}

	public Vector3f(Vector3f v)
	{
		this(v.x, v.y, v.z);
	}

	public Vector3f add(float ax, float ay, float az)
	{
		x = x + ax;
		y = y + ay;
		z = z + az;
		return this;
	}

	public Vector3f add(Vector3f v)
	{
		return addInto(v, this);
	}

	public Vector3f addInto(Vector3f v, Vector3f r)
	{
		if (r == null)
			r = new Vector3f();
		r.x = x + v.x;
		r.y = y + v.y;
		r.z = z + v.z;
		return r;
	}

	public Vector3f conjugate()
	{
		return conjugateInto(this);
	}

	public Vector3f conjugateInto(Vector3f r)
	{
		if (r == null)
			r = new Vector3f();
		r.y = -y;
		r.z = -z;
		return r;
	}

	public float dot(Vector3f v)
	{
		return this.x * v.x + this.y * v.y + this.z * v.z;
	}

	public float getX()
	{
		return x;
	}

	public float getY()
	{
		return x;
	}

	public float getZ()
	{
		return x;
	}

	public Vector3f lerp(Vector3f v, float s)
	{
		return lerpInto(v, s, this);
	}

	public Vector3f lerpInto(Vector3f v, float lerp, Vector3f r)
	{
		if (r == null)
			r = new Vector3f();
		r.x = ((x - v.x) * lerp) + x;
		r.y = ((y - v.y) * lerp) + y;
		r.z = ((z - v.z) * lerp) + z;
		return r;
	}

	public float magnitude()
	{
		return (float) FastMath.sqrt(x * x + y * y + z * z);
	}

	public float magnitudeSquared()
	{
		return (x * x + y * y + z * z);
	}

	public Vector3f multiply(Matrix3f m)
	{
		return multiplyInto(m, this);
	}

	public Vector3f multiplyInto(Matrix3f m, Vector3f r)
	{
		if (r == null)
			r = new Vector3f();
		float xt = x;
		float yt = y;
		float zt = z;
		r.x = xt * m.f00 + yt * m.f10 + zt * m.f20;
		r.y = xt * m.f01 + yt * m.f11 + zt * m.f21;
		r.z = xt * m.f02 + yt * m.f12 + zt * m.f22;
		return r;
	}

	public Vector3f normalize()
	{
		return normalizeInto(this);
	}

	public Vector3f normalizeInto(Vector3f r)
	{
		if (r == null)
			r = new Vector3f();
		float mag = 1.0f / magnitude();
		r.x = mag * x;
		r.y = mag * y;
		r.z = mag * z;
		return r;
	}

	public Vector3f scale(float s)
	{
		return scaleInto(s, this);
	}

	public Vector3f scale(float ax, float ay, float az)
	{
		x *= ax;
		y *= ay;
		z *= az;
		return this;
	}

	public Vector3f scale(Vector3f v)
	{
		return scaleInto(v, this);
	}

	public Vector3f scaleInto(float s, Vector3f r)
	{
		if (r == null)
			r = new Vector3f();
		r.x = x * s;
		r.y = y * s;
		r.z = z * s;
		return r;
	}

	public Vector3f scaleInto(Vector3f v, Vector3f r)
	{
		if (r == null)
			r = new Vector3f();
		r.x = x * v.x;
		r.y = y * v.y;
		r.z = z * v.z;
		return r;
	}

	public Vector3f set(float f)
	{
		return set(f, f, f);
	}

	public Vector3f set(float vx, float vy, float vz)
	{
		x = vx;
		y = vy;
		z = vz;
		return this;
	}

	public Vector3f set(Vector3f v)
	{
		return set(v.x, v.y, v.z);
	}

	public Vector3f subtract(float ax, float ay, float az)
	{
		x -= ax;
		y -= ay;
		z -= az;
		return this;
	}

	public Vector3f subtract(Vector3f v)
	{
		return subtractInto(v, this);
	}

	public Vector3f subtractInto(Vector3f v, Vector3f r)
	{
		if (r == null)
			r = new Vector3f();
		r.x = x - v.x;
		r.y = y - v.y;
		r.z = z - v.z;
		return r;
	}

	@Override
	public float[] toArray(float[] a)
	{
		if (a == null || a.length != 3)
			a = new float[3];
		a[0] = x;
		a[1] = y;
		a[2] = z;
		return a;
	}

	@Override
	public Vector3f loadFromBuffer(FloatBuffer fb)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector3f loadFromArray(float[] f)
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
		return String.format("[%f,%f,%f]", x, y, z);
	}
}
