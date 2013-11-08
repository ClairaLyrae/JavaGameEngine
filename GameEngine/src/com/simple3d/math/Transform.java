package com.simple3d.math;

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

	public Matrix4f getRotationMatrix()
	{
		return rotation.toRotationMatrix();
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

	public Matrix4f getTransformMatrix()
	{
		return getTranslationScaleMatrix().multiply(getRotationMatrix());
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

	public void lerp(Transform t, float f)
	{
		rotation.lerpInto(t.getRotation(), f, rotation);
		scale.lerpInto(t.getScale(), f, scale);
		position.lerpInto(t.getPosition(), f, position);
	}

	public void loadIdentity()
	{
		rotation.loadIdentity();
		position.set(0.0f);
		scale.set(1.0f);
	}

	public void scale(float x, float y, float z)
	{
		position = position.add(x, y, z);
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

	public void translate(float x, float y, float z)
	{
		position = position.add(x, y, z);
	}

}
