package com.simple3d.math;

public interface Matrix<T extends Matrix<T>>
{
	public static final int COL_MAJOR = 0;
	public static final int ROW_MAJOR = 1;

	public T add(T m);

	public T addInto(T m, T r);

	public T adjoint();

	public T adjointInto(T r);

	public float det();

	public T inverse() throws MatrixOperationException;

	public T inverseInto(T s) throws MatrixOperationException;

	public T loadIdentity();

	public T multiply(T m);

	public T multiplyInto(T m, T r);

	public T scale(float f);

	public T scale(T m);

	public T scaleInto(float f, T r);

	public T scaleInto(T m, T r);

	public T set(T m);

	public T subtract(T m);

	public T subtractInto(T m, T r);

	public float[] toFloatArray(int major);

	public float[] toFloatArray(int major, float[] r);

	public T transpose();

	public T transposeInto(T r);
}
