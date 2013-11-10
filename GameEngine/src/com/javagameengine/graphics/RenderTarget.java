package com.javagameengine.graphics;

//TODO Need to plan out this class and the render system

/**
 * @author ClairaLyrae
 * Defines a target for a the engine to draw to. For example, may be a screen, texture, or file.
 */
public abstract class RenderTarget
{
	/**
	 * Sets up openGL to draw to this renderTarget
	 */
	public void load()
	{
		
	}
	
	/**
	 * Prepares this RenderTarget for a new rendering
	 */
	public void initialize()
	{
		
	}
	
	/**
	 * Finalizes a rendering to this RenderTarget 
	 */
	public void finalize()
	{
		
	}
}
