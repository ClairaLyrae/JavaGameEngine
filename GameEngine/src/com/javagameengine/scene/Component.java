package com.javagameengine.scene;

import com.javagameengine.Game;

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
	protected boolean isEnabled = true;
	protected Node node;
	protected Scene scene;
	
	private boolean isDestroyed = false;
	
	/**
	 * @return True if component was destroyed
	 */
	public boolean isDestroyed()
	{
		return isDestroyed;
	}
	
	/**
	 * Removes this component from the parent node, if attached. If the component has
	 * no parent node and is unlinked, this method does nothing.
	 */
	public void destroy()
	{
		if(node != null)
			node.removeComponent(this);
		isDestroyed = true;
	}
	
	/**
	 * Update this component for the current frame
	 * @param deltaf Time since last frame (seconds)
	 */
	public void onUpdate(float deltaf) {}
	
	/**
	 * Called when this component is unlinked from its parent node, and that parent node
	 * is linked to a scene
	 */
	public void onUnlink() {}
	
	/**
	 * Called when this component is linked to a parent node, and that parent node
	 * is linked to a scene
	 */
	public void onLink() {}
	
	/**
	 * Called when this component is either added to a node which is part of an active
	 * scene, or when the scene this node is part of is loaded
	 */
	public void onActivate() {}

	/**
	 * Called when this component is either removed from a node which is part of an active
	 * scene, or when the scene this node is part of is unloaded
	 */
	public void onDeactivate() {}
	
	/**
	 * Enables or disables updating of this component.
	 * @param bool	True for enabled
	 */
	public void setEnabled(boolean bool)
	{
		isEnabled = bool;
	}
	
	/**
	 * Determines if component is allowing updates.
	 * @return True for enabled
	 */
	public boolean isEnabled()
	{
		return isEnabled;
	}
	
	/**
	 * Determines if this component is connected to a scene graph.
	 * @return True if component is linked to a scene graph.
	 */
	public boolean isLinked()
	{
		return scene != null;
	}
	
	/**
	 * Determines if this component is connected to a scene graph.
	 * @return True if component is linked to a scene graph.
	 */
	public boolean isActive()
	{
		if(scene == null)
			return false;
		return scene == Game.getHandle().getActiveScene();
	}

	/**
	 * Fetches the parent node of this component if available
	 * @return Parent node of this component (null if unlinked)
	 */
	public Node getNode()
	{
		return node;
	}	
	
	/**
	 * Fetches the scene this component is in if available
	 * @return Scene containing this component (null if unlinked)
	 */
	public Scene getScene()
	{
		return scene;
	}	
	
	public String toString()
	{
		return String.format("Component[class=%s, hasNode=%b, hasScene=%b]", getClass().getSimpleName(), node != null, getScene() != null);
	}
}
