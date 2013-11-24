package com.javagameengine.scene;

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
	private int layerID = 0;
	
	public void setLayer(int layer)
	{
		layerID = layer;
	}
	
	public int getLayer()
	{
		return layerID;
	}
	
	public Component()
	{
		
	}
	
	public void destroy()
	{
		onDestroy();
		if(node != null)
			node.removeComponent(this);
		node = null;
	}
	
	public abstract void onUpdate(int delta);
	public abstract void onDestroy();
	public abstract void onCreate();
	
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
		if(node == null)
			return false;
		return node.scene != null;
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
		if(node == null)
			return null;
		return node.getScene();
	}	
	
	public String toString()
	{
		return String.format("Component[class=%s, hasNode=%b, hasScene=%b]", getClass().getSimpleName(), node != null, getScene() != null);
	}
}
