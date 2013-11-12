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

import com.javagameengine.Renderable;
import com.javagameengine.console.Console;
import com.javagameengine.events.EventMethod;
import com.javagameengine.events.KeyEvent;
import com.javagameengine.events.Listener;
import com.javagameengine.events.MouseScrollEvent;
import com.javagameengine.graphics.RenderOperation;
import com.javagameengine.graphics.RenderState;
import com.javagameengine.math.FastMath;
import com.javagameengine.math.Transform;
import com.javagameengine.scene.Bounded;
import com.javagameengine.scene.Bounds;
import com.javagameengine.scene.Component;
import com.javagameengine.scene.Node;

// This is just an example component to have something to play with and provide a rough guide for what you can do. 

public class TestComponent extends Component implements Renderable, Listener, Bounded
{
	private Bounds bounds = Bounds.getVoid();
	public int index = 0;
	public boolean selected = false;
	public boolean enableRotation = false;
	public boolean solid = false;
	
	public float angle = 0.0f;
	Transform t = new Transform();
	
	public TestComponent()
	{
	}
	
	public TestComponent(Bounds b, int index)
	{
		this.bounds = b;
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
				newnode.addComponent(new TestComponent(new Bounds(bounds), index+1));
			}
		}
	}
	
	@EventMethod
	public void onMouseScroll(MouseScrollEvent e)
	{
		if(!selected || node == null)
			return;
		float s = (float)(e.getAmount());
		if(Keyboard.isKeyDown(Keyboard.KEY_S))
		{
			float scaling = 1f;
			if(s >= 0)
				scaling = (2f*s);
			else
				scaling = (-0.5f*s);
			Console.println("Scale component " + index + " by " + s + " units");
			node.getTransform().scale(scaling);
		}
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
	
	public Bounds getBox()
	{
		return bounds;
	}

	public void drawGeo()
	{
		glEnable(GL_NORMALIZE);
		glBegin(GL_QUADS); // Start Drawing 
		glNormal3f(0f, 0f, 1f); 
		glVertex3f(bounds.minX, bounds.maxY, bounds.maxZ);
		glVertex3f(bounds.minX, bounds.minY, bounds.maxZ);
		glVertex3f(bounds.maxX, bounds.minY, bounds.maxZ); 
		glVertex3f(bounds.maxX, bounds.maxY, bounds.maxZ); 

		glNormal3f(0f, 0f, -1f);
		glVertex3f(bounds.maxX, bounds.maxY, bounds.minZ);  
		glVertex3f(bounds.maxX, bounds.minY, bounds.minZ); 
		glVertex3f(bounds.minX, bounds.minY, bounds.minZ); 
		glVertex3f(bounds.minX, bounds.maxY, bounds.minZ);
		
		glNormal3f(0f, 1f, 0f);
		glVertex3f(bounds.maxX, bounds.maxY, bounds.minZ); 
		glVertex3f(bounds.minX, bounds.maxY, bounds.minZ);
		glVertex3f(bounds.minX, bounds.maxY, bounds.maxZ); 
		glVertex3f(bounds.maxX, bounds.maxY, bounds.maxZ); 

		glNormal3f(0f, -1f, 0f);
		glVertex3f(bounds.maxX, bounds.minY, bounds.maxZ); 
		glVertex3f(bounds.minX, bounds.minY, bounds.maxZ); 
		glVertex3f(bounds.minX, bounds.minY, bounds.minZ); 
		glVertex3f(bounds.maxX, bounds.minY, bounds.minZ); 

		glNormal3f(1f, 0f, 0f);
		glVertex3f(bounds.maxX, bounds.maxY, bounds.maxZ);
		glVertex3f(bounds.maxX, bounds.minY, bounds.maxZ); 
		glVertex3f(bounds.maxX, bounds.minY, bounds.minZ);  
		glVertex3f(bounds.maxX, bounds.maxY, bounds.minZ); 

		glNormal3f(-1f, 0f, 0f);
		glVertex3f(bounds.minX, bounds.maxY, bounds.minZ);
		glVertex3f(bounds.minX, bounds.minY, bounds.minZ);  
		glVertex3f(bounds.minX, bounds.minY, bounds.maxZ);
		glVertex3f(bounds.minX, bounds.maxY, bounds.maxZ); 
		glEnd();
		glDisable(GL_NORMALIZE);
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
			glLineWidth( 3.0f );
			if(selected)
				glColor3f(0f, 1f, 0f); 
			else
				glColor3f(0.0f, 0.5f, 0.0f);
			drawGeo();

		    // we enable lighting right before rendering
		    GL11.glEnable(GL11.GL_LIGHTING);
		    GL11.glEnable(GL11.GL_LIGHT0);   

			glShadeModel(GL_FLAT);
			glPolygonMode( GL_FRONT_AND_BACK, GL_FILL );
			glColor3f( 0.2f, 0.2f, 0.2f );
			drawGeo();
			glPopAttrib();

	        GL11.glDisable(GL11.GL_LIGHT0);
	        GL11.glDisable(GL11.GL_LIGHTING);
		}
		else
		{
			glPushAttrib( GL_ALL_ATTRIB_BITS );
			glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
			if(selected)
				glColor3f(0f, 1f, 0f); 
			else
				glColor3f(0.0f, 0.5f, 0.0f); 
			drawGeo();
			glPopAttrib();
		}
	}

	public Bounds getBounds()
	{
		return bounds;
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

	@Override
	public RenderState getRenderState()
	{
		return new RenderState();
	}

	@Override
	public Bounds getRenderBounds()
	{
		return bounds;
	}

	public void onUpdate(int delta)
	{
		if(node != null && enableRotation)
		{
			node.getTransform().rotate((FastMath.PI/10f)/((float)delta), 0f, 1f, 0f);
		}
	}
}
