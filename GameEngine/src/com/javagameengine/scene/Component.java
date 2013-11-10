package com.javagameengine.scene;

import com.javagameengine.Logic;

/**
 * Generic object used to define anything that affects the scene. All 'objects' in a scene extend Component 
 * and use the framework of component to define how they are rendered, updated, manipulated, or manipulate
 * other objects during the game loop.
 * <p>
 * Components are attached to Nodes in the scene graph and contain a reference to that node. Components are therefore
 * the leaf 'nodes' of the scene graph.
 * @author ClairaLyrae
 */
public abstract class Component
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
	
	/**
	 * Enables or disables updating of this component.
	 * @param bool	True for enabled
	 */
	public void setEnabled(boolean bool)
	{
		enabled = bool;
	}
	
	/**
	 * Determines if component is allowing updates.
	 * @return True for enabled
	 */
	public boolean isEnabled()
	{
		return enabled;
	}
	
	/**
	 * Fetches the parent node of this component if available
	 * @return Parent node of this component (null if unlinked)
	 */
	public Node getNode()
	{
		return node;
	}	
}
