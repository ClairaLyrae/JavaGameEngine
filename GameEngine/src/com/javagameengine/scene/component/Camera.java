package com.javagameengine.scene.component;

import org.lwjgl.opengl.Display;

import com.javagameengine.math.FastMath;
import com.javagameengine.math.Matrix4f;
import com.javagameengine.math.Quaternion;
import com.javagameengine.renderer.Renderer;
import com.javagameengine.scene.Component;

public class Camera extends Component
{
	public enum Type{
		PERSPECTIVE,
		ORTHOGONAL;
	}
	
	private Type type;
	
	private float aspect;
	private float fovy;

	private float zNear;
	private float zFar;

	private float left;
	private float right;
	private float bottom;
	private float top;

	public void setAspectRatio(float f)
	{
		aspect = f;
	}
	
	public void setFOV(float fov)
	{
		this.fovy = fov;
	}
	
	public void setOrthoBounds(float top, float bottom, float left, float right)
	{
		this.top = top;
		this.bottom = bottom;
		this.left = left;
		this.right = right;
	}
	
	public void setDepth(float zNear, float zFar)
	{
		this.zFar = zFar;
		this.zNear = zNear;
	}
	
	public void setType(Type type)
	{
		this.type = type;
	}
	
	private int[] ignoreLayers = new int[Renderer.MAX_LAYERS];
	
	private boolean isEnabled = false;
	
	public void setEnabled(boolean state)
	{
		isEnabled = state;
	}
	
	public boolean isEnabled()
	{
		return isEnabled;
	}
	
	private boolean useDisplayBorders = true;
	
	public void useDisplayBorders(boolean b)
	{
		useDisplayBorders = b;
	}
	
	public boolean isAlignedToDisplay()
	{
		return useDisplayBorders;
	}
	
	public float getAspect()
	{
		if(useDisplayBorders)
		{
			float width = (float)Display.getWidth();
			float height = (float)Display.getHeight();
			return width/height;
		}
		return aspect;
	}
	
	public float getOrthoLeft()
	{
		if(useDisplayBorders)
			return 0;
		return left;
	}

	public float getOrthoRight()
	{
		if(useDisplayBorders)
			return (float)Display.getWidth();
		return right;
	}
	
	public float getOrthoBottom()
	{
		if(useDisplayBorders)
			return 0;
		return bottom;
	}
	
	public float getOrthoTop()
	{
		if(useDisplayBorders)
			return (float)Display.getHeight();
		return top;
	}
	
	public float getFOV()
	{
		return fovy;
	}
	
	public float getDepthNear()
	{
		return zNear;
	}

	public float getDepthFar()
	{
		return zFar;
	}
	
	public Matrix4f getViewMatrix()
	{
		if(node == null)
			return new Matrix4f();
		Matrix4f cinv = node.getWorldTransform().getTransformMatrix().inverseInto(null);
		Matrix4f.rotationMatrix(FastMath.PI, 0f, 1f, 0f).multiplyInto(cinv, cinv);
		return cinv;
	}
	
	public Matrix4f getProjectionMatrix()
	{
		if(type == Type.PERSPECTIVE)
			return Matrix4f.perspectiveMatrix(getFOV(), getAspect(), getDepthNear(), getDepthFar());
		if(type == Type.ORTHOGONAL)
			return Matrix4f.orthoMatrix(getOrthoLeft(), getOrthoRight(), getOrthoTop(), getOrthoBottom(), getDepthNear(), getDepthFar());
		return new Matrix4f();
	}
	
	@Override
	public void onUpdate(float delta)
	{
	}

	@Override
	public void onUnlink()
	{
		getScene().setCamera(null);
	}

	@Override
	public void onLink()
	{
		getScene().setCamera(this);
	}

	@Override
	public void onActivate()
	{
	}

	@Override
	public void onDeactivate()
	{
	}
}
