package com.javagameengine.scene;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.javagameengine.console.Console;
import com.javagameengine.events.EventManager;
import com.javagameengine.math.Transform;
import com.javagameengine.renderer.Renderable;
import com.javagameengine.renderer.Renderer;

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

	public void update(int delta)
	{
		if(root == null)
			return;
		root.logic(delta);
	}
	
	// For debugging scenes
	public void print()
	{
		Console.println("Scene: " + getName());
		print(root, "");
	}

	// For debugging scenes
	private void print(Node n, String sb)
	{
		sb += "  ";
		
		Console.println(sb + "N: " + n.toString());
		
		for(Component c : n.getComponents())
			Console.println(sb + "C: " + c.toString());
		
		for(Node node : n.getChildren())
			print(node, sb);
	}
}
