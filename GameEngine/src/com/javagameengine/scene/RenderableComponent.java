package com.javagameengine.scene;

import com.javagameengine.math.Matrix4f;
import com.javagameengine.renderer.Renderable;

public abstract class RenderableComponent extends Component implements Renderable, Bounded
{
	public int layer = 0;
	
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
}
