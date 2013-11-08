package com.simple3d.scene;

import com.simple3d.Logic;

public abstract class Component implements Logic
{
	protected boolean enabled = true;
	protected boolean modified = false;
	protected Node node;
	
	public Component()
	{
		
	}
	
	protected Component(Scene scene)
	{
	}
	
	public boolean isEnabled()
	{
		return enabled;
	}
	
	public Node getNode()
	{
		return node;
	}	
}
