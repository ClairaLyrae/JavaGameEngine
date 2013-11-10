package com.javagameengine.scene.component;

import com.javagameengine.Graphics;
import com.javagameengine.graphics.RenderState;
import com.javagameengine.scene.Component;

/**
 * @author ClairaLyrae
 * Renderer components are objects which can be directly drawn by openGL and contain
 * a context in which they are drawn in.
 */
public abstract class Renderer extends Component implements Graphics
{
	private RenderState renderState = new RenderState();
	
	/**
	 * Returns a RenderState describing the rendering context of the renderable Component
	 * @return RenderState for renderable Component
	 */
	public RenderState getRenderState()
	{
		return renderState;
	}
}
