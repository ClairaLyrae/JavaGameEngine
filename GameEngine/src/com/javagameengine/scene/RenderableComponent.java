package com.javagameengine.scene;

import com.javagameengine.math.Matrix4f;
import com.javagameengine.renderer.Renderable;

public abstract class RenderableComponent extends Component implements Renderable, Bounded
{
	protected int layer = 0;
	protected boolean isVisible;
	protected boolean castsShadows;
	
	public boolean castsShadow()
	{
		return castsShadows;
	}
	
	public void setCastsShadows(boolean b)
	{
		castsShadows = true;
	}
	
	public boolean isVisible()
	{
		return isVisible;
	}
	
	public void setLayer(int layer)
	{
		this.layer = layer;
	}
	
	public void setVisible(boolean state)
	{
		isVisible = state;
	}
	
	@Override
	public Bounds getBounds()
	{
		return Bounds.getVoid();
	}
	
	@Override
	public Matrix4f getMatrix()
	{
		if(node == null)
			return null;
		return node.getWorldTransform().getTransformMatrix();
	}

	@Override
	public int getLayer()
	{
		return layer;
	}

	@Override
	public boolean onRender()
	{
		return true;
	}
}
