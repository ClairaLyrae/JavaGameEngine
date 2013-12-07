package com.javagameengine.scene;

import java.util.HashSet;
import java.util.Set;

import com.javagameengine.assets.AssetManager;
import com.javagameengine.console.Console;
import com.javagameengine.events.EventManager;
import com.javagameengine.renderer.Renderer;
import com.javagameengine.scene.component.Camera;
import com.javagameengine.scene.component.PhysicsComponent;

/**
 * A Scene object describes a particular state of the game world, and manages a scene graph comprised of Node objects.
 * A single reference to a root node allows for access to the scene graph structure.
 * <p>
 * The scene object delegates game loop calls to the scene graph structure.
 * @author ClairaLyrae
 */
public class Scene
{
	private String name;
	private Node root;
	private EventManager eventManager = new EventManager();
	private Camera camera = null;
	
	public Scene(String name)
	{
		this.name = name;
		root = new Node("root");
		root.scene = this;
	}
	
	public Camera getCamera()
	{
		return camera;
	}
	
	public void setCamera(Camera c)
	{
		camera = c;
	}	
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String newname)
	{
		boolean isAsset = AssetManager.getScene(name) != null;
		if(isAsset)
		{
			if(AssetManager.getScene(newname) != null)
				throw new IllegalStateException("Cannot rename scene when asset pool already contains the given name.");
			AssetManager.removeScene(name);
			name = newname;
			AssetManager.addScene(this);
		}
		else
			name = newname;
	}
	
	/**
	 * Destroys the entire scene graph tree and resets the root node.
	 */
	public void clear()
	{
		if(root.getScene() != null)
			root.destroy();
		root = new Node("root");
		root.scene = this;
	}

	public EventManager getEventManager()
	{
		return eventManager;
	}
	
	public Node getRoot()
	{
		return root;
	}

	public void queueRender()
	{
		Renderer.camera = camera;
		root.queueRender();
	}
	
	public void update(float deltaf)
	{
		if(root == null)
			return;
		root.update(deltaf);
		PhysicsComponent.calculateCollisions();
	}
}
