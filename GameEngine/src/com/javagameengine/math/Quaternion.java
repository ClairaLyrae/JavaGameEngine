package com.javagameengine.math;

import java.nio.FloatBuffer;

//TODO This class is far from complete and totally untested. Someone needs to get a handle on 
//how the quaternions work, finish the class, and test it to make sure it makes sense

/**
 * Quaternion vector utilizing floats. Replicates the capabilities of Vector4f in quaternion space. Provides a number of
 * quaternion-specific methods for rotational operations.
 * @author ClairaLyrae
 */
public class Quaternion extends Vector<Quaternion>
{
	public static final Quaternion identity = new Quaternion(1f, 0f, 0f, 0f);
	public static final Quaternion zero = new Quaternion(0f, 0f, 0f, 0f);
	public static final Quaternion one = new Quaternion(1f, 1f, 1f, 1f);
	public static final Quaternion unit_w = new Quaternion(1f, 0f, 0f, 0f);
	public static final Quaternion unit_x = new Quaternion(0f, 1f, 0f, 0f);
	public static final Quaternion unit_y = new Quaternion(0f, 0f, 1f, 0f);
	public static final Quaternion unit_z = new Quaternion(0f, 0f, 0f, 1f);

	public float w, x, y, z;

	public Quaternion()
	{
		this(1f, 0f, 0f, 0f);
	}

	public Quaternion(float w, float x, float y, float z)
	{
		set(w, x, y, z);
	}

	public Quaternion(float[] a) throws IndexOutOfBoundsException
	{
		if (a.length != 4)
			throw new IndexOutOfBoundsException();
		else
			set(a[0], a[1], a[2], a[3]);
	}

	public Quaternion(Quaternion v)
	{
		set(v.w, v.x, v.y, v.z);
	}

	public Quaternion(Vector4f v)
	{
		set(v.w, v.x, v.y, v.z);
	}

	public Quaternion add(Quaternion v)
	{
		return addInto(v, this);
	}

	public Quaternion addInto(Quaternion v, Quaternion r)
	{
		if (r == null)
			r = new Quaternion();
		r.w = w + v.w;
		r.x = x + v.x;
		r.y = y + v.y;
		r.z = z + v.z;
		return r;
	}

	public Quaternion conjugate()
	{
		return conjugateInto(this);
	}

	public Quaternion conjugateInto(Quaternion r)
	{
		if (r == null)
			r = new Quaternion();
		r.x = -x;
		r.y = -y;
		r.z = -z;
		return r;
	}

	public float dot(Quaternion v)
	{
		return w * v.w + x * v.x + y * v.y + z * v.z;
	}

	public Quaternion lerp(Quaternion v, float s)
	{
		return lerpInto(v, s, this);
	}

	public Quaternion lerpInto(Quaternion v, float lerp, Quaternion r)
	{
		// TODO I think this isn't actually useful for anything, and it needs to be slerp instead (spherical linear interpolation)
		// Pretty sure normal linear interpolation doesnt actually mean anything with quaternions...
		if (r == null)
			r = new Quaternion();
		r.w = ((w - v.w) * lerp) + w;
		r.x = ((x - v.x) * lerp) + x;
		r.y = ((y - v.y) * lerp) + y;
		r.z = ((z - v.z) * lerp) + z;
		return r;
	}

	public Quaternion loadIdentity()
	{
		return set(1.0f, 0.0f, 0.0f, 0.0f);
	}

	public float magnitude()
	{
		return (float) MathUtil.fastSqrt(w * w + x * x + y * y + z * z);
	}

	public float magnitudeSquared()
	{
		return (w * w + x * x + y * y + z * z);
	}

	public Quaternion multiply(Quaternion m)
	{
		return multiplyInto(m, this);
	}
	
	public Quaternion multiplyInto(Quaternion q, Quaternion r)
	{
		// TODO check if this works!
		if(r == null)
			r = new Quaternion();
		float w1 = w;
		float x1 = x;
		float y1 = y;
		float z1 = z;	
		float w2 = q.w;
		float x2 = q.x;
		float y2 = q.y;
		float z2 = q.z;	
		r.w = w1*w2 - x1*x2 - y1*y2 - z1*z2;
		r.x = w1*x2 + x1*w2 + y1*z2 - z1*y2;
		r.y = w1*y2 - x1*z2 + y1*w2 + z1*x2;
		r.z = w1*z2 + x1*y2 - y1*x2 + z1*w2;
		return r;
	}
	
