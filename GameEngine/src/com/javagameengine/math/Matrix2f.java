package com.javagameengine.math;

import java.nio.FloatBuffer;

/**
 * 2x2 square matrix class implemented using floats.
 * @author ClairaLyrae
 */
public class Matrix2f extends Matrix<Matrix2f>
{
	public float f00, f01, f10, f11;

	public Matrix2f()
	{
		loadIdentity();
	}

	public Matrix2f(float f1, float f2, float f3, float f4)
	{
		f00 = f1;
		f01 = f2;
		f10 = f3;
		f11 = f4;
	}

	public Matrix2f(float[] m)
	{
		this(m[0], m[1], m[2], m[3]);
	}

	public Matrix2f(Matrix2f m)
	{
		this(m.f00, m.f01, m.f10, m.f11);
	}

	public Matrix2f add(Matrix2f m)
	{
		return addInto(m, this);
	}

	public Matrix2f addInto(Matrix2f m, Matrix2f r)
	{
		if (r == null)
			r = new Matrix2f();
		r.f00 = f00 + m.f00;
		r.f01 = f01 + m.f01;
		r.f10 = f10 + m.f10;
		r.f11 = f11 + m.f11;
		return r;
	}

	public Matrix2f adjoint()
	{
		return adjointInto(this);
	}

	public Matrix2f adjointInto(Matrix2f r)
	{
		if (r == null)
			r = new Matrix2f();
		r.f00 = f11;
		r.f01 = -f01;
		r.f10 = -f10;
		r.f11 = f00;
		return r;
	}

	public float det()
	{
		return (f00 * f11) - (f01 * f10);
	}

	public Matrix2f inverse()
	{
		return inverseInto(this);
	}

	public Matrix2f inverseInto(Matrix2f r)
	{
		if (r == null)
			r = new Matrix2f();
		float f00t = f00;
		float f11t = f11;
		float det = 1.0f / det();
		r.f00 = det * f11t;
		r.f01 = -det * f01;
		r.f10 = -det * f10;
		r.f11 = det * f00t;
		return r;
	}

	public Matrix2f loadIdentity()
	{
		return set(0f, 0f, 0f, 0f);
	}

	public Matrix2f multiply(Matrix2f m)
	{
		return multiplyInto(m, this);
	}

	public Matrix2f multiplyInto(Matrix2f m, Matrix2f r)
	{
		if (r == null)
			r = new Matrix2f();
		float f00t = f00 * m.f00 + f01 * m.f10;
		float f01t = f00 * m.f01 + f01 * m.f11;
		float f10t = f10 * m.f00 + f11 * m.f10;
		float f11t = f10 * m.f01 + f11 * m.f11;
		r.f00 = f00t;
		r.f01 = f01t;
		r.f10 = f10t;
		r.f11 = f11t;
		return r;
	}

	public Vector2f multiplyInto(Vector2f v, Vector2f r)
	{
		if (r == null)
			r = new Vector2f();
		float f0t = f00 * v.x + f01 * v.y;
		float f1t = f10 * v.x + f11 * v.y;
		r.x = f0t;
		r.y = f1t;
		return r;
	}

	public Matrix2f scale(float f)
	{
		return scaleInto(f, this);
	}

	public Matrix2f scale(Matrix2f m)
	{
		return scaleInto(m, this);
	}

	public Matrix2f scaleInto(float s, Matrix2f r)
	{
		if (r == null)
			r = new Matrix2f();
		r.f00 = f00 * s;
		r.f01 = f01 * s;
		r.f10 = f10 * s;
		r.f11 = f11 * s;
		return r;
	}

	public Matrix2f scaleInto(Matrix2f m, Matrix2f r)
	{
		if (r == null)
			r = new Matrix2f();
		r.f00 = f00 * m.f00;
		r.f01 = f01 * m.f01;
		r.f10 = f10 * m.f10;
		r.f11 = f11 * m.f11;
		return r;
	}

	public Matrix2f fill(float f)
	{
		return set(f, f, f, f);
	}

	public Matrix2f set(float m00, float m01, float m10, float m11)
	{
		f00 = m00;
		f01 = m01;
		f10 = m10;
		f11 = m11;
		return this;
	}

	public Matrix2f set(Matrix2f m)
	{
		f00 = m.f00;
		f01 = m.f01;
		f10 = m.f10;
		f11 = m.f11;
		return this;
	}

	public Matrix2f subtract(Matrix2f m)
	{
		return subtractInto(m, this);
	}

	public Matrix2f subtractInto(Matrix2f m, Matrix2f r)
	{
		if (r == null)
			r = new Matrix2f();
		r.f00 = f00 - m.f00;
		r.f01 = f01 - m.f01;
		r.f10 = f10 - m.f10;
		r.f11 = f11 - m.f11;
		return r;
	}

	public float[] toFloatArray(int major)
	{
		return toFloatArray(major, new float[4]);
	}

	public float[] toFloatArray(int major, float[] a)
	{
		if (a.length != 4)
			return a;
		if (major == Matrix.ROW_MAJOR)
		{
			a[0] = f00;
			a[1] = f01;
			a[2] = f10;
			a[3] = f11;
		} else
		{
			a[0] = f00;
			a[1] = f10;
			a[2] = f01;
			a[3] = f11;
		}
		return a;
	}

	public String toString()
	{
		return String.format("[%f,%f;%f,%f]", f00, f01, f10, f11);
	}

	public Matrix2f transpose()
	{
		return transposeInto(this);
	}

	public Matrix2f transposeInto(Matrix2f r)
	{
		if (r == null)
			r = new Matrix2f();
		float f01t = f01;
		r.f00 = f00;
		r.f01 = f10;
		r.f10 = f01t;
		r.f11 = f11;
		return r;
	}

	public Matrix2f loadFromBuffer(FloatBuffer fb)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Matrix2f loadFromArray(float[] f)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public FloatBuffer toBuffer(FloatBuffer fb)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public float[] toArray(float[] f)
	{
		// TODO Auto-generated method stub
		return null;
	}
}
