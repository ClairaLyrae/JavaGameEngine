package com.simple3d.scene.component;

import org.lwjgl.input.Keyboard;

import com.simple3d.events.EventMethod;
import com.simple3d.events.Listener;
import com.simple3d.events.MouseMoveEvent;
import com.simple3d.events.MouseScrollEvent;
import com.simple3d.math.geometry.Box;
import com.simple3d.math.geometry.Sphere;
import static org.lwjgl.opengl.GL11.*;


public class GBox extends Geometry implements Listener
{
	private Box box = new Box();

	public GBox()
	{
		this.box = new Box(1f, 1f, 1f);
	}
	
	public GBox(Box b)
	{
		this.box = b;
	}
	
	@EventMethod
	public void onMouseScroll(MouseScrollEvent e)
	{
		float s = (float)(e.getAmount())/100.0f;
		if(Keyboard.isKeyDown(Keyboard.KEY_S))
		{
			if(s < 0)
				s = -1.0f/s;
			box.scale(s);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_X))
		{
			box.translate(s, 0f, 0f);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_Y))
		{
			box.translate(0f, s, 0f);
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_Z))
		{
			box.translate(0f, 0f, s);
		}
	}
	
	public Box getBox()
	{
		return box;
	}
	
	@Override
	public void logic(int delta)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void graphics()
	{
		glTranslatef(-1.5f, 0.0f, -6.0f); // Move Left And Into The Screen
		glBegin(GL_QUADS); // Start Drawing The Pyramid
		glColor3f(1.0f, 0.0f, 0.0f); // Red
		glVertex3f(box.minX, box.minY, box.minZ); 
		glVertex3f(box.minX, box.maxY, box.minZ); 
		glVertex3f(box.maxX, box.maxY, box.minZ); 
		glVertex3f(box.maxX, box.minY, box.minZ); 
		glColor3f(1.0f, 1.0f, 0.0f); // Red
		glVertex3f(box.minX, box.minY, box.minZ); 
		glVertex3f(box.minX, box.minY, box.maxZ); 
		glVertex3f(box.maxX, box.minY, box.maxZ); 
		glVertex3f(box.minX, box.minY, box.maxZ); 
		glColor3f(1.0f, 0.0f, 1.0f); // Red
		glVertex3f(box.minX, box.minY, box.minZ); 
		glVertex3f(box.minX, box.minY, box.maxZ); 
		glVertex3f(box.minX, box.maxY, box.maxZ); 
		glVertex3f(box.minX, box.maxY, box.minZ);		
		glColor3f(0.0f, 0.0f, 1.0f); // Red
		glVertex3f(box.maxX, box.maxY, box.maxZ); 
		glVertex3f(box.maxX, box.minY, box.maxZ); 
		glVertex3f(box.minX, box.minY, box.maxZ); 
		glVertex3f(box.minX, box.maxY, box.maxZ); 
		glColor3f(0.0f, 1.0f, 0.0f); // Red
		glVertex3f(box.maxX, box.maxY, box.maxZ); 
		glVertex3f(box.maxX, box.maxY, box.minZ); 
		glVertex3f(box.minX, box.maxY, box.minZ); 
		glVertex3f(box.maxX, box.maxY, box.minZ); 
		glColor3f(0.0f, 1.0f, 1.0f); // Red
		glVertex3f(box.maxX, box.maxY, box.maxZ); 
		glVertex3f(box.maxX, box.maxY, box.minZ); 
		glVertex3f(box.maxX, box.minY, box.minZ); 
		glVertex3f(box.maxX, box.minY, box.maxZ); 
		glEnd();
	}
}
