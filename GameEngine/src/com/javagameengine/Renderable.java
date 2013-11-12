package com.javagameengine;

import com.javagameengine.graphics.RenderOperation;
import com.javagameengine.graphics.RenderState;
import com.javagameengine.math.Transform;
import com.javagameengine.scene.Bounds;



/**
 * Describes an object that can be rendered into openGL or affects the rendering process.
 * @author ClairaLyrae
 */
public interface Renderable
{
	public void draw();
	
	public RenderState getRenderState();
	
	public Bounds getRenderBounds();
}
