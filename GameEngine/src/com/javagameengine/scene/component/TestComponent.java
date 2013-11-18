package com.javagameengine.scene.component;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex3f;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glUseProgram;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.javagameengine.assets.material.Texture;
import com.javagameengine.assets.mesh.Mesh;
import com.javagameengine.assets.mesh.MeshUtil;
import com.javagameengine.assets.mesh.VertexBuffer;
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
import com.javagameengine.renderer.Renderer;
import com.javagameengine.scene.Bounded;
import com.javagameengine.scene.Bounds;
import com.javagameengine.scene.Component;
import com.javagameengine.scene.Node;

// This is just an example component to have something to play with and provide a rough guide for what you can do. 

public class TestComponent extends Component implements Renderable, Listener, Bounded
{
	private Mesh mesh = null;
	public int index = 0;
	public boolean selected = false;
	public boolean enableRotation = false;
	public boolean solid = false;
	public boolean smooth = false;
	
	public float angle = 0.0f;
	Transform t = new Transform();
	
	public TestComponent(int index)
	{
		this.index = index;
	}
	
	public TestComponent(Mesh m, int index)
	{
		setMesh(m);
		this.index = index;
	}
	
	@EventMethod
	public void onKeyEvent(KeyEvent e)
	{
		if(e.isCancelled())
			return;
		if(e.state() && selected && e.getKey() == Keyboard.KEY_F)
		{
			solid = !solid;
			if(solid)
				Console.println("TestComponent " + index + " is now solid!");
			else
				Console.println("TestComponent " + index + " is now wireframe!");
		}
		if(e.state() && selected && e.getKey() == Keyboard.KEY_M)
		{
			smooth = !smooth;
			if(smooth)
				Console.println("TestComponent " + index + " is now smooth!");
			else
				Console.println("TestComponent " + index + " is now flat!");
		}
		if(e.state() && selected && e.getKey() == Keyboard.KEY_R)
		{
			enableRotation = !enableRotation;
			Console.println("TestComponent " + index + " rotation is now " + enableRotation);
		}
		if(e.state() && index + '0' == e.getChar())
		{
			selected = !selected;
			if(selected)
				Console.println("TestComponent " + index + " was selected");
			else
				Console.println("TestComponent " + index + " was deselected.");
		}
		if(e.state() && e.getKey() == Keyboard.KEY_DELETE && selected)
		{
			Console.println("TestComponent " + index + " has been destroyed");
			if(node != null)
			{
				node.destroy();
			}
			else
				this.destroy();
		}
		if(e.state() && e.getKey() == Keyboard.KEY_C && selected)
		{
			if(node != null)
			{
				Console.println("TestComponent " + index + " has been copied with index " + (index+1));
				Node newnode = new Node("Box " + (index+1));
				node.addChild(newnode);
				newnode.addComponent(new TestComponent(mesh, index+1));
			}
		}
	}
	
	@EventMethod
	public void onMouseScroll(MouseScrollEvent e)
	{
		if(!selected || node == null || e.isCancelled())
			return;
		float s = (float)(e.getAmount());
		if(Keyboard.isKeyDown(Keyboard.KEY_X))
		{
			node.getTransform().translate(s*0.1f, 0f, 0f);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_Y))
		{
			node.getTransform().translate(0f, s*0.1f, 0f);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_Z))
		{
			node.getTransform().translate(0f, 0f, s*0.1f);
		}
	}
	
	public int meshHandle = -1;
	
	public void setMesh(Mesh m)
	{
		this.mesh = m;
		m.create();
	}
	
	public Texture tex = null;
	
	public void setTexture(Texture tex)
	{
		this.tex = tex;
	}
	
	@Override
	public void draw()
	{

		if(solid)
		{ 
			glPushAttrib( GL_ALL_ATTRIB_BITS );
			glEnable( GL_POLYGON_OFFSET_FILL );
			glPolygonOffset( -2.5f, -2.5f );
			glPolygonMode( GL_FRONT_AND_BACK, GL_LINE );
			glLineWidth( 2.0f );
			if(selected)
			{
				glColor3f(0f, 1f, 0f); 
			}
			else
			{
				glColor3f(0.0f, 0.0f, 0.0f);
			}
			glDisable(GL_TEXTURE_2D);
			mesh.draw();

		    // we enable lighting right before rendering
		    GL11.glEnable(GL11.GL_LIGHTING);
		    GL11.glEnable(GL11.GL_LIGHT0);   

		    if(smooth)
		    	glShadeModel(GL_SMOOTH);
		    else
		    	glShadeModel(GL_FLAT);
			glPolygonMode( GL_FRONT_AND_BACK, GL_FILL );
			if(tex != null && tex.getId() >= 0)
			{
				glBindTexture(GL_TEXTURE_2D, tex.getId());
				glEnable(GL_TEXTURE_2D); 
			}
			mesh.draw();
	        GL11.glDisable(GL11.GL_LIGHT0);
	        GL11.glDisable(GL11.GL_LIGHTING);
			glPopAttrib();

		}
		else
		{
			glPushAttrib( GL_ALL_ATTRIB_BITS );
			glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
			if(selected)
				glColor3f(0f, 1f, 0f); 
			else
				glColor3f(1f, 1f, 1f); 
			mesh.draw();
			glPopAttrib();
		}
	}

	public Bounds getBounds()
	{
		return mesh.getBounds();
	}
	
	public void onDestroy()
	{
		if(getScene() != null)
		{
			getScene().getEventManager().unregisterListener(this);
		}
	}

	public void onCreate()
	{
		if(getScene() != null)
			getScene().getEventManager().registerListener(this);
	}

	public void onUpdate(int delta)
	{
		if(node != null && enableRotation)
		{
			node.getTransform().rotate((FastMath.PI/10f)/((float)delta), 0f, 1f, 0f);
		}
	}
}
