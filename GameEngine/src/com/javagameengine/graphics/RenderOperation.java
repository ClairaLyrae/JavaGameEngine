package com.javagameengine.graphics;

import java.lang.reflect.Method;

import com.javagameengine.Graphics;
import com.javagameengine.scene.Bounds;

/**
 * @author ClairaLyrae
 * 
 */
public class RenderOperation implements Comparable<RenderOperation>
{
	private RenderState state;
	private Graphics object;
	private Bounds bounds;
	
	public RenderOperation(RenderState rs, Graphics g, Bounds b)
	{
		this.state = rs;
		this.object = g;
		this.bounds = b;
	}
	
	public RenderOperation(RenderState rs, Graphics g)
	{
		this.state = rs;
		this.object = g;
		this.bounds = Bounds.getVoid();
	}
	
	public void render()
	{
		object.graphics();
	}
	
	public Bounds getBounds()
	{
		return bounds;
	}
	
	public RenderState getRenderState()
	{
		return state;
	}
	
	public Graphics getGraphicsObject()
	{
		return object;
	}

	public int compareTo(RenderOperation ro)
	{
		return 0;
	}
}
