package com.simple3d.scene.component;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex3f;

import org.lwjgl.input.Keyboard;

import com.simple3d.events.EventMethod;
import com.simple3d.events.KeyEvent;
import com.simple3d.events.Listener;
import com.simple3d.events.MouseScrollEvent;
import com.simple3d.math.geometry.Box;


public class TestComponent extends Geometry implements Listener
{
	private Box box = new Box();
	public int index = 0;
	public boolean selected = false;
	
	public TestComponent()
	{
		this.box = new Box(1f, 1f, 1f);
	}
	
	public TestComponent(Box b)
	{
		this.box = b;
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
		float s = (float)(e.getAmount())/100.0f;
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
	
	public Box getBox()
	{
		return box;
	}

	@Override
	public void graphics()
	{
		glTranslatef(0.0f, 0.0f, -5.0f); // Move Into The Screen
		glBegin(GL_QUADS); // Start Drawing 
		glColor3f(1.0f, 0.0f, 0.0f); 
		glVertex3f(box.minX, box.minY, box.minZ); 
		glVertex3f(box.minX, box.maxY, box.minZ); 
		glVertex3f(box.maxX, box.maxY, box.minZ); 
		glVertex3f(box.maxX, box.minY, box.minZ); 
		glColor3f(1.0f, 1.0f, 0.0f); 
		glVertex3f(box.minX, box.minY, box.minZ); 
		glVertex3f(box.minX, box.minY, box.maxZ); 
		glVertex3f(box.maxX, box.minY, box.maxZ); 
		glVertex3f(box.minX, box.minY, box.maxZ); 
		glColor3f(1.0f, 0.0f, 1.0f); 
		glVertex3f(box.minX, box.minY, box.minZ); 
		glVertex3f(box.minX, box.minY, box.maxZ); 
		glVertex3f(box.minX, box.maxY, box.maxZ); 
		glVertex3f(box.minX, box.maxY, box.minZ);		
		glColor3f(0.0f, 0.0f, 1.0f); 
		glVertex3f(box.maxX, box.maxY, box.maxZ); 
		glVertex3f(box.maxX, box.minY, box.maxZ); 
		glVertex3f(box.minX, box.minY, box.maxZ); 
		glVertex3f(box.minX, box.maxY, box.maxZ); 
		glColor3f(0.0f, 1.0f, 0.0f); 
		glVertex3f(box.maxX, box.maxY, box.maxZ); 
		glVertex3f(box.maxX, box.maxY, box.minZ); 
		glVertex3f(box.minX, box.maxY, box.minZ); 
		glVertex3f(box.maxX, box.maxY, box.minZ); 
		glColor3f(0.0f, 1.0f, 1.0f);
		glVertex3f(box.maxX, box.maxY, box.maxZ); 
		glVertex3f(box.maxX, box.maxY, box.minZ); 
		glVertex3f(box.maxX, box.minY, box.minZ); 
		glVertex3f(box.maxX, box.minY, box.maxZ); 
		glEnd();
	}
}
