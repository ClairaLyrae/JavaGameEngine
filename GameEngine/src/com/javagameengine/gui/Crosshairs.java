package com.javagameengine.gui;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.javagameengine.events.EventMethod;
import com.javagameengine.events.Listener;
import com.javagameengine.events.MouseClickEvent;
import com.javagameengine.math.Color4f;
import com.javagameengine.util.SimpleText;

public class Crosshairs extends GUIcomponent implements Listener{
	
	int x, y, x2, y2;
	
	

	public void draw()
	{
		float theta;
		float angle_increment = (float) Math.PI / 500;	
		width = 40;
		height = 40;
		if(!visible)
			return;
		
	
	//	glBegin(GL_QUADS);
		glColor4f(1f, 0f, 0f, 0.5f);
		
		glBegin(GL11.GL_LINES);
		
//		glLineWidth(5.0f);

		glVertex2f(x-20, y); // horizontal
		glVertex2f(x+20, y); // horizontal
		glVertex2f(x, y-20); // top right
		glVertex2f(x, y+20); // bottom right
		
		glEnd();
		
	    GL11.glBegin(GL11.GL_LINE_LOOP);
	    for(theta = 0.0f; theta < 2*Math.PI; theta += angle_increment) {
	    	x2 = (int) (width/2 * Math.cos(theta));
	    	y2 = (int) (height/2 * Math.sin(theta));
	    
	    	GL11.glVertex2f(x+x2, y+y2);
	     }
	         
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
	
	@EventMethod
	public void onMouseClick(MouseClickEvent e)
	{
		if(e.isCancelled() || e.getButton() != 0 || !e.state())
			return;
		x = e.getX();
		y = e.getY();

	}

	@Override
	public void onUpdate(float delta) {
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
