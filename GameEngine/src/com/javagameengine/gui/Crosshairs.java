package com.javagameengine.gui;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.javagameengine.events.Listener;
import com.javagameengine.math.Color4f;
import com.javagameengine.util.SimpleText;

public class Crosshairs extends GUIcomponent implements Listener{
	
	int x, y;

	public void draw()
	{
		
		if(!visible)
			return;
		
	
		glBegin(GL_QUADS);
		glColor4f(1f, 0f, 0f, 0.5f);
		glBegin(GL11.GL_LINE);

		glVertex2f(x-20, y); // horizontal
		glVertex2f(x+20, y); // horizontal
		glVertex2f(x, y-20); // top right
		glVertex2f(x, y-20); // bottom right
		glEnd();
			



		

		
		// draw children of current component
		drawChildren();
	}
	
	public void drawChildren()
	{
		int i;
		
		for(i=0;i<this.children.size();i++)
		{
			this.children.get(i).draw();
		}
	}
	@Override
	public void onUpdate(int delta) {
		x = Mouse.getX();
		y = Mouse.getY();
	}

	@Override
	public void onDestroy()
	{
		int i;

		for(i=0; i<children.size();i++)
		{
			children.get(i).onDestroy();
		}
		getScene().getEventManager().unregisterListener(this);
	}

	@Override
	public void onCreate()
	{
		int i;
		
		getScene().getEventManager().registerListener(this);
		
		for(i=0; i<children.size();i++)
		{
			children.get(i).onCreate();
		}
	
		getScene().getEventManager().registerListener(this);
	}

}
