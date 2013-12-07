package com.javagameengine.scene;

import java.util.HashSet;
import java.util.Set;

import com.javagameengine.console.Console;
import com.javagameengine.events.EventManager;
import com.javagameengine.gui.GUI;
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
	private GUI gui;
	
	public Camera getCamera()
	{
		return camera;
	}
	
	public void resetCamera()
	{
		camera = null;
	}
	
	public void setCamera(Camera c)
	{
		if(camera != null)
			throw new IllegalStateException("Camera is already mounted.");
		camera = c;
	}	
	
	public Scene(String name)
	{
		this.name = name;
		root = new Node("root");
		root.scene = this;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
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
		root.queueRenderables();
	}
	
	public void update(int delta)
	{
		if(root == null)
			return;
		root.logic(delta);
		PhysicsComponent.calculateCollisions();
	}
	
	public void addGUI(GUI newGUI) {
		gui = newGUI;
//		gui.addScene(this);
	}

	public GUI getGui() {
		
		return gui;
	}
}
