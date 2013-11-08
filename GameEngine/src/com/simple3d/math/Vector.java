package com.simple3d.math;

public interface Vector<T extends Vector<T>>
{
	public T add(T v);

	public T addInto(T v, T r);

	public T conjugate();

	public T conjugateInto(T r);

	public float dot(T v);

	public T lerp(T q, float lerp);

	public T lerpInto(T q, float lerp, T r);

	public float magnitude();

	public float magnitudeSquared();

	public T normalize();

	public T normalizeInto(T r);

	public T scale(float s);

	public T scale(T v);

	public T scaleInto(float s, T r);

	public T scaleInto(T v, T r);

	public T set(float f);

	public T set(T m);

	public T subtract(T v);

	public T subtractInto(T v, T r);

	public float[] toFloatArray();

	public float[] toFloatArray(float[] a);
}
