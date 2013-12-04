package com.javagameengine.gui;

import java.util.ArrayList;

import com.javagameengine.math.Color4f;

import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_LINE;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL11.glVertex2d;
import static org.lwjgl.opengl.GL11.glVertex2f;
import static org.lwjgl.opengl.GL11.glVertex2i;
import static org.lwjgl.opengl.GL11.glVertex3f;

import org.lwjgl.opengl.GL11;

public abstract class GUIcomponent {
	
	protected int width;
	protected int height;
	protected int xPos;
	protected int yPos;
	protected Color4f borderColor;
	protected Color4f backgroundColor;
	protected GUIcomponent parent;
	protected ArrayList<GUIcomponent> children;
	protected boolean visible = true;

	public GUIcomponent()
	{
		children = new ArrayList<GUIcomponent>();
	}
	
	public GUIcomponent(int w, int h, int x, int y, 
			Color4f borC, Color4f bgC, GUIcomponent p)
	{
		width = w;
		height = h;
		xPos = x;
		yPos = y;
		borderColor = borC;
		backgroundColor = bgC;
		parent = p;
		children = new ArrayList<GUIcomponent>();
	}
	
	public void addChild(int w, int h, int x, int y, 
			Color4f borC, Color4f bgC, GUIcomponent newChild)
	{
		newChild.parent = this;
		this.children.add(newChild);
	}

	public void draw()
	{
		int parentX; 
		int parentY;
		
		if(parent == null)
		{
			parentX = 0;
			parentY = 0;
		}
		else
		{
			parentX = parent.xPos;
			parentY = parent.yPos;
		}
		
		if(!visible)
			return;
		
		// draw current component
		glBegin(GL_QUADS);
		glColor4f(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
		
		glVertex2f(parentX + xPos, parentY + yPos); // bottom left
		glVertex2f(parentX + xPos, parentY + yPos + height); // top left
		glVertex2f(parentX + xPos + width, parentY + yPos + height); // top right
		glVertex2f(parentX + xPos + width, parentY + yPos); // bottom right

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
	
	public abstract void onUpdate(int delta);
	public abstract void onDestroy();
	public abstract void onCreate();
}


