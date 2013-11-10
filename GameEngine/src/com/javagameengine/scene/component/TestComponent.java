package com.javagameengine.scene.component;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex3f;

import org.lwjgl.input.Keyboard;

import com.javagameengine.Graphics;
import com.javagameengine.events.EventMethod;
import com.javagameengine.events.KeyEvent;
import com.javagameengine.events.Listener;
import com.javagameengine.events.MouseScrollEvent;
import com.javagameengine.scene.Bounded;
import com.javagameengine.scene.Bounds;
import com.javagameengine.scene.Component;

// This is just an example component to have something to play with and provide a rough guide for what you can do. 

public class TestComponent extends Component implements Graphics, Listener, Bounded
{
	private Bounds bounds = new Bounds();
	public int index = 0;
	public boolean selected = false;
	
	public TestComponent()
	{
		this.bounds = new Bounds(1f, 1f, 1f);
	}
	
	public TestComponent(Bounds b)
	{
		this.bounds = b;
	}
	
	@EventMethod
	public void onKeyEvent(KeyEvent e)
	{
		if(e.state() && index + '0' == e.getChar())
		{
			selected = !selected;
			System.out.println("Component " + index + " is " + selected);
		}
	}
	
	@EventMethod
	public void onMouseScroll(MouseScrollEvent e)
	{
		if(!selected)
			return;
		float s = (float)(e.getAmount());
		if(Keyboard.isKeyDown(Keyboard.KEY_S))
		{
			if(s < 0)
				s = -1.0f/s;
			node.getTransform().scale(s);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_X))
		{
			node.getTransform().translate(s, 0f, 0f);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_Y))
		{
			node.getTransform().translate(0f, s, 0f);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_Z))
		{
			node.getTransform().translate(0f, 0f, s);
		}
	}
	
	public Bounds getBox()
	{
		return bounds;
	}

	@Override
	public void graphics()
	{
		glTranslatef(0.0f, 0.0f, -5.0f); // Move Into The Screen
		glBegin(GL_QUADS); // Start Drawing 
		glColor3f(1.0f, 0.0f, 0.0f); 
		glVertex3f(bounds.minX, bounds.minY, bounds.minZ); 
		glVertex3f(bounds.minX, bounds.maxY, bounds.minZ); 
		glVertex3f(bounds.maxX, bounds.maxY, bounds.minZ); 
		glVertex3f(bounds.maxX, bounds.minY, bounds.minZ); 
		glColor3f(1.0f, 1.0f, 0.0f); 
		glVertex3f(bounds.minX, bounds.minY, bounds.minZ); 
		glVertex3f(bounds.minX, bounds.minY, bounds.maxZ); 
		glVertex3f(bounds.maxX, bounds.minY, bounds.maxZ); 
		glVertex3f(bounds.minX, bounds.minY, bounds.maxZ); 
		glColor3f(1.0f, 0.0f, 1.0f); 
		glVertex3f(bounds.minX, bounds.minY, bounds.minZ); 
		glVertex3f(bounds.minX, bounds.minY, bounds.maxZ); 
		glVertex3f(bounds.minX, bounds.maxY, bounds.maxZ); 
		glVertex3f(bounds.minX, bounds.maxY, bounds.minZ);		
		glColor3f(0.0f, 0.0f, 1.0f); 
		glVertex3f(bounds.maxX, bounds.maxY, bounds.maxZ); 
		glVertex3f(bounds.maxX, bounds.minY, bounds.maxZ); 
		glVertex3f(bounds.minX, bounds.minY, bounds.maxZ); 
		glVertex3f(bounds.minX, bounds.maxY, bounds.maxZ); 
		glColor3f(0.0f, 1.0f, 0.0f); 
		glVertex3f(bounds.maxX, bounds.maxY, bounds.maxZ); 
		glVertex3f(bounds.maxX, bounds.maxY, bounds.minZ); 
		glVertex3f(bounds.minX, bounds.maxY, bounds.minZ); 
		glVertex3f(bounds.maxX, bounds.maxY, bounds.minZ); 
		glColor3f(0.0f, 1.0f, 1.0f);
		glVertex3f(bounds.maxX, bounds.maxY, bounds.maxZ); 
		glVertex3f(bounds.maxX, bounds.maxY, bounds.minZ); 
		glVertex3f(bounds.maxX, bounds.minY, bounds.minZ); 
		glVertex3f(bounds.maxX, bounds.minY, bounds.maxZ); 
		glEnd();
	}

	public Bounds getBounds()
	{
		return bounds;
	}
}
