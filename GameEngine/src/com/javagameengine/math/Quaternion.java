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
		return (float) FastMath.sqrt(w * w + x * x + y * y + z * z);
	}

	public float magnitudeSquared()
	{
		return (w * w + x * x + y * y + z * z);
	}
	
	public Vector3f multiply(Vector3f v)
	{
		return multiplyInto(v, null);
	}
	
	public Vector3f multiplyInto(Vector3f v, Vector3f r) 
	{
		if(r == null)
			r = new Vector3f();
        if (v.x == 0 && v.y == 0 && v.z == 0)
            r.set(0, 0, 0);
        else
        {
            float tempX, tempY;
            tempX = w * w * r.x + 2 * y * w * r.z - 2 * z * w * r.y + x * x * r.x
                    + 2 * y * x * r.y + 2 * z * x * r.z - z * z * r.x - y * y * r.x;
            tempY = 2 * x * y * r.x + y * y * r.y + 2 * z * y * r.z + 2 * w * z
                    * r.x - z * z * r.y + w * w * r.y - 2 * x * w * r.z - x * x
                    * r.y;
            r.z = 2 * x * z * r.x + 2 * y * z * r.y + z * z * r.z - 2 * w * y * r.x
                    - y * y * r.z + 2 * w * x * r.y - x * x * r.z + w * w * r.z;
            r.x = tempX;
            r.y = tempY;
        }
        return r;
    }

	public Quaternion multiply(Quaternion m)
	{
		return multiplyInto(m, this);
	}
	
    public Quaternion multiplyInto(Quaternion q, Quaternion r) 
    {
        if (r == null)
            r = new Quaternion();
        float qw = q.w, qx = q.x, qy = q.y, qz = q.z;
        r.x = x * qw + y * qz - z * qy + w * qx;
        r.y = -x * qz + y * qw + z * qx + w * qy;
        r.z = x * qy - y * qx + z * qw + w * qz;
        r.w = -x * qx - y * qy - z * qz + w * qw;
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

    public Quaternion fromAxisAngle(Vector4f axis) 
    {
        return fromAxisAngle(axis.w, axis.x, axis.y, axis.z);
    }
    
    public Quaternion fromAxisAngle(float angle, float ax, float ay, float az) 
    {
        if (ax == 0 && ay == 0 && az == 0)
                loadIdentity();
        else 
        {
                float halfAngle = 0.5f * angle;
                float sin = FastMath.sin(halfAngle);
                w = FastMath.cos(halfAngle);
                x = sin * ax;
                y = sin * ay;
                z = sin * az;
        }
        return this;
    }

    public Vector4f toAxisAngle() 
    {
        return toAxisAngle(null);
    }	
    
    public Vector4f toAxisAngle(Vector4f r) 
    {
    	if(r == null)
			r = new Vector4f();
        float sqrLength = x * x + y * y + z * z;
        if (sqrLength == 0f) 
        {
            r.w = 0f;
            r.x = 1f;
            r.y = 0f;
            r.z = 0f;
        } else {
            r.w = (2f * FastMath.acos(w));
            float invLength = FastMath.invSqrt(sqrLength);
            r.x = x * invLength;
            r.y = y * invLength;
            r.z = z * invLength;
        }
        return r;
    }	

	public Matrix3f toRotationMatrix3f()
	{
		Matrix3f m = new Matrix3f();
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
	
	public Matrix4f toRotationMatrix4f()
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
