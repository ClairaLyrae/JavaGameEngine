package com.javagameengine.math;

// TODO Not fully featured yet. A lot of methods this class might need are
// somewhat dependent on what we actually end up using it for, so it may
// be subject to change over time. ALSO, note that the vector/quat classes
// are not complete, so by extension this may have errors!

/**
 * Transform encapsulates two Vector3f objects and a single Quaternion object, representing translation, scale, and rotation 
 * respectively. The class provides methods for modifying the transform in useful ways and applying the transform to other
 * transforms to create total transforms.
 * @author ClairaLyrae
 */
public class Transform
{
	private Vector3f scale;
	private Vector3f position;
	private Quaternion rotation;

	public Transform()
	{
		scale = new Vector3f(1.0f);
		position = new Vector3f(0.0f);
		rotation = new Quaternion().loadIdentity();
	}
	
	public Transform(Quaternion rotation)
	{
		this(new Vector3f(0, 0, 0), new Vector3f(1.0f, 1.0f, 1.0f), rotation);
	}

	public Transform(Vector3f position)
	{
		this(position, new Vector3f(1.0f, 1.0f, 1.0f));
	}

	public Transform(Vector3f position, Quaternion rotation)
	{
		this(position, new Vector3f(1.0f, 1.0f, 1.0f), rotation);
	}

	public Transform(Vector3f position, Vector3f scale)
	{
		this(position, scale, new Quaternion());
	}

	public Transform(Vector3f position, Vector3f scale, Quaternion rotation)
	{
		this.setScale(scale);
		this.setPosition(position);
		this.setRotation(rotation);
	}

	public Vector3f getPosition()
	{
		return position;
	}

	public Quaternion getRotation()
	{
		return rotation;
	}

	public Vector3f getScale()
	{
		return scale;
	}

	public Matrix4f getScaleMatrix()
	{
		Matrix4f r = new Matrix4f();
		r.f00 = scale.x;
		r.f11 = scale.y;
		r.f22 = scale.z;
		return r;
	}

	public Matrix4f getTranslationMatrix()
	{
		Matrix4f r = new Matrix4f();
		r.f03 = position.x;
		r.f13 = position.y;
		r.f23 = position.z;
		return r;
	}

	public Matrix4f getTranslationScaleMatrix()
	{
		Matrix4f r = new Matrix4f();
		r.f00 = scale.x;
		r.f11 = scale.y;
		r.f22 = scale.z;
		r.f03 = position.x;
		r.f13 = position.y;
		r.f23 = position.z;
		return r;
	}

	public Matrix4f getTransformMatrix()
	{
		return getTranslationScaleMatrix().multiply(rotation.toRotationMatrix());
	}

	public void lerp(Transform t, float f)
	{
		rotation.lerpInto(t.getRotation(), f, rotation);	// TODO See the Quaternion.lerp method for info
		scale.lerpInto(t.getScale(), f, scale);
		position.lerpInto(t.getPosition(), f, position);
	}

	public void loadIdentity()
	{
		rotation.loadIdentity();
		position.set(0.0f);
		scale.set(1.0f);
	}

	public void scale(float x)
	{
		scale.scale(x);
	}
	
	public void scale(float x, float y, float z)
	{
		scale = scale.scale(x, y, z);
	}
	
	public void translate(float x, float y, float z)
	{
		position = position.add(x, y, z);
	}
	
	/**
	 * Transforms this transform by the given Transform t and stores the results back into this transform.
	 * @param t Transform to transform by
	 * @return This transform
	 */
	public Transform transform(Transform t)
	{
		return transformInto(t, this);
	}

	/**
	 * Transforms this transform by the given Transform t and stores the results into the given Transform r.
	 * @param t Transform to transform by
	 * @param r Transform to store result in
	 * @return The given Transform r
	 */
	public Transform transformInto(Transform t, Transform r)
	{
		if(r == null)
			r = new Transform();
		position.addInto(t.position, r.position);
		scale.scaleInto(t.scale, r.scale);
		rotation.multiplyInto(t.rotation, r.rotation);
		return r;
	}

	public void setPosition(Vector3f position)
	{
		this.position = position;
	}

	public void setRotation(Quaternion rotation)
	{
		this.rotation = rotation;
	}

	public void setScale(Vector3f scale)
	{
		this.scale = scale;
	}
}
