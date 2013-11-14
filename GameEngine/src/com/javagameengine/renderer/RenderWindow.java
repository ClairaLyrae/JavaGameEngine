package com.javagameengine.renderer;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

//TODO Need to plan out this class and the render system

/**
 * @author ClairaLyrae
 * Screen is a RenderTarget that represents writing to the LWJGL Display.
 */
public class RenderWindow extends RenderTarget
{	
	public void initialize()
	{
	}
	
	public void begin()
	{
		
	}
	
	public void finish()
	{
		Display.update();
	}
}
