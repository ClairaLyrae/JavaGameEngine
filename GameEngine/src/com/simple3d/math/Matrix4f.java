package com.simple3d.math;

public class Matrix4f implements Matrix<Matrix4f>
{
	public float f00, f01, f02, f03, f10, f11, f12, f13, f20, f21, f22, f23,
			f30, f31, f32, f33;

	public Matrix4f()
	{
		loadIdentity();
	}

	public Matrix4f(float f1, float f2, float f3, float f4, float f5, float f6,
			float f7, float f8, float f9, float f10, float f11, float f12,
			float f13, float f14, float f15, float f16)
	{
		f00 = f1;
		f01 = f2;
		f02 = f3;
		f03 = f4;
		f10 = f5;
		f11 = f6;
		f12 = f7;
		f13 = f8;
		f20 = f9;
		f21 = f10;
		f22 = f11;
		f23 = f12;
		f30 = f13;
		f31 = f14;
		f32 = f15;
		f33 = f16;
	}

	public Matrix4f add(Matrix4f m)
	{
		return addInto(m, this);
	}

	public Matrix4f addInto(Matrix4f m, Matrix4f r)
	{
		if (r == null)
			r = new Matrix4f();
		r.f00 = f00 + m.f00;
		r.f01 = f01 + m.f01;
		r.f02 = f02 + m.f02;
		r.f03 = f03 + m.f03;
		r.f10 = f10 + m.f10;
		r.f11 = f11 + m.f11;
		r.f12 = f12 + m.f12;
		r.f13 = f13 + m.f13;
		r.f20 = f20 + m.f20;
		r.f21 = f21 + m.f21;
		r.f22 = f22 + m.f22;
		r.f23 = f23 + m.f23;
		r.f30 = f30 + m.f30;
		r.f31 = f31 + m.f31;
		r.f32 = f32 + m.f32;
		r.f33 = f33 + m.f33;
		return r;
	}

	public Matrix4f adjoint()
	{
		return adjointInto(this);
	}

	public Matrix4f adjointInto(Matrix4f r)
	{
		// TODO
		if (r == null)
			r = new Matrix4f();
		r.f00 = f11;
		r.f01 = -f01;
		r.f10 = -f10;
		r.f11 = f00;
		return r;
	}

	public float det()
	{
		// TODO
		return 0.0f;
	}

	public Matrix4f inverse() throws MatrixOperationException
	{
		return inverseInto(this);
	}

	public Matrix4f inverseInto(Matrix4f r) throws MatrixOperationException
	{
		// TODO Auto-generated method stub
		return null;
	}

	public Matrix4f loadIdentity()
	{
		return set(1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f,
				1f);
	}

	public Matrix4f multiply(Matrix4f m)
	{
		return multiplyInto(m, this);
	}

