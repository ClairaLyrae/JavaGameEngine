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
	protected Node root = new Node("scene");
	protected List<ViewState> views = new ArrayList<ViewState>();
	
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

}
