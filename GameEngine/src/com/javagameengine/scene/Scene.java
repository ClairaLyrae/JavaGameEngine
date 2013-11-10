package com.javagameengine.scene;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.javagameengine.Graphics;
import com.javagameengine.Logic;
import com.javagameengine.Physics;
import com.javagameengine.events.EventManager;
import com.javagameengine.graphics.RenderTarget;
import com.javagameengine.graphics.ViewState;

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
	private List<ViewState> views = new ArrayList<ViewState>();
	private EventManager eventManager = new EventManager();
	
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
		views.clear();
	}

	public EventManager getEventManager()
	{
		return eventManager;
	}
	
	public Node getRoot()
	{
		return root;
	}
	
	public void renderTo(RenderTarget rt)
	{
		// Setup anything that needs to be set up as far as the RenderTarget...
		
		// Clear screen and reset model view matrix
		glMatrixMode(GL11.GL_MODELVIEW);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		// Thats it, if the scene is empty
		if(root == null)
			return;
		
		// If no views are available, use default view state
		if(views.isEmpty())
		{
			ViewState.loadDefault();
			root.graphics();
		}
		// Otherwise, we need to run through each view state in turn and do a render pass
		else
		{
			for(ViewState v : views)
			{
				v.load();
				root.graphics();
			}
		}
		
		
	}

	public void logic(int delta)
	{
		if(root == null)
			return;
		root.logic(delta);
	}
	
	public void physics(int delta)
	{
		if(root == null)
			return;
		root.physics(delta);
	}
	
	// For debugging scenes
	public void print()
	{
		System.out.println("Scene: " + getName());
		print(root, " ");
	}

	// For debugging scenes
	private void print(Node n, String spacer)
	{
		StringBuilder sb = new StringBuilder(spacer).append(spacer);
		spacer = sb.toString();
		System.out.println(spacer + "N: " + n.toString());
		for(Component c : n.getComponents())
			System.out.println(spacer + "|-> C: " + c.toString());
		for(Node node : n.getChildren())
			print(node, spacer);
		
	}
}
