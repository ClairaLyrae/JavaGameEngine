package com.javagameengine.renderer;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glMatrixMode;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.EnumSet;

import org.lwjgl.opengl.GL11;

/**
 * @author ClairaLyrae
 * Describes an context for the Renderer to draw in. Includes initialization of a given <code>RenderTarget</code>, 
 * setting up the viewport properties, and performing the initial inverse camera transformation. 
 */
public abstract class RenderContext
{
	protected EnumSet<RenderBucket> allowedBuckets = EnumSet.noneOf(RenderBucket.class);
	
	/**
	 * Retrieves the default RenderContext.
	 */
	public static RenderContext getDefault()
	{
		return new RenderContext() {
			public void load()
			{
				
			}
		};
	}
	
	/**
	 * Load the viewport state into openGL.
	 */
	public abstract void load();
}
