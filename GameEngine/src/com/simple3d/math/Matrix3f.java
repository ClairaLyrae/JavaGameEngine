package com.simple3d.math;

public class Matrix3f implements Matrix<Matrix3f>
{
	public float f00, f01, f02, f10, f11, f12, f20, f21, f22;

	public Matrix3f()
	{
		loadIdentity();
	}

	public Matrix3f(float f1, float f2, float f3, float f4, float f5, float f6,
			float f7, float f8, float f9)
	{
		f00 = f1;
		f01 = f2;
		f02 = f3;
		f10 = f4;
		f11 = f5;
		f12 = f6;
		f20 = f7;
		f21 = f8;
		f22 = f9;
	}

	public Matrix3f add(Matrix3f m)
	{
		return addInto(m, this);
	}

	public Matrix3f addInto(Matrix3f m, Matrix3f r)
	{
		if (r == null)
			r = new Matrix3f();
		r.f00 = f00 + m.f00;
		r.f01 = f01 + m.f01;
		r.f02 = f02 + m.f02;
		r.f10 = f10 + m.f10;
		r.f11 = f11 + m.f11;
		r.f12 = f12 + m.f12;
		r.f20 = f20 + m.f20;
		r.f21 = f21 + m.f21;
		r.f22 = f22 + m.f22;
		return r;
	}

	public Matrix3f adjoint()
	{
		return adjointInto(this);
	}

	public Matrix3f adjointInto(Matrix3f r)
	{
		if (r == null)
			r = new Matrix3f();
		r.f00 = f11 * f22 - f12 * f21;
		r.f01 = f02 * f21 - f01 * f22;
		r.f02 = f01 * f12 - f02 * f11;
		r.f10 = f12 * f20 - f10 * f22;
		r.f11 = f00 * f22 - f02 * f20;
		r.f12 = f02 * f10 - f00 * f12;
		r.f20 = f10 * f21 - f11 * f20;
		r.f21 = f01 * f20 - f00 * f21;
		r.f22 = f00 * f11 - f01 * f10;
		return r;
	}

	public float det()
	{
		return (f00 * (f11 * f22 - f21 * f12) - f10 * (f01 * f22 - f21 * f02) + f20
				* (f01 * f12 - f11 * f02));
	}

	public Matrix3f inverse() throws MatrixOperationException
	{
		return inverseInto(this);
	}

	public Matrix3f inverseInto(Matrix3f r) throws MatrixOperationException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Matrix3f loadIdentity()
	{
		return set(1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f);
	}

	public Matrix3f multiply(Matrix3f m)
	{
		return multiplyInto(m, this);
	}

	public Matrix3f multiplyInto(Matrix3f m, Matrix3f r)
	{
		if (r == null)
			r = new Matrix3f();
		float m00 = f00 * m.f00 + f01 * m.f10 + f02 * m.f20;
		float m01 = f00 * m.f01 + f01 * m.f11 + f02 * m.f21;
		float m02 = f00 * m.f02 + f01 * m.f12 + f02 * m.f22;

		float m10 = f10 * m.f00 + f11 * m.f10 + f12 * m.f20;
		float m11 = f10 * m.f01 + f11 * m.f11 + f12 * m.f21;
		float m12 = f10 * m.f02 + f11 * m.f12 + f12 * m.f22;

		float m20 = f20 * m.f00 + f21 * m.f10 + f22 * m.f20;
		float m21 = f20 * m.f01 + f21 * m.f11 + f22 * m.f21;
		float m22 = f20 * m.f02 + f21 * m.f12 + f22 * m.f22;
		r.f00 = m00;
		r.f01 = m01;
		r.f02 = m02;
		r.f10 = m10;
		r.f11 = m11;
		r.f12 = m12;
		r.f20 = m20;
		r.f21 = m21;
		r.f22 = m22;
		return r;
	}

