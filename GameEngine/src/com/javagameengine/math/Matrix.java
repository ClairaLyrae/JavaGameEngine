package com.javagameengine.math;

import java.nio.FloatBuffer;

/**
 * Interface describing operations that the Matrix classes support that are not dimension specific. Operations are 
 * generally in two formats: operation, and operationInto. The normal operation methods do the operation and modify
 * the object the operation was called from. For example, obj.normalize() will normalize obj. The Into suffix 
 * denotes that the method has another parameter which is a reference to the object the operation will save the
 * result into. For example, obj1.normalizeInto(obj2) will normalize obj1 and store the result in obj2, without
 * modifying the contents of obj1. 
 */
public abstract class Matrix<T  extends Matrix<T>>
{
	public static final int COL_MAJOR = 0;
	public static final int ROW_MAJOR = 1;

	public abstract T add(T m);

	public abstract T addInto(T m, T r);

	public abstract T adjoint();

	public abstract T adjointInto(T r);

	public abstract float det();

	public abstract T inverse();

	public abstract T inverseInto(T s);

	public abstract T loadIdentity();

	public abstract T multiply(T m);

	public abstract T multiplyInto(T m, T r);

	public abstract T scale(float f);

	public abstract T scale(T m);

	public abstract T scaleInto(float f, T r);

	public abstract T scaleInto(T m, T r);

	public abstract T set(T m);

	public abstract T subtract(T m);

	public abstract T subtractInto(T m, T r);

	public abstract T transpose();

	public abstract T transposeInto(T r);
	
	public abstract T fill(float f);
	
	public abstract T fromBuffer(FloatBuffer fb);
	
	public abstract T fromArray(float[] f);
	
	public abstract T set(int i, int j, float f);

	public abstract FloatBuffer toBuffer();
	
	public abstract float[] toArray();
}
