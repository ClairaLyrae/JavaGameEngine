package com.javagameengine.assets.mesh;

import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_STREAM_DRAW;

/**
 * AttributeUsage is an enum which has an equivalent set in OpenGL. It provides a hint to the graphics card
 * which describes how the data should be stored, in order to achieve optimal performance.
 */
public enum AttributeUsage 
{
    STATIC(GL_STATIC_DRAW),	// Mesh data is rarely changed
    DYNAMIC(GL_DYNAMIC_DRAW),	// Mesh data is updated occasionally
    STREAM(GL_STREAM_DRAW);		// Updated every frame
    
	private int gl;
	
	private AttributeUsage(int gl)
	{
		this.gl = gl;
	}
	
	public int getGLEnum()
	{
		return gl;
	}
}