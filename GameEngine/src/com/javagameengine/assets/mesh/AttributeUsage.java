package com.javagameengine.assets.mesh;

import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_STREAM_DRAW;

// Hints to GPU what the mesh is used for (guides GPU to place it in the optimal spot)
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