	public Quaternion multiply(Matrix4f m)
	{
		return multiplyInto(m, this);
	}

	public Quaternion multiplyInto(Matrix4f m, Quaternion r)
	{
		if (r == null)
			r = new Quaternion();
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

	public Quaternion normalize()
	{
		return normalizeInto(this);
	}

	public Quaternion normalizeInto(Quaternion r)
	{
		if (r == null)
			r = new Quaternion();
		float mag = 1.0f / magnitude();
		r.w = mag * w;
		r.x = mag * x;
		r.y = mag * y;
		r.z = mag * z;
		return r;
	}

	public Quaternion rotate(float x, float y, float z)
	{
		return new Quaternion();
	}

	public Quaternion rotate(Vector3f v)
	{
		return new Quaternion();
	}

	public Quaternion rotateTo(Quaternion q)
	{
		return new Quaternion();
	}

	public Quaternion rotateTo(Quaternion q, float step)
	{
		return new Quaternion();
	}

	public Quaternion scale(float s)
	{
		return scaleInto(s, this);
	}

	public Quaternion scale(Quaternion v)
	{
		return scaleInto(v, this);
	}

	public Quaternion scaleInto(float s, Quaternion r)
	{
		if (r == null)
			r = new Quaternion();
		r.w = w * s;
		r.x = x * s;
		r.y = y * s;
		r.z = z * s;
		return r;
	}

	public Quaternion scaleInto(Quaternion v, Quaternion r)
	{
		if (r == null)
			r = new Quaternion();
		r.w = w * v.w;
		r.x = x * v.x;
		r.y = y * v.y;
		r.z = z * v.z;
		return r;
	}

	public Quaternion set(float f)
	{
		return set(f, f, f, f);
	}

	public Quaternion set(float vw, float vx, float vy, float vz)
	{
		w = vw;
		x = vx;
		y = vy;
		z = vz;
		return this;
	}

	public Quaternion set(Quaternion v)
	{
		return set(v.w, v.x, v.y, v.z);
	}

	public Quaternion subtract(Quaternion v)
	{
		return subtractInto(v, this);
	}

	public Quaternion subtractInto(Quaternion v, Quaternion r)
	{
		if (r == null)
			r = new Quaternion();
		r.w = w - v.w;
		r.x = x - v.x;
		r.y = y - v.y;
		r.z = z - v.z;
		return r;
	}
	
	public Vector4f getAxisAngle()
	{
		Quaternion q = normalizeInto(new Quaternion());
		float angle = 2f*(float)Math.acos((double)q.w);
		return new Vector4f(angle, q.x, q.y, q.z);
	}

	public Matrix4f toRotationMatrix()
	{
		Matrix4f m = new Matrix4f();
		float Nq = magnitudeSquared();
		float s = 0.0f;
		if (Nq > 0.0f)
			s = 2.0f / Nq;
		float X = x * s;
		float Y = y * s;
		float Z = z * s;
		float wX = w * X;
		float wY = w * Y;
		float wZ = w * Z;
		float xX = x * X;
		float xY = x * Y;
		float xZ = x * Z;
		float yY = y * Y;
		float yZ = y * Z;
		float zZ = z * Z;
		m.f00 = 1.0f - (yY + zZ);
		m.f01 = xY - wZ;
		m.f02 = xZ + wY;
		m.f10 = xY + wZ;
		m.f11 = 1.0f - (xX + zZ);
		m.f12 = yZ - wX;
		m.f20 = xZ - wY;
		m.f21 = yZ + wX;
		m.f22 = 1.0f - (xX + yY);
		return m;
	}

	@Override
	public Quaternion loadFromBuffer(FloatBuffer fb)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Quaternion loadFromArray(float[] f)
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

	public String toString()
	{
		return String.format("[%f,%f,%f,%f]", w, x, y, z);
	}
}
