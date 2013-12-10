package com.javagameengine.renderer;

import com.javagameengine.math.Matrix4f;

/**
 * Describes an object that can be rendered into openGL or affects the rendering process.
 * @author ClairaLyrae
 */
public interface Renderable
{
	public Bindable getBindable();
	public Drawable getDrawable();
	public Matrix4f getMatrix();
	public int getLayer();
	public boolean isTransparent();
	public boolean onRender();
	public RendererState getRendererState();
}
