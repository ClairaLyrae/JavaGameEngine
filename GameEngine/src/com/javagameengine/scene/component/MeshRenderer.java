package com.javagameengine.scene.component;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex3f;
import static org.lwjgl.opengl.GL11.*;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.javagameengine.assets.material.Material;
import com.javagameengine.assets.material.Texture;
import com.javagameengine.assets.mesh.Mesh;
import com.javagameengine.console.Console;
import com.javagameengine.events.EventMethod;
import com.javagameengine.events.KeyEvent;
import com.javagameengine.events.Listener;
import com.javagameengine.events.MouseScrollEvent;
import com.javagameengine.math.FastMath;
import com.javagameengine.math.Transform;
import com.javagameengine.renderer.RenderOperation;
import com.javagameengine.renderer.RenderState;
import com.javagameengine.renderer.Renderable;
import com.javagameengine.scene.Bounded;
import com.javagameengine.scene.Bounds;
import com.javagameengine.scene.Component;
import com.javagameengine.scene.Node;

public class MeshRenderer extends Component implements Renderable, Listener, Bounded
{
	private Mesh mesh = null;
	private Material material = null;
	
	public MeshRenderer()
	{
	}
	
	public void setMesh(Mesh m)
	{
		this.mesh = m;
	}
	
	public void setMaterial(Material m)
	{
		this.material = m;
	}
	
	@Override
	public void draw()
	{
		// Draw the mesh
		if(mesh != null)
			mesh.draw();
	}

	
	@Override
	public int bind()
	{
		// TEMP This is for loading materials before we have render sorting
		if(material != null)
		{
			material.bind();
			return material.getId();
		}
		return -1;
	}
	
	public Bounds getBounds()
	{
		if(mesh == null)
			return Bounds.getVoid();
		return mesh.getBounds();
	}
	
	public void onDestroy()
	{
	}

	public void onCreate()
	{
	}

	public void onUpdate(int delta)
	{
	}
}
