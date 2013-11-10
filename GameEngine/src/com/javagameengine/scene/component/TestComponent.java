package com.javagameengine.scene.component;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex3f;
import static org.lwjgl.opengl.GL11.*;

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
	private Bounds bounds = Bounds.getVoid();
	public int index = 0;
	public boolean selected = false;
	
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
		if(e.state() && index + '0' == e.getChar())
		{
			selected = !selected;
			System.out.println("Component " + index + " is " + selected);
		}
		if(e.state() && e.getKey() == Keyboard.KEY_DELETE && selected)
		{
			System.out.println("Component " + index + " has been destroyed");
			this.destroy();
			System.out.println("This is the new state of it: " + this.toString());
		}
		if(e.state() && e.getKey() == Keyboard.KEY_C && selected)
		{
			if(node != null)
			{
				System.out.println("Component " + index + " has been copied with index " + (index+1));
				node.addComponent(new TestComponent(new Bounds(bounds), index+1));
			}
		}
	}
	
	@EventMethod
	public void onMouseScroll(MouseScrollEvent e)
	{
		if(!selected)
			return;
		System.out.println("OnMouseScroll for component: " + index);
		float s = (float)(e.getAmount())*1.1f;
		if(Keyboard.isKeyDown(Keyboard.KEY_S))
		{
			if(s < 0)
				s = -1.0f/s;
			System.out.println("Scale component " + index + " by " + s + " units");
			bounds.scale(s);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_X))
		{
			bounds.translate(s, 0f, 0f);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_Y))
		{
			bounds.translate(0f, s, 0f);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_Z))
		{
			bounds.translate(0f, 0f, s);
		}
	}
	
	public Bounds getBox()
	{
		return bounds;
	}

	@Override
	public void graphics()
	{
		if(selected)
			glColor3f(1f, 1f, 1f); 
		else
			glColor3f(0.5f, 0.5f, 0.5f); 
		bounds.graphics();
	}

	public Bounds getBounds()
	{
		return bounds;
	}
	
	public void onDestroy()
	{
		System.out.println("Destroying TestComponent!");
		if(getScene() != null)
		{
			System.out.println("Unregistering its listener!");
			getScene().getEventManager().unregisterListener(this);
		}
	}

	public void onCreate()
	{
		System.out.println("Creating TestComponent!");
		if(getScene() != null)
			getScene().getEventManager().registerListener(this);
	}
}