	public Vector3f multiplyInto(Vector3f v, Vector3f r)
	{
		if (r == null)
			r = new Vector3f();
		float xt = f00 * v.x + f01 * v.y + f02 * v.z;
		float yt = f10 * v.x + f11 * v.y + f12 * v.z;
		float zt = f20 * v.x + f21 * v.y + f22 * v.z;
		r.x = xt;
		r.y = yt;
		r.z = zt;
		return r;
	}

	public Matrix3f scale(float f)
	{
		return scaleInto(f, this);
	}

	public Matrix3f scale(Matrix3f m)
	{
		return scaleInto(m, this);
	}

	public Matrix3f scaleInto(float s, Matrix3f r)
	{
		if (r == null)
			r = new Matrix3f();
		r.f00 = f00 * s;
		r.f01 = f01 * s;
		r.f02 = f02 * s;
		r.f10 = f10 * s;
		r.f11 = f11 * s;
		r.f12 = f12 * s;
		r.f20 = f20 * s;
		r.f21 = f21 * s;
		r.f22 = f22 * s;
		return r;
	}

	public Matrix3f scaleInto(Matrix3f m, Matrix3f r)
	{
		if (r == null)
			r = new Matrix3f();
		r.f00 = f00 * m.f00;
		r.f01 = f01 * m.f01;
		r.f02 = f02 * m.f02;
		r.f10 = f10 * m.f10;
		r.f11 = f11 * m.f11;
		r.f12 = f12 * m.f12;
		r.f20 = f20 * m.f20;
		r.f21 = f21 * m.f21;
		r.f22 = f22 * m.f22;
		return r;
	}

	public Matrix3f set(float f)
	{
		return set(f, f, f, f, f, f, f, f, f);
	}

	public Matrix3f set(float m00, float m01, float m02, float m10, float m11,
			float m12, float m20, float m21, float m22)
	{
		f00 = m00;
		f01 = m01;
		f02 = m02;
		f10 = m10;
		f11 = m11;
		f12 = m12;
		f20 = m20;
		f21 = m21;
		f22 = m22;
		return this;
	}

	public Matrix3f set(Matrix3f m)
	{
		return set(m.f00, m.f01, m.f02, m.f10, m.f11, m.f12, m.f20, m.f21,
				m.f22);
	}

	public Matrix3f subtract(Matrix3f m)
	{
		return subtractInto(m, this);
	}

	public Matrix3f subtractInto(Matrix3f m, Matrix3f r)
	{
		if (r == null)
			r = new Matrix3f();
		r.f00 = f00 - m.f00;
		r.f01 = f01 - m.f01;
		r.f02 = f02 - m.f02;
		r.f10 = f10 - m.f10;
		r.f11 = f11 - m.f11;
		r.f12 = f12 - m.f12;
		r.f20 = f20 - m.f20;
		r.f21 = f21 - m.f21;
		r.f22 = f22 - m.f22;
		return r;
	}

	public float[] toFloatArray(int major)
	{
		return toFloatArray(major, new float[9]);
	}

	public float[] toFloatArray(int major, float[] a)
	{
		if (a.length != 9)
			return a;
		if (major == Matrix.ROW_MAJOR)
		{
			a[0] = f00;
			a[1] = f01;
			a[2] = f02;
			a[3] = f10;
			a[4] = f11;
			a[5] = f12;
			a[6] = f20;
			a[7] = f21;
			a[8] = f22;
		} else
		{
			a[0] = f00;
			a[1] = f10;
			a[2] = f20;
			a[3] = f01;
			a[4] = f11;
			a[5] = f21;
			a[6] = f02;
			a[7] = f12;
			a[8] = f22;
		}
		return a;
	}

	public String toString()
	{
		return String.format("[%f,%f,%f;%f,%f,%f;%f,%f,%f]", f00, f01, f02,
				f10, f11, f12, f20, f21, f22);
	}

	public Matrix3f transpose()
	{
		return transposeInto(this);
	}

	public Matrix3f transposeInto(Matrix3f r)
	{
		return r.set(f00, f10, f20, f01, f11, f21, f02, f12, f22);
	}
}
