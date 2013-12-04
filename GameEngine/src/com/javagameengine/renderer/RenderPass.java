package com.javagameengine.renderer;

import com.javagameengine.math.Matrix4f;

public class RenderPass
{
	//private RenderTarget target;
	
	protected int priority;
	
	private float zNear;
	private float zFar;
	private float orthoTop;
	private float orthoBottom;
	private float orthoLeft;
	private float orthoRight;
	private float fovy;
	private float aspect;
	private boolean[] enabledLayers = new boolean[Renderer.MAX_LAYERS];
	
	public void setLayerState(int layer, boolean enable)
	{
		enabledLayers[layer] = enable;
	}
	
	public boolean isLayerEnabled(int layer)
	{
		return enabledLayers[layer];
	}
	
	public enum Mode {
		ORTHO,
		PERSPECTIVE;
	}
	
	public RenderPass(Mode t)
	{
		type = t;
	}
	
	private Mode type;
	
	public Matrix4f getProjectionMatrix()
	{
		if(type == Mode.PERSPECTIVE)
			return Matrix4f.perspectiveMatrix(fovy, aspect, zNear, zFar);
		else if(type == Mode.ORTHO)
			return Matrix4f.orthoMatrix(orthoLeft, orthoRight, orthoTop, orthoBottom, zNear, zFar);
		else
			return null;
	}	
}
