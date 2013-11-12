package com.javagameengine.math;

import java.nio.FloatBuffer;

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
		position = new Vector3f(0f); 
		scale = new Vector3f(1.0f);
		rotation = new Quaternion();
	}
	
	public Transform(Quaternion rotation)
	{
		this(new Vector3f(0f), new Vector3f(1.0f), rotation);
	}

	public Transform(Vector3f position)
	{
		this(position, new Vector3f(1.0f));
	}

	public Transform(Vector3f position, Quaternion rotation)
	{
		this(position, new Vector3f(1.0f), rotation);
	}

	public Transform(Vector3f position, Vector3f scale)
	{
		this(position, scale, new Quaternion());
	}

	public Transform(Transform world)
	{
		this();
		set(world);
	}

	public Transform(Vector3f position, Vector3f scale, Quaternion rotation)
	{
		this();
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
		scale.scale(x, y, z);
	}
	
	public void translate(float x, float y, float z)
	{
		position.add(x, y, z);
	}
	
	public void rotate(float angle, float x, float y, float z)
	{
		Quaternion rot = new Quaternion().fromAxisAngle(angle, x, y, z);
		rot.multiplyInto(rotation, rotation);
	}

	/**
	 * Inherits the transform of a given parent transform.
	 * @param parent Transform to inherit
	 * @return This Transform
	 */
	public Transform inherit(Transform parent)
	{
        scale.scale(parent.scale);
        parent.rotation.multiplyInto(rotation, rotation);
        position.scale(parent.scale);
        parent.rotation.multiplyInto(position, position);
        position.add(parent.position);
        return this;
	}

	/**
	 * Inherits the transform of a given parent transform and stores it in a given Transform.
	 * @param parent Transform to inherit
	 * @param r Transform to store result in
	 * @return This Transform
	 */
	public Transform inheritInto(Transform parent, Transform r)
	{
		if(r == null)
			r = new Transform();
        scale.scaleInto(parent.scale, r.scale);
        rotation.multiplyInto(parent.rotation, r.rotation);
        position.scaleInto(parent.scale, r.scale);
        parent.rotation.multiplyInto(position, r.position);
        r.position.add(parent.position);
        return r;
	}
	
	public Vector3f transform(Vector3f v)
	{
		return transformInto(v, null);
	}
	
	public Vector3f transformInto(Vector3f v, Vector3f r)
	{
		if(r == null)
			r = new Vector3f();
	    return rotation.multiplyInto(r.set(v).scale(scale), r).add(position);
	}
	
	public void setPosition(Vector3f p)
	{
		this.position.x = p.x;
		this.position.y = p.y;
		this.position.z = p.z;
	}

	public void setRotation(Quaternion q)
	{
		this.rotation.w = q.w;
		this.rotation.x = q.x;
		this.rotation.y = q.y;
		this.rotation.z = q.z;
	}

	public void setScale(Vector3f s)
	{
		this.scale.x = s.x;
		this.scale.y = s.y;
		this.scale.z = s.z;
	}

	public void set(Transform world)
	{
		setPosition(world.position);
		setScale(world.scale);
		setRotation(world.rotation);
	}
	
	public FloatBuffer toFloatBuffer()
	{
		// TODO 
		return null;
	}
	
	public String toString()
	{
		return String.format("[Position=%s, Scale=%s, Rotation=%s]", position.toString(), scale.toString(), rotation.toString());
	}
}