	public Matrix4f multiplyInto(Matrix4f m, Matrix4f r)
	{
		// TODO
		if (r == null)
			r = new Matrix4f();
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

	public Vector4f multiplyInto(Vector4f v, Vector4f r)
	{
		if (r == null)
			r = new Vector4f();
		float wt = f00 * v.w + f01 * v.x + f02 * v.y + f03 * v.z;
		float xt = f10 * v.w + f11 * v.x + f12 * v.y + f13 * v.z;
		float yt = f20 * v.w + f21 * v.x + f22 * v.y + f23 * v.z;
		float zt = f30 * v.w + f31 * v.x + f32 * v.y + f33 * v.z;
		r.w = wt;
		r.x = xt;
		r.y = yt;
		r.z = zt;
		return r;
	}

	public Matrix4f scale(float f)
	{
		return scaleInto(f, this);
	}

	public Matrix4f scale(Matrix4f m)
	{
		return scaleInto(m, this);
	}

	public Matrix4f scaleInto(float s, Matrix4f r)
	{
		if (r == null)
			r = new Matrix4f();
		r.f00 = f00 * s;
		r.f01 = f01 * s;
		r.f02 = f02 * s;
		r.f03 = f03 * s;
		r.f10 = f10 * s;
		r.f11 = f11 * s;
		r.f12 = f12 * s;
		r.f13 = f13 * s;
		r.f20 = f20 * s;
		r.f21 = f21 * s;
		r.f22 = f22 * s;
		r.f23 = f23 * s;
		r.f30 = f30 * s;
		r.f31 = f31 * s;
		r.f32 = f32 * s;
		r.f33 = f33 * s;
		return r;
	}

	public Matrix4f scaleInto(Matrix4f m, Matrix4f r)
	{
		if (r == null)
			r = new Matrix4f();
		r.f00 = f00 * m.f00;
		r.f01 = f01 * m.f01;
		r.f02 = f02 * m.f02;
		r.f03 = f03 * m.f03;
		r.f10 = f10 * m.f10;
		r.f11 = f11 * m.f11;
		r.f12 = f12 * m.f12;
		r.f13 = f13 * m.f13;
		r.f20 = f20 * m.f20;
		r.f21 = f21 * m.f21;
		r.f22 = f22 * m.f22;
		r.f23 = f23 * m.f23;
		r.f30 = f30 * m.f30;
		r.f31 = f31 * m.f31;
		r.f32 = f32 * m.f32;
		r.f33 = f33 * m.f33;
		return r;
	}

	public Matrix4f set(float f)
	{
		return set(f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f);
	}

	public Matrix4f set(float m00, float m01, float m02, float m03, float m10,
			float m11, float m12, float m13, float m20, float m21, float m22,
			float m23, float m30, float m31, float m32, float m33)
	{
		f00 = m00;
		f01 = m01;
		f02 = m02;
		f03 = m03;
		f10 = m10;
		f11 = m11;
		f12 = m12;
		f13 = m13;
		f20 = m20;
		f21 = m21;
		f22 = m22;
		f23 = m23;
		f30 = m30;
		f31 = m31;
		f32 = m32;
		f33 = m33;
		return this;
	}

	public Matrix4f set(Matrix4f m)
	{
		return set(m.f00, m.f01, m.f02, m.f03, m.f10, m.f11, m.f12, m.f13,
				m.f20, m.f21, m.f22, m.f23, m.f30, m.f31, m.f32, m.f33);
	}

	public Matrix4f subtract(Matrix4f m)
	{
		return subtractInto(m, this);
	}

	public Matrix4f subtractInto(Matrix4f m, Matrix4f r)
	{
		if (r == null)
			r = new Matrix4f();
		r.f00 = f00 - m.f00;
		r.f01 = f01 - m.f01;
		r.f02 = f02 - m.f02;
		r.f03 = f03 - m.f03;
		r.f10 = f10 - m.f10;
		r.f11 = f11 - m.f11;
		r.f12 = f12 - m.f12;
		r.f13 = f13 - m.f13;
		r.f20 = f20 - m.f20;
		r.f21 = f21 - m.f21;
		r.f22 = f22 - m.f22;
		r.f23 = f23 - m.f23;
		r.f30 = f30 - m.f30;
		r.f31 = f31 - m.f31;
		r.f32 = f32 - m.f32;
		r.f33 = f33 - m.f33;
		return r;
	}

	public float[] toFloatArray(int major)
	{
		return toFloatArray(major, new float[16]);
	}

	public float[] toFloatArray(int major, float[] a)
	{
		if (a.length != 16)
			return a;
		if (major == Matrix.ROW_MAJOR)
		{
			a[0] = f00;
			a[1] = f01;
			a[2] = f02;
			a[3] = f03;
			a[4] = f10;
			a[5] = f11;
			a[6] = f12;
			a[7] = f13;
			a[8] = f20;
			a[9] = f21;
			a[10] = f22;
			a[11] = f23;
			a[12] = f30;
			a[13] = f31;
			a[14] = f32;
			a[15] = f33;
		} else
		{
			a[0] = f00;
			a[1] = f10;
			a[2] = f20;
			a[3] = f30;
			a[4] = f01;
			a[5] = f11;
			a[6] = f21;
			a[7] = f31;
			a[8] = f02;
			a[9] = f12;
			a[10] = f22;
			a[11] = f32;
			a[12] = f03;
			a[13] = f13;
			a[14] = f23;
			a[15] = f33;
		}
		return a;
	}

	public String toString()
	{
		return String.format(
				"[%f,%f,%f,%f;%f,%f,%f,%f;%f,%f,%f,%f;%f,%f,%f,%f;]", f00, f01,
				f02, f03, f10, f11, f12, f13, f20, f21, f22, f23, f30, f31,
				f32, f33);
	}

	public Matrix4f transpose()
	{
		return transposeInto(this);
	}

	public Matrix4f transposeInto(Matrix4f r)
	{
		return r.set(f00, f10, f20, f30, f01, f11, f21, f31, f02, f12, f22,
				f32, f03, f13, f23, f33);
	}
}
