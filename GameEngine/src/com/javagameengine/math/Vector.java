package com.javagameengine.math;

import java.nio.FloatBuffer;

/**
 * Interface describing operations that the Vector classes support that are not dimension specific. Operations are 
 * generally in two formats: operation, and operationInto. The normal operation methods do the operation and modify
 * the object the operation was called from. For example, obj.normalize() will normalize obj. The Into suffix 
 * denotes that the method has another parameter which is a reference to the object the operation will save the
 * result into. For example, obj1.normalizeInto(obj2) will normalize obj1 and store the result in obj2, without
 * modifying the contents of obj1. 
 * @author ClairaLyrae
 */
public abstract class Vector<T extends Vector<T>>
{
	public abstract T add(T v);

	public abstract T addInto(T v, T r);

	public abstract T conjugate();

	public abstract T conjugateInto(T r);

	public abstract float dot(T v);

	public abstract T lerp(T q, float lerp);

	public abstract T lerpInto(T q, float lerp, T r);

	public abstract float magnitude();

	public abstract float magnitudeSquared();
	
	public abstract T normalize();

	public abstract T normalizeInto(T r);

	public abstract T scale(float s);

	public abstract T scale(T v);

	public abstract T scaleInto(float s, T r);

	public abstract T scaleInto(T v, T r);

	public abstract T set(float f);

	public abstract T set(T m);

	public abstract T subtract(T v);

	public abstract T subtractInto(T v, T r);
	
	public abstract T loadFromBuffer(FloatBuffer fb);
	
	public abstract T loadFromArray(float[] f);

	public abstract FloatBuffer toBuffer(FloatBuffer fb);
	
	public FloatBuffer toBuffer() { return toBuffer(null); }
	
	public abstract float[] toArray(float[] f);
	
	public float[] toArray() { return toArray(null); }
}
