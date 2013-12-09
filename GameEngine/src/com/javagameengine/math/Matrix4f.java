package com.javagameengine.math;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

/**
 * 4x4 square matrix class implemented using floats.
 * @author ClairaLyrae
 */
public class Matrix4f extends Matrix<Matrix4f>
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

	public Matrix4f(float f)
	{
		set(f);
	}

	public Matrix4f set(float f)
	{
		return set(f, f, f, f, f, f, f, f, f, f, f, f, f, f, f, f);
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
		if (r == null)
			r = new Matrix4f();
		// TODO Need to do adjoint 4x4 matrix calculation and store it in r
		return r;
	}

	public float det()
	{
		float f = f00 * ((f11 * f22 * f33 + f12 * f23 * f31 + f13 * f21 * f32)
					- f13 * f22 * f31
					- f11 * f23 * f32
					- f12 * f21 * f33);
		f -= f01 * ((f10 * f22 * f33 + f12 * f23 * f30 + f13 * f20 * f32)
				- f13 * f22 * f30
				- f10 * f23 * f32
				- f12 * f20 * f33);
		f += f02 * ((f10 * f21 * f33 + f11 * f23 * f30 + f13 * f20 * f31)
				- f13 * f21 * f30
				- f10 * f23 * f31
				- f11 * f20 * f33);
		f -= f03 * ((f10 * f21 * f32 + f11 * f22 * f30 + f12 * f20 * f31)
				- f12 * f21 * f30
				- f10 * f22 * f31
				- f11 * f20 * f32);
		return f;
	}

	public Matrix4f inverse()
	{
		return inverseInto(this);
	}

	private static float determinant3x3(float t00, float t01, float t02, float t10, float t11, float t12, float t20, float t21, float t22)
	{
		return   t00 * (t11 * t22 - t12 * t21)
		       + t01 * (t12 * t20 - t10 * t22)
		       + t02 * (t10 * t21 - t11 * t20);
	}

	public Matrix4f inverseInto(Matrix4f dest)
	{
		float det = this.det();
		if (det == 0) 
			return null;
		
		if (dest == null)
			dest = new Matrix4f();
		float determinant_inv = 1f/det;

		float t00 =  determinant3x3(f11, f12, f13, f21, f22, f23, f31, f32, f33);
		float t01 = -determinant3x3(f10, f12, f13, f20, f22, f23, f30, f32, f33);
		float t02 =  determinant3x3(f10, f11, f13, f20, f21, f23, f30, f31, f33);
		float t03 = -determinant3x3(f10, f11, f12, f20, f21, f22, f30, f31, f32);
		
		float t10 = -determinant3x3(f01, f02, f03, f21, f22, f23, f31, f32, f33);
		float t11 =  determinant3x3(f00, f02, f03, f20, f22, f23, f30, f32, f33);
		float t12 = -determinant3x3(f00, f01, f03, f20, f21, f23, f30, f31, f33);
		float t13 =  determinant3x3(f00, f01, f02, f20, f21, f22, f30, f31, f32);
		
		float t20 =  determinant3x3(f01, f02, f03, f11, f12, f13, f31, f32, f33);
		float t21 = -determinant3x3(f00, f02, f03, f10, f12, f13, f30, f32, f33);
		float t22 =  determinant3x3(f00, f01, f03, f10, f11, f13, f30, f31, f33);
		float t23 = -determinant3x3(f00, f01, f02, f10, f11, f12, f30, f31, f32);
		
		float t30 = -determinant3x3(f01, f02, f03, f11, f12, f13, f21, f22, f23);
		float t31 =  determinant3x3(f00, f02, f03, f10, f12, f13, f20, f22, f23);
		float t32 = -determinant3x3(f00, f01, f03, f10, f11, f13, f20, f21, f23);
		float t33 =  determinant3x3(f00, f01, f02, f10, f11, f12, f20, f21, f22);

		dest.f00 = t00*determinant_inv;
		dest.f11 = t11*determinant_inv;
		dest.f22 = t22*determinant_inv;
		dest.f33 = t33*determinant_inv;
		dest.f01 = t10*determinant_inv;
		dest.f10 = t01*determinant_inv;
		dest.f20 = t02*determinant_inv;
		dest.f02 = t20*determinant_inv;
		dest.f12 = t21*determinant_inv;
		dest.f21 = t12*determinant_inv;
		dest.f03 = t30*determinant_inv;
		dest.f30 = t03*determinant_inv;
		dest.f13 = t31*determinant_inv;
		dest.f31 = t13*determinant_inv;
		dest.f32 = t23*determinant_inv;
		dest.f23 = t32*determinant_inv;
		return dest;
	}

	public Matrix4f loadIdentity()
	{
		return set(1f, 0f, 0f, 0f, 
				0f, 1f, 0f, 0f, 
				0f, 0f, 1f, 0f, 
				0f, 0f, 0f, 1f);
	}

	public Matrix4f multiply(Matrix4f m)
	{
		return multiplyInto(m, this);
	}

	public Matrix4f multiplyInto(Matrix4f m, Matrix4f r)
	{
		if (r == null)
			r = new Matrix4f();
		float m00 = f00 * m.f00 + f01 * m.f10 + f02 * m.f20 + f03 * m.f30;
		float m01 = f00 * m.f01 + f01 * m.f11 + f02 * m.f21 + f03 * m.f31;
		float m02 = f00 * m.f02 + f01 * m.f12 + f02 * m.f22 + f03 * m.f32;
		float m03 = f00 * m.f03 + f01 * m.f13 + f02 * m.f23 + f03 * m.f33;

		float m10 = f10 * m.f00 + f11 * m.f10 + f12 * m.f20 + f13 * m.f30;
		float m11 = f10 * m.f01 + f11 * m.f11 + f12 * m.f21 + f13 * m.f31;
		float m12 = f10 * m.f02 + f11 * m.f12 + f12 * m.f22 + f13 * m.f32;
		float m13 = f10 * m.f03 + f11 * m.f13 + f12 * m.f23 + f13 * m.f33;

		float m20 = f20 * m.f00 + f21 * m.f10 + f22 * m.f20 + f23 * m.f30;
		float m21 = f20 * m.f01 + f21 * m.f11 + f22 * m.f21 + f23 * m.f31;
		float m22 = f20 * m.f02 + f21 * m.f12 + f22 * m.f22 + f23 * m.f32;
		float m23 = f20 * m.f03 + f21 * m.f13 + f22 * m.f23 + f23 * m.f33;

		float m30 = f30 * m.f00 + f31 * m.f10 + f32 * m.f20 + f33 * m.f30;
		float m31 = f30 * m.f01 + f31 * m.f11 + f32 * m.f21 + f33 * m.f31;
		float m32 = f30 * m.f02 + f31 * m.f12 + f32 * m.f22 + f33 * m.f32;
		float m33 = f30 * m.f03 + f31 * m.f13 + f32 * m.f23 + f33 * m.f33;
		
		r.f00 = m00;
		r.f01 = m01;
		r.f02 = m02;
		r.f03 = m03;
		r.f10 = m10;
		r.f11 = m11;
		r.f12 = m12;
		r.f13 = m13;
		r.f20 = m20;
		r.f21 = m21;
		r.f22 = m22;
		r.f23 = m23;
		r.f30 = m30;
		r.f31 = m31;
		r.f32 = m32;
		r.f33 = m33;
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

	public Matrix4f fill(float f)
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
		return set(m.f00, m.f01, m.f02, m.f03, 
				m.f10, m.f11, m.f12, m.f13,
				m.f20, m.f21, m.f22, m.f23, 
				m.f30, m.f31, m.f32, m.f33);
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

	public String toString()
	{
		return String.format(
				"matrix=\n" +
				"%.4f %.4f %.4f %.4f;\n" +
				"%.4f %.4f %.4f %.4f;\n" +
				"%.4f %.4f %.4f %.4f;\n" +
				"%.4f %.4f %.4f %.4f;\n", f00, f01,
				f02, f03, f10, f11, f12, f13, f20, f21, f22, f23, f30, f31,
				f32, f33);
	}

	public Matrix4f transpose()
	{
		return transposeInto(this);
	}

	public Matrix4f transposeInto(Matrix4f r)
	{
		if(r == null)
			r = new Matrix4f();
		return r.set(f00, f10, f20, f30, f01, f11, f21, f31, f02, f12, f22,
				f32, f03, f13, f23, f33);
	}

	public FloatBuffer toBuffer(FloatBuffer fb)
	{
		fb.clear();
		fb.put(f00).put(f01).put(f02).put(f03)
			.put(f10).put(f11).put(f12).put(f13)
			.put(f20).put(f21).put(f22).put(f23)
			.put(f30).put(f31).put(f32).put(f33);
		fb.flip();
		return fb;
	}
	
	public FloatBuffer toBuffer()
	{
		return toBuffer(BufferUtils.createFloatBuffer(16));
	}
	
	public Matrix4f fromBuffer(FloatBuffer fb)
	{
		if(fb == null)
			return null;
		f00 = fb.get();
		f01 = fb.get();
		f02 = fb.get();
		f03 = fb.get();
		f10 = fb.get();
		f11 = fb.get();
		f12 = fb.get();
		f13 = fb.get();
		f20 = fb.get();
		f21 = fb.get();
		f22 = fb.get();
		f23 = fb.get();
		f30 = fb.get();
		f31 = fb.get();
		f32 = fb.get();
		f33 = fb.get();
		return this;
	}

	public Matrix4f fromArray(float[] f)
	{
		if(f == null || f.length < 16)
			return null;
		f00 = f[0];
		f01 = f[1];
		f02 = f[2];
		f03 = f[3];
		f10 = f[4];
		f11 = f[5];
		f12 = f[6];
		f13 = f[7];
		f20 = f[8];
		f21 = f[9];
		f22 = f[10];
		f23 = f[11];
		f30 = f[12];
		f31 = f[13];
		f32 = f[14];
		f33 = f[15];
		return this;
	}

	public float[] toArray()
	{
		float[] a = new float[4];
		a[0] = f00;
		a[1] = f01;
		a[2] = f10;
		a[3] = f11;
		return a;
	}

	@Override
	public Matrix4f set(int i, int j, float f)
	{
		switch(i)
		{
		case 0:
			switch(j){
			case 0: f00 = f; break;
			case 1: f01 = f; break;
			case 2: f02 = f; break;
			case 3: f03 = f; break;
			} break;
		case 1:
			switch(j){
			case 0: f10 = f; break;
			case 1: f11 = f; break;
			case 2: f12 = f; break;
			case 3: f13 = f; break;
			} break;
		case 2:
			switch(j){
			case 0: f20 = f; break;
			case 1: f21 = f; break;
			case 2: f22 = f; break;
			case 3: f23 = f; break;
			} break;
		case 3:
			switch(j){
			case 0: f30 = f; break;
			case 1: f31 = f; break;
			case 2: f32 = f; break;
			case 3: f33 = f; break;
			} break;
		}
		return this;
	}
	
	public Matrix4f scaleTransform(float x, float y, float z)
	{
		f00 = f00 * x;
		f01 = f01 * x;
		f02 = f02 * x;
		f03 = f03 * x;
		f10 = f10 * y;
		f11 = f11 * y;
		f12 = f12 * y;
		f13 = f13 * y;
		f20 = f20 * z;
		f21 = f21 * z;
		f22 = f22 * z;
		f23 = f23 * z;
		return this;
	}
	
	public Matrix4f translateTransform(float x, float y, float z)
	{
		f30 += f00 * x + f10 * y + f20 * z;
		f31 += f01 * x + f11 * y + f21 * z;
		f32 += f02 * x + f12 * y + f22 * z;
		f33 += f03 * x + f13 * y + f23 * z;
		return this;
	}

	public static Matrix4f orthoMatrix(float left, float right, float top, float bottom, float znear, float zfar)
	{
		  Matrix4f m = new Matrix4f();
		  m.f00 = 2f/(right-left); 
		  m.f11 = 2f/(top-bottom); 
		  m.f22 = -2f/(zfar-znear); 
		  m.f03 = -1*((right+left)/(right-left));
		  m.f13 = -1*((top+bottom)/(top-bottom));
		  m.f23 = -1*((zfar+znear)/(zfar-znear));
		  return m;
	}
	
	public static Matrix4f perspectiveMatrix(float fovy, float aspect, float zNear, float zFar)
	{
		float cotangent, deltaZ;
		float radians = fovy*(FastMath.PI / 360f);

		deltaZ = zFar - zNear;

		float tan = FastMath.tan(radians);
		if ((deltaZ == 0) || (tan == 0) || (aspect == 0)) {
			return null;
		}

		cotangent = 1.0f/tan;

		Matrix4f m = new Matrix4f();
		
		m.f00 = cotangent / aspect;
		m.f11 = cotangent;
		m.f22 =  - (zFar + zNear) / deltaZ;
		m.f23 =  -1;
		m.f32 =  -2 * zNear * zFar / deltaZ;
		m.f33 =  0;

		return m.transpose();
	}
	
	public static Matrix4f lookAt(
		float eyex,
		float eyey,
		float eyez,
		float centerx,
		float centery,
		float centerz,
		float upx,
		float upy,
		float upz) 
	{
		Vector3f forward = new Vector3f();
		Vector3f side = new Vector3f();
		Vector3f up = new Vector3f();

		forward.x = centerx - eyex;
		forward.y = centery - eyey;
		forward.z = centerz - eyez;

		up.x = upx;
		up.y = upy;
		up.z = upz;

		forward.normalize();

		/* Side = forward x up */
		forward.crossInto(up, side);
		side.normalize();

		/* Recompute up as: up = side x forward */
		side.crossInto(forward, up);

		Matrix4f matrix = new Matrix4f();
		matrix.f00 = side.x;
		matrix.f10 = side.y;
		matrix.f20 = side.z;

		matrix.f01 = up.x;
		matrix.f11 = up.y;
		matrix.f21 = up.z;

		matrix.f02 = -forward.x;
		matrix.f12 = -forward.y;
		matrix.f22 = -forward.z;
		
		matrix.f03 = -eyex;
		matrix.f13 = -eyey;
		matrix.f23 = -eyez;
		return matrix;
	}
	
	public static Matrix4f lookAtMatrix(
			float eyex,
			float eyey,
			float eyez,
			float centerx,
			float centery,
			float centerz,
			float upx,
			float upy,
			float upz)
	{
		Vector3f position = new Vector3f(eyex, eyey, eyez);
		Vector3f target = new Vector3f(centerx, centery, centerz);
		Vector3f upVector = new Vector3f(upx, upy, upz);
	  Vector3f forward = target.subtractInto(position, null);
	  forward.normalize();
	  Vector3f right = forward.crossInto(upVector, null);
	  right.normalize();
	  Vector3f up = right.crossInto(forward, null);
	  up.normalize();  
	  Matrix4f mat = new Matrix4f();
	 
	  mat.f00 = right.x;
	  mat.f01 = right.y;
	  mat.f02 = right.z;

	  mat.f10 = up.x;
	  mat.f11 = up.y;
	  mat.f12 = up.z;

	  mat.f20 = -forward.x;
	  mat.f21 = -forward.y;
	  mat.f22 = -forward.z;

	  mat.f03 = -right.dot(position);
	  mat.f13 = -up.dot(position);
	  mat.f23 = forward.dot(position);
	  return mat;
	}
	
	public static Matrix4f rotationMatrix(float angle, float x, float y, float z) 
	{
		Matrix4f m = new Matrix4f(0f);
        m.f33 = 1;

        float fCos = FastMath.cos(angle);
        float fSin = FastMath.sin(angle);
        float fOneMinusCos = ((float) 1.0) - fCos;
        float fX2 = x * x;
        float fY2 = y * y;
        float fZ2 = z * z;
        float fXYM = x * y * fOneMinusCos;
        float fXZM = x * z * fOneMinusCos;
        float fYZM = y * z * fOneMinusCos;
        float fXSin = x * fSin;
        float fYSin = y * fSin;
        float fZSin = z * fSin;

        m.f00 = fX2 * fOneMinusCos + fCos;
        m.f01 = fXYM - fZSin;
        m.f02 = fXZM + fYSin;
        m.f10 = fXYM + fZSin;
        m.f11 = fY2 * fOneMinusCos + fCos;
        m.f12 = fYZM - fXSin;
        m.f20 = fXZM - fYSin;
        m.f21 = fYZM + fXSin;
        m.f22 = fZ2 * fOneMinusCos + fCos;
		return m;
	}

}
