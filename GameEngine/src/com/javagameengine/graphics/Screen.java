package com.javagameengine.graphics;

import org.lwjgl.opengl.Display;

//TODO Need to plan out this class and the render system

/**
 * @author ClairaLyrae
 * Screen is a RenderTarget that represents writing to the LWJGL Display.
 */
public class Screen extends RenderTarget
{
	public void load()
	{
		
	}
	
	public void initialize()
	{
		
	}
	
	public void finalize()
	{
		Display.update();
	}
}